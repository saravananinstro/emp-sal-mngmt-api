package com.topecq.salmngmtapi.domain;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO<T extends Serializable> implements Serializable {

		private T data;
		private String message;
		private String errorCode;
		private HttpStatus status;
		
}
