package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.MstParametersModuleWise;

@Repository
public interface MstParametersModuleWiseRepository extends JpaRepository<MstParametersModuleWise, Integer> {
	Boolean existsByParamNameAndStatusCag(String paramName, boolean statusCag);

	Boolean existsByParamNameAndStatusAudit(String paramName, boolean statusAudit);

	Boolean existsByParamNameAndStatusScrutiny(String paramName, boolean statusScrutiny);

	Boolean existsByParamNameAndStatusAssessment(String paramName, boolean statusAssessment);

	Optional<MstParametersModuleWise> findByParamNameAndStatusCag(String paramName, boolean statusCag);

	Optional<MstParametersModuleWise> findByParamNameAndStatusAudit(String paramName, boolean statusAudit);

	Optional<MstParametersModuleWise> findByParamNameAndStatusScrutiny(String paramName, boolean statusScrutiny);

	Optional<MstParametersModuleWise> findByParamNameAndStatusAssessment(String paramName, boolean statusAssessment);

	Optional<MstParametersModuleWise> findByParamNameAndStatusEnforcement(String paramName, boolean statusAssessment);

	Optional<MstParametersModuleWise> findByParamName(String dateCellValue);

	List<MstParametersModuleWise> findByStatusScrutinyTrue();

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_parameters_module_wise mpmw \r\n"
			+ "on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "where erc.category = ?1 group by mpmw.id", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategory(String category);

	@Query(value = "select distinct mpmw.* from analytics.audit_master am  \r\n"
			+ "join analytics.mst_parameters_module_wise mpmw \r\n"
			+ "on split_part(am.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "where am.category = ?1 and am.working_location in (?2) group by mpmw.id", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAuditCategory(Long categoryId, List<String> locationList);

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id = ?3 order by mpmw.param_name", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserId(
			String category, String action, Integer foUserId);

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id = ?3 and (ercau.foa_user_id is null or ercau.foa_user_id = 0) order by mpmw.param_name", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserIdAndNotAssignedToAnyFOA(
			String category, String action, Integer foUserId);

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id = ?3 and ercau.foa_user_id > 0 order by mpmw.param_name", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserIdAndAssignedToAnyFOA(
			String category, String action, Integer foUserId);

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and ercau.foa_user_id = ?3 order by mpmw.param_name", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFoaUserId(
			String category, String action, Integer foaUserId);

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and (ercau.fo_user_id = ?3 or ercau.fo_user_id = 0) order by mpmw.param_name", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndActionStatusAndFOUserIdAndDefaultFOUserId(
			String category, String action, Integer foUserId);

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw \r\n"
			+ "on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text) join analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and ercau.fo_user_id = ?2 group by mpmw.id", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndFOUserId(String category,
			Integer foUserId);

	@Query(value = "select distinct pt.* from analytics.enforcement_review_case erc \r\n"
			+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) ", nativeQuery = true)
	List<MstParametersModuleWise> findAllCasesParameter();

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw \r\n"
			+ "on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text) join analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and ercau.ap_user_id = ?2 group by mpmw.id", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndAPUserId(String category,
			Integer apUserId);

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw \r\n"
			+ "on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text) join analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and ercau.ru_user_id = ?2 group by mpmw.id", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndRuUserId(String category,
			Integer ruUserId);

	List<MstParametersModuleWise> findAllByStatusSelfDetectedCases(boolean b);

	List<MstParametersModuleWise> findAllByScrutinySelfDetectedCases(boolean b);

	Optional<MstParametersModuleWise> findByParamNameAndStatusSelfDetectedCases(String string, boolean b);

	@Query(value = "select distinct mpmw.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw \r\n"
			+ "on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text) join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name \r\n"
			+ "where erc.category = ?1 and working_location in (?2) and action not in (?3) group by mpmw.id", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAssessmentCategoryAndLocationListAndExceptActionList(
			String categoryName, List<String> locationList, List<String> exceptActionList);

	@Query(value = "select distinct mpmw.* from analytics.audit_master am  \r\n"
			+ "inner join analytics.audit_master_cases_allocating_users amcau on amcau.case_id = am.case_id \r\n"
			+ "join analytics.mst_parameters_module_wise mpmw \r\n"
			+ "on split_part(am.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "where am.category = ?1 and amcau.l3_user = ?2 and am.working_location in (?3) group by mpmw.id", nativeQuery = true)
	List<MstParametersModuleWise> findAllAssessmentParameterByAuditCategoryAndL3UserId(Long categoryId,
			Integer l3UserId, List<String> locationList);
}
