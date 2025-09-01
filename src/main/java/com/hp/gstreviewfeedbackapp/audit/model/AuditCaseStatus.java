package com.hp.gstreviewfeedbackapp.audit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_case_status")
@Getter
@Setter
public class AuditCaseStatus {
	@Id
	private Integer id;
	private String category;
	private String status;

	@Column(name = "used_by_role")
	private String usedByRole;
	@Column(name = "jsp_page")
	private String jspPage;
	@Column(name = "activation_order")
	private Integer activationOrder;
	private Integer sequence;
	private String name;
	private String successMesage;
}
