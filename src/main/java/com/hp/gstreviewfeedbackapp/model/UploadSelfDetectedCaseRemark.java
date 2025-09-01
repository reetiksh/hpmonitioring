package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_dd_fo_self_detected_case_remark")
public class UploadSelfDetectedCaseRemark {
	@Id
	private Integer id;

	private String name;

	@Column(name = "active_status")
	private boolean activeStatus;

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
