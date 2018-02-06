package com.polus.fibicomp.committee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.committee.service.CommitteeService;
import com.polus.fibicomp.committee.vo.CommitteeVo;

@RestController
public class CommitteeController {

	protected static Logger logger = Logger.getLogger(CommitteeController.class.getName());

	@Autowired
	@Qualifier(value = "committeeService")
	private CommitteeService committeeService;

	@RequestMapping(value = "/createCommittee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String createCommittee(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for createCommittee");
		String committeeDatas = committeeService.createCommittee(vo.getCommitteeTypeCode());
		//logger.info("committeeDatas : " + committeeDatas);
		return committeeDatas;
	}

	@RequestMapping(value = "/saveCommittee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveCommittee(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommittee");
		String committeeDatas = committeeService.saveCommittee(vo);
		//logger.info("committeeDatas : " + committeeDatas);
		return committeeDatas;
	}

	@RequestMapping(value = "/loadCommitteeById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadCommitteeById(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for saveCommittee");
		String committeeDatas = committeeService.loadCommitteeById(vo.getCommitteeId());
		//logger.info("committeeDatas : " + committeeDatas);
		return committeeDatas;
	}

	@RequestMapping(value = "/addSchedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addSchedule(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addSchedule");
		String committeeDatas = "";
		try {
			committeeDatas = committeeService.addSchedule(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//logger.info("committeeDatas : " + committeeDatas);
		return committeeDatas;
	}

	/*@RequestMapping(value = "/fetchInitialDatas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String fetchInitialDatas(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for fetchInitialDatas");
		String initialDatas = committeeService.fetchInitialDatas();
		return initialDatas;
	}*/

	@RequestMapping(value = "/saveAreaOfResearch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveAreaOfResearch(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		return committeeService.saveAreaOfResearch(vo);
	}

	@RequestMapping(value = "/deleteAreaOfResearch/{researchAreaId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void deleteAreaOfResearch(@PathVariable("researchAreaId") int researchAreaId, HttpServletRequest request, HttpServletResponse response) {
		committeeService.deleteAreaOfResearch(researchAreaId);
	}

	@RequestMapping(value = "/deleteSchedule/{scheduleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void deleteSchedule(@PathVariable("scheduleId") int scheduleId, HttpServletRequest request, HttpServletResponse response) {
		committeeService.deleteSchedule(scheduleId);
	}

	/*@RequestMapping(value = "/loadAllResearchAreas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadAllResearchAreas(HttpServletRequest request, HttpServletResponse response) {
		return committeeService.loadAllResearchAreas();
	}

	@RequestMapping(value = "/loadAllUnitsAndReviewTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loadAllUnitsAndReviewTypes(HttpServletRequest request, HttpServletResponse response) {
		return committeeService.loadAllUnitsAndReviewTypes();
	}*/

	/*@RequestMapping(value = "/getAllEmployees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getEmployees(HttpServletRequest request, HttpServletResponse response) {
		return committeeService.getAllEmployees();
	}

	@RequestMapping(value = "/getAllNonEmployees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getNonEmployees(HttpServletRequest request, HttpServletResponse response) {
		return committeeService.getAllNonEmployees();
	}*/

	@RequestMapping(value = "/addCommitteeMembership", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String addCommitteeMembership(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Requesting for addCommitteeMembership");
		String committeeDatas = committeeService.addCommitteeMembership(vo);
		//logger.info("committeeDatas : " + committeeDatas);
		return committeeDatas;
	}

	@RequestMapping(value = "/saveCommitteeMembers", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String saveCommitteeMembers(@RequestBody CommitteeVo vo, HttpServletRequest request, HttpServletResponse response) {
		return committeeService.saveAreaOfResearch(vo);
	}

}
