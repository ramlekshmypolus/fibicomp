package com.polus.fibicomp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.vo.AwardDetailsVO;

import oracle.jdbc.OracleTypes;

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
	public AwardDetailsVO fetchAwardSummaryData(String awardId) {
		logger.info("---------- fetchAwardDetails -----------");
		AwardDetailsVO awardDetailsVO = getAwardSummaryDetails(awardId);
		logger.info("awardDetailsVO : " + awardDetailsVO);
		return awardDetailsVO;
	}

	public AwardDetailsVO getAwardSummaryDetails(String awardId) {
		AwardDetailsVO awardDetailsVO = new AwardDetailsVO();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		
		//Query query = session.createSQLQuery("CALL get_fibi_award_details(:av_award_id)");
		/*Query query = session.getNamedQuery("get_fibi_award_details");
		query.setString("av_award_id", awardId);
		
		List<Award> awards = query.list();
		System.out.println("\n awards : " + awards);
		
		if (awards != null && !awards.isEmpty()) {
			for (Award award : awards) {
				//Award row = (Award)award;
				System.out.println("AwardId : " + award.getAwardId());
				System.out.println("AwardNumber : " + award.getAwardNumber());
				System.out.println("Title : " + award.getTitle());
				System.out.println("AccountNumber : " + award.getAccountNumber());
			}
		}*/

		SessionImpl sessionImpl = (SessionImpl) session;
		Connection con = sessionImpl.connection();
		CallableStatement callstm = null;
		String aProcedureName = "get_fibi_award_details";
		String functionCall = "{call " + aProcedureName + "(?,?)}";
		try {
			callstm = con.prepareCall(functionCall);
			callstm.registerOutParameter(1, OracleTypes.CURSOR);
			callstm.setString(2, awardId);
			callstm.execute();

			ResultSet rset = (ResultSet) callstm.getObject(1);
			List<HashMap<String, Object>> awardDetails = new ArrayList<HashMap<String, Object>>();
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				System.out.println(rset.getString("award_id"));
				System.out.println(rset.getString("award_number"));
				detailsField.put("award_number", rset.getString("award_number"));
				System.out.println(rset.getString("account_number"));
				detailsField.put("account_number", rset.getString("account_number"));
				System.out.println(rset.getString("DOCUMENT_NUMBER"));
				detailsField.put("DOCUMENT_NUMBER", rset.getString("DOCUMENT_NUMBER"));
				System.out.println(rset.getString("ACTIVITY_TYPE_CODE"));
				detailsField.put("ACTIVITY_TYPE_CODE", rset.getString("ACTIVITY_TYPE_CODE"));
				System.out.println(rset.getString("activity_type"));
				detailsField.put("activity_type", rset.getString("activity_type"));
				System.out.println(rset.getString("AWARD_TYPE_CODE"));
				detailsField.put("AWARD_TYPE_CODE", rset.getString("AWARD_TYPE_CODE"));
				System.out.println(rset.getString("award_type"));
				detailsField.put("award_type", rset.getString("award_type"));
				System.out.println(rset.getString("ACCOUNT_TYPE_CODE"));
				detailsField.put("ACCOUNT_TYPE_CODE", rset.getString("ACCOUNT_TYPE_CODE"));
				System.out.println(rset.getString("account_type"));
				detailsField.put("account_type", rset.getString("account_type"));
				System.out.println(rset.getString("SPONSOR_AWARD_NUMBER"));
				detailsField.put("SPONSOR_AWARD_NUMBER", rset.getString("SPONSOR_AWARD_NUMBER"));
				System.out.println(rset.getString("title"));
				detailsField.put("title", rset.getString("title"));
				System.out.println(rset.getString("award_effective_date"));
				detailsField.put("award_effective_date", rset.getString("award_effective_date"));
				System.out.println(rset.getString("obligation_start"));
				detailsField.put("obligation_start", rset.getString("obligation_start"));
				System.out.println(rset.getString("obligation_end"));
				detailsField.put("obligation_end", rset.getString("obligation_end"));
				System.out.println(rset.getString("NOTICE_DATE"));
				detailsField.put("NOTICE_DATE", rset.getString("NOTICE_DATE"));
				System.out.println(rset.getString("obligated_amount"));
				detailsField.put("obligated_amount", rset.getString("obligated_amount"));
				System.out.println(rset.getString("anticipated_amount"));
				detailsField.put("anticipated_amount", rset.getString("anticipated_amount"));
				System.out.println(rset.getString("lead_unit_number"));
				detailsField.put("lead_unit_number", rset.getString("lead_unit_number"));
				System.out.println(rset.getString("lead_unit_name"));
				detailsField.put("lead_unit_name", rset.getString("lead_unit_name"));
				System.out.println(rset.getString("sponsor_code"));
				detailsField.put("sponsor_code", rset.getString("sponsor_code"));
				System.out.println(rset.getString("sponsor_name"));
				detailsField.put("sponsor_name", rset.getString("sponsor_name"));
				System.out.println(rset.getString("award_status"));
				detailsField.put("award_status", rset.getString("award_status"));
				System.out.println(rset.getString("last_update"));
				detailsField.put("last_update", rset.getString("last_update"));
				awardDetails.add(detailsField);
			}
			awardDetailsVO.setAwardDetails(awardDetails);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return awardDetailsVO;
	}

}
