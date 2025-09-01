package com.hp.gstreviewfeedbackapp.cag.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.hp.gstreviewfeedbackapp.model.ActionStatus;
import com.hp.gstreviewfeedbackapp.model.CaseStage;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.ExtensionNoDocument;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;

@Entity
@Table(name = "Cag_Master_Cases")

public class MstCagCases {
	
	@EmbeddedId
	private CagCompositeKey id;
	
    @Column(name = "uniqueId")
    private Long uniqueId;

	@Column(name = "taxpayerName") 
	private String taxpayerName;

	@Column(name = "extensionNo")
	private String extensionNo;

	@Column(name = "category")
	private String category;
    
	@Column(name = "indicativeTaxValue")
	private Long indicativeTaxValue;

	@Column(name = "assignedTo")
	private String assignedTo;

	@Column(name = "recoveryByDRC03")
	private Long recoveryByDRC03;

	@Column(name = "caseId")
	private String caseId;

	@Column(name = "action")
	private String action;
	
	@Column(name = "initiatedModule")
	private String initiatedModule;
	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "action_status")
	private CagCategory actionStatus;
    
	@Column(name = "assignedFrom")
	private String assignedFrom;
	
	@Column(name = "assigned_from_userId")
	private Integer assignedFromUserId = 0;

	@Column(name = "assigned_to_userId")
	private Integer assignedToUserId = 0;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "caseUpdateDate")
	private Date caseUpdateDate;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "filePath")
	private String filePath;
	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "reasonId")
	private CagReason cagReason;
	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "workingLocation")
	private LocationDetails locationDetails;

	@ManyToOne
	@JoinColumn(name = "EXTENSION_FILE_NAME_id")
	private CagExtensionNoDocument cagExtensionNoDocument;
	
	@Column(name = "close_remark")
	private String closeRemark;
	
	@Column(name = "close_filePath")
	private String closeFilePath;
	
	
	public CagCompositeKey getId() {
		return id;
	}

	public void setId(CagCompositeKey id) {
		this.id = id;
	}

	public CagExtensionNoDocument getCagExtensionNoDocument() {
		return cagExtensionNoDocument;
	}

	public void setCagExtensionNoDocument(CagExtensionNoDocument cagExtensionNoDocument) {
		this.cagExtensionNoDocument = cagExtensionNoDocument;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
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

	public Long getRecoveryByDRC03() {
		return recoveryByDRC03;
	}

	public void setRecoveryByDRC03(Long recoveryByDRC03) {
		this.recoveryByDRC03 = recoveryByDRC03;
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

	public LocationDetails getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(LocationDetails locationDetails) {
		this.locationDetails = locationDetails;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public CagReason getCagReason() {
		return cagReason;
	}

	public void setCagReason(CagReason cagReason) {
		this.cagReason = cagReason;
	}

	public Long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(Long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public CagCategory getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(CagCategory actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getInitiatedModule() {
		return initiatedModule;
	}

	public void setInitiatedModule(String initiatedModule) {
		this.initiatedModule = initiatedModule;
	}

	public String getCloseRemark() {
		return closeRemark;
	}

	public void setCloseRemark(String closeRemark) {
		this.closeRemark = closeRemark;
	}

	public String getCloseFilePath() {
		return closeFilePath;
	}

	public void setCloseFilePath(String closeFilePath) {
		this.closeFilePath = closeFilePath;
	}
	
	
}
