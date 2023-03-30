package com.topecq.salmngmtapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.topecq.salmngmtapi.service.EmpSalaryService;

@SpringBootTest
@AutoConfigureMockMvc
public class EmpSalaryControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void invalidMinSalaryRequestParamTest() throws Exception {
		
		MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
		requestParam.add("minSalary", "");
		requestParam.add("maxSalary", "100");
		requestParam.add("offset", "0");
		requestParam.add("limit", "2");
		requestParam.add("sort", "+name");
		
		mockMvc.perform(MockMvcRequestBuilders.get("/users").params(requestParam))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EmpSalaryService.INVALID_REQUEST_PARAM_MIN_SALARY));
		
	}
	
	@Test
	public void invalidMaxSalaryRequestParamTest() throws Exception {
		
		MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
		requestParam.add("minSalary", "10");
		requestParam.add("maxSalary", "");
		requestParam.add("offset", "0");
		requestParam.add("limit", "2");
		requestParam.add("sort", "+name");
		
		mockMvc.perform(MockMvcRequestBuilders.get("/users").params(requestParam))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EmpSalaryService.INVALID_REQUEST_PARAM_MAX_SALARY));
		
	}
	
	@Test
	public void invalidOffsetRequestParamTest() throws Exception {
		
		MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
		requestParam.add("minSalary", "10");
		requestParam.add("maxSalary", "100");
		requestParam.add("offset", "");
		requestParam.add("limit", "2");
		requestParam.add("sort", "+name");
		
		mockMvc.perform(MockMvcRequestBuilders.get("/users").params(requestParam))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EmpSalaryService.INVALID_REQUEST_PARAM_OFFSET));
		
	}
	
	@Test
	public void invalidLimitRequestParamTest() throws Exception {
		
		MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
		requestParam.add("minSalary", "10");
		requestParam.add("maxSalary", "100");
		requestParam.add("offset", "0");
		requestParam.add("limit", "");
		requestParam.add("sort", "+name");
		
		mockMvc.perform(MockMvcRequestBuilders.get("/users").params(requestParam))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EmpSalaryService.INVALID_REQUEST_PARAM_LIMIT));
		
	}

	@Test
	public void sortParamWithoutOrderIndicatorTest() throws Exception {
		
		MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
		requestParam.add("minSalary", "10");
		requestParam.add("maxSalary", "100");
		requestParam.add("offset", "0");
		requestParam.add("limit", "2");
		requestParam.add("sort", "name");
		
		mockMvc.perform(MockMvcRequestBuilders.get("/users").params(requestParam))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EmpSalaryService.INVALID_REQUEST_PARAM_SORT));
		
	}
}
