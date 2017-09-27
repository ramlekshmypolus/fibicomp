package com.polus.fibicomp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.PrincipalBo;
import com.polus.fibicomp.view.PersonDetailsView;
import oracle.jdbc.driver.OracleTypes;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;

@Transactional
@Service(value = "loginDao")
public class LoginDaoImpl implements LoginDao {

	protected static Logger logger = Logger.getLogger(LoginDaoImpl.class.getName());

	@Value("${oracledb}")
	private String oracledb;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public boolean authenticate(String userName, String password) {
		boolean isLoginSuccess = false;
		try {
			logger.info("userName : " + userName + " and password : " + password);
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria crit = session.createCriteria(PrincipalBo.class);
			crit.add(Restrictions.eq("principalName", userName));
			PrincipalBo principalBo = (PrincipalBo) crit.uniqueResult();
			if (principalBo != null) {
				isLoginSuccess = true;
			}
			logger.info("isLoginSuccess :" + isLoginSuccess);
		} catch (Exception e) {
			logger.debug("Sql Exception: " + e);
		}
		return isLoginSuccess;
	}

	public PersonDTO readPersonData1(String userName) {
		PersonDTO personDTO = new PersonDTO();
		try {
			logger.info("readPersonData");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			if (oracledb.equals("Y")) {
				ResultSet resultSet = null;
				String aProcedureName = "get_ab_person_details";
				String functionCall = "{call " + aProcedureName + "(?, ?)}";
				Connection aConnection = ((SessionImpl) session).connection();
				CallableStatement callstm = null;
				callstm = aConnection.prepareCall(functionCall);

				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, userName);
				callstm.execute();
				resultSet = (ResultSet) callstm.getObject(1);
				System.out.println("resultSet : " + resultSet);
				while (resultSet.next()) {
					personDTO.setPersonID(resultSet.getString(1));
					personDTO.setFirstName(resultSet.getString(2));
					personDTO.setHasDual(resultSet.getString(3));
					personDTO.setLastName(resultSet.getString(4));
					personDTO.setFullName(resultSet.getString(5));
					personDTO.setEmail(resultSet.getString(6));
					personDTO.setUnitNumber(resultSet.getString(7));
					personDTO.setUserName(userName);
				}
			} else {
				Query query = session.createSQLQuery("CALL get_ab_person_details(:av_prncpl_nm)");
				query.setString("av_prncpl_nm", userName);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<Object> fields = query.list();
				System.out.println("\n fields : " + fields);
				if (fields != null && !fields.isEmpty()) {
					for (Object person : fields) {
						Map row = (Map) person;
						personDTO.setPersonID((String) row.get("prncpl_id"));
						personDTO.setFirstName((String) row.get("first_nm"));
						personDTO.setLastName((String) row.get("last_nm"));
						personDTO.setFullName((String) row.get("full_name"));
						personDTO.setEmail((String) row.get("email_addr"));
						personDTO.setUnitNumber((String) row.get("unit_name"));
						personDTO.setUserName(userName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method readPersonData", e);
		}
		return personDTO;
	}

	public PersonDTO readPersonData(String userName) {
		PersonDTO personDTO = new PersonDTO();
		try {
			logger.info("readPersonData :" + userName);
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(PersonDetailsView.class);
			criteria.add(Restrictions.eq("prncplName", userName));
			PersonDetailsView person = (PersonDetailsView) criteria.uniqueResult();
			logger.info("Person Detail :" + person);
			if (person != null) {
				personDTO.setPersonID(person.getPrncplId());
				personDTO.setFirstName(person.getFirstName());
				personDTO.setLastName(person.getLastName());
				personDTO.setFullName(person.getFullName());
				personDTO.setEmail(person.getEmailAddress());
				personDTO.setUnitNumber(person.getUnitNumber());
				personDTO.setUserName(userName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method readPersonData", e);
		}
		return personDTO;
	}
}
