package com.hp.gstreviewfeedbackapp.audit.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;

@Entity
@Table(name = "audit_master_case_workflow")
public class AuditMasterCaseWorkflow {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "audit_master_case_workflow_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;

	@Column(name = "case_id")
	private String caseId;

	@Column(name = "gstin")
	private String GSTIN;

	@DateTimeFormat(iso = ISO.DATE)
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

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "workingLocation")
	private LocationDetails locationDetails;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "category")
	private Category category;

	@Column(name = "assigned_from")
	private String assignedFrom;

	@Column(name = "assign_to")
	private String assignTo;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "action")
	private AuditCaseStatus action;

	@Column(name = "updating_timestamp")
	private Date updatingTimestamp;

	@Column(name = "assigned_from_user_id")
	private Integer assignedFromUserId;

	@Column(name = "assign_to_user_id")
	private Integer assignToUserId;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "assigned_from_location_id")
	private LocationDetails assignedFromLocationId;

	public LocationDetails getAssignedFromLocationId() {
		return assignedFromLocationId;
	}

	public void setAssignedFromLocationId(LocationDetails assignedFromLocationId) {
		this.assignedFromLocationId = assignedFromLocationId;
	}

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

	public LocationDetails getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(LocationDetails locationDetails) {
		this.locationDetails = locationDetails;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public AuditCaseStatus getAction() {
		return action;
	}

	public void setAction(AuditCaseStatus action) {
		this.action = action;
	}

	public Date getUpdatingTimestamp() {
		return updatingTimestamp;
	}

	public void setUpdatingTimestamp(Date updatingTimestamp) {
		this.updatingTimestamp = updatingTimestamp;
	}

	public Integer getAssignedFromUserId() {
		return assignedFromUserId;
	}

	public void setAssignedFromUserId(Integer assignedFromUserId) {
		this.assignedFromUserId = assignedFromUserId;
	}

	public Integer getAssignToUserId() {
		return assignToUserId;
	}

	public void setAssignToUserId(Integer assignToUserId) {
		this.assignToUserId = assignToUserId;
	}

	@Override
	public String toString() {
		return "AuditMasterCaseWorkflow [id=" + id + ", caseId=" + caseId + ", GSTIN=" + GSTIN + ", caseReportingDate="
				+ caseReportingDate + ", period=" + period + ", taxpayerName=" + taxpayerName + ", indicativeTaxValue="
				+ indicativeTaxValue + ", parameter=" + parameter + ", locationDetails=" + locationDetails
				+ ", category=" + category + ", assignedFrom=" + assignedFrom + ", assignTo=" + assignTo + ", action="
				+ action + ", updatingTimestamp=" + updatingTimestamp + ", assignedFromUserId=" + assignedFromUserId
				+ ", assignToUserId=" + assignToUserId + "]";
	}
}
