package com.hp.gstreviewfeedbackapp.scrutiny.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ScrutinyProceedingModel {

	private String gstin;
	private String taxpayerName;
	private String circle;
	private String caseCategory;
	private String period;
	private String casereportingdate;
	private Long indicativeTaxValue;
	private Integer recoveryStage;
	private Long demand;
	private Long recoveryByDRC3;
	private MultipartFile uploadedFile;
	private List<String> recoveryStageArn;

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public String getTaxpayerName() {
		return taxpayerName;
	}

	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getCaseCategory() {
		return caseCategory;
	}

	public void setCaseCategory(String caseCategory) {
		this.caseCategory = caseCategory;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getCasereportingdate() {
		return casereportingdate;
	}

	public void setCasereportingdate(String casereportingdate) {
		this.casereportingdate = casereportingdate;
	}

	public Long getIndicativeTaxValue() {
		return indicativeTaxValue;
	}

	public void setIndicativeTaxValue(Long indicativeTaxValue) {
		this.indicativeTaxValue = indicativeTaxValue;
	}

	public Integer getRecoveryStage() {
		return recoveryStage;
	}

	public void setRecoveryStage(Integer recoveryStage) {
		this.recoveryStage = recoveryStage;
	}

	public Long getDemand() {
		return demand;
	}

	public void setDemand(Long demand) {
		this.demand = demand;
	}

	public Long getRecoveryByDRC3() {
		return recoveryByDRC3;
	}

	public void setRecoveryByDRC3(Long recoveryByDRC3) {
		this.recoveryByDRC3 = recoveryByDRC3;
	}

	public MultipartFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(MultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public List<String> getRecoveryStageArn() {
		return recoveryStageArn;
	}

	public void setRecoveryStageArn(List<String> recoveryStageArn) {
		this.recoveryStageArn = recoveryStageArn;
	}

}
