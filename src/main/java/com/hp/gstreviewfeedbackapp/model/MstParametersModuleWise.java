package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mst_parameters_module_wise")
@Getter
@Setter
public class MstParametersModuleWise {
	@Id
	private Integer id;
	private String paramName;
	private boolean statusAudit;
	private boolean statusAssessment;
	private boolean statusScrutiny;
	private boolean statusEnforcement;
	private boolean statusCag;
	private Boolean statusSelfDetectedCases = false;
	private Boolean scrutinySelfDetectedCases = true;
}
