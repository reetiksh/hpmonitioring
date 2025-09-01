package com.hp.gstreviewfeedbackapp.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Enforcement_Review_Case")
//@IdClass(CompositeKey.class)
@Getter
@Setter
public class EnforcementReviewCase {
	@EmbeddedId
	private CompositeKey id;
	@Column(name = "TAXPAYER_NAME")
	private String taxpayerName;
	@Column(name = "EXTENSION_NO")
	private String extensionNo;
	@Column(name = "CATEGORY")
	private String category;
	@Column(name = "INDICATIVE_TAX_VALUE")
	private Long indicativeTaxValue;
	@Column(name = "ASSIGNED_TO")
	private String assignedTo;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ACTION_STATUS")
	private ActionStatus actionStatus;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "CASE_STAGE")
	private CaseStage caseStage;
	@Column(name = "DEMAND")
	private Long demand;
	@Column(name = "RECOVERY_BY_DRC3")
	private Long recoveryByDRC3;
	@Column(name = "RECOVERY_AGAINST_DEMAND")
	private Long recoveryAgainstDemand;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "RECOVERY_STAGE")
	private RecoveryStage recoveryStage;
	@Column(name = "AMOUNT_RECOVERED")
	private Long amountRecovered;
	@Column(name = "action")
	private String action;
	@Column(name = "assignedFrom")
	private String assignedFrom;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "case_update_date")
	private Date caseUpdateDate;
	@Column(name = "assigned_from_userId")
	private int assignedFromUserId = 0;
	@Column(name = "assigned_to_userId")
	private Integer assignedToUserId = 0;
	@Column(name = "case_Id")
	private String caseId;
	@Column(name = "caste_stage_arn")
	private String caseStageArn;
	@Column(name = "recovery_stage_arn")
	private String recoveryStageArn;
	@Column(name = "finding_category")
	private String findingCategory;
	@Column(name = "parameter")
	private String parameter;
	@Column(name = "scrutinyCaseId")
	private String scrutinyCaseId;
	private String auditCaseId;
	private String enforcementCaseId;
	@Transient
	private String fileName;
	@Transient
	private String fileAbsolutePath;
	@Transient
	private List<String> remark;
	@Transient
	private List<String> apRemarks;
	@Transient
	private List<ApproverRemarksToRejectTheCase> approverRemarksToRejectTheCase;
	@Transient
	private String appealRevisionFilePath;
	@Transient
	private String appealRevisionRemarkByRu;
	@Transient
	private String appealRevisionRemarkByAp;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "workingLocation")
	private LocationDetails locationDetails;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "EXTENSION_FILE_NAME_id")
	private ExtensionNoDocument extensionNoDocument;
	private String caseidUpdationStatus;
	private String recoveryStatus;
	@Column(name = "FULL_RECOVERY_DATE")
	private Date fullRecoveryDate;

	@Override
	public String toString() {
		return "EnforcementReviewCase [id=" + id + ", taxpayerName=" + taxpayerName + ", extensionNo=" + extensionNo
				+ ", category=" + category + ", indicativeTaxValue=" + indicativeTaxValue + ", assignedTo=" + assignedTo
				+ ", actionStatus=" + actionStatus + ", caseStage=" + caseStage + ", demand=" + demand
				+ ", recoveryByDRC3=" + recoveryByDRC3 + ", recoveryAgainstDemand=" + recoveryAgainstDemand
				+ ", recoveryStage=" + recoveryStage + ", amountRecovered=" + amountRecovered + ", action=" + action
				+ ", assignedFrom=" + assignedFrom + ", caseUpdateDate=" + caseUpdateDate + ", assignedFromUserId="
				+ assignedFromUserId + ", assignedToUserId=" + assignedToUserId + ", caseId=" + caseId
				+ ", caseStageArn=" + caseStageArn + ", recoveryStageArn=" + recoveryStageArn + ", findingCategory="
				+ findingCategory + ", parameter=" + parameter + ", scrutinyCaseId=" + scrutinyCaseId + ", auditCaseId="
				+ auditCaseId + ", fileName=" + fileName + ", fileAbsolutePath=" + fileAbsolutePath + ", remark="
				+ remark + ", apRemarks=" + apRemarks + ", approverRemarksToRejectTheCase="
				+ approverRemarksToRejectTheCase + ", appealRevisionFilePath=" + appealRevisionFilePath
				+ ", appealRevisionRemarkByRu=" + appealRevisionRemarkByRu + ", appealRevisionRemarkByAp="
				+ appealRevisionRemarkByAp + ", locationDetails=" + locationDetails + ", extensionNoDocument="
				+ extensionNoDocument + ", caseidUpdationStatus=" + caseidUpdationStatus + ", recoveryStatus="
				+ recoveryStatus + ", fullRecoveryDate=" + fullRecoveryDate + "]";
	}
}
