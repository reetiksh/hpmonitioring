package com.hp.gstreviewfeedbackapp.data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseLogHistory {
	private String GSTIN;
	private Date caseReportingDate;
	private String period;
	private String assignedFrom;
	private String assignedTo;
	private Date updatingDate;
	private String action;
	private String suggestedJurisdiction;
	private String transferRemarks;
	private String otherRemarks;
	private String assignedFromUser;
	private String assigntoUser;
	private String assignedFromLocation;
	private String assignedToLocation;
	private String verifierRaiseQueryRemarks;
	private String approverRemarksToRejectTheCase;
	private String taxpayerName;
	private String category;
	private Long indicativeTaxValue;
	private String actionStatus;
	private String caseStage;
	private Long demand;
	private String recoveryStage;
	private Long recoveryByDRC3;
	private Long recoveryAgainstDemand;
	private String caseId;
	private String caseStageArn;
	private String recoveryStageArn;
	private String reviewMeetingComment;
	private String parameter;

	@Override
	public String toString() {
		return "CaseLogHistory [GSTIN=" + GSTIN + ", caseReportingDate=" + caseReportingDate + ", period=" + period
				+ ", assignedFrom=" + assignedFrom + ", assignedTo=" + assignedTo + ", updatingDate=" + updatingDate
				+ ", action=" + action + ", suggestedJurisdiction=" + suggestedJurisdiction + ", transferRemarks="
				+ transferRemarks + ", otherRemarks=" + otherRemarks + ", assignedFromUser=" + assignedFromUser
				+ ", assigntoUser=" + assigntoUser + ", assignedFromLocation=" + assignedFromLocation
				+ ", assignedToLocation=" + assignedToLocation + ", verifierRaiseQueryRemarks="
				+ verifierRaiseQueryRemarks + ", approverRemarksToRejectTheCase=" + approverRemarksToRejectTheCase
				+ ", taxpayerName=" + taxpayerName + ", category=" + category + ", indicativeTaxValue="
				+ indicativeTaxValue + ", actionStatus=" + actionStatus + ", caseStage=" + caseStage + ", demand="
				+ demand + ", recoveryStage=" + recoveryStage + ", recoveryByDRC3=" + recoveryByDRC3
				+ ", recoveryAgainstDemand=" + recoveryAgainstDemand + ", caseId=" + caseId + ", caseStageArn="
				+ caseStageArn + ", recoveryStageArn=" + recoveryStageArn + ", reviewMeetingComment="
				+ reviewMeetingComment + ", parameter=" + parameter + "]";
	}
}
