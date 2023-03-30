package com.topecq.salmngmtapi.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topecq.salmngmtapi.domain.EmpSalaryFileUploadTracker;

public interface EmpSalaryFileUploadTrackerRepository extends JpaRepository<EmpSalaryFileUploadTracker, Long>{

	long countByStatus(String string);

}
