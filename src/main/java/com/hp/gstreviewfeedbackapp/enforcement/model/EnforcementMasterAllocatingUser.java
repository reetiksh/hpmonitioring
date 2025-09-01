package com.hp.gstreviewfeedbackapp.enforcement.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EnforcementMasterAllocatingUser {
	@EmbeddedId
	private CompositeKey id;
	private Integer hqUserId = 0;
	private Integer enfFoUserId = 0;
	private Integer enfSvoUserId = 0;
}
