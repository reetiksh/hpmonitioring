package com.hp.gstreviewfeedbackapp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;

@Repository
public interface EnforcementReviewCaseRepository extends JpaRepository<EnforcementReviewCase, CompositeKey> {
	/* Query Repo for Verifier Start */
	List<EnforcementReviewCase> findByCategory(String category);

	List<EnforcementReviewCase> findAllByAssignedToAndActionAndAssignedToUserId(String string, String string2, int i);
//	@Query(value="select * from analytics.enforcement_review_case erc where erc.assigned_to = 'AP' and erc.action= 'verifyerRecommended' ",nativeQuery=true)
//	List<EnforcementReviewCase> recommendeForClosureByVerifier(String assignedTo,String action);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.working_location in(?1) and erc.assigned_to='AP'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='RU' \r\n"
			+ "and erc.action = 'verifyerRecommended' \r\n"
			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ru_user_id=?2 or ercau.ru_user_id='0')", nativeQuery = true)
	List<EnforcementReviewCase> recommendeForClosureByVerifier(List<String> workingLocation, Integer userId);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.working_location in(?1) and erc.assigned_to='FO'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='RU' \r\n"
			+ "and erc.\"action\"='verifyerRaiseQuery' \r\n"
			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ru_user_id=?2 or ercau.ru_user_id='0')", nativeQuery = true)
	List<EnforcementReviewCase> raisedQueryByVerifier(List<String> workingLocation, Integer userId);
//	@Query(value="select * from analytics.enforcement_review_case erc where erc.assigned_to = 'RU' and erc.action= 'close' ",nativeQuery=true)
//	List<EnforcementReviewCase> findByAssignedToAndAction(String assignedTo,String action);

//	@Query(value="select * from(\r\n"
//			+ "select * from analytics.enforcement_review_case erc \r\n"
//			+ "where erc.assigned_to='RU' and erc.\"action\"='close'\r\n"
//			+ "and erc.working_location in(select lm.circle_id from analytics.mst_location_mapping lm where \r\n"
//			+ "lm.district_id in(select urm.district_id  from analytics.mst_user_role_mapping urm where urm.user_id=?1 and urm.user_role_id=3))\r\n"
//			+ "or erc.working_location in(select urm.district_id  from analytics.mst_user_role_mapping urm where urm.user_id=?1 and urm.user_role_id=3)\r\n"
//			+ ") as filtered_cases where filtered_cases.assigned_to='RU'"
//			,nativeQuery=true)
	@Query(value = "select *  from analytics.mst_user_role_mapping urm where urm.user_id ='352' and urm.user_role_id='3'", nativeQuery = true)
	List<EnforcementReviewCase> findByAssignedToAndActionWithMapping(Integer userId);
//	@Query(value="select * from analytics.enforcement_review_case erc where erc.working_location in(?1) and erc.assigned_to='RU'",nativeQuery=true)
//	List<EnforcementReviewCase> findAllByWorkingLocation(List<String> workingLocation);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.working_location in(?1) and erc.assigned_to='RU'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='FO' \r\n"
			+ "and erc.\"action\"='close' \r\n"
			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ru_user_id=?2 or ercau.ru_user_id ='0')", nativeQuery = true)
	List<EnforcementReviewCase> findAllByWorkingLocation(List<String> workingLocation, Integer userId);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.working_location in(?1) and erc.assigned_to='RU'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='FO' \r\n"
			+ "and erc.\"action\"='close' \r\n"
			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ru_user_id=?2 or ercau.ru_user_id ='0') and erc.category = ?3", nativeQuery = true)
	List<EnforcementReviewCase> findAllByWorkingLocationAndCategory(List<String> workingLocation, Integer userId,
			String categoryName);

	@Query(value = "select distinct erc.category from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.working_location in(?1) and erc.assigned_to='RU'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='FO' \r\n"
			+ "and erc.action='close' and ercau.period = erc.period and (ercau.ru_user_id=?2 or ercau.ru_user_id ='0')", nativeQuery = true)
	List<String> findAllDistinctCategoryForVerifierByWorkingLocationsAndUserId(List<String> workingLocation,
			Integer userId);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.working_location in(?1) and erc.assigned_to='RU'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='AP' \r\n"
			+ "and erc.\"action\"='apRejectAppealRevision' \r\n"
			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ru_user_id=?2 or ercau.ru_user_id ='0')", nativeQuery = true)
	List<EnforcementReviewCase> findAllAppealRevisionRejectedCasesList(List<String> workingLocation, Integer userId);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.gstin=?1 ", nativeQuery = true)
	List<EnforcementReviewCase> findByGstin(String gstin);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.gstin = ?1 and erc.\"period\"=?2 and erc.case_reporting_date = ?3 ", nativeQuery = true)
	List<EnforcementReviewCase> findByGstinPeriodRepotingDate(String gstin, String period, Date caseReportingDate);

	@Query(value = "select lm.circle_id  from analytics.mst_location_mapping lm where lm.zone_id in(?1)", nativeQuery = true)
	List<String> findAllCirclesByZoneIds(List<String> zoneIds);

	@Query(value = "select lm.circle_id  from analytics.mst_location_mapping lm where lm.enforcement_zone_id in(?1)", nativeQuery = true)
	List<String> findAllCirclesByEnfZoneIds(List<String> enfZoneIds);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.\"action\"='close' and erc.assigned_from='FO' and erc.assigned_to='RU'", nativeQuery = true)
	List<EnforcementReviewCase> verifierStateLevelCasesList();

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.action='apRejectAppealRevision' and erc.assigned_from='AP' and erc.assigned_to='RU'", nativeQuery = true)
	List<EnforcementReviewCase> verifierStateLevelAppealRejectionCasesList();

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.\"action\"='verifyerRaiseQuery' and erc.assigned_from='RU' and erc.assigned_to='FO'  ", nativeQuery = true)
	List<EnforcementReviewCase> verifierRaiseQueryCasesList();
	/* Query Repo for Verifier End */
	/* Query Repo for Approver Start */
//	@Query(value="select * from analytics.enforcement_review_case erc where erc.assigned_to = 'AP' and erc.action= 'verifyerRecommended' ",nativeQuery=true)
//	List<EnforcementReviewCase> findByAssignedToAndActionForApprover();
//	@Query(value="select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
//			+ "where erc.working_location in(?1) and erc.assigned_to='AP'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='RU' \r\n"
//			+ "and erc.\"action\"='verifyerRecommended' \r\n"
//			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ru_user_id=?2 or ercau.ru_user_id='0')",nativeQuery=true)
//	List<EnforcementReviewCase> findByAssignedToAndActionForApprover(List<String> workingLocation,Integer userId);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where erc.working_location in(?1) and erc.assigned_to='AP'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='RU' \r\n"
			+ "and erc.\"action\"='verifyerRecommended' \r\n"
			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ap_user_id=?2 or ercau.ap_user_id='0')", nativeQuery = true)
	List<EnforcementReviewCase> findApproverCasesList(List<String> workingLoacations, Integer userId);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
			+ "where  erc.assigned_to='AP'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='RU' \r\n"
			+ "and (erc.action='ruAppeal' or erc.\"action\"='ruRevision') \r\n"
			+ "and ercau.period = erc.period and (ercau.ap_user_id=?1 or ercau.ap_user_id='0')", nativeQuery = true)
	List<EnforcementReviewCase> findAppealRevisionCasesList(Integer userId);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.assigned_to = 'HQ' and erc.action= 'closed' ", nativeQuery = true)
	List<EnforcementReviewCase> approvedCasesListByApprover();

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.assigned_from='AP' and erc.assigned_to='HQ' and erc.\"action\"='apApproveAppeal'", nativeQuery = true)
	List<EnforcementReviewCase> appealedCasesListByApprover();

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.assigned_from='AP' and erc.assigned_to='HQ' and erc.\"action\"='apApproveRevision'", nativeQuery = true)
	List<EnforcementReviewCase> revisionedCasesListByApprover();
