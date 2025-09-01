package com.hp.gstreviewfeedbackapp.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCaseModel;

@Repository
public class CategoryRepository {

	private final JdbcTemplate jdbcTemplate;
	private List<Map<String, Object>> queryList = null;

	public CategoryRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<String> findAllCategoryNames() {
		String sql = "SELECT name FROM analytics.mst_dd_hq_category";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	public List<String> getNameFromTableAppRole() {
		String sql = "select name from analytics.tbl_app_role";

		return jdbcTemplate.queryForList(sql, String.class);
	}

	public Map<String, String> getAllZoneIdAndName() {
		Map<String, String> array = new HashMap<>();

		String sql = "select distinct lm.zone_id, concat(lm.state_name, '/', lm.zone_name, ' (ZONE)') as zone_name from analytics.mst_location_mapping lm";
		queryList = jdbcTemplate.queryForList(sql);

		for (Map<String, Object> row : queryList) {
			array.put(row.get("zone_id") != null ? row.get("zone_id").toString() : "NA",
					(row.get("zone_name") != null ? row.get("zone_name").toString() : "NA"));
		}

		return array;
	}

	public Map<String, String> getAllCircleIdAndName() {
		Map<String, String> array = new HashMap<>();

		String sql = "select distinct lm.circle_id , concat(lm.state_name, '/', lm.zone_name, '/', lm.circle_name , ' (CIRCLE)') as circle_name  from analytics.mst_location_mapping lm order by lm.circle_id";
		queryList = jdbcTemplate.queryForList(sql);

		for (Map<String, Object> row : queryList) {
			array.put(row.get("circle_id") != null ? row.get("circle_id").toString() : "NA",
					(row.get("circle_name") != null ? row.get("circle_name").toString() : "NA"));
		}

		return array;
	}

	// Review meeting code

	public List<Map<String, Object>> getAllEnforcementCaseCategoryWise() {

		List<Map<String, Object>> obj = new ArrayList<Map<String, Object>>();

		List<EnforcementReviewCaseModel> list = new ArrayList<EnforcementReviewCaseModel>();

		String sql = "select j.name, count(j.category), coalesce(sum(j.indicative_tax_value), 0) as sum from (select * from analytics.mst_dd_hq_category c left join "
				+ "analytics.enforcement_review_case e on c.name = e.category and e.action != 'closed') as j where j.id !=7 group by j.name,j.id order by j.id asc";

		List<Map<String, Object>> query = jdbcTemplate.queryForList(sql);

		return query;

	}

	public List<Map<String, Object>> getDetailedEnforcementCasesDetails() {

		List<Map<String, Object>> obj = new ArrayList<Map<String, Object>>();

		List<EnforcementReviewCaseModel> list = new ArrayList<EnforcementReviewCaseModel>();

		String sql = "select * from analytics.enforcement_review_case erc where category = 'Detailed Enforcement Cases' and action != 'closed'";

		List<Map<String, Object>> query = jdbcTemplate.queryForList(sql);

		return query;

	}

	public List<Map<String, Object>> getDetailedCasesDetails() {

		List<Map<String, Object>> obj = new ArrayList<Map<String, Object>>();

		List<EnforcementReviewCaseModel> list = new ArrayList<EnforcementReviewCaseModel>();

		String sql = "select erc.* from analytics.enforcement_review_case erc inner join analytics.mst_dd_hq_category mdhc \r\n"
				+ "on mdhc.name = erc.category where mdhc.id in (7,8,9) and erc.action != 'closed'";

		List<Map<String, Object>> query = jdbcTemplate.queryForList(sql);

		return query;

	}

	public Map<String, String> getStateIdAndName() {
		Map<String, String> array = new HashMap<>();

		String sql = "select state_id , state_name from analytics.mst_dd_state_details where state_id != 'NA'";
		queryList = jdbcTemplate.queryForList(sql);

		for (Map<String, Object> row : queryList) {
			array.put(row.get("state_id") != null ? row.get("state_id").toString() : "NA",
					(row.get("state_name") != null ? row.get("state_name").toString() : "NA"));
		}
		return array;
	}

	public Map<String, String> getAllCenterIdAndName() {
		Map<String, String> array = new HashMap<>();

		String sql = "select state_id , state_name from analytics.mst_dd_state_details where state_id = 'NC'";
		queryList = jdbcTemplate.queryForList(sql);

		for (Map<String, Object> row : queryList) {
			array.put(row.get("state_id") != null ? row.get("state_id").toString() : "NA",
					(row.get("state_name") != null ? row.get("state_name").toString() : "NA"));
		}
		return array;
	}

}
