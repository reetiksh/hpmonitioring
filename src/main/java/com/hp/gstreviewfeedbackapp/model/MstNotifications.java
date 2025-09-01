package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "mst_notifications")
public class MstNotifications {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "sequence-generator")
	private Long id;

	@Column(name = "gstin")
	private String gstin;

	@Column(name = "caseReportingDate")
	private Date caseReportingDate;

	@Column(name = "period")
	private String period;

	@Column(name = "assignedTo")
	private String assignedTo;

	@Column(name = "action")
	private String action;

	@Column(name = "description")
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatingDate")
	private Date updatingDate;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "workingLocation")
	private LocationDetails locationDetails;

	@Column(name = "activeStatus")
	private Boolean activeStatus = true;

	@Column(name = "notificationPertainTo")
	private Integer notificationPertainTo;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "caseNotificationCategoryId")
	private MstCaseNotificationCategory caseNotificationCategory;

	@Column(name = "isDisplayed")
	private Boolean isDisplayed = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
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

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdatingDate() {
		return updatingDate;
	}

	public void setUpdatingDate(Date updatingDate) {
		this.updatingDate = updatingDate;
	}

	public LocationDetails getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(LocationDetails locationDetails) {
		this.locationDetails = locationDetails;
	}

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getNotificationPertainTo() {
		return notificationPertainTo;
	}

	public void setNotificationPertainTo(Integer notificationPertainTo) {
		this.notificationPertainTo = notificationPertainTo;
	}

	public MstCaseNotificationCategory getCaseNotificationCategory() {
		return caseNotificationCategory;
	}

	public void setCaseNotificationCategory(MstCaseNotificationCategory caseNotificationCategory) {
		this.caseNotificationCategory = caseNotificationCategory;
	}

	public Boolean getIsDisplayed() {
		return isDisplayed;
	}

	public void setIsDisplayed(Boolean isDisplayed) {
		this.isDisplayed = isDisplayed;
	}
}
