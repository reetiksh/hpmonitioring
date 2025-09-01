package com.hp.gstreviewfeedbackapp.data;

public class FoConsolidateCategoryWiseCaseData {
	
	private String period;
	private Long notApplicable;	// notApplicable = 'yet to be initiated'
	private Long dRC01AIssued;
	private Long dRC01Issued;
	private Long aSMT10Issued;
	private Long caseDropped;
	private Long demandCreatedByDrc07;
	private Long notAcknowledge;
	private Long partialVoluntaryPaymentRemainingDemand;
	private Long demandCreatedHoweverDischargedViaDrc03;
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Long getNotApplicable() {
		return notApplicable;
	}
	public void setNotApplicable(Long notApplicable) {
		this.notApplicable = notApplicable;
	}
	public Long getdRC01AIssued() {
		return dRC01AIssued;
	}
	public void setdRC01AIssued(Long dRC01AIssued) {
		this.dRC01AIssued = dRC01AIssued;
	}
	public Long getdRC01Issued() {
		return dRC01Issued;
	}
	public void setdRC01Issued(Long dRC01Issued) {
		this.dRC01Issued = dRC01Issued;
	}
	public Long getaSMT10Issued() {
		return aSMT10Issued;
	}
	public void setaSMT10Issued(Long aSMT10Issued) {
		this.aSMT10Issued = aSMT10Issued;
	}
	public Long getCaseDropped() {
		return caseDropped;
	}
	public void setCaseDropped(Long caseDropped) {
		this.caseDropped = caseDropped;
	}
	public Long getDemandCreatedByDrc07() {
		return demandCreatedByDrc07;
	}
	public void setDemandCreatedByDrc07(Long demandCreatedByDrc07) {
		this.demandCreatedByDrc07 = demandCreatedByDrc07;
	}
	public Long getNotAcknowledge() {
		return notAcknowledge;
	}
	public void setNotAcknowledge(Long notAcknowledge) {
		this.notAcknowledge = notAcknowledge;
	}
	public Long getPartialVoluntaryPaymentRemainingDemand() {
		return partialVoluntaryPaymentRemainingDemand;
	}
	public void setPartialVoluntaryPaymentRemainingDemand(Long partialVoluntaryPaymentRemainingDemand) {
		this.partialVoluntaryPaymentRemainingDemand = partialVoluntaryPaymentRemainingDemand;
	}
	public Long getDemandCreatedHoweverDischargedViaDrc03() {
		return demandCreatedHoweverDischargedViaDrc03;
	}
	public void setDemandCreatedHoweverDischargedViaDrc03(Long demandCreatedHoweverDischargedViaDrc03) {
		this.demandCreatedHoweverDischargedViaDrc03 = demandCreatedHoweverDischargedViaDrc03;
	}
}
