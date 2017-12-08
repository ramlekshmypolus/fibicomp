package com.polus.fibicomp.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.service.AwardService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
@CrossOrigin(origins = { "http://demo.fibiweb.com/fibi30", "http://demo.fibiweb.com/kc-dev",
		"http://192.168.1.76:8080/fibi30" })
public class AwardController {

	protected static Logger logger = Logger.getLogger(AwardController.class.getName());

	@Autowired
	@Qualifier(value = "awardService")
	private AwardService awardService;

	@RequestMapping(value = "/getAwardSummary", method = RequestMethod.POST)
	public String fetchAwardSummary(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		return awardService.getAwardSummaryData(vo.getAwardId());
	}
}
