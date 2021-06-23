package uk.gov.hmcts.reform.roleassignmentbatch.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.reform.domain.model.CcdCaseUsers;

@Getter
@Setter
@Builder
@ToString
public class EntityWrapper {

    private CcdCaseUsers ccdCaseUsers;
    private HistoryEntity historyEntity;
    private RequestEntity requestEntity;
}
