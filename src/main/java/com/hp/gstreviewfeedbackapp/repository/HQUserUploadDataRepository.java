package com.hp.gstreviewfeedbackapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;

@Repository
public interface HQUserUploadDataRepository extends JpaRepository<EnforcementReviewCase, CompositeKey> {
	List<EnforcementReviewCase> findByAssignedTo(String assignedTo);

	List<EnforcementReviewCase> findByCategory(String category);

	@Override
	Optional<EnforcementReviewCase> findById(CompositeKey compositeKey);

	List<EnforcementReviewCase> findByAction(String action);

	List<EnforcementReviewCase> findByActionIn(List<String> actionList);

	@Query(value = "select * from analytics.enforcement_review_case e where e.working_location IN (?1) and action IN (?2);", nativeQuery = true)
	List<EnforcementReviewCase> findByLocationDetail(List<String> locationList, List<String> actionList);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc, analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "and erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id in (?3);", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndUserIdList(String categoryName, String action,
			List<Integer> userIdList);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc, analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "and erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id in (?3) and (ercau.foa_user_id is null or ercau.foa_user_id = 0)", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndUserIdListAndNotAssignedToAnyFOA(String categoryName,
			String action, List<Integer> userIdList);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc, analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "and erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id in (?3) and ercau.foa_user_id > 0", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndUserIdListAndAssignedToAnyFOA(String categoryName,
			String action, List<Integer> userIdList);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc, analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "and erc.category = ?1 and erc.action = ?2 and ercau.foa_user_id in (?3);", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndFoaUserIdList(String categoryName, String action,
			List<Integer> userIdList);

	/*****************************
	 * scrutiny case start
	 *********************************/
	@Query(value = "select erc.* from analytics.enforcement_review_case erc, analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "and erc.category = ?1 and erc.action = ?2 and (ercau.fo_user_id = ?3 or ercau.fo_user_id = 0);", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndUserIdListAndDefaultUserId(String categoryName, String action,
			List<Integer> userIdList);

	/*****************************
	 * scrutiny case end
	 *********************************/
	// get scritomu cases list
	@Query(value = "select * from analytics.enforcement_review_case erc where erc.category ='Scrutiny' and erc.assigned_from='SFO' and erc.assigned_to = 'FO'  \r\n"
			+ "and erc.\"action\" ='recommendedForAssesAndAdjudication' and erc.working_location in(?1);", nativeQuery = true)
	List<EnforcementReviewCase> getScrutinyCasesList(List<String> workingLocationList);

	/*
	 * @Query(value =
	 * "select * from analytics.enforcement_review_case erc where erc.action = 'closed' and erc.recovery_status != 'foRecoveryClose' \r\n"
	 * +
	 * "and erc.recovery_stage in (1,2) and erc.case_stage in (5,8,9) and working_location in (?1) and erc.category = ?2 "
	 * , nativeQuery = true)
	 */
	@Query(value = "select * from analytics.enforcement_review_case erc where erc.action = 'closed' \r\n"
			+ " and erc.recovery_stage in (1,2) and erc.case_stage in (5,8,9) and working_location in (?1)"
			+ " and erc.category = ?2 and (erc.recovery_status = 'foRecoveryUpdated' OR erc.recovery_status IS NULL) ", nativeQuery = true)
	List<EnforcementReviewCase> findByCategorywiseRecoveryList(List<String> location, String category);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.action = 'closed' and erc.recovery_status = 'foRecoveryClose' ", nativeQuery = true)
	List<EnforcementReviewCase> findFullRecoveryList();

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.action = 'closed' and erc.recovery_status = 'foRecoveryRaiseQuery'"
			+ " and erc.working_location in (?1) and erc.category = ?2 ", nativeQuery = true)
	List<EnforcementReviewCase> findRevertedByApproverRecoveryList(List<String> location, String category);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id in (?3) and split_part(erc.parameter, ',', 1) = ?4 ", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndUserIdListAnd1stParameter(String category, String action,
			List<Integer> userIdList, String parameterId);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id in (?3) and (ercau.foa_user_id is null or ercau.foa_user_id = 0) and split_part(erc.parameter, ',', 1) = ?4 ", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndUserIdListAnd1stParameterAndNotAssignedToAnyFOA(
			String category, String action, List<Integer> userIdList, String parameterId);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and ercau.fo_user_id in (?3) and ercau.foa_user_id > 0 and split_part(erc.parameter, ',', 1) = ?4 ", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndUserIdListAnd1stParameterAndAssignedToAnyFOA(String category,
			String action, List<Integer> userIdList, String parameterId);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and ercau.foa_user_id in (?3) and split_part(erc.parameter, ',', 1) = ?4 ", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndFoaUserIdListAnd1stParameter(String category, String action,
			List<Integer> FoaUserIdList, String parameterId);

	/*****************************
	 * scrutiny case start
	 *********************************/
	@Query(value = "select erc.* from analytics.enforcement_review_case erc join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter, ',', 1) = cast(mpmw.id as text)\r\n"
			+ "join analytics.enforcement_review_case_assigned_users ercau on erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and erc.action = ?2 and (ercau.fo_user_id = ?3 or ercau.fo_user_id = 0) and split_part(erc.parameter, ',', 1) = ?4 ", nativeQuery = true)
	List<EnforcementReviewCase> findByCategoryAndActionAndUserIdListAndDefaultUserIdAnd1stParameter(String category,
			String action, List<Integer> userIdList, String parameterId);
	/*****************************
	 * scrutiny case end
	 *********************************/
}
