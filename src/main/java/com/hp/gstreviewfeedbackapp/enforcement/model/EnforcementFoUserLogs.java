package com.hp.gstreviewfeedbackapp.enforcement.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnforcementFoUserLogs {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "enforcement_fo_user_logs_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;
	private String gstin;
	@Temporal(TemporalType.DATE)
	private Date caseReportingDate;
	private String period;
	private String assignedFrom;
	private String assignedTo;
	private Integer assignedFromUserId;
	private Integer assigntoUserId;
	private Date caseUpdatingTimestamp;
	private String assignedFromLocationName;
	private String assignedToLocationName;
	private Integer caseUpdatedByUser;
	private String action;
	private String caseId;
}
