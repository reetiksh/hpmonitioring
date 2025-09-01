package com.hp.gstreviewfeedbackapp.scrutiny.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.model.CompositeKey;
import com.hp.gstreviewfeedbackapp.scrutiny.model.MstScrutinyCases;

@Repository
public interface MstScrutinyCasesRepository extends JpaRepository<MstScrutinyCases, CompositeKey> {

	@Query(value = "select * from analytics.mst_scrutiny_cases msc where msc.assigned_from='SHQ' and msc.assigned_to='SFO' and msc.\"action\" in(?1) and msc.working_location in(?2)", nativeQuery = true)
	List<MstScrutinyCases> getScrutinyCases(List<String> actionStatusList, List<String> workingLoacationList);

	@Query(value = "select * from analytics.mst_scrutiny_cases msc where msc.assigned_from in('SHQ','SRU') and msc.assigned_to='SFO' and msc.\"action\"  in('acknowledgeScrutinyCase','recommendForScrutiny') and msc.working_location in(?1)", nativeQuery = true)
	List<MstScrutinyCases> getInitiatedScrutinyCases(List<String> workingLoacationList);

	@Query(value = "select * from analytics.mst_scrutiny_cases msc where msc.assigned_from in('SHQ','SRU') and msc.assigned_to='SFO' and msc.\"action\"  = 'asmtTenIssued' and msc.asmt_ten_issued_or_not = true and msc.working_location in(?1)", nativeQuery = true)
	List<MstScrutinyCases> getScrutinyUpdateStatusCases(List<String> workingLoacationList);

	@Query(value = "select * from analytics.mst_scrutiny_cases msc where msc.assigned_from = 'SFO' and msc.assigned_to ='SHQ' and msc.\"action\" ='requestForTransfer'", nativeQuery = true)
	List<MstScrutinyCases> getRequestForTransferList();

	@Query(value="select * from analytics.mst_scrutiny_cases msc where msc.\"action\" in('noNeedScrutiny','closureReportDropped') and msc.indicative_tax_value > 10000000 and msc.working_location in(?1)",nativeQuery=true)
	List<MstScrutinyCases> getMandatoryCasesList(List<String> workingLocationList);

	@Query(value="select * from analytics.mst_scrutiny_cases msc where msc.\"action\" in('noNeedScrutiny','closureReportDropped') and msc.indicative_tax_value <= 10000000 and msc.working_location in(?1)",nativeQuery=true)
	List<MstScrutinyCases> getRandomCasesList(List<String> workingLocationList);
	
	@Query(value="select * from analytics.mst_scrutiny_cases msc where msc.\"action\" = 'noNeedScrutiny' and msc.indicative_tax_value <= 10000000 and msc.working_location in(?1)",nativeQuery=true)
	List<MstScrutinyCases> getRandomCasesListExceptClosureReport(List<String> workingLocationList);
	
	@Query(value="select * from analytics.mst_scrutiny_cases msc where msc.\"action\" ='hqRecommendForScrutiny' and msc.assigned_from='SHQ' and msc.assigned_to ='SRU' and msc.working_location in(?1)",nativeQuery=true)
	List<MstScrutinyCases> getRecommendedFromHqUserCasesList(List<String> workingLocationList);

	@Query(value = "select * from analytics.mst_scrutiny_cases msc where msc.working_location in(?1)", nativeQuery = true)
	List<MstScrutinyCases> getAllScrutinyCasesList(List<String> workingLocationList);

	@Query(value = "select * from analytics.mst_scrutiny_cases msc where msc.\"action\"=?1 and msc.working_location in(?2)", nativeQuery = true)
	List<MstScrutinyCases> getAllScrutinyCasesListByAction(String action, List<String> workingLocationList);
	
	@Query(value="select * from analytics.mst_scrutiny_cases mst \r\n"
			+ "where mst.gstin =?1 and mst.period =?2 \r\n"
			+ "and mst.parameters like CONCAT('%', ?3, '%') and action != 'recommendedForAssesAndAdjudication' ",nativeQuery=true)
	List<MstScrutinyCases> findScrutinyCase(String gstin, String period, String parameters);
	
	@Query(value = "select count(msc.case_id) from analytics.mst_scrutiny_cases msc where upper(msc.case_id) = upper(?1)", nativeQuery = true)
	Integer findTotalCountOfCaseIdMatches(String caseId);

}
