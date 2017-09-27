package com.polus.fibicomp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.service.LoginService;
import com.polus.fibicomp.vo.CommonVO;

@RestController
public class LoginController {

	protected static Logger logger = Logger.getLogger(LoginController.class.getName());

	@Autowired
	@Qualifier(value = "loginService")
	private LoginService loginService;

	@Value("${LOGIN_MODE}")
	private String login_mode;

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loginUser(@RequestBody CommonVO vo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("Received request for login: ");
		String userName = vo.getUserName();
		String password = vo.getPassword();
		Boolean loginCheck = loginService.loginCheck(login_mode, userName, password, request, response);
		if (loginCheck) {
			return "SUCCESS";
		} else {
			return "FAIL";
		}
	}

	@RequestMapping(value = "/fibi.dashboard", method = RequestMethod.GET)
	public String home(Model model, HttpSession session) {

		PersonDTO personDTO = (PersonDTO) session.getAttribute("personDTO");
		if (personDTO != null) {
			model.addAttribute("name", personDTO.getUserName());
			model.addAttribute("fullName", personDTO.getFullName());
			model.addAttribute("roleNumber", personDTO.getRoleNumber());
			model.addAttribute("createPermNo", personDTO.getCreateNo());
			model.addAttribute("personID", personDTO.getPersonID());
		} else {
			return "redirect:/login";
		}
		return "/fibi.dashboard";
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		logger.debug("Login failed");
		model.addAttribute("error", "true");
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Log Out");
		// invalidate the session if exists
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		if ("TOUCHSTONE".equalsIgnoreCase(login_mode)) {
			return "logout";
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/logoutSession", method = RequestMethod.POST)
	public @ResponseBody void logoutSession(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

}
