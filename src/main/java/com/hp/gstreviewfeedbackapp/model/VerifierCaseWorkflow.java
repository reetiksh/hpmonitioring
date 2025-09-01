package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "verifier_case_workflow")
public class VerifierCaseWorkflow {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "verifier_case_workflow_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	private int id;
	@Column(name = "gstin")
	private String gstin;
	@Column(name = "case_reporting_date")
	private Date caseReportingDate;
	@Column(name = "period")
	private String period;;
//	@Column(name = "action_status")
//	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
//	@JoinColumn(name = "ACTION_STATUS")
//	private ActionStatus actionStatus;
	private String actionStatusName;
//	@Column(name = "case_stage")
//	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
//	@JoinColumn(name = "CASE_STAGE")
//	private CaseStage caseStage;
	private String caseStageName;
	@Column(name = "category")
	private String category;
	@Column(name = "demand")
	private Long demand;
	@Column(name = "indicative_tax_value")
	private Long indicativeTaxValue;
	@Column(name = "recovery_against_demand")
	private Long recoveryAgainstDemand;
	@Column(name = "recovery_by_drc3")
	private Long recoveryByDrc3;
	@Column(name = "review_meeting_comments")
	private String reviewMeetingComments;
	@Column(name = "updating_date")
	private Date updatingDate;
	@Column(name = "action")
	private String action;
	@Column(name = "assigned_to")
	private String assignedTo;
//	@Column(name = "recovery_stage")
//	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
//	@JoinColumn(name = "RECOVERY_STAGE")
//	private RecoveryStage recoveryStage;
	private String recoveryStageName;
	@Column(name = "taxpayer_name")
	private String taxpayerName;
	@Column(name = "AMOUNT_RECOVERED")
	private Long amountRecovered;
	@Column(name = "assignedFrom")
	private String assignedFrom;
	@Column(name = "assigned_from_userId")
	private Integer assignedFromUserId;
	@Column(name = "assigned_to_userId")
	private Integer assignedToUserId;
	@Column(name = "case_id")
	private String caseId;
	@Column(name = "caste_stage_arn")
	private String caseStageArn;
	@Column(name = "EXTENSION_NO")
	private String extensionNo;
	@Column(name = "recovery_stage_arn")
	private String recoveryStageArn;
//	@ManyToOne
//	@JoinColumn(name = "EXTENSION_FILE_NAME_id")
//	private ExtensionNoDocument extensionNoDocument;
	private String extensionFileName;
//	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
//	@JoinColumn(name = "workingLocation")
//	private LocationDetails locationDetails;
	private String workingLocationName;
}
