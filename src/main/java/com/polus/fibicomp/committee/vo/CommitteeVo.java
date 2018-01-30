package com.polus.fibicomp.committee.vo;

import java.util.List;

import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.pojo.Unit;

public class CommitteeVo {

	private Integer committeeTypeCode;

	private Committee committee;

	private List<ProtocolReviewType> reviewTypes;

	private List<Unit> homeUnits;

	private List<ResearchArea> researchAreas;

	private String updateType;

	private String currentUser;

	public List<ProtocolReviewType> getReviewTypes() {
		return reviewTypes;
	}

	public void setReviewTypes(List<ProtocolReviewType> reviewTypes) {
		this.reviewTypes = reviewTypes;
	}

	public List<Unit> getHomeUnits() {
		return homeUnits;
	}

	public void setHomeUnits(List<Unit> homeUnits) {
		this.homeUnits = homeUnits;
	}

	public List<ResearchArea> getResearchAreas() {
		return researchAreas;
	}

	public void setResearchAreas(List<ResearchArea> researchAreas) {
		this.researchAreas = researchAreas;
	}

	public Committee getCommittee() {
		return committee;
	}

	public void setCommittee(Committee committee) {
		this.committee = committee;
	}

	public Integer getCommitteeTypeCode() {
		return committeeTypeCode;
	}

	public void setCommitteeTypeCode(Integer committeeTypeCode) {
		this.committeeTypeCode = committeeTypeCode;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
}
