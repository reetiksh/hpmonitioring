package com.hp.gstreviewfeedbackapp.audit.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_master")
@Getter
@Setter
public class AuditMaster {
	@Id
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
	
	@Column(name = "scrutinyCaseId")
	private String scrutinyCaseId;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "workingLocation")
	private LocationDetails locationDetails;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "category")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "EXTENSION_FILE_NAME_id")
	private AuditExtensionNoDocument auditExtensionNoDocument;

	@Column(name = "extension_no")
	private String extensionNo;

	@Column(name = "assigned_from")
	private String assignedFrom;

	@Column(name = "assign_to")
	private String assignTo;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "action")
	private AuditCaseStatus action;

	@OneToMany(mappedBy = "caseId", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<AuditCaseDateDocumentDetails> auditCaseDateDocumentDetails;

	@Column(name = "last_updated_timestamp")
	private Date lastUpdatedTimeStamp;

	private Long totalInvolvedAmount;

	private String fullyRecovered;

	@JoinColumn
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private UserDetails foUserDetailsForShowCauseNotice;

	private String recommendedModule;

	private String arnNumber;

	private Long toalAmountToBeRecovered = 0l;

	@Temporal(TemporalType.DATE)
	private Date assignedDateFromL2ToL3;

	@Transient
	private List<String> parametersNameList;

	@Transient
	private Date currentStatusLastWorkingDate;

	public Long getTotalInvolvedAmount() {
		return totalInvolvedAmount;
	}

	public void setTotalInvolvedAmount(Long totalInvolvedAmount) {
		this.totalInvolvedAmount = totalInvolvedAmount;
	}

	public List<AuditCaseDateDocumentDetails> getAuditCaseDateDocumentDetails() {
		return auditCaseDateDocumentDetails;
	}

	public void setAuditCaseDateDocumentDetails(List<AuditCaseDateDocumentDetails> auditCaseDateDocumentDetails) {
		this.auditCaseDateDocumentDetails = auditCaseDateDocumentDetails;
	}

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
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

	public String getExtensionNo() {
		return extensionNo;
	}

	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}

	public AuditExtensionNoDocument getAuditExtensionNoDocument() {
		return auditExtensionNoDocument;
	}

	public void setAuditExtensionNoDocument(AuditExtensionNoDocument auditExtensionNoDocument) {
		this.auditExtensionNoDocument = auditExtensionNoDocument;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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
	
	

	@Override
	public String toString() {
		return "AuditMaster [caseId=" + caseId + ", GSTIN=" + GSTIN + ", caseReportingDate=" + caseReportingDate
				+ ", period=" + period + ", taxpayerName=" + taxpayerName + ", indicativeTaxValue=" + indicativeTaxValue
				+ ", parameter=" + parameter + ", locationDetails=" + locationDetails + "]";
	}
}
