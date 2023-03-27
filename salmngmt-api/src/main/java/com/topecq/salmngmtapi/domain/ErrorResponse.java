package com.topecq.salmngmtapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ErrorResponse {
	
	private String message;
	private String error;
	private int status;
	
	public ErrorResponse(String message, String error, int status) {
		super();
		this.message = message;
		this.error = error;
		this.status = status;
	}
	
}
