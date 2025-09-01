package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_dd_location_details")
public class LocationDetails {
	@Id
	@Column(name = "location_id")
	private String locationId;

	@Column(name = "location_name")
	private String locationName;

	@Column(name = "location_type")
	private String locationType;

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	@Override
	public String toString() {
		return "LocationDetails [locationId=" + locationId + ", locationName=" + locationName + ", locationType="
				+ locationType + "]";
	}
}
