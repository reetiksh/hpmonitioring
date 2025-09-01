package com.hp.gstreviewfeedbackapp.audit.data;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.hp.gstreviewfeedbackapp.model.PdfData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class L3UserAuditCaseUpdate {
	private String caseId;
	private Integer updateStatusId;
	@DateTimeFormat(iso = ISO.DATE)
	private Date workingDate;
	private PdfData pdfData;
	private PdfData pdfDataReply;
	private List<List<String>> para;
	private String nilDar;
	private Long totalInvolvedAmount;
	private String darApproval;
	private String comment;
	private Integer foUserId;
	private PdfData pdfDataClosureReport;
	@DateTimeFormat(iso = ISO.DATE)
	private Date workingDateClosureReport;
	private String arn;
}
