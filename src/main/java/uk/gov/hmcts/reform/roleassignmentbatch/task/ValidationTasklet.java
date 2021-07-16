package uk.gov.hmcts.reform.roleassignmentbatch.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.CcdCaseUser;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.AuditFaults;

@Component
public class ValidationTasklet implements Tasklet {

    File downloadedFile;
    FlatFileItemReader<CcdCaseUser> reader;
    List<String> roleMappings;

    String roleReason = "This role does not exist in the list of valid ccd roles";
    String caseIdReason = "The following caseId is not of valid length";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcBatchItemWriter<AuditFaults> auditFaultsWriter;

    @Autowired
    public ValidationTasklet(String fileName, String filePath, FlatFileItemReader<CcdCaseUser> fileItemReader,
                             List<String> roleMapping) {
        downloadedFile = new File(filePath + fileName);
        reader = fileItemReader;
        reader.open(new ExecutionContext());
        roleMappings = roleMapping;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        RepeatStatus repeatStatus;
        repeatStatus = validateRoleMappings(roleMappings);
        validateCaseId();
        if(repeatStatus.isContinuable()) {
            validateCaseId();
        } else {
            contribution.setExitStatus(ExitStatus.FAILED);
        }
        return repeatStatus;
    }

    protected void validateCaseId() throws Exception {
        var ccdRoleQuery = "SELECT distinct (case_data_id) FROM ccd_view WHERE LENGTH(case_data_id) != 16";
        List<String> invalidCcdViewCaseIds
                = jdbcTemplate.query(ccdRoleQuery, (rs, rowNum) -> rs.getString(1));
        persistFaults(invalidCcdViewCaseIds, caseIdReason);
    }

    protected RepeatStatus validateRoleMappings(List<String> roleMappings) throws Exception {
        RepeatStatus status = RepeatStatus.CONTINUABLE;
        var ccdRoleQuery = "select distinct (case_role) from ccd_view";
        List<String> ccdViewRoles = jdbcTemplate.query(ccdRoleQuery, (rs, rowNum) -> rs.getString(1));

        if(!isASubsetOf(roleMappings, ccdViewRoles)) {
            persistFaults(findDifferences(roleMappings, ccdViewRoles), roleReason);
            status = RepeatStatus.FINISHED;
        }
        return status;
    }

    protected boolean isASubsetOf(List<?> list, List<?> sublist) {
        return list.containsAll(sublist);
    }

    protected List<String> findDifferences(List<String> list, List<String> sublist) {
        return sublist.stream()
                .filter(element -> !list.contains(element))
                .collect(Collectors.toList());
    }

    protected void persistFaults(List<String> list, String reason) throws Exception {

        List<AuditFaults> auditFaults = new ArrayList<>();

        list.forEach(invalidProperty -> {
            var fault = new AuditFaults();
            fault.setFailedAt(invalidProperty);
            fault.setReason(reason);
            auditFaults.add(fault);
        });

        auditFaultsWriter.write(auditFaults);
    }



}
