package com.polus.fibicomp.schedule.service;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.schedule.vo.ScheduleVo;

@Service
public interface ScheduleService {

	public String loadScheduleById(Integer scheduleId);

	public String updateSchedule(ScheduleVo scheduleVo);
}
