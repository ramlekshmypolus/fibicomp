package com.polus.fibicomp.schedule.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.CommitteeScheduleActItems;
import com.polus.fibicomp.committee.pojo.ScheduleActItemType;
import com.polus.fibicomp.view.ProtocolView;

@Service
public interface ScheduleDao {

	public ProtocolView fetchProtocolViewByParams(Integer protocolId, String personId, String fullName);

	public List<ScheduleActItemType> fetchAllScheduleActItemType();

	public CommitteeSchedule updateCommitteeSchedule(CommitteeSchedule committeeSchedule);

	public CommitteeScheduleActItems addOtherActions(CommitteeScheduleActItems committeeScheduleActItems);

}