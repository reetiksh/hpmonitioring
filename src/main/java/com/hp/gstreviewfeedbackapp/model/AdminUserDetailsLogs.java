package com.hp.gstreviewfeedbackapp.model;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "admin_user_details_logs")
public class AdminUserDetailsLogs {
	
	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "admin_user_details_logs_sequence"),
        @Parameter(name = "initial_value", value = "200"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	private Long id;
	
	@Column(name = "login_id")
	private String loginId;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "middle_name")
	private String middleName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@JoinColumn(name = "designation_id")
	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private Designation designation;
	
	@Column(name = "user_status")
	private String userStatus;
	
	@Column(name="reason_for_update")
	private String reasonForUpdate;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private UserDetails userDetails;
	
	@Column(name = "updating_time")
	private Date updatingTime;
	
	@Column(name = "action")
	private String action;
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "admin_user_id")
	private UserDetails adminUserDetails;
	
	public AdminUserDetailsLogs() {
		super();
	}

	public AdminUserDetailsLogs(String loginId, String mobileNumber, String firstName, String middleName,
			String lastName, String emailId, Date dateOfBirth, Designation designation, String userStatus,
			String reasonForUpdate, UserDetails userDetails, Date updatingTime, String action,
			UserDetails adminUserDetails) {
		super();
		this.loginId = loginId;
		this.mobileNumber = mobileNumber;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.dateOfBirth = dateOfBirth;
		this.designation = designation;
		this.userStatus = userStatus;
		this.reasonForUpdate = reasonForUpdate;
		this.userDetails = userDetails;
		this.updatingTime = updatingTime;
		this.action = action;
		this.adminUserDetails = adminUserDetails;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public UserDetails getAdminUserDetails() {
		return adminUserDetails;
	}

	public void setAdminUserDetails(UserDetails adminUserDetails) {
		this.adminUserDetails = adminUserDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getReasonForUpdate() {
		return reasonForUpdate;
	}

	public void setReasonForUpdate(String reasonForUpdate) {
		this.reasonForUpdate = reasonForUpdate;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public Date getUpdatingTime() {
		return updatingTime;
	}

	public void setUpdatingTime(Date updatingTime) {
		this.updatingTime = updatingTime;
	}

}
