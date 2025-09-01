package com.hp.gstreviewfeedbackapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.gstreviewfeedbackapp.audit.model.AnnexureReportRow;
import com.hp.gstreviewfeedbackapp.audit.repository.AuditMasterRepository;
import com.hp.gstreviewfeedbackapp.model.SubAppMenu;
import com.hp.gstreviewfeedbackapp.repository.EnforcementReviewCaseRepository;
import com.hp.gstreviewfeedbackapp.repository.SubAppMenuRepository;
import com.hp.gstreviewfeedbackapp.service.ViewService;

@Service
public class ViewServiceImpl implements ViewService {
	@Autowired
	private EnforcementReviewCaseRepository enforcementReviewCaseRepository;

	@Autowired
	private AuditMasterRepository auditMasterRepository;
	
	@Override
	public String getDashBoardTotalCases(List<String> workingLocation) {
		String dashBoardTotalCases = enforcementReviewCaseRepository.getViewWiseDashBoardTotalCases(workingLocation);
		return dashBoardTotalCases;
	}

	@Override
	public String getDashBoardTotalAcknowledgedCases(List<String> workingLocation) {
		String dashBoardTotalAcknowledgedCases = enforcementReviewCaseRepository
				.getViewWiseDashBoardTotalAcknowledgedCases(workingLocation);
		return dashBoardTotalAcknowledgedCases;
	}

	@Override
	public String getDashBoardTotalInitiatedCases(List<String> workingLocation) {
		String dashBoardTotalInitiatedCases = enforcementReviewCaseRepository
				.getViewWiseDashBoardTotalInitiatedCases(workingLocation);
		return dashBoardTotalInitiatedCases;
	}

	@Override
	public String getDashBoardTotalCasesClosedByFo(List<String> workingLocation) {
		String dashBoardTotalCasesClosedByFo = enforcementReviewCaseRepository
				.getViewWiseDashBoardTotalCasesClosedByFo(workingLocation);
		return dashBoardTotalCasesClosedByFo;
	}

	@Override
	public Long getDashBoardTotalSuspectedIndicativeAmount(List<String> workingLocation) {
		Long dashBoardTotalSuspectedIndicativeAmount = enforcementReviewCaseRepository
				.getViewWiseDashBoardTotalSuspectedIndicativeAmount(workingLocation);
		return dashBoardTotalSuspectedIndicativeAmount;
	}

	@Override
	public Long getDashBoardTotalAmount(List<String> workingLocation) {
		Long dashBoardTotalAmount = enforcementReviewCaseRepository.getViewWiseDashBoardTotalAmount(workingLocation);
		return dashBoardTotalAmount;
	}

	@Override

	public Long getDashBoardTotalDemand(List<String> workingLocation) {
		Long dashBoardTotalDemand = enforcementReviewCaseRepository.getViewWiseDashBoardTotalDemand(workingLocation);
		return dashBoardTotalDemand;
	}

	@Override
	public Long getDashBoardTotalRecovery(List<String> workingLocation) {
		Long dashBoardTotalRecovery = enforcementReviewCaseRepository
				.getViewWiseDashBoardTotalRecovery(workingLocation);
		return dashBoardTotalRecovery;
	}

	public List<List<String>> getTotalIndicativeTaxValueCategoryAndViewWiseList(List<String> locationDistId) {
		List<List<String>> array = null;
		try {
			if (locationDistId != null && locationDistId.size() > 0) {
				// List<String> locationIds =
				// locationDistId.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
				List<Object[]> objectList = enforcementReviewCaseRepository
						.getTotalIndicativeTaxValueCategoryWise(locationDistId);

				if (objectList != null && objectList.size() > 0) {
					array = new ArrayList<>();
					List<String> infoType = new ArrayList<>();
					infoType.add("'Category'");
					infoType.add("'Amount'");
					array.add(infoType);

					for (Object[] objArray : objectList) {
						List<String> data = new ArrayList<>();

						data.add("'" + (objArray[0] != null ? objArray[0].toString() : "NA") + "'");
						data.add((objArray[1] != null ? objArray[1].toString() : "0"));

						array.add(data);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	public List<List<String>> getTotalDemandCategoryAndViewWiseList(List<String> locationDistId) {
		List<List<String>> array = null;
		try {
			if (locationDistId != null && locationDistId.size() > 0) {
				// List<String> locationIds =
				// locationMap.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
				List<Object[]> objectList = enforcementReviewCaseRepository
						.getTotalDemandCategoryAndViewWise(locationDistId);

				if (objectList != null && objectList.size() > 0) {
					array = new ArrayList<>();
					List<String> infoType = new ArrayList<>();
					infoType.add("'Category'");
					infoType.add("'Amount'");
					array.add(infoType);

					for (Object[] objArray : objectList) {
						List<String> data = new ArrayList<>();

						data.add("'" + (objArray[0] != null ? objArray[0].toString() : "NA") + "'");
						data.add((objArray[1] != null ? objArray[1].toString() : "0"));

						array.add(data);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	public List<List<String>> getTotalRecoveryCategoryAndViewWiseList(List<String> locationIds) {
		List<List<String>> array = null;
		try {
			if (locationIds != null && locationIds.size() > 0) {
				// List<String> locationIds =
				// locationMap.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
				List<Object[]> objectList = enforcementReviewCaseRepository
						.getTotalRecoveryCategoryAndViewWise(locationIds);

				if (objectList != null && objectList.size() > 0) {
					array = new ArrayList<>();
					List<String> infoType = new ArrayList<>();
					infoType.add("'Category'");
					infoType.add("'Amount'");
					array.add(infoType);

					for (Object[] objArray : objectList) {
						List<String> data = new ArrayList<>();

						data.add("'" + (objArray[0] != null ? objArray[0].toString() : "NA") + "'");
						data.add((objArray[1] != null ? objArray[1].toString() : "0"));

						array.add(data);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}
	

	public List<AnnexureReportRow> getAnnexureReport(List<String> allMappedLocations) {

        List<Object[]> rawData = auditMasterRepository.getAnnexureReportRaw(allMappedLocations);
        List<AnnexureReportRow> result = new ArrayList<>();

        for (Object[] row : rawData) {
            AnnexureReportRow dto = new AnnexureReportRow();
            dto.setZoneName((String) row[0]);
            dto.setCircleName((String) row[1]);
            dto.setAllottedCases(((Number) row[2]).intValue());
            dto.setAuditCasesCompleted(((Number) row[3]).intValue());
            dto.setPendingDeskReview(((Number) row[4]).intValue());
            dto.setPendingApprovalAuditPlan(((Number) row[5]).intValue());
            dto.setPendingExaminationBooks(((Number) row[6]).intValue());
            dto.setPendingDAR(((Number) row[7]).intValue());
            dto.setPendingFAR(((Number) row[8]).intValue());
            result.add(dto);
        }
        return result;
    
	}
	
}
