package com.hp.gstreviewfeedbackapp.scrutiny.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;

@Entity
@Table(name = "scrutiny_case_pertain_to_users")
public class ScrutinyCasesPertainToUsers {
	@EmbeddedId
	private CompositeKey id;

	@Column(name = "hq_user_id")
	private Integer hqUserId = 0;

	@Column(name = "fo_user_id")
	private Integer foUserId = 0;

	@Column(name = "ru_user_id")
	private Integer ruUserId = 0;

	public CompositeKey getId() {
		return id;
	}

	public void setId(CompositeKey id) {
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

	public Integer getRuUserId() {
		return ruUserId;
	}

	public void setRuUserId(Integer ruUserId) {
		this.ruUserId = ruUserId;
	}
}
