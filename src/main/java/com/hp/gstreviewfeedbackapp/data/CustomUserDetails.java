package com.hp.gstreviewfeedbackapp.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomUserDetails {
	private Integer userId;
	private String userName;
	private String designation;
	private String loginId;
	
	public CustomUserDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomUserDetails(Integer userId, String userName, String designation, String loginId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.designation = designation;
		this.loginId = loginId;
	}
	
}
