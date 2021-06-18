package uk.gov.hmcts.reform.roleassignmentbatch.processors;

import static uk.gov.hmcts.reform.roleassignmentbatch.util.JacksonUtils.convertValueJsonNode;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.batch.item.ItemProcessor;
import uk.gov.hmcts.reform.domain.model.CcdCaseUsers;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.GrantType;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.RoleCategory;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.RoleType;
import uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums.Status;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.ActorCacheEntity;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.EntityWrapper;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.HistoryEntity;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.RequestEntity;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.RoleAssignmentEntity;


public class EntityWrapperProcessor implements ItemProcessor<CcdCaseUsers, EntityWrapper> {


    /**
     * Process the provided item, returning a potentially modified or new item for continued
     * processing.  If the returned result is null, it is assumed that processing of the item
     * should not continue.
     *
     * @param ccdCaseUsers to be processed
     * @return potentially modified or new item for continued processing, {@code null} if processing of the
     * provided item should not continue.
     * @throws Exception thrown if exception occurs during processing.
     */
    @Override
    public EntityWrapper process(CcdCaseUsers ccdCaseUsers) throws Exception {
        UUID requestUuid = UUID.randomUUID();
        Map<String, JsonNode> attributes = new HashMap<>();
        attributes.put("caseId", convertValueJsonNode("1234567890123456"));
        RequestEntity requestEntity = RequestEntity.builder()
                .id(requestUuid)
                .correlationId(UUID.randomUUID().toString())
                .clientId("ccd_migration")
                .authenticatedUserId("A fixed Authenticated User Id")
                .assignerId(ccdCaseUsers.getUserId())
                .requestType("CREATE")
                .status("APPROVED")
                .process("CCD")
                .replaceExisting(false)
                .roleAssignmentId(UUID.randomUUID())
                .reference(ccdCaseUsers.getCaseDataId()
                        .concat(ccdCaseUsers.getUserId()))
                .log(null)
                .created(LocalDateTime.now())
                .build();
        HistoryEntity roleAssignmentHistoryEntity =
                HistoryEntity.builder()
                        .id(UUID.randomUUID())
                        .status(Status.APPROVED.name())
                        .requestId(requestUuid)
                        .actorId(ccdCaseUsers.getUserId())
                        .actorIdType("IDAM")
                        .roleType(RoleType.CASE.name())
                        .roleName("secret-agent-man")
                        .sequence(1)
                        .classification("Classified")
                        .grantType(GrantType.STANDARD.name())
                        .roleCategory(RoleCategory.JUDICIAL.name())
                        .readOnly(false)
                        .created(LocalDateTime.now())
                        .attributes(convertValueJsonNode(attributes).toString())
                        .build();
        RoleAssignmentEntity roleAssignmentEntity =
                RoleAssignmentEntity.builder()
                        .id(requestUuid)
                        .actorIdType("IDAM")
                        .actorId(ccdCaseUsers.getUserId())
                        .roleType(RoleType.CASE.name())
                        .roleName("secret-agent-man")
                        .classification("Classified")
                        .grantType(GrantType.STANDARD.name())
                        .roleCategory(RoleCategory.JUDICIAL.name())
                        .readOnly(false)
                        .created(LocalDateTime.now())
                        .attributes(convertValueJsonNode(attributes).toString())
                        .build();
        ActorCacheEntity actorCacheEntity =
                ActorCacheEntity.builder()
                        .actorIds(UUID.randomUUID().toString()) //using random as dummy data violates unique key rule
                        .etag(0L)
                        .roleAssignmentResponse(convertValueJsonNode("{roleAssignmentResponse:1234}").toString())
                        .build();
        return EntityWrapper.builder()
                .actorCacheEntity(actorCacheEntity)
                .requestEntity(requestEntity)
                .roleAssignmentHistoryEntity(roleAssignmentHistoryEntity)
                .roleAssignmentEntity(roleAssignmentEntity)
                .build();
    }
}