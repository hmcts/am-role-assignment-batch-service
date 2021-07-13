package uk.gov.hmcts.reform.roleassignmentbatch.domain.model.enums;

public enum ReconQuery {

    //Audit Fault
    AUDIT_FAULTS_TOTAL_COUNT("select count(1) from audit_faults"),

    //CCD
    CCD_TOTAL_COUNT("select count(1) from ccd_view"),
    GROUP_BY_CCD_JURISDICTION("select jurisdiction,count(1) from ccd_view group by jurisdiction order by jurisdiction"),
    GROUP_BY_CCD_CASE_ROLE("select case_role,count(1) from ccd_view group by case_role order by case_role"),

    //AM
    AM_TOTAL_COUNT("select count(1) from replica_role_assignment"),
    GROUP_BY_AM_JURISDICTION("select attributes->>'caseTypeId' as caseTypeId,count(1) from replica_role_assignment ra "
            + "group by attributes->>'caseTypeId' order by attributes->>'caseTypeId'"),
    GROUP_BY_AM_CASE_ROLE("select role_name,count(1) from replica_role_assignment group by role_name order by role_name"),

    //Reconciliation
    INSERT_RECONCILIATION_QUERY("insert into reconciliation_data (run_id, ccd_jurisdiction_data,"
            + "ccd_role_name_data,am_jurisdiction_data,am_role_name_data,total_count_from_ccd,"
            + "total_count_from_am,status,notes) values (?, ?,?,?,?,?,?,?,?)"),
    CCD_JURISDICTION_KEY("jurisdiction"),
    CCD_CASE_ROLE_KEY("case_role"),
    AM_JURISDICTION_KEY("caseTypeId"),
    AM_CASE_ROLE_KEY("role_name"),
    PASSED("PASSED"),
    FAILED("FAILED"),
    SUCCESS_STATUS("Total Record are matching from both ccd_view and am_role_assignment table."),
    FAILED_STATUS("Total Record are NOT matching from both ccd_view and am_role_assignment table."),
    COUNT("count");

    private final String key;

    ReconQuery(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
