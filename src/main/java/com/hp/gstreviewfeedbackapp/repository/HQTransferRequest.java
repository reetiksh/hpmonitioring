package com.hp.gstreviewfeedbackapp.repository;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hp.gstreviewfeedbackapp.controller.HQUserController;
import com.hp.gstreviewfeedbackapp.data.HqTransfer;
import com.hp.gstreviewfeedbackapp.model.EnforcementReviewCase;

@Repository
public class HQTransferRequest {
	private final JdbcTemplate jdbcTemplate;
	private List<Map<String, Object>> queryList = null;
	private static final DecimalFormat decfor = new DecimalFormat("0.00");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger logger = LoggerFactory.getLogger(HQUserController.class);

	@Autowired
	private LocationDetailsRepository locationDetailsRepository;

	public HQTransferRequest(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<HqTransfer> getHqTransferList(int userId) {
		try {
			String sql = "select cw1.gstin, cw1.case_reporting_date, erc1.category, cw1.period, erc1.indicative_tax_value, erc1.taxpayer_name, cw1.assigned_from_location_id, cw1.suggested_jurisdiction, tr.name as remark, cw1.other_remarks, cw1.updating_date, cw1.transfer_file_path\r\n"
					+ "from\r\n"
					+ "(select cw.gstin, cw.case_reporting_date, cw.period, max(cw.updating_date) as updating_date from\r\n"
					+ "(select erc.gstin, erc.case_reporting_date, erc.period from analytics.enforcement_review_case erc, analytics.enforcement_review_case_assigned_users ercau  \r\n"
					+ "where erc.assigned_to = 'HQ' and erc.\"action\" = 'transfer' and ercau.gstin = erc.gstin and ercau.case_reporting_date = erc.case_reporting_date and ercau.\"period\" = erc.\"period\"  and ercau.hq_user_id in ('"
					+ userId + "', '0')) table1, \r\n" + "analytics.case_workflow cw \r\n"
					+ "where cw.gstin=table1.gstin and cw.case_reporting_date= table1.case_reporting_date and cw.period=table1.period \r\n"
					+ "group by cw.gstin, cw.case_reporting_date, cw.period) table2, analytics.case_workflow cw1, analytics.enforcement_review_case erc1, analytics.mst_remarks_fo_transfer tr\r\n"
					+ "where table2.gstin = cw1.gstin and table2.case_reporting_date = cw1.case_reporting_date and table2.period = cw1.period and table2.updating_date = cw1.updating_date and\r\n"
					+ "table2.gstin = erc1.gstin and table2.case_reporting_date = erc1.case_reporting_date and table2.period = erc1.period and cw1.remarks = tr.id;";

			queryList = jdbcTemplate.queryForList(sql);

			if (queryList != null && queryList.size() > 0) {
				List<HqTransfer> objectList = new ArrayList<>();

				for (Map<String, Object> row : queryList) {
					HqTransfer hqTransfer = new HqTransfer();
					hqTransfer.setGSTIN(row.get("gstin").toString());
					hqTransfer.setCaseReportingDate(dateFormat.parse(row.get("case_reporting_date").toString()));
					hqTransfer.setPeriod(row.get("period").toString());
					hqTransfer.setCategory(row.get("category").toString());
//				hqTransfer.setIndicativeTaxValue(Double.parseDouble(decfor.format(Double.parseDouble(row.get("indicative_tax_value").toString()))));
					hqTransfer.setIndicativeTaxValue(
							Math.round(Double.parseDouble(row.get("indicative_tax_value").toString())));
					hqTransfer.setTaxpayerName(row.get("taxpayer_name").toString());
					hqTransfer.setAssignedFromLocationId(row.get("assigned_from_location_id").toString());
					hqTransfer.setAssignedFromLocationName(
							row.get("assigned_from_location_id") != null ? locationDetailsRepository
									.findById(row.get("assigned_from_location_id").toString()).get().getLocationName()
									: "NA");
					hqTransfer.setRemark((row.get("other_remarks") != null
							&& row.get("other_remarks").toString().trim().length() > 0)
									? row.get("other_remarks").toString()
									: row.get("remark").toString());
					hqTransfer.setSuggestedJurisdictionName((row.get("suggested_jurisdiction") != null
							&& row.get("suggested_jurisdiction").toString().trim().length() != 0)
									? locationDetailsRepository.findById(row.get("suggested_jurisdiction").toString())
											.get().getLocationName()
									: "Not Available");
					hqTransfer.setSuggestedJurisdictionId((row.get("suggested_jurisdiction") != null
							&& row.get("suggested_jurisdiction").toString().trim().length() != 0)
									? row.get("suggested_jurisdiction").toString()
									: "NA");
					hqTransfer.setUpdatingDate(dateFormat
							.parse(row.get("updating_date") != null ? row.get("updating_date").toString() : null));
					if (hqTransfer.getSuggestedJurisdictionId().equals("NC")) {
						hqTransfer.setTransferFilePath(
								row.get("transfer_file_path") != null ? row.get("transfer_file_path").toString()
										: null);
					}

					objectList.add(hqTransfer);
				}
				return objectList;

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HQTransferRequest : getHqTransferList : " + e.getMessage());
		}
		return null;
	}

	public Map<String, String> getSuggestedJurisdiction(EnforcementReviewCase enCase) {
		Map<String, String> map = null;
		String sql = "select cw.suggested_jurisdiction, remarktable.name as remark, cw.other_remarks from \r\n"
				+ "(select cw.gstin, cw.case_reporting_date, cw.period, max(cw.updating_date) as updating_date from \r\n"
				+ "analytics.case_workflow cw where cw.gstin = '" + enCase.getId().getGSTIN()
				+ "' and cw.\"period\" = '" + enCase.getId().getPeriod() + "' and cw.case_reporting_date = '"
				+ enCase.getId().getCaseReportingDate() + "' and cw.\"action\" = 'transfer'\r\n"
				+ "group by cw.gstin, cw.case_reporting_date, cw.period) table1, analytics.case_workflow cw, analytics.mst_remarks_fo_transfer remarktable\r\n"
				+ "where cw.gstin=table1.gstin and cw.case_reporting_date= table1.case_reporting_date and cw.period=table1.period and cw.updating_date = table1.updating_date and cw.remarks = remarktable.id\r\n"
				+ "group by cw.suggested_jurisdiction, cw.remarks, cw.other_remarks, remarktable.name";

		queryList = jdbcTemplate.queryForList(sql);
		if (queryList != null && queryList.size() > 0) {
			map = new HashMap<>();
			Map<String, Object> row = queryList.get(0);

			map.put("suggested_jurisdiction",
					row.get("suggested_jurisdiction") != null ? row.get("suggested_jurisdiction").toString() : "NA");
			map.put("remark",
					((row.get("other_remarks") != null && row.get("other_remarks").toString().trim().length() > 0)
							? row.get("other_remarks").toString()
							: row.get("remark").toString()));
		}
		return map;
	}

}
