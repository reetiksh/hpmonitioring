package com.hp.gstreviewfeedbackapp.service.impl;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.model.AdminUserDetailsLogs;
import com.hp.gstreviewfeedbackapp.model.FileMaster;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.repository.AdminUserDetailsLogsRepository;
import com.hp.gstreviewfeedbackapp.repository.FileMasterRepository;
import com.hp.gstreviewfeedbackapp.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	private AdminUserDetailsLogsRepository adminUserDetailsLogsRepository;

	@Autowired
	private FileMasterRepository fileMasterRepository;

	public List<FileMaster> findFilePath(String year, String category) {
		try {
			return fileMasterRepository.findByYearAndCategory(year, category);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean checkDateExpiry(Date lastPasswordUpdationDate, int paswordExpiryDays) {
		try {
			if (lastPasswordUpdationDate == null) {
				return false;
			}
			Date todayDate = new Date();
			logger.info("");
			long diffInMillies = Math.abs(todayDate.getTime() - lastPasswordUpdationDate.getTime());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			System.out.println("Difference: " + diff);
			if (diff > paswordExpiryDays) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean checkTimeExpiry(Date date, int timeInMinutes) {
		try {
			// Current Date and time
			Date d2 = new Date();

			// Calculate time difference in milliseconds
			long difference_In_Time = d2.getTime() - date.getTime();

			// Calculate time difference in minutes
			long difference_In_Minutes = (difference_In_Time / (1000 * 60));

			if (difference_In_Minutes > Long.valueOf(timeInMinutes)) {
				return true;
			}
		}

		// Catch the Exception
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void findDifference(String start_date, String end_date) {

		// SimpleDateFormat converts the
		// string format to date object
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// Try Block
		try {

			// parse method is used to parse
			// the text from a string to
			// produce the date
			Date d1 = sdf.parse(start_date);
			Date d2 = sdf.parse(end_date);

			// Calculate time difference
			// in milliseconds
			long difference_In_Time = d2.getTime() - d1.getTime();

			// Calculate time difference in
			// seconds, minutes, hours, years,
			// and days
			long difference_In_Seconds = (difference_In_Time / 1000) % 60;

			long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

			long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

			long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

			long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

			// Print the date difference in
			// years, in days, in hours, in
			// minutes, and in seconds

			System.out.print("Difference " + "between two dates is: ");

			System.out.println(difference_In_Years + " years, " + difference_In_Days + " days, " + difference_In_Hours
					+ " hours, " + difference_In_Minutes + " minutes, " + difference_In_Seconds + " seconds");
		}

		// Catch the Exception
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String generateOTP() {
		// Length of the OTP
		int length = 6;

		// Generate random numbers using SecureRandom
		SecureRandom secureRandom = new SecureRandom();

		// Allowed characters for the OTP (you can adjust this if needed)
		String allowedChars = "0123456789"; // Include other characters if needed

		StringBuilder otp = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int randomIndex = secureRandom.nextInt(allowedChars.length());
			otp.append(allowedChars.charAt(randomIndex));
		}

		return otp.toString();
	}

	@Override
	public boolean checkIfTotalOtpCountExpired(UserDetails userDetails, int totalOtpSedingCountLimit,
			int totalOtpLimitExpiryUnlockTimeInMinutes) {
		if (userDetails.getTotalOtpCount() == null) {
			userDetails.setTotalOtpCount(0);
		}
		if (userDetails.getTotalOtpCount() < totalOtpSedingCountLimit) {
			return true;
		}
		if (userDetails.getTotalOtpCount() >= totalOtpSedingCountLimit) {
			if (checkTimeExpiry(userDetails.getOtpSendingTime(), totalOtpLimitExpiryUnlockTimeInMinutes)) {
				userDetails.setTotalOtpCount(0);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public void saveUserLogs(UserDetails userDetails, String action) {
		AdminUserDetailsLogs aLogs = new AdminUserDetailsLogs(userDetails.getLoginName(), userDetails.getMobileNumber(),
				userDetails.getFirstName(), userDetails.getMiddleName(), userDetails.getLastName(),
				userDetails.getEmailId(), userDetails.getDateOfBirth(), userDetails.getDesignation(),
				userDetails.getUserStatus(), action, userDetails, new Date(), action, userDetails);
		adminUserDetailsLogsRepository.save(aLogs);
	}
}
