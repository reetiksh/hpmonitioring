package com.hp.gstreviewfeedbackapp.cag.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cag_category")
public class CagCategory {
	
	@Id
	private 
	Integer id;
	
	private String name;
	
	private boolean status;

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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CagCaseUploadParameters [id=" + id + ", name=" + name + ", status=" + status + "]";
	}
	
	
}
