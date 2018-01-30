package com.polus.fibicomp.committee.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeType;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.vo.CommitteeVo;
import com.polus.fibicomp.pojo.Unit;

@Transactional
@Service(value = "committeeService")
public class CommitteeServiceImpl implements CommitteeService {

	protected static Logger logger = Logger.getLogger(CommitteeServiceImpl.class.getName());

	@Autowired
	private CommitteeDao committeeDao;

	@Override
	public String fetchInitialDatas() {
		CommitteeVo committeeVo = new CommitteeVo();
		String response = "";
		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);

		ObjectMapper mapper = new ObjectMapper();
		try {
			response = mapper.writeValueAsString(committeeVo);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// logger.info("reviewTypes : " + response);
		return response;
	}

	@Override
	public String createCommittee(Integer committeeTypeCode) {
		CommitteeVo committeeVo = new CommitteeVo();
		Committee committee = new Committee();
		committeeVo.setCommitteeTypeCode(committeeTypeCode);
		CommitteeType committeeType = committeeDao.fetchCommitteeType(committeeTypeCode);
		committee.setCommitteeTypeCode(committeeType.getCommitteeTypeCode());
		committee.setCommitteeType(committeeType);
		committeeVo.setCommittee(committee);

		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);

		String response = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			response = mapper.writeValueAsString(committeeVo);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}

}
