package com.hp.gstreviewfeedbackapp.model;

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

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "case_workflow")
@Getter
@Setter
public class CaseWorkflow {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "case_workflow_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	private long id;
	@Column(name = "GSTIN")
	private String GSTIN;
	@Temporal(TemporalType.DATE)
	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;
	@Column(name = "PERIOD")
	private String period;
	@Column(name = "ASSIGNED_FROM")
	private String assignedFrom;
	@Column(name = "ASSIGNED_TO")
	private String assignedTo;
	@Column(name = "updating_date")
	private Date updatingDate;
	@Column(name = "action")
	private String action;
	@Column(name = "suggested_jurisdiction")
	private String suggestedJurisdiction;
	@Column(name = "verifier_appeal_revision_file")
	private String ruAppealRevisionFile;
	@Column(name = "reverted_case_file_path")
	private String apRevertedCaseFile;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "remarks")
	private TransferRemarks remarks;
	@Column(name = "other_remarks")
	private String otherRemarks;
	@Column(name = "assigned_from_user_id")
	private Integer assignedFromUserId;
	@Column(name = "assigned_to_user_id")
	private Integer assigntoUserId;
	@Column(name = "assigned_from_location_id")
	private String assignedFromLocationId;
	@Column(name = "assigned_to_location_id")
	private String assignedToLocationId;
	@Column(name = "transfer_file_path")
	private String transferFilePath;
	private String status;
	private String recoveryStatus;

	public String getRecoveryStatus() {
		return recoveryStatus;
	}

	public void setRecoveryStatus(String recoveryStatus) {
		this.recoveryStatus = recoveryStatus;
	}

	public String getTransferFilePath() {
		return transferFilePath;
	}

	public void setTransferFilePath(String transferFilePath) {
		this.transferFilePath = transferFilePath;
	}

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "verifier_raise_query_remarks")
	private VerifierRaiseQueryRemarks verifierRaiseQueryRemarks;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "approver_reject_case_remarks")
	private ApproverRemarksToRejectTheCase approverRemarksToRejectTheCase;
	// New Column for strings
	private String assignedFromLocationName;
	private String assignedToLocationName;
	private String suggestedJurisdictionName;
	private String parameter;

	@Override
	public String toString() {
		return "CaseWorkflow [id=" + id + ", GSTIN=" + GSTIN + ", caseReportingDate=" + caseReportingDate + ", period="
				+ period + ", assignedFrom=" + assignedFrom + ", assignedTo=" + assignedTo + ", updatingDate="
				+ updatingDate + ", action=" + action + ", suggestedJurisdiction=" + suggestedJurisdiction
				+ ", ruAppealRevisionFile=" + ruAppealRevisionFile + ", apRevertedCaseFile=" + apRevertedCaseFile
				+ ", remarks=" + remarks + ", otherRemarks=" + otherRemarks + ", assignedFromUserId="
				+ assignedFromUserId + ", assigntoUserId=" + assigntoUserId + ", assignedFromLocationId="
				+ assignedFromLocationId + ", assignedToLocationId=" + assignedToLocationId + ", transferFilePath="
				+ transferFilePath + ", status=" + status + ", recoveryStatus=" + recoveryStatus
				+ ", verifierRaiseQueryRemarks=" + verifierRaiseQueryRemarks + ", approverRemarksToRejectTheCase="
				+ approverRemarksToRejectTheCase + ", assignedFromLocationName=" + assignedFromLocationName
				+ ", assignedToLocationName=" + assignedToLocationName + ", suggestedJurisdictionName="
				+ suggestedJurisdictionName + ", parameter=" + parameter + "]";
	}
}
