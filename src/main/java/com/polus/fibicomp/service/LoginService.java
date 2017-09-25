package com.polus.fibicomp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface LoginService {

	public Boolean loginCheck(String loginMode, String userName, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}
