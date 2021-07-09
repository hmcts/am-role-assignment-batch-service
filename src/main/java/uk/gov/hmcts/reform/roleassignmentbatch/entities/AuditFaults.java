package uk.gov.hmcts.reform.roleassignmentbatch.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuditFaults implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "failed_at", columnDefinition = "jsonb")
    private String failedAt;

    private String reason;

    @Column(name = "ccd_users", columnDefinition = "jsonb")
    private String ccdUsers;

    @Column(columnDefinition = "jsonb")
    private String request;

    @Column(columnDefinition = "jsonb")
    private String history;

    @Column(columnDefinition = "jsonb")
    private String live;

    @Column(name = "actor_cache", columnDefinition = "jsonb")
    private String actorCache;
}
