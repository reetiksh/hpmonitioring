package com.hp.gstreviewfeedbackapp.cag.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Embeddable
public class CagCompositeKey implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "GSTIN")
	private String GSTIN;
	
	@DateTimeFormat(iso=ISO.DATE)
	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;
	
	@Column(name = "PERIOD")
	private String period;
	
	@Column(name = "parameter")
	private String parameter;
	
	public CagCompositeKey() {
   
	}

	public CagCompositeKey(String gSTIN, Date caseReportingDate, String period, String parameter) {
		super();
		GSTIN = gSTIN;
		this.caseReportingDate = caseReportingDate;
		this.period = period;
		this.parameter = parameter;
	}

	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
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

	@Override
	public int hashCode() {
		return Objects.hash(GSTIN, caseReportingDate, parameter, period);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CagCompositeKey other = (CagCompositeKey) obj;
		return Objects.equals(GSTIN, other.GSTIN) && Objects.equals(caseReportingDate, other.caseReportingDate)
				&& Objects.equals(parameter, other.parameter) && Objects.equals(period, other.period);
	}
    
    
    
    

	

	
}
