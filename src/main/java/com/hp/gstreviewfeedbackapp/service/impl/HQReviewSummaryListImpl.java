package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.model.CategoryTotal;
import com.hp.gstreviewfeedbackapp.service.HQReviewSummaryList;

@Service
public class HQReviewSummaryListImpl implements HQReviewSummaryList {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<CategoryTotal> getExtensionTotals() {
//		String sql = "SELECT category, COUNT(*) AS total_number_of_Cases, SUM(indicative_tax_value) AS total_indicative_tax, SUM(CAST(demand AS bigint)) as total_demand, (SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as total_recovery "
//				+ "FROM analytics.enforcement_review_case " + "GROUP BY category";

		String sql = "SELECT erc.category, COUNT(*) AS total_number_of_Cases, \r\n"
				+ "SUM(indicative_tax_value) AS total_indicative_tax, \r\n"
				+ "SUM(CAST(demand AS bigint)) as total_amount, \r\n"
				+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as total_recovery,\r\n"
				+ "table1.orginal_demand as total_demand\r\n"
				+ "FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
				+ "(select distinct category, sum(demand) as orginal_demand from analytics.enforcement_review_case \r\n"
				+ "where action_status = '3' and case_stage in ('5', '8', '9')\r\n" + "group by category) table1\r\n"
				+ "on erc.category = table1.category\r\n" + "GROUP BY erc.category, table1.orginal_demand;";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new CategoryTotal(rs.getString("category"), rs.getLong("total_number_of_Cases"),
						rs.getLong("total_indicative_tax"), rs.getLong("total_amount"), rs.getLong("total_recovery"),
						rs.getLong("total_demand")));
	}

}
