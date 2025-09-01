package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_sms_templates")
public class SMSTemplate {

	@Id
	@Column(name = "template_id")
	private String templateId;
	private String template;
	private String templateParam;
	private String templateUsage;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplateParam() {
		return templateParam;
	}

	public void setTemplateParam(String templateParam) {
		this.templateParam = templateParam;
	}

	public String getTemplateUsage() {
		return templateUsage;
	}

	public void setTemplateUsage(String templateUsage) {
		this.templateUsage = templateUsage;
	}

}
