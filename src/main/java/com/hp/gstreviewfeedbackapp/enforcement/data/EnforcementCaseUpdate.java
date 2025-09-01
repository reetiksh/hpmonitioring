package com.hp.gstreviewfeedbackapp.enforcement.data;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.hp.gstreviewfeedbackapp.model.PdfData;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnforcementCaseUpdate {
	private String GSTIN;
	@DateTimeFormat(iso = ISO.DATE)
	private Date caseReportingDate;
	private String period;
	@DateTimeFormat(iso = ISO.DATE)
	private Date workingDate;
	private PdfData pdfData;
	private Integer updateStatusId;
	private String location;
	private Integer assignedUser;
	private String caseId;
	
	 RecoveryStage recoveryStage;
	private List<String> recoveryStageArn;
}
