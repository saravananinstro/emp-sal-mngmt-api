package com.topecq.salmngmtapi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topecq.salmngmtapi.domain.EmpSalary;
import com.topecq.salmngmtapi.service.repository.EmpSalaryRepository;

@Service
public class EmpSalaryHelperService {
	
	@Autowired
	private EmpSalaryRepository empSalRepo;
	

	@Transactional
	public void saveEmpList(List<EmpSalary> empList) {
		empSalRepo.saveAll(empList);
	}
	
}
