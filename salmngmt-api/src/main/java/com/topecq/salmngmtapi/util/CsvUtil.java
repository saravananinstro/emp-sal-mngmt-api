package com.topecq.salmngmtapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.topecq.salmngmtapi.domain.EmpSalary;
import com.topecq.salmngmtapi.domain.EmpSalaryUploadFileHeader;

public class CsvUtil {

	public static String TYPE = "text/csv";
	public static char COMMENT_MARKER = '#';

	public static boolean isCsvFile(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}

		return true;
	}

	public static List<EmpSalary> parseEmpSalFile(MultipartFile file) throws UnsupportedEncodingException, IOException {
	    
		CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
		        .setHeader(EmpSalaryUploadFileHeader.class)
		        .setCommentMarker(COMMENT_MARKER)
		        .setSkipHeaderRecord(true)
		        .build();
		
		List<EmpSalary> empSalList = new ArrayList<>();
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))){
			
			Iterable<CSVRecord> records = csvFormat.parse(reader);
		    
			for (CSVRecord record : records) {
		        
				String id = record.get(EmpSalaryUploadFileHeader.id).trim();
		        String login = record.get(EmpSalaryUploadFileHeader.login).trim();
		        String name = record.get(EmpSalaryUploadFileHeader.name).trim();
		        String salaryStr = record.get(EmpSalaryUploadFileHeader.salary).trim();
		        
		        empSalList.add(new EmpSalary(id, login, name, salaryStr));
		    }
			
		}
		
		return empSalList;
	}
		
}
