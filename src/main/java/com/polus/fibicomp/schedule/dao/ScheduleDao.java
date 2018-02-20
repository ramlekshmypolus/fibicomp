package com.polus.fibicomp.schedule.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.pojo.ProtocolSubmission;
import com.polus.fibicomp.committee.pojo.ScheduleActItemType;

@Service
public interface ScheduleDao {

	public List<ProtocolSubmission> fetchProtocolSubmissionByIds(Integer scheduleId, String committeeId);

	public List<ScheduleActItemType> fetchAllScheduleActItemType();

}
