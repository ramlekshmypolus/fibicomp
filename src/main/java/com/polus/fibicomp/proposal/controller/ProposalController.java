package com.polus.fibicomp.proposal.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.proposal.service.ProposalService;

@RestController
public class ProposalController {

	protected static Logger logger = Logger.getLogger(ProposalController.class.getName());

	@Autowired
	@Qualifier(value = "proposalService")
	private ProposalService proposalService;

}
