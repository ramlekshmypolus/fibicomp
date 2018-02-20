package com.polus.fibicomp.schedule.service;

import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {

	public String loadScheduleById(Integer scheduleId);

}
