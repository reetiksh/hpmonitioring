package com.hp.gstreviewfeedbackapp.cag.model;

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
@Table(name = "cag_extension_no_documents")
public class CagExtensionNoDocument {

	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "cag_extension_No_Documents_sequence"),
        @Parameter(name = "initial_value", value = "1"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	private Long id;
	
	@Column(name = "extensionNo")
	private String extensionNo;

	@Column(name = "extensionFileName")
	private String extensionFileName;
	
	@OneToMany(mappedBy = "cagExtensionNoDocument",cascade = {CascadeType.PERSIST, CascadeType.DETACH ,CascadeType.REFRESH}, orphanRemoval = false)
	private List<MstCagCases> cagCasesList = new ArrayList<>();

	public CagExtensionNoDocument() {
		super();
		// TODO Auto-generated constructor stub
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

	public void setExtensionFileName(String extensionFileName) {
		this.extensionFileName = extensionFileName;
	}

	public List<MstCagCases> getCagCasesList() {
		return cagCasesList;
	}

	public void setCagCasesList(List<MstCagCases> cagCasesList) {
		this.cagCasesList = cagCasesList;
	}

	public CagExtensionNoDocument(Long id, String extensionNo, String extensionFileName,
			List<MstCagCases> cagCasesList) {
		super();
		this.id = id;
		this.extensionNo = extensionNo;
		this.extensionFileName = extensionFileName;
		this.cagCasesList = cagCasesList;
	}

	@Override
	public String toString() {
		return "CagExtensionNoDocument [id=" + id + ", extensionNo=" + extensionNo + ", extensionFileName="
				+ extensionFileName + ", cagCasesList=" + cagCasesList + "]";
	}

	
}
