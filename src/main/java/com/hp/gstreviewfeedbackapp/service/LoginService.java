package com.hp.gstreviewfeedbackapp.service;

import java.util.Date;

import com.hp.gstreviewfeedbackapp.model.UserDetails;

public interface LoginService {

	public boolean checkDateExpiry(Date lastPasswordUpdationDate, int paswordExpiryDays);

	public boolean checkTimeExpiry(Date date, int timeInMinutes);

	public void findDifference(String start_date, String end_date);

	public String generateOTP();

	public boolean checkIfTotalOtpCountExpired(UserDetails userDetails, int totalOtpSedingCountLimit,
			int totalOtpLimitExpiryUnlockTimeInMinutes);

	public void saveUserLogs(UserDetails userDetails, String action);

}
