package com.polus.fibicomp.committee.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.pojo.Unit;

@Service
public interface CommitteeDao {

	public List<ProtocolReviewType> fetchAllReviewType();

	public List<Unit> fetchAllHomeUnits();

	public List<ResearchArea> fetchAllResearchAreas();

}
