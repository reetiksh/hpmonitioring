package com.hp.gstreviewfeedbackapp.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

@Embeddable
public class SubAppMenuId implements Serializable {

	private static final long serialVersionUID = 1L; // Adding a serialVersionUID

	@Column(name = "subId", nullable = false)
	private int subId;

	@Column(name = "menuid", nullable = false)
	private int menuId;

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(menuId, subId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubAppMenuId other = (SubAppMenuId) obj;
		return menuId == other.menuId && subId == other.subId;
	}
	
	

}
