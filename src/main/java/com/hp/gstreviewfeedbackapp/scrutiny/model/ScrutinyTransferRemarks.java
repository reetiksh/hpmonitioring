package com.hp.gstreviewfeedbackapp.scrutiny.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "mst_scrutiny_transfer_remarks")
public class ScrutinyTransferRemarks {

	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "mst_scrutiny_transfer_remarks_sequence"),
        @Parameter(name = "initial_value", value = "4000"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	private boolean status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
