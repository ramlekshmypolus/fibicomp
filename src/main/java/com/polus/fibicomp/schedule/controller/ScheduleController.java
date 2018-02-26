package com.polus.fibicomp.schedule.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.schedule.service.ScheduleService;
import com.polus.fibicomp.schedule.vo.ScheduleVo;

@RestController
public class ScheduleController {

	protected static Logger logger = Logger.getLogger(ScheduleController.class.getName());

	@Autowired
	@Qualifier(value = "scheduleService")
	private ScheduleService scheduleService;
	
	@RequestMapping(value = "/loadScheduleById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadScheduleById(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for loadScheduleById");
		logger.info("scheduleId : " + vo.getScheduleId());
		String committeeDatas = scheduleService.loadScheduleById(vo.getScheduleId());
		return committeeDatas;
	}

	@RequestMapping(value = "/updateSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String updateSchedule(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for updateSchedule");
		String scheduleDatas = scheduleService.updateSchedule(vo);
		return scheduleDatas;
	}

	@RequestMapping(value = "/addOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addOtherActions");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("scheduleId : " + vo.getScheduleId());
		logger.info("scheduleActItemTypecode : " + vo.getCommitteeScheduleActItems().getScheduleActItemTypecode());
		logger.info("scheduleActItemTypeDescription : " + vo.getCommitteeScheduleActItems().getScheduleActItemTypeDescription());
		logger.info("itemDescription : " + vo.getCommitteeScheduleActItems().getItemDescription());
		String scheduleDatas = scheduleService.addOtherActions(vo);
		return scheduleDatas;
	}

	@RequestMapping(value = "/deleteOtherActions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String deleteOtherActions(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for deleteOtherActions");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		logger.info("actionItemId : " + vo.getCommScheduleActItemsId());
		return scheduleService.deleteOtherActions(vo);
	}

	@RequestMapping(value = "/addCommitteeScheduleMinute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addCommitteeScheduleMinute(@RequestBody ScheduleVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addCommitteeScheduleMinute");
		logger.info("committeeId : " + vo.getCommitteeId());
		logger.info("CommitteeScheduleId : " + vo.getScheduleId());
		return scheduleService.addCommitteeScheduleMinute(vo);
	}

}
