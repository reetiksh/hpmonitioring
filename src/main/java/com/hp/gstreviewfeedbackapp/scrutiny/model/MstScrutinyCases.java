package com.hp.gstreviewfeedbackapp.scrutiny.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hp.gstreviewfeedbackapp.model.ActionStatus;
import com.hp.gstreviewfeedbackapp.model.CaseStage;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;

@Entity
@Table(name = "Mst_Scrutiny_Cases")

public class MstScrutinyCases {
	@EmbeddedId
	private CompositeKey id;

	@Column(name = "taxpayerName")
	private String taxpayerName;

	@Column(name = "extensionNo")
	private String extensionNo;

//	@Column(name = "category")
//	private String category;
	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "category")
	private Category category;

	@Column(name = "indicativeTaxValue")
	private Long indicativeTaxValue;

	@Column(name = "assignedTo")
	private String assignedTo;

	@Column(name = "demand")
	private Long demand;

	@Column(name = "recoveryByDRC03")
	private Long recoveryByDRC03;

	@Column(name = "recoveryAgainstDemand")
	private Long recoveryAgainstDemand;

	@Column(name = "amountRecovered")
	private Long amountRecovered;

	@Column(name = "action")
	private String action;

	@Column(name = "assignedFrom")
	private String assignedFrom;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "caseUpdateDate")
	private Date caseUpdateDate;

	@Column(name = "caseId")
	private String caseId;

	@Column(name = "caseStageArn")
	private String caseStageArn;

	@Column(name = "recoveryStageArn")
	private String recoveryStageArn;

	@Column(name = "asmtTenIssuedOrNot")
	private Boolean asmtTenIssuedOrNot = false;

	@Column(name = "acknowlegeByFoOrNot")
	private Boolean acknowlegeByFoOrNot = false;

	@Column(name = "parameters")
	private String parameters;

	@Transient
	private String allConcatParametersValue;

	@Transient
	private String remark;

	@Transient
	private String suggestedJurisdictionName;

	@Transient
	private String suggestedJurisdictionId;

	@Transient
	private List<String> verifierRemarks;

	@Transient
	private List<String> hqRemarks;
	
	@Transient
	private String filePath;
	
	@Transient
	private String actionDescription;
	
	@Transient
	private String currentlyAssignedTo;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "recoveryStage")
	private ScrutinyRecoveryStage recoveryStage;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "workingLocation")
	private LocationDetails locationDetails;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "actionStatus")
	private ActionStatus actionStatus;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "caseStage")
	private CaseStage caseStage;

	@ManyToOne
	@JoinColumn(name = "EXTENSION_FILE_NAME_id")
	private ScrutinyExtensionNoDocument scrutinyExtensionNoDocument;

	public CompositeKey getId() {
		return id;
	}

	public void setId(CompositeKey id) {
		this.id = id;
	}

	public String getTaxpayerName() {
		return taxpayerName;
	}

	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}

	public String getExtensionNo() {
		return extensionNo;
	}

	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getIndicativeTaxValue() {
		return indicativeTaxValue;
	}

	public void setIndicativeTaxValue(Long indicativeTaxValue) {
		this.indicativeTaxValue = indicativeTaxValue;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public ActionStatus getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(ActionStatus actionStatus) {
		this.actionStatus = actionStatus;
	}

	public CaseStage getCaseStage() {
		return caseStage;
	}

	public void setCaseStage(CaseStage caseStage) {
		this.caseStage = caseStage;
	}

	public Long getDemand() {
		return demand;
	}

	public void setDemand(Long demand) {
		this.demand = demand;
	}

	public Long getRecoveryByDRC03() {
		return recoveryByDRC03;
	}

	public void setRecoveryByDRC03(Long recoveryByDRC03) {
		this.recoveryByDRC03 = recoveryByDRC03;
	}

	public Long getRecoveryAgainstDemand() {
		return recoveryAgainstDemand;
	}

	public void setRecoveryAgainstDemand(Long recoveryAgainstDemand) {
		this.recoveryAgainstDemand = recoveryAgainstDemand;
	}

	public ScrutinyRecoveryStage getRecoveryStage() {
		return recoveryStage;
	}

	public void setRecoveryStage(ScrutinyRecoveryStage recoveryStage) {
		this.recoveryStage = recoveryStage;
	}

	public Long getAmountRecovered() {
		return amountRecovered;
	}

	public void setAmountRecovered(Long amountRecovered) {
		this.amountRecovered = amountRecovered;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAssignedFrom() {
		return assignedFrom;
	}

	public void setAssignedFrom(String assignedFrom) {
		this.assignedFrom = assignedFrom;
	}

	public Date getCaseUpdateDate() {
		return caseUpdateDate;
	}

	public void setCaseUpdateDate(Date caseUpdateDate) {
		this.caseUpdateDate = caseUpdateDate;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getCaseStageArn() {
		return caseStageArn;
	}

	public void setCaseStageArn(String caseStageArn) {
		this.caseStageArn = caseStageArn;
	}

	public String getRecoveryStageArn() {
		return recoveryStageArn;
	}

	public void setRecoveryStageArn(String recoveryStageArn) {
		this.recoveryStageArn = recoveryStageArn;
	}

	public LocationDetails getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(LocationDetails locationDetails) {
		this.locationDetails = locationDetails;
	}

	public ScrutinyExtensionNoDocument getScrutinyExtensionNoDocument() {
		return scrutinyExtensionNoDocument;
	}

	public void setScrutinyExtensionNoDocument(ScrutinyExtensionNoDocument scrutinyExtensionNoDocument) {
		this.scrutinyExtensionNoDocument = scrutinyExtensionNoDocument;
	}

	public Boolean getAsmtTenIssuedOrNot() {
		return asmtTenIssuedOrNot;
	}

	public void setAsmtTenIssuedOrNot(Boolean asmtTenIssuedOrNot) {
		this.asmtTenIssuedOrNot = asmtTenIssuedOrNot;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Boolean getAcknowlegeByFoOrNot() {
		return acknowlegeByFoOrNot;
	}

	public void setAcknowlegeByFoOrNot(Boolean acknowlegeByFoOrNot) {
		this.acknowlegeByFoOrNot = acknowlegeByFoOrNot;
	}

	public String getAllConcatParametersValue() {
		return allConcatParametersValue;
	}

	public void setAllConcatParametersValue(String allConcatParametersValue) {
		this.allConcatParametersValue = allConcatParametersValue;
	}

	public String getSuggestedJurisdictionName() {
		return suggestedJurisdictionName;
	}

	public void setSuggestedJurisdictionName(String suggestedJurisdictionName) {
		this.suggestedJurisdictionName = suggestedJurisdictionName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSuggestedJurisdictionId() {
		return suggestedJurisdictionId;
	}

	public void setSuggestedJurisdictionId(String suggestedJurisdictionId) {
		this.suggestedJurisdictionId = suggestedJurisdictionId;
	}

	public List<String> getVerifierRemarks() {
		return verifierRemarks;
	}

	public void setVerifierRemarks(List<String> verifierRemarks) {
		this.verifierRemarks = verifierRemarks;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<String> getHqRemarks() {
		return hqRemarks;
	}

	public String getCurrentlyAssignedTo() {
		return currentlyAssignedTo;
	}

	public void setCurrentlyAssignedTo(String currentlyAssignedTo) {
		this.currentlyAssignedTo = currentlyAssignedTo;
	}

	public void setHqRemarks(List<String> hqRemarks) {
		this.hqRemarks = hqRemarks;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}
}
