package com.polus.fibicomp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.pojo.AwardHierarchy;
import com.polus.fibicomp.vo.AwardDetailsVO;
import com.polus.fibicomp.vo.AwardHierarchyVO;
import com.polus.fibicomp.vo.AwardTermsAndReportsVO;
import com.polus.fibicomp.vo.CommitmentsVO;

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
		AwardDetailsVO awardDetailsVO = new AwardDetailsVO();
		getAwardSummaryDetails(awardId, awardDetailsVO);
		getPersonDetails(awardId, awardDetailsVO);
		getSponsorContactDetails(awardId, awardDetailsVO);
		getUnitContactDetails(awardId, awardDetailsVO);
		getFundedProposalsDetails(awardId, awardDetailsVO);
		getSpecialReviewsDetails(awardId, awardDetailsVO);
		logger.info("awardDetailsVO : " + awardDetailsVO);
		return awardDetailsVO;
	}

	@Override
	public AwardTermsAndReportsVO fetchAwardReportsAndTerms(String awardId) {
		logger.info("---------- getAwardReportsAndTerms -----------");
		AwardTermsAndReportsVO awardTermsAndReportsVO = new AwardTermsAndReportsVO();
		getApprovdEquipmentDetails(awardId, awardTermsAndReportsVO);
		getApprovedTravelDetails(awardId, awardTermsAndReportsVO);
		getTermsDetails(awardId, awardTermsAndReportsVO);
		getPaymentDetails(awardId, awardTermsAndReportsVO);
		getReportDetails(awardId, awardTermsAndReportsVO);
		getPaymentSchedule(awardId, awardTermsAndReportsVO);
		getPaymentAndInvoiceRequirement(awardId, awardTermsAndReportsVO);
		logger.info("awardDetailsVO : " + awardTermsAndReportsVO);
		return awardTermsAndReportsVO;
	}

	@Override
	public AwardHierarchyVO fetchAwardHierarchyData(String awardNumber, String selectedAwardNumber) {
		AwardHierarchyVO awardHierarchyVO = new AwardHierarchyVO();
		getHierarchyDetails(awardNumber, awardHierarchyVO, selectedAwardNumber);
		// getBudgetVersionDetails(awardNumber, awardHierarchyVO);
		logger.info("awardHierarchyVO : " + awardHierarchyVO);
		return awardHierarchyVO;
	}

	@Override
	public CommitmentsVO fetchAwardCommitmentsData(String awardId) {
		CommitmentsVO commitmentsVO = new CommitmentsVO();
		getCostShareData(awardId, commitmentsVO);
		getFandAData(awardId, commitmentsVO);
		getBenefitsRates(awardId, commitmentsVO);
		logger.info("commitmentsVO : " + commitmentsVO);
		return commitmentsVO;
	}

	public void getAwardSummaryDetails(String awardId, AwardDetailsVO awardDetailsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> awardDetails = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_details(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_details";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("account_number", rset.getString("account_number"));
				detailsField.put("document_number", rset.getString("document_number"));
				detailsField.put("activity_type_code", rset.getString("activity_type_code"));
				detailsField.put("activity_type", rset.getString("activity_type"));
				detailsField.put("award_type_code", rset.getString("award_type_code"));
				detailsField.put("award_type", rset.getString("award_type"));
				detailsField.put("account_type_code", rset.getString("account_type_code"));
				detailsField.put("account_type", rset.getString("account_type"));
				detailsField.put("sponsor_award_number", rset.getString("sponsor_award_number"));
				detailsField.put("title", rset.getString("title"));
				detailsField.put("award_effective_date", rset.getString("award_effective_date"));
				detailsField.put("obligation_start", rset.getString("obligation_start"));
				detailsField.put("obligation_end", rset.getString("obligation_end"));
				detailsField.put("notice_date", rset.getString("notice_date"));
				detailsField.put("obligated_amount", rset.getString("obligated_amount"));
				detailsField.put("anticipated_amount", rset.getString("anticipated_amount"));
				detailsField.put("lead_unit_number", rset.getString("lead_unit_number"));
				detailsField.put("lead_unit_name", rset.getString("lead_unit_name"));
				detailsField.put("sponsor_code", rset.getString("sponsor_code"));
				detailsField.put("sponsor_name", rset.getString("sponsor_name"));
				detailsField.put("award_status", rset.getString("award_status"));
				detailsField.put("last_update", rset.getString("last_update"));
				detailsField.put("root_award_number", rset.getString("root_award_number"));
				detailsField.put("person_id", rset.getString("person_id"));
				detailsField.put("full_name", rset.getString("full_name"));
				detailsField.put("prncpl_nm", rset.getString("prncpl_nm"));
				awardDetails.add(detailsField);
			}
			awardDetailsVO.setAwardDetails(awardDetails);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getPersonDetails(String awardId, AwardDetailsVO awardDetailsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement callstm = null;
		ResultSet rset = null;
		List<HashMap<String, Object>> awardPersons = new ArrayList<HashMap<String, Object>>();
		try {
			if (oracledb.equals("Y")) {
				String procedureName = "get_fibi_award_person_details";
				String functionCall = "{call " + procedureName + "(?,?)}";
				callstm = connection.prepareCall(functionCall);
				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, awardId);
				callstm.execute();
				rset = (ResultSet) callstm.getObject(1);
			} else if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				callstm = connection.prepareCall("{call get_fibi_award_person_details(?)}");
				callstm.setInt(1, award_id);
				callstm.execute();
				rset = callstm.getResultSet();
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("person_id", rset.getString("person_id"));
				detailsField.put("full_name", rset.getString("full_name"));
				detailsField.put("contact_role_code", rset.getString("contact_role_code"));
				detailsField.put("unit_number", rset.getString("unit_number"));
				detailsField.put("unit_name", rset.getString("unit_name"));
				detailsField.put("user_name", rset.getString("prncpl_nm"));
				awardPersons.add(detailsField);
			}
			awardDetailsVO.setAwardPersons(awardPersons);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getSponsorContactDetails(String awardId, AwardDetailsVO awardDetailsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement callstm = null;
		ResultSet rset = null;
		List<HashMap<String, Object>> awardSponsorContact = new ArrayList<HashMap<String, Object>>();
		try {
			if (oracledb.equals("Y")) {
				String procedureName = "get_fibi_award_spnsor_contact";
				String functionCall = "{call " + procedureName + "(?,?)}";
				callstm = connection.prepareCall(functionCall);
				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, awardId);
				callstm.execute();
				rset = (ResultSet) callstm.getObject(1);
			} else if (oracledb.equals("N")) {
				int award_id = Integer.parseInt(awardId);
				callstm = connection.prepareCall("{call get_fibi_award_spnsor_contact(?)}");
				callstm.setInt(1, award_id);
				callstm.execute();
				rset = callstm.getResultSet();
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("full_name", rset.getString("full_name"));
				detailsField.put("address_line_1", rset.getString("address_line_1"));
				detailsField.put("address_line_2", rset.getString("address_line_2"));
				detailsField.put("address_line_3", rset.getString("address_line_3"));
				detailsField.put("email_address", rset.getString("email_address"));
				detailsField.put("city", rset.getString("city"));
				detailsField.put("postal_code", rset.getString("postal_code"));
				detailsField.put("contact_role_code", rset.getString("contact_role_code"));
				detailsField.put("contact_type", rset.getString("contact_type"));
				awardSponsorContact.add(detailsField);
			}
			awardDetailsVO.setAwardSponsorContact(awardSponsorContact);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getUnitContactDetails(String awardId, AwardDetailsVO awardDetailsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement callstm = null;
		ResultSet rset = null;
		List<HashMap<String, Object>> awardUnitContact = new ArrayList<HashMap<String, Object>>();
		try {
			if (oracledb.equals("Y")) {
				String procedureName = "get_fibi_award_unit_contact";
				String functionCall = "{call " + procedureName + "(?,?)}";
				callstm = connection.prepareCall(functionCall);
				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, awardId);
				callstm.execute();
				rset = (ResultSet) callstm.getObject(1);
			} else if (oracledb.equals("N")) {
				int award_id = Integer.parseInt(awardId);
				callstm = connection.prepareCall("{call get_fibi_award_unit_contact(?)}");
				callstm.setInt(1, award_id);
				callstm.execute();
				rset = callstm.getResultSet();
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("unit_administrator_type_code", rset.getString("unit_administrator_type_code"));
				detailsField.put("unit_administrator_type", rset.getString("unit_administrator_type"));
				detailsField.put("person_id", rset.getString("person_id"));
				detailsField.put("full_name", rset.getString("full_name"));
				detailsField.put("email_addr", rset.getString("email_addr"));
				detailsField.put("phone_nbr", rset.getString("phone_nbr"));
				awardUnitContact.add(detailsField);
			}
			awardDetailsVO.setAwardUnitContact(awardUnitContact);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getFundedProposalsDetails(String awardId, AwardDetailsVO awardDetailsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement callstm = null;
		ResultSet rset = null;
		List<HashMap<String, Object>> awardFundedProposals = new ArrayList<HashMap<String, Object>>();
		try {
			if (oracledb.equals("Y")) {
				String procedureName = "get_fibi_award_funded_proposal";
				String functionCall = "{call " + procedureName + "(?,?)}";
				callstm = connection.prepareCall(functionCall);
				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, awardId);
				callstm.execute();
				rset = (ResultSet) callstm.getObject(1);
			} else if (oracledb.equals("N")) {
				int award_id = Integer.parseInt(awardId);
				callstm = connection.prepareCall("{call get_fibi_award_funded_proposal(?)}");
				callstm.setInt(1, award_id);
				callstm.execute();
				rset = callstm.getResultSet();
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("proposal_number", rset.getString("proposal_number"));
				detailsField.put("pi", rset.getString("pi"));
				detailsField.put("lead_unit_number", rset.getString("lead_unit_number"));
				detailsField.put("unit_name", rset.getString("unit_name"));
				detailsField.put("sponsor_code", rset.getString("sponsor_code"));
				detailsField.put("sponsor_name", rset.getString("sponsor_name"));
				detailsField.put("start_date", rset.getString("start_date"));
				detailsField.put("end_date", rset.getString("end_date"));
				detailsField.put("total_direct_cost_total", rset.getString("total_direct_cost_total"));
				awardFundedProposals.add(detailsField);
			}
			awardDetailsVO.setAwardFundedProposals(awardFundedProposals);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getSpecialReviewsDetails(String awardId, AwardDetailsVO awardDetailsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement callstm = null;
		ResultSet rset = null;
		try {
			if (oracledb.equals("Y")) {
				String procedureName = "get_fibi_award_special_review";
				String functionCall = "{call " + procedureName + "(?,?)}";
				callstm = connection.prepareCall(functionCall);
				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, awardId);
				callstm.execute();
				rset = (ResultSet) callstm.getObject(1);
			} else if (oracledb.equals("N")) {
				int award_id = Integer.parseInt(awardId);
				callstm = connection.prepareCall("{call get_fibi_award_special_review(?)}");
				callstm.setInt(1, award_id);
				callstm.execute();
				rset = callstm.getResultSet();
			}
			List<HashMap<String, Object>> awardSpecialReviews = new ArrayList<HashMap<String, Object>>();
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("special_review_code", rset.getString("special_review_code"));
				detailsField.put("special_review_type", rset.getString("special_review_type"));
				detailsField.put("approval_type_code", rset.getString("approval_type_code"));
				detailsField.put("description", rset.getString("description"));
				detailsField.put("approval_status", rset.getString("approval_status"));
				detailsField.put("protocol_number", rset.getString("protocol_number"));
				detailsField.put("application_date", rset.getString("application_date"));
				detailsField.put("approval_date", rset.getString("approval_date"));
				detailsField.put("expiration_date", rset.getString("expiration_date"));
				awardSpecialReviews.add(detailsField);
			}
			awardDetailsVO.setAwardSpecialReviews(awardSpecialReviews);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getApprovdEquipmentDetails(String awardId, AwardTermsAndReportsVO awardTermsAndReportsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> awardApprovdEquipment = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_approvd_equpmnt(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_approvd_equpmnt";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("item", rset.getString("item"));
				detailsField.put("vendor", rset.getString("vendor"));
				detailsField.put("model", rset.getString("model"));
				detailsField.put("amount", rset.getString("amount"));
				awardApprovdEquipment.add(detailsField);
			}
			awardTermsAndReportsVO.setAwardApprovdEquipment(awardApprovdEquipment);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getApprovedTravelDetails(String awardId, AwardTermsAndReportsVO awardTermsAndReportsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> approvedTravel = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_approvd_travel(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_approvd_travel";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("traveler_name", rset.getString("traveler_name"));
				detailsField.put("destination", rset.getString("destination"));
				detailsField.put("start_date", rset.getString("start_date"));
				detailsField.put("end_date", rset.getString("end_date"));
				detailsField.put("amount", rset.getString("amount"));
				approvedTravel.add(detailsField);
			}
			awardTermsAndReportsVO.setApprovedTravel(approvedTravel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getTermsDetails(String awardId, AwardTermsAndReportsVO awardTermsAndReportsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Map<String, List<HashMap<String, Object>>> awardTerms = new HashMap<String, List<HashMap<String, Object>>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_terms(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_terms";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("sponsor_term_type_code", rset.getString("sponsor_term_type_code"));
				detailsField.put("sponsor_term_type", rset.getString("sponsor_term_type"));
				detailsField.put("sponsor_term", rset.getString("sponsor_term"));
				List<HashMap<String, Object>> details = new ArrayList<HashMap<String, Object>>();
				details.add(detailsField);
				if (!awardTerms.containsKey(rset.getString("sponsor_term_type"))) {
					awardTerms.put(rset.getString("sponsor_term_type"), details);
				} else {
					awardTerms.get(rset.getString("sponsor_term_type")).add(detailsField);
				}
			}
			awardTermsAndReportsVO.setAwardTerms(awardTerms);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getPaymentDetails(String awardId, AwardTermsAndReportsVO awardTermsAndReportsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> awardPayment = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_payment(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_payment";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("basis_of_payment_code", rset.getString("basis_of_payment_code"));
				detailsField.put("payment_basis", rset.getString("payment_basis"));
				detailsField.put("method_of_payment_code", rset.getString("method_of_payment_code"));
				detailsField.put("payment_method", rset.getString("payment_method"));
				awardPayment.add(detailsField);
			}
			awardTermsAndReportsVO.setAwardPayment(awardPayment);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getReportDetails(String awardId, AwardTermsAndReportsVO awardTermsAndReportsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Map<String, List<HashMap<String, Object>>> awardReport = new HashMap<String, List<HashMap<String, Object>>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_report(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_report";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("report_class_code", rset.getString("report_class_code"));
				detailsField.put("report_class", rset.getString("report_class"));
				detailsField.put("report_code", rset.getString("report_code"));
				detailsField.put("report", rset.getString("report"));
				detailsField.put("frequency_code", rset.getString("frequency_code"));
				detailsField.put("frequency", rset.getString("frequency"));
				detailsField.put("frequency_base_code", rset.getString("frequency_base_code"));
				detailsField.put("frequency_base", rset.getString("frequency_base"));
				detailsField.put("osp_distribution_code", rset.getString("osp_distribution_code"));
				detailsField.put("osp_distribution", rset.getString("osp_distribution"));
				List<HashMap<String, Object>> details = new ArrayList<HashMap<String, Object>>();
				details.add(detailsField);
				if (!awardReport.containsKey(rset.getString("report_class"))) {
					awardReport.put(rset.getString("report_class"), details);
				} else {
					awardReport.get(rset.getString("report_class")).add(detailsField);
				}
			}
			awardTermsAndReportsVO.setAwardReport(awardReport);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getPaymentSchedule(String awardId, AwardTermsAndReportsVO awardTermsAndReportsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> awardPaymntSchedule = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_paymnt_schedule(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_paymnt_schedule";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("schedule_details", rset.getString("schedule_details"));
				detailsField.put("due_date", rset.getString("due_date"));
				detailsField.put("submitted_by_person_id", rset.getString("submitted_by_person_id"));
				detailsField.put("full_name", rset.getString("full_name"));
				detailsField.put("report_status_code", rset.getString("report_status_code"));
				detailsField.put("status", rset.getString("status"));
				detailsField.put("comments", rset.getString("comments"));
				detailsField.put("amount", rset.getString("amount"));
				awardPaymntSchedule.add(detailsField);
			}
			awardTermsAndReportsVO.setAwardPaymntSchedule(awardPaymntSchedule);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getPaymentAndInvoiceRequirement(String awardId, AwardTermsAndReportsVO awardTermsAndReportsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> awardPaymntInvoice = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_payment_invoice(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_payment_invoice";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("report_class_code", rset.getString("report_class_code"));
				detailsField.put("report_class", rset.getString("report_class"));
				detailsField.put("report_code", rset.getString("report_code"));
				detailsField.put("report", rset.getString("report"));
				detailsField.put("frequency_code", rset.getString("frequency_code"));
				detailsField.put("frequency", rset.getString("frequency"));
				detailsField.put("frequency_base_code", rset.getString("frequency_base_code"));
				detailsField.put("frequency_base", rset.getString("frequency_base"));
				detailsField.put("osp_distribution_code", rset.getString("osp_distribution_code"));
				detailsField.put("osp_distribution", rset.getString("osp_distribution"));
				detailsField.put("due_date", rset.getString("due_date"));
				awardPaymntInvoice.add(detailsField);
			}
			awardTermsAndReportsVO.setAwardPaymntInvoice(awardPaymntInvoice);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getHierarchyDetails(String awardNumber, AwardHierarchyVO awardHierarchyVO, String selectedAwardNumber) {
		AwardHierarchy returnAwardHierarchy = new AwardHierarchy();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection con = sessionImpl.connection();
		CallableStatement callstm = null;
		ResultSet rset = null;
		HashMap<String, AwardHierarchy> hmAwards = new HashMap<String, AwardHierarchy>();
		AwardHierarchy parentHierarchy = new AwardHierarchy();
		try {
			if (oracledb.equalsIgnoreCase("Y")) {
				String aProcedureName = "get_fibi_award_hierarchy";
				String functionCall = "{call " + aProcedureName + "(?,?)}";
				callstm = con.prepareCall(functionCall);
				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, awardNumber);
				callstm.execute();
				rset = (ResultSet) callstm.getObject(1);
			} else if (oracledb.equalsIgnoreCase("N")) {
				callstm = con.prepareCall("{call get_fibi_award_hierarchy(?)}");
				callstm.setString(1, awardNumber);
				callstm.execute();
				rset = callstm.getResultSet();
			}
			while (rset.next()) {
				AwardHierarchy awardHierarchy = new AwardHierarchy();
				awardHierarchy.setAwardNumber((String) rset.getString("award_number"));
				awardHierarchy.setSelected(
						selectedAwardNumber.equalsIgnoreCase((String) rset.getString("award_number")) ? true : false);
				awardHierarchy.setOpen(true);
				awardHierarchy.setParentAwardNumber((String) rset.getString("parent_award_number"));
				awardHierarchy.setPrincipalInvestigator((String) rset.getString("pi_name"));
				awardHierarchy.setAccountNumber((String) rset.getString("account_number"));
				awardHierarchy.setStatusCode(Integer.parseInt((rset.getString("status_code").toString())));
				awardHierarchy.setAwardId(rset.getString("award_id"));
				awardHierarchy.setRootAwardNumber(rset.getString("root_award_number"));
				String accountNumber;
				String piName;
				if ((String) rset.getString("account_number") == null) {
					accountNumber = "";
				} else {
					accountNumber = " : " + (String) rset.getString("account_number");
				}
				if ((String) rset.getString("pi_name") == null) {
					piName = "";
				} else {
					piName = " : " + (String) rset.getString("pi_name");
				}
				awardHierarchy.setName((String) rset.getString("award_number") + accountNumber + piName);
				awardHierarchy.setLevel(Integer.valueOf(rset.getString("level")));

				if (!hmAwards.isEmpty()) {
					parentHierarchy = hmAwards.get((String) rset.getString("parent_award_number"));
					if (parentHierarchy != null) {
						ArrayList<AwardHierarchy> alist = parentHierarchy.getChildren();
						if (alist == null) {
							alist = new ArrayList<AwardHierarchy>();
						}
						alist.add(awardHierarchy);
						parentHierarchy.setChildren(alist);
					}
				}
				hmAwards.put((String) rset.getString("award_number"), awardHierarchy);
			}
			returnAwardHierarchy = hmAwards.get(awardNumber);
			awardHierarchyVO.setAwardHierarchy(returnAwardHierarchy);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getBudgetVersionDetails(String awardNumber, AwardHierarchyVO hierarchyVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection con = sessionImpl.connection();
		CallableStatement callstm = null;
		String aProcedureName = "get_fibi_award_budget_versions";
		String functionCall = "{call " + aProcedureName + "(?,?)}";
		try {
			callstm = con.prepareCall(functionCall);
			callstm.registerOutParameter(1, OracleTypes.CURSOR);
			callstm.setString(2, awardNumber);
			callstm.execute();

			ResultSet rset = (ResultSet) callstm.getObject(1);
			List<HashMap<String, Object>> hierarchyBudgetVersions = new ArrayList<HashMap<String, Object>>();
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("version_number", rset.getString("version_number"));
				detailsField.put("budget_start", rset.getString("budget_start"));
				detailsField.put("budget_end", rset.getString("budget_end"));
				detailsField.put("award_budget_type_code", rset.getString("award_budget_type_code"));
				detailsField.put("budget_type", rset.getString("budget_type"));
				detailsField.put("on_off_campus", rset.getString("on_off_campus"));
				detailsField.put("direct_cost", rset.getString("direct_cost"));
				detailsField.put("total_indirect_cos", rset.getString("total_indirect_cos"));
				detailsField.put("total_cost", rset.getString("total_cost"));
				detailsField.put("fa_rate_type", rset.getString("fa_rate_type"));
				detailsField.put("last_updated", rset.getString("last_updated"));
				hierarchyBudgetVersions.add(detailsField);
			}
			hierarchyVO.setHierarchyBudgetVersions(hierarchyBudgetVersions);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getCostShareData(String awardId, CommitmentsVO commitmentsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> costShareDetails = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_cost_share(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_cost_share";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("cost_share_percentage", rset.getString("cost_share_percentage"));
				detailsField.put("cost_share_type_code", rset.getString("cost_share_type_code"));
				detailsField.put("cost_share_type", rset.getString("cost_share_type"));
				detailsField.put("project_period", rset.getString("project_period"));
				detailsField.put("commitment_amount", rset.getString("commitment_amount"));
				costShareDetails.add(detailsField);
			}
			commitmentsVO.setCostShareDetails(costShareDetails);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getFandAData(String awardId, CommitmentsVO commitmentsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> fAndADetails = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_fa_rates(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_fa_rates";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("applicable_idc_rate", rset.getString("applicable_idc_rate"));
				detailsField.put("idc_rate_type_code", rset.getString("idc_rate_type_code"));
				detailsField.put("idc_rate_type", rset.getString("idc_rate_type"));
				detailsField.put("fiscal_year", rset.getString("fiscal_year"));
				detailsField.put("start_date", rset.getString("start_date"));
				detailsField.put("campus", rset.getString("campus"));
				fAndADetails.add(detailsField);
			}
			commitmentsVO.setfAndADetails(fAndADetails);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getBenefitsRates(String awardId, CommitmentsVO commitmentsVO) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		List<HashMap<String, Object>> benefitsRates = new ArrayList<HashMap<String, Object>>();
		SessionImpl sessionImpl = (SessionImpl) session;
		Connection connection = sessionImpl.connection();
		CallableStatement statement = null;
		ResultSet rset = null;
		try {
			if (oracledb.equalsIgnoreCase("N")) {
				int award_id = Integer.parseInt(awardId);
				statement = connection.prepareCall("{call get_fibi_award_benefits_rates(?)}");
				statement.setInt(1, award_id);
				statement.execute();
				rset = statement.getResultSet();
			} else if (oracledb.equalsIgnoreCase("Y")) {
				String procedureName = "get_fibi_award_benefits_rates";
				String functionCall = "{call " + procedureName + "(?,?)}";
				statement = connection.prepareCall(functionCall);
				statement.registerOutParameter(1, OracleTypes.CURSOR);
				statement.setString(2, awardId);
				statement.execute();
				rset = (ResultSet) statement.getObject(1);
			}
			while (rset.next()) {
				HashMap<String, Object> detailsField = new HashMap<String, Object>();
				detailsField.put("award_id", rset.getString("award_id"));
				detailsField.put("award_number", rset.getString("award_number"));
				detailsField.put("off_campus", rset.getString("off_campus"));
				detailsField.put("on_campus", rset.getString("on_campus"));
				detailsField.put("comments", rset.getString("comments"));
				benefitsRates.add(detailsField);
			}
			commitmentsVO.setBenefitsRates(benefitsRates);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
