package com.topecq.salmngmtapi.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.topecq.salmngmtapi.domain.EmpSalary;
import com.topecq.salmngmtapi.domain.EmpSalaryFileUploadException;
import com.topecq.salmngmtapi.domain.InvalidRequestParamException;
import com.topecq.salmngmtapi.service.repository.EmpSalaryFileUploadTrackerRepository;
import com.topecq.salmngmtapi.service.repository.EmpSalaryRepository;
import com.topecq.salmngmtapi.util.CsvUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmpSalaryService {
	
	private static final String EMPTY = "";
	private static final char DESC_ORDER_INDICATOR = '+';
	private static final char ASC_ORDER_INDICATOR = '-';

	public static final String INVALID_DATA_IN_FILE = "Processing failed due to invalid data in file.";
	public static final String LOGIN_ALREADY_PRESENT_FOR_DIFFERENT_ID = "Processing failed due to login already present for different id";
	public static final String EMPTY_FILE = "Processing failed due to Empty file.";
	public static final String INVALID_FILE_FORMAT = "Processing failed due to Invalid File format, System accepts only csv file.";
	public static final String A_FILE_IS_IN_PROCESSING_ALREADY = "A file is in processing already. Kindly upload after sometime.";

	public static final String INVALID_REQUEST_PARAM_MIN_SALARY = "Invalid Request param minSalary, it should be a valid number";
	public static final String INVALID_REQUEST_PARAM_MAX_SALARY = "Invalid Request param maxSalary, it should be a valid number";
	public static final String INVALID_REQUEST_PARAM_OFFSET = "Invalid Request param offset, it should be a valid number";
	public static final String INVALID_REQUEST_PARAM_LIMIT = "Invalid Request param limit, it should be a valid number";
	public static final String INVALID_REQUEST_PARAM_SORT = "Invalid Request param sort, it should start with +/- followed by id or login or name or salary ";
	
	private static final List<String> eligibleSortBy = List.of("id","login","name","salary");
	private static final List<Character> eligibleSortOrder = List.of(DESC_ORDER_INDICATOR,ASC_ORDER_INDICATOR);
	
	@Autowired
	private EmpSalaryHelperService empHelper;
	
	@Autowired
	private EmpSalaryRepository empSalRepo;

	@Autowired
	private EmpSalaryFileUploadTrackerRepository empSalFileUploadTrackerRepo;
	
	public void uploadEmpSalary(MultipartFile file) throws UnsupportedEncodingException, IOException, EmpSalaryFileUploadException {
		log.info("in uploadEmpSalary");
		
		long count = empSalFileUploadTrackerRepo.countByStatus("IN_PROGRESS");
		if (count > 0) {
			throw new EmpSalaryFileUploadException(A_FILE_IS_IN_PROCESSING_ALREADY);
		}
		
		if(!CsvUtil.isCsvFile(file)) {
			throw new EmpSalaryFileUploadException(INVALID_FILE_FORMAT);
		}
		
		List<EmpSalary> empList = CsvUtil.parseEmpSalFile(file);
		
		if(CollectionUtils.isEmpty(empList)){
			throw new EmpSalaryFileUploadException(EMPTY_FILE);
		}

		validateEmpList(empList);
		
		validateEmpListForDuplicate(empList);
		
		empHelper.saveEmpSalary(file, empList);
		
	}

	public List<EmpSalary> getUsers(String minSalaryParam, String maxSalaryParam, String offsetParam, String limitParam, String sortParam) throws InvalidRequestParamException{
		
		double minSalary = validateAndGetMinSalary(minSalaryParam);
		double maxSalary = validateAndGetMaxSalary(maxSalaryParam);
		int offset = validateAndGetOffset(offsetParam);
		int limit = validateAndGetLimit(limitParam);
		validateAndGetSort(sortParam);
		
		char sortOrderIndicator = sortParam.charAt(0);
		String sortByIndicator = sortParam.substring(1);
		
		List<EmpSalary> userList = empSalRepo.findAllBySalaryBetween(minSalary, maxSalary, sortOrderIndicator, sortByIndicator, offset, limit);
		
		return userList;
	}

	private void validateEmpListForDuplicate(List<EmpSalary> empList) throws EmpSalaryFileUploadException{
		
		for(EmpSalary emp : empList) {
			
			List<EmpSalary> existingEmp = empSalRepo.findAllByIdOrLogin(emp.getId(), emp.getLogin());
			
			int noOfRecords = existingEmp.size();
			String empId = emp.getId();
			
			if (noOfRecords > 1) {

				Optional<EmpSalary> empWithSameLoginButDifferentIdInDb = existingEmp.stream()
						.filter(e -> !e.getId().equals(empId) && e.getLogin().equals(emp.getLogin())).findFirst();

				if (empWithSameLoginButDifferentIdInDb.isPresent()) {
					Optional<EmpSalary> empWithSameIdInFile = empList.stream()
							.filter(e -> e.getId().equals(empWithSameLoginButDifferentIdInDb.get().getId()))
							.findFirst();
					if (!empWithSameIdInFile.isPresent() || empWithSameIdInFile.get().getLogin()
							.equals(empWithSameLoginButDifferentIdInDb.get().getLogin())) {
						throw new EmpSalaryFileUploadException(LOGIN_ALREADY_PRESENT_FOR_DIFFERENT_ID);
					}
				}

			}
			
		}
		
	}

	private void validateEmpList(List<EmpSalary> empList) throws EmpSalaryFileUploadException {
		
		for(EmpSalary emp : empList) {
			
			if(emp.getId().equals(EMPTY) || emp.getLogin().equals(EMPTY) || emp.getName().equals(EMPTY) || emp.getSalaryStr().equals(EMPTY)) {
				throw new EmpSalaryFileUploadException(INVALID_DATA_IN_FILE);
			}
			Double salary = Double.valueOf(emp.getSalaryStr());
			emp.setSalary(salary);
			if(emp.getSalary() <= 0.0) {
				throw new EmpSalaryFileUploadException(INVALID_DATA_IN_FILE);
			}
		}
	
	}

	private void validateAndGetSort(String sortParam) throws InvalidRequestParamException {
		
		try {

			char sortOrderIndicator = sortParam.charAt(0);
			String sortByIndicator = sortParam.substring(1);
		
			if(!eligibleSortOrder.contains(sortOrderIndicator)
					|| !eligibleSortBy.contains(sortByIndicator)) {
				throw new InvalidRequestParamException(INVALID_REQUEST_PARAM_SORT);
			}
		
		
		} catch (IndexOutOfBoundsException e) {
			
			throw new InvalidRequestParamException(INVALID_REQUEST_PARAM_SORT);

		}
		
	}
	
	private int validateAndGetLimit(String limitParam) throws InvalidRequestParamException {
		int limit = 0;
		try {
			limit = Integer.valueOf(limitParam);
		} catch( NumberFormatException e ) {
			throw new InvalidRequestParamException(INVALID_REQUEST_PARAM_LIMIT);
		}
		return limit;
	}
	
	private int validateAndGetOffset(String offsetParam) throws InvalidRequestParamException {
		int offset = 0;
		try {
			offset = Integer.valueOf(offsetParam);
		} catch( NumberFormatException e ) {
			throw new InvalidRequestParamException(INVALID_REQUEST_PARAM_OFFSET);
		}
		return offset;
	}

	private double validateAndGetMaxSalary(String maxSalaryStr) throws InvalidRequestParamException {
		double maxSalary = 0.0d;
		try {
			maxSalary = Double.valueOf(maxSalaryStr);
		} catch( NumberFormatException e ) {
			throw new InvalidRequestParamException(INVALID_REQUEST_PARAM_MAX_SALARY);
		}
		return maxSalary;
	}

	private double validateAndGetMinSalary(String minSalaryStr) throws InvalidRequestParamException {
		double minSalary = 0.0d;
		try {
			minSalary = Double.valueOf(minSalaryStr);
		} catch( NumberFormatException e ) {
			throw new InvalidRequestParamException(INVALID_REQUEST_PARAM_MIN_SALARY);
		}
		return minSalary;
	}
}
