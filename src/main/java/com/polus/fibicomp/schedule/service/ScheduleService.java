package com.polus.fibicomp.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.polus.fibicomp.schedule.vo.ScheduleVo;

@Service
public interface ScheduleService {

	public String loadScheduleById(Integer scheduleId);

	public String updateSchedule(ScheduleVo scheduleVo);

	public String addOtherActions(ScheduleVo scheduleVo);

	public String deleteOtherActions(ScheduleVo scheduleVo);

	public String addCommitteeScheduleMinute(ScheduleVo scheduleVo);

	public String updateScheduleAttendance(ScheduleVo scheduleVo);

	public String addOthersPresent(ScheduleVo scheduleVo);

	public String deleteScheduleMinute(ScheduleVo scheduleVo);

	public String deleteScheduleAttachment(ScheduleVo scheduleVo);

	public String addScheduleAttachment(ScheduleVo scheduleVo, MultipartFile file);

}
