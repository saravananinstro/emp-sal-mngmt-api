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
import com.topecq.salmngmtapi.domain.ErrorResponse;
import com.topecq.salmngmtapi.domain.InvalidRequestParamException;
import com.topecq.salmngmtapi.domain.ResponseVO;
import com.topecq.salmngmtapi.service.EmpSalaryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EmpSalaryController {
	
	@Autowired
	private EmpSalaryService empSalService;
	
	@PostMapping(value = "/users/upload")
	public ResponseEntity<ResponseVO<String>> uploadEmployeeSalary(@RequestParam("file") MultipartFile file) {
		
		log.info("In uploadEmployeeSalary");
		
		ResponseVO<String> responseVo = null;
		
		try {
			boolean status = empSalService.uploadEmpSalary(file);
			if(status) {
				responseVo = new ResponseVO<String>("Success", "File Uploaded Successfully", null, HttpStatus.OK);
			} else {
				responseVo = new ResponseVO<String>("Failure", "File Upload rejected due to validation failure", "E001", HttpStatus.BAD_REQUEST);
			}
			
		} catch (IOException e) {
			responseVo = new ResponseVO<String>("Failure", "File Uploaded Successfully", "E005", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Exit uploadEmployeeSalary");
		
		return new ResponseEntity<>(responseVo, responseVo.getStatus());
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
