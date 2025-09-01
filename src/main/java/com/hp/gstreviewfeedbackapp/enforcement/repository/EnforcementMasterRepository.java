package com.hp.gstreviewfeedbackapp.enforcement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.enforcement.model.EnforcementMaster;
import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;

@Repository
public interface EnforcementMasterRepository extends JpaRepository<EnforcementMaster, CompositeKey> {
	@Query(value = "select count(em.*) from analytics.enforcement_master em", nativeQuery = true)
	Integer findTotalCountOfCases();

	@Query(value = "select TO_CHAR(sum(em.indicative_tax_value), 'FM99,99,99,99,99,99,990') from analytics.enforcement_master em", nativeQuery = true)
	String findTotalSumOfIndicativeTaxValue();

	@Query(value = "select count(em.*) from analytics.enforcement_master em \r\n"
			+ "left outer join analytics.enforcement_action_status eas on em.action_id = eas.id \r\n"
			+ "where eas.code_name != 'upload'", nativeQuery = true)
	Integer findTotalCountOfActiontakenCases();

	@Query(value = "select * from analytics.enforcement_master em \r\n"
			+ "inner join analytics.enforcement_master_allocating_user emau on emau.gstin = em.gstin and emau.case_reporting_date = em.case_reporting_date and emau.\"period\" = em.\"period\" \r\n"
			+ "inner join analytics.enforcement_action_status eas on eas.id = em.action_id \r\n"
			+ "where em.case_location_location_id in (?1) and emau.enf_fo_user_id in (?3) \r\n"
			+ "and assigned_to = 'Enforcement_FO' and eas.code_name in (?2)", nativeQuery = true)
	List<EnforcementMaster> findAllEnforcementFoCasesByWorkingLocationListAndActionListAndUserIdList(
			List<String> allMappedLocations, List<String> actionList, List<Integer> userIdList);

	@Query(value = "select * from analytics.enforcement_master em \r\n"
			+ "inner join analytics.enforcement_master_allocating_user emau on emau.gstin = em.gstin and emau.case_reporting_date = em.case_reporting_date and emau.\"period\" = em.\"period\" \r\n"
			+ "inner join analytics.enforcement_action_status eas on eas.id = em.action_id \r\n"
			+ "where em.case_location_location_id in (?1) and emau.enf_fo_user_id in (?4) \r\n"
			+ "and em.category_id = ?2 and assigned_to = 'Enforcement_FO' and eas.code_name in (?3)", nativeQuery = true)
	List<EnforcementMaster> findAllEnforcementFoCasesByWorkingLocationListAndCatgoryIdAndActionListAndUserIdList(
			List<String> allMappedLocations, Integer categoryId, List<String> actionList, List<Integer> userIdList);

	@Query(value = "select count(em.*)  from analytics.enforcement_master em \r\n"
			+ "inner join analytics.enforcement_master_allocating_user emau \r\n"
			+ "on emau.gstin = em.gstin and emau.case_reporting_date = em.case_reporting_date and emau.\"period\" = em.\"period\"\r\n"
			+ "inner join analytics.enforcement_action_status eas on eas.id = em.action_id \r\n"
			+ "where emau.enf_fo_user_id in (?1) and em.assigned_to = 'Enforcement_FO' \r\n"
			+ "and eas.code_name in (?2) and em.case_location_location_id in (?3)", nativeQuery = true)
	Integer findTotalCountOfCasesPendingWithEnfFoByUserIdListAndActionList(List<Integer> userIdList,
			List<String> actionList, List<String> locationIdList);

