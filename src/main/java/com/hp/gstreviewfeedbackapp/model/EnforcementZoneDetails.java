package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_dd_enforcement_zone_details")
public class EnforcementZoneDetails {
	@Id
	@Column(name = "enforcement_zone_id")
	private String enforcementZoneId;

	@Column(name = "enforcement_zone_name")
	private String enforcementZoneName;

	public String getEnforcementZoneId() {
		return enforcementZoneId;
	}

	public void setEnforcementZoneId(String enforcementZoneId) {
		this.enforcementZoneId = enforcementZoneId;
	}

	public String getEnforcementZoneName() {
		return enforcementZoneName;
	}

	public void setEnforcementZoneName(String enforcementZoneName) {
		this.enforcementZoneName = enforcementZoneName;
	}
}
