package uk.gov.hmcts.reform.roleassignmentbatch2.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import uk.gov.hmcts.reform.roleassignmentbatch2.helper.TestDataBuilder;

@RunWith(MockitoJUnitRunner.class)
class DeleteExpiredRecordsTest {

    @Mock
    private JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

    @Mock
    StepContribution stepContribution = mock(StepContribution.class);

    @Mock
    ChunkContext chunkContext = mock(ChunkContext.class);

    @Mock
    ResultSet rs = mock(ResultSet.class);

    private DeleteExpiredRecords sut = new DeleteExpiredRecords(jdbcTemplate, 5);

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void execute() throws IOException {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class)))
            .thenReturn(400);

        List<RoleAssignmentHistory> list = new ArrayList<>();
        list.add(TestDataBuilder.buildRoleAssignmentHistory());

        when(jdbcTemplate.query(anyString(), ArgumentMatchers.<ResultSetExtractor<Object>>any())).thenReturn(list);

        Assertions.assertEquals(RepeatStatus.FINISHED, sut.execute(stepContribution, chunkContext));
    }

    @Test
    void executeThrowsException() throws IOException {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class)))
                .thenThrow(NullPointerException.class);

        List<RoleAssignmentHistory> list = new ArrayList<>();
        list.add(TestDataBuilder.buildRoleAssignmentHistory());

        when(jdbcTemplate.query(anyString(), ArgumentMatchers.<ResultSetExtractor<Object>>any())).thenReturn(list);

        Assertions.assertThrows(NullPointerException.class, () ->
                sut.execute(stepContribution, chunkContext)
        );
    }

    @Test
    void deleteRoleAssignmentRecords() throws IOException {

        List<RoleAssignmentHistory> list = new ArrayList<>();
        list.add(TestDataBuilder.buildRoleAssignmentHistory());

        when(jdbcTemplate.update(any(), any(), any())).thenReturn(1);

        Assertions.assertEquals(1, sut.deleteRoleAssignmentRecords(list));
    }

    @Test
    void insertIntoRoleAssignmentHistoryTable() throws IOException {

        List<RoleAssignmentHistory> list = new ArrayList<>();
        list.add(TestDataBuilder.buildRoleAssignmentHistory());
        int[][] data = new int[1][1];
        data[0][0] = 1;
        when(jdbcTemplate.batchUpdate(anyString(), any(), anyInt(), any())).thenReturn(data);

        Assertions.assertEquals(data, sut.insertIntoRoleAssignmentHistoryTable(list));
    }

    @Test
    void getLiveRecordsFromHistoryTable() throws IOException {
        List<RoleAssignmentHistory> list = new ArrayList<>();
        list.add(TestDataBuilder.buildRoleAssignmentHistory());
        when(jdbcTemplate.query(anyString(), ArgumentMatchers.<ResultSetExtractor<Object>>any())).thenReturn(list);
        Assertions.assertEquals(list, sut.getLiveRecordsFromHistoryTable());
    }


    @Test
    public void getLiveRecordsFromHistoryTableWithValidValues() {
        when(jdbcTemplate.query(
                ArgumentMatchers.anyString(), ArgumentMatchers.<ResultSetExtractor<Object>>any()))
                .thenAnswer((invocation) -> {

                    final ResultSetExtractor<List<RoleAssignmentHistory>> resultSetExtractor =
                            invocation.getArgument(1);
                    when(rs.next()).thenReturn(true, false);
                    when(rs.getObject(ArgumentMatchers.eq("id"))).thenReturn(java.util.UUID.class);
                    when(rs.getObject(ArgumentMatchers.eq("request_id"))).thenReturn(java.util.UUID.class);
                    when(rs.getString(ArgumentMatchers.eq("actor_id_type"))).thenReturn("string");
                    when(rs.getObject(ArgumentMatchers.eq("actor_id")))
                            .thenReturn("3168da13-00b3-41e3-81fa-cbc71ac28a0f");
                    when(rs.getString(ArgumentMatchers.eq("role_type"))).thenReturn("string");
                    when(rs.getString(ArgumentMatchers.eq("role_name"))).thenReturn("Judge");
                    when(rs.getString(ArgumentMatchers.eq("classification"))).thenReturn("PUBLIC");
                    when(rs.getString(ArgumentMatchers.eq("grant_type"))).thenReturn("string");
                    when(rs.getString(ArgumentMatchers.eq("role_category"))).thenReturn("string");
                    when(rs.getBoolean(ArgumentMatchers.eq("read_only"))).thenReturn(true);
                    return resultSetExtractor.extractData(rs);
                });

        List<RoleAssignmentHistory> result = sut.getLiveRecordsFromHistoryTable();

        Assertions.assertEquals("PUBLIC", result.get(0).getClassification());
        Assertions.assertEquals("Judge", result.get(0).getRoleName());
    }

    @Test
    void getCountFromHistoryTable() {
        when(jdbcTemplate.queryForObject("SELECT count(*) from role_assignment_history rah", Integer.class))
            .thenReturn(400);
        Assertions.assertEquals(400, sut.getCountFromHistoryTable());
    }
}
