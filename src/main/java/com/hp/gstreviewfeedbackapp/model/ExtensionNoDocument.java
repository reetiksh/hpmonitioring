package com.hp.gstreviewfeedbackapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "Extension_No_Documents")
public class ExtensionNoDocument {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "extension_No_Documents_sequence"),
			@Parameter(name = "initial_value", value = "1000"), @Parameter(name = "increment_size", value = "1") })
	private Long id;

	@Column(name = "EXTENSION_NO")
	private String extensionNo;

	@Column(name = "EXTENSION_FILE_NAME")
	private String extensionFileName;

	@OneToMany(mappedBy = "extensionNoDocument", cascade = { CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, orphanRemoval = false)
	private List<EnforcementReviewCase> enforcementReviewCases = new ArrayList<>();

	public ExtensionNoDocument() {

	}

	public ExtensionNoDocument(Long id, String extensionNo, String pdfFileName,
			List<EnforcementReviewCase> enforcementReviewCases) {
		super();
		this.id = id;
		this.extensionNo = extensionNo;
		this.extensionFileName = pdfFileName;
		this.enforcementReviewCases = enforcementReviewCases;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExtensionNo() {
		return extensionNo;
	}

	public void setExtensionNo(String extensionNo) {
		this.extensionNo = extensionNo;
	}

	public String getExtensionFileName() {
		return extensionFileName;
	}

	public void setExtensionFileName(String pdfFileName) {
		this.extensionFileName = pdfFileName;
	}

	public List<EnforcementReviewCase> getEnforcementReviewCases() {
		return enforcementReviewCases;
	}

	public void setEnforcementReviewCases(List<EnforcementReviewCase> enforcementReviewCases) {
		this.enforcementReviewCases = enforcementReviewCases;
	}

}
