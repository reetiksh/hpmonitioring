package com.hp.gstreviewfeedbackapp.audit.model;

public class AnnexureReportRow {

    private String zoneName;
    private String circleName;
    private int allottedCases;
    private int auditCasesCompleted;
    private int pendingDeskReview;
    private int pendingApprovalAuditPlan;
    private int pendingExaminationBooks;
    private int pendingDAR;
    private int pendingFAR;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public int getAllottedCases() {
        return allottedCases;
    }

    public void setAllottedCases(int allottedCases) {
        this.allottedCases = allottedCases;
    }

    public int getAuditCasesCompleted() {
        return auditCasesCompleted;
    }

    public void setAuditCasesCompleted(int auditCasesCompleted) {
        this.auditCasesCompleted = auditCasesCompleted;
    }

    public int getPendingDeskReview() {
        return pendingDeskReview;
    }

    public void setPendingDeskReview(int pendingDeskReview) {
        this.pendingDeskReview = pendingDeskReview;
    }

    public int getPendingApprovalAuditPlan() {
        return pendingApprovalAuditPlan;
    }

    public void setPendingApprovalAuditPlan(int pendingApprovalAuditPlan) {
        this.pendingApprovalAuditPlan = pendingApprovalAuditPlan;
    }

    public int getPendingExaminationBooks() {
        return pendingExaminationBooks;
    }

    public void setPendingExaminationBooks(int pendingExaminationBooks) {
        this.pendingExaminationBooks = pendingExaminationBooks;
    }

    public int getPendingDAR() {
        return pendingDAR;
    }

    public void setPendingDAR(int pendingDAR) {
        this.pendingDAR = pendingDAR;
    }

    public int getPendingFAR() {
        return pendingFAR;
    }

    public void setPendingFAR(int pendingFAR) {
        this.pendingFAR = pendingFAR;
    }
}
