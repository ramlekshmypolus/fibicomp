package com.polus.fibicomp.report.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.report.dao.ReportDao;
import com.polus.fibicomp.report.vo.ReportVO;

@Transactional
@Service(value = "reportService")
public class ReportServiceImpl implements ReportService {

	protected static Logger logger = Logger.getLogger(ReportServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "reportDao")
	private ReportDao reportDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Override
	public String applicationReport(ReportVO reportVO) {
		Integer grantCallId = reportVO.getGrantCallId();
		String reportName = reportVO.getReportName();
		Long proposalCount = 0L;
		if (reportName.equals("Submitted Application by Grant")) {
			proposalCount = reportDao.fetchApplicationCountByGrantCallId(grantCallId);
		} else if (reportName.equals("Awards by Grant")) {
			
		} else if (reportName.equals("Expenditure by Award")) {
			
		}
		reportVO.setProposalCount(proposalCount);
		String response = committeeDao.convertObjectToJSON(reportVO);
		return response;
	}

	@Override
	public String fetchOpenGrantIds(ReportVO reportVO) {
		List<GrantCall> grantIds = reportDao.fetchOpenGrantIds();
		reportVO.setGrantIds(grantIds);
		String response = committeeDao.convertObjectToJSON(reportVO);
		return response;
	}

}
