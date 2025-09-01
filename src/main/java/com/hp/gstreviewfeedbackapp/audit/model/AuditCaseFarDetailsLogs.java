package com.hp.gstreviewfeedbackapp.audit.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.hp.gstreviewfeedbackapp.model.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class AuditCaseFarDetailsLogs {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "audit_case_far_details_logs_sequence_new"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;

	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	private Date actionDate;

	private String nilDar;
	private String auditParaCategory;
	private Long amountInvolved;
	private Long amountRecovered;
	private Long amountDropped;
	private Long amountToBeRecovered;

	@Column(name = "last_updated_timestamp")
	private Date lastUpdatedTimeStamp;
	private Long auditCaseDateDocumentDetailsId;
	private Long AuditCaseFarDetailsId;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private UserDetails updatedBy;
	private String action;
}
