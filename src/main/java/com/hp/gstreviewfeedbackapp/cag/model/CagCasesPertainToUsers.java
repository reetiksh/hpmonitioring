package com.hp.gstreviewfeedbackapp.cag.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;

@Entity
@Table(name = "cag_case_pertain_to_users")
public class CagCasesPertainToUsers {
	
	@EmbeddedId
	private CagCompositeKey id;
	
	@Column(name = "hq_user_id")
	private Integer hqUserId=0;
	
	@Column(name = "fo_user_id")
	private Integer foUserId=0;

	public CagCompositeKey getId() {
		return id;
	}

	public void setId(CagCompositeKey id) {
		this.id = id;
	}

	public Integer getHqUserId() {
		return hqUserId;
	}

	public void setHqUserId(Integer hqUserId) {
		this.hqUserId = hqUserId;
	}

	public Integer getFoUserId() {
		return foUserId;
	}

	public void setFoUserId(Integer foUserId) {
		this.foUserId = foUserId;
	}

}
