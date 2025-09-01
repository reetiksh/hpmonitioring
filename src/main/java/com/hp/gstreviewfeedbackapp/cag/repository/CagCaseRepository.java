package com.hp.gstreviewfeedbackapp.cag.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hp.gstreviewfeedbackapp.audit.model.AuditMaster;
import com.hp.gstreviewfeedbackapp.cag.model.CagCompositeKey;
import com.hp.gstreviewfeedbackapp.cag.model.MstCagCases;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;

public interface CagCaseRepository  extends JpaRepository<MstCagCases, CagCompositeKey>{

	@Query(value = "select * from analytics.cag_master_cases e where e.working_location IN (?1) and action IN (?2);", nativeQuery = true)
	List<MstCagCases> findByLocationDetail(List<String> locationList, List<String> actionList);
	
	@Query(value = "select erc.* from analytics.cag_master_cases erc, analytics.cag_case_pertain_to_users ercau \r\n"
			+ "where erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "and erc.parameter = ercau.parameter and erc.action = ?1 and erc.assigned_to_user_id in (?2);", nativeQuery = true)
	List<MstCagCases> findByCategoryAndActionAndUserIdList(String acknowledge, List<Integer> userIdList);
	
	
	@Query(value = "select erc.* from analytics.cag_master_cases erc, analytics.cag_case_pertain_to_users ercau \r\n"
			+ "where erc.gstin = ercau.gstin and erc.case_reporting_date = ercau.case_reporting_date and erc.period = ercau.period\r\n"
			+ "and erc.parameter = ercau.parameter and erc.action = ?1 and ercau.hq_user_id in (?2);", nativeQuery = true)
	List<MstCagCases> findTransferList(String acknowledge, Integer userIdList);
	
	
	@Query(value = "select cmc.parameter, count(*), sum(cmc.indicative_tax_value) from analytics.cag_master_cases cmc \r\n"
			+ "where cmc.working_location in (?1) group by cmc.parameter", nativeQuery = true)
	List<Object[]> getCagParameterDetails(List<String> locationList);
	
	
	@Query(value = "select cc.name, cmc.*, mdld.location_name, TO_CHAR(cmc.case_reporting_date, 'YYYY-MM-DD') AS "
			+ "formatted_date from analytics.cag_master_cases cmc left join \r\n"
			+ "analytics.cag_category cc on cmc.action_status = cc.id \r\n"
			+ "left join analytics.mst_dd_location_details mdld \r\n"
			+ "on mdld.location_id = cmc.working_location "
			+ " where cmc.working_location in (?1) and cmc.parameter = ?2", nativeQuery = true)
	List<Object[]> getCagCaseDetails(List<String> locations, String parameter);
	
	
	@Query(value = "select mdld.location_name, ca.name as category, cs.name as case_stage, rs.name as recovery_stage,\r\n"
			+ "msc.amount_recovered, msc.assigned_from, msc.assigned_to, COALESCE(msc.demand, 0) as demand, COALESCE(msc.indicative_tax_value, 0) as indicative_tax_value,\r\n"
			+ "COALESCE(msc.recovery_against_demand, 0) as recovery_against_demand,COALESCE(msc.recovery_bydrc03, 0) as recovery_bydrc03, msc.taxpayer_name, msc.case_id \r\n"
			+ "from analytics.mst_scrutiny_cases msc left join\r\n"
			+ "analytics.mst_dd_hq_category ca on ca.id = msc.category \r\n"
			+ "left join analytics.mst_dd_fo_case_stage cs on cs.id = msc.case_stage \r\n"
			+ "left join analytics.scrutiny_recovery_stage rs on rs.id = msc.recovery_stage \r\n"
			+ "left join analytics.scrutiny_extension_no_documents ext on ext.id = msc.extension_file_name_id\r\n"
			+ "left join analytics.mst_dd_location_details mdld on mdld.location_id = msc.working_location \r\n"
			+ "where msc.gstin =?1 and msc.case_reporting_date =?2 and msc.period =?3 ", nativeQuery = true)
	List<Object[]> getScrutinyCaseDetails(String gstn, Date date, String period);
	
	
	@Query(value = "select mdld.location_name, erc.category as category, cs.name as case_stage, rs.name as recovery_stage,  \r\n"
			+ "erc.amount_recovered, erc.assigned_from, erc.assigned_to,COALESCE(erc.demand, 0) as demand,COALESCE(erc.indicative_tax_value, 0) as indicative_tax_value,\r\n"
			+ "COALESCE(erc.recovery_against_demand, 0) as recovery_against_demand,COALESCE(erc.recovery_by_drc3, 0) as recovery_by_drc3, erc.taxpayer_name, erc.case_id, acs.name \r\n"
			+ "from analytics.enforcement_review_case erc\r\n"
			+ "left join analytics.mst_dd_fo_case_stage cs on cs.id = erc.case_stage\r\n"
			+ "left join analytics.mst_dd_fo_recovery_stage rs on rs.id = erc.recovery_stage \r\n"
			+ "left join analytics.extension_no_documents ext on ext.id = erc.extension_file_name_id\r\n"
			+ "left join analytics.mst_dd_location_details mdld on mdld.location_id = erc.working_location \r\n"
			+ "left join analytics.mst_dd_fo_action_status acs on acs.id = erc.action_status \r\n"
			+ "where erc.gstin =?1 and erc.case_reporting_date =?2 and erc.period =?3 ", nativeQuery = true)
	List<Object[]> getAssessmentCaseDetails(String gstn, Date date, String period);
	
	
	@Query(value = "select mdld.location_name, cs.name as category,\r\n"
			+ "am.assigned_from, am.assign_to, COALESCE(am.total_involved_amount, 0) as total_involved_amount,\r\n"
			+ "am.fully_recovered, am.taxpayer_name, am.case_id \r\n"
			+ "from analytics.audit_master am\r\n"
			+ "left join analytics.mst_dd_hq_category cs on cs.id = am.category \r\n"
			+ "left join analytics.audit_extension_no_documents ext on ext.id = am.extension_file_name_id\r\n"
			+ "left join analytics.mst_dd_location_details mdld on mdld.location_id = am.working_location \r\n"
			+ "left join analytics.audit_case_status acs on acs.id = am.action  \r\n"
			+ "where am.gstin = ?1 and am.case_reporting_date = ?2 \r\n"
			+ "and am.period = ?3 and am.case_id = ?4 ", nativeQuery = true)
	List<Object[]> getAuditCaseDetails(String gstn, Date date, String period, String caseId);
	
	
	
	
}
