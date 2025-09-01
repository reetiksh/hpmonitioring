package com.hp.gstreviewfeedbackapp.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.model.SMSProperties;
import com.hp.gstreviewfeedbackapp.model.SMSTemplate;
import com.hp.gstreviewfeedbackapp.repository.SMSMasterRepository;
import com.hp.gstreviewfeedbackapp.repository.SMSTemplateRepository;
import com.hp.gstreviewfeedbackapp.service.SMSService;

@Service
public class SMSServiceImpl implements SMSService {

	private static final Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);

	@Autowired
	private SMSMasterRepository smsMasterRepo;

	@Autowired
	private SMSTemplateRepository smsTemplRepo;

	@Override
	public String sendSMS(String templateID, String content, String number) {
		Optional<SMSProperties> prop = smsMasterRepo.findById("aHBnb3Z0LWVnc3Q=");
		Optional<SMSTemplate> smsTempl = smsTemplRepo.findById(templateID);
		if (prop.isPresent()) {
			if (smsTempl.isPresent()) {
				return sendSingleSMS(new String(Base64.getDecoder().decode(prop.get().getUsername())),
						new String(Base64.getDecoder().decode(prop.get().getPassword())), content,
						new String(Base64.getDecoder().decode(prop.get().getSenderId())), number,
						new String(Base64.getDecoder().decode(prop.get().getSecureKey())),
						smsTempl.get().getTemplateId());
			} else {

				logger.error("Something went wrong! Provided template properties are not avaliable");
				return null;
			}
		} else {
			logger.error("Something went wrong! User properties are not avaliable");
			return null;
		}
	}

	private String sendSingleSMS(String username, String password, String message, String senderId, String mobileNumber,
			String secureKey, String templateid) {

		String responseString = "";
		SSLSocketFactory sf = null;
		SSLContext context = null;
		String encryptedPassword;
		try {
//context=SSLContext.getInstance("TLSv1.1"); // Use this line for Java version 6
			context = SSLContext.getInstance("TLSv1.2"); // Use this line for Java version 7 and above
			context.init(null, null, null);
			sf = new SSLSocketFactory(context, SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			Scheme scheme = new Scheme("https", 443, sf);
			HttpClient client = new DefaultHttpClient();
			client.getConnectionManager().getSchemeRegistry().register(scheme);
			HttpPost post = new HttpPost("https://msdgweb.mgov.gov.in/esms/sendsmsrequestDLT");
			encryptedPassword = MD5(password);
			String genratedhashKey = hashGenerator(username, senderId, message, secureKey);
			List nameValuePairs = new ArrayList(1);
			nameValuePairs.add(new BasicNameValuePair("mobileno", mobileNumber));
			nameValuePairs.add(new BasicNameValuePair("senderid", senderId));
			nameValuePairs.add(new BasicNameValuePair("content", message));
			nameValuePairs.add(new BasicNameValuePair("smsservicetype", "singlemsg"));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", encryptedPassword));
			nameValuePairs.add(new BasicNameValuePair("key", genratedhashKey));
			nameValuePairs.add(new BasicNameValuePair("templateid", templateid));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			BufferedReader bf = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = bf.readLine()) != null) {
				responseString = responseString + line;

			}
			System.out.println(responseString);
		} catch (NoSuchAlgorithmException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;
	}

	private String hashGenerator(String userName, String senderId, String content, String secureKey) {
// TODO Auto-generated method stub
		StringBuffer finalString = new StringBuffer();
		finalString.append(userName.trim()).append(senderId.trim()).append(content.trim()).append(secureKey.trim());
// logger.info("Parameters for SHA-512 : "+finalString);
		String hashGen = finalString.toString();
		StringBuffer sb = null;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(hashGen.getBytes());
			byte byteData[] = md.digest();
//convert the byte to hex format method 1
			sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

		} catch (NoSuchAlgorithmException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] md5 = new byte[64];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5 = md.digest();
		return convertedToHex(md5);
	}

	private static String convertedToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < data.length; i++) {
			int halfOfByte = (data[i] >>> 4) & 0x0F;
			int twoHalfBytes = 0;

			do {
				if ((0 <= halfOfByte) && (halfOfByte <= 9)) {
					buf.append((char) ('0' + halfOfByte));
				}

				else {
					buf.append((char) ('a' + (halfOfByte - 10)));
				}

				halfOfByte = data[i] & 0x0F;

			} while (twoHalfBytes++ < 1);
		}
		return buf.toString();
	}

	public String extractStringAfter(String original, String searchString) {
		int index = original.indexOf(searchString);

		if (index != -1) {
			// Add the length of the searchString to get the substring after it
			return original.substring(index + searchString.length()).trim();
		} else {
			return null; // Substring not found
		}
	}

}