package com.hp.gstreviewfeedbackapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "mst_user_role_mapping")
public class UserRoleMapping {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "mst_user_role_mapping_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	@Column(name = "user_role_mapping_id")
	private long userRoleMappingId;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "user_id")
	private UserDetails userDetails;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "user_role_id")
	private UserRole userRole;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "zone_id")
	private ZoneDetails zoneDetails;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "circle_id")
	private CircleDetails circleDetails;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "state_id")
	private StateDetails stateDetails;

	@Transient
	@OneToOne(mappedBy = "userRoleMapping", cascade = { CascadeType.ALL })
	private TransferRole transferRole;

	public TransferRole getTransferRole() {
		return transferRole;
	}

	public void setTransferRole(TransferRole transferRole) {
		this.transferRole = transferRole;
	}

	public UserRoleMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserRoleMapping(UserDetails userDetails, UserRole userRole, ZoneDetails zoneDetails,CircleDetails circleDetails, StateDetails stateDetails) {
		super();
		this.userDetails = userDetails;
		this.userRole = userRole;
		this.zoneDetails = zoneDetails;
		this.circleDetails = circleDetails;
		this.stateDetails = stateDetails;
	}

	public long getUserRoleMappingId() {
		return userRoleMappingId;
	}

	public void setUserRoleMappingId(long userRoleMappingId) {
		this.userRoleMappingId = userRoleMappingId;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public ZoneDetails getZoneDetails() {
		return zoneDetails;
	}

	public void setZoneDetails(ZoneDetails zoneDetails) {
		this.zoneDetails = zoneDetails;
	}

	public CircleDetails getCircleDetails() {
		return circleDetails;
	}

	public void setCircleDetails(CircleDetails circleDetails) {
		this.circleDetails = circleDetails;
	}

	public StateDetails getStateDetails() {
		return stateDetails;
	}

	public void setStateDetails(StateDetails stateDetails) {
		this.stateDetails = stateDetails;
	}

	@Override
	public String toString() {
		return "UserRoleMapping [userRoleMappingId=" + userRoleMappingId + ", userDetails=" + userDetails
				+ ", userRole=" + userRole + ", zoneDetails=" + zoneDetails + ", circleDetails=" + circleDetails 
				+ ", stateDetails=" + stateDetails + ", transferRole=" + transferRole + "]";
	}

}
