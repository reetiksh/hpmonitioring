package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class ReviewMeetingModel {
   	
	private Long[] catogy;
	
    private Integer categoryId;
    
    private String category;
    
    private Integer userId;
    
    private String[] gstin;
    
    private Date caseReportingDate;
    
    private String[] caseReportingDateId;
    
    private String[] period;
    
    private String action;
    
    private String recoverAgainstDemand;
    
    private String recoveryByDrc;
    
    private String demand;
    
    private String taxpayerName;
   
    private Integer actionStaus;
   
    private String actionStatusName;
   
    private Integer caseStage;
   
    private String caseStageName;
   
    private Integer recoveryStage;
    
    private String recoveryStageName;
   
    private String indicativeValue;
   
    private Integer circleId;
   
    private String circleName;
    
    private String reviewMeetingDate;
    
    private MultipartFile meetingDocument;
    
    private String tdsCases;
    
    private String[] remarks;        
    
    private String[] categoryoldremarks;
    
    private String[] categoryremarks;


    
	public Long[] getCatogy() {
		return catogy;
	}

	public void setCatogy(Long[] catogy) {
		this.catogy = catogy;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String[] getGstin() {
		return gstin;
	}

	public void setGstin(String[] gstin) {
		this.gstin = gstin;
	}

	public Date getCaseReportingDate() {
		return caseReportingDate;
	}

	public void setCaseReportingDate(Date caseReportingDate) {
		this.caseReportingDate = caseReportingDate;
	}

	public String[] getCaseReportingDateId() {
		return caseReportingDateId;
	}

	public void setCaseReportingDateId(String[] caseReportingDateId) {
		this.caseReportingDateId = caseReportingDateId;
	}

	public String[] getPeriod() {
		return period;
	}

	public void setPeriod(String[] period) {
		this.period = period;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRecoverAgainstDemand() {
		return recoverAgainstDemand;
	}

	public void setRecoverAgainstDemand(String recoverAgainstDemand) {
		this.recoverAgainstDemand = recoverAgainstDemand;
	}

	public String getRecoveryByDrc() {
		return recoveryByDrc;
	}

	public void setRecoveryByDrc(String recoveryByDrc) {
		this.recoveryByDrc = recoveryByDrc;
	}

	public String getDemand() {
		return demand;
	}

	public void setDemand(String demand) {
		this.demand = demand;
	}

	public String getTaxpayerName() {
		return taxpayerName;
	}

	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}

	public Integer getActionStaus() {
		return actionStaus;
	}

	public void setActionStaus(Integer actionStaus) {
		this.actionStaus = actionStaus;
	}

	public String getActionStatusName() {
		return actionStatusName;
	}

	public void setActionStatusName(String actionStatusName) {
		this.actionStatusName = actionStatusName;
	}

	public Integer getCaseStage() {
		return caseStage;
	}

	public void setCaseStage(Integer caseStage) {
		this.caseStage = caseStage;
	}

	public String getCaseStageName() {
		return caseStageName;
	}

	public void setCaseStageName(String caseStageName) {
		this.caseStageName = caseStageName;
	}

	public Integer getRecoveryStage() {
		return recoveryStage;
	}

	public void setRecoveryStage(Integer recoveryStage) {
		this.recoveryStage = recoveryStage;
	}

	public String getRecoveryStageName() {
		return recoveryStageName;
	}

	public void setRecoveryStageName(String recoveryStageName) {
		this.recoveryStageName = recoveryStageName;
	}

	public String getIndicativeValue() {
		return indicativeValue;
	}

	public void setIndicativeValue(String indicativeValue) {
		this.indicativeValue = indicativeValue;
	}

	public Integer getCircleId() {
		return circleId;
	}

	public void setCircleId(Integer circleId) {
		this.circleId = circleId;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}

	public String getReviewMeetingDate() {
		return reviewMeetingDate;
	}

	public void setReviewMeetingDate(String reviewMeetingDate) {
		this.reviewMeetingDate = reviewMeetingDate;
	}

	public MultipartFile getMeetingDocument() {
		return meetingDocument;
	}

	public void setMeetingDocument(MultipartFile meetingDocument) {
		this.meetingDocument = meetingDocument;
	}

	public String getTdsCases() {
		return tdsCases;
	}

	public void setTdsCases(String tdsCases) {
		this.tdsCases = tdsCases;
	}

	public String[] getRemarks() {
		return remarks;
	}

	public void setRemarks(String[] remarks) {
		this.remarks = remarks;
	}

	public String[] getCategoryoldremarks() {
		return categoryoldremarks;
	}

	public void setCategoryoldremarks(String[] categoryoldremarks) {
		this.categoryoldremarks = categoryoldremarks;
	}

	public String[] getCategoryremarks() {
		return categoryremarks;
	}

	public void setCategoryremarks(String[] categoryremarks) {
		this.categoryremarks = categoryremarks;
	}
    
    
}
