package com.hp.gstreviewfeedbackapp.model;

import org.springframework.web.multipart.MultipartFile;

public class WorkFlowModel {

	private String caseAssignedTo;
	private String remarkOptions;
	private String otherRemarks;
	private String gstno;
	private String date;
	private String period;
	private String category;
	private String financialyear;
	private String view;
	private MultipartFile uploadedFile;
	private String parameter;

	public WorkFlowModel(String caseAssignedTo, String remarkOptions, String otherRemarks, String gstno, String date,
			String period, String category, String financialyear, String view) {
		super();
		this.caseAssignedTo = caseAssignedTo;
		this.remarkOptions = remarkOptions;
		this.otherRemarks = otherRemarks;
		this.gstno = gstno;
		this.date = date;
		this.period = period;
		this.category = category;
		this.financialyear = financialyear;
		this.view = view;
	}
	
	public MultipartFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(MultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public String getCaseAssignedTo() {
		return caseAssignedTo;
	}

	public void setCaseAssignedTo(String caseAssignedTo) {
		this.caseAssignedTo = caseAssignedTo;
	}

	public String getRemarkOptions() {
		return remarkOptions;
	}

	public void setRemarkOptions(String remarkOptions) {
		this.remarkOptions = remarkOptions;
	}

	public String getOtherRemarks() {
		return otherRemarks;
	}

	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}

	public String getGstno() {
		return gstno;
	}

	public void setGstno(String gstno) {
		this.gstno = gstno;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFinancialyear() {
		return financialyear;
	}

	public void setFinancialyear(String financialyear) {
		this.financialyear = financialyear;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
    
	
}
