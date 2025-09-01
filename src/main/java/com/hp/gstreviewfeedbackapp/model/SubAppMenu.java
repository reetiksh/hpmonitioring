/**
 * 
 */
package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.hibernate.validator.constraints.br.CNPJ;

/**
 * 
 */
@Entity
@Table(name = "mst_tbl_sub_app_menu")
public class SubAppMenu {

	@EmbeddedId
	private SubAppMenuId id;

	@Column(name = "sub_url", length = 255)
	private String subUrl;

	@Column(name = "sub_name", length = 255)
	private String subName;

	@Column(name = "priority")
	private int priority;

	public String getSubUrl() {
		return subUrl;
	}

	public void setSubUrl(String subUrl) {
		this.subUrl = subUrl;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public SubAppMenuId getId() {
		return id;
	}

	public void setId(SubAppMenuId id) {
		this.id = id;
	}

}
