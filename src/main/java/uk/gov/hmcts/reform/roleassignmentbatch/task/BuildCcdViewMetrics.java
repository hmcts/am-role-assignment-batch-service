package uk.gov.hmcts.reform.roleassignmentbatch.task;

import java.util.List;
import java.util.Map;
import java.util.OptionalLong;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.ReconQuery;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.ReconciliationData;
import uk.gov.hmcts.reform.roleassignmentbatch.service.ReconciliationDataService;
import uk.gov.hmcts.reform.roleassignmentbatch.util.BatchUtil;
import uk.gov.hmcts.reform.roleassignmentbatch.util.Constants;

@Slf4j
@Component
public class BuildCcdViewMetrics implements Tasklet {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReconciliationDataService reconDataService;

    /**
     * Reconciliation Logic with below steps.
     * 1. Ccd_view count is validate against am_role_assgnment table.
     * 2. If both count does not match then status column updated with Failed in Reconciliation_data table
     * 3. ccd_jurisdiction_data,ccd_role_name_data,replica_am_jurisdiction_data,replica_am_role_name_data
     * updated with grouing by role and total record
     * 4. Migration job status is updated with Failed status if there is any record in Audit_table.
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Building CCD Reconciliation data");

        String jobId = contribution.getStepExecution().getJobExecution().getId().toString();

        int totalCountFromCcdView = reconDataService.populateTotalRecord(ReconQuery.CCD_TOTAL_COUNT.getKey());

        List<Map<String, Object>> groupByCcdJurisdiction = reconDataService
            .groupByFieldNameAndCount(ReconQuery.GROUP_BY_CCD_JURISDICTION.getKey());

        List<Map<String, Object>> groupByCcdRoleName = reconDataService
            .groupByFieldNameAndCount(ReconQuery.GROUP_BY_CCD_CASE_ROLE.getKey());

        String notes = Constants.EMPTY_STRING;

        notes = notes.concat(validateCounts(groupByCcdJurisdiction, totalCountFromCcdView, contribution));
        notes = notes.concat(validateCounts(groupByCcdRoleName, totalCountFromCcdView, contribution));

        String ccdJurisdictionData = reconDataService
            .populateAsJsonData(groupByCcdJurisdiction, ReconQuery.CCD_JURISDICTION_KEY.getKey());
        String ccdRoleNameData = reconDataService
            .populateAsJsonData(groupByCcdRoleName, ReconQuery.CCD_CASE_ROLE_KEY.getKey());

        ReconciliationData reconciliationData =
            ReconciliationData.builder()
                              .runId(jobId)
                              .ccdJurisdictionData(ccdJurisdictionData)
                              .ccdRoleNameData(ccdRoleNameData)
                              .totalCountFromCcd(totalCountFromCcdView)
                              .status(StringUtils.hasText(notes) ? ReconQuery.FAILED.getKey()
                                                                 : ReconQuery.IN_PROGRESS.getKey())
                              .amRecordsBeforeMigration(BatchUtil.getAmRecordsCount(jdbcTemplate))
                              .notes(StringUtils.hasText(notes) ? notes : ReconQuery.IN_PROGRESS.name())
                              .build();
        reconDataService.saveReconciliationData(reconciliationData);

        log.info("CCD Reconciliation data built successfully");
        return RepeatStatus.FINISHED;
    }

    private String validateCounts(List<Map<String, Object>> groupByCcdFields,
                                  int totalCountFromCcdView, StepContribution contribution) {
        if (totalCountFromCcdView != getCounts(groupByCcdFields)) {
            log.error(Constants.ERROR_BUILDIND_CCD_RECONCILIATION_DATA);
            contribution.setExitStatus(ExitStatus.FAILED);
            return Constants.ERROR_BUILDIND_CCD_RECONCILIATION_DATA;
        }
        return Constants.EMPTY_STRING;

    }

    private long getCounts(List<Map<String, Object>> list) {
        return list.stream()
                   .map(map -> map.values()
                                  .stream()
                                  .filter(Long.class::isInstance)
                                  .mapToLong(val -> (long) val)
                                  .findFirst())
                   .mapToLong(OptionalLong::getAsLong)
                   .sum();
    }
}
