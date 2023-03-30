package com.topecq.salmngmtapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topecq.salmngmtapi.domain.EmpSalary;
import com.topecq.salmngmtapi.domain.EmpSalaryFileUploadException;
import com.topecq.salmngmtapi.domain.ErrorResponse;
import com.topecq.salmngmtapi.domain.InvalidRequestParamException;
import com.topecq.salmngmtapi.service.EmpSalaryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EmpSalaryController {
	
	@Autowired
	private EmpSalaryService empSalService;
	
	@PostMapping(value = "/users/upload")
	public ResponseEntity<?> uploadEmployeeSalary(@RequestParam("file") MultipartFile file) {
		
		log.info("In uploadEmployeeSalary");
		
		Map<String, String> map = new HashMap<>();
		ErrorResponse errorResponse = null;
		try {
			empSalService.uploadEmpSalary(file);
			map.put("message", "File uploaded successfully.");
			
		} catch (EmpSalaryFileUploadException | IOException e) {
			errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
		}
		
		if(errorResponse != null) {
			return ResponseEntity.badRequest().body(errorResponse);
		}
		
		log.info("Exit uploadEmployeeSalary");
		
		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/users")
	public ResponseEntity<?> getUsers(@RequestParam("minSalary") String minSalary, @RequestParam("maxSalary") String maxSalary,
			@RequestParam("offset") String offset, @RequestParam("limit") String limit, @RequestParam("sort")  String sort) {
		
		log.info("In uploadEmployeeSalary");
		Map<String, List<EmpSalary>> map = new HashMap<>();
		ErrorResponse errorResponse = null;
		try {
			
			List<EmpSalary> userList = empSalService.getUsers(minSalary, maxSalary, offset, limit,
					sort);
			map.put("results", userList);
			
		} catch (InvalidRequestParamException e) {
			
			errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
			log.error(e.getMessage(), e);
		}
		
		if(errorResponse != null) {
			return ResponseEntity.badRequest().body(errorResponse);
		}
		
		return ResponseEntity.ok(map);
	}

}
