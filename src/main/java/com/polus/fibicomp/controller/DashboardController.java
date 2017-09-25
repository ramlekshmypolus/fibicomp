package com.polus.fibicomp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/*import com.polus.fibicomp.pojo.AwardView;*/
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;
import com.polus.fibicomp.service.DashboardService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
// @RequestMapping("fibicomp")
public class DashboardController {

	@Autowired
	@Qualifier(value = "dashboardService")
	private DashboardService dashboardService;

	/*
	 * @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
	 * public @ResponseBody List<PrincipalBo> searchByName(HttpServletRequest
	 * request) throws Exception { return dashboardService.getAllUsers(); }
	 */

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

	/*
	 * @RequestMapping(value = "/getAllAwards", method = RequestMethod.GET)
	 * public @ResponseBody List<AwardView> getAllAwardViews(HttpServletRequest
	 * request) throws Exception { List<AwardView> awardViews =
	 * dashboardService.getAllAwardViews(); return awardViews; return
	 * dashboardService.getAllAwardViews(); }
	 */

}
