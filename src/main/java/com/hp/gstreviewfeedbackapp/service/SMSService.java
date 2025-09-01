package com.hp.gstreviewfeedbackapp.service;

public interface SMSService {

	/**
	 * Send Single text SMS
	 * 
	 * @param templateID : Predefined Template ID
	 * @param content    : Predefined content with replaced parameter
	 * @param number     : Number to whom we want to send
	 * @return {@link String} response from Mobile Seva Gateway e.g. '402,MsgID =
	 *         150620161466003974245msdgsms' If anything went wrong u will receive
	 *         null value
	 * 
	 */

	public String sendSMS(String templateID, String content, String number);

}
