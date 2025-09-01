package com.hp.gstreviewfeedbackapp.audit.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "audit_extension_no_documents")
public class AuditExtensionNoDocument {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "audit_extension_No_Documents_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;

	@Column(name = "EXTENSION_NO")
	private String extensionNo;

	@Column(name = "EXTENSION_FILE_NAME")
	private String extensionFileName;

	@OneToMany(mappedBy = "auditExtensionNoDocument", cascade = { CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, orphanRemoval = false)
	private List<AuditMaster> auditMasterList = new ArrayList<>();

	public AuditExtensionNoDocument() {
		super();
	}

	public AuditExtensionNoDocument(Long id, String extensionNo, String extensionFileName,
			List<AuditMaster> auditMasterList) {
		super();
		this.id = id;
		this.extensionNo = extensionNo;
		this.extensionFileName = extensionFileName;
		this.auditMasterList = auditMasterList;
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

	public String getPdfFileName() {
		return extensionFileName;
	}

	public void setPdfFileName(String pdfFileName) {
		this.extensionFileName = pdfFileName;
	}

	public String getExtensionFileName() {
		return extensionFileName;
	}

	public void setExtensionFileName(String extensionFileName) {
		this.extensionFileName = extensionFileName;
	}

	public List<AuditMaster> getAuditMasterList() {
		return auditMasterList;
	}

	public void setAuditMasterList(List<AuditMaster> auditMasterList) {
		this.auditMasterList = auditMasterList;
	}

	@Override
	public String toString() {
		return "AuditExtensionNoDocument [id=" + id + ", extensionNo=" + extensionNo + ", extensionFileName="
				+ extensionFileName + ", auditMasterList=" + auditMasterList + "]";
	}

}
