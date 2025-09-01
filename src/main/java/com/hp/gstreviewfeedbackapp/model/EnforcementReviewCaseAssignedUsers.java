package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "enforcement_review_case_assigned_users")
@Getter
@Setter
public class EnforcementReviewCaseAssignedUsers {
	@EmbeddedId
	private CompositeKey id;
	@Column(name = "hq_user_id")
	private Integer hqUserId = 0;
	@Column(name = "fo_user_id")
	private Integer foUserId = 0;
	@Column(name = "ap_user_id")
	private Integer apUserId = 0;
	@Column(name = "ru_user_id")
	private Integer ruUserId = 0;
	@Column(name = "rm_user_id")
	private Integer rmUserId = 0;
	private Integer foaUserId = 0;

	@Override
	public String toString() {
		return "EnforcementReviewCaseAssignedUsers [id=" + id + ", hqUserId=" + hqUserId + ", foUserId=" + foUserId
				+ ", apUserId=" + apUserId + ", ruUserId=" + ruUserId + ", rmUserId=" + rmUserId + ", foaUserId="
				+ foaUserId + "]";
	}
}
