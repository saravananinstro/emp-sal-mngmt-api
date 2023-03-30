package com.topecq.salmngmtapi.service.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topecq.salmngmtapi.domain.EmpSalary;

@Repository
public class EmpSalaryCustomRepositoryImpl implements EmpSalaryCustomRepository {

	@Autowired
	EntityManager em;
	
	@Override
	public List<EmpSalary> findAllBySalaryBetween(double minSalary, double maxSalary, char order, String orderBy, int offset, int limit) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EmpSalary> cq = cb.createQuery(EmpSalary.class);
		Root<EmpSalary> empSal = cq.from(EmpSalary.class);
		Predicate salaryPredicate = cb.between(empSal.get("salary"), minSalary, maxSalary);
		cq.where(salaryPredicate);
		
		if(order == '+') {
			cq.orderBy(cb.asc(empSal.get(orderBy)));
		} else {
			cq.orderBy(cb.desc(empSal.get(orderBy)));
		}
		
		List<EmpSalary> empList = em.createQuery(cq).setMaxResults(limit).setFirstResult(offset).getResultList();
		
		return empList;
		
	}
}
