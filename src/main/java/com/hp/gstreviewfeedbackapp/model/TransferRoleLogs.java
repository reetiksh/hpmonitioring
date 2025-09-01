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
@Table(name = "admin_transfer_role_logs")
public class TransferRoleLogs {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "admin_transfer_role_logs_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	@Column(name = "Transfer_role_logs_id")
	private long transferRoleLogsId;

	@Column(name = "transfer_from_user_id")
	private Integer transferFromUserId;

	@Column(name = "user_role")
	private String userRoleCode;

	@Column(name = "location_name")
	private String locationName;

	@Column(name = "transfer_to_user_id")
	private Integer transferToUserId;

	@Column(name = "action_date")
	private Date actionDate;

	@Column(name = "requested_by_user_id")
	private Integer requestedByUserId;

	@Column(name = "action_taken_timing")
	private Date actionTiming;

	@Column(name = "action")
	private String action;

	public TransferRoleLogs() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransferRoleLogs(Integer transferFromUserId, String userRoleCode, String locationName,
			Integer transferToUserId, Date actionDate, Integer requestedByUserId, Date actionTiming, String action) {
		super();
		this.transferFromUserId = transferFromUserId;
		this.userRoleCode = userRoleCode;
		this.locationName = locationName;
		this.transferToUserId = transferToUserId;
		this.actionDate = actionDate;
		this.requestedByUserId = requestedByUserId;
		this.actionTiming = actionTiming;
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getActionTiming() {
		return actionTiming;
	}

	public void setActionTiming(Date actionTiming) {
		this.actionTiming = actionTiming;
	}

	public long getTransferRoleLogsId() {
		return transferRoleLogsId;
	}

	public void setTransferRoleLogsId(long transferRoleLogsId) {
		this.transferRoleLogsId = transferRoleLogsId;
	}

	public Integer getTransferFromUserId() {
		return transferFromUserId;
	}

	public void setTransferFromUserId(Integer transferFromUserId) {
		this.transferFromUserId = transferFromUserId;
	}

	public String getUserRoleCode() {
		return userRoleCode;
	}

	public void setUserRoleCode(String userRoleCode) {
		this.userRoleCode = userRoleCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Integer getTransferToUserId() {
		return transferToUserId;
	}

	public void setTransferToUserId(Integer transferToUserId) {
		this.transferToUserId = transferToUserId;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public Integer getRequestedByUserId() {
		return requestedByUserId;
	}

	public void setRequestedByUserId(Integer requestedByUserId) {
		this.requestedByUserId = requestedByUserId;
	}

}
