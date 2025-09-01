package com.hp.gstreviewfeedbackapp.enforcement.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.hp.gstreviewfeedbackapp.model.LocationDetails;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnforcementCaseCaseWorkflow {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "enforcement_case_case_workflow_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private long id;
	private String gstin;
	@Temporal(TemporalType.DATE)
	private Date caseReportingDate;
	private String period;
	private String assignedFrom;
	private String assignedTo;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private LocationDetails assignedFromLocation;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private LocationDetails assignedToLocation;
	private Integer assignedFromUserId;
	private Integer assigntoUserId;
	private Date caseUpdatingTimestamp;
	private String supportingFile;
	private String supportingFileDirectory;
	private String assignedFromLocationName;
	private String assignedToLocationName;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private EnforcementActionStatus action;
	private String parameterName;
}
