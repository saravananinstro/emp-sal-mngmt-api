package com.topecq.salmngmtapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.topecq.salmngmtapi.BaseUnitTest;
import com.topecq.salmngmtapi.domain.EmpSalary;
import com.topecq.salmngmtapi.domain.InvalidRequestParamException;
import com.topecq.salmngmtapi.service.repository.EmpSalaryRepository;

public class EmpSalaryServiceTest extends BaseUnitTest{
	
	@Mock
	EmpSalaryRepository empSalaryRepository;
	
	@Mock
	EmpSalaryHelperService empHelper;
	
	@InjectMocks
	EmpSalaryService empService;

	
	@Test
	@DisplayName("Invalid File Type Test")
	public void invalidFileTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append("id1,login1,name1,100").append("\n")
				.append("id2,login2,name2,200").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/xml",csvData.getBytes("utf-8"));
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertFalse(status);
		
	}

	@Test
	@DisplayName("Empty File Test")
	public void emptyFileTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertFalse(status);
		
	}
	
	@Test
	@DisplayName("Missing Id Test")
	public void missingIdTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append(" ,login1,name1,100").append("\n")
				.append("id2,login2,name2,200").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertFalse(status);
		
	}
	
	@Test
	@DisplayName("Missing Login Test")
	public void missingLoginTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append("id1,login1,name1,100").append("\n")
				.append("id2,,name2,200").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertFalse(status);
		
	}
	
	@Test
	@DisplayName("Missing name Test")
	public void missingNameTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append("id1,login1,name1,100").append("\n")
				.append("id2,login2,,200").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertFalse(status);
		
	}

	@Test
	@DisplayName("Missing salary Test")
	public void missingSalaryTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append("id1,login1,name1,100").append("\n")
				.append("id2,login2,name2,").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertFalse(status);
		
	}
	
	@Test
	@DisplayName("Salary equal to 0 Test")
	public void equalToZeroSalaryTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append("id1,login1,name1,100").append("\n")
				.append("id2,login2,name2,0").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertFalse(status);
		
	}

	@Test
	@DisplayName("Login already exists for different id Test")
	public void loginAlreadyExistsTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append("id1,login1,name1,100").append("\n").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		EmpSalary emp1 = new EmpSalary("id1","login2","name1", "100");
		EmpSalary emp2 = new EmpSalary("id2","login1","name1", "300");
		
		List<EmpSalary> empSalaryList = List.of(emp1, emp2);
		
		when(empSalaryRepository.findAllByIdOrLogin(anyString(), anyString())).thenReturn(empSalaryList);
		doNothing().when(empHelper).saveEmpList(anyList());
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertFalse(status);
		verify(empHelper, times(0)).saveEmpList(anyList());
		
		
	}
	
	@Test
	@DisplayName("Swap Login Test")
	public void loginSwapTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append("id1,login2,name1,100").append("\n")
				.append("id2,login1,name2,100").append("\n").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		EmpSalary emp1 = new EmpSalary("id1","login1","name1", "100");
		EmpSalary emp2 = new EmpSalary("id2","login2","name2", "300");
		
		List<EmpSalary> empSalaryList = List.of(emp1, emp2);
		
		when(empSalaryRepository.findAllByIdOrLogin(anyString(), anyString())).thenReturn(empSalaryList);
		doNothing().when(empHelper).saveEmpList(anyList());
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertTrue(status);
		verify(empHelper, times(1)).saveEmpList(anyList());
		
		
	}
	
	@Test
	public void uploadEmpSalaryTest() throws IOException {
		
		String csvData = new StringBuffer().append("id, login, name, salary").append("\n")
				.append("id1,login1,name1,100").append("\n")
				.append("id2,login2,name2,200").toString();
		
		MultipartFile mockFile=new MockMultipartFile("mockFile","test.csv","text/csv",csvData.getBytes("utf-8"));
		
		when(empSalaryRepository.findAllByIdOrLogin(anyString(), anyString())).thenReturn(new ArrayList<>());
		doNothing().when(empHelper).saveEmpList(anyList());
		
		boolean status = empService.uploadEmpSalary(mockFile);
		
		Assertions.assertTrue(status);
		verify(empHelper, times(1)).saveEmpList(anyList());
		
		
	}

	
	@Test
	@DisplayName("Invalid(empty) minSalary Param Test")
	public void invalidMinSalaryRequestParamTest() throws Exception {
		
		String minSalary = "";
		String maxSalary = "100";
		String offset = "0";
		String limit = "1";
		String sort = "+name";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_MIN_SALARY);
			
	}
	
	@Test
	@DisplayName("Invalid(empty) maxSalary Param Test")
	public void invalidMaxSalaryRequestParamTest() throws Exception {
		String minSalary = "10";
		String maxSalary = "";
		String offset = "0";
		String limit = "1";
		String sort = "+name";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_MAX_SALARY);
	}
	
	@Test
	@DisplayName("Invalid(empty) offset Param Test")
	public void invalidOffsetRequestParamTest() throws Exception {
		String minSalary = "10";
		String maxSalary = "100";
		String offset = "";
		String limit = "1";
		String sort = "+name";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_OFFSET);
		
	}
	
	@Test
	@DisplayName("Invalid(empty) limit Param Test")
	public void invalidLimitRequestParamTest() throws Exception {
		
		String minSalary = "10";
		String maxSalary = "100";
		String offset = "0";
		String limit = "";
		String sort = "+name";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_LIMIT);
	}

	@Test
	@DisplayName("Invalid(empty) sort Param Test")
	public void sortParamEmptyTest() throws Exception {
		
		String minSalary = "10";
		String maxSalary = "100";
		String offset = "0";
		String limit = "100";
		String sort = "name";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_SORT);
		
	}

	
	@Test
	@DisplayName("Invalid(missing asc/desc indicator) sort Param Test")
	public void sortParamWithoutOrderIndicatorTest() throws Exception {
		
		String minSalary = "10";
		String maxSalary = "100";
		String offset = "0";
		String limit = "100";
		String sort = "name";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_SORT);
		
	}
	
	@Test
	@DisplayName("Invalid(incorrect asc/desc indicator) sort Param Test")
	public void sortParamIncorrectOrderIndicatorTest() throws Exception {
		
		String minSalary = "10";
		String maxSalary = "100";
		String offset = "0";
		String limit = "100";
		String sort = "*name";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_SORT);
		
	}
	
	@Test
	@DisplayName("Invalid(missing orderby indicator) sort Param Test")
	public void sortParamWithoutOrderByIndicatorTest() throws Exception {
		
		String minSalary = "10";
		String maxSalary = "100";
		String offset = "0";
		String limit = "100";
		String sort = "+";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_SORT);
		
	}
	
	@Test
	@DisplayName("Invalid(incorrect orderby indicator) sort Param Test")
	public void sortParamIncorrectOrderByIndicatorTest() throws Exception {
		
		String minSalary = "10";
		String maxSalary = "100";
		String offset = "0";
		String limit = "100";
		String sort = "+empName";
		
		InvalidRequestParamException exception = Assertions.assertThrows(InvalidRequestParamException.class, () -> {
			empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		});
		
		assertEquals(exception.getMessage(), EmpSalaryService.INVALID_REQUEST_PARAM_SORT);
		
	}
	
	@Test
	@DisplayName("Success case")
	public void getUsersSuccessTest() throws Exception {
		
		String minSalary = "10";
		String maxSalary = "100";
		String offset = "0";
		String limit = "100";
		String sort = "+name";
		
		when(empSalaryRepository.findAllBySalaryBetween(anyDouble(), anyDouble(), anyChar(), anyString(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
		
		empService.getUsers(minSalary, maxSalary, offset, limit, sort);
		
		verify(empSalaryRepository, times(1)).findAllBySalaryBetween(anyDouble(), anyDouble(), anyChar(), anyString(), anyInt(), anyInt());
		
	}
}
