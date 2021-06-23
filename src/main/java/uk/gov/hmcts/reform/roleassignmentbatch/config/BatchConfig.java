package uk.gov.hmcts.reform.roleassignmentbatch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
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
import org.springframework.core.io.PathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.domain.model.CcdCaseUsers;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.EntityWrapper;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.AuditFaults;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.HistoryEntity;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.RequestEntity;
import uk.gov.hmcts.reform.roleassignmentbatch.processors.EntityWrapperProcessor;
import uk.gov.hmcts.reform.roleassignmentbatch.task.DeleteExpiredRecords;
import uk.gov.hmcts.reform.roleassignmentbatch.util.Constants;
import uk.gov.hmcts.reform.roleassignmentbatch.writer.EntityWrapperWriter;

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
    DataSource dataSource;

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
            .resource(new PathResource("src/main/resources/book2.csv"))
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
    public static class CcdFieldSetMapper implements FieldSetMapper<CcdCaseUsers> {
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

    /*@Bean
    public RequestProcessor requestProcessor() {
        return new RequestProcessor();
    }

    @Bean
    public HistoryProcessor historyProcessor() {
        return new HistoryProcessor();
    }*/

    @Bean
    public EntityWrapperProcessor entityWrapperProcessor() {
        return new EntityWrapperProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<RequestEntity> insertInRequestTable() {
        return new JdbcBatchItemWriterBuilder<RequestEntity>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql(Constants.REQUEST_QUERY)
            .dataSource(dataSource)
            .build();
    }

    @Bean
    public JdbcBatchItemWriter<AuditFaults> insertInAuditFaults() {
        return new JdbcBatchItemWriterBuilder<AuditFaults>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(Constants.AUDIT_QUERY)
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<HistoryEntity> insertInHistoryTable() {
        return
                new JdbcBatchItemWriterBuilder<HistoryEntity>()
                        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                        .sql(Constants.HISTORY_QUERY)
                        .dataSource(dataSource)
                        .build();
    }

    @Bean
    EntityWrapperWriter entityWrapperWriter() {
        return new EntityWrapperWriter();
    }

    @Bean
    SkipListener<CcdCaseUsers, EntityWrapper> auditSkipListener() {
        return new AuditSkipListener();
    }

    @Bean
    public Step ccdToRasStep() {
        return steps.get("ccdToRasStep")
                .<CcdCaseUsers, EntityWrapper>chunk(5)
                .faultTolerant()
                .retryLimit(3)
                .retry(DeadlockLoserDataAccessException.class)
                .skip(Exception.class).skip(NumberFormatException.class)
                .skipLimit(10)
                .listener(auditSkipListener())
                .reader(ccdCaseUsersReader())
                .processor(entityWrapperProcessor())
                .writer(entityWrapperWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(10)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public Job ccdToRasBatchJob(@Autowired NotificationListener listener, Step ccdToRasStep) {
        return jobs.get("ccdToRasBatchJob")
                   .incrementer(new RunIdIncrementer())
                   .listener(listener)
                   .flow(ccdToRasStep)
                   .end()
                   .build();

    }

}
