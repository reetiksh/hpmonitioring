package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

public class UserProfileDetails {

	private String userName;
	private String mob;
	private String emailId;
	private Date dob;
	private String firstName;
	private String lastName;
	private String designation;
	private String assignedWorkingLocations;
	private String assignedRoles;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getAssignedWorkingLocations() {
		return assignedWorkingLocations;
	}

	public void setAssignedWorkingLocations(String assignedWorkingLocations) {
		this.assignedWorkingLocations = assignedWorkingLocations;
	}

	public String getAssignedRoles() {
		return assignedRoles;
	}

	public void setAssignedRoles(String assignedRoles) {
		this.assignedRoles = assignedRoles;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
}
