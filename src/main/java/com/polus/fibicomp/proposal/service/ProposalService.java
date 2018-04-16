package com.polus.fibicomp.proposal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.proposal.vo.ProposalVO;

@Transactional
@Service(value = "proposalService")
public interface ProposalService {

	/**
	 * This method is used to create proposal.
	 * @param vo - Object of ProposalVO class.
	 * @return Set of values to create proposal.
	 */
	public String createProposal(ProposalVO vo);

	/**
	 * This method is used to add attachments for a proposal.
	 * @param files - attached files.
	 * @param formDataJSON - form data for the attachment.
	 * @return A String of details of proposal data with list of attachments.
	 */
	public String addProposalAttachment(MultipartFile[] files, String formDataJSON);

	/**
	 * This method is used to save or update proposal.
	 * @param vo - Object of ProposalVO class.
	 * @return A string of details of a proposal.
	 */
	public String saveOrUpdateProposal(ProposalVO vo);

	/**
	 * This method is used to load proposal based on id.
	 * @param proposalId - Id of the proposal.
	 * @return A string of details of a proposal.
	 */
	public String loadProposalById(Integer proposalId);

	/**
	 * This method is used fetch cost elements based on budget category.
	 * @param vo - Object of ProposalVO class.
	 * @return A string of details of proposal which include list of cost elements.
	 */
	public String fetchCostElementByBudgetCategory(ProposalVO vo);

}
