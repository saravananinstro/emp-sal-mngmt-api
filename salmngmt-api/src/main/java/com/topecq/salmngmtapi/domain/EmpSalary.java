package com.topecq.salmngmtapi.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "TB_EMP_SALARY")
public class EmpSalary {
	
	@Id
	private String id;
	private String login;
	private String name;
	private double salary;
	
	@Transient
	@JsonIgnore
	private String salaryStr;
	
	public EmpSalary(String id, String login, String name, String salaryStr) {
		super();
		this.id = id;
		this.login = login;
		this.name = name;
		this.salaryStr = salaryStr;
	}

}
