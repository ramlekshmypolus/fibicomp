package com.polus.fibicomp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

/**
 * LoginService class for login functionality.
 *
 */
@Service
public interface LoginService {

	/**
	 * This method is used to authenticate the user.
	 * @param loginMode - Login mode of the user.
	 * @param userName - Username of the user.
	 * @param password - Password of the user.
	 * @param request
	 * @param response
	 * @return String containing details of the user.
	 * @throws Exception
	 */
	public String loginCheck(String loginMode, String userName, String password, HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * This method is used to find the role of the user.
	 * @param personId - ID of the user.
	 * @return A boolean value to indicate role of the user.
	 */
	public boolean isUnitAdmin(String personId);
}
