package com.polus.fibicomp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.PersonDTO;

@Service
public interface LoginService {

	public PersonDTO loginCheck(String loginMode, String userName, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	public PersonDTO readPersonData(String userName);
}
