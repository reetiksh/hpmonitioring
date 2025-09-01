package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fo_review_case")
@Getter
@Setter
public class FoReviewCase {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "fo_review_case_sequence"),
			@Parameter(name = "initial_value", value = "1000"), @Parameter(name = "increment_size", value = "1") })
	private Long id;
	@Column(name = "GSTIN")
	private String GSTIN;
	@Temporal(TemporalType.DATE)
	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;
	@Column(name = "PERIOD")
	private String period;
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
	@Column(name = "CASE_STAGE")
	private int caseStage;
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
	@Column(name = "caste_stage_arn")
	private String caseStageArn;
	@Column(name = "recovery_stage_arn")
	private String recoveryStageArn;
	@Column(name = "ASSIGNED_TO")
	private String assignedTo;
	@Column(name = "fo_filepath")
	private String foFilepath;
	@Column(name = "review_meeting_comment")
	private String reviewMeetingComment;
	@Column(name = "user_id")
	private int userId;
	@Column(name = "assigned_from_user_id")
	private Integer assignedFromUserId;
	@Column(name = "assigned_to_user_id")
	private Integer assignedToUserId;
	@Column(name = "other_remarks")
	private String otherRemarks;
	@Column(name = "suggested_jurisdiction")
	private String suggestedJurisdiction;
	@Column(name = "transfer_file_path")
	private String transferFilePath;
	@Column(name = "other_remarks_recovery")
	private String otherRemarksRecovery;
	@Column(name = "transfer_remarks")
	private String transferRemarks;
	@Column(name = "extension_file_name")
	private String extensionFilename;
	@Column(name = "suggested_jurisdiction_name")
	private String suggestedJurisdictionName;
	private String assignedFrom;
	private String caseJurisdiction;
}
