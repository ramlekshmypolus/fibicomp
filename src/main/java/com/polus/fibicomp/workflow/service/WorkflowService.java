package com.polus.fibicomp.workflow.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.proposal.vo.ProposalVO;

@Service
public interface WorkflowService {

	/**
	 * @param proposalVO
	 * @return
	 */
	public String submitProposal(ProposalVO proposalVO);

	public String approveProposal(ProposalVO proposalVO);

	public String disapproveProposal(ProposalVO proposalVO);

}
