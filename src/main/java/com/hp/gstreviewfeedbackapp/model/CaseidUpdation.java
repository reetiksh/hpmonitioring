package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "caseid_updation")
public class CaseidUpdation {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "caseid_updation_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	private long id;

	@Column(name = "GSTIN")
	private String GSTIN;

	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;

	@Column(name = "PERIOD")
	private String period;

	@Column(name = "ASSIGNED_FROM")
	private String assignedFrom;

	@Column(name = "ASSIGNED_TO")
	private String assignedTo;

	@Column(name = "updating_date")
	private Date updatingDate;

	@Column(name = "status")
	private String status;

	@Column(name = "jurisdiction")
	private String jurisdiction;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "remarks")
	private Integer remarks;

	@Column(name = "other_remarks")
	private String otherRemarks;

	@Column(name = "assigned_from_user_id")
	private Integer assignedFromUserId;

	@Column(name = "assigned_to_user_id")
	private Integer assigntoUserId;

	private String oldCaseid;

	private String caseid;

	private String approvalRemarks;

	public String getApprovalRemarks() {
		return approvalRemarks;
	}

	public void setApprovalRemarks(String approvalRemarks) {
		this.approvalRemarks = approvalRemarks;
	}

	public String getOldCaseid() {
		return oldCaseid;
	}

	public void setOldCaseid(String oldCaseid) {
		this.oldCaseid = oldCaseid;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getAssignedFrom() {
		return assignedFrom;
	}

	public void setAssignedFrom(String assignedFrom) {
		this.assignedFrom = assignedFrom;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Date getUpdatingDate() {
		return updatingDate;
	}

	public void setUpdatingDate(Date updatingDate) {
		this.updatingDate = updatingDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
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

	public Integer getAssignedFromUserId() {
		return assignedFromUserId;
	}

	public void setAssignedFromUserId(Integer assignedFromUserId) {
		this.assignedFromUserId = assignedFromUserId;
	}

	public Integer getAssigntoUserId() {
		return assigntoUserId;
	}

	public void setAssigntoUserId(Integer assigntoUserId) {
		this.assigntoUserId = assigntoUserId;
	}

}
