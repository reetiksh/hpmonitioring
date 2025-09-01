package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FieldOfficeAssistantUserLogs {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "field_office_assistant_logs_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;
	private String GSTIN;
	@Temporal(TemporalType.DATE)
	private Date caseReportingDate;
	private String period;
	private String taxpayerName;
	private String jurisdiction;
	private String category;
	private Date caseUpdatingDate;
	private Long indicativeTaxValue;
	private String action;
	private Integer actionStatus;
	private Integer caseStage;
	private Long demand;
	private Integer recoveryStage;
	private Long recoveryByDRC3;
	private Long recoveryAgainstDemand;
	private String caseId;
	private String caseStageArn;
	private String recoveryStageArn;
	private String assignedTo;
	private String foFilepath;
	private String reviewMeetingComment;
	private Integer userId;
	private Integer assignedFromUserId;
	private Integer assignedToUserId;
	private String otherRemarks;
	private String otherRemarksRecovery;
	private String extensionFilename;
	private String assignedFrom;
	private String caseJurisdiction;
}
