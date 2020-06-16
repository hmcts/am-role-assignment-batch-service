package uk.gov.hmcts.reform.roleassignmentbatch.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.roleassignmentbatch.task.LeafRouteTask;
import uk.gov.hmcts.reform.roleassignmentbatch.task.ParentRouteTask;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Override
    public void setDataSource(DataSource dataSource) {
        // initialize a Map based Job Repository by default
    }

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    ParentRouteTask parentRouteTask;

    @Autowired
    LeafRouteTask leafRouteTask;

    @Value("${leaf-route-task}")
    String taskLeaf;

    @Value("${parent-route-task}")
    String taskParent;

    @Value("${batchjob-name}")
    String jobName;

    @Bean
    public Step stepLeafRoute() {
        return steps.get(taskLeaf)
                    .tasklet(leafRouteTask)
                    .build();
    }

    @Bean
    public Step stepOrchestration() {
        return steps.get(taskParent)
                    .tasklet(parentRouteTask)
                    .build();
    }

    @Bean
    public Job runRoutesJob() {
        return jobs.get(jobName)
                   .incrementer(new RunIdIncrementer())
                   .start(stepLeafRoute())
                   .next(stepOrchestration())
                   .build();
    }
}
