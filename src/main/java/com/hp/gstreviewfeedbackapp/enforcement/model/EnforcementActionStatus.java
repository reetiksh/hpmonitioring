package com.hp.gstreviewfeedbackapp.enforcement.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnforcementActionStatus {
	@Id
	Integer id;
	String codeName;
	String status;
	Boolean state;
	String usedIn;
	String usedByRole;
	Integer activationSequence;

	@Override
	public String toString() {
		return "EnforcementActionStatus [id=" + id + ", codeName=" + codeName + ", status=" + status + ", state="
				+ state + ", usedIn=" + usedIn + ", usedByRole=" + usedByRole + ", activationSequence="
				+ activationSequence + "]";
	}
}
