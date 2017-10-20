package com.polus.fibicomp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.dao.LoginDao;
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;

@Transactional
@Service(value = "loginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao loginDao;

	@Override
	public PersonDTO loginCheck(String loginMode, String userName, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrincipalBo principalBo = null;
		PersonDTO personDTO = null;
		if ("USERID".equalsIgnoreCase(loginMode)) {
			principalBo = loginDao.authenticate(userName, password);
		}
		if (principalBo != null) {
			personDTO = loginDao.readPersonData(userName);
			return personDTO;
		}
		return personDTO;
	}

	@Override
	public boolean isUnitAdmin(String personId) {
		return loginDao.isUnitAdmin(personId);
	}
}
