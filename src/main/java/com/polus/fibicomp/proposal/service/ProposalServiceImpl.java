package com.polus.fibicomp.proposal.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.dao.GrantCallDao;
import com.polus.fibicomp.proposal.dao.ProposalDao;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalAttachment;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@Transactional
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
			proposal.setGrantCall(grantCallDao.fetchGrantCallById(grantCallId));
			proposal.setGrantCallId(grantCallId);
		}
		proposalVO.setGrantCalls(proposalDao.fetchAllGrantCalls());
		proposal.setStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS);
		proposal.setProposalStatus(proposalDao.fetchStatusByStatusCode(Constants.PROPOSAL_STATUS_CODE_IN_PROGRESS));
		proposalVO.setGrantCallTypes(grantCallDao.fetchAllGrantCallTypes());
		proposalVO.setProposalCategories(proposalDao.fetchAllCategories());
		proposalVO.setScienceKeywords(grantCallDao.fetchAllScienceKeywords());
		proposalVO.setResearchAreas(committeeDao.fetchAllResearchAreas());
		proposalVO.setProposalResearchTypes(proposalDao.fetchAllProposalResearchTypes());
		proposalVO.setFundingSourceTypes(grantCallDao.fetchAllFundingSourceTypes());
		proposalVO.setProtocols(proposalDao.fetchAllProtocols());
		proposalVO.setProposalPersonRoles(proposalDao.fetchAllProposalPersonRoles());
		proposalVO.setProposalAttachmentTypes(proposalDao.fetchAllProposalAttachmentTypes());
		proposalVO.setProposalBudgetCategories(proposalDao.fetchAllBudgetCategories());

		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String addProposalAttachment(MultipartFile[] files, String formDataJSON) {
		ProposalVO proposalVO = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			proposalVO = mapper.readValue(formDataJSON, ProposalVO.class);
			Proposal proposal = proposalVO.getProposal();
			ProposalAttachment newAttachment = proposalVO.getNewAttachment();
			List<ProposalAttachment> proposalAttachments = new ArrayList<ProposalAttachment>();
			for (int i = 0; i < files.length; i++) {
				ProposalAttachment proposalAttachment = new ProposalAttachment();
				proposalAttachment.setAttachmentType(newAttachment.getAttachmentType());
				proposalAttachment.setAttachmentTypeCode(newAttachment.getAttachmentTypeCode());
				proposalAttachment.setDescription(newAttachment.getDescription());
				proposalAttachment.setUpdateTimeStamp(newAttachment.getUpdateTimeStamp());
				proposalAttachment.setUpdateUser(newAttachment.getUpdateUser());
				proposalAttachment.setAttachment(files[i].getBytes());
				proposalAttachment.setFileName(files[i].getOriginalFilename());
				proposalAttachment.setMimeType(files[i].getContentType());
				proposalAttachments.add(proposalAttachment);
			}
			proposal.getProposalAttachments().addAll(proposalAttachments);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String saveOrUpdateProposal(ProposalVO vo) {
		Proposal proposal = vo.getProposal();
		proposal = proposalDao.saveOrUpdateProposal(proposal);
		vo.setStatus(true);
		String updateType = vo.getUpdateType();
		if (updateType != null && updateType.equals("SAVE")) {
			vo.setMessage("Proposal saved successfully");
		} else {
			vo.setMessage("Proposal updated successfully");
		}
		vo.setProposal(proposal);
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

	@Override
	public String loadProposalById(Integer proposalId) {
		ProposalVO proposalVO = new ProposalVO();
		Proposal proposal = proposalDao.fetchProposalById(proposalId);
		proposalVO.setProposal(proposal);
		proposalVO.setGrantCalls(proposalDao.fetchAllGrantCalls());
		proposalVO.setGrantCallTypes(grantCallDao.fetchAllGrantCallTypes());
		proposalVO.setProposalCategories(proposalDao.fetchAllCategories());
		proposalVO.setScienceKeywords(grantCallDao.fetchAllScienceKeywords());
		proposalVO.setResearchAreas(committeeDao.fetchAllResearchAreas());
		proposalVO.setProposalResearchTypes(proposalDao.fetchAllProposalResearchTypes());
		proposalVO.setFundingSourceTypes(grantCallDao.fetchAllFundingSourceTypes());
		proposalVO.setProtocols(proposalDao.fetchAllProtocols());
		proposalVO.setProposalPersonRoles(proposalDao.fetchAllProposalPersonRoles());
		proposalVO.setProposalAttachmentTypes(proposalDao.fetchAllProposalAttachmentTypes());
		proposalVO.setProposalBudgetCategories(proposalDao.fetchAllBudgetCategories());

		String response = committeeDao.convertObjectToJSON(proposalVO);
		return response;
	}

	@Override
	public String fetchCostElementByBudgetCategory(ProposalVO vo) {
		vo.setProposalCostElements(proposalDao.fetchCostElementByBudgetCategory(vo.getBudgetCategoryCode()));
		String response = committeeDao.convertObjectToJSON(vo);
		return response;
	}

}
