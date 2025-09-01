package com.hp.gstreviewfeedbackapp.data;

import java.util.Date;

public class HqTransfer {
	
	private String GSTIN;
	private Date caseReportingDate;
	private String period;
	private Long indicativeTaxValue;
	private String taxpayerName;
	private String assignedFromLocationId;
	private String assignedFromLocationName;
	private String remark;
	private String suggestedJurisdictionId;
	private String suggestedJurisdictionName;
	private String category;
	private Date updatingDate;
	private String transferFilePath;
	
	public String getTransferFilePath() {
		return transferFilePath;
	}
	public void setTransferFilePath(String transferFilePath) {
		this.transferFilePath = transferFilePath;
	}
	public Date getUpdatingDate() {
		return updatingDate;
	}
	public void setUpdatingDate(Date updatingDate) {
		this.updatingDate = updatingDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getGSTIN() {
		return GSTIN;
	}
	public void setGSTIN(String gSTIN) {
		GSTIN = gSTIN;
	}
	
	public Date getCaseReportingDate() {
		return caseReportingDate;
	}
	public void setCaseReportingDate(Date caseReportingDate) {
		this.caseReportingDate = caseReportingDate;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Long getIndicativeTaxValue() {
		return indicativeTaxValue;
	}
	public void setIndicativeTaxValue(Long indicativeTaxValue) {
		this.indicativeTaxValue = indicativeTaxValue;
	}
	public String getTaxpayerName() {
		return taxpayerName;
	}
	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}
	public String getAssignedFromLocationId() {
		return assignedFromLocationId;
	}
	public void setAssignedFromLocationId(String assignedFromLocationId) {
		this.assignedFromLocationId = assignedFromLocationId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAssignedFromLocationName() {
		return assignedFromLocationName;
	}
	public void setAssignedFromLocationName(String assignedFromLocationName) {
		this.assignedFromLocationName = assignedFromLocationName;
	}
	public String getSuggestedJurisdictionId() {
		return suggestedJurisdictionId;
	}
	public void setSuggestedJurisdictionId(String suggestedJurisdictionId) {
		this.suggestedJurisdictionId = suggestedJurisdictionId;
	}
	public String getSuggestedJurisdictionName() {
		return suggestedJurisdictionName;
	}
	public void setSuggestedJurisdictionName(String suggestedJurisdictionName) {
		this.suggestedJurisdictionName = suggestedJurisdictionName;
	}
	@Override
	public String toString() {
		return "HqTransfer [GSTIN=" + GSTIN + ", caseReportingDate=" + caseReportingDate + ", period=" + period
				+ ", indicativeTaxValue=" + indicativeTaxValue + ", taxpayerName=" + taxpayerName
				+ ", assignedFromLocationId=" + assignedFromLocationId + ", assignedFromLocationName="
				+ assignedFromLocationName + ", remark=" + remark + ", suggestedJurisdictionId="
				+ suggestedJurisdictionId + ", suggestedJurisdictionName=" + suggestedJurisdictionName + ", category="
				+ category + ", updatingDate=" + updatingDate + ", transferFilePath=" + transferFilePath + "]";
	}
	
}