//	@Query(value="select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
//			+ "where erc.working_location in(?1) and erc.assigned_to='HQ'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='AP' \r\n"
//			+ "and erc.\"action\"='closed' \r\n"
//			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ru_user_id=?2 or ercau.ru_user_id='0')",nativeQuery=true)
//	List<EnforcementReviewCase> approvedCasesListByApprover(List<String> workingLoacations,Integer userId);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.assigned_to = 'FO' and erc.action= 'approverRaiseQuery' ", nativeQuery = true)
	List<EnforcementReviewCase> rejectedCasesListByApprover();
//	@Query(value="select erc.* from analytics.enforcement_review_case erc ,analytics.enforcement_review_case_assigned_users ercau \r\n"
//			+ "where erc.working_location in(?1) and erc.assigned_to='FO'  and ercau.gstin=erc.gstin and ercau.case_reporting_date=erc.case_reporting_date and erc.assigned_from='AP' \r\n"
//			+ "and erc.\"action\"='approverRaiseQuery' \r\n"
//			+ "and ercau.\"period\" = erc.\"period\" and (ercau.ru_user_id=?2 or erc.assigned_to_user_id='0')",nativeQuery=true)
//	List<EnforcementReviewCase> rejectedCasesListByApprover(List<String> workingLoacations,Integer userId);

	@Query(value = "select frc.fo_filepath from analytics.fo_review_case frc where frc.gstin=?1 and\r\n"
			+ "frc.\"period\" = ?2 and frc.case_reporting_date =?3\r\n" + "and\r\n"
			+ "frc.action in('status_updated','status_updated_a','status_updated_v')"
			+ "order by frc.id desc limit 1\r\n" + "", nativeQuery = true)
	String findFileNameByGstin(String gstin, String period, Date caseReportingDate);

	@Query(value = "select frc.fo_filepath from analytics.fo_review_case frc where frc.gstin=?1 and\r\n"
			+ "frc.\"period\" = ?2 and frc.case_reporting_date =?3 and\r\n"
			+ "frc.action in('foRecoveryClose') order by frc.id desc limit 1 ", nativeQuery = true)
	String findFoRecoveryCloseFileNameByGstinAndPeriodAndCaseZReportingDate(String gstin, String period,
			Date caseReportingDate);

	@Query(value = "select erc.category, sum(erc.indicative_tax_value) from analytics.enforcement_review_case erc where working_location in (?1) group by erc.category;", nativeQuery = true)
	List<Object[]> getTotalIndicativeTaxValueCategoryWise(List<String> locationIds);

	@Query(value = "select erc.category, SUM(CAST(erc.demand AS BIGINT)) from analytics.enforcement_review_case erc where working_location in (?1) group by erc.category;", nativeQuery = true)
	List<Object[]> getTotalDemandCategoryWise(List<String> locationIds);
	/*
	 * @Query(
	 * value="select erc.category, (SUM(CAST(erc.recovery_against_demand AS BIGINT)) + SUM(CAST(erc.recovery_by_drc3 AS BIGINT))) as total_recovery from analytics.enforcement_review_case erc where working_location in (?1) group by erc.category;"
	 * ,nativeQuery=true) List<Object[]> getTotalRecoveryCategoryWise(List<String>
	 * locationIds);
	 */

	@Query(value = "select erc.category, sum(COALESCE(recovery_against_demand, 0) + COALESCE(recovery_by_drc3, 0)) as total_recovery from analytics.enforcement_review_case erc"
			+ " where working_location in (?1) group by erc.category;", nativeQuery = true)
	List<Object[]> getTotalRecoveryCategoryWise(List<String> locationIds);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.action = 'verifyerRecommended' and erc.assigned_from='RU' and erc.assigned_to='AP'  ", nativeQuery = true)
	List<EnforcementReviewCase> verifierRecommendedCasesList();

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.\"action\"='verifyerRecommended' and erc.assigned_from='RU' and erc.assigned_to='AP'  ", nativeQuery = true)
	Page<EnforcementReviewCase> verifierRecommendedCasesList(Pageable page);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.action= ?1 and erc.assigned_from='RU' and erc.assigned_to='AP' and erc.indicative_tax_value > ?2", nativeQuery = true)
	Page<EnforcementReviewCase> verifierRecommendedCasesListForStateLevelByAction(Pageable page, String action,
			Long maximunIndicativeTaxValueForZoneLevelAp);

	@Query(value = "select * from analytics.enforcement_review_case erc where erc.action= ?1 and erc.assigned_from='RU' "
			+ "and erc.assigned_to='AP' and erc.indicative_tax_value <= ?2 and erc.working_location in (?3)", nativeQuery = true)
	Page<EnforcementReviewCase> verifierRecommendedCasesListForZoneLevelByAction(Pageable page, String action,
			Long maximunIndicativeTaxValueForZoneLevelAp, List<String> allMappedLocations);

	@Query(value = "SELECT erc.* FROM analytics.enforcement_review_case erc\r\n"
			+ "LEFT JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "LEFT JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id,\r\n"
			+ "analytics.mst_user_role mur, analytics.mst_dd_location_details mdld\r\n"
			+ "WHERE erc.action ='verifyerRecommended' and erc.assigned_from='RU' and erc.assigned_to='AP' and\r\n"
			+ "erc.assigned_to = mur.code AND erc.working_location = mdld.location_id\r\n"
			+ "AND (LOWER(erc.gstin) LIKE %:searchValue% OR CAST(erc.case_reporting_date AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.taxpayer_name) LIKE %:searchValue% OR LOWER(erc.working_location) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mdld.location_name) LIKE %:searchValue% OR erc.period LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.extension_no) LIKE %:searchValue% OR CAST(erc.indicative_tax_value AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mur.name) LIKE %:searchValue% OR LOWER(erc.case_id) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.caste_stage_arn) LIKE %:searchValue% OR CAST(erc.demand AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.recovery_stage_arn) LIKE %:searchValue% OR CAST(erc.recovery_stage AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR CAST(erc.recovery_by_drc3 AS VARCHAR) LIKE %:searchValue% OR CAST(erc.recovery_against_demand AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mdfas.name) like %:searchValue% OR LOWER(mdfcs.name) LIKE %:searchValue%)", nativeQuery = true)
	Page<EnforcementReviewCase> verifierRecommendedCasesList(@Param("searchValue") String searchValue, Pageable page);

	@Query(value = "SELECT erc.* FROM analytics.enforcement_review_case erc\r\n"
			+ "LEFT JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "LEFT JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id,\r\n"
			+ "analytics.mst_user_role mur, analytics.mst_dd_location_details mdld\r\n"
			+ "WHERE erc.action = :actionStatus and erc.assigned_from='RU' and erc.assigned_to='AP' and erc.indicative_tax_value > :maximunIndicativeTaxValueForZoneLevelAp and \r\n"
			+ "erc.assigned_to = mur.code AND erc.working_location = mdld.location_id\r\n"
			+ "AND (LOWER(erc.gstin) LIKE %:searchValue% OR CAST(erc.case_reporting_date AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.taxpayer_name) LIKE %:searchValue% OR LOWER(erc.working_location) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mdld.location_name) LIKE %:searchValue% OR erc.period LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.extension_no) LIKE %:searchValue% OR CAST(erc.indicative_tax_value AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mur.name) LIKE %:searchValue% OR LOWER(erc.case_id) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.caste_stage_arn) LIKE %:searchValue% OR CAST(erc.demand AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.recovery_stage_arn) LIKE %:searchValue% OR CAST(erc.recovery_stage AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR CAST(erc.recovery_by_drc3 AS VARCHAR) LIKE %:searchValue% OR CAST(erc.recovery_against_demand AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mdfas.name) like %:searchValue% OR LOWER(mdfcs.name) LIKE %:searchValue%)", nativeQuery = true)
	Page<EnforcementReviewCase> verifierRecommendedCasesListForStateLevelByAction(
			@Param("searchValue") String searchValue, Pageable page, @Param("actionStatus") String actionStatus,
			@Param("maximunIndicativeTaxValueForZoneLevelAp") Long maximunIndicativeTaxValueForZoneLevelAp);

	@Query(value = "SELECT erc.* FROM analytics.enforcement_review_case erc\r\n"
			+ "LEFT JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "LEFT JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id,\r\n"
			+ "analytics.mst_user_role mur, analytics.mst_dd_location_details mdld\r\n"
			+ "WHERE erc.action = :actionStatus and erc.assigned_from='RU' and erc.assigned_to='AP' "
			+ "and erc.indicative_tax_value <= :maximunIndicativeTaxValueForZoneLevelAp and erc.working_location in (:allMappedLocations) and \r\n"
			+ "erc.assigned_to = mur.code AND erc.working_location = mdld.location_id\r\n"
			+ "AND (LOWER(erc.gstin) LIKE %:searchValue% OR CAST(erc.case_reporting_date AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.taxpayer_name) LIKE %:searchValue% OR LOWER(erc.working_location) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mdld.location_name) LIKE %:searchValue% OR erc.period LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.extension_no) LIKE %:searchValue% OR CAST(erc.indicative_tax_value AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mur.name) LIKE %:searchValue% OR LOWER(erc.case_id) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.caste_stage_arn) LIKE %:searchValue% OR CAST(erc.demand AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(erc.recovery_stage_arn) LIKE %:searchValue% OR CAST(erc.recovery_stage AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR CAST(erc.recovery_by_drc3 AS VARCHAR) LIKE %:searchValue% OR CAST(erc.recovery_against_demand AS VARCHAR) LIKE %:searchValue%\r\n"
			+ "OR LOWER(mdfas.name) like %:searchValue% OR LOWER(mdfcs.name) LIKE %:searchValue%)", nativeQuery = true)
	Page<EnforcementReviewCase> verifierRecommendedCasesListForZoneLevelByAction(
			@Param("searchValue") String searchValue, Pageable page, @Param("actionStatus") String actionStatus,
			@Param("maximunIndicativeTaxValueForZoneLevelAp") Long maximunIndicativeTaxValueForZoneLevelAp,
			List<String> allMappedLocations);
	/* Query Repo for Approver End */

	/******* find cases pending on verifier more than 15 days start *********/
	@Query(value = "select * from analytics.enforcement_review_case erc where erc.\"action\"='close' and erc.assigned_from='FO' and erc.assigned_to='RU' and erc.case_update_date < (current_date - interval '14 days');", nativeQuery = true)
	List<EnforcementReviewCase> findAllCasesPendingToVerifierMoreThanFifteenDays();

	/******* find cases pending on verifier more than 15 days end *********/
	/*********** Fo Dashboard consolidate case stage list table start ************/
