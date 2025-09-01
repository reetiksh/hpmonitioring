/**
 * 
 */
package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.Category;

/**
 * 
 */
@Repository
public interface CategoryListRepository extends JpaRepository<Category, Long> {
	@Override
	List<Category> findAll();

	@Override
	Optional<Category> findById(Long id);

	Category findByName(String name);
	
	Category findByNameAndModule(String name, String module);

	@Query(value = "select name from analytics.mst_dd_hq_category mdhc where name not in ('Self Detected Cases', 'Detailed Enforcement Old Cases') and module='assessment'", nativeQuery = true)
	List<String> findAllCategoryNamesForHQUploadCase();

	@Query(value = "select * from analytics.mst_dd_hq_category mdhc where id not in (7,8,9) order by id asc", nativeQuery = true)
	List<Category> findAllCategoryforReview();

	List<Category> findAllByModule(String string);

	List<Category> findAllByActiveStatus(boolean active);

	List<Category> findAllByActiveStatusAndModule(boolean active, String module);

	@Query(value = "select mdhc.id from analytics.mst_dd_hq_category mdhc where mdhc.module = 'assessment' ", nativeQuery = true)
	List<Long> findOnlyId();

	@Query(value = "select mdhc.id from analytics.mst_dd_hq_category mdhc where mdhc.active_status = true ", nativeQuery = true)
	List<Long> findOnlyActiveId();

	Optional<Category> findByActiveStatusAndModuleAndName(boolean activeStatus, String module, String name);

	Boolean existsByActiveStatusAndModuleAndName(boolean activeStatus, String module, String name);

	Category findByNameAndModuleAndActiveStatus(String name, String module, boolean activeStatus);

	@Query(value = "select mdhc.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.\"name\" group by mdhc.id order by mdhc.\"name\"", nativeQuery = true)
	List<Category> findAllCategoryForAssessmentCases();

	@Query(value = "select mdhc.* from analytics.audit_master am join analytics.mst_dd_hq_category mdhc on am.category = mdhc.id where am.working_location in (?1) "
			+ "group by mdhc.id order by mdhc.name", nativeQuery = true)
	List<Category> findAllCategoryForAuditCases(List<String> locationList);

	@Query(value = "select distinct mdhc.* from analytics.enforcement_review_case erc join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.action in(?1) and (ercau.fo_user_id = ?2 or ercau.fo_user_id = 0 ) order by mdhc.name", nativeQuery = true)
	List<Category> findAllActiveCasesCategoriesByActionStatusAndFOUserId(List<String> action, Integer foUserId);

	@Query(value = "select distinct mdhc.* from analytics.enforcement_review_case erc join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.action in(?1) and ((ercau.fo_user_id = ?2 or ercau.fo_user_id = 0) and (ercau.foa_user_id is null or ercau.foa_user_id = 0)) order by mdhc.name", nativeQuery = true)
	List<Category> findAllActiveCasesCategoriesByActionStatusAndFOUserIdAndNotAssisnedToSomeFOA(List<String> action,
			Integer foUserId);

	@Query(value = "select distinct mdhc.* from analytics.enforcement_review_case erc join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.action in(?1) and (ercau.fo_user_id = ?2 or ercau.fo_user_id = 0) and ercau.foa_user_id > 0 order by mdhc.name", nativeQuery = true)
	List<Category> findAllActiveCasesCategoriesByActionStatusAndFOUserIdAndAssisnedToSomeFOA(List<String> action,
			Integer foUserId);

	@Query(value = "select distinct mdhc.* from analytics.enforcement_review_case erc join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period \r\n"
			+ "where erc.action in(?1) and (ercau.foa_user_id = ?2) order by mdhc.name", nativeQuery = true)
	List<Category> findAllActiveCasesCategoriesByActionStatusAndFoaUserId(List<String> action, Integer foaUserId);

	@Query(value = "select mdhc.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where ercau.fo_user_id = ?1 group by mdhc.id order by mdhc.name", nativeQuery = true)
	List<Category> findAllCategoryForAssessmentCasesByFOUserId(Integer foUserId);

	@Query(value = "select mdhc.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where ercau.ap_user_id = ?1 group by mdhc.id order by mdhc.name", nativeQuery = true)
	List<Category> findAllCategoryForAssessmentCasesByAPUserId(Integer apUserId);

	@Query(value = "select mdhc.* from analytics.enforcement_review_case erc \r\n"
			+ "join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where ercau.ru_user_id = ?1 group by mdhc.id order by mdhc.name", nativeQuery = true)
	List<Category> findAllCategoryForAssessmentCasesByRuUserId(Integer ruUserId);

	@Query(value = "select distinct mdhc.* from analytics.enforcement_master em \r\n"
			+ "inner join analytics.enforcement_master_allocating_user emau on emau.gstin = em.gstin and emau.case_reporting_date = em.case_reporting_date and emau.period = em.period\r\n"
			+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.id = em.category_id \r\n"
			+ "inner join analytics.enforcement_action_status eas on eas.id = em.action_id \r\n"
			+ "where em.case_location_location_id in (?1) and emau.enf_fo_user_id in (?2) \r\n"
			+ "and eas.code_name in (?3) and em.assigned_to = ?4", nativeQuery = true)
	List<Category> findAllCategoryOfEnfFOCasesByLocationListAndUserIdAndActionListAndAssignedToUserRole(
			List<String> allMappedLocations, List<Integer> userIds, List<String> actionList, String assignedToUserRole);

	@Query(value = "select distinct mdhc.* from analytics.enforcement_master em \r\n"
			+ "inner join analytics.enforcement_master_allocating_user emau on emau.gstin = em.gstin and emau.case_reporting_date = em.case_reporting_date and emau.period = em.period\r\n"
			+ "inner join analytics.mst_dd_hq_category mdhc on mdhc.id = em.category_id \r\n"
			+ "inner join analytics.enforcement_action_status eas on eas.id = em.action_id \r\n"
			+ "where em.case_location_location_id in (?1) and emau.enf_svo_user_id in (?2) \r\n"
			+ "and eas.code_name in (?3) and em.assigned_to = ?4", nativeQuery = true)
	List<Category> findAllCategoryOfEnfSvoCasesByLocationListAndUserIdAndActionListAndAssignedToUserRole(
			List<String> allMappedLocations, List<Integer> userIds, List<String> actionList, String assignedToUserRole);

	@Query(value = "select distinct (mdhc.*) from analytics.enforcement_review_case erc left outer join analytics.mst_dd_hq_category mdhc on erc.category = mdhc.name "
			+ "where working_location in (?1) and action not in (?2)", nativeQuery = true)
	List<Category> findAllCategoryForAssessmentCasesByLocationListAndExceptActionList(List<String> locationList,
			List<String> exceptActionList);

	@Query(value = "select mdhc.* from analytics.audit_master am join analytics.mst_dd_hq_category mdhc on am.category = mdhc.id \r\n"
			+ "inner join analytics.audit_master_cases_allocating_users amcau on amcau.case_id = am.case_id and am.working_location in (?2) \r\n"
			+ "where amcau.l3_user = ?1 group by mdhc.id order by mdhc.name", nativeQuery = true)
	List<Category> findAllCategoryForAuditCasesByL3UserId(Integer l3UserId, List<String> locationList);
}
