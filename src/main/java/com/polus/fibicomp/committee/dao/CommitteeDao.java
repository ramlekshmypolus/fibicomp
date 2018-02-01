package com.polus.fibicomp.committee.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeType;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.committee.pojo.ScheduleStatus;
import com.polus.fibicomp.pojo.Unit;

@Service
public interface CommitteeDao {

	public CommitteeType fetchCommitteeType(Integer committeeTypeCode);

	public List<ProtocolReviewType> fetchAllReviewType();

	public List<Unit> fetchAllHomeUnits();

	public List<ResearchArea> fetchAllResearchAreas();

	public Date getCurrentDate();

	public Timestamp getCurrentTimestamp();

	public String convertObjectToJSON(Object object);

	public Committee saveCommittee(Committee committee);

	public Committee fetchCommitteeById(String committeeId);

	public ScheduleStatus fetchScheduleStatusByStatus(String scheduleStatus);

	public List<Committee> loadAllCommittee();

}
