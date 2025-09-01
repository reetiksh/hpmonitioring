package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnforcementReviewCaseModel {

	private String GSTIN_ID;
	private String caseReportingDate_ID;
	private String period_ID;
	private String taxpayerName;
	private String extensionNo;
	private String category;
	private String circle;
	private Long indicativeTaxValue;
	private String assignedTo;
	private int actionStatus;
	private int caseStage;
	private Long demand;
	private Long recoveryByDRC3;
	private Long recoveryAgainstDemand;
	private int recoveryStage;
	private String amountRecovered;
	private MultipartFile uploadedFile;
	private String actionStatusName;
	private String caseStageName;
	private String recoveryStageName;
	private String recovery;
	private String remarks;
	private Long count;
	private String sum;
	private Integer recoveryByDRCInt;
	private Integer recoveryAgainstDemandInt;
	private String caseId;
	private String caseStageArn;
	private List<String> recoveryStageArn;
	private String recoveryStageArnStr;
	private String caseIdUpdated;
	private String reportingDate;
	private Date date;
	private Long indicativeTaxVal;
	private String uploadedFileName;
	private Long categoryId;
	private String caseIdUpdate;
	private String recoveryStatus;
	private String auditCase;
	private String auditCaseId;
	private String scrutinyCaseId;
	private String parameter;
	private Date caseUpdatedDate;
	private String module;
	private String reason;
	private String categoryListId;
	private List<String> caseList;
	private String cagCaseId;
	private String assignedFrom;
	
	
	public EnforcementReviewCaseModel() {
		super();
	}

	public EnforcementReviewCaseModel(String gSTIN_ID, String period_ID, Date date, String parameter,
			Long indicativeTaxValue, Long demand, String caseStageName, Long recoveryByDRC3,
			Long recoveryAgainstDemand, String caseId, String module, String caseReportingDate_ID) {
		super();
		GSTIN_ID = gSTIN_ID;
		this.period_ID = period_ID;
		this.indicativeTaxValue = indicativeTaxValue;
		this.demand = demand;
		this.caseStageName = caseStageName;
		this.recoveryByDRC3 = recoveryByDRC3;
		this.recoveryAgainstDemand = recoveryAgainstDemand;
		this.caseId = caseId;
		this.date = date;
		this.parameter = parameter;
		this.module = module;
		this.caseReportingDate_ID = caseReportingDate_ID;
	}
	
	public String getRecoveryStatus() {
		return recoveryStatus;
	}

	public void setRecoveryStatus(String recoveryStatus) {
		this.recoveryStatus = recoveryStatus;
	}

	public String getCaseIdUpdate() {
		return caseIdUpdate;
	}

	public void setCaseIdUpdate(String caseIdUpdate) {
		this.caseIdUpdate = caseIdUpdate;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}

	public Long getIndicativeTaxVal() {
		return indicativeTaxVal;
	}

	public void setIndicativeTaxVal(Long indicativeTaxVal) {
		this.indicativeTaxVal = indicativeTaxVal;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(String reportingDate) {
		this.reportingDate = reportingDate;
	}

	public String getCaseIdUpdated() {
		return caseIdUpdated;
	}

	public void setCaseIdUpdated(String caseIdUpdated) {
		this.caseIdUpdated = caseIdUpdated;
	}

	public String getRecoveryStageArnStr() {
		return recoveryStageArnStr;
	}

	public void setRecoveryStageArnStr(String recoveryStageArnStr) {
		this.recoveryStageArnStr = recoveryStageArnStr;
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

	public Integer getRecoveryByDRCInt() {
		return recoveryByDRCInt;
	}

	public void setRecoveryByDRCInt(Integer recoveryByDRCInt) {
		this.recoveryByDRCInt = recoveryByDRCInt;
	}

	public Integer getRecoveryAgainstDemandInt() {
		return recoveryAgainstDemandInt;
	}

	public void setRecoveryAgainstDemandInt(Integer recoveryAgainstDemandInt) {
		this.recoveryAgainstDemandInt = recoveryAgainstDemandInt;
	}

	public String getGSTIN_ID() {
		return GSTIN_ID;
	}

	public void setGSTIN_ID(String gSTIN_ID) {
		GSTIN_ID = gSTIN_ID;
	}

	public String getCaseReportingDate_ID() {
		return caseReportingDate_ID;
	}

	public void setCaseReportingDate_ID(String caseReportingDate_ID) {
		this.caseReportingDate_ID = caseReportingDate_ID;
	}

	public String getPeriod_ID() {
		return period_ID;
	}

	public void setPeriod_ID(String period_ID) {
		this.period_ID = period_ID;
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

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
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

	public int getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(int actionStatus) {
		this.actionStatus = actionStatus;
	}

	public int getCaseStage() {
		return caseStage;
	}

	public void setCaseStage(int caseStage) {
		this.caseStage = caseStage;
	}

	public Long getDemand() {
		return demand;
	}

	public void setDemand(Long demand) {
		this.demand = demand;
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

	public int getRecoveryStage() {
		return recoveryStage;
	}

	public void setRecoveryStage(int recoveryStage) {
		this.recoveryStage = recoveryStage;
	}

	public String getAmountRecovered() {
		return amountRecovered;
	}

	public void setAmountRecovered(String amountRecovered) {
		this.amountRecovered = amountRecovered;
	}

	public MultipartFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(MultipartFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getActionStatusName() {
		return actionStatusName;
	}

	public void setActionStatusName(String actionStatusName) {
		this.actionStatusName = actionStatusName;
	}

	public String getCaseStageName() {
		return caseStageName;
	}

	public void setCaseStageName(String caseStageName) {
		this.caseStageName = caseStageName;
	}

	public String getRecoveryStageName() {
		return recoveryStageName;
	}

	public void setRecoveryStageName(String recoveryStageName) {
		this.recoveryStageName = recoveryStageName;
	}

	public String getRecovery() {
		return recovery;
	}

	public void setRecovery(String recovery) {
		this.recovery = recovery;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public List<String> getRecoveryStageArn() {
		return recoveryStageArn;
	}

	public void setRecoveryStageArn(List<String> recoveryStageArn) {
		this.recoveryStageArn = recoveryStageArn;
	}
	
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public Date getCaseUpdatedDate() {
		return caseUpdatedDate;
	}
	public void setCaseUpdatedDate(Date caseUpdatedDate) {
		this.caseUpdatedDate = caseUpdatedDate;
	}

	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	

	public String getCategoryListId() {
		return categoryListId;
	}

	public void setCategoryListId(String categoryListId) {
		this.categoryListId = categoryListId;
	}

	public List<String> getCaseList() {
		return caseList;
	}

	public void setCaseList(List<String> caseList) {
		this.caseList = caseList;
	}
	

	public String getCagCaseId() {
		return cagCaseId;
	}

	public void setCagCaseId(String cagCaseId) {
		this.cagCaseId = cagCaseId;
	}
	
	public String getAssignedFrom() {
		return assignedFrom;
	}

	public void setAssignedFrom(String assignedFrom) {
		this.assignedFrom = assignedFrom;
	}
	
	
    
	@Override
	public String toString() {
		return "EnforcementReviewCaseModel [GSTIN_ID=" + GSTIN_ID + ", caseReportingDate_ID=" + caseReportingDate_ID
				+ ", period_ID=" + period_ID + ", taxpayerName=" + taxpayerName + ", extensionNo=" + extensionNo
				+ ", category=" + category + ", circle=" + circle + ", indicativeTaxValue=" + indicativeTaxValue
				+ ", assignedTo=" + assignedTo + ", actionStatus=" + actionStatus + ", caseStage=" + caseStage
				+ ", demand=" + demand + ", recoveryByDRC3=" + recoveryByDRC3 + ", recoveryAgainstDemand="
				+ recoveryAgainstDemand + ", recoveryStage=" + recoveryStage + ", amountRecovered=" + amountRecovered
				+ ", uploadedFile=" + uploadedFile + ", actionStatusName=" + actionStatusName + ", caseStageName="
				+ caseStageName + ", recoveryStageName=" + recoveryStageName + ", recovery=" + recovery + ", remarks="
				+ remarks + ", count=" + count + ", sum=" + sum + ", recoveryByDRCInt=" + recoveryByDRCInt
				+ ", recoveryAgainstDemandInt=" + recoveryAgainstDemandInt + ", caseId=" + caseId + ", caseStageArn="
				+ caseStageArn + ", recoveryStageArn=" + recoveryStageArn + ", recoveryStageArnStr="
				+ recoveryStageArnStr + ", caseIdUpdated=" + caseIdUpdated + ", reportingDate=" + reportingDate
				+ ", date=" + date + ", indicativeTaxVal=" + indicativeTaxVal + ", uploadedFileName=" + uploadedFileName
				+ ", categoryId=" + categoryId + ", caseIdUpdate=" + caseIdUpdate + ", recoveryStatus=" + recoveryStatus
				+ ", auditCase=" + auditCase + ", auditCaseId=" + auditCaseId + ", scrutinyCaseId=" + scrutinyCaseId
				+ ", parameter=" + parameter + ", caseUpdatedDate=" + caseUpdatedDate + ", module=" + module
				+ ", reason=" + reason + ", categoryListId=" + categoryListId + ", caseList=" + caseList
				+ ", cagCaseId=" + cagCaseId + "]";
	}

}
