package com.polus.fibicomp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.dao.LoginDao;
import com.polus.fibicomp.pojo.PersonDTO;

@Transactional
@Service(value = "loginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao loginDao;

	@Override
	public PersonDTO loginCheck(String loginMode, String userName, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean isLoginSuccess = false;
		PersonDTO personDTO = null;
		if ("USERID".equalsIgnoreCase(loginMode)) {
			isLoginSuccess = loginDao.authenticate(userName, password);
		}
		if (isLoginSuccess) {
			HttpSession session = request.getSession();
			personDTO = loginDao.readPersonData(userName);
			session.setAttribute("personDTO", personDTO);
			session.setAttribute("user", personDTO.getFullName());
			return personDTO;
		}
		return personDTO;
	}

	@Override
	public PersonDTO readPersonData(String userName) {
		return loginDao.readPersonData(userName);
	}
}
