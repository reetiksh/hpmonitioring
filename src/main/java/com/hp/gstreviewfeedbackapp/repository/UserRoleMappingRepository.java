package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.StateDetails;
import com.hp.gstreviewfeedbackapp.model.SubAppMenu;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {

	List<UserRoleMapping> findAllByUserRole(UserRole userRole);

	List<UserRoleMapping> findAllByUserDetailsAndUserRole(UserDetails userDetails, UserRole userRole);

	List<UserRoleMapping> findByUserDetailsOrderByUserRole(UserDetails userDetails);

	List<UserRoleMapping> findAllByUserDetails(UserDetails userDetails);

	@Query(value = "select sub_url,sub_name from analytics.mst_tbl_sub_app_menu a join analytics.mst_tbl_app_menu b on menuid=id \r\n"
			+ "where b.user_type ='vw'\r\n" + "group by sub_url,sub_name,a.priority\r\n"
			+ "order by a.priority ", nativeQuery = true)
	List<Object[]> findSubMenu();

	@Query(value = "select distinct mlm.zone_name AS jurisdiction,\r\n"
			+ "COUNT(distinct user_id) AS user_count,\r\n" + "COUNT(erc.*) AS no_of_cases,\r\n"
			+ "SUM(erc.indicative_tax_value) AS indicative_value,\r\n" + "COALESCE(SUM(erc.demand),0) AS demand,\r\n"
			+ "COALESCE(SUM(erc.recovery_against_demand + erc.recovery_by_drc3), 0) AS recovered_amount,\r\n"
			+ "SUM(CASE WHEN erc.action IN ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS not_acknowledge,\r\n"
			+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge'\r\n"
			+ "and erc.action_status IS NULL THEN 1 ELSE 0 END) AS not_applicable,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01a_issued,\r\n"
			+ " SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10_issued,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01_issued,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demand_by_drc07,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS case_dropped,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partial_voluntary_demand,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demand_discharge_by_drc03,\r\n"
			+ "mlm.zone_id AS zone_code, \r\n" + "erc.period AS year \r\n"
			+ "from analytics.mst_user_role_mapping murm\r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id or murm.zone_id = mlm.zone_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id \r\n"
			+ "inner JOIN analytics.mst_dd_hq_category mdhc ON erc.category = mdhc.name \r\n" + "WHERE\r\n"
			+ "murm.user_role_id = 2\r\n" + "AND murm.circle_id IN (?1)\r\n" + "AND erc.period IN (?2)\r\n"
			+ "AND mdhc.id IN (?3) and mlm.zone_id != 'Z04' \r\n" + "GROUP by mlm.zone_name,mlm.zone_id,erc.period", nativeQuery = true)
	List<Object[]> zoneWiseFoCount(List<String> locationDistId, List<String> year, List<Long> id);

	@Query(value = "select distinct mlm.zone_name AS jurisdiction,\r\n"
			+ "			COUNT(distinct user_id) AS user_count, COUNT(erc.*) AS no_of_cases,\r\n"
			+ "			SUM(erc.indicative_tax_value) AS indicative_value, COALESCE(SUM(erc.demand),0) AS demand,\r\n"
			+ "			COALESCE(SUM(erc.recovery_against_demand + erc.recovery_by_drc3), 0) AS recovered_amount,\r\n"
			+ "			SUM(CASE WHEN erc.action IN ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS not_acknowledge,\r\n"
			+ "			SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge'\r\n"
			+ "			and erc.action_status IS NULL THEN 1 ELSE 0 END) AS not_applicable,\r\n"
			+ "			SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01a_issued,\r\n"
			+ "			 SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10_issued,\r\n"
			+ "			SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01_issued,\r\n"
			+ "			SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demand_by_drc07,\r\n"
			+ "			SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS case_dropped,\r\n"
			+ "			SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partial_voluntary_demand,\r\n"
			+ "			SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demand_discharge_by_drc03,\r\n"
			+ "			mlm.zone_id AS zone_code  from analytics.mst_user_role_mapping murm\r\n"
			+ "			inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id or murm.zone_id  = mlm.zone_id\r\n"
			+ "			inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id\r\n"
			+ "			inner JOIN analytics.mst_dd_hq_category mdhc ON erc.category = mdhc.name  WHERE\r\n"
			+ "			murm.user_role_id = 2 AND murm.circle_id IN (?1) AND erc.period IN (?2)\r\n"
			+ "			AND mdhc.id IN (?3) and mlm.zone_id != 'Z04' GROUP by mlm.zone_name ,mlm.zone_id", nativeQuery = true)
	List<Object[]> zoneAverageWiseFoCount(List<String> locationDistId, List<String> year, List<Long> id);

	@Query(value = "select\r\n"
			+ "    ROW_NUMBER() OVER (ORDER BY mlm.zone_name, ud.first_name, ud.last_name) AS row_num,\r\n"
			+ "    mlm.zone_name AS jurisdiction,mlm.circle_name,\r\n"
			+ "    ud.first_name,ud.last_name,ud.login_name,ud.email_id,ud.mobile_number,\r\n"
			+ "    COUNT(distinct murm.user_id) AS user_count,\r\n" + "    COUNT(erc.*) AS no_of_cases,\r\n"
			+ "    SUM(erc.indicative_tax_value) AS indicative_value,\r\n"
			+ "    COALESCE(SUM(erc.demand),0) AS demand,\r\n"
			+ "    COALESCE(SUM(erc.recovery_against_demand + erc.recovery_by_drc3), 0) AS recovered_amount,\r\n"
			+ "    SUM(CASE WHEN erc.action IN ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS not_acknowledge,\r\n"
			+ "    SUM(CASE WHEN erc.action = 'acknowledge' OR case_stage = 7 THEN 1 ELSE 0 END) AS not_applicable,\r\n"
			+ "    SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01a_issued,\r\n"
			+ "    SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10_issued,\r\n"
			+ "    SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01_issued,\r\n"

			+ "    SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demand_by_drc07,\r\n"
			+ "    SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS case_dropped,\r\n"
			+ "    SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partial_voluntary_demand,\r\n"
			+ "    SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demand_discharge_by_drc03,\r\n"
			+ "    erc.period " + "FROM\r\n"
			+ "    analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "    inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "    inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.zone_id\r\n"
			+ "    inner JOIN analytics.mst_dd_hq_category mdhc ON erc.category = mdhc.name \r\n" + "WHERE\r\n"
			+ "    murm.user_role_id = 2  and  mlm.zone_id= ?2 \r\n" + "    AND murm.circle_id IN (?1)\r\n"
			+ "    AND erc.period IN (?3)\r\n" + " AND mdhc.id IN (?4)\r\n" + "GROUP BY\r\n"
			+ "    mlm.zone_name,ud.first_name,ud.last_name,ud.login_name,ud.email_id,ud.mobile_number,mlm.circle_name,erc.period\r\n"
			+ ";", nativeQuery = true)
	List<Object[]> circleWiseFoCount(List<String> locationDistId, String zoneName, List<String> year,
			List<Long> categoryId);

	/*
	 * @Query(value="\r\n" + "select\r\n" +
	 * "    ROW_NUMBER() OVER (ORDER BY mlm.district_name, ud.first_name, ud.last_name) AS row_num,\r\n"
	 * +
	 * " ud.first_name,ud.last_name,ud.login_name,ud.email_id,ud.mobile_number,\r\n"
	 * + "    mlm.district_name AS jurisdiction,mlm.circle_name,\r\n" +
	 * "    COUNT(distinct murm.user_id) AS user_count,\r\n" +
	 * "    COUNT(erc.*) AS no_of_cases,\r\n" +
	 * "    SUM(erc.indicative_tax_value) AS indicative_value,\r\n" +
	 * "    COALESCE(SUM(erc.demand),0) AS demand,\r\n" +
	 * "    COALESCE(SUM(erc.recovery_against_demand + erc.recovery_by_drc3), 0) AS recovered_amount,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.action IN ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS not_acknowledge,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.action = 'acknowledge' OR case_stage = 7 THEN 1 ELSE 0 END) AS not_applicable,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01a_issued,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10_issued,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01_issued,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demand_by_drc07,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS case_dropped,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partial_voluntary_demand,\r\n"
	 * +
	 * "    SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demand_discharge_by_drc03\r\n"
	 * + "FROM\r\n" +
	 * "    analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
	 * +
	 * "    inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
	 * +
	 * "    inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
	 * + "WHERE\r\n" + "    murm.user_role_id = 2\r\n" +
	 * "    AND murm.circle_id IN (?1)\r\n" + "GROUP BY\r\n" +
	 * "    mlm.district_name,ud.first_name,ud.last_name,ud.login_name,ud.email_id,ud.mobile_number,mlm.circle_name\r\n"
	 * + ";\r\n" + "\r\n" + "",nativeQuery=true) List<Object[]>
	 * FoWiseCount(List<String> locationDistId);
	 */

	@Query(value = "select ROW_NUMBER() OVER (ORDER BY mlm.zone_name, ud.first_name, ud.last_name) AS row_num,\r\n"
			+ "ud.first_name,ud.last_name,ud.login_name,ud.email_id,ud.mobile_number,\r\n"
			+ "mlm.zone_name AS jurisdiction,mlm.circle_name,\r\n"
			+ "COUNT(distinct murm.user_id) AS user_count,\r\n" + "COUNT(erc.*) AS no_of_cases,\r\n"
			+ "SUM(erc.indicative_tax_value) AS indicative_value,\r\n" + "COALESCE(SUM(erc.demand),0) AS demand,\r\n"
			+ "COALESCE(SUM(erc.recovery_against_demand + erc.recovery_by_drc3), 0) AS recovered_amount,\r\n"
			+ "SUM(CASE WHEN erc.action IN ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS not_acknowledge,\r\n"
			+ "SUM(CASE WHEN erc.action = 'acknowledge' and erc.action_status = 1 or erc.action = 'acknowledge'\r\n"
			+ "and erc.action_status IS NULL THEN 1 ELSE 0 END) AS not_applicable,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01a_issued,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10_issued,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01_issued,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demand_by_drc07,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS case_dropped,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partial_voluntary_demand,\r\n"
			+ "SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demand_discharge_by_drc03,\r\n"
			+ "erc.period AS year, " + "ud.user_id AS userId, " + "murm.circle_id AS circleId, "
			+ "mdhc.id AS category "
			+ "from analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id\r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id or murm.zone_id = mlm.zone_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id \r\n"
			+ "inner JOIN analytics.mst_dd_hq_category mdhc ON erc.category = mdhc.name \r\n"
			+ "where murm.user_role_id = 2 AND murm.circle_id IN (?1)\r\n" + "AND erc.period IN (?2)\r\n"
			+ "AND mdhc.id IN (?3)\r\n"
			+ "GROUP by mlm.zone_name,ud.first_name,ud.last_name,ud.login_name,ud.email_id,ud.mobile_number,erc.period,ud.user_id,mdhc.id,mlm.circle_name,murm.circle_id", nativeQuery = true)
	List<Object[]> FoWiseCount(List<String> locationDistId, List<String> year, List<Long> id);

	@Query(value = "select\r\n" + "	 		distinct \r\n" + "	 		    COUNT(distinct user_id) AS user_count,\r\n"
			+ "	 		    COUNT(erc.*) AS no_of_cases,\r\n"
			+ "	 		    SUM(erc.indicative_tax_value) AS indicative_value,\r\n"
			+ "	 		    COALESCE(SUM(erc.demand),0) AS demand,\r\n"
			+ "	 		    COALESCE(SUM(erc.recovery_against_demand + erc.recovery_by_drc3), 0) AS recovered_amount,\r\n"
			+ "	 		    SUM(CASE WHEN erc.action IN ('upload', 'transfer', 'hqTransfer') THEN 1 ELSE 0 END) AS not_acknowledge,\r\n"
			+ "	 		    SUM(CASE WHEN erc.action = 'acknowledge' OR case_stage = 7 THEN 1 ELSE 0 END) AS not_applicable,\r\n"
			+ "	 		    SUM(CASE WHEN erc.case_stage = 1 THEN 1 ELSE 0 END) AS drc01a_issued,\r\n"
			+ "	 		    SUM(CASE WHEN erc.case_stage = 2 THEN 1 ELSE 0 END) AS amst10_issued,\r\n"
			+ "	 		    SUM(CASE WHEN erc.case_stage = 3 THEN 1 ELSE 0 END) AS drc01_issued,\r\n"
			+ "	 		    SUM(CASE WHEN erc.case_stage = 5 THEN 1 ELSE 0 END) AS demand_by_drc07,\r\n"
			+ "	 		    SUM(CASE WHEN erc.case_stage = 6 THEN 1 ELSE 0 END) AS case_dropped,\r\n"
			+ "	 		    SUM(CASE WHEN erc.case_stage = 8 THEN 1 ELSE 0 END) AS partial_voluntary_demand,\r\n"
			+ "	 		    SUM(CASE WHEN erc.case_stage = 9 THEN 1 ELSE 0 END) AS demand_discharge_by_drc03\r\n"
			+ "	 		    \r\n" + "	 		FROM\r\n" + "	 		    analytics.mst_user_role_mapping murm\r\n"
			+ "	 		    inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "	 		    inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "	 		WHERE\r\n" + "	 		    murm.user_role_id = 2\r\n"
			+ "	 		    AND murm.circle_id IN (?1)\r\n", nativeQuery = true)
	List<Object[]> locationWiseTotalCount(List<String> locationDistId);

	@Query(value = "select *  from analytics.mst_user_role_mapping urm where urm.user_id =?1 and urm.user_role_id= ?2 ", nativeQuery = true)
	List<UserRoleMapping> findAllRegionsMapWithUsers(Integer loginId, int roleId);

	@Query(value = "select *  from analytics.mst_user_role_mapping urm where urm.user_id =?1 and urm.user_role_id='2'", nativeQuery = true)
	List<UserRoleMapping> findAllRolesMapWithFOUsers(Integer loginId);

	@Query(value = "select table1.role_name, cd.circle_name, zd.zone_name, sd.state_name from analytics.mst_user_role_mapping urm,\r\n"
			+ "analytics.mst_dd_circle_details cd, analytics.mst_dd_zone_details zd, analytics.mst_dd_state_details sd,\r\n"
			+ "(select distinct urm.user_role_id, ur.name as role_name from analytics.mst_user_role_mapping urm, analytics.mst_user_role ur where urm.user_id = ?1 and ur.id = urm.user_role_id) table1\r\n"
			+ "where urm.user_id = ?1 and urm.user_role_id = table1.user_role_id and urm.circle_id = cd.circle_id and urm.zone_id = zd.zone_id \r\n"
			+ "and urm.state_id = sd.state_id order by table1.role_name", nativeQuery = true)
	List<List<String>> findAllUserRoleAndLocationByUserId(Integer userId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where user_id = ?1 and user_role_id  in (?2) \r\n"
			+ "and (circle_id in (?3) or zone_id in (?3) or state_id in (?3))", nativeQuery = true)
	List<UserRoleMapping> findUserByUserIdAndRolesAndLocations(Integer userId, List<Integer> roleList,
			List<String> locationList);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where user_id = ?1 and (circle_id in (?2) or zone_id in (?2) or state_id in (?2))", nativeQuery = true)
	List<UserRoleMapping> findUserByUserIdAndLocations(Integer userId, List<String> locationList);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where user_id = ?1 and user_role_id in(?2)", nativeQuery = true)
	List<UserRoleMapping> findUserByUserIdAndRoles(Integer userId, List<Integer> roleList);

	@Query(value = "select * from analytics.mst_user_role_mapping murm \r\n"
			+ "where user_id = ?1 and user_role_id = ?2 and circle_id = ?3 and zone_id = ?4 and state_id = ?5", nativeQuery = true)
	Optional<UserRoleMapping> findByUserIdAndUserRoleIdAndCircleIdAndZoneIdAndStateId(
			Integer userId, int userRoleId, String circleId, String zoneId, String stateId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where user_id = ?1 and user_role_id = ?2 and zone_id != 'NA'", nativeQuery = true)
	List<UserRoleMapping> findByUserIdAndUserRoleIdAndZoneList(Integer userId, int roleId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where user_id = ?1 and user_role_id = ?2 and enforcement_zone_id != 'NA'", nativeQuery = true)
	List<UserRoleMapping> findByUserIdAndUserRoleIdAndEnfZoneList(Integer userId, int roleId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where user_id = ?1 and user_role_id = ?2", nativeQuery = true)
	List<UserRoleMapping> findByUserIdAndUserRoleId(Integer userId, int roleId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where user_id = ?1 and user_role_id = ?2 and state_id = ?3", nativeQuery = true)
	List<UserRoleMapping> findByUserIdAndUserRoleIdAndStateId(Integer userId, int roleId, String string);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where murm.user_id = ?1 and murm.user_role_id = ?2 and \r\n"
			+ "((murm.circle_id in (?3) \r\n"
			+ "and murm.zone_id in ('NA') and murm.state_id in ('NA'))\r\n"
			+ "or (murm.circle_id in ('NA')\r\n"
			+ "and murm.zone_id in (?3) and murm.state_id in ('NA'))\r\n"
			+ "or (murm.circle_id in ('NA')\r\n"
			+ "and murm.zone_id in ('NA') and murm.state_id in (?3)));", nativeQuery = true)
	List<UserRoleMapping> getAllUrmByUserIdRoleIdLocationList(Integer userId, int id, List<String> allLocations);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where murm.user_role_id = ?1 and \r\n"
			+ "((murm.circle_id in (?2)\r\n"
			+ "and murm.zone_id in ('NA') and murm.state_id in ('NA'))\r\n"
			+ "or (murm.circle_id in ('NA')\r\n"
			+ "and murm.zone_id in (?2) and murm.state_id in ('NA'))\r\n"
			+ "or (murm.circle_id in ('NA')\r\n"
			+ "and murm.zone_id in ('NA') and murm.state_id in (?2)));", nativeQuery = true)
	List<UserRoleMapping> getAllUrmByRoleIdLocationList(Integer roleId, List<String> allLocations);

	Optional<UserRoleMapping> findByUserDetailsAndUserRoleAndStateDetails(UserDetails userDetails, UserRole userRole,
			StateDetails stateDetails);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where circle_id = ?1 and user_role_id = ?2", nativeQuery = true)
	List<UserRoleMapping> findbyCircleIdAndUserRoleId(String location, Integer userRoleId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where enforcement_zone_id = ?1 and user_role_id = ?2", nativeQuery = true)
	List<UserRoleMapping> findbyEnforcementZoneIdAndUserRoleId(String location, Integer userRoleId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where zone_id = ?1 and user_role_id = ?2", nativeQuery = true)
	List<UserRoleMapping> findbyZoneIdAndUserRoleId(String location, Integer userRoleId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where state_id = ?1 and user_role_id = ?2", nativeQuery = true)
	List<UserRoleMapping> findbyStateIdAndUserRoleId(String location, Integer userRoleId);

	@Query(value = "select * from analytics.mst_user_role_mapping murm where user_id = ?1 and user_role_id = ?2", nativeQuery = true)
	List<UserRoleMapping> findLocationByRolebased(Integer userId, Integer userRoleId);

	@Query(value = "select mlm.location_name, ocum.working_location, SUM(ocum.indicative_tax_value),\r\n"
			+ "count(ocum.*)  from analytics.old_cases_upload_manually ocum left join \r\n"
			+ "analytics.mst_dd_location_details mlm on ocum.working_location = mlm.location_id\r\n"
			+ "where ocum.is_uploaded = false and ocum.working_location like ?1 \r\n"
			+ "group by ocum.working_location, mlm.location_name", nativeQuery = true)
	List<Object[]> getOldCasesManuallyUploaded(String location);

	@Query(value = "select mlm.location_name, ocum.* from analytics.old_cases_upload_manually ocum left join \r\n"
			+ "analytics.mst_dd_location_details mlm on ocum.working_location = mlm.location_id\r\n"
			+ "where ocum.is_uploaded = false and ocum.working_location = ?1 ", nativeQuery = true)
	List<Object[]> getOldCasesManuallyUploadedByLocation(String location);

	@Query(value="select *  from analytics.mst_user_role_mapping urm where urm.user_id =?1 and urm.user_role_id='14'",nativeQuery=true)
	List<UserRoleMapping>  findAllRolesMapWithCagFOUsers(Integer loginId);
	
	
}
