package com.hp.gstreviewfeedbackapp.data;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelfDetectedCase {
	private String gstIn;
	private String taxpayerName;
	@DateTimeFormat(iso = ISO.DATE)
	private Date caseReportingDate;
	private String suspectedIndicativeTaxValue;
	private String period;
	private String assignedToCircle;
	private String category;
	private String remark;
	private String otherRemarks;
	private String caseId;
}
