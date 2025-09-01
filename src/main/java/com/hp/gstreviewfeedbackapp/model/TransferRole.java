package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "admin_transfer_role")
public class TransferRole {

	@Id
	private Long id;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "transfer_role_id", referencedColumnName = "user_role_mapping_id")
	private UserRoleMapping userRoleMapping;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "transfer_to_user_id")
	private UserDetails transferToUser;

	@Column(name = "action_date")
	private Date actionDate;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "requested_by_user_id")
	private UserDetails requestedByUserId;

	public TransferRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransferRole(Long id, UserRoleMapping userRoleMapping, UserDetails transferToUser, Date actionDate,
			UserDetails requestedByUserId) {
		super();
		this.id = id;
		this.userRoleMapping = userRoleMapping;
		this.transferToUser = transferToUser;
		this.actionDate = actionDate;
		this.requestedByUserId = requestedByUserId;
	}

	public UserDetails getRequestedByUserId() {
		return requestedByUserId;
	}

	public void setRequestedByUserId(UserDetails requestedByUserId) {
		this.requestedByUserId = requestedByUserId;
	}

	public UserRoleMapping getUserRoleMapping() {
		return userRoleMapping;
	}

	public void setUserRoleMapping(UserRoleMapping userRoleMapping) {
		this.userRoleMapping = userRoleMapping;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDetails getTransferToUser() {
		return transferToUser;
	}

	public void setTransferToUser(UserDetails transferToUser) {
		this.transferToUser = transferToUser;
	}
}
