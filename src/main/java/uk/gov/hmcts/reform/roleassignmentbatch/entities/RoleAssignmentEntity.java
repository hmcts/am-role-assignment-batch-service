package uk.gov.hmcts.reform.roleassignmentbatch.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import uk.gov.hmcts.reform.roleassignmentbatch.config.JsonBConverter;


import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role_assignment")
public class RoleAssignmentEntity implements Serializable {

    @Id
    private UUID id;

    @Column(name = "actor_id_type", nullable = false)
    private String actorIdType;

    @Column(name = "actor_id", nullable = false)
    private String actorId;

    @Column(name = "role_type", nullable = false)
    private String roleType;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "classification", nullable = false)
    private String classification;

    @Column(name = "grant_type", nullable = false)
    private String grantType;

    @Column(name = "role_category")
    private String roleCategory;

    @Column(name = "read_only", nullable = false)
    private boolean readOnly;

    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "attributes", nullable = false, columnDefinition = "jsonb")
    @Convert(converter = JsonBConverter.class)
    private String attributes;

//    @Column(name = "authorisations")
//    @Type(type = "uk.gov.hmcts.reform.roleassignmentbatch.config.GenericArrayUserType")
//    private String[] authorisations;

}
