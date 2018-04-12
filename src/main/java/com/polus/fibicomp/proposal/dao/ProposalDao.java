package com.polus.fibicomp.proposal.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalCategory;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalStatus;

@Service
public interface ProposalDao {

	public ProposalStatus fetchStatusByStatusCode(Integer statusCode);

	public List<ProposalCategory> fetchAllCategories();

	public List<Protocol> fetchAllProtocols();

	public List<ProposalAttachmentType> fetchAllProposalAttachmentTypes();

	public List<GrantCall> fetchAllGrantCalls();

	public List<ProposalPersonRole> fetchAllProposalPersonRoles();

	public List<ProposalResearchType> fetchAllProposalResearchTypes();
}
