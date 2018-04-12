package com.polus.fibicomp.proposal.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.dao.GrantCallDao;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.FundingSourceType;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalCategory;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalStatus;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Service(value = "proposalService")
public class ProposalServiceImpl implements ProposalService {

	protected static Logger logger = Logger.getLogger(ProposalServiceImpl.class.getName());

	@Autowired
	@Qualifier(value = "proposalDao")
	private ProposalDao proposalDao;

	@Autowired
	private CommitteeDao committeeDao;

	@Autowired
	private GrantCallDao grantCallDao;

	@Override
	public String createProposal(ProposalVO proposalVO) {
		Integer grantCallId = proposalVO.getGrantCallId();
		Proposal proposal = proposalVO.getProposal();
		if (grantCallId != null) {
			/*GrantCall grantCall = grantCallDao.fetchGrantCallById(grantCallId);
			proposal.setGrantCall(grantCall);
			proposal.setGrantCallId(grantCall.getGrantCallId());*/
		}

		List<GrantCall> grantCalls = proposalDao.fetchAllGrantCalls();
		proposalVO.setGrantCalls(grantCalls);

		ProposalStatus proposalStatus = proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE);
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE);
		proposal.setProposalStatus(proposalStatus);

		List<GrantCallType> grantCallTypes = grantCallDao.fetchAllGrantCallTypes();
		proposalVO.setGrantCallTypes(grantCallTypes);

		List<ProposalCategory> categories = proposalDao.fetchAllCategories();
		proposalVO.setProposalCategories(categories);

		List<ScienceKeyword> scienceKeywords = grantCallDao.fetchAllScienceKeywords();
		proposalVO.setScienceKeywords(scienceKeywords);

		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		proposalVO.setResearchAreas(researchAreas);

		List<ProposalResearchType> proposalResearchTypes = proposalDao.fetchAllProposalResearchTypes();
		proposalVO.setProposalResearchTypes(proposalResearchTypes);

		List<FundingSourceType> fundingSourceTypes = grantCallDao.fetchAllFundingSourceTypes();
		proposalVO.setFundingSourceTypes(fundingSourceTypes);

		List<Protocol> protocols = proposalDao.fetchAllProtocols();
		proposalVO.setProtocols(protocols);

		List<ProposalPersonRole> proposalPersonRoles = proposalDao.fetchAllProposalPersonRoles();
		proposalVO.setProposalPersonRoles(proposalPersonRoles);

		List<ProposalAttachmentType> proposalAttachmentTypes = proposalDao.fetchAllProposalAttachmentTypes();
		proposalVO.setProposalAttachmentTypes(proposalAttachmentTypes);

		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

}
