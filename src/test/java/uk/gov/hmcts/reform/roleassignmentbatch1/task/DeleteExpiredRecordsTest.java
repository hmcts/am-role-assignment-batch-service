package uk.gov.hmcts.reform.roleassignmentbatch1.task;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import uk.gov.hmcts.reform.roleassignmentbatch1.helper.TestDataBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DeleteExpiredRecordsTest {

    @Mock
    JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    @Mock
    StepContribution stepContribution = mock(StepContribution.class);

    @Mock
    ChunkContext chunkContext = mock(ChunkContext.class);

    DeleteExpiredRecords sut = new DeleteExpiredRecords(jdbcTemplate, 5);

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void execute() {
        when(jdbcTemplate.queryForObject("SELECT count(*) from role_assignment_history rah", Integer.class))
                .thenReturn(400);

        Assertions.assertEquals(RepeatStatus.FINISHED, sut.execute(stepContribution, chunkContext));
        //need to do more here but is failing because execute calls getLiveRecordsFromHistoryTable()
    }

    @Test
    void deleteRoleAssignmentRecords() throws IOException {

        List<RoleAssignmentHistory> list = new ArrayList<>();
        list.add(TestDataBuilder.buildRoleAssignmentHistory());

        when(jdbcTemplate.update(any(), any(), any()))
                .thenReturn(1);

        Assertions.assertEquals(1, sut.deleteRoleAssignmentRecords(list));
    }

    //@Test
    void insertIntoRoleAssignmentHistoryTable() throws IOException {

        List<RoleAssignmentHistory> list = new ArrayList<>();
        list.add(TestDataBuilder.buildRoleAssignmentHistory());

        //when(jdbcTemplate.batchUpdate(any(), any(), any(), any())).thenReturn(new int[1][0]);

        Assertions.assertEquals(new int[1][0], sut.insertIntoRoleAssignmentHistoryTable(list));
    }

    //@Test
    void getLiveRecordsFromHistoryTable() throws IOException {

        List<RoleAssignmentHistory> list = new ArrayList<>();
        list.add(TestDataBuilder.buildRoleAssignmentHistory());

        //when(jdbcTemplate.query(any(String.class),)).thenReturn(list);

        Assertions.assertEquals(new int[1][0], sut.insertIntoRoleAssignmentHistoryTable(list));
    }

    @Test
    void getCountFromHistoryTable() {
        when(jdbcTemplate.queryForObject("SELECT count(*) from role_assignment_history rah", Integer.class))
                .thenReturn(400);
        Assertions.assertEquals(400, sut.getCountFromHistoryTable());
    }
}
