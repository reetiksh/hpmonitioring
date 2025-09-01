package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mst_dd_district_details")
public class DistrictDetails {

	@Id
	@Column(name = "district_id")
	private String districtId;

	@Column(name = "district_name")
	private String districtName;

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

}
