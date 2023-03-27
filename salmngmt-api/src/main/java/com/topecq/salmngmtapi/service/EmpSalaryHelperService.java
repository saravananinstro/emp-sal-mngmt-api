package com.topecq.salmngmtapi.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.topecq.salmngmtapi.domain.EmpSalary;
import com.topecq.salmngmtapi.domain.EmpSalaryFileUploadTracker;
import com.topecq.salmngmtapi.service.repository.EmpSalaryFileUploadTrackerRepository;
import com.topecq.salmngmtapi.service.repository.EmpSalaryRepository;

@Service
public class EmpSalaryHelperService {
	
	@Autowired
	private EmpSalaryRepository empSalRepo;
	
	@Autowired
	private EmpSalaryFileUploadTrackerRepository empSalFileUploadTrackerRepo;

	@Transactional
	public void saveEmpSalary(MultipartFile file, List<EmpSalary> empList) {
		
		EmpSalaryFileUploadTracker empSalFileUploadTracker = new EmpSalaryFileUploadTracker(file.getName(),
				"IN_PROGRESS", LocalDateTime.now());

		empSalFileUploadTracker = empSalFileUploadTrackerRepo.save(empSalFileUploadTracker);
		
		empSalRepo.saveAll(empList);
		
		empSalFileUploadTracker.setStatus("COMPLETED");
		empSalFileUploadTracker.setCompletedOn(LocalDateTime.now());
		empSalFileUploadTrackerRepo.save(empSalFileUploadTracker);
		
	}
	
}
