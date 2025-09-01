package com.hp.gstreviewfeedbackapp.scrutiny.model;

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
@Table(name = "scrutiny_extension_no_documents")
public class ScrutinyExtensionNoDocument {

	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "scrutiny_extension_No_Documents_sequence"),
        @Parameter(name = "initial_value", value = "1"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	private Long id;

	@Column(name = "extensionNo")
	private String extensionNo;

	@Column(name = "extensionFileName")
	private String extensionFileName;
	
	@OneToMany(mappedBy = "scrutinyExtensionNoDocument",cascade = {CascadeType.PERSIST, CascadeType.DETACH ,CascadeType.REFRESH}, orphanRemoval = false)
	private List<MstScrutinyCases> scrutinyCasesList = new ArrayList<>();

	public ScrutinyExtensionNoDocument() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScrutinyExtensionNoDocument(Long id, String extensionNo, String extensionFileName,
			List<MstScrutinyCases> scrutinyCasesList) {
		super();
		this.id = id;
		this.extensionNo = extensionNo;
		this.extensionFileName = extensionFileName;
		this.scrutinyCasesList = scrutinyCasesList;
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

	public List<MstScrutinyCases> getScrutinyCasesList() {
		return scrutinyCasesList;
	}

	public void setScrutinyCasesList(List<MstScrutinyCases> scrutinyCasesList) {
		this.scrutinyCasesList = scrutinyCasesList;
	}

	@Override
	public String toString() {
		return "ScrutinyExtensionNoDocument [id=" + id + ", extensionNo=" + extensionNo + ", extensionFileName="
				+ extensionFileName + ", scrutinyCasesList=" + scrutinyCasesList + "]";
	}
}
