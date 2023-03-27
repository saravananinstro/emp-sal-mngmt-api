package com.topecq.salmngmtapi.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "TB_EMP_SAL_FILE_UPD_TRACKER")
public class EmpSalaryFileUploadTracker {

	@Id
	@GeneratedValue
	private long id;
	private String fileName;
	private String status;
	private LocalDateTime uploadedOn;
	private LocalDateTime completedOn;
	
	public EmpSalaryFileUploadTracker(String fileName, String status, LocalDateTime uploadedOn) {
		super();
		this.fileName = fileName;
		this.status = status;
		this.uploadedOn = uploadedOn;
	}

}
