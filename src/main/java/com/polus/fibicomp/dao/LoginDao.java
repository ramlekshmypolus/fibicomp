package com.polus.fibicomp.dao;

//import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.polus.fibicomp.pojo.PersonDTO;

@Service
public interface LoginDao {

	public boolean authenticate(String userName, String password);

	public PersonDTO readPersonData(String userName);

}
