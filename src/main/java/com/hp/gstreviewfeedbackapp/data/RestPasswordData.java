package com.hp.gstreviewfeedbackapp.data;

public class RestPasswordData {
	private String loginName;
	private String oldPassword;
	private String newPassword;
	private String otp;
	private String otpEncd;
	private String otpTime;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getOtpEncd() {
		return otpEncd;
	}
	public void setOtpEncd(String otpEncd) {
		this.otpEncd = otpEncd;
	}
	public String getOtpTime() {
		return otpTime;
	}
	public void setOtpTime(String otpTime) {
		this.otpTime = otpTime;
	}
	
}
