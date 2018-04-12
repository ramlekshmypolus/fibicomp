package com.polus.fibicomp.proposal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.proposal.vo.ProposalVO;

@Transactional
@Service(value = "proposalService")
public interface ProposalService {

	public String createProposal(ProposalVO vo);
}
