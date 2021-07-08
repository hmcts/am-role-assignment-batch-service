package uk.gov.hmcts.reform.roleassignmentbatch.task;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.netflix.config.validation.ValidationException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.CcdCaseUser;

@Component
public class ValidationTasklet implements Tasklet {

    File downloadedFile;
    FlatFileItemReader<CcdCaseUser> reader;
    String roleMappings;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    public ValidationTasklet(String fileName, String filePath, FlatFileItemReader<CcdCaseUser> fileItemReader,
                             String roleMapping) {
        downloadedFile = new File(filePath + fileName);
        reader = fileItemReader;
        reader.open(new ExecutionContext());
        roleMappings = roleMapping;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        validateRoleMappings(roleMappings);
        CcdCaseUser ccdUser;
        do {
            ccdUser = reader.read();
            if (ccdUser != null) {
                validate(ccdUser);
            }
        } while (ccdUser != null);

        return RepeatStatus.FINISHED;
    }

    protected void validate(CcdCaseUser ccdCaseUser) {
        validateCaseId(ccdCaseUser.getCaseDataId());
    }

    protected void validateCaseId(String caseId) {
        if (caseId.length() > 16) {
            throw new ValidationException("caseId invalid");
        }
    }

    protected void validateRoleMappings(String roleMappings) {
        String ccdRoleQuery = "select distinct (case_role) from ccd_view";
        List<String> ccdViewRoles = jdbcTemplate.query(ccdRoleQuery, (rs, rowNum) -> rs.getString(1));
        ccdViewRoles.size();



    }
}
