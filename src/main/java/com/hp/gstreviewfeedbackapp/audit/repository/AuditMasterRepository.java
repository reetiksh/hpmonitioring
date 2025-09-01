package com.hp.gstreviewfeedbackapp.audit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.audit.model.AuditCaseStatus;
import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;

@Repository
public interface AuditMasterRepository extends JpaRepository<AuditMaster, String> {
	@Query(value = "select * from analytics.audit_master am where working_location in (?1)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationList(List<String> allMappedLocations);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and action = (?2)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndAction(List<String> allMappedLocations,
			Integer actionId);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and category in (?2)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndCatgoryId(List<String> allMappedLocations,
			Integer categoryId);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and category in (?2) and action = (?3)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndCatgoryIdAndAction(List<String> allMappedLocations,
			Integer categoryId, Integer actionId);

	@Query(value = "select am.* from analytics.audit_master am, analytics.audit_master_cases_allocating_users amcau, analytics.audit_case_status acs "
			+ "where am.case_id = amcau.case_id and acs.id = am.action and acs.status != 'auditcompleted' "
			+ "and working_location in (?1) and am.category in (?2) and amcau.l3_user = ?3", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndCatgoryIdAndL3UserId(List<String> allMappedLocations,
			Integer categoryId, Integer userId);

	@Query(value = "select am.* from analytics.audit_master am, analytics.audit_master_cases_allocating_users amcau, analytics.audit_case_status acs "
			+ "where am.case_id = amcau.case_id and acs.id = am.action and acs.status != 'auditcompleted' and working_location in (?1) and amcau.l3_user = ?2", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndL3UserId(List<String> allMappedLocations,
			Integer userId);

	@Query(value = "select distinct am.* from analytics.audit_master am,\r\n"
			+ "analytics.audit_case_status acs, analytics.audit_case_date_document_details acddd \r\n"
			+ "where acs.id = am.action and acs.status = 'auditcompleted' and am.working_location in (?1)\r\n"
			+ "and am.case_id in\r\n"
			+ "(select acddd.case_id from analytics.audit_case_date_document_details acddd, \r\n"
			+ "analytics.audit_case_status acs, analytics.audit_master_cases_allocating_users amcau\r\n"
			+ "where acddd.case_id = amcau.case_id and amcau.l3_user = ?3 and acs.id = acddd.action \r\n"
			+ "and acs.status in ('recommended_for_assessment_adjudication','recommended_for_enforcement','showCauseNotice')) and am.category in (?2)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndCatgoryIdAndL3UserId(
			List<String> allMappedLocations, Integer categoryId, Integer userId);

	@Query(value = "select distinct am.* from analytics.audit_master am,\r\n"
			+ "analytics.audit_case_status acs, analytics.audit_case_date_document_details acddd \r\n"
			+ "where acs.id = am.action and acs.status = 'auditcompleted' and am.working_location in (?1)\r\n"
			+ "and am.case_id in\r\n"
			+ "(select acddd.case_id from analytics.audit_case_date_document_details acddd, \r\n"
			+ "analytics.audit_case_status acs, analytics.audit_master_cases_allocating_users amcau\r\n"
			+ "where acddd.case_id = amcau.case_id and amcau.l3_user = ?2 and acs.id = acddd.action \r\n"
			+ "and acs.status in ('recommended_for_assessment_adjudication','recommended_for_enforcement','showCauseNotice')) ", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndL3UserId(
			List<String> allMappedLocations, Integer userId);

	@Query(value = "select distinct am.* from analytics.audit_master am,\r\n"
			+ "analytics.audit_case_status acs, analytics.audit_case_date_document_details acddd \r\n"
			+ "where acs.id = am.action and acs.status = 'auditcompleted' and am.working_location in (?1)\r\n"
			+ "and am.case_id in\r\n"
			+ "(select acddd.case_id from analytics.audit_case_date_document_details acddd, \r\n"
			+ "analytics.audit_case_status acs, analytics.audit_master_cases_allocating_users amcau\r\n"
			+ "where acddd.case_id = amcau.case_id and amcau.l2_user = ?3 and acs.id = acddd.action \r\n"
			+ "and acs.status in ('recommended_for_assessment_adjudication','recommended_for_enforcement','showCauseNotice')) and am.category in (?2)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndCatgoryIdAndL2UserId(
			List<String> allMappedLocations, Integer categoryId, Integer userId);

	@Query(value = "select distinct am.* from analytics.audit_master am,\r\n"
			+ "analytics.audit_case_status acs, analytics.audit_case_date_document_details acddd \r\n"
			+ "where acs.id = am.action and acs.status = 'auditcompleted' and am.working_location in (?1)\r\n"
			+ "and am.case_id in\r\n"
			+ "(select acddd.case_id from analytics.audit_case_date_document_details acddd, \r\n"
			+ "analytics.audit_case_status acs, analytics.audit_master_cases_allocating_users amcau\r\n"
			+ "where acddd.case_id = amcau.case_id and amcau.l2_user = ?2 and acs.id = acddd.action \r\n"
			+ "and acs.status in ('recommended_for_assessment_adjudication','recommended_for_enforcement','showCauseNotice')) ", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndL2UserId(
			List<String> allMappedLocations, Integer userId);

	@Query(value = "select distinct am.* from analytics.audit_master am,\r\n"
			+ "analytics.audit_case_status acs, analytics.audit_case_date_document_details acddd \r\n"
			+ "where acs.id = am.action and acs.status = 'auditcompleted' and am.working_location in (?1)\r\n"
			+ "and am.case_id in\r\n"
			+ "(select acddd.case_id from analytics.audit_case_date_document_details acddd, \r\n"
			+ "analytics.audit_case_status acs, analytics.audit_master_cases_allocating_users amcau\r\n"
			+ "where acddd.case_id = amcau.case_id and amcau.mcm_user = ?3 and acs.id = acddd.action \r\n"
			+ "and acs.status in ('recommended_for_assessment_adjudication','recommended_for_enforcement','showCauseNotice')) and am.category in (?2)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndCatgoryIdAndMcmUserId(
			List<String> allMappedLocations, Integer categoryId, Integer userId);

	@Query(value = "select distinct am.* from analytics.audit_master am,\r\n"
			+ "analytics.audit_case_status acs, analytics.audit_case_date_document_details acddd \r\n"
			+ "where acs.id = am.action and acs.status = 'auditcompleted' and am.working_location in (?1)\r\n"
			+ "and am.case_id in\r\n"
			+ "(select acddd.case_id from analytics.audit_case_date_document_details acddd, \r\n"
			+ "analytics.audit_case_status acs, analytics.audit_master_cases_allocating_users amcau\r\n"
			+ "where acddd.case_id = amcau.case_id and amcau.mcm_user = ?2 and acs.id = acddd.action \r\n"
			+ "and acs.status in ('recommended_for_assessment_adjudication','recommended_for_enforcement','showCauseNotice')) ", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesRecommendedToOtherModuleByWorkingLocationListAndMcmUserId(
			List<String> allMappedLocations, Integer userId);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and action in (?2) and am.assign_to = (?3)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndActionListAndAssignTo(List<String> allMappedLocations,
			List<Integer> caseStatusList, String assignTo);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and category in (?2) and action in (?3) and am.assign_to = (?4)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionListAndAssignTo(
			List<String> allMappedLocations, Integer categoryId, List<Integer> caseStatusList, String assignTo);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and action = (?2) and am.assign_to = (?3)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndActionAndAssignTo(List<String> allMappedLocations,
			Integer id, String assignTo);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and category in (?2) and action = (?3) and am.assign_to = (?4)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionAndAssignTo(
			List<String> allMappedLocations, Integer categoryId, Integer id, String assignTo);

	Optional<AuditMaster> findByCaseIdAndAction(String caseId, AuditCaseStatus auditCaseStatus);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and action in (?2) and am.assign_to = (?3) and am.assigned_from = (?4)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndActionListAndAssignToAndAssignedFrom(
			List<String> allMappedLocations, List<Integer> caseStatusList, String assignTo, String assignedFrom);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and category in (?2) and action in (?3) and am.assign_to = (?4) and am.assigned_from = (?5)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionListAndAssignToAndAssignedFrom(
			List<String> allMappedLocations, Integer categoryId, List<Integer> caseStatusList, String assignTo,
			String assignedFrom);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and action = (?2) and am.assign_to = (?3) and am.fully_recovered = (?4)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndActionAndAssignToAndFullyRecovered(
			List<String> allMappedLocations, Integer statusId, String assignTo, String fullyRecovered);

	@Query(value = "select * from analytics.audit_master am where working_location in (?1) and category in (?2) and action = (?3) and am.assign_to = (?4) and am.fully_recovered = (?4)", nativeQuery = true)
	List<AuditMaster> findAllAuditCasesByWorkingLocationListAndCatgoryIdAndActionAndAssignToAndFullyRecovered(
			List<String> allMappedLocations, Integer categoryId, Integer statusId, String assignTo,
			String fullyRecovered);

	@Query(value = "select * from analytics.audit_master mst \r\n" + "where mst.gstin =?1 and mst.period =?2 \r\n"
			+ "and mst.parameter like CONCAT('%', ?3, '%') and action != 23", nativeQuery = true)
	List<AuditMaster> findAuditCase(String gstin, String period, String parameter);

	@Query(value = "select mdhc.\"name\", mpmw.param_name, count(case_id), split_part(am.parameter, ',', 1), mdhc.id from analytics.audit_master am\r\n"
			+ "inner join analytics.mst_dd_hq_category mdhc on am.category = mdhc.id \r\n"
			+ "inner join analytics.audit_case_status acs on am.action = acs.id \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(am.parameter , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "where split_part(am.parameter, ',', 1) = ?2 and am.category = ?1 and am.working_location in (?3) \r\n"
			+ "group by mdhc.name, mpmw.param_name, split_part(am.parameter, ',', 1), mdhc.id", nativeQuery = true)
	List<Object[]> getAllCaseCountByCategoryAnd1stParameterId(Long categoryId, String parameterId,
			List<String> locationList);

	@Query(value = "select mdhc.name, mpmw.param_name, count(case_id), split_part(am.parameter, ',', 1) as parameter_id , mdhc.id as category_id from analytics.audit_master am\r\n"
			+ "inner join analytics.mst_dd_hq_category mdhc on am.category = mdhc.id \r\n"
			+ "inner join analytics.audit_case_status acs on am.action = acs.id \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(am.parameter , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "where am.category = ?1 and am.working_location in (?2) group by mdhc.name, mpmw.param_name, split_part(am.parameter, ',', 1), mdhc.id", nativeQuery = true)
	List<Object[]> getAllCaseCountByCategory(Long categoryId, List<String> locationList);

	@Query(value = "SELECT case_id, gstin, arn_number, assign_to, assigned_date_froml2tol3, assigned_from, case_reporting_date, \r\n"
			+ "extension_no, fully_recovered, indicative_tax_value, last_updated_timestamp, STRING_AGG(pt.param_name , ', ') AS \"parameter\", \"period\", \r\n"
			+ "recommended_module, scrutiny_case_id, taxpayer_name, toal_amount_to_be_recovered, total_involved_amount, \r\n"
			+ "\"action\", extension_file_name_id, category, fo_user_details_for_show_cause_notice_user_id, working_location\r\n"
			+ "FROM analytics.audit_master am\r\n"
			+ "JOIN LATERAL unnest(string_to_array(am.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text)\r\n"
			+ "where split_part(am.parameter, ',', 1) = ?2 and am.category = ?1 and am.working_location in (?3) \r\n"
			+ "group by case_id, gstin, arn_number, assign_to, assigned_date_froml2tol3, assigned_from, case_reporting_date, \r\n"
			+ "extension_no, fully_recovered, indicative_tax_value, last_updated_timestamp, \"period\", \r\n"
			+ "recommended_module, scrutiny_case_id, taxpayer_name, toal_amount_to_be_recovered, total_involved_amount, \r\n"
			+ "\"action\", extension_file_name_id, category, fo_user_details_for_show_cause_notice_user_id, working_location", nativeQuery = true)
	List<AuditMaster> findAllCasesByCategoryAnd1stParameterId(Long categoryId, String parameterId,
			List<String> locationList);

	@Query(value = "SELECT case_id, gstin, arn_number, assign_to, assigned_date_froml2tol3, assigned_from, case_reporting_date, \r\n"
			+ "extension_no, fully_recovered, indicative_tax_value, last_updated_timestamp, STRING_AGG(pt.param_name , ', ') AS \"parameter\", \"period\", \r\n"
			+ "recommended_module, scrutiny_case_id, taxpayer_name, toal_amount_to_be_recovered, total_involved_amount, \r\n"
			+ "\"action\", extension_file_name_id, category, fo_user_details_for_show_cause_notice_user_id, working_location\r\n"
			+ "FROM analytics.audit_master am\r\n"
			+ "JOIN LATERAL unnest(string_to_array(am.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text)\r\n"
			+ "where am.category = ?1 and am.working_location in (?3) \r\n"
			+ "group by case_id, gstin, arn_number, assign_to, assigned_date_froml2tol3, assigned_from, case_reporting_date, \r\n"
			+ "extension_no, fully_recovered, indicative_tax_value, last_updated_timestamp, \"period\", \r\n"
			+ "recommended_module, scrutiny_case_id, taxpayer_name, toal_amount_to_be_recovered, total_involved_amount, \r\n"
			+ "\"action\", extension_file_name_id, category, fo_user_details_for_show_cause_notice_user_id, working_location", nativeQuery = true)
	List<AuditMaster> findAllCasesByCategory(Long categoryId, List<String> locationList);

	@Query(value = "select mdhc.name, mpmw.param_name, count(am.case_id), split_part(am.parameter, ',', 1), mdhc.id from analytics.audit_master am\r\n"
			+ "inner join analytics.mst_dd_hq_category mdhc on am.category = mdhc.id \r\n"
			+ "inner join analytics.audit_case_status acs on am.action = acs.id \r\n"
			+ "inner join analytics.audit_master_cases_allocating_users amcau on amcau.case_id = am.case_id \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(am.parameter , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "where split_part(am.parameter, ',', 1) = ?2 and am.category = ?1 and am.working_location in (?3) and amcau.l3_user = ?4\r\n"
			+ "group by mdhc.name, mpmw.param_name, split_part(am.parameter, ',', 1), mdhc.id", nativeQuery = true)
	List<Object[]> getAllCaseCountByCategoryAnd1stParameterIdAndL3UserId(Long id, String string,
			List<String> locationList, Integer l3UserId);

	@Query(value = "select mdhc.name, mpmw.param_name, count(am.case_id), split_part(am.parameter, ',', 1) as parameter_id , mdhc.id as category_id from analytics.audit_master am\r\n"
			+ "inner join analytics.mst_dd_hq_category mdhc on am.category = mdhc.id \r\n"
			+ "inner join analytics.audit_case_status acs on am.action = acs.id \r\n"
			+ "inner join analytics.audit_master_cases_allocating_users amcau on amcau.case_id = am.case_id \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(am.parameter , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "where am.category = ?1 and am.working_location in (?2) and amcau.l3_user = ?3 group by mdhc.name, mpmw.param_name, split_part(am.parameter, ',', 1), mdhc.id", nativeQuery = true)
	List<Object[]> getAllCaseCountByCategoryAndL3UserId(Long id, List<String> locationList, Integer l3userId);

	@Query(value = "SELECT am.case_id, gstin, arn_number, assign_to, assigned_date_froml2tol3, assigned_from, case_reporting_date, \r\n"
			+ "extension_no, fully_recovered, indicative_tax_value, last_updated_timestamp, STRING_AGG(pt.param_name , ', ') AS parameter, period, \r\n"
			+ "recommended_module, scrutiny_case_id, taxpayer_name, toal_amount_to_be_recovered, total_involved_amount, \r\n"
			+ "action, extension_file_name_id, category, fo_user_details_for_show_cause_notice_user_id, working_location\r\n"
			+ "FROM analytics.audit_master am\r\n"
			+ "inner join analytics.audit_master_cases_allocating_users amcau on amcau.case_id = am.case_id \r\n"
			+ "JOIN LATERAL unnest(string_to_array(am.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text)\r\n"
			+ "where split_part(am.parameter, ',', 1) = ?2 and am.category = ?1 and am.working_location in (?3) and amcau.l3_user = ?4 \r\n"
			+ "group by am.case_id, gstin, arn_number, assign_to, assigned_date_froml2tol3, assigned_from, case_reporting_date, \r\n"
			+ "extension_no, fully_recovered, indicative_tax_value, last_updated_timestamp, period, \r\n"
			+ "recommended_module, scrutiny_case_id, taxpayer_name, toal_amount_to_be_recovered, total_involved_amount, \r\n"
			+ "action, extension_file_name_id, category, fo_user_details_for_show_cause_notice_user_id, working_location", nativeQuery = true)
	List<AuditMaster> findAllCasesByCategoryAnd1stParameterIdAndL3UserId(Long id, String parameterId,
			List<String> locationList, Integer l3UserId);
	
	
	  @Query(value = "SELECT * FROM ( "
	  		+ "    SELECT \n"
	  		+ "        mlm.zone_name,\n"
	  		+ "        mlm.circle_name,\n"
	  		+ " 	COUNT(DISTINCT am.case_id) AS allotted_cases,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN am.action in (17,18,19,20,21,22,23) THEN am.case_id END) AS audit_cases_completed,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (1,2,3) THEN am.case_id END) AS pending_desk_review,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in(4,5,6) THEN am.case_id END) AS pending_approval_audit_plan,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (7,8) THEN am.case_id END) AS pending_examination_books,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (9,10,11,12,13,14,15) THEN am.case_id END) AS pending_DAR,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (16) THEN am.case_id END) AS pending_FAR,\n"
	  		+ "        0 AS sort_order,\n"
	  		+ "        mlm.zone_name AS zone_sort\n"
	  		+ "    FROM analytics.audit_master am\n"
	  		+ "    JOIN analytics.audit_case_status acs ON am.action = acs.id\n"
	  		+ "    JOIN analytics.mst_location_mapping mlm ON am.working_location = mlm.circle_id where working_location in (?1)\n"
	  		+ "    GROUP BY mlm.zone_name, mlm.circle_name\n"
	  		+ "\n"
	  		+ "    UNION ALL \n"
	  		+ "\n"
	  		+ "    SELECT \n"
	  		+ "        'Zone Total' AS zone_name,\n"
	  		+ "        '' AS circle_name,\n"
	  		+ "        COUNT(DISTINCT am.case_id) AS allotted_cases,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN am.action in (17,18,19,20,21,22,23) THEN am.case_id END) AS audit_cases_completed,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (1,2,3) THEN am.case_id END) AS pending_desk_review,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in(4,5,6) THEN am.case_id END) AS pending_approval_audit_plan,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (7,8) THEN am.case_id END) AS pending_examination_books,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (9,10,11,12,13,14,15) THEN am.case_id END) AS pending_DAR,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (16) THEN am.case_id END) AS pending_FAR,\n"
	  		+ "        1 AS sort_order,\n"
	  		+ "        mlm.zone_name AS zone_sort\n"
	  		+ "    FROM analytics.audit_master am\n"
	  		+ "    JOIN analytics.audit_case_status acs ON am.action = acs.id\n"
	  		+ "    JOIN analytics.mst_location_mapping mlm ON am.working_location = mlm.circle_id where working_location in (?1)\n"
	  		+ "    GROUP BY mlm.zone_name\n"
	  		+ "\n"
	  		+ "    UNION ALL \n"
	  		+ "\n"
	  		+ "    SELECT \n"
	  		+ "        'Grand Total' AS zone_name,\n"
	  		+ "        '' AS circle_name,\n"
	  		+ "        COUNT(DISTINCT am.case_id) AS allotted_cases,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN am.action in (17,18,19,20,21,22,23) THEN am.case_id END) AS audit_cases_completed,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (1,2,3) THEN am.case_id END) AS pending_desk_review,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in(4,5,6) THEN am.case_id END) AS pending_approval_audit_plan,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (7,8) THEN am.case_id END) AS pending_examination_books,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (9,10,11,12,13,14,15) THEN am.case_id END) AS pending_DAR,\n"
	  		+ "    COUNT(DISTINCT CASE WHEN acs.sequence in (16) THEN am.case_id END) AS pending_FAR,\n"
	  		+ "        2 AS sort_order,\n"
	  		+ "        'ZZZ' AS zone_sort\n"
	  		+ "    FROM analytics.audit_master am\n"
	  		+ "    JOIN analytics.audit_case_status acs ON am.action = acs.id\n"
	  		+ "    JOIN analytics.mst_location_mapping mlm ON am.working_location = mlm.circle_id where working_location in (?1) \n"
	  		+ ") AS final_report "
	  		+ " ORDER BY \n"
	  		+ "    zone_sort, \n"
	  		+ "    sort_order, \n"
	  		+ "    circle_name;\n"
	  		+ "", nativeQuery = true)
	    List<Object[]> getAnnexureReportRaw(List<String> allMappedLocations);
	    
	    
	    @Query(value = "                SELECT       \n"
	    		+ "	  		 	COUNT(DISTINCT am.case_id )  AS allotted_cases, \n"
	    		+ "	  		 	COUNT(DISTINCT CASE WHEN acs.sequence>1 THEN am.case_id END)  AS assigned_cases, \n"
	    		+ "	  		    COUNT(DISTINCT CASE WHEN am.action in (17,18,19,20,21,22,23) THEN am.case_id END) AS audit_cases_completed, \n"
	    		+ "	  		    COUNT(DISTINCT CASE WHEN acs.sequence in(3,4,5,6) THEN am.case_id END) AS pending_approval_audit_plan, \n"
	    		+ "	  		    COUNT(DISTINCT CASE WHEN acs.sequence in (7,8,9,10,11,12,13,14,15) THEN am.case_id END) AS pending_DAR, \n"
	    		+ "	  		    COUNT(DISTINCT CASE WHEN acs.sequence in (16) THEN am.case_id END) AS pending_FAR \n"
	    		+ "	  		    FROM analytics.audit_master am \n"
	    		+ "	  		    JOIN analytics.audit_case_status acs ON am.action = acs.id  \n"
	    		+ "	  		    JOIN analytics.mst_location_mapping mlm ON am.working_location = mlm.circle_id where working_location in (?1)  ", nativeQuery = true)
	    List<Object[]> getDashboardMetrics(List<String> allMappedLocations);
	
}
