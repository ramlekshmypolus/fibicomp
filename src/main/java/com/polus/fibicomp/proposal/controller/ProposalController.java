package com.polus.fibicomp.proposal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.proposal.service.ProposalService;
import com.polus.fibicomp.proposal.vo.ProposalVO;

@RestController
public class ProposalController {

	protected static Logger logger = Logger.getLogger(ProposalController.class.getName());

	@Autowired
	@Qualifier(value = "proposalService")
	private ProposalService proposalService;

	@RequestMapping(value = "/createProposal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String createProposal(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createProposal");
		return proposalService.createProposal(vo);
	}

	@RequestMapping(value = "/addProposalAttachment", method = RequestMethod.POST)
	public String addProposalAttachment(@RequestParam(value = "files", required = false) MultipartFile[] files, @RequestParam("formDataJson") String formDataJson) {
		logger.info("Requesting for addProposalAttachment");
		return proposalService.addProposalAttachment(files, formDataJson);
	}

	@RequestMapping(value = "/saveOrUpdateProposal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveOrUpdateProposal(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveOrUpdateProposal");
		return proposalService.saveOrUpdateProposal(vo);
	}

	@RequestMapping(value = "/loadProposalById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadProposalById(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadProposalById");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("personId : " + vo.getPersonId());
		return proposalService.loadProposalById(vo.getProposalId());
	}

	@RequestMapping(value = "/fetchCostElementByBudgetCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchCostElementByBudgetCategory(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchCostElementByBudgetCategory");
		logger.info("BudgetCategoryCode : " + vo.getBudgetCategoryCode());
		return proposalService.fetchCostElementByBudgetCategory(vo);
	}

	@RequestMapping(value = "/fetchAllAreaOfExcellence", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchAllAreaOfExcellence(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchAllAreaOfExcellence");
		return proposalService.fetchAllAreaOfExcellence(vo);
	}

	@RequestMapping(value = "/deleteProposalKeyword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalKeyword(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalKeyword");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("KeywordId : " + vo.getKeywordId());
		return proposalService.deleteProposalKeyword(vo);
	}

	@RequestMapping(value = "/deleteProposalResearchArea", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalResearchArea(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalResearchArea");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("researchAreaId : " + vo.getResearchAreaId());
		return proposalService.deleteProposalResearchArea(vo);
	}

	@RequestMapping(value = "/deleteProposalPerson", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalPerson(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalPerson");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("proposalPersonId : " + vo.getProposalPersonId());
		return proposalService.deleteProposalPerson(vo);
	}

	@RequestMapping(value = "/deleteProposalSponsor", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalSponsor(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalSponsor");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("sponsorId : " + vo.getSponsorId());
		return proposalService.deleteProposalSponsor(vo);
	}

	@RequestMapping(value = "/deleteIrbProtocol", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteIrbProtocol(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteIrbProtocol");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("irbProtocolId : " + vo.getIrbProtocolId());
		return proposalService.deleteIrbProtocol(vo);
	}

	@RequestMapping(value = "/deleteProposalBudget", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalBudget(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalBudget");
		logger.info("proposalId : " + vo.getProposalId());
		logger.info("budgetId : " + vo.getBudgetId());
		return proposalService.deleteProposalBudget(vo);
	}

	@RequestMapping(value = "/deleteProposalAttachment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteProposalAttachment(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteProposalBudget");
		logger.info("propodalId : " + vo.getProposalId());
		logger.info("attachmentId : " + vo.getAttachmentId());
		return proposalService.deleteProposalAttachment(vo);
	}

	@RequestMapping(value = "/downloadProposalAttachment", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadProposalAttachment(HttpServletResponse response, @RequestHeader("attachmentId") String attachmentId) {
		logger.info("Requesting for downloadProposalAttachment");
		logger.info("attachmentId : " + attachmentId);
		Integer attachmentid = Integer.parseInt(attachmentId);
		return proposalService.downloadProposalAttachment(attachmentid);
	}

	@RequestMapping(value = "/submitProposal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String submitProposal(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for submitProposal");
		return proposalService.submitProposal(vo);
	}

	@RequestMapping(value = "/approveOrRejectProposal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String approveProposal(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for approveOrRejectProposal");
		logger.info("actionType : " + vo.getActionType());
		logger.info("personId : " + vo.getPersonId());
		return proposalService.approveOrRejectProposal(vo);
	}

}
