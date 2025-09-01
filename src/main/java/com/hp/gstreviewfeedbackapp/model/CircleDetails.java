package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_dd_circle_details")
public class CircleDetails {
	@Id
	@Column(name = "circle_id")
	private String circleId;

	@Column(name = "circle_name")
	private String circleName;

	public String getCircleId() {
		return circleId;
	}

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
}
