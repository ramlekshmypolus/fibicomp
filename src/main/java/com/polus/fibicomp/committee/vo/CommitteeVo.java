package com.polus.fibicomp.committee.vo;

import java.util.List;

import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.pojo.Unit;

public class CommitteeVo {

	private List<ProtocolReviewType> reviewTypes;

	private List<Unit> homeUnits;

	private List<ResearchArea> researchAreas;

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
}
