package com.polus.fibicomp.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.DisclosureView;
import com.polus.fibicomp.view.IacucView;
import com.polus.fibicomp.view.ProposalView;
import com.polus.fibicomp.view.ProtocolView;
import com.polus.fibicomp.view.ResearchSummaryView;

public class DashBoardProfile {

	private ArrayList<HashMap<String, Object>> dashBoardDetailMap;

	private ArrayList<HashMap<String, Object>> dashBoardResearchSummaryMap;

	private Integer totalServiceRequest;

	private Integer serviceRequestCount;

	private List<Integer> pageNumbers;

	private PersonDTO personDTO;

	private String encryptedUserName;

	private List<AwardView> awardViews;
	
	private List<ProposalView> proposalViews;
	
	private List<ProtocolView> protocolViews;
	
	private List<IacucView> iacucViews;

	private List<DisclosureView> disclosureViews;
	
	private List<ResearchSummaryView> summaryViews;
	
	public ArrayList<HashMap<String, Object>> getDashBoardDetailMap() {
		return dashBoardDetailMap;
	}

	public void setDashBoardDetailMap(ArrayList<HashMap<String, Object>> dashBoardDetailMap) {
		this.dashBoardDetailMap = dashBoardDetailMap;
	}

	public Integer getTotalServiceRequest() {
		return totalServiceRequest;
	}

	public void setTotalServiceRequest(Integer totalServiceRequest) {
		this.totalServiceRequest = totalServiceRequest;
	}

	public Integer getServiceRequestCount() {
		return serviceRequestCount;
	}

	public void setServiceRequestCount(Integer serviceRequestCount) {
		this.serviceRequestCount = serviceRequestCount;
	}

	public List<Integer> getPageNumbers() {
		return pageNumbers;
	}

	public void setPageNumbers(List<Integer> pageNumbers) {
		this.pageNumbers = pageNumbers;
	}

	public PersonDTO getPersonDTO() {
		return personDTO;
	}

	public void setPersonDTO(PersonDTO personDTO) {
		this.personDTO = personDTO;
	}

	public ArrayList<HashMap<String, Object>> getDashBoardResearchSummaryMap() {
		return dashBoardResearchSummaryMap;
	}

	public void setDashBoardResearchSummaryMap(ArrayList<HashMap<String, Object>> dashBoardResearchSummaryMap) {
		this.dashBoardResearchSummaryMap = dashBoardResearchSummaryMap;
	}

	public String getEncryptedUserName() {
		return encryptedUserName;
	}

	public void setEncryptedUserName(String encryptedUserName) {
		this.encryptedUserName = encryptedUserName;
	}

	public List<AwardView> getAwardViews() {
		return awardViews;
	}

	public void setAwardViews(List<AwardView> awardViews) {
		this.awardViews = awardViews;
	}

	public List<ProposalView> getProposalViews() {
		return proposalViews;
	}

	public void setProposalViews(List<ProposalView> proposalViews) {
		this.proposalViews = proposalViews;
	}

	public List<ProtocolView> getProtocolViews() {
		return protocolViews;
	}

	public void setProtocolViews(List<ProtocolView> protocolViews) {
		this.protocolViews = protocolViews;
	}

	public List<IacucView> getIacucViews() {
		return iacucViews;
	}

	public void setIacucViews(List<IacucView> iacucViews) {
		this.iacucViews = iacucViews;
	}

	public List<DisclosureView> getDisclosureViews() {
		return disclosureViews;
	}

	public void setDisclosureViews(List<DisclosureView> disclosureViews) {
		this.disclosureViews = disclosureViews;
	}

	public List<ResearchSummaryView> getSummaryViews() {
		return summaryViews;
	}

	public void setSummaryViews(List<ResearchSummaryView> summaryViews) {
		this.summaryViews = summaryViews;
	}
}
