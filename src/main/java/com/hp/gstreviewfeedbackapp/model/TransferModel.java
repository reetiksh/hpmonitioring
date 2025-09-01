package com.hp.gstreviewfeedbackapp.model;

public class TransferModel {

	private String caseAssignedTo;
	private String remarkOptions;
	private String otherRemarks;
	private String gstno;
	private String date;
	private String period;

	public TransferModel(String caseAssignedTo, String remarkOptions, String otherRemarks, String gstno, String date,
			String period) {
		super();
		this.caseAssignedTo = caseAssignedTo;
		this.remarkOptions = remarkOptions;
		this.otherRemarks = otherRemarks;
		this.gstno = gstno;
		this.date = date;
		this.period = period;
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

}
