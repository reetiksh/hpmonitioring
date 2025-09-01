package com.hp.gstreviewfeedbackapp.audit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "l1_user_work_logs")
public class L1UserWorkLogs {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "l1_user_work_logs_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;

	@Column(name = "case_id")
	private String caseId;

	@Column(name = "gstin")
	private String GSTIN;

	@Column(name = "case_reporting_date")
	@Temporal(TemporalType.DATE)
	private Date caseReportingDate;

	@Column(name = "period")
	private String period;

	@Column(name = "taxpayer_name")
	private String taxpayerName;

	@Column(name = "indicative_tax_value")
	private Long indicativeTaxValue;

	@Column(name = "parameter")
	private String parameter;

	@Column(name = "workingLocation")
	private String locationDetails;

	@Column(name = "category")
	private String category;

	@Column(name = "extension_file_name")
	private String auditExtensionNoDocument;

	@Column(name = "extension_no")
	private String extensionNo;

	@Column(name = "assigned_from")
	private String assignedFrom;

	@Column(name = "assign_to")
	private String assignTo;

	@Column(name = "action")
	private String action;

	@Column(name = "last_updated_timestamp")
	private Date lastUpdatedTimeStamp;

	@Column(name = "assigned_from_location")
	private String assignedFromLocation;

	@Column(name = "assign_to_location")
	private String assignToLocation;

	@Column(name = "assigned_from_user_id")
	private Integer assignedFromUserId;

	@Column(name = "assigned_to_user_id")
	private Integer assignedToUserId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
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

	public String getTaxpayerName() {
		return taxpayerName;
	}

	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}

	public Long getIndicativeTaxValue() {
		return indicativeTaxValue;
	}

	public void setIndicativeTaxValue(Long indicativeTaxValue) {
		this.indicativeTaxValue = indicativeTaxValue;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(String locationDetails) {
		this.locationDetails = locationDetails;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAuditExtensionNoDocument() {
		return auditExtensionNoDocument;
	}

	public void setAuditExtensionNoDocument(String auditExtensionNoDocument) {
		this.auditExtensionNoDocument = auditExtensionNoDocument;
	}

	public String getExtensionNo() {
		return extensionNo;
	}

	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}

	public String getAssignedFrom() {
		return assignedFrom;
	}

	public void setAssignedFrom(String assignedFrom) {
		this.assignedFrom = assignedFrom;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	public String getAssignedFromLocation() {
		return assignedFromLocation;
	}

	public void setAssignedFromLocation(String assignedFromLocation) {
		this.assignedFromLocation = assignedFromLocation;
	}

	public String getAssignToLocation() {
		return assignToLocation;
	}

	public void setAssignToLocation(String assignToLocation) {
		this.assignToLocation = assignToLocation;
	}

	public Integer getAssignedFromUserId() {
		return assignedFromUserId;
	}

	public void setAssignedFromUserId(Integer assignedFromUserId) {
		this.assignedFromUserId = assignedFromUserId;
	}

	public Integer getAssignedToUserId() {
		return assignedToUserId;
	}

	public void setAssignedToUserId(Integer assignedToUserId) {
		this.assignedToUserId = assignedToUserId;
	}
}
