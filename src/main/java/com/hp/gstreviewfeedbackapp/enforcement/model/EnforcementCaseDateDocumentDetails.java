package com.hp.gstreviewfeedbackapp.enforcement.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.RecoveryStage;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnforcementCaseDateDocumentDetails {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "enforcement_case_date_document_details_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumns({ @JoinColumn(name = "GSTIN", referencedColumnName = "GSTIN"),
			@JoinColumn(name = "CASE_REPORTING_DATE", referencedColumnName = "CASE_REPORTING_DATE"),
			@JoinColumn(name = "PERIOD", referencedColumnName = "PERIOD") })
	private EnforcementMaster enforcementMaster;
	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	private Date actionDate;
	@Column(name = "file_name")
	private String actionFileName;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private EnforcementActionStatus action;
	@Column(name = "last_updated_timestamp")
	private Date lastUpdatedTimeStamp;
	private String caseId;
	private Integer assignedToAssessmentFOUserId;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private LocationDetails assignedToAssessmentLocation;
	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "RECOVERY_STAGE")
	private RecoveryStage recoveryStage;
	@Column(name = "recovery_stage_arn")
	private String recoveryStageArn;
}
