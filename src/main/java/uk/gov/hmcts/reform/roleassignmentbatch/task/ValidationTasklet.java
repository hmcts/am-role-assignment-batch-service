package uk.gov.hmcts.reform.roleassignmentbatch.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.AuditOperationType;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.CcdCaseUser;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.AuditFaults;
import uk.gov.hmcts.reform.roleassignmentbatch.util.JacksonUtils;

@Component
@Slf4j
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
    
        log.info("Validating CcdCaseUsers ");
        var rm = (RowMapper<CcdCaseUser>) (ResultSet rs, int rowNum) -> {
        var ccdCaseUser = new CcdCaseUser();
        ccdCaseUser.setCaseDataId(rs.getString("case_data_id"));
        ccdCaseUser.setUserId(rs.getString("user_id"));
        ccdCaseUser.setCaseRole(rs.getString("case_role"));
        ccdCaseUser.setCaseType(rs.getString("case_type"));
        ccdCaseUser.setBeginDate(rs.getString("begin_date"));
        ccdCaseUser.setRoleCategory(rs.getString("role_category"));
        ccdCaseUser.setJurisdiction(rs.getString("jurisdiction"));
        return ccdCaseUser;
            
        List<CcdCaseUser> ccdCaseUsers = jdbcTemplate.query("select id,case_data_id,user_id,case_role,jurisdiction,"
                + "case_type,role_category,begin_date from ccd_view where case_data_id is null or user_id is null "
                + "or case_role is null or jurisdiction is null or case_type is null or role_category is null or "
                + "begin_date is null", rm);
        if (!ccdCaseUsers.isEmpty()) {
            log.warn("Validation CcdCaseUsers was skipped due to NULLS: " + ccdCaseUsers);
            var auditFaults = AuditFaults.builder()
                    .reason("Validation CcdCaseUsers was skipped due to NULLs")
                    .failedAt(AuditOperationType.VALIDATION.getLabel())
                    .ccdUsers(JacksonUtils.convertValueJsonNode(ccdCaseUsers).toString()).build();
            auditWriter.write(Collections.singletonList(auditFaults));
            contribution.setExitStatus(ExitStatus.FAILED);
        }
    
        RepeatStatus repeatStatus;
        repeatStatus = validateRoleMappings(roleMappings);
        validateCaseId();
        if(repeatStatus.isContinuable()) {
            validateCaseId();
            repeatStatus = RepeatStatus.FINISHED;
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
