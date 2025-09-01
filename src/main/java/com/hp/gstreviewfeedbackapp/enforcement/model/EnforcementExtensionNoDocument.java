package com.hp.gstreviewfeedbackapp.enforcement.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnforcementExtensionNoDocument {
	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "enforcement_extension_No_Documents_sequence"),
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	private Long id;
	private String extensionNo;
	private String extensionFileName;
	private String fileDirectory;
	@OneToMany(mappedBy = "extensionNoDocumentId", cascade = { CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, orphanRemoval = false)
	private List<EnforcementMaster> enforcementCases = new ArrayList<>();
}
