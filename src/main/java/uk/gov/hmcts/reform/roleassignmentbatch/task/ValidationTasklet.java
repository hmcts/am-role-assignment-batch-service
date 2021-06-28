package uk.gov.hmcts.reform.roleassignmentbatch.task;

import com.netflix.config.validation.ValidationException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.domain.model.CcdCaseUser;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.RoleCategory;

import java.io.File;

@Component
public class ValidationTasklet implements Tasklet {

    File downloadedFile;
    FlatFileItemReader<CcdCaseUser> reader;

    @Autowired
    public ValidationTasklet(String fileName, String filePath, FlatFileItemReader<CcdCaseUser> fileItemReader) {
        downloadedFile = new File(filePath + fileName);
        reader = fileItemReader;

        reader.open(new ExecutionContext());
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
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
        validateUserId(ccdCaseUser.getUserId());
        validateCaseRole(ccdCaseUser.getCaseRole());
        validateRoleCategory(ccdCaseUser.getRoleCategory());
        validateJurisdiction(ccdCaseUser.getJurisdiction());
        validateCaseTypeId(ccdCaseUser.getCaseType());
    }

    protected void validateCaseId(String caseId) {
        if(caseId.length() > 16) {
            throw new ValidationException("caseId invalid");
        }
    }

    protected void validateUserId(String userId) {
        if(userId.length() > 64) {
            throw new ValidationException("userId invalid");
        }
    }

    protected void validateCaseRole(String caseRole) {
        if(caseRole.length() > 40) {
            throw new ValidationException("caseRole invalid");
        }
    }

    protected void validateRoleCategory(String roleCategory) {
        var isValid = false;
        for(RoleCategory realRole : RoleCategory.values()) {
            if(realRole.name().equalsIgnoreCase(roleCategory)) {
                isValid = true;
                break;
            }
        }
        if(!isValid) {
            throw new ValidationException(
                String.format("The RoleCategory parameter supplied: %s is not a valid role.", roleCategory));
        }
    }

    protected void validateJurisdiction(String jurisdiction) {
        if(jurisdiction.length() > 255) {
            throw new ValidationException("jurisdiction invalid");
        }
    }

    protected void validateCaseTypeId(String caseTypeId) {
        if(caseTypeId.length() > 255) {
            throw new ValidationException("caseTypeId invalid");
        }
    }

    protected void validateStartDate(String startDate) {
        if(startDate.length() < 16) {
            throw new ValidationException("startDate invalid length");
        }

    }
    //case reference - bigint in db, in reality these are all 16 digits long
    //user id - varchar(64) in db, but just the IDAM ID which is usually a UUID. Sometimes it can be a string as well. please don’t enforce UUID check.
    //case role - varchar(40) in db
    //role category - doesn’t currently exist in db, but will be a value of PROFESSIONAL/CITIZEN/JUDICIAL
    //jurisdiction - varchar(255) in DB, though the longest in AAT is 20 chars
    //case type id - varchar(255) in DB, though the longest in AAT is 35 chars
    //start date - timestamp in DB
}