//	@Query(value="select erc.case_stage as case_stage_category ,mdfcs.\"name\" as case_stage_name, count(gstin) as total from analytics.enforcement_review_case erc \r\n"
//			+ "inner join analytics.mst_dd_fo_case_stage mdfcs on mdfcs.id = erc.case_stage \r\n"
//			+ "where erc.case_reporting_date between ?1 and ?2 \r\n"
//			+ "group by erc.case_stage,mdfcs.\"name\" order by case_stage_category",nativeQuery=true)
//	List<Object> getConsolidateCaseStageList(Date previousDate,Date latestDate);
	@Query(value = "SELECT erc FROM EnforcementReviewCase erc where erc.id.caseReportingDate >= ?1 and erc.id.caseReportingDate <= ?2\r\n")
	List<EnforcementReviewCase> getConsolidateCaseStageList(Date previousDate, Date latestDate);
	/*
	 * @Query(value =
	 * "select erc from analytics.enforcement_review_case erc where erc.case_reporting_date >= ?1 and erc.case_reporting_date <= ?2 and erc.working_location in(?3)"
	 * ,nativeQuery = true) List<EnforcementReviewCase>
	 * getConsolidateCaseStageList(Date previousDate, Date latestDate, List<String>
	 * workingLocationsIds);
	 */

	@Query(value = "select mrvrq.\"name\" , cw.other_remarks from analytics.enforcement_review_case erc, analytics.case_workflow cw, analytics.mst_remarks_verifier_raise_query mrvrq \r\n"
			+ "where erc.gstin = cw.gstin and erc.case_reporting_date = erc.case_reporting_date and erc.\"period\" = cw.\"period\" and cw.verifier_raise_query_remarks is not null and mrvrq.id = cw.verifier_raise_query_remarks  \r\n"
			+ "and erc.gstin = ?1 and erc.\"period\" = ?2 and erc.case_reporting_date = ?3 ", nativeQuery = true)
	List<String[]> getVerifierRemarks(String gstin, String period, Date caseReportingDate);

	@Query(value = "select mdar.\"name\" , cw.other_remarks from analytics.enforcement_review_case erc, analytics.case_workflow cw, analytics.mst_dd_ap_rejected mdar \r\n"
			+ "where erc.gstin = cw.gstin and erc.case_reporting_date = erc.case_reporting_date and erc.\"period\" = cw.\"period\" and cw.approver_reject_case_remarks  is not null and mdar.id = cw.approver_reject_case_remarks  \r\n"
			+ "and erc.gstin = ?1 and erc.\"period\" = ?2 and erc.case_reporting_date = ?3 ", nativeQuery = true)
	List<String[]> getApproverRemarks(String gstin, String period, Date caseReportingDate);

	@Query(value = "select location_id from analytics.mst_dd_location_details mdld where location_name != 'NA'", nativeQuery = true)
	List<String> finAllLocationsByStateid();

	@Query(value = "select distinct period from analytics.enforcement_review_case erc  order by period", nativeQuery = true)
	List<String> findAllDistinctPeriod();

	@Query(value = "select * from analytics.enforcement_review_case erc  where gstin = ?1 and period = ?2", nativeQuery = true)
	List<EnforcementReviewCase> findAllByGstinPeriod(String gstin, String period);

	@Query(value = "select * from analytics.enforcement_review_case erc  where gstin = ?1 and case_reporting_date = ?2", nativeQuery = true)
	List<EnforcementReviewCase> findAllByGstinCaseReportingDate(String gstin, Date caseReportingDate);

	@Query(value = "select * from analytics.enforcement_review_case erc  where gstin = ?1", nativeQuery = true)
	List<EnforcementReviewCase> findAllByGstin(String gstin);

	@Query(value = "SELECT * FROM analytics.enforcement_review_case erc where erc.working_location IN (?1) and category = ?2 ", nativeQuery = true)
	List<EnforcementReviewCase> getListBasedLocationAndCategory(List<String> locationId, String category);

	@Query(value = "SELECT * FROM analytics.enforcement_review_case where category = ?1", nativeQuery = true)
	List<EnforcementReviewCase> enforcementReviewList(String category);

	@Query(value = "select distinct period from analytics.enforcement_review_case erc order by period", nativeQuery = true)
	List<String> findAllFinancialYear();

	@Query(value = "select count(gstin) from analytics.enforcement_review_case where working_location in(?1) and category != 'Old Cases' ", nativeQuery = true)
	String getDashBoardTotalCases(List<String> workingLocation);

	@Query(value = "select count (gstin) from analytics.enforcement_review_case where working_location in(?1) and action not in('upload', 'hqTransfer', 'transfer') and category != 'Old Cases' ", nativeQuery = true)
	String getDashBoardTotalAcknowledgedCases(List<String> workingLocation);

	@Query(value = "select count (gstin) from analytics.enforcement_review_case where working_location in(?1) and \r\n"
			+ "enforcement_review_case.action_status in(2,3) and category != 'Old Cases' ", nativeQuery = true)
	String getDashBoardTotalInitiatedCases(List<String> workingLocation);

	@Query(value = "select count(gstin) from analytics.enforcement_review_case "
			+ "where working_location in(?1) and action not in ('acknowledge', 'upload', 'hqTransfer', 'transfer', 'verifyerRaiseQuery', 'approverRaiseQuery') and category != 'Old Cases' ", nativeQuery = true)
	String getDashBoardTotalCasesClosedByFo(List<String> workingLocation);

	@Query(value = "select sum(indicative_tax_value) from analytics.enforcement_review_case where working_location in(?1) and category != 'Old Cases' ", nativeQuery = true)
	Long getDashBoardTotalSuspectedIndicativeAmount(List<String> workingLocation);

	@Query(value = "select sum(demand) from analytics.enforcement_review_case where working_location in(?1) and category != 'Old Cases' ", nativeQuery = true)
	Long getDashBoardTotalAmount(List<String> workingLocation);

	@Query(value = "select sum(demand) from analytics.enforcement_review_case where action_status = '3' and case_stage in ('5', '8', '9') and working_location in(?1) and category != 'Old Cases' ", nativeQuery = true)
	Long getDashBoardTotalDemand(List<String> workingLocation);

	@Query(value = "select sum(COALESCE(recovery_against_demand, 0) + COALESCE(recovery_by_drc3, 0)) from analytics.enforcement_review_case where working_location in(?1)  and category != 'Old Cases' ", nativeQuery = true)
	Long getDashBoardTotalRecovery(List<String> workingLocation);

	/*********** Fo Dashboard consolidate case stage list table start ************/
	@Query(value = "select * from analytics.enforcement_review_case erc where erc.caseid_updation_status = 'foUpdated' and erc.working_location in (?1)", nativeQuery = true)
	List<EnforcementReviewCase> getAllCaseIdList(List<String> locationList);

	Page<EnforcementReviewCase> findByCategory(String category, Pageable pageable);

	@Query(value = "SELECT erc.* FROM analytics.enforcement_review_case erc "
			+ "LEFT JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id "
			+ "LEFT JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id, "
			+ "analytics.mst_user_role mur, analytics.mst_dd_location_details mdld "
			+ "WHERE erc.assigned_to = mur.code AND erc.working_location = mdld.location_id "
			+ "AND erc.category = :category "
			+ "AND (LOWER(erc.gstin) LIKE %:searchValue% OR CAST(erc.case_reporting_date AS VARCHAR) LIKE %:searchValue% "
			+ "OR LOWER(erc.taxpayer_name) LIKE %:searchValue% OR LOWER(erc.working_location) LIKE %:searchValue% "
			+ "OR LOWER(mdld.location_name) LIKE %:searchValue% OR erc.period LIKE %:searchValue% "
			+ "OR LOWER(erc.extension_no) LIKE %:searchValue% OR CAST(erc.indicative_tax_value AS VARCHAR) LIKE %:searchValue% "
			+ "OR LOWER(mur.name) LIKE %:searchValue% OR LOWER(erc.case_id) LIKE %:searchValue% "
			+ "OR LOWER(erc.caste_stage_arn) LIKE %:searchValue% OR CAST(erc.demand AS VARCHAR) LIKE %:searchValue% "
			+ "OR LOWER(erc.recovery_stage_arn) LIKE %:searchValue% OR CAST(erc.recovery_stage AS VARCHAR) LIKE %:searchValue% "
			+ "OR CAST(erc.recovery_by_drc3 AS VARCHAR) LIKE %:searchValue% OR CAST(erc.recovery_against_demand AS VARCHAR) LIKE %:searchValue% "
			+ "OR LOWER(mdfas.name) like %:searchValue% OR LOWER(mdfcs.name) LIKE %:searchValue%)", countQuery = "SELECT COUNT(erc.*) FROM analytics.enforcement_review_case erc "
					+ "LEFT JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id "
					+ "LEFT JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id, "
					+ "analytics.mst_user_role mur, analytics.mst_dd_location_details mdld "
					+ "WHERE erc.assigned_to = mur.code AND erc.working_location = mdld.location_id "
					+ "AND erc.category = :category "
					+ "AND (LOWER(erc.gstin) LIKE %:searchValue% OR CAST(erc.case_reporting_date AS VARCHAR) LIKE %:searchValue% "
					+ "OR LOWER(erc.taxpayer_name) LIKE %:searchValue% OR LOWER(erc.working_location) LIKE %:searchValue% "
					+ "OR LOWER(mdld.location_name) LIKE %:searchValue% OR erc.period LIKE %:searchValue% "
					+ "OR LOWER(erc.extension_no) LIKE %:searchValue% OR CAST(erc.indicative_tax_value AS VARCHAR) LIKE %:searchValue% "
					+ "OR LOWER(mur.name) LIKE %:searchValue% OR LOWER(erc.case_id) LIKE %:searchValue% "
					+ "OR LOWER(erc.caste_stage_arn) LIKE %:searchValue% OR CAST(erc.demand AS VARCHAR) LIKE %:searchValue% "
					+ "OR LOWER(erc.recovery_stage_arn) LIKE %:searchValue% OR CAST(erc.recovery_stage AS VARCHAR) LIKE %:searchValue% "
					+ "OR CAST(erc.recovery_by_drc3 AS VARCHAR) LIKE %:searchValue% OR CAST(erc.recovery_against_demand AS VARCHAR) LIKE %:searchValue% "
					+ "OR LOWER(mdfas.name) like %:searchValue% OR LOWER(mdfcs.name) LIKE %:searchValue%)", nativeQuery = true)
	Page<EnforcementReviewCase> findByCategoryWithSearchValue(@Param("searchValue") String searchValue,
			@Param("category") String category, Pageable pageable);

	@Query(value = "SELECT erc.* FROM analytics.enforcement_review_case erc "
			+ "LEFT JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id "
			+ "LEFT JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id, "
			+ "analytics.mst_user_role mur, analytics.mst_dd_location_details mdld "
			+ "WHERE erc.assigned_to = mur.code AND erc.working_location = mdld.location_id "
			+ "AND (LOWER(erc.gstin) LIKE %:searchValue% OR CAST(erc.case_reporting_date AS VARCHAR) LIKE %:searchValue% "
			+ "OR LOWER(erc.taxpayer_name) LIKE %:searchValue% OR LOWER(erc.working_location) LIKE %:searchValue% "
			+ "OR LOWER(mdld.location_name) LIKE %:searchValue% OR erc.period LIKE %:searchValue% "
			+ "OR LOWER(erc.extension_no) LIKE %:searchValue% OR CAST(erc.indicative_tax_value AS VARCHAR) LIKE %:searchValue% "
			+ "OR LOWER(mur.name) LIKE %:searchValue% OR LOWER(erc.case_id) LIKE %:searchValue% "
			+ "OR LOWER(erc.caste_stage_arn) LIKE %:searchValue% OR CAST(erc.demand AS VARCHAR) LIKE %:searchValue% "
			+ "OR LOWER(erc.recovery_stage_arn) LIKE %:searchValue% OR CAST(erc.recovery_stage AS VARCHAR) LIKE %:searchValue% "
			+ "OR CAST(erc.recovery_by_drc3 AS VARCHAR) LIKE %:searchValue% OR CAST(erc.recovery_against_demand AS VARCHAR) LIKE %:searchValue% "
			+ "OR LOWER(mdfas.name) like %:searchValue% OR LOWER(mdfcs.name) LIKE %:searchValue%)", countQuery = "SELECT COUNT(erc.*) FROM analytics.enforcement_review_case erc "
					+ "LEFT JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id "
					+ "LEFT JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id, "
					+ "analytics.mst_user_role mur, analytics.mst_dd_location_details mdld "
					+ "WHERE erc.assigned_to = mur.code AND erc.working_location = mdld.location_id "
					+ "AND (LOWER(erc.gstin) LIKE %:searchValue% OR CAST(erc.case_reporting_date AS VARCHAR) LIKE %:searchValue% "
					+ "OR LOWER(erc.taxpayer_name) LIKE %:searchValue% OR LOWER(erc.working_location) LIKE %:searchValue% "
					+ "OR LOWER(mdld.location_name) LIKE %:searchValue% OR erc.period LIKE %:searchValue% "
					+ "OR LOWER(erc.extension_no) LIKE %:searchValue% OR CAST(erc.indicative_tax_value AS VARCHAR) LIKE %:searchValue% "
					+ "OR LOWER(mur.name) LIKE %:searchValue% OR LOWER(erc.case_id) LIKE %:searchValue% "
					+ "OR LOWER(erc.caste_stage_arn) LIKE %:searchValue% OR CAST(erc.demand AS VARCHAR) LIKE %:searchValue% "
					+ "OR LOWER(erc.recovery_stage_arn) LIKE %:searchValue% OR CAST(erc.recovery_stage AS VARCHAR) LIKE %:searchValue% "
					+ "OR CAST(erc.recovery_by_drc3 AS VARCHAR) LIKE %:searchValue% OR CAST(erc.recovery_against_demand AS VARCHAR) LIKE %:searchValue% "
					+ "OR LOWER(mdfas.name) like %:searchValue% OR LOWER(mdfcs.name) LIKE %:searchValue%)", nativeQuery = true)
	Page<EnforcementReviewCase> findBySearchValue(@Param("searchValue") String searchValue, Pageable pageable);
	/////////////////////// view page

	@Query(value = "select count(gstin) FROM\r\n"
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "WHERE\r\n" + "murm.user_role_id = 2 \r\n" + "AND murm.circle_id IN (?1)\r\n" + "", nativeQuery = true)
	String getViewWiseDashBoardTotalCases(List<String> workingLocation);

	@Query(value = "select count (gstin) from\r\n"
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "where murm.user_role_id = 2\r\n"
			+ "AND working_location in(?1) and action not in('upload', 'hqTransfer', 'transfer')\r\n"
			+ "", nativeQuery = true)
	String getViewWiseDashBoardTotalAcknowledgedCases(List<String> workingLocation);

	@Query(value = "select count (gstin) from \r\n"
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ " \r\n" + "AND working_location in(?1) and action not in ('upload', 'hqTransfer', 'transfer') and\r\n"
			+ "erc.action_status != 1", nativeQuery = true)
	String getViewWiseDashBoardTotalInitiatedCases(List<String> workingLocation);

	@Query(value = "select count (gstin) from \r\n"
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "where murm.user_role_id = 2\r\n"
			+ "AND working_location in(?1) and action not in ('acknowledge', 'upload', 'hqTransfer', 'transfer')\r\n", nativeQuery = true)
	String getViewWiseDashBoardTotalCasesClosedByFo(List<String> workingLocation);

	@Query(value = "select sum(indicative_tax_value) from \r\n"
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ " where murm.user_role_id = 2\r\n" + "AND working_location in(?1)", nativeQuery = true)
	Long getViewWiseDashBoardTotalSuspectedIndicativeAmount(List<String> workingLocation);

	@Query(value = "select sum(demand) from "
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "where murm.user_role_id = 2\r\n" + "AND working_location in(?1)", nativeQuery = true)
	Long getViewWiseDashBoardTotalAmount(List<String> workingLocation);

	@Query(value = "select sum(demand) from "
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "where  murm.user_role_id = 2 \r\n"
			+ "AND action_status = '3' and case_stage in ('5', '8', '9') and working_location in(?1)", nativeQuery = true)
	Long getViewWiseDashBoardTotalDemand(List<String> workingLocation);

	@Query(value = "select sum(recovery_by_drc3 + recovery_against_demand) from "
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "where murm.user_role_id = 2 \r\n" + "AND working_location in(?1)", nativeQuery = true)
	Long getViewWiseDashBoardTotalRecovery(List<String> workingLocation);

	@Query(value = "select erc.category, sum(erc.indicative_tax_value) \r\n" + "from\r\n"
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "where working_location in (?1) \r\n" + "AND murm.user_role_id = 2", nativeQuery = true)
	String getViewWiseTaxValue(List<String> workingLocation);

	@Query(value = "select erc.category,count (gstin) from\r\n"
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id\r\n"
			+ "where working_location in(?1) and murm.user_role_id = 2 \r\n"
			+ "group by erc.category", nativeQuery = true)
	List<Object[]> getTotalIndicativeTaxValueCategoryAndViewWise(List<String> locationIds);

	@Query(value = "select erc.category, SUM(CAST(erc.demand AS BIGINT)) from\r\n"
			+ "analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id =murm.user_id \r\n"
			+ "inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id \r\n"
			+ "where murm.user_role_id = 2 \r\n" + "and working_location in(?1) \r\n"
			+ "group by erc.category", nativeQuery = true)
	List<Object[]> getTotalDemandCategoryAndViewWise(List<String> locationIds);

	@Query(value = "SELECT erc.category, \r\n"
			+ "       COALESCE(SUM(CAST(erc.recovery_against_demand AS BIGINT)) + SUM(CAST(erc.recovery_by_drc3 AS BIGINT)), 0) AS total_recovery\r\n"
			+ "FROM analytics.user_details ud\r\n"
			+ "INNER JOIN analytics.mst_user_role_mapping murm ON ud.user_id = murm.user_id \r\n"
			+ "INNER JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "INNER JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.district_id \r\n"
			+ "WHERE murm.user_role_id = 2 \r\n" + "AND erc.working_location IN(?1)\r\n" + "GROUP BY erc.category\r\n"
			+ "", nativeQuery = true)
	List<Object[]> getTotalRecoveryCategoryAndViewWise(List<String> locationIds);
	////////////////////////////////////

	@Query(value = "SELECT * FROM analytics.enforcement_review_case erc where erc.recovery_status = 'foRecoveryClose' \r\n"
			+ "and erc.full_recovery_date < CURRENT_DATE - INTERVAL '3 months' ", nativeQuery = true)
	List<EnforcementReviewCase> getlistOfRecoveryCasePendingForApproval();

	@Query(value = "select erc.*\r\n"
			+ "	from analytics.user_details ud inner join analytics.mst_user_role_mapping murm on ud.user_id = murm.user_id\r\n"
			+ "	inner JOIN analytics.mst_location_mapping mlm ON murm.circle_id = mlm.circle_id\r\n"
			+ "	inner JOIN analytics.enforcement_review_case erc ON erc.working_location = mlm.circle_id OR erc.working_location = mlm.zone_id\r\n"
			+ " inner JOIN analytics.mst_dd_hq_category mdhc ON erc.category = mdhc.name "
			+ "	where murm.user_role_id = 2 AND murm.circle_id IN (?1)\r\n"
			+ "	AND erc.period IN (?2) and ud.user_id = ?3 and mdhc.id = ?4 ", nativeQuery = true)
	List<EnforcementReviewCase> enforcementReviewCaseListFoWise(String locationIds, String category, Integer userId,
			Long categoryId);

	@Query(value = "select * from analytics.enforcement_review_case \r\n" + "where gstin =?1 and period =?2 \r\n"
			+ "and parameter like CONCAT('%', ?3, '%') ", nativeQuery = true)
	List<EnforcementReviewCase> findAssesmentCaseByGstinAndPeriodAndCategory(String gstin, String period,
			String parameter);

	@Query(value = "select erc.* from analytics.enforcement_review_case erc where erc.audit_case_id in (?1)", nativeQuery = true)
	List<EnforcementReviewCase> findAllByAuditCaseId(List<String> auditCaseIds);

	@Query(value = "SELECT erc.category as category, mpmw.param_name, COUNT(*) AS totalRows, \r\n"
			+ "SUM(indicative_tax_value) AS totalIndicativeTax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as totalAmount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as totalRecovery,\r\n"
			+ "table1.orginal_demand as totalDemand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
			+ "(select category, sum(demand) as orginal_demand from analytics.enforcement_review_case \r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and split_part(parameter, ',', 1) = ?2 group by category) table1\r\n"
			+ "on erc.category = table1.category \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.\"parameter\" , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "where erc.category = ?1 and split_part(erc.parameter, ',', 1) = ?2 \r\n"
			+ "GROUP BY erc.category, table1.orginal_demand, mpmw.param_name, split_part(erc.parameter, ',', 1)", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAnd1stParameterId(String categoryName, String parameterId);

	@Query(value = "SELECT erc.category as category, mpmw.param_name, COUNT(*) AS totalRows, \r\n"
			+ "SUM(indicative_tax_value) AS totalIndicativeTax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as totalAmount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as totalRecovery,\r\n"
			+ "table1.orginal_demand as totalDemand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
			+ "(select category, sum(demand) as orginal_demand from analytics.enforcement_review_case erc1 FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau1  \r\n"
			+ "ON erc1.gstin = ercau1.gstin and erc1.case_reporting_date = ercau1.case_reporting_date and erc1.period = ercau1.period\r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and split_part(parameter, ',', 1) = ?2 and ercau1.fo_user_id = ?3 group by category) table1\r\n"
			+ "on erc.category = table1.category \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and split_part(erc.parameter, ',', 1) = ?2 and ercau.fo_user_id = ?3 \r\n"
			+ "GROUP BY erc.category, table1.orginal_demand, mpmw.param_name, split_part(erc.parameter, ',', 1)", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAnd1stParameterIdAndFoUserId(String categoryName, String parameterId,
			Integer foUserId);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,99,99,990') as recovery_against_demand, STRING_AGG(pt.param_name , ', ') AS parameter_names, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
			+ "WHERE erc.category = ?1 AND split_part(erc.parameter, ',', 1) = ?2 \r\n"
			+ "group by erc.parameter, erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findByCategoryAnd1stParameterId(String category, String parameterId);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,99,99,990') as recovery_against_demand, STRING_AGG(pt.param_name , ', ') AS parameter_names, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
			+ "WHERE erc.category = ?1 AND split_part(erc.parameter, ',', 1) = ?2 and ercau.fo_user_id = ?3 \r\n"
			+ "group by erc.parameter, erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findByCategoryAnd1stParameterIdAndFOUserId(String category, String parameterId, Integer foUserId);

	@Query(value = "SELECT erc.category, mpmw.param_name, COUNT(*) AS total_number_of_cases, SUM(indicative_tax_value) AS total_indicative_tax, SUM(CAST(demand AS bigint)) AS total_amount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) AS total_recovery,table1.original_demand AS total_demand, \r\n"
			+ "COALESCE(NULLIF(split_part(erc.parameter, ',', 1), ''), '0') AS parameter_id\r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n" + "FULL OUTER JOIN (\r\n"
			+ "SELECT COALESCE(NULLIF(split_part(parameter, ',', 1), ''), '0') AS first_parameter, SUM(CAST(demand AS bigint)) AS original_demand\r\n"
			+ "FROM analytics.enforcement_review_case WHERE action_status = '3' AND case_stage IN ('5', '8', '9') AND category = ?1 GROUP BY \r\n"
			+ "COALESCE(NULLIF(split_part(parameter, ',', 1), ''), '0')) table1 \r\n"
			+ "ON COALESCE(NULLIF(split_part(erc.parameter, ',', 1), ''), '0') = table1.first_parameter\r\n"
			+ "FULL OUTER JOIN analytics.mst_parameters_module_wise mpmw ON \r\n"
			+ "COALESCE(NULLIF(split_part(erc.parameter, ',', 1), ''), '0') = CAST(mpmw.id AS text)\r\n"
			+ "WHERE erc.category = ?1 \r\n"
			+ "GROUP BY COALESCE(NULLIF(split_part(erc.parameter, ',', 1), ''), '0'), erc.category, table1.original_demand, mpmw.param_name", nativeQuery = true)
	List<String[]> getAllCaseCountByCategory(String category);

	@Query(value = "select erc.category, mpmw.param_name, COUNT(*) AS total_number_of_Cases, \r\n"
			+ "SUM(indicative_tax_value) AS total_indicative_tax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as total_amount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as total_recovery,\r\n"
			+ "table1.orginal_demand as total_demand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc \r\n"
			+ "FULL OUTER join (select distinct split_part(parameter, ',', 1) as first_parameter, sum(demand) as orginal_demand from analytics.enforcement_review_case erc1 FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau1  \r\n"
			+ "ON erc1.gstin = ercau1.gstin and erc1.case_reporting_date = ercau1.case_reporting_date and erc1.period = ercau1.period\r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and ercau1.fo_user_id = ?2 group by split_part(parameter, ',', 1)) table1\r\n"
			+ "on split_part(erc.parameter , ',', 1) = table1.first_parameter \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter , ',', 1) =  cast(mpmw.id as text) \r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and ercau.fo_user_id = ?2 \r\n"
			+ "GROUP BY split_part(erc.parameter, ',', 1), erc.category, table1.orginal_demand, mpmw.param_name", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAndFOUserId(String category, Integer fouserId);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,99,99,990') as recovery_against_demand, erc.parameter, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "WHERE erc.category = ?1 \r\n"
			+ "group by erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name;", nativeQuery = true)
	List<String[]> findReviewCasesListByCategory(String category);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,99,99,990') as recovery_against_demand, erc.parameter, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "WHERE erc.category = ?1 and ercau.fo_user_id = ?2 \r\n"
			+ "group by erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findReviewCasesListByCategoryAndFOUserId(String category, Integer foUserId);

	@Query(value = "select STRING_AGG(pt.param_name , ', ') AS parameter_names from analytics.enforcement_review_case erc \r\n"
			+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
			+ "where erc.gstin = ?1 and erc.period = ?2 and erc.case_reporting_date = ?3 ", nativeQuery = true)
	String findParameterNamesById(String gstIn, String period, Date reportingDate);

	@Query(value = "SELECT erc.category as category, mpmw.param_name, COUNT(*) AS totalRows, \r\n"
			+ "SUM(indicative_tax_value) AS totalIndicativeTax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as totalAmount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as totalRecovery,\r\n"
			+ "table1.orginal_demand as totalDemand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
			+ "(select category, sum(demand) as orginal_demand from analytics.enforcement_review_case erc1 FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau1  \r\n"
			+ "ON erc1.gstin = ercau1.gstin and erc1.case_reporting_date = ercau1.case_reporting_date and erc1.period = ercau1.period\r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and split_part(parameter, ',', 1) = ?2 and ercau1.ap_user_id = ?3 group by category) table1\r\n"
			+ "on erc.category = table1.category \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and split_part(erc.parameter, ',', 1) = ?2 and ercau.ap_user_id = ?3 \r\n"
			+ "GROUP BY erc.category, table1.orginal_demand, mpmw.param_name, split_part(erc.parameter, ',', 1)", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAnd1stParameterIdAndApUserId(String categoryName, String parameterId,
			Integer apUserId);

	@Query(value = "select erc.category, mpmw.param_name, COUNT(*) AS total_number_of_Cases, \r\n"
			+ "SUM(indicative_tax_value) AS total_indicative_tax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as total_amount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as total_recovery,\r\n"
			+ "table1.orginal_demand as total_demand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc \r\n"
			+ "FULL OUTER join (select distinct split_part(parameter, ',', 1) as first_parameter, sum(demand) as orginal_demand from analytics.enforcement_review_case erc1 FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau1  \r\n"
			+ "ON erc1.gstin = ercau1.gstin and erc1.case_reporting_date = ercau1.case_reporting_date and erc1.period = ercau1.period\r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and ercau1.ap_user_id = ?2 group by split_part(parameter, ',', 1)) table1\r\n"
			+ "on split_part(erc.parameter , ',', 1) = table1.first_parameter \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter , ',', 1) =  cast(mpmw.id as text) \r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and ercau.ap_user_id = ?2 \r\n"
			+ "GROUP BY split_part(erc.parameter, ',', 1), erc.category, table1.orginal_demand, mpmw.param_name", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAndApUserId(String category, Integer apUserId);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,990') as recovery_against_demand, STRING_AGG(pt.param_name , ', ') AS parameter_names, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
			+ "WHERE erc.category = ?1 AND split_part(erc.parameter, ',', 1) = ?2 and ercau.ap_user_id = ?3 \r\n"
			+ "group by erc.parameter, erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findByCategoryAnd1stParameterIdAndApUserId(String category, String parameterId, Integer apUserId);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,990') as recovery_against_demand, erc.parameter, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "WHERE erc.category = ?1 and ercau.ap_user_id = ?2 \r\n"
			+ "group by erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findReviewCasesListByCategoryAndApUserId(String category, Integer apUserId);

	@Query(value = "SELECT erc.category as category, mpmw.param_name, COUNT(*) AS totalRows, \r\n"
			+ "SUM(indicative_tax_value) AS totalIndicativeTax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as totalAmount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as totalRecovery,\r\n"
			+ "table1.orginal_demand as totalDemand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
			+ "(select category, sum(demand) as orginal_demand from analytics.enforcement_review_case erc1 FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau1  \r\n"
			+ "ON erc1.gstin = ercau1.gstin and erc1.case_reporting_date = ercau1.case_reporting_date and erc1.period = ercau1.period\r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and split_part(parameter, ',', 1) = ?2 and ercau1.ru_user_id = ?3 group by category) table1\r\n"
			+ "on erc.category = table1.category \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and split_part(erc.parameter, ',', 1) = ?2 and ercau.ru_user_id = ?3 \r\n"
			+ "GROUP BY erc.category, table1.orginal_demand, mpmw.param_name, split_part(erc.parameter, ',', 1)", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAnd1stParameterIdAndRuUserId(String categoryName, String parameterId,
			Integer ruUserId);

	@Query(value = "select erc.category, mpmw.param_name, COUNT(*) AS total_number_of_Cases, \r\n"
			+ "SUM(indicative_tax_value) AS total_indicative_tax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as total_amount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as total_recovery,\r\n"
			+ "table1.orginal_demand as total_demand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc \r\n"
			+ "FULL OUTER join (select distinct split_part(parameter, ',', 1) as first_parameter, sum(demand) as orginal_demand from analytics.enforcement_review_case erc1 FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau1  \r\n"
			+ "ON erc1.gstin = ercau1.gstin and erc1.case_reporting_date = ercau1.case_reporting_date and erc1.period = ercau1.period\r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and ercau1.ru_user_id = ?2 group by split_part(parameter, ',', 1)) table1\r\n"
			+ "on split_part(erc.parameter , ',', 1) = table1.first_parameter \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter , ',', 1) =  cast(mpmw.id as text) \r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "where erc.category = ?1 and ercau.ru_user_id = ?2 \r\n"
			+ "GROUP BY split_part(erc.parameter, ',', 1), erc.category, table1.orginal_demand, mpmw.param_name", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAndRuUserId(String category, Integer ruUserId);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,990') as recovery_against_demand, STRING_AGG(pt.param_name , ', ') AS parameter_names, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
			+ "WHERE erc.category = ?1 AND split_part(erc.parameter, ',', 1) = ?2 and ercau.ru_user_id = ?3 \r\n"
			+ "group by erc.parameter, erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findByCategoryAnd1stParameterIdAndRuUserId(String category, String parameterId, Integer ruUserId);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,990') as recovery_against_demand, erc.parameter, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "FULL OUTER JOIN analytics.enforcement_review_case_assigned_users ercau  \r\n"
			+ "ON erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "WHERE erc.category = ?1 and ercau.ru_user_id = ?2 \r\n"
			+ "group by erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findReviewCasesListByCategoryAndRuUserId(String category, Integer ruUserId);

	@Query(value = "SELECT * FROM analytics.enforcement_review_case erc " + "WHERE erc.action = 'deemApproved' "
			+ "AND erc.assigned_from = 'RU' " + "AND erc.assigned_to = 'AP' "
			+ "AND erc.case_update_date < NOW() - CAST(:months || ' months' AS INTERVAL) UNION \r\n"
			+ "SELECT erc.* FROM analytics.enforcement_review_case erc \r\n"
			+ "INNER JOIN analytics.deemed_approved_cases_threshold_dates dac ON erc.period = dac.period\r\n"
			+ "WHERE erc.assigned_to = 'AP' AND erc.assigned_from = 'RU' AND erc.action = 'deemApproved' \r\n"
			+ "AND erc.case_update_date >= dac.threshold_dates", nativeQuery = true)
	List<EnforcementReviewCase> findAllDeemedApprovedCasesbyLastUpdatedTimeStampMoreThanMonthsOrPeriodThresholdDates(
			@Param("months") Long months);

	@Query(value = "select * from analytics.enforcement_review_case erc \r\n"
			+ "where indicative_tax_value <= ?1 and assigned_from = 'RU' AND assigned_to = 'AP' and action = 'verifyerRecommended'", nativeQuery = true)
	List<EnforcementReviewCase> findAllCasesBelowTheDeemedApprovedThresholdValue(
			Long maximunIndicativeTaxValueForDeemApproval);

	@Query(value = "SELECT erc.category as category, mpmw.param_name, COUNT(*) AS totalRows, \r\n"
			+ "SUM(indicative_tax_value) AS totalIndicativeTax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as totalAmount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as totalRecovery,\r\n"
			+ "table1.orginal_demand as totalDemand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc FULL OUTER JOIN\r\n"
			+ "(select category, sum(demand) as orginal_demand from analytics.enforcement_review_case erc1\r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and split_part(parameter, ',', 1) = ?2 \r\n"
			+ "and erc1.working_location in (?3) and erc1.action not in (?4) group by category) table1\r\n"
			+ "on erc.category = table1.category \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter , ',', 1) =  cast(mpmw.id as text)\r\n"
			+ "where erc.category = ?1 and split_part(erc.parameter, ',', 1) = ?2 and erc.working_location in (?3) and erc.action not in (?4)\r\n"
			+ "GROUP BY erc.category, table1.orginal_demand, mpmw.param_name, split_part(erc.parameter, ',', 1)", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAnd1stParameterIdAndLocationListAndExceptActionList(String categoryName,
			String parameterId, List<String> locationList, List<String> exceptActionList);

	@Query(value = "select erc.category, mpmw.param_name, COUNT(*) AS total_number_of_Cases, \r\n"
			+ "SUM(indicative_tax_value) AS total_indicative_tax, \r\n"
			+ "SUM(CAST(demand AS bigint)) as total_amount, \r\n"
			+ "(SUM(CAST(recovery_against_demand AS bigint)) + SUM(CAST(recovery_by_drc3 AS bigint))) as total_recovery,\r\n"
			+ "table1.orginal_demand as total_demand, split_part(erc.parameter, ',', 1) as parameterId \r\n"
			+ "FROM analytics.enforcement_review_case erc \r\n"
			+ "FULL OUTER join (select distinct split_part(parameter, ',', 1) as first_parameter, sum(demand) as orginal_demand from analytics.enforcement_review_case erc1\r\n"
			+ "where action_status = '3' and case_stage in ('5', '8', '9') and category = ?1 and erc1.working_location in (?2) \r\n"
			+ "and erc1.action not in (?3) group by split_part(parameter, ',', 1)) table1\r\n"
			+ "on split_part(erc.parameter , ',', 1) = table1.first_parameter \r\n"
			+ "FULL OUTER join analytics.mst_parameters_module_wise mpmw on split_part(erc.parameter , ',', 1) =  cast(mpmw.id as text) \r\n"
			+ "where erc.category = ?1 and erc.working_location in (?2) and erc.action not in (?3)\r\n"
			+ "GROUP BY split_part(erc.parameter, ',', 1), erc.category, table1.orginal_demand, mpmw.param_name", nativeQuery = true)
	List<String[]> getAllCaseCountByCategoryAndLocationListAndExceptActionList(String categoryName,
			List<String> locationList, List<String> exceptActionList);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,99,99,990') as recovery_against_demand, STRING_AGG(pt.param_name , ', ') AS parameter_names, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "JOIN LATERAL unnest(string_to_array(erc.parameter, ',')) AS param_id ON TRUE\r\n"
			+ "JOIN analytics.mst_parameters_module_wise pt ON param_id = cast(pt.id as text) \r\n"
			+ "WHERE erc.category = ?1 AND split_part(erc.parameter, ',', 1) = ?2 and erc.working_location in (?3) and erc.action not in (?4)\r\n"
			+ "group by erc.parameter, erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findByCategoryAnd1stParameterIdAndLocationListAndExceptActionList(String category,
			String parameterId, List<String> locationList, List<String> exceptActionList);

	@Query(value = "SELECT erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, TO_CHAR(erc.case_reporting_date, 'DD-MM-YYYY') as formatted_case_reporting_date, \r\n"
			+ "TO_CHAR(erc.indicative_tax_value, 'FM99,99,99,99,99,990') as indicative_tax_value, mur.name AS assigned_user_name, mdfas.name AS action_status_name, erc.case_id, \r\n"
			+ "mdfcs.name AS case_stage_name, erc.caste_stage_arn, TO_CHAR(erc.demand, 'FM99,99,99,99,99,990') as demand, mdfrs.name AS recovery_stage_name, \r\n"
			+ "erc.recovery_stage_arn, TO_CHAR(erc.recovery_by_drc3, 'FM99,99,99,99,99,990') as recovery_by_drc3, \r\n"
			+ "TO_CHAR(erc.recovery_against_demand, 'FM99,99,99,99,99,990') as recovery_against_demand, erc.parameter, end2.extension_file_name  \r\n"
			+ "FROM analytics.enforcement_review_case erc\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_location_details mdld ON erc.working_location = mdld.location_id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_action_status mdfas ON erc.action_status = mdfas.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_user_role mur ON erc.assigned_to = mur.code\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_case_stage mdfcs ON erc.case_stage = mdfcs.id\r\n"
			+ "FULL OUTER JOIN analytics.mst_dd_fo_recovery_stage mdfrs ON erc.recovery_stage = mdfrs.id\r\n"
			+ "FULL OUTER JOIN analytics.extension_no_documents end2 ON erc.extension_file_name_id = end2.id\r\n"
			+ "WHERE erc.category = ?1 and erc.working_location in (?2) and erc.action not in (?3)\r\n"
			+ "group by erc.gstin, erc.taxpayer_name, mdld.location_name, erc.period, erc.extension_no, erc.case_reporting_date, \r\n"
			+ "erc.indicative_tax_value, mur.name, mdfas.name, erc.case_id, mdfcs.name, erc.caste_stage_arn, erc.demand, mdfrs.name, \r\n"
			+ "erc.recovery_stage_arn, erc.recovery_by_drc3, erc.recovery_against_demand, end2.extension_file_name", nativeQuery = true)
	List<String[]> findReviewCasesListByCategoryAndLocationListAndExceptActionList(String category,
			List<String> locationList, List<String> exceptActionList);

	@Query(value = "select count(gstin) from analytics.enforcement_review_case erc where assigned_to in ('AP', 'RU') and category != 'Old Cases' and working_location in (?1)", nativeQuery = true)
	Optional<String> getVerificationTotalCases(List<String> locationIdList);

	@Query(value = "select count(gstin) from analytics.enforcement_review_case erc "
			+ "where assigned_to in ('AP')  and category != 'Old Cases' and working_location in (?1)", nativeQuery = true)
	Optional<String> getPendingVerificationTotalCases(List<String> locationIdList);

	@Query(value = "select count(gstin) from analytics.enforcement_review_case erc where assigned_to in ('AP') and action != 'deemApproved' and category != 'Old Cases' and working_location in (?1)", nativeQuery = true)
	Optional<String> getApprovalTotalCases(List<String> locationId);

	@Query(value = "select count(gstin) from analytics.enforcement_review_case erc "
			+ "where action in ('closed') and category != 'Old Cases' and working_location in (?1)", nativeQuery = true)
	Optional<String> getPendingApprovalTotalCases(List<String> locationId);

	@Query(value = "SELECT count(case_id) FROM analytics.enforcement_review_case erc WHERE UPPER(case_id) = UPPER(?1)", nativeQuery = true)
	Integer findTotalCountOfCaseIdMatches(String caseId);

	@Query(value = "select erc.* from analytics.enforcement_master em \r\n"
			+ "join analytics.enforcement_master_allocating_user emau on em.gstin = emau.gstin and em.case_reporting_date = emau.case_reporting_date and em.period = emau.period \r\n"
			+ "join analytics.enforcement_review_case erc on em.gstin = erc.gstin and em.case_reporting_date = erc.case_reporting_date and em.period = erc.period \r\n"
			+ "where emau.enf_fo_user_id = ?1 and em.case_location_location_id in (?2)", nativeQuery = true)
	List<EnforcementReviewCase> findEnforcementReviewCaseListByEnforcementMasterUserIdAndLocationList(Integer userId,
			List<String> allMappedLocations);
}
