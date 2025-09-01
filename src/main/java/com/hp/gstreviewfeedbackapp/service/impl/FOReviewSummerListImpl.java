package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.model.CategoryTotal;
import com.hp.gstreviewfeedbackapp.service.FOReviewSummeryList;

@Service
public class FOReviewSummerListImpl implements FOReviewSummeryList {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("deprecation")
	@Override
	public List<CategoryTotal> getCategoryDetails(List<String> locations) {

		List<CategoryTotal> categoryTotal = null;

		/*
		 * String inSql = String.join(",", Collections.nCopies(locations.size(), "?"));
		 * 
		 * String sql =
		 * "SELECT erc.category, COUNT(*) AS total_number_of_Cases, SUM(indicative_tax_value) AS total_indicative_tax, SUM(CAST(demand AS bigint)) as total_amount, \r\n"
		 * +
		 * "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as total_recovery,\r\n"
		 * +
		 * "table1.orginal_demand as total_demand FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
		 * +
		 * "(select distinct category, sum(demand) as orginal_demand from analytics.enforcement_review_case\r\n"
		 * +
		 * "where action_status = '3' and case_stage in ('5', '8', '9') and working_location IN (%s) \r\n"
		 * + "group by category) table1\r\n" + "on erc.category = table1.category\r\n" +
		 * "where erc.working_location IN (%s)\r\n" +
		 * "GROUP BY erc.category, table1.orginal_demand";
		 * 
		 * if(locations.size() != 0) {
		 * 
		 * categoryTotal = jdbcTemplate.query(String.format(sql , inSql, inSql),
		 * locations.toArray() , (rs, rowNum) -> new
		 * CategoryTotal(rs.getString("category"),
		 * rs.getLong("total_number_of_Cases"),rs.getLong("total_indicative_tax"),
		 * rs.getLong("total_amount"), rs.getLong("total_recovery"),
		 * rs.getLong("total_demand")));
		 * 
		 * }
		 */

		// Generate placeholders for IN clause
		String inSql = String.join(",", Collections.nCopies(locations.size(), "?"));

		// SQL query with placeholders
		String sql = "SELECT erc.category, COUNT(*) AS total_number_of_Cases, SUM(indicative_tax_value) AS total_indicative_tax, SUM(CAST(demand AS bigint)) AS total_amount, \r\n"
				+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) AS total_recovery,\r\n"
				+ "table1.original_demand AS total_demand FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
				+ "(SELECT DISTINCT category, SUM(demand) AS original_demand FROM analytics.enforcement_review_case\r\n"
				+ "WHERE action_status = '3' AND case_stage IN ('5', '8', '9') AND working_location IN (%s) \r\n"
				+ "GROUP BY category) table1\r\n" + "ON erc.category = table1.category\r\n"
				+ "WHERE erc.working_location IN (%s)\r\n" + "GROUP BY erc.category, table1.original_demand";

		// Check if locations list is not empty
		if (locations.size() != 0) {
			// Combine the location list with itself to match the two placeholders
			List<Object> params = new ArrayList<>();
			params.addAll(locations);
			params.addAll(locations);

			// Execute the query
			categoryTotal = jdbcTemplate.query(String.format(sql, inSql, inSql), params.toArray(),
					(rs, rowNum) -> new CategoryTotal(rs.getString("category"), rs.getLong("total_number_of_Cases"),
							rs.getLong("total_indicative_tax"), rs.getLong("total_amount"),
							rs.getLong("total_recovery"), rs.getLong("total_demand")));
		}

		return categoryTotal;

	}

	@Override
	public List<CategoryTotal> getStateCategoryDetails() {

		List<CategoryTotal> categoryTotal = null;

		String sql = "SELECT erc.category, COUNT(*) AS total_number_of_Cases, SUM(indicative_tax_value) AS total_indicative_tax, SUM(CAST(demand AS bigint)) as total_amount, \r\n"
				+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as total_recovery, \r\n"
				+ "table1.orginal_demand as total_demand FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
				+ "(select distinct category, sum(demand) as orginal_demand from analytics.enforcement_review_case\r\n"
				+ "where action_status = '3' and case_stage in ('5', '8', '9')\r\n" + "group by category) table1\r\n"
				+ "on erc.category = table1.category\r\n" + "GROUP BY erc.category, table1.orginal_demand";

		categoryTotal = jdbcTemplate.query(sql,
				(rs, rowNum) -> new CategoryTotal(rs.getString("category"), rs.getLong("total_number_of_Cases"),
						rs.getLong("total_indicative_tax"), rs.getLong("total_amount"), rs.getLong("total_recovery"),
						rs.getLong("total_demand")));

		return categoryTotal;

	}

}
