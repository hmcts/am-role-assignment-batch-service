package uk.gov.hmcts.reform.roleassignmentbatch.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class ReplicateTablesTasklet implements Tasklet {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("Dropping audit_faults");
        jdbcTemplate.update("DROP TABLE IF EXISTS audit_faults");
        log.info("Drop Table audit_faults: Successful");

        log.info("Dropping replica_actor_cache");
        jdbcTemplate.update("DROP TABLE IF EXISTS replica_actor_cache_control");
        log.info("Drop Table replica_role_assignment: Successful");

        log.info("Dropping replica_role_assignment");
        jdbcTemplate.update("DROP TABLE IF EXISTS replica_role_assignment");
        log.info("Drop Table replica_role_assignment: Successful");

        log.info("Dropping replica_role_assignment_history");
        jdbcTemplate.update("DROP TABLE IF EXISTS replica_role_assignment_history");
        log.info("Drop Table replica_role_assignment_history: Successful");

        log.info("Dropping replicated Request Table");
        jdbcTemplate.update("DROP TABLE IF EXISTS replica_role_assignment_request");
        log.info("Drop Table: Successful");

        log.info("Creating audit_faults Table");
        jdbcTemplate
            .update("CREATE TABLE audit_faults (id int8 NOT NULL," +
                    "failed_at varchar NULL," +
                    "reason varchar NULL," +
                    "ccd_users varchar NULL," +
                    "request varchar NULL," +
                    "history varchar NULL," +
                    "live varchar NULL);");
        jdbcTemplate.update("CREATE SEQUENCE IF NOT EXISTS AUDIT_ID_SEQ");
        jdbcTemplate.update("ALTER TABLE audit_faults ALTER COLUMN id SET DEFAULT nextval('AUDIT_ID_SEQ');");
        log.info("Creating audit_faults Table: Successful");

        log.info("Starting table replication");

        log.info("Replicating Request Table");
        log.info("Replication Request Table Successful ? "
                 + (1 <= jdbcTemplate
            .update("CREATE TABLE replica_role_assignment_request (LIKE role_assignment_request INCLUDING ALL);")));
        log.info("Replicating Request Table: Successful");

        log.info("Replicating History Table");
        log.info("Replication History Table Successful ? "
                 + (1 <= jdbcTemplate
            .update("CREATE TABLE replica_role_assignment_history (LIKE role_assignment_history INCLUDING ALL);")));
        log.info("Replicating History Table: Successful");

        log.info("Replicating Live Table");
        log.info("Replication Live Table Successful ? "
                 + (1 <= jdbcTemplate.update("CREATE TABLE replica_role_assignment (LIKE role_assignment INCLUDING ALL);")));
        log.info("Replicating Live Table: Successful");

        log.info("Replicating Actor Cache Table");
        log.info("Replication Actor Cache Table Successful ? "
                 + (1 <= jdbcTemplate.update("CREATE TABLE replica_actor_cache_control (LIKE actor_cache_control INCLUDING ALL);")));
        log.info("Replicating Actor Cache Table: Successful");


        jdbcTemplate.update("ALTER TABLE replica_role_assignment_history ADD CONSTRAINT fk_role_assignment_history_role_assignment_request" +
                            " FOREIGN KEY (request_id) REFERENCES replica_role_assignment_request(id);");

        /*log.info("Insert data from current tables to Replicas");
        jdbcTemplate.update("INSERT into replica_actor_cache_control(SELECT * FROM actor_cache_control);");
        jdbcTemplate.update("INSERT into replica_role_assignment(SELECT * FROM role_assignment);");
        jdbcTemplate.update("INSERT into replica_role_assignment_history(SELECT * FROM role_assignment_history);");
        jdbcTemplate.update("INSERT into replica_role_assignment_request(SELECT * FROM role_assignment_request);");
        log.info("Data insertion from Current tables to replicas is complete");*/

        return RepeatStatus.FINISHED;
    }
}
