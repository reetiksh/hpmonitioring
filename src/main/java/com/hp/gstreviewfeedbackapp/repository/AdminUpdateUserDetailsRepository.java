package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CircleDetails;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;

@Repository
public class AdminUpdateUserDetailsRepository {
	private final JdbcTemplate jdbcTemplate;
	private List<Map<String, Object>> queryList = null;

	public AdminUpdateUserDetailsRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public boolean checkHaveRelationBetweenUserAndCircleAndRole(int userId, String circleId, int roleId) {
		String sql = "select urm.user_id from analytics.mst_user_role_mapping urm where urm.user_id = " + userId
				+ " and urm.circle_id = '" + circleId + "' and urm.user_role_id = " + roleId;
		queryList = jdbcTemplate.queryForList(sql);
		if (queryList != null && queryList.size() > 0) {
			return true;
		}
		return false;
	}

	public boolean checkHaveRelationBetweenUserAndZoneAndRole(int userId, String zoneId, int roleId) {
		String sql = "select urm.user_id from analytics.mst_user_role_mapping urm where urm.user_id = " + userId
				+ " and urm.zone_id = '" + zoneId + "' and urm.user_role_id = " + roleId;
		queryList = jdbcTemplate.queryForList(sql);
		if (queryList != null && queryList.size() > 0) {
			return true;
		}
		return false;
	}

	public boolean checkHaveRelationBetweenUserAndStateAndRole(int userId, String stateId, int roleId) {
		String sql = "select urm.user_id from analytics.mst_user_role_mapping urm where urm.user_id = " + userId
				+ " and urm.state_id = '" + stateId + "' and urm.user_role_id = " + roleId;
		queryList = jdbcTemplate.queryForList(sql);
		if (queryList != null && queryList.size() > 0) {
			return true;
		}
		return false;
	}

	public String getLocationHierarchyForCircleByCircleId(String circleId) {
		String sql = "select distinct concat(lm.state_name, '/', lm.zone_name, '/', "
				+ "lm.circle_name , ' (CIRCLE)') as circle_name  from analytics.mst_location_mapping lm where lm.circle_id = '"
				+ circleId + "'";
		queryList = jdbcTemplate.queryForList(sql);
		if (queryList != null && queryList.size() > 0) {
			return queryList.get(0).get("circle_name").toString();
		}
		return null;
	}

	public String getLocationHierarchyForZoneByZoneId(String zoneId) {
		String sql = "select distinct concat(lm.state_name, '/', lm.zone_name, ' (ZONE)') as zone_name from analytics.mst_location_mapping lm where lm.zone_id = '"
				+ zoneId + "'";
		queryList = jdbcTemplate.queryForList(sql);
		if (queryList != null && queryList.size() > 0) {
			return queryList.get(0).get("zone_name").toString();
		}
		return null;
	}

	public String getLocationHierarchyForStateByStateId(String stateId) {
		String sql = "select distinct concat(lm.state_name) as state_name from analytics.mst_dd_state_details lm where lm.state_id = '"
				+ stateId + "'";
		queryList = jdbcTemplate.queryForList(sql);
		if (queryList != null && queryList.size() > 0) {
			return queryList.get(0).get("state_name").toString();
		}
		return null;
	}
}
