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
import org.springframework.web.bind.annotation.RestController;

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

}
