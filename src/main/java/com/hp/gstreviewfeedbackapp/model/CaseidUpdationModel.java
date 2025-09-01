package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class CaseidUpdationModel {

	private String gstnocaseid;

	private String datecaseid;

	private String periodcaseid;

	private MultipartFile filePath;

	private Integer remarks;

	private String otherRemarks;

	private String caseid;

	private String status;

	private String approvalRemarks;

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getGstnocaseid() {
		return gstnocaseid;
	}

	public void setGstnocaseid(String gstnocaseid) {
		this.gstnocaseid = gstnocaseid;
	}

	public String getDatecaseid() {
		return datecaseid;
	}

	public void setDatecaseid(String datecaseid) {
		this.datecaseid = datecaseid;
	}

	public String getPeriodcaseid() {
		return periodcaseid;
	}

	public void setPeriodcaseid(String periodcaseid) {
		this.periodcaseid = periodcaseid;
	}

	public MultipartFile getFilePath() {
		return filePath;
	}

	public void setFilePath(MultipartFile filePath) {
		this.filePath = filePath;
	}

	public Integer getRemarks() {
		return remarks;
	}

	public void setRemarks(Integer remarks) {
		this.remarks = remarks;
	}

	public String getOtherRemarks() {
		return otherRemarks;
	}

	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}

}
