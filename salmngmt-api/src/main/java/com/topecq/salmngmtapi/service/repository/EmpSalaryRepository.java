package com.topecq.salmngmtapi.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.topecq.salmngmtapi.domain.EmpSalary;

@Repository
public interface EmpSalaryRepository extends JpaRepository<EmpSalary, String>, EmpSalaryCustomRepository {

	List<EmpSalary> findAllByIdOrLogin(String id, String login);
	
}
