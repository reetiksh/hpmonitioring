package com.hp.gstreviewfeedbackapp.model;

public class caseStageResultDTO {
	private Long caseStageCategory;
	private String caseStageName;
	private Long total;

	public Long getCaseStageCategory() {
		return caseStageCategory;
	}

	public void setCaseStageCategory(Long caseStageCategory) {
		this.caseStageCategory = caseStageCategory;
	}

	public String getCaseStageName() {
		return caseStageName;
	}

	public void setCaseStageName(String caseStageName) {
		this.caseStageName = caseStageName;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

}
