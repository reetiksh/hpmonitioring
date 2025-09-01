package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_case_notification_category")
public class MstCaseNotificationCategory {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "sequence-generator")
	private Long id;

	@Column(name = "type")
	private String type;

	@Column(name = "template")
	private String template;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
