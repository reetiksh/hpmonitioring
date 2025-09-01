package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CircleRepository {

	private final JdbcTemplate jdbcTemplate;
	private List<Map<String, Object>> queryList = null;

	public CircleRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

//	public String findCircle(String circleName) {
//		String sql = "SELECT circle_name FROM analytics.circle WHERE circle_name = ?";
//		return jdbcTemplate.queryForObject(sql, new Object[] { circleName }, String.class);
//	}

	public String getCircleIdByCircleName(String circleName) {
		String sql = "select cd.circle_id from analytics.mst_dd_circle_details cd where cd.circle_name = '"
				+ circleName.trim() + "'";
		queryList = jdbcTemplate.queryForList(sql);
		if (queryList != null && queryList.size() > 0) {
			return queryList.get(0).get("circle_id").toString();
		}
		return null;
	}
}