	@Query(value = "select * from analytics.enforcement_master em \r\n"
			+ "inner join analytics.enforcement_master_allocating_user emau on emau.gstin = em.gstin and emau.case_reporting_date = em.case_reporting_date and emau.\"period\" = em.\"period\" \r\n"
			+ "inner join analytics.enforcement_action_status eas on eas.id = em.action_id \r\n"
			+ "where em.case_location_location_id in (?1) and emau.enf_svo_user_id in (?3) \r\n"
			+ "and assigned_to = 'Enforcement_SVO' and eas.code_name in (?2)", nativeQuery = true)
	List<EnforcementMaster> findAllEnforcementSvoCasesByWorkingLocationListAndActionListAndUserIdList(
			List<String> allMappedLocations, List<String> actionList, List<Integer> userIdList);

	@Query(value = "select * from analytics.enforcement_master em \r\n"
			+ "inner join analytics.enforcement_master_allocating_user emau on emau.gstin = em.gstin and emau.case_reporting_date = em.case_reporting_date and emau.\"period\" = em.\"period\" \r\n"
			+ "inner join analytics.enforcement_action_status eas on eas.id = em.action_id \r\n"
			+ "where em.case_location_location_id in (?1) and emau.enf_svo_user_id in (?4) \r\n"
			+ "and em.category_id = ?2 and assigned_to = 'Enforcement_SVO' and eas.code_name in (?3)", nativeQuery = true)
	List<EnforcementMaster> findAllEnforcementSvoCasesByWorkingLocationListAndCatgoryIdAndActionListAndUserIdList(
			List<String> allMappedLocations, Integer categoryId, List<String> actionList, List<Integer> userIdList);

	@Query(value = "select count(em.*)  from analytics.enforcement_master em \r\n"
			+ "inner join analytics.enforcement_master_allocating_user emau \r\n"
			+ "on emau.gstin = em.gstin and emau.case_reporting_date = em.case_reporting_date and emau.\"period\" = em.\"period\"\r\n"
			+ "inner join analytics.enforcement_action_status eas on eas.id = em.action_id \r\n"
			+ "where emau.enf_svo_user_id in (?1) and em.assigned_to = 'Enforcement_SVO' \r\n"
			+ "and eas.code_name in (?2) and em.case_location_location_id in (?3)", nativeQuery = true)
	Object findTotalCountOfCasesPendingWithEnfSvoByUserIdListAndActionList(List<Integer> userIdList,
			List<String> actionList, List<String> locationIdList);

	@Query(value = "select distinct period from analytics.enforcement_master em ", nativeQuery = true)
	List<String> findfinancialyearlistenforcement();

	@Query(value = "SELECT DISTINCT mpmw.id, mpmw.param_name\r\n" + "FROM analytics.mst_parameters_module_wise mpmw\r\n"
			+ "WHERE mpmw.id IN (\r\n" + "  SELECT DISTINCT CAST(value AS INTEGER)\r\n"
			+ "  FROM analytics.enforcement_master em,\r\n"
			+ "       unnest(string_to_array(em.parameter, ',')) AS value\r\n" + "  WHERE TRIM(value) <> ''\r\n "
			+ "); ", nativeQuery = true)
	String[][] findcategoriesenforcement();

	@Query(value = " SELECT \r\n" + "                mlm.zone_name AS zoneName,\r\n"
			+ "                mlm.circle_name AS circleName,\r\n"
			+ "                SUM(em.indicative_tax_value) AS indicativeTaxValue,\r\n"
			+ "                COUNT(DISTINCT em.gstin) AS allottedCases,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 9 THEN em.gstin END) AS casesCompleted,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 1 THEN em.gstin END) AS notAcknowledged,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 2 THEN em.gstin END) AS transferToScrutiny,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 3 THEN em.gstin END) AS acknowledged,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 4 THEN em.gstin END) AS panchnama,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 5 THEN em.gstin END) AS preliminaryReport,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 6 THEN em.gstin END) AS finalReport,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 7 THEN em.gstin END) AS referToAdjudication,\r\n"
			+ "                COUNT(DISTINCT CASE WHEN em.action_id = 8 THEN em.gstin END) AS showCauseIssued,\r\n"
			+ "                0 AS sortOrder,\r\n" + "                mlm.zone_name AS zoneSort\r\n"
			+ "            FROM analytics.enforcement_master em\r\n"
			+ "            JOIN analytics.mst_location_mapping mlm ON em.case_location_location_id = mlm.circle_id\r\n"
			+ "            WHERE ((',' || em.parameter || ',') LIKE CONCAT('%,', :parameterId, ',%') OR :parameterId = '-1')\r\n"
			+ "              AND (em.period = :period OR :period = '-1')  and em.case_location_location_id in (:allMappedLocations)\r\n"
			+ "            GROUP BY mlm.zone_name, mlm.circle_name ", nativeQuery = true)
	List<Object[]> getDashboardSummaryByParamAndPeriod(@Param("parameterId") String parameterId,
			@Param("period") String period, @Param("allMappedLocations") List<String> allMappedLocations);

