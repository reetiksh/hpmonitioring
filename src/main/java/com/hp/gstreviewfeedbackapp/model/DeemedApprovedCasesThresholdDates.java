package com.hp.gstreviewfeedbackapp.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DeemedApprovedCasesThresholdDates {
	@Id
	private String period;
	private Date thresholdDates;
}
