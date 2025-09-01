package com.hp.gstreviewfeedbackapp.service.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;
import com.hp.gstreviewfeedbackapp.model.UserDetails;
import com.hp.gstreviewfeedbackapp.model.UserRole;
import com.hp.gstreviewfeedbackapp.model.UserRoleMapping;
import com.hp.gstreviewfeedbackapp.repository.MstParametersModuleWiseRepository;
import com.hp.gstreviewfeedbackapp.repository.UserRoleMappingRepository;

@Component
public class CustomUtility {
	private static final Logger logger = LoggerFactory.getLogger(CustomUtility.class);

	@Autowired
	private UserRoleMappingRepository userRoleMappingRepository;
	
	@Autowired
	private MstParametersModuleWiseRepository mstParametersRepository;

	public List<String> roleMapWithLocations(List<UserRoleMapping> roleMapDatList, UserDetails userDetails) {
//		logger.info("CustomUtility : roleMapWithLocations() : start");
		List<String> roleMapLocationsList = new ArrayList<>();
		List<UserRole> distinctRoleIds = roleMapDatList.stream().map(roleMapping -> roleMapping.getUserRole())
				.distinct().collect(Collectors.toList());

		for (Integer i = 0; i < distinctRoleIds.size(); i++) {
			List<UserRoleMapping> userRoleMappings = userRoleMappingRepository
					.findAllByUserDetailsAndUserRole(userDetails, distinctRoleIds.get(i));
			String locationsNameRoleWise = returnLocationsName(userRoleMappings);
			roleMapLocationsList.add(distinctRoleIds.get(i).getRoleName() + " : " + locationsNameRoleWise);
		}

//		logger.info("CustomUtility : roleMapWithLocations() : end");
		return roleMapLocationsList;
	}

	/**************** return locations name start ******************/

	private String returnLocationsName(List<UserRoleMapping> userRoleMapList) {
		List<String> returnResultentSet = new ArrayList<>();
		List<UserRoleMapping> stateId = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getStateDetails().getStateId())).collect(Collectors.toList());

		List<UserRoleMapping> zoneIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getZoneDetails().getZoneId())).collect(Collectors.toList());

		List<UserRoleMapping> circleIds = userRoleMapList.stream()
				.filter(mapping -> !"NA".equals(mapping.getCircleDetails().getCircleId())).collect(Collectors.toList());

		if (!stateId.isEmpty()) {
			returnResultentSet.add(stateId.get(0).getStateDetails().getStateName());
		}

		if (!zoneIds.isEmpty()) {
			for (UserRoleMapping zoneIdsIdsSolo : zoneIds) {
				returnResultentSet.add(zoneIdsIdsSolo.getZoneDetails().getZoneName());
			}
		}

		if (!circleIds.isEmpty()) {
			for (UserRoleMapping circleIdsSolo : circleIds) {
				returnResultentSet.add(circleIdsSolo.getCircleDetails().getCircleName());
			}
		}

		String commaSeperatedLocationsNameList = returnResultentSet.stream().collect(Collectors.joining(", "));

		return commaSeperatedLocationsNameList;
	}

	/**************** return locations name end ******************/
	
	
	public byte[] createApproveRejectExcelFile(List<EnforcementReviewCase> dataList) throws IOException {
		 // Create a new workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {
                "GSTIN", "Taxpayer Name", "Location", "Period", "Case Reporting Date", 
                "Indicative Tax Value", "Category", "Case ID", "Case Stage", "Case Stage ARN",
                "Demand", "Recovery Stage", "Recovery Stage ARN", "Recovery by DRC3", 
                "Recovery Against Demand","Parameter"
                //, "Remark"
        };

        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
        }
		
     // Populate the data rows
        int rowNum = 1;
        for (EnforcementReviewCase caseData : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(caseData.getId().getGSTIN());
            row.createCell(1).setCellValue(caseData.getTaxpayerName());
            row.createCell(2).setCellValue(caseData.getLocationDetails().getLocationName());
            row.createCell(3).setCellValue(caseData.getId().getPeriod());

            // Format the case reporting date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                row.createCell(4).setCellValue(sdf.format(sdf.parse(caseData.getId().getCaseReportingDate().toString())));
            } catch (Exception e) {
                row.createCell(4).setCellValue(caseData.getId().getCaseReportingDate().toString());
            }

            row.createCell(5).setCellValue(caseData.getIndicativeTaxValue());
            row.createCell(6).setCellValue(caseData.getCategory());
            row.createCell(7).setCellValue(caseData.getCaseId());
            row.createCell(8).setCellValue(caseData.getCaseStage().getName());
            row.createCell(9).setCellValue(caseData.getCaseStageArn());
            row.createCell(10).setCellValue(caseData.getDemand() != null ? caseData.getDemand() : 0);
            row.createCell(11).setCellValue(caseData.getRecoveryStage().getName());
            row.createCell(12).setCellValue(caseData.getRecoveryStageArn());
            row.createCell(13).setCellValue(caseData.getRecoveryByDRC3() != null ? caseData.getRecoveryByDRC3() : 0);
            row.createCell(14).setCellValue(caseData.getRecoveryAgainstDemand() != null ? caseData.getRecoveryAgainstDemand() : 0);
            row.createCell(15).setCellValue(getParameterName(caseData.getParameter()));
     //       row.createCell(16).setCellValue("testing");
        }

        // Write the workbook to a byte array output stream
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }
	
	public String getParameterName(String parameterId) {
		if (parameterId == null || parameterId.length() == 0) {
			return "Not Available";
		}
		String parameter = parameterId.replaceAll("^,|,$", "");

		String[] parameterList = parameter.split(",");

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < parameterList.length; i++) {

			String parameterName = mstParametersRepository.findById(Integer.parseInt(parameterList[i])).get()
					.getParamName();

			stringBuilder.append(parameterName);

			if (i != parameterList.length - 1) {
				stringBuilder.append(",");
			}

		}

		return stringBuilder.toString();
	}
}
