package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "enforcement_cases_remarks_details")
public class EnforcementCasesRemarksDetails {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "enforcement_cases_remarks_details_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	private Long id;

	private Date recordCreationDate;

	@Column(length = 5000)
	private String remarks;

	private Date reviewMeetingDate;

	private String momDocument;

	@Column(name = "GSTIN")
	private String GSTIN;

	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;

	@Column(name = "PERIOD")
	private String period;

	private Long categoryId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRecordCreationDate() {
		return recordCreationDate;
	}

	public void setRecordCreationDate(Date recordCreationDate) {
		this.recordCreationDate = recordCreationDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getReviewMeetingDate() {
		return reviewMeetingDate;
	}

	public void setReviewMeetingDate(Date reviewMeetingDate) {
		this.reviewMeetingDate = reviewMeetingDate;
	}

	public String getMomDocument() {
		return momDocument;
	}

	public void setMomDocument(String momDocument) {
		this.momDocument = momDocument;
	}

	@Override
	public String toString() {
		return "EnforcementCasesRemarksDetails [id=" + id + ", recordCreationDate=" + recordCreationDate + ", remarks="
				+ remarks + ", reviewMeetingDate=" + reviewMeetingDate + ", momDocument=" + momDocument + ", GSTIN="
				+ GSTIN + ", caseReportingDate=" + caseReportingDate + ", period=" + period + "]";
	}

}
