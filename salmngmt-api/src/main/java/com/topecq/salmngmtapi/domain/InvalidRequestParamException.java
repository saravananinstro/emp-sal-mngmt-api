package com.topecq.salmngmtapi.domain;

public class InvalidRequestParamException extends Exception {

	public InvalidRequestParamException(String errorMessage) {
		super(errorMessage);
	}
}
