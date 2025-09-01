package com.hp.gstreviewfeedbackapp.scrutiny.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ScrutinyAsmtTenModel {

	private String asmtGstin;
	private String asmtPeriod;
	private String asmtCaseReportingDate;
	private String caseId;
	private String caseStageArn;
	private Long demand; // Demand
	private Integer recoveryStage;
	private Long recoveryByDRC3;
	private MultipartFile asmtTenUploadedFile;
	private MultipartFile closureReportFile;
	private List<String> recoveryStageArn;
	private String recommendedOnDate;
	private MultipartFile recommendedForAssessAndAdjudicationFile;
	private MultipartFile recommendedForAuditFile;
	
	
	public String getAsmtGstin() {
		return asmtGstin;
	}

	public void setAsmtGstin(String asmtGstin) {
		this.asmtGstin = asmtGstin;
	}

	public String getAsmtPeriod() {
		return asmtPeriod;
	}

	public void setAsmtPeriod(String asmtPeriod) {
		this.asmtPeriod = asmtPeriod;
	}

	public String getAsmtCaseReportingDate() {
		return asmtCaseReportingDate;
	}

	public void setAsmtCaseReportingDate(String asmtCaseReportingDate) {
		this.asmtCaseReportingDate = asmtCaseReportingDate;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getCaseStageArn() {
		return caseStageArn;
	}

	public void setCaseStageArn(String caseStageArn) {
		this.caseStageArn = caseStageArn;
	}

	public Long getDemand() {
		return demand;
	}

	public void setDemand(Long demand) {
		this.demand = demand;
	}

	public Integer getRecoveryStage() {
		return recoveryStage;
	}

	public void setRecoveryStage(Integer recoveryStage) {
		this.recoveryStage = recoveryStage;
	}

	public Long getRecoveryByDRC3() {
		return recoveryByDRC3;
	}

	public void setRecoveryByDRC3(Long recoveryByDRC3) {
		this.recoveryByDRC3 = recoveryByDRC3;
	}

	public MultipartFile getAsmtTenUploadedFile() {
		return asmtTenUploadedFile;
	}

	public void setAsmtTenUploadedFile(MultipartFile asmtTenUploadedFile) {
		this.asmtTenUploadedFile = asmtTenUploadedFile;
	}

	public List<String> getRecoveryStageArn() {
		return recoveryStageArn;
	}

	public void setRecoveryStageArn(List<String> recoveryStageArn) {
		this.recoveryStageArn = recoveryStageArn;
	}

	public MultipartFile getClosureReportFile() {
		return closureReportFile;
	}

	public void setClosureReportFile(MultipartFile closureReportFile) {
		this.closureReportFile = closureReportFile;
	}
	public String getRecommendedOnDate() {
		return recommendedOnDate;
	}
	public void setRecommendedOnDate(String recommendedOnDate) {
		this.recommendedOnDate = recommendedOnDate;
	}
	public MultipartFile getRecommendedForAssessAndAdjudicationFile() {
		return recommendedForAssessAndAdjudicationFile;
	}
	public void setRecommendedForAssessAndAdjudicationFile(MultipartFile recommendedForAssessAndAdjudicationFile) {
		this.recommendedForAssessAndAdjudicationFile = recommendedForAssessAndAdjudicationFile;
	}
	public MultipartFile getRecommendedForAuditFile() {
		return recommendedForAuditFile;
	}

	public void setRecommendedForAuditFile(MultipartFile recommendedForAuditFile) {
		this.recommendedForAuditFile = recommendedForAuditFile;
	}
}
