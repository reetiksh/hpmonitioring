package com.hp.gstreviewfeedbackapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "user_details")
public class UserDetails {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "user_details_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	private Integer userId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "login_name")
	private String loginName;

	@Column(name = "password")
	private String password;

	@Column(name = "office_phone")
	private String officePhone;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "user_status")
	private String userStatus;

	@Column(name = "password_updation_date")
	private Date passwordUpdationDate;

	@Column(name = "lastLoginDate")
	private Date lastLoginDate;

	@Column(name = "wrongPswrdAtmpt")
	private Integer wrongPasswordAttempt;

	@Column(name = "userLockedTime")
	private Date userLockedTime;

	@Column(name = "otpEncd")
	private String otpEncd;

	@Column(name = "otpSendingTime")
	private Date otpSendingTime;

	@Column(name = "totalOtpCount")
	private Integer totalOtpCount = 0;

	@Column(name = "incorrect_otp_attempts")
	private Integer incorrectOtpAttempts = 0;

	@JoinColumn(name = "designation_id")
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Designation designation;

	@OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserRoleMapping> userRoleMappings = new ArrayList<>();

	@OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdminUserDetailsLogs> adminUserDetailsLogsList = new ArrayList<>();

	public List<UserRoleMapping> getUserRoleMappings() {
		return userRoleMappings;
	}

	public void setUserRoleMappings(List<UserRoleMapping> userRoleMappings) {
		this.userRoleMappings = userRoleMappings;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
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

	public Date getPasswordUpdationDate() {
		return passwordUpdationDate;
	}

	public void setPasswordUpdationDate(Date passwordUpdationDate) {
		this.passwordUpdationDate = passwordUpdationDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Integer getWrongPasswordAttempt() {
		return wrongPasswordAttempt;
	}

	public void setWrongPasswordAttempt(Integer wrongPasswordAttempt) {
		this.wrongPasswordAttempt = wrongPasswordAttempt;
	}

	public Date getUserLockedTime() {
		return userLockedTime;
	}

	public void setUserLockedTime(Date userLockedTime) {
		this.userLockedTime = userLockedTime;
	}

	public String getOtpEncd() {
		return otpEncd;
	}

	public void setOtpEncd(String otpEncd) {
		this.otpEncd = otpEncd;
	}

	public Date getOtpSendingTime() {
		return otpSendingTime;
	}

	public void setOtpSendingTime(Date otpSendingTime) {
		this.otpSendingTime = otpSendingTime;
	}

	public Integer getTotalOtpCount() {
		return totalOtpCount;
	}

	public void setTotalOtpCount(Integer totalOtpCount) {
		this.totalOtpCount = totalOtpCount;
	}

	public Integer getIncorrectOtpAttempts() {
		return incorrectOtpAttempts;
	}

	public void setIncorrectOtpAttempts(Integer incorrectOtpAttempts) {
		this.incorrectOtpAttempts = incorrectOtpAttempts;
	}

	public List<AdminUserDetailsLogs> getAdminUserDetailsLogsList() {
		return adminUserDetailsLogsList;
	}

	public void setAdminUserDetailsLogsList(List<AdminUserDetailsLogs> adminUserDetailsLogsList) {
		this.adminUserDetailsLogsList = adminUserDetailsLogsList;
	}

	@Override
	public String toString() {
		return "UserDetails [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", middleName="
				+ middleName + ", mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", loginName=" + loginName
				+ ", password=" + password + ", officePhone=" + officePhone + ", dateOfBirth=" + dateOfBirth
				+ ", userStatus=" + userStatus + ", passwordUpdationDate=" + passwordUpdationDate + ", lastLoginDate="
				+ lastLoginDate + ", wrongPasswordAttempt=" + wrongPasswordAttempt + ", userLockedTime="
				+ userLockedTime + ", otpEncd=" + otpEncd + ", otpSendingTime=" + otpSendingTime + ", totalOtpCount="
				+ totalOtpCount + ", incorrectOtpAttempts=" + incorrectOtpAttempts + ", designation=" + designation
				+ ", userRoleMappings=" + userRoleMappings + ", adminUserDetailsLogsList=" + adminUserDetailsLogsList
				+ "]";
	}

}
