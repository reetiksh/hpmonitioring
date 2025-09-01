package com.hp.gstreviewfeedbackapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "mst_user_role")
public class UserRole {

	@Id
	@GeneratedValue(generator = "sequence-generator")
	@GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
			@Parameter(name = "sequence_name", value = "mst_user_role_sequence"),
			@Parameter(name = "initial_value", value = "4000"), @Parameter(name = "increment_size", value = "1") })
	private int id;

	@Column(name = "name")
	private String roleName;

	@Column(name = "code")
	private String roleCode;

	@Column(name = "url")
	private String url;

	@Column(name = "category")
	private String category;

	@OneToMany(mappedBy = "userRole", cascade = CascadeType.ALL)
	private List<UserRoleMapping> userRoleMappings = new ArrayList<>();

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<UserRoleMapping> getUserRoleMappings() {
		return userRoleMappings;
	}

	public void setUserRoleMappings(List<UserRoleMapping> userRoleMappings) {
		this.userRoleMappings = userRoleMappings;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", roleName=" + roleName + ", roleCode=" + roleCode + "]";
	}

}
