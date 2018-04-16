package com.polus.fibicomp.proposal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
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
		return proposalService.loadProposalById(vo.getProposalId());
	}

	@RequestMapping(value = "/fetchCostElementByBudgetCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchCostElementByBudgetCategory(@RequestBody ProposalVO vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchCostElementByBudgetCategory");
		logger.info("BudgetCategoryCode : " + vo.getBudgetCategoryCode());
		return proposalService.fetchCostElementByBudgetCategory(vo);
	}
}
