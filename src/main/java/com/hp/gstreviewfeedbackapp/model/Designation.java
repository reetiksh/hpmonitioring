package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_dd_admin_designation")
public class Designation {
	@Id
	@Column(name = "designation_id")
	private int designationId;

	@Column(name = "designation_name")
	private String designationName;

	@Column(name = "designation_acronym")
	private String designationAcronym;

	public int getDesignationId() {
		return designationId;
	}

	public void setDesignationId(int designationId) {
		this.designationId = designationId;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public String getDesignationAcronym() {
		return designationAcronym;
	}

	public void setDesignationAcronym(String designationAcronym) {
		this.designationAcronym = designationAcronym;
	}

}
