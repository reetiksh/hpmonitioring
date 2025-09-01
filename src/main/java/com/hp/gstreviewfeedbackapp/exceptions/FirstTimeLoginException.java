package com.hp.gstreviewfeedbackapp.exceptions;

import org.springframework.security.core.AuthenticationException;

public class FirstTimeLoginException extends AuthenticationException{

	private static final long serialVersionUID = -5031312991424699192L;

	public FirstTimeLoginException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
