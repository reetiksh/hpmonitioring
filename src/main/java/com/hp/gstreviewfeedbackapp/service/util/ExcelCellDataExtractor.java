package com.hp.gstreviewfeedbackapp.service.util;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

@Component
public class ExcelCellDataExtractor {

	public static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}

		CellType cellType = cell.getCellType();

		if (cellType == CellType.STRING) {
			return cell.getStringCellValue();
		} else if (cellType == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				DataFormatter dataFormatter = new DataFormatter();
				return dataFormatter.formatCellValue(cell);
			} else {
				double numericValue = cell.getNumericCellValue();
				return String.valueOf(numericValue);
			}
		} else if (cellType == CellType.BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else {
			return "";
		}
	}
}
