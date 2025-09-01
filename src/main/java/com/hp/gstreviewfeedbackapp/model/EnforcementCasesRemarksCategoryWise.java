package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "enforcement_remarks_category_wise")
public class EnforcementCasesRemarksCategoryWise {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "enforcement_remarks_category_wise_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	private Long id;
	private Date recordCreationDate;
	private Date reviewMeetingDate;
	private String momDocument;
	@Column(length = 5000)
	private String remarks;
	private Long categoryId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRecordCreationDate() {
		return recordCreationDate;
	}

	public void setRecordCreationDate(Date recordCreationDate) {
		this.recordCreationDate = recordCreationDate;
	}

	public Date getReviewMeetingDate() {
		return reviewMeetingDate;
	}

	public void setReviewMeetingDate(Date reviewMeetingDate) {
		this.reviewMeetingDate = reviewMeetingDate;
	}

	public String getMomDocument() {
		return momDocument;
	}

	public void setMomDocument(String momDocument) {
		this.momDocument = momDocument;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "EnforcementCasesRemarksCategoryWise [id=" + id + ", recordCreationDate=" + recordCreationDate
				+ ", reviewMeetingDate=" + reviewMeetingDate + ", momDocument=" + momDocument + ", remarks=" + remarks
				+ ", categoryId=" + categoryId + "]";
	}

}
