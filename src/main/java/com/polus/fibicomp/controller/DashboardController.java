package com.polus.fibicomp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.service.DashboardService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
public class DashboardController {

	protected static Logger logger = Logger.getLogger(DashboardController.class.getName());

	@Autowired
	@Qualifier(value = "dashboardService")
	private DashboardService dashboardService;

	@RequestMapping(value = "/getResearchSummaryData", method = RequestMethod.POST)
	public String requestResearchSummaryData(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO");
		return dashboardService.getDashBoardResearchSummary(personDTO);
	}

	@RequestMapping(value = "/fibiDashBoard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String requestInitialLoad(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO");
		String tabIndex = vo.getTabIndex();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		return dashboardService.getDashBoardData(personDTO, tabIndex, pageNumber, sortBy, reverse);

	}

	@RequestMapping(value = "/searchDashBoard", method = RequestMethod.POST)
	public String searchByName(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO");
		String inputData = vo.getInputData();
		String tabIndex = vo.getTabIndex();
		logger.info("TabIndex : " + tabIndex);
		return dashboardService.getSearchData(personDTO, tabIndex, inputData);
	}

}
