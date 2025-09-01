package com.hp.gstreviewfeedbackapp.audit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_master_cases_allocating_users")
@Getter
@Setter
public class AuditMasterCasesAllocatingUsers {
	@Id
	@Column(name = "case_id")
	private String CaseId;

	@Column(name = "l1_user")
	private Integer l1User;

	@Column(name = "l2_user")
	private Integer l2User;

	@Column(name = "l3_user")
	private Integer l3User;

	@Column(name = "mcm_user")
	private Integer mcmUser;

	public String getCaseId() {
		return CaseId;
	}

	public void setCaseId(String caseId) {
		CaseId = caseId;
	}

	public Integer getL1User() {
		return l1User;
	}

	public void setL1User(Integer l1User) {
		this.l1User = l1User;
	}

	public Integer getL2User() {
		return l2User;
	}

	public void setL2User(Integer l2User) {
		this.l2User = l2User;
	}

	public Integer getL3User() {
		return l3User;
	}

	public void setL3User(Integer l3User) {
		this.l3User = l3User;
	}

	@Override
	public String toString() {
		return "AuditMasterCasesAllocatingUsers [CaseId=" + CaseId + ", l1User=" + l1User + ", l2User=" + l2User
				+ ", l3User=" + l3User + ", mcmUser=" + mcmUser + "]";
	}

	public AuditMasterCasesAllocatingUsers(String caseId, Integer l1User, Integer l2User, Integer l3User,
			Integer mcmUser) {
		super();
		CaseId = caseId;
		this.l1User = l1User;
		this.l2User = l2User;
		this.l3User = l3User;
		this.mcmUser = mcmUser;
	}

	public AuditMasterCasesAllocatingUsers() {
		super();
		// TODO Auto-generated constructor stub
	}

}
