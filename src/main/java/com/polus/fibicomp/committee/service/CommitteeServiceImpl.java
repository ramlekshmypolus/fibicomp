package com.polus.fibicomp.committee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.committee.dao.CommitteeDao;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeResearchAreas;
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
		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);

		String response = committeeDao.convertObjectToJSON(committeeVo);
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
		committee.setResearchAreas(new ArrayList<CommitteeResearchAreas>());
		committeeVo.setCommittee(committee);

		List<ProtocolReviewType> reviewTypes = committeeDao.fetchAllReviewType();
		committeeVo.setReviewTypes(reviewTypes);
		List<Unit> units = committeeDao.fetchAllHomeUnits();
		committeeVo.setHomeUnits(units);
		List<ResearchArea> researchAreas = committeeDao.fetchAllResearchAreas();
		committeeVo.setResearchAreas(researchAreas);

		String response = committeeDao.convertObjectToJSON(committeeVo);
		return response;
	}

	@Override
	public String saveCommittee(CommitteeVo vo) {
		Committee committee = vo.getCommittee();
		String updateType = vo.getUpdateType();
		if (updateType != null && updateType.equals("SAVE")) {
			committee.setVerNbr(1);
			committee.setCreateTimestamp(committeeDao.getCurrentTimestamp());
			committee.setCreateUser(vo.getCurrentUser());
			committee.setObjId(UUID.randomUUID().toString());
			committee.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
			committee.setUpdateUser(vo.getCurrentUser());
		}
		committee.setUpdateTimestamp(committeeDao.getCurrentTimestamp());
		committee.setUpdateUser(vo.getCurrentUser());
		//committee = committeeDao.saveCommittee(committee);
		vo.setCommittee(committee);
		String response = committeeDao.convertObjectToJSON(vo);
		logger.info("response : " + response);
		return response;
	}

}
