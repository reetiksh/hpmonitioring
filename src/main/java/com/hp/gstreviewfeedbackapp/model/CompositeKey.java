package com.hp.gstreviewfeedbackapp.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Embeddable
public class CompositeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "GSTIN")
	private String GSTIN;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "CASE_REPORTING_DATE")
	private Date caseReportingDate;

	@Column(name = "PERIOD")
	private String period;

	public CompositeKey() {
	}

	public CompositeKey(String GSTIN, Date caseReportingDate, String period) {
		this.GSTIN = GSTIN;
		this.caseReportingDate = caseReportingDate;
		this.period = period;
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CompositeKey that = (CompositeKey) o;
		return Objects.equals(GSTIN, that.GSTIN) && Objects.equals(caseReportingDate, that.caseReportingDate)
				&& Objects.equals(period, that.period);
	}

	@Override
	public int hashCode() {
		return Objects.hash(GSTIN, caseReportingDate, period);
	}

	@Override
	public String toString() {
		return "CompositeKey [GSTIN=" + GSTIN + ", Case Reporting Date="
				+ (caseReportingDate != null ? new SimpleDateFormat("dd-MM-yyyy").format(caseReportingDate) : "")
				+ ", Period=" + period + "]";
	}

}
