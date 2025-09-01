package com.hp.gstreviewfeedbackapp.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.DashboardDistrictCircle;

@Repository
public class DashboardRepository {
	@Autowired
	private JdbcTemplate template;

	public DashboardRepository(JdbcTemplate jdbctemplate) {
		this.template = jdbctemplate;
	}

	public List<DashboardDistrictCircle> getStateCircle() {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id\r\n"
				+ "where category != 'Old Cases' and mlm.location_id not in ('HPT', 'C81', 'Z04') group by mlm.location_name;";
		list = template.query(sql,
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings("deprecation")
	public List<DashboardDistrictCircle> getLocationCircle(List<String> locations) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inSql = String.join(",", Collections.nCopies(locations.size(), "?"));
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id\r\n"
				+ "where category != 'Old Cases' and erc.working_location in (%s) group by mlm.location_name";
		if (locations.size() != 0) {
			list = template.query(String.format(sql, inSql), locations.toArray(),
					(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
							rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
							rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
							rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
							rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
							rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		}
		return list;
	}

	public List<DashboardDistrictCircle> getStateZone() {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.zone_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped,"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc \r\n"
				+ "on erc.working_location = mlm.circle_id or erc.working_location = mlm.zone_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category \r\n"
				+ "where mdhc.active_status = true and mlm.zone_id != 'Z04'" + "group by mlm.zone_name";
		list = template.query(sql,
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	public List<DashboardDistrictCircle> getStateEnforcementZone() {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.enforcement_zone_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped,"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc \r\n"
				+ "on erc.working_location = mlm.circle_id or erc.working_location = mlm.district_id "
				+ "where mlm.enforcement_zone_id != 'EZ04' \r\n" + "group by mlm.enforcement_zone_name";
		list = template.query(sql,
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings("deprecation")
	public List<DashboardDistrictCircle> getLocationZone(List<String> locations) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inSql = String.join(",", Collections.nCopies(locations.size(), "?"));
		String sql = "select mlm.zone_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped,"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc \r\n"
				+ "on erc.working_location = mlm.circle_id or erc.working_location = mlm.zone_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category "
				+ "where erc.working_location in (%s) " + "and mdhc.active_status = true and mlm.zone_id != 'Z04'"
				+ "group by mlm.zone_name";
		list = template.query(String.format(sql, inSql), locations.toArray(),
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getStateCircleByCategory(int id) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category\r\n"
				+ "where mdhc.id =? and mlm.location_id not in ('HPT', 'C81', 'Z04') group by mlm.location_name";
		list = template.query(sql, new Object[] { id },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getStateCircleByCategoryAndParameterName(int id, String parameterName) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category\r\n"
				+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
				+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
				+ "where mdhc.id =? and mlm.location_id not in ('HPT', 'C81', 'Z04') and pt.param_name = ? group by mlm.location_name";
		list = template.query(sql, new Object[] { id, parameterName },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationCircleByCategory(int id, List<String> locations) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category\r\n"
				+ "where mdhc.id =? and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) group by mlm.location_name";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationCircleByCategoryAndParameterName(int id, List<String> locations,
			String parameterName) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category\r\n"
				+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
				+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
				+ "where mdhc.id =? and pt.param_name = ? and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) group by mlm.location_name";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id, parameterName },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationDistrictByCategory(int id, List<String> locations) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.district_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, "
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.district_id\r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category where mdhc.id =? \r\n"
				+ "and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) group by mlm.district_name";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationZoneByCategorygetLocationZoneByCategory(int id,
			List<String> locations, String parameterName) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.zone_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, "
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.zone_id\r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category "
				+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
				+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
				+ "where mdhc.id =? and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) and pt.param_name = ? "
				+ "and mlm.zone_id != 'Z04' group by mlm.zone_name ";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id, parameterName },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationZoneByCategory(int id, List<String> locations) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.zone_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, "
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.zone_id\r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category where mdhc.id =? \r\n"
				+ "and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) "
				+ "and mlm.zone_id != 'Z04' group by mlm.zone_name ";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationZoneByCategoryAndParameterName(int id, List<String> locations,
			String parameterName) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.zone_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, "
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.zone_id\r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category "
				+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
				+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
				+ "where mdhc.id =? \r\n"
				+ "and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) and pt.param_name = ? "
				+ "and mlm.zone_id != 'Z04' group by mlm.zone_name ";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id, parameterName },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationCircleByCategoryFinancialyear(int id, String year,
			List<String> locations) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category\r\n"
				+ "where mdhc.id =? and erc.period =? and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) group by mlm.location_name";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id, year },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationCircleByCategoryFinancialyearAndParameterName(int id, String year,
			List<String> locations, String parameterName) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category\r\n"
				+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
				+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
				+ "where mdhc.id =? and erc.period =? and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) and pt.param_name = ? group by mlm.location_name";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id, year, parameterName },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationDistrictByCategoryFinancialyear(int id, String year,
			List<String> locations) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.district_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped,"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.district_id\r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category where mdhc.id =?  and erc.period = ? \r\n"
				+ "and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) group by mlm.district_name";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id, year },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationZoneByCategoryFinancialyear(int id, String year,
			List<String> locations) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.zone_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped,"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.zone_id\r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category where mdhc.id =?  and erc.period = ? \r\n"
				+ "and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) "
				+ "and mlm.zone_id != 'Z04' group by mlm.zone_name";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id, year },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getLocationZoneByCategoryFinancialyearAndParameterName(int id, String year,
			List<String> locations, String parameterName) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String inClause = locations.stream().map(loc -> "'" + loc + "'").collect(Collectors.joining(", "));
		String sql = "select mlm.zone_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped,"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.zone_id\r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category "
				+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
				+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
				+ "where mdhc.id =?  and erc.period = ? \r\n"
				+ "and erc.working_location IN (SELECT UNNEST(ARRAY[%s])) and pt.param_name = ? "
				+ "and mlm.zone_id != 'Z04' group by mlm.zone_name";
		sql = String.format(sql, inClause);
		list = template.query(sql, new Object[] { id, year, parameterName },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings({ "deprecation" })
	public List<DashboardDistrictCircle> getStateDistrictByCategory(int id) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.district_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped,"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.district_id \r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category where mdhc.id =? \r\n"
				+ "group by mlm.district_name";
		list = template.query(sql, new Object[] { id },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings("deprecation")
	public List<DashboardDistrictCircle> getStateCircleByCategoryFinancialyear(int id, String year) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category\r\n"
				+ "where mdhc.id =? and erc.period =? and mlm.location_id not in ('HPT', 'C81', 'Z04') group by mlm.location_name";
		list = template.query(sql, new Object[] { id, year },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings("deprecation")
	public List<DashboardDistrictCircle> getStateCircleByCategoryFinancialyearAndParameterName(int id, String year,
			String parameterName) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.location_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand, \r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, \r\n"
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_dd_location_details mlm inner join analytics.enforcement_review_case erc\r\n"
				+ "on erc.working_location = mlm.location_id "
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category\r\n"
				+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
				+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
				+ "where mdhc.id =? and erc.period =? and mlm.location_id not in ('HPT', 'C81', 'Z04') and pt.param_name = ? group by mlm.location_name";
		list = template.query(sql, new Object[] { id, year, parameterName },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}

	@SuppressWarnings("deprecation")
	public List<DashboardDistrictCircle> getStateDistrictByCategoryFinancialyear(int caterogryId,
			String financialyear) {
		List<DashboardDistrictCircle> list = new ArrayList<DashboardDistrictCircle>();
		String sql = "select mlm.district_name as juristiction, count(erc.*) as noOfCase, sum(erc.indicative_tax_value) as indicativeValue, COALESCE(sum(erc.demand),0) as demand,\r\n"
				+ "COALESCE(sum(erc.recovery_against_demand + erc.recovery_by_drc3), 0) as recoveredAmount, SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01aissued,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10issued, SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01issued,\r\n"
				+ "SUM(CASE WHEN erc.action in ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS notAcknowledege, SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demandByDrc07,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS caseDroped, "
				+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge' and erc.action_status IS NULL THEN 1 ELSE 0 END) AS notApplicable,\r\n"
				+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partialVoluentryDemand, SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demandDischageByDrc03,\r\n"
				+ "SUM(CASE WHEN erc.action = 'closed' THEN 1 ELSE 0 END) AS totalClosed \r\n"
				+ "from analytics.mst_location_mapping mlm inner join analytics.enforcement_review_case erc on erc.working_location = mlm.circle_id or erc.working_location = mlm.district_id  \r\n"
				+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.name = erc.category where mdhc.id =? and erc.period =? \r\n"
				+ "group by mlm.district_name";
		list = template.query(sql, new Object[] { caterogryId, financialyear },
				(rs, rowNum) -> new DashboardDistrictCircle(rs.getString("juristiction"), rs.getLong("noOfCase"),
						rs.getLong("indicativeValue"), rs.getLong("demand"), rs.getLong("recoveredAmount"),
						rs.getLong("drc01aissued"), rs.getLong("amst10issued"), rs.getLong("drc01issued"),
						rs.getLong("notAcknowledege"), rs.getLong("demandByDrc07"), rs.getLong("caseDroped"),
						rs.getLong("notApplicable"), rs.getLong("partialVoluentryDemand"),
						rs.getLong("demandDischageByDrc03"), rs.getLong("totalClosed")));
		return list;
	}
}