	@Query(value = "select * from analytics.enforcement_master em where em.case_location_location_id in(?1)", nativeQuery = true)
	List<EnforcementMaster> getAllEnforcementCasesList(List<String> workingLoacationList);
	
	 @Query(value = "select * from analytics.enforcement_master em where em.action_id =(?1) and em.case_location_location_id in(?2)", nativeQuery = true)
	List<EnforcementMaster> getAllEnforcementCasesListByAction(int i, List<String> workingLoacationList);

	
    @Query(value = "  						SELECT * FROM ( 	SELECT \r\n"
    		+ "    		                mlm.zone_name AS zoneName,\r\n"
    		+ "    		                mlm.circle_name AS circleName,\r\n"
    		+ "    		                SUM(em.indicative_tax_value) AS indicativeTaxValue,\r\n"
    		+ "    		                COUNT(DISTINCT em.gstin) AS allottedCases,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 9 THEN em.gstin END) AS casesCompleted,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 1 THEN em.gstin END) AS notAcknowledged,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 2 THEN em.gstin END) AS transferToScrutiny,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 3 THEN em.gstin END) AS acknowledged,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 4 THEN em.gstin END) AS panchnama,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 5 THEN em.gstin END) AS preliminaryReport,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 6 THEN em.gstin END) AS finalReport,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 7 THEN em.gstin END) AS referToAdjudication,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 8 THEN em.gstin END) AS showCauseIssued,\r\n"
    		+ "    		                0 AS sortOrder,\r\n"
    		+ "    		                mlm.zone_name AS zoneSort\r\n"
    		+ "    		            FROM analytics.enforcement_master em\r\n"
    		+ "    		            JOIN analytics.mst_location_mapping mlm ON em.case_location_location_id = mlm.circle_id\r\n"
    		+ "    		            WHERE ((',' || em.parameter || ',') LIKE CONCAT('%,', :parameterId, ',%') OR :parameterId = '-1')\r\n"
    		+ "    		              AND (em.period = :period OR :period = '-1')  \r\n"
    		+ "   		              and em.case_location_location_id in (:allMappedLocations)\r\n"
    		+ "    		            GROUP BY mlm.zone_name, mlm.circle_name\r\n"
    		+ "    		            \r\n"
    		+ "    		            UNION ALL \r\n"
    		+ "	  		\r\n"
    		+ "	  		    SELECT \r\n"
    		+ "	  		        'Zone Total' AS zoneName,\r\n"
    		+ "	  		        '' AS circleName,\r\n"
    		+ "	  		         SUM(em.indicative_tax_value) AS indicativeTaxValue,\r\n"
    		+ "    		                COUNT(DISTINCT em.gstin) AS allottedCases,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 9 THEN em.gstin END) AS casesCompleted,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 1 THEN em.gstin END) AS notAcknowledged,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 2 THEN em.gstin END) AS transferToScrutiny,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 3 THEN em.gstin END) AS acknowledged,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 4 THEN em.gstin END) AS panchnama,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 5 THEN em.gstin END) AS preliminaryReport,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 6 THEN em.gstin END) AS finalReport,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 7 THEN em.gstin END) AS referToAdjudication,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 8 THEN em.gstin END) AS showCauseIssued,\r\n"
    		+ "	  		        1 AS sortOrder,\r\n"
    		+ "	  		        mlm.zone_name AS zoneSort\r\n"
    		+ "	  		 FROM analytics.enforcement_master em\r\n"
    		+ "    		            JOIN analytics.mst_location_mapping mlm ON em.case_location_location_id = mlm.circle_id\r\n"
    		+ "    		            WHERE ((',' || em.parameter || ',') LIKE CONCAT('%,', :parameterId, ',%') OR :parameterId = '-1')\r\n"
    		+ "    		              AND (em.period = :period OR :period = '-1')  \r\n"
    		+ "    		              and em.case_location_location_id in (:allMappedLocations)\r\n"
    		+ "    		            GROUP BY mlm.zone_name\r\n"
    		+ "	  		\r\n"
    		+ "	  		    UNION ALL \r\n"
    		+ "	  		\r\n"
    		+ "	  		    SELECT \r\n"
    		+ "	  		        'Grand Total' AS zoneName,\r\n"
    		+ "	  		        '' AS circleName,\r\n"
    		+ "	  		         SUM(em.indicative_tax_value) AS indicativeTaxValue,\r\n"
    		+ "    		                COUNT(DISTINCT em.gstin) AS allottedCases,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 9 THEN em.gstin END) AS casesCompleted,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 1 THEN em.gstin END) AS notAcknowledged,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 2 THEN em.gstin END) AS transferToScrutiny,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 3 THEN em.gstin END) AS acknowledged,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 4 THEN em.gstin END) AS panchnama,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 5 THEN em.gstin END) AS preliminaryReport,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 6 THEN em.gstin END) AS finalReport,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 7 THEN em.gstin END) AS referToAdjudication,\r\n"
    		+ "    		                COUNT(DISTINCT CASE WHEN em.action_id = 8 THEN em.gstin END) AS showCauseIssued,\r\n"
    		+ "	  		        2 AS sortOrder,\r\n"
    		+ "	  		        'ZZZ' AS zoneSort\r\n"
    		+ "	  		     FROM analytics.enforcement_master em\r\n"
    		+ "    		            JOIN analytics.mst_location_mapping mlm ON em.case_location_location_id = mlm.circle_id\r\n"
    		+ "    		            WHERE ((',' || em.parameter || ',') LIKE CONCAT('%,', :parameterId, ',%') OR :parameterId = '-1')\r\n"
    		+ "    		              AND (em.period = :period OR :period = '-1')  \r\n"
    		+ "  		              and em.case_location_location_id in (:allMappedLocations)\r\n"
    		+ "	  		) AS final_report\r\n"
    		+ "	  		\r\n"
    		+ "	  		ORDER BY \r\n"
    		+ "	  		  zoneSort, \r\n"
    		+ "	  	  sortOrder, \r\n"
    		+ "	  	  circleName;", nativeQuery = true)
        List<Object[]> getDashboardSummaryByParamAndPeriod(@Param("parameterId") String parameterId,
                                                           @Param("period") String period,@Param("allMappedLocations") List<String> allMappedLocations);
    
        
        @Query(value = "select TO_CHAR(sum(em.indicative_tax_value), 'FM99,99,99,99,99,99,990') from analytics.enforcement_master em where em.case_location_location_id in (:allMappedLocations)", nativeQuery = true)
    	String findTotalSumOfIndicativeTaxValueAsLocations(@Param("allMappedLocations") List<String> allMappedLocations);
        
        @Query(value = "select count(em.*) from analytics.enforcement_master em where em.case_location_location_id in (:allMappedLocations)", nativeQuery = true)
    	Integer findTotalCountOfCasesAsLocations(@Param("allMappedLocations") List<String> allMappedLocations);
}
