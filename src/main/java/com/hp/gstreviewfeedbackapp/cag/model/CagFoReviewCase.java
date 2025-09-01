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
@Table(name = "cag_fo_review_case")
public class CagFoReviewCase {

	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "cag_fo_review_case_sequence"),
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
	@Column(name = "CATEGORY")
	private String category;
	@Column(name = "case_updating_date")
	private Date caseUpdatingDate;
	@Column(name = "INDICATIVE_TAX_VALUE")
	private Long indicativeTaxValue;
	@Column(name = "action")
	private String action;
	@Column(name = "initiatedModule")
	private String initiatedModule;
	@Column(name = "RECOVERY_BY_DRC3")
	private Long recoveryByDRC3;
	@Column(name = "case_Id")
	private String caseId;
	@Column(name = "ASSIGNED_TO")
	private String assignedTo;
	@Column(name = "fo_filepath")
	private String foFilepath;
	@Column(name = "user_id")
	private int userId;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "action_status")
	private CagCategory actionStatus;
	@Column(name = "remark")
	private String remark;
	@Column(name = "filePath")
	private String filePath;
	@Column(name = "circle")
	private String circle;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "reasonId")
	private CagReason cagReason;
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
	@Column(name="other_remarks")
	private String otherRemarks;
	@Column(name="suggested_jurisdiction")
	private String suggestedJurisdiction;
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "remarks")
	private TransferRemarks remarks;
	@Column(name = "transfer_file_path")
	private String transferFilePath;
	@Column(name = "close_remark")
	private String closeRemark;
	@Column(name = "close_filePath")
	private String closeFilePath;
	
	
	public String getTransferFilePath() {
		return transferFilePath;
	}
	public void setTransferFilePath(String transferFilePath) {
		this.transferFilePath = transferFilePath;
	}
	public Integer getAssignedFromUserId() {
		return assignedFromUserId;
	}
	public void setAssignedFromUserId(Integer assignedFromUserId) {
		this.assignedFromUserId = assignedFromUserId;
	}
	public LocationDetails getWorkingLocation() {
		return workingLocation;
	}
	public void setWorkingLocation(LocationDetails workingLocation) {
		this.workingLocation = workingLocation;
	}
	public TransferRemarks getRemarks() {
		return remarks;
	}
	public void setRemarks(TransferRemarks remarks) {
		this.remarks = remarks;
	}
	public Integer getAssignedToUserId() {
		return assignedToUserId;
	}
	public void setAssignedToUserId(Integer assignedToUserId) {
		this.assignedToUserId = assignedToUserId;
	}
	public String getOtherRemarks() {
		return otherRemarks;
	}
	public void setOtherRemarks(String otherRemarks) {
		this.otherRemarks = otherRemarks;
	}
	public String getSuggestedJurisdiction() {
		return suggestedJurisdiction;
	}
	public void setSuggestedJurisdiction(String suggestedJurisdiction) {
		this.suggestedJurisdiction = suggestedJurisdiction;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getTaxpayerName() {
		return taxpayerName;
	}
	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
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
	
	public Long getRecoveryByDRC3() {
		return recoveryByDRC3;
	}
	public void setRecoveryByDRC3(Long recoveryByDRC3) {
		this.recoveryByDRC3 = recoveryByDRC3;
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public CagExtensionNoDocument getExtensionNoDocument() {
		return extensionNoDocument;
	}
	public void setExtensionNoDocument(CagExtensionNoDocument extensionNoDocument) {
		this.extensionNoDocument = extensionNoDocument;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public CagCategory getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(CagCategory actionStatus) {
		this.actionStatus = actionStatus;
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
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public CagReason getCagReason() {
		return cagReason;
	}
	public void setCagReason(CagReason cagReason) {
		this.cagReason = cagReason;
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
