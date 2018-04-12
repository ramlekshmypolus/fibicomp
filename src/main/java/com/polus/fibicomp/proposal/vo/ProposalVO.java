package com.polus.fibicomp.proposal.vo;

import java.util.List;

import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.FundingSourceType;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.pojo.ScienceKeyword;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.proposal.pojo.ProposalCategory;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalStatus;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;

public class ProposalVO {

	private Integer grantCallId;

	private Integer statusCode;

	private ProposalStatus proposalStatus;

	private Proposal proposal;

	private List<GrantCallType> grantCallTypes;

	private List<ProposalCategory> proposalCategories;

	private List<ScienceKeyword> scienceKeywords;

	private List<ResearchArea> researchAreas;

	private List<ProposalResearchType> proposalResearchTypes;

	private List<FundingSourceType> fundingSourceTypes;

	private List<Protocol> protocols;

	private List<ProposalAttachmentType> proposalAttachmentTypes;

	private List<GrantCall> grantCalls;

	private List<ProposalPersonRole> proposalPersonRoles;
	
	public ProposalVO() {
		proposal = new Proposal();
	}

	public Integer getGrantCallId() {
		return grantCallId;
	}

	public void setGrantCallId(Integer grantCallId) {
		this.grantCallId = grantCallId;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public ProposalStatus getProposalStatus() {
		return proposalStatus;
	}

	public void setProposalStatus(ProposalStatus proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public List<GrantCallType> getGrantCallTypes() {
		return grantCallTypes;
	}

	public void setGrantCallTypes(List<GrantCallType> grantCallTypes) {
		this.grantCallTypes = grantCallTypes;
	}

	public List<ProposalCategory> getProposalCategories() {
		return proposalCategories;
	}

	public void setProposalCategories(List<ProposalCategory> proposalCategories) {
		this.proposalCategories = proposalCategories;
	}

	public List<ScienceKeyword> getScienceKeywords() {
		return scienceKeywords;
	}

	public void setScienceKeywords(List<ScienceKeyword> scienceKeywords) {
		this.scienceKeywords = scienceKeywords;
	}

	public List<ResearchArea> getResearchAreas() {
		return researchAreas;
	}

	public void setResearchAreas(List<ResearchArea> researchAreas) {
		this.researchAreas = researchAreas;
	}

	public List<ProposalResearchType> getProposalResearchTypes() {
		return proposalResearchTypes;
	}

	public void setProposalResearchTypes(List<ProposalResearchType> proposalResearchTypes) {
		this.proposalResearchTypes = proposalResearchTypes;
	}

	public List<FundingSourceType> getFundingSourceTypes() {
		return fundingSourceTypes;
	}

	public void setFundingSourceTypes(List<FundingSourceType> fundingSourceTypes) {
		this.fundingSourceTypes = fundingSourceTypes;
	}

	public List<Protocol> getProtocols() {
		return protocols;
	}

	public void setProtocols(List<Protocol> protocols) {
		this.protocols = protocols;
	}

	public List<ProposalAttachmentType> getProposalAttachmentTypes() {
		return proposalAttachmentTypes;
	}

	public void setProposalAttachmentTypes(List<ProposalAttachmentType> proposalAttachmentTypes) {
		this.proposalAttachmentTypes = proposalAttachmentTypes;
	}

	public List<GrantCall> getGrantCalls() {
		return grantCalls;
	}

	public void setGrantCalls(List<GrantCall> grantCalls) {
		this.grantCalls = grantCalls;
	}

	public List<ProposalPersonRole> getProposalPersonRoles() {
		return proposalPersonRoles;
	}

	public void setProposalPersonRoles(List<ProposalPersonRole> proposalPersonRoles) {
		this.proposalPersonRoles = proposalPersonRoles;
	}
}
