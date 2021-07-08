package uk.gov.hmcts.reform.roleassignmentbatch.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReconciliationData {

    @Id
    @Column(name = "run_id", nullable = false)
    private String runId;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @Column(name = "ccd_jurisdiction_data", nullable = false, columnDefinition = "jsonb")
    private String ccdJurisdictionData;

    @Column(name = "ccd_role_name_data", nullable = false, columnDefinition = "jsonb")
    private String ccdRoleNameData;

    @Column(name = "am_jurisdiction_data", nullable = false, columnDefinition = "jsonb")
    private String amJurisdictionData;

    @Column(name = "am_role_name_data", nullable = false, columnDefinition = "jsonb")
    private String amRoleNameData;

    @Column(name = "total_count_from_ccd", nullable = false, columnDefinition = "jsonb")
    private int totalCountFromCcd;

    @Column(name = "total_count_from_am", nullable = false, columnDefinition = "jsonb")
    private int totalCountFromAm;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "notes", nullable = false)
    private String notes;
}
