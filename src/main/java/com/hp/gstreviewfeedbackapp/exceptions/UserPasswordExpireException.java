package com.hp.gstreviewfeedbackapp.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserPasswordExpireException extends AuthenticationException{

	private static final long serialVersionUID = -885741393659698911L;

	public UserPasswordExpireException(String msg) {
		super(msg);
	}

}
