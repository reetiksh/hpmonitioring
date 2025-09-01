package com.hp.gstreviewfeedbackapp.cag.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "cag_linked_cases")
public class CagLinkedCases {
	
	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "cag_linked_cases_sequence"),
        @Parameter(name = "initial_value", value = "1"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	private Integer id;
	
	@Column(name = "GSTIN")
	private String GSTIN;
	
	@DateTimeFormat(iso=ISO.DATE)
	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;
	
	@Column(name = "PERIOD")
	private String period;
	
	@Column(name = "parameter")
	private String parameter;
	
	private String module;
	
	@Column(name = "Linked_GSTIN")
	private String LinkedGSTIN;
	
	@DateTimeFormat(iso=ISO.DATE)
	@Column(name = "Linked_CASE_REPORTING_DATE")
	private Date LinkedcaseReportingDate;
	
	@Column(name = "Linked_PERIOD")
	private String Linkedperiod;
	
	@Column(name = "Linked_CaseId")
	private String LinkedCaseId;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(String gSTIN) {
		GSTIN = gSTIN;
	}

	public Date getCaseReportingDate() {
		return caseReportingDate;
	}

	public void setCaseReportingDate(Date caseReportingDate) {
		this.caseReportingDate = caseReportingDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String isModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getLinkedGSTIN() {
		return LinkedGSTIN;
	}

	public void setLinkedGSTIN(String linkedGSTIN) {
		LinkedGSTIN = linkedGSTIN;
	}

	public Date getLinkedcaseReportingDate() {
		return LinkedcaseReportingDate;
	}

	public void setLinkedcaseReportingDate(Date linkedcaseReportingDate) {
		LinkedcaseReportingDate = linkedcaseReportingDate;
	}

	public String getLinkedperiod() {
		return Linkedperiod;
	}

	public void setLinkedperiod(String linkedperiod) {
		Linkedperiod = linkedperiod;
	}

	public String getLinkedCaseId() {
		return LinkedCaseId;
	}

	public void setLinkedCaseId(String linkedCaseId) {
		LinkedCaseId = linkedCaseId;
	}

	public String getModule() {
		return module;
	}

	
}
