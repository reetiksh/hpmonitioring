package com.hp.gstreviewfeedbackapp.cag.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.TransferRemarks;

@Entity
@Table(name = "cag_hq_review_case")
public class CagHqReviewCase {

	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "cag_hq_review_case_sequence"),
        @Parameter(name = "initial_value", value = "1000"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	private Long id;
	
	@Column(name = "GSTIN")
	private String GSTIN;
	
	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;
	
	@Column(name = "PERIOD")
	private String period;
    
	@Column(name = "parameter")
	private String parameter;
	
	@Column(name = "TAXPAYER_NAME")
	private String taxpayerName;
	@Column(name = "CIRCLE")
	private String circle;
	@Column(name = "CATEGORY")
	private String category;
	@Column(name = "case_updating_date")
	private Date caseUpdatingDate;
	@Column(name = "INDICATIVE_TAX_VALUE")
	private Long indicativeTaxValue;
	@Column(name = "action")
	private String action;
	@Column(name = "ACTION_STATUS")
	private int actionStatus;
	@Column(name = "DEMAND")
	private Long demand;
	@Column(name = "RECOVERY_STAGE")
	private Integer recoveryStage;
	@Column(name = "RECOVERY_BY_DRC3")
	private Long recoveryByDRC3;
	@Column(name = "RECOVERY_AGAINST_DEMAND")
	private Long recoveryAgainstDemand;
	@Column(name = "case_Id")
	private String caseId;
	@Column(name = "recovery_stage_arn")
	private String recoveryStageArn;
	@Column(name = "ASSIGNED_TO")
	private String assignedTo;
	@Column(name = "fo_filepath")
	private String foFilepath;
	@Column(name = "user_id")
	private int userId;
	@Column(name="assigned_from_user_id")
	private Integer assignedFromUserId;
	@Column(name="assigned_to_user_id")
	private Integer assignedToUserId;
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "extension_file_name_id")
	private CagExtensionNoDocument extensionNoDocument;
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="working_location")
	private LocationDetails workingLocation;
	@Column(name="transfer_remarks")
	private String transferRemarks;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getTaxpayerName() {
		return taxpayerName;
	}
	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Date getCaseUpdatingDate() {
		return caseUpdatingDate;
	}
	public void setCaseUpdatingDate(Date caseUpdatingDate) {
		this.caseUpdatingDate = caseUpdatingDate;
	}
	public Long getIndicativeTaxValue() {
		return indicativeTaxValue;
	}
	public void setIndicativeTaxValue(Long indicativeTaxValue) {
		this.indicativeTaxValue = indicativeTaxValue;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(int actionStatus) {
		this.actionStatus = actionStatus;
	}
	
	public Long getDemand() {
		return demand;
	}
	public void setDemand(Long demand) {
		this.demand = demand;
	}
	public Integer getRecoveryStage() {
		return recoveryStage;
	}
	public void setRecoveryStage(Integer recoveryStage) {
		this.recoveryStage = recoveryStage;
	}
	public Long getRecoveryByDRC3() {
		return recoveryByDRC3;
	}
	public void setRecoveryByDRC3(Long recoveryByDRC3) {
		this.recoveryByDRC3 = recoveryByDRC3;
	}
	public Long getRecoveryAgainstDemand() {
		return recoveryAgainstDemand;
	}
	public void setRecoveryAgainstDemand(Long recoveryAgainstDemand) {
		this.recoveryAgainstDemand = recoveryAgainstDemand;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getRecoveryStageArn() {
		return recoveryStageArn;
	}
	public void setRecoveryStageArn(String recoveryStageArn) {
		this.recoveryStageArn = recoveryStageArn;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getFoFilepath() {
		return foFilepath;
	}
	public void setFoFilepath(String foFilepath) {
		this.foFilepath = foFilepath;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public CagExtensionNoDocument getExtensionNoDocument() {
		return extensionNoDocument;
	}
	public void setExtensionNoDocument(CagExtensionNoDocument extensionNoDocument) {
		this.extensionNoDocument = extensionNoDocument;
	}
	public LocationDetails getWorkingLocation() {
		return workingLocation;
	}
	public void setWorkingLocation(LocationDetails workingLocation) {
		this.workingLocation = workingLocation;
	}
	public String getTransferRemarks() {
		return transferRemarks;
	}
	public void setTransferRemarks(String transferRemarks) {
		this.transferRemarks = transferRemarks;
	}
	
	
	
}
