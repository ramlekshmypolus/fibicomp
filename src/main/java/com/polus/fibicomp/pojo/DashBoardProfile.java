package com.polus.fibicomp.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashBoardProfile {

	private ArrayList<HashMap<String, Object>> dashBoardDetailMap;

	private ArrayList<HashMap<String, Object>> dashBoardResearchSummaryMap;

	private Integer totalServiceRequest;

	private Integer serviceRequestCount;

	private List<Integer> pageNumbers;

	private PersonDTO personDTO;

	private String encryptedUserName;

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
}
