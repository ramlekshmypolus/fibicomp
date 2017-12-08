package com.polus.fibicomp.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.pojo.AwardProfile;
import com.polus.fibicomp.view.AwardView;

@Transactional
@Service(value = "awardDao")
@PropertySource("classpath:application.properties")
public class AwardDaoImpl implements AwardDao {

	protected static Logger logger = Logger.getLogger(AwardDaoImpl.class.getName());

	@Value("${oracledb}")
	private String oracledb;

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@Override

	public String fetchAwardSummaryData(String awardId) throws Exception {

		AwardProfile awardProfile = new AwardProfile();
		AwardView awardView = new AwardView();
		try {
			logger.info("---------- fetchAwardDetails -----------");
			awardView = getAwardSummaryDetails(awardId, awardView);
			logger.info("awardViewDetails : " + awardView);

			awardProfile.setAwardView(awardView);
		} catch (Exception e) {
			logger.error("Error in method AwardDaoImpl.fetchAwardSummaryData");
			e.printStackTrace();
		}
		return null;
	}
	public AwardView getAwardSummaryDetails(String awardId, AwardView awardView) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		return awardView;
	}

}
