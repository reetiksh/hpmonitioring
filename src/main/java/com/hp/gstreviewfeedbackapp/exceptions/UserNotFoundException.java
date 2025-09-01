package com.hp.gstreviewfeedbackapp.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException{

	private static final long serialVersionUID = 8054129980286537587L;

	public UserNotFoundException(String msg) {
		super(msg);
	}

}
