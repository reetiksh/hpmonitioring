package com.hp.gstreviewfeedbackapp.enforcement.model;

import java.math.BigDecimal;

public class EnforcementDashboardSummaryDTO {
    private String zoneName;
    private String circleName;
    private BigDecimal indicativeTaxValue;
    private Long allottedCases;
    private Long casesCompleted;
    private Long notAcknowledged;
    private Long transferToScrutiny;
    private Long acknowledged;
    private Long panchnama;
    private Long preliminaryReport;
    private Long finalReport;
    private Long referToAdjudication;
    private Long showCauseIssued;
    private Integer sortOrder;
    private String zoneSort;
    
    
    
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getCircleName() {
		return circleName;
	}
	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
	public BigDecimal getIndicativeTaxValue() {
		return indicativeTaxValue;
	}
	public void setIndicativeTaxValue(BigDecimal indicativeTaxValue) {
		this.indicativeTaxValue = indicativeTaxValue;
	}
	public Long getAllottedCases() {
		return allottedCases;
	}
	public void setAllottedCases(Long allottedCases) {
		this.allottedCases = allottedCases;
	}
	public Long getCasesCompleted() {
		return casesCompleted;
	}
	public void setCasesCompleted(Long casesCompleted) {
		this.casesCompleted = casesCompleted;
	}
	public Long getNotAcknowledged() {
		return notAcknowledged;
	}
	public void setNotAcknowledged(Long notAcknowledged) {
		this.notAcknowledged = notAcknowledged;
	}
	public Long getTransferToScrutiny() {
		return transferToScrutiny;
	}
	public void setTransferToScrutiny(Long transferToScrutiny) {
		this.transferToScrutiny = transferToScrutiny;
	}
	public Long getAcknowledged() {
		return acknowledged;
	}
	public void setAcknowledged(Long acknowledged) {
		this.acknowledged = acknowledged;
	}
	public Long getPanchnama() {
		return panchnama;
	}
	public void setPanchnama(Long panchnama) {
		this.panchnama = panchnama;
	}
	public Long getPreliminaryReport() {
		return preliminaryReport;
	}
	public void setPreliminaryReport(Long preliminaryReport) {
		this.preliminaryReport = preliminaryReport;
	}
	public Long getFinalReport() {
		return finalReport;
	}
	public void setFinalReport(Long finalReport) {
		this.finalReport = finalReport;
	}
	public Long getReferToAdjudication() {
		return referToAdjudication;
	}
	public void setReferToAdjudication(Long referToAdjudication) {
		this.referToAdjudication = referToAdjudication;
	}
	public Long getShowCauseIssued() {
		return showCauseIssued;
	}
	public void setShowCauseIssued(Long showCauseIssued) {
		this.showCauseIssued = showCauseIssued;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getZoneSort() {
		return zoneSort;
	}
	public void setZoneSort(String zoneSort) {
		this.zoneSort = zoneSort;
	}
    
    
    
}
