package com.hp.gstreviewfeedbackapp.audit.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_case_date_document_details")
@Getter
@Setter
public class AuditCaseDateDocumentDetails {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "audit_case_date_document_details_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;
	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	private Date actionDate;
	@Column(name = "file_path")
	private String actionFilePath;
	private String otherFilePath;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "action")
	private AuditCaseStatus action;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "case_id")
	private AuditMaster caseId;
	@Column(name = "last_updated_timestamp")
	private Date lastUpdatedTimeStamp;
	@Column(name = "comment_from_l2_officer")
	private String commentFromL2Officer;
	@Column(name = "comment_from_mcm_officer")
	private String commentFromMcmOfficer;
	@OneToMany(mappedBy = "auditCaseDateDocumentDetailsId", cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<AuditCaseDarDetails> auditCaseDarDetailsList;
	@OneToMany(mappedBy = "auditCaseDateDocumentDetailsId", cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<AuditCaseFarDetails> auditCaseFarDetailsList;
	@OneToMany(mappedBy = "auditCaseDateDocumentDetailsId", cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<AuditCaseFinalAmountRecoveryDetails> auditCaseFinalAmountRecoveryDetailsList;
	private String nilDar;
}
