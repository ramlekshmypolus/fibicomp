package com.polus.fibicomp.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.grantcall.dao.GrantCallDao;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.ProtocolType;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.report.dao.ReportDao;
import com.polus.fibicomp.report.vo.ReportVO;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.ProtocolView;

@Transactional
@Service(value = "reportService")
public class ReportServiceImpl implements ReportService {

	protected static Logger logger = Logger.getLogger(ReportServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "reportDao")
	private ReportDao reportDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private GrantCallDao grantCallDao;

	@Override
	public String applicationReport(ReportVO reportVO) {
		String reportName = reportVO.getReportName();
		logger.info("reportName : " + reportName);
		if (reportName.equals("Submitted Applications by Grant Call")) {
			reportVO = reportDao.fetchApplicationByGrantCallId(reportVO);
		} else if (reportName.equals("Projects by Grant Call")) {
			reportVO = reportDao.fetchAwardByGrantCallId(reportVO);
		} else if (reportName.equals("Expenditure by Project")) {
			reportVO = reportDao.fetchExpenditureByAward(reportVO);
		}
		String response = committeeDao.convertObjectToJSON(reportVO);
		return response;
	}

	public void fetchOpenGrantIds(ReportVO reportVO) {
		List<GrantCall> grantIds = reportDao.fetchOpenGrantIds();
		reportVO.setGrantIds(grantIds);
	}

	public void fetchApplicationsCountByGrantCallType(ReportVO reportVO) {
		List<GrantCallType> grantCallTypes = grantCallDao.fetchAllGrantCallTypes();
		if (grantCallTypes != null && !grantCallTypes.isEmpty()) {
			Map<String, List<Proposal>> applicationsByGrantCallType = new HashMap<String, List<Proposal>>();
			for (GrantCallType grantCallType : grantCallTypes) {
				String grantCallTypeDesc = grantCallType.getDescription();
				List<Proposal> proposals = reportDao.fetchApplicationsByGrantCallType(grantCallType.getGrantTypeCode());
				logger.info("Grant Call Type : " + grantCallTypeDesc + " ---------- Applications : " + proposals);
				applicationsByGrantCallType.put(grantCallTypeDesc, proposals);
			}
			reportVO.setApplicationsByGrantCallType(applicationsByGrantCallType);
		}
	}

	public void fetchProtocolsCountByProtocolType(ReportVO reportVO) {
		List<ProtocolType> protocolTypes = reportDao.fetchAllProtocolTypes();
		if (protocolTypes != null && !protocolTypes.isEmpty()) {
			Map<String, List<ProtocolView>> protocolsByType = new HashMap<String, List<ProtocolView>>();
			for (ProtocolType protocolType : protocolTypes) {
				String protocolTypeDesc = protocolType.getDescription();
				List<ProtocolView> protocols = reportDao.fetchProtocolsByProtocolType(protocolType.getProtocolTypeCode());
				logger.info("Protocol Type : " + protocolTypeDesc + " ---------- Protocols : " + protocols);
				protocolsByType.put(protocolTypeDesc, protocols);
			}
			reportVO.setProtocolsByType(protocolsByType);
		}
	}

	@Override
	public String fetchReportData(ReportVO reportVO) {
		fetchOpenGrantIds(reportVO);
		fetchApplicationsCountByGrantCallType(reportVO);
		fetchProtocolsCountByProtocolType(reportVO);
		fetchAwardByGrantCallType(reportVO);
		fetchAwardNumbers(reportVO);
		String response = committeeDao.convertObjectToJSON(reportVO);
		return response;
	}

	public void fetchAwardByGrantCallType(ReportVO reportVO) {
		List<GrantCallType> grantCallTypes = reportDao.fetchAllGrantCallTypes();
		if (grantCallTypes != null && !grantCallTypes.isEmpty()) {
			Map<String, Long> awardByGrantType = new HashMap<String, Long>();
			for (GrantCallType grantCallType : grantCallTypes) {
				String grantCallTypeDesc = grantCallType.getDescription();
				List<Integer> proposalId = reportDao.fetchProposalIdByGrantTypeCode(grantCallType.getGrantTypeCode());
				if (proposalId != null && !proposalId.isEmpty()) {
					Long count = reportDao.fetchAwardCountByGrantType(proposalId);
					logger.info("GrantCallType : " + grantCallTypeDesc + " ---------- count : " + count);
					awardByGrantType.put(grantCallTypeDesc, count);
				}
			}
			reportVO.setAwardByGrantType(awardByGrantType);
		}
	}

	public void fetchAwardNumbers(ReportVO reportVO) {
		List<AwardView> awardNumbers = reportDao.fetchAwardNumbers();
		reportVO.setAwardNumbers(awardNumbers);
	}

}
