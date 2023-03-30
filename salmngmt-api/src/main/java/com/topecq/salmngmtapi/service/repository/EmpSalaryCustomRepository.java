package com.topecq.salmngmtapi.service.repository;

import java.util.List;

import com.topecq.salmngmtapi.domain.EmpSalary;

public interface EmpSalaryCustomRepository {

	public List<EmpSalary> findAllBySalaryBetween(double minSalary, double maxSalary, char order, String orderBy, int offset, int limit);
}
