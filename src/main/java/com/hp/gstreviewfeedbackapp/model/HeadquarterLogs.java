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
@Table(name = "headquarter_logs")
@Getter
@Setter
public class HeadquarterLogs {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "headquarter_logs_sequence"),
			@Parameter(name = "initial_value", value = "1000"), @Parameter(name = "increment_size", value = "1") })
	private Long id;

	@Column(name = "GSTIN")
	private String GSTIN;

	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;

	@Column(name = "PERIOD")
	private String period;

	@Column(name = "TAXPAYER_NAME")
	private String taxpayerName;

	@Column(name = "EXTENSION_NO")
	private String extensionNo;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "INDICATIVE_TAX_VALUE")
	private Long indicativeTaxValue;

	@Column(name = "action")
	private String action;

	@Column(name = "ASSIGNED_TO")
	private String assignedTo;

	@Column(name = "assignedFrom")
	private String assignedFrom;

	@Column(name = "assigned_from_userId")
	private Integer assignedFromUserId;

	@Column(name = "assigned_to_userId")
	private Integer assignedToUserId;

	@Column(name = "suggested_jurisdiction")
	private String suggestedJurisdiction;

	@Column(name = "workingLocation")
	private String workingLocation;

	@Column(name = "Remark")
	private String remark;

	@Column(name = "case_id")
	private String caseId;

	@Column(name = "EXTENSION_FILE_NAME")
	private String extensionFileName;

	@Column(name = "case_updating_time")
	private Date caseUpdatingTime;

	private String parameter;

	

}
