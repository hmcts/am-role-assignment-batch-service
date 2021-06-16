package uk.gov.hmcts.reform.roleassignmentbatch.config;

import java.util.UUID;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.domain.model.CcdCaseUsers;
import uk.gov.hmcts.reform.roleassignmentbatch.ActorCacheRepository;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.HistoryEntity;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.Newtable;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.RequestEntity;
import uk.gov.hmcts.reform.roleassignmentbatch.task.DeleteExpiredRecords;
import uk.gov.hmcts.reform.roleassignmentbatch.task.HistoryEntityProcessor;
import uk.gov.hmcts.reform.roleassignmentbatch.task.NewTableProcessor;
import uk.gov.hmcts.reform.roleassignmentbatch.task.RequestEntityProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Value("${delete-expired-records}")
    String taskParent;

    @Value("${batchjob-name}")
    String jobName;

    @Autowired
    JobBuilderFactory jobs;
    @Autowired
    StepBuilderFactory steps;
    @Autowired
    ActorCacheRepository actorCacheRepository;
    @Autowired
    DataSource dataSource;

    public final String REQUEST_QUERY = "INSERT INTO role_assignment_request(id, correlation_id,client_id,authenticated_user_id,assigner_id,request_type," +
                                        "status," +
                                        "process,reference," +
                                        "replace_existing,role_assignment_id,log,created)" +
                                        " VALUES (:id, :correlationId,:clientId,:authenticatedUserId,:assignerId,:requestType,:status,:process,:reference," +
                                        ":replaceExisting," +
                                        ":roleAssignmentId,:log,:created)";

    @Bean
    public Step stepOrchestration(@Autowired StepBuilderFactory steps,
                                  @Autowired DeleteExpiredRecords deleteExpiredRecords) {
        return steps.get(taskParent)
                    .tasklet(deleteExpiredRecords)
                    .build();
    }

    @Bean
    public Job runRoutesJob(@Autowired JobBuilderFactory jobs,
                            @Autowired StepBuilderFactory steps,
                            @Autowired DeleteExpiredRecords deleteExpiredRecords) {

        return jobs.get(jobName)
                   .incrementer(new RunIdIncrementer())
                   .start(stepOrchestration(steps, deleteExpiredRecords))
                   .build();
    }

    @Bean
    public FlatFileItemReader<CcdCaseUsers> ccdCaseUsersReader() {
        return new FlatFileItemReaderBuilder<CcdCaseUsers>()
            .name("historyEntityReader")
            .linesToSkip(1)
            .resource(new ClassPathResource("book2.csv"))
            .delimited()
            .names("case_data_id", "user_id", "case_role", "jurisdiction", "case_type", "role_category")
            .lineMapper(lineMapper())
            .fieldSetMapper(new BeanWrapperFieldSetMapper<CcdCaseUsers>() {{
                setTargetType(CcdCaseUsers.class);
            }})
            .build();
    }

    @Bean
    public LineMapper<CcdCaseUsers> lineMapper() {
        final DefaultLineMapper<CcdCaseUsers> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("case_data_id", "user_id", "case_role", "jurisdiction", "case_type", "role_category");
        final CcdFieldSetMapper ccdFieldSetMapper = new CcdFieldSetMapper();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(ccdFieldSetMapper);
        return defaultLineMapper;
    }

    @Component
    public class HistoryFieldSetMapper implements FieldSetMapper<HistoryEntity> {
        @Override
        public HistoryEntity mapFieldSet(FieldSet fieldSet) {
            final HistoryEntity historyEntity = new HistoryEntity();
            historyEntity.setId(UUID.fromString(fieldSet.readString("id")));
            historyEntity.setLog(fieldSet.readString("classification"));
            return historyEntity;
        }
    }

    @Component
    public class CcdFieldSetMapper implements FieldSetMapper<CcdCaseUsers> {
        @Override
        public CcdCaseUsers mapFieldSet(FieldSet fieldSet) {
            final CcdCaseUsers caseUsers = new CcdCaseUsers();
            caseUsers.setCaseDataId(fieldSet.readString("case_data_id"));
            caseUsers.setUserId(fieldSet.readString("user_id"));
            caseUsers.setCaseRole(fieldSet.readString("case_role"));
            caseUsers.setJurisdiction(fieldSet.readString("jurisdiction"));
            caseUsers.setCaseType(fieldSet.readString("case_type"));
            caseUsers.setRoleCategory(fieldSet.readString("role_category"));
            return caseUsers;
        }
    }

    @Bean
    public HistoryEntityProcessor historyEntityProcessor() {
        return new HistoryEntityProcessor();
    }

    @Bean
    public RequestEntityProcessor requestEntityProcessor() {
        return new RequestEntityProcessor();
    }

    @Bean
    public NewTableProcessor newTableProcessor() {
        return new NewTableProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<RequestEntity> insertInRequestTable() {
        return new JdbcBatchItemWriterBuilder<RequestEntity>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql(REQUEST_QUERY)
            .dataSource(dataSource)
            .build();
    }

    @Bean
    public JdbcBatchItemWriter<Newtable> insertInRequestTableNewTable() {
        return
            new JdbcBatchItemWriterBuilder<Newtable>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into nitish_table(myid,column2) values(:myid,:column2)")
                .dataSource(dataSource)
                .build();
    }

   /* @Bean
    @SuppressWarnings("rawtypes")
    public CompositeItemWriter compositeItemWriter() {
        List<ItemWriter> writers = new ArrayList<>(2);
        writers.add(insertInRequestTableNewTable());
        writers.add(insertInRequestTable());

        CompositeItemWriter itemWriter = new CompositeItemWriter();

        itemWriter.setDelegates(writers);

        return itemWriter;
    }*/

    @Bean
    public Step step1() {
        return steps.get("step1")
                    .<CcdCaseUsers, RequestEntity>chunk(10)
                    .reader(ccdCaseUsersReader())
                    .processor(requestEntityProcessor())
                    .writer(insertInRequestTable())
                    .build();
    }

    @Bean
    public Job importVoltageJob(@Autowired NotificationListener listener, Step step1) {
        return jobs.get("importVoltageJob")
                   .incrementer(new RunIdIncrementer())
                   .listener(listener)
                   .flow(step1)
                   .end()
                   .build();

    }

}
