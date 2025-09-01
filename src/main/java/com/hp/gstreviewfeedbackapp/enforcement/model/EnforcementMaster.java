package com.hp.gstreviewfeedbackapp.enforcement.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hp.gstreviewfeedbackapp.model.Category;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.LocationDetails;
import com.hp.gstreviewfeedbackapp.model.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnforcementMaster {
	@EmbeddedId
	private CompositeKey id;
	private String taxpayerName;
	private String extensionNo;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private Category category;
	private Long indicativeTaxValue;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private EnforcementActionStatus action;
	private String assignedFrom;
	private String assignedTo;
	private String parameter;
	private String caseId;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private LocationDetails caseLocation;
	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private EnforcementExtensionNoDocument extensionNoDocumentId;
	private Date caseUpdatingTimestamp;
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn
	private UserDetails caseUpdatedByUser;
	private String inspectionRequired;
	@Transient
	private String parametersNameList;
	@OneToMany(mappedBy = "enforcementMaster", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<EnforcementCaseDateDocumentDetails> enforcementCaseDateDocumentDetails;
}
