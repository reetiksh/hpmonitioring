package com.hp.gstreviewfeedbackapp.audit.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "audit_para_category")
public class AuditParaCategory {
	@Id
	private Integer id;
	private String name;
	private boolean active;

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "AuditParaCategory [id=" + id + ", name=" + name + ", active=" + active + "]";
	}
}
