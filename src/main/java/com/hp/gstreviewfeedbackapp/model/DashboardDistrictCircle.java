package com.hp.gstreviewfeedbackapp.model;

public class DashboardDistrictCircle {

	private String juristiction;

	private Long noOfFos;

	private Long noOfCase;

	private Long indicativeValue;

	private Long demand;

	private Long recoveredAmount;

	private Long drc01aissued;

	private Long amst10issued;

	private Long drc01issued;

	private Long notAcknowledege;

	private Long demandByDrc07;

	private Long caseDroped;

	private Long notApplicable;

	private Long partialVoluentryDemand;

	private Long demandDischageByDrc03;

	private Long totalClosed;

	public Long getNoOfFos() {
		return noOfFos;
	}

	public void setNoOfFos(Long noOfFos) {
		this.noOfFos = noOfFos;
	}

	public DashboardDistrictCircle(String juristiction, Long noOfCase, Long indicativeValue, Long demand,
			Long recoveredAmount, Long drc01aissued, Long amst10issued, Long drc01issued, Long notAcknowledege,
			Long demandByDrc07, Long caseDroped, Long notApplicable, Long partialVoluentryDemand,
			Long demandDischageByDrc03, Long totalClosed) {
		super();
		this.juristiction = juristiction;
		this.noOfCase = noOfCase;
		this.indicativeValue = indicativeValue;
		this.demand = demand;
		this.recoveredAmount = recoveredAmount;
		this.drc01aissued = drc01aissued;
		this.amst10issued = amst10issued;
		this.drc01issued = drc01issued;
		this.notAcknowledege = notAcknowledege;
		this.demandByDrc07 = demandByDrc07;
		this.caseDroped = caseDroped;
		this.notApplicable = notApplicable;
		this.partialVoluentryDemand = partialVoluentryDemand;
		this.demandDischageByDrc03 = demandDischageByDrc03;
		this.totalClosed = totalClosed;
	}

	public Long getDrc01aissued() {
		return drc01aissued;
	}

	public void setDrc01aissued(Long drc01aissued) {
		this.drc01aissued = drc01aissued;
	}

	public String getJuristiction() {
		return juristiction;
	}

	public void setJuristiction(String juristiction) {
		this.juristiction = juristiction;
	}

	public Long getNoOfCase() {
		return noOfCase;
	}

	public void setNoOfCase(Long noOfCase) {
		this.noOfCase = noOfCase;
	}

	public Long getIndicativeValue() {
		return indicativeValue;
	}

	public void setIndicativeValue(Long indicativeValue) {
		this.indicativeValue = indicativeValue;
	}

	public Long getDemand() {
		return demand;
	}

	public void setDemand(Long demand) {
		this.demand = demand;
	}

	public Long getRecoveredAmount() {
		return recoveredAmount;
	}

	public void setRecoveredAmount(Long recoveredAmount) {
		this.recoveredAmount = recoveredAmount;
	}

	public Long getDrc01issued() {
		return drc01issued;
	}

	public void setDrc01issued(Long drc01issued) {
		this.drc01issued = drc01issued;
	}

	public Long getAmst10issued() {
		return amst10issued;
	}

	public void setAmst10issued(Long amst10issued) {
		this.amst10issued = amst10issued;
	}

	public Long getNotAcknowledege() {
		return notAcknowledege;
	}

	public void setNotAcknowledege(Long notAcknowledege) {
		this.notAcknowledege = notAcknowledege;
	}

	public Long getDemandByDrc07() {
		return demandByDrc07;
	}

	public void setDemandByDrc07(Long demandByDrc07) {
		this.demandByDrc07 = demandByDrc07;
	}

	public Long getCaseDroped() {
		return caseDroped;
	}

	public void setCaseDroped(Long caseDroped) {
		this.caseDroped = caseDroped;
	}

	public Long getNotApplicable() {
		return notApplicable;
	}

	public void setNotApplicable(Long notApplicable) {
		this.notApplicable = notApplicable;
	}

	public Long getPartialVoluentryDemand() {
		return partialVoluentryDemand;
	}

	public void setPartialVoluentryDemand(Long partialVoluentryDemand) {
		this.partialVoluentryDemand = partialVoluentryDemand;
	}

	public Long getDemandDischageByDrc03() {
		return demandDischageByDrc03;
	}

	public void setDemandDischageByDrc03(Long demandDischageByDrc03) {
		this.demandDischageByDrc03 = demandDischageByDrc03;
	}

	public Long getTotalClosed() {
		return totalClosed;
	}

	public void setTotalClosed(Long totalClosed) {
		this.totalClosed = totalClosed;
	}

}
