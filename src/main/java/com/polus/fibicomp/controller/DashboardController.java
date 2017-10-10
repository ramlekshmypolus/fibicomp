package com.polus.fibicomp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.service.DashboardService;
import com.polus.fibicomp.service.LoginService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
//@CrossOrigin(origins = {"http://192.168.1.72:8080"})
@CrossOrigin(origins = {"http://demo.fibiweb.com/fibi30"})
public class DashboardController {

	protected static Logger logger = Logger.getLogger(DashboardController.class.getName());

	@Autowired
	@Qualifier(value = "dashboardService")
	private DashboardService dashboardService;

	@Autowired
	@Qualifier(value = "loginService")
	private LoginService loginService;

	@RequestMapping(value = "/getResearchSummaryData", method = RequestMethod.GET)
	public String requestResearchSummaryData(HttpServletRequest request) throws Exception {
		/*HttpSession session = request.getSession();
		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO");*/
		PersonDTO personDTO = loginService.readPersonData("quickstart");
		return dashboardService.getDashBoardResearchSummary(personDTO);
	}

	@RequestMapping(value = "/fibiDashBoard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String requestInitialLoad(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		PersonDTO personDTO = loginService.readPersonData(vo.getUserName());
		String tabIndex = vo.getTabIndex();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		logger.info("tabIndex : " + tabIndex);
		logger.info("pageNumber : " + pageNumber);
		logger.info("sortBy : " + sortBy);
		logger.info("reverse : " + reverse);
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

	@RequestMapping(value = "/searchByProperty", method = RequestMethod.POST)
	public String searchByProperty(@RequestBody CommonVO vo, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO");
		return dashboardService.getSearchDataByProperty(personDTO, vo);
	}
	
	@RequestMapping(value = "/getUserNotification", method = RequestMethod.GET)
	public List<ActionItem> getUserNotification(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO");
		return dashboardService.getUserNotification(personDTO.getPersonID());
	}
}
