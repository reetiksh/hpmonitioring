package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "mst_user_role_mapping_logs")
public class UserRoleMappingLogs {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "mst_user_role_mapping_logs_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	@Column(name = "user_role_mapping_log_id")
	private long userRoleMappingLogId;

	@Column(name = "location_name")
	private String locationName;

	@Column(name = "action_taken_timing")
	private Date actionTiming;

	@Column(name = "requested_by_user_id")
	private Integer requestedByUserId;

	@Column(name = "user_role")
	private String userRoleCode;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "action_taken")
	private String actionTaken;

	@Column(name = "user_role_mapping_id")
	private Long userRoleMapping;

	public UserRoleMappingLogs(String locationName, Date actionTiming, Integer requestedByUserId, String userRoleCode,
			Integer userId, String actionTaken, Long userRoleMapping) {
		super();
		this.locationName = locationName;
		this.actionTiming = actionTiming;
		this.requestedByUserId = requestedByUserId;
		this.userRoleCode = userRoleCode;
		this.userId = userId;
		this.actionTaken = actionTaken;
		this.userRoleMapping = userRoleMapping;
	}

	public long getUserRoleMappingLogId() {
		return userRoleMappingLogId;
	}

	public void setUserRoleMappingLogId(long userRoleMappingLogId) {
		this.userRoleMappingLogId = userRoleMappingLogId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Date getActionTiming() {
		return actionTiming;
	}

	public void setActionTiming(Date actionTiming) {
		this.actionTiming = actionTiming;
	}

	public Integer getRequestedByUserId() {
		return requestedByUserId;
	}

	public void setRequestedByUserId(Integer requestedByUserId) {
		this.requestedByUserId = requestedByUserId;
	}

	public String getUserRoleCode() {
		return userRoleCode;
	}

	public void setUserRoleCode(String userRoleCode) {
		this.userRoleCode = userRoleCode;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public Long getUserRoleMapping() {
		return userRoleMapping;
	}

	public void setUserRoleMapping(Long userRoleMapping) {
		this.userRoleMapping = userRoleMapping;
	}
}
