package com.hp.gstreviewfeedbackapp.model;

public class CategoryTotal {

	private String category;
	private Long totalRows;
	private Long totalIndicativeTax;
	private Long totalDemand;
	private Long totalRecovery;
	private Long totalAmount;

	public CategoryTotal() {

	}

	public CategoryTotal(String category, Long totalRows, Long totalIndicativeTax, Long totalAmount,
			Long totalRecovery) {
		super();
		this.category = category;
		this.totalRows = totalRows;
		this.totalIndicativeTax = totalIndicativeTax;
		this.totalRecovery = totalRecovery;
		this.totalAmount = totalAmount;
	}

	public CategoryTotal(String category, Long totalRows, Long totalIndicativeTax, Long totalAmount, Long totalRecovery,
			long totalDemand) {
		this.category = category;
		this.totalRows = totalRows;
		this.totalIndicativeTax = totalIndicativeTax;
		this.totalDemand = totalDemand;
		this.totalRecovery = totalRecovery;
		this.totalAmount = totalAmount;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Long totalRows) {
		this.totalRows = totalRows;
	}

	public Long getTotalIndicativeTax() {
		return totalIndicativeTax;
	}

	public void setTotalIndicativeTax(Long totalIndicativeTax) {
		this.totalIndicativeTax = totalIndicativeTax;
	}

	public Long getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(Long totalDemand) {
		this.totalDemand = totalDemand;
	}

	public Long getTotalRecovery() {
		return totalRecovery;
	}

	public void setTotalRecovery(Long totalRecovery) {
		this.totalRecovery = totalRecovery;
	}

	@Override
	public String toString() {
		return "CategoryTotal [category=" + category + ", totalRows=" + totalRows + ", totalIndicativeTax="
				+ totalIndicativeTax + ", totalDemand=" + totalDemand + ", totalRecovery=" + totalRecovery
				+ ", totalAmount=" + totalAmount + "]";
	}

}
