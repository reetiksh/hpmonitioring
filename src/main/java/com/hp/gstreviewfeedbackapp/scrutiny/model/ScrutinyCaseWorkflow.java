package com.hp.gstreviewfeedbackapp.scrutiny.model;

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

import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;

@Entity
@Table(name = "scrutiny_case_workflow")
public class ScrutinyCaseWorkflow {
	
	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "case_workflow_sequence"),
        @Parameter(name = "initial_value", value = "4000"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	private long id;

	@Column(name = "gstin")
	private String gstin;

	@Temporal(TemporalType.DATE)
	@Column(name = "caseReportingDate")
	private Date caseReportingDate; 

	@Column(name = "period")
	private String period;

	@Column(name = "taxpayerName")
	private String taxpayerName;

	@Column(name = "extensionNo")
	private String extensionNo;

	@Column(name = "category")
	private String category;

	@Column(name = "indicativeTaxValue")
	private Long indicativeTaxValue;

	@Column(name = "assignedFrom")
	private String assignedFrom;

	@Column(name = "assignedTo")
	private String assignedTo;

	@Column(name = "recoveryByDRC3")
	private Long recoveryByDRC3;

	@Column(name = "amountRecovered")
	private Long amountRecovered;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatingDate")
	private Date updatingDate;

	@Column(name = "action")
	private String action;

	@Column(name = "caseId")
	private String caseId;

	@Column(name = "caseStageArn")
	private String caseStageArn;

	@Column(name = "recoveryStageArn")
	private String recoveryStageArn;

	@Column(name = "asmtTenIssuedOrNot")
	private Boolean asmtTenIssuedOrNot = false;

	@Column(name = "recoveryStage")
	private Integer recoveryStage;

	@Column(name = "caseStage")
	private String caseStage;

	@Column(name = "assigned_from_user_id")
	private Integer assignedFromUserId;

	@Column(name = "workingLocation")
	private String workingLocation;

	@Column(name = "filePath")
	private String filePath;

	@Column(name = "assignedFromLocationId")
	private String assignedFromLocationId;

	@Column(name = "assignedToLocationId")
	private String assignedToLocationId;

	@Column(name = "otherRemarks")
	private String otherRemarks;
	
	@Column(name = "caseRecommendedDateForAssessmentAndAdjudication")
	private Date caseRecommendedDateForAssessmentAndAdjudication;
	
	@Column(name = "parameter")
	private String parameter;
	
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "remarks")
	private ScrutinyTransferRemarks remarks;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
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

	public Long getRecoveryByDRC3() {
		return recoveryByDRC3;
	}

	public void setRecoveryByDRC3(Long recoveryByDRC3) {
		this.recoveryByDRC3 = recoveryByDRC3;
	}

	public Long getAmountRecovered() {
		return amountRecovered;
	}

	public void setAmountRecovered(Long amountRecovered) {
		this.amountRecovered = amountRecovered;
	}

	public Date getUpdatingDate() {
		return updatingDate;
	}

	public void setUpdatingDate(Date updatingDate) {
		this.updatingDate = updatingDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public Boolean getAsmtTenIssuedOrNot() {
		return asmtTenIssuedOrNot;
	}

	public void setAsmtTenIssuedOrNot(Boolean asmtTenIssuedOrNot) {
		this.asmtTenIssuedOrNot = asmtTenIssuedOrNot;
	}

	public Integer getRecoveryStage() {
		return recoveryStage;
	}

	public void setRecoveryStage(Integer recoveryStage) {
		this.recoveryStage = recoveryStage;
	}

	public String getCaseStage() {
		return caseStage;
	}

	public void setCaseStage(String caseStage) {
		this.caseStage = caseStage;
	}

	public Integer getAssignedFromUserId() {
		return assignedFromUserId;
	}

	public void setAssignedFromUserId(Integer assignedFromUserId) {
		this.assignedFromUserId = assignedFromUserId;
	}

	public String getWorkingLocation() {
		return workingLocation;
	}

	public void setWorkingLocation(String workingLocation) {
		this.workingLocation = workingLocation;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getAssignedFromLocationId() {
		return assignedFromLocationId;
	}

	public void setAssignedFromLocationId(String assignedFromLocationId) {
		this.assignedFromLocationId = assignedFromLocationId;
	}

	public String getAssignedToLocationId() {
		return assignedToLocationId;
	}

	public void setAssignedToLocationId(String assignedToLocationId) {
		this.assignedToLocationId = assignedToLocationId;
	}

	public String getOtherRemarks() {
		return otherRemarks;
	}

	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}

	public ScrutinyTransferRemarks getRemarks() {
		return remarks;
	}

	public void setRemarks(ScrutinyTransferRemarks remarks) {
		this.remarks = remarks;
	}

	public Date getCaseRecommendedDateForAssessmentAndAdjudication() {
		return caseRecommendedDateForAssessmentAndAdjudication;
	}

	public void setCaseRecommendedDateForAssessmentAndAdjudication(Date caseRecommendedDateForAssessmentAndAdjudication) {
		this.caseRecommendedDateForAssessmentAndAdjudication = caseRecommendedDateForAssessmentAndAdjudication;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
