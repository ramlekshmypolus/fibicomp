package com.polus.fibicomp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.pojo.PersonDTO;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.DisclosureView;
import com.polus.fibicomp.view.IacucView;
import com.polus.fibicomp.view.ProposalView;
import com.polus.fibicomp.view.ProtocolView;
import com.polus.fibicomp.view.ResearchSummaryView;

import oracle.jdbc.driver.OracleTypes;

@Transactional
@Service(value = "dashboardDao")
@PropertySource("classpath:application.properties")
public class DashboardDaoImpl implements DashboardDao {

	protected static Logger logger = Logger.getLogger(DashboardDaoImpl.class.getName());

	@Value("${oracledb}")
	private String oracledb;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public String getDashBoardResearchSummary1(PersonDTO personDTO) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			// Session session =
			// hibernateTemplate.getSessionFactory().getCurrentSession();
			Session session = hibernateTemplate.getSessionFactory().openSession();
			ArrayList<HashMap<String, Object>> dashboardResearchMap = new ArrayList<HashMap<String, Object>>();
			if (oracledb.equals("Y")) {
				ResultSet resultSet = null;
				String aProcedureName = "GET_MY_RESEARCH_SUMMARY";
				String functionCall = "{call " + aProcedureName + "(?, ?)}";
				Connection aConnection = ((SessionImpl) session).connection();
				CallableStatement callstm = null;
				callstm = aConnection.prepareCall(functionCall);
				// callstm.registerOutParameter(1, OracleTypes.CURSOR );
				callstm.setString(1, personDTO.getPersonID());
				callstm.registerOutParameter(2, OracleTypes.CURSOR);
				callstm.execute();
				resultSet = (ResultSet) callstm.getObject(2);
				System.out.println("resultSet : " + resultSet);
				java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
				int colCount = metaData.getColumnCount();
				String colName = null;
				Object colValue = null;
				while (resultSet.next()) {
					HashMap<String, Object> htRow = new HashMap<>();
					for (int i = 1; i <= colCount; i++) {
						colName = metaData.getColumnName(i);
						colValue = resultSet.getObject(i);
						htRow.put(colName, colValue);
					}
					dashboardResearchMap.add(htRow);
				}
				System.out.println("dashboardResearchMap : " + dashboardResearchMap);

			} else {
				Query query = session.createSQLQuery("CALL GET_MY_RESEARCH_SUMMARY(:av_person_id)");
				query.setString("av_person_id", personDTO.getPersonID());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<Object> dashBoardResearchMap = query.list();
				System.out.println("\n dashboardResearchMap : " + dashBoardResearchMap);
				for (Object dashboardResearchDetail : dashBoardResearchMap) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					Map row = (Map) dashboardResearchDetail;
					fieldMap.put("RESEARCH_TYPE", row.get("research_type"));
					fieldMap.put("RESEARCH_COUNT", row.get("research_count"));
					fieldMap.put("TOTAL_AMOUNT", row.get("total_amount"));
					fieldMap.put("SORT_ORDER", row.get("sort_order"));
					dashboardResearchMap.add(fieldMap);
				}
			}
			dashBoardProfile.setDashBoardResearchSummaryMap(dashboardResearchMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method getDashBoardResearchSummary");
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	public String getDashBoardResearchSummary(PersonDTO personDTO) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			logger.info("getDashBoardResearchSummary");
			ArrayList<HashMap<String, Object>> dashboardResearchMap = new ArrayList<HashMap<String, Object>>();
			List<ResearchSummaryView> dashboardResearchMapdetails = hibernateTemplate
					.loadAll(ResearchSummaryView.class);
			if (dashboardResearchMapdetails != null && !dashboardResearchMapdetails.isEmpty()) {
				for (ResearchSummaryView dashboardResearchDetail : dashboardResearchMapdetails) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					fieldMap.put("RESEARCH_TYPE", dashboardResearchDetail.getResearchType());
					fieldMap.put("RESEARCH_COUNT", dashboardResearchDetail.getResearchCount());
					fieldMap.put("TOTAL_AMOUNT", dashboardResearchDetail.getTotalAmount());
					fieldMap.put("SORT_ORDER", dashboardResearchDetail.getSortOrder());
					dashboardResearchMap.add(fieldMap);
				}
			}
			//logger.info("DashboardResearchMap Details : " + dashboardResearchMap);
			dashBoardProfile.setDashBoardResearchSummaryMap(dashboardResearchMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method getDashBoardResearchSummary");
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	public DashBoardProfile getDashBoardDataForAward1(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			dashBoardProfile.setTotalServiceRequest(getDashBoardCount(personDTO, requestType, pageNumber));
			// Session session =
			// hibernateTemplate.getSessionFactory().getCurrentSession();
			Session session = hibernateTemplate.getSessionFactory().openSession();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();

			if (oracledb.equals("Y")) {
				ResultSet resultSet = null;
				String aProcedureName = "GET_AWARDS_FOR_DASHBOARD";
				String functionCall = "{call " + aProcedureName + "(?, ?, ?, ?, ?, ?, ?)}";
				Connection aConnection = ((SessionImpl) session).connection();
				CallableStatement callstm = null;
				callstm = aConnection.prepareCall(functionCall);

				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, personDTO.getPersonID());
				callstm.setLong(3, pageNumber - 30 + 1);
				callstm.setLong(4, pageNumber);
				callstm.setString(5, sortBy);
				callstm.setString(6, reverse);
				callstm.setString(7, "");
				callstm.execute();
				resultSet = (ResultSet) callstm.getObject(1);
				System.out.println("resultSet : " + resultSet);
				java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
				int colCount = metaData.getColumnCount();
				String colName = null;
				Object colValue = null;
				// ArrayList<HashMap<String,Object>> alResSet = new
				// ArrayList<>();
				List<String> fields = new ArrayList<String>();
				fields.add("AWARD_NUMBER");
				fields.add("ACCOUNT_NUMBER");
				fields.add("TITLE");
				fields.add("SPONSOR");
				fields.add("PI");
				fields.add("UNIT_NAME");
				while (resultSet.next()) {
					HashMap<String, Object> htRow = new HashMap<>();
					for (int i = 1; i <= colCount; i++) {
						colName = metaData.getColumnName(i);
						// int colDatatype = metaData.getColumnType(i);
						colValue = resultSet.getObject(i);
						if (fields.contains(colName)) {
							htRow.put(colName, colValue);
						}
					}
					// alResSet.add(htRow);
					dashBoardDetailsMap.add(htRow);
				}
				// System.out.println("alResSet : " + alResSet);
				System.out.println("dashBoardDetailsMap : " + dashBoardDetailsMap);
			} else {
				Query query = session.createSQLQuery(
						"CALL GET_AWARDS_FOR_DASHBOARD(:av_person_id,:av_from_row,:av_to_row,:av_sort_column,:av_sort_order,:av_search_field)");
				query.setString("av_person_id", personDTO.getPersonID());
				query.setLong("av_from_row", pageNumber - 30 + 1);
				query.setLong("av_to_row", pageNumber);
				query.setString("av_sort_column", sortBy);
				query.setString("av_sort_order", reverse);
				query.setString("av_search_field", "");
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<Object> awards = query.list();
				System.out.println("\n awards : " + awards);
				for (Object award : awards) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					Map row = (Map) award;
					fieldMap.put("AWARD_NUMBER", row.get("AWARD_NUMBER"));
					fieldMap.put("ACCOUNT_NUMBER", row.get("ACCOUNT_NUMBER"));
					fieldMap.put("TITLE", row.get("TITLE"));
					fieldMap.put("SPONSOR", row.get("SPONSOR"));
					fieldMap.put("PI", row.get("PI"));
					fieldMap.put("UNIT_NAME", row.get("UNIT_NAME"));
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = dashBoardDetailsMap.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 10) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 20) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = dashBoardDetailsMap.size();
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method getDashBoardDataForAward", e);
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForAward(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			logger.info("getDashBoardDataForAward");
			dashBoardProfile.setTotalServiceRequest(getDashBoardCount(personDTO, requestType, pageNumber));
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(AwardView.class);
			if (sortBy.isEmpty()) {
				criteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				criteria.addOrder(Order.desc(sortBy));
			}
			criteria.setFirstResult(pageNumber - 30 + 1);
			criteria.setMaxResults(pageNumber);
			List<AwardView> awards = criteria.list();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
			// List<AwardView> awards =
			// hibernateTemplate.loadAll(AwardView.class);
			if (awards != null && !awards.isEmpty()) {
				for (AwardView award : awards) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					fieldMap.put("AWARD_NUMBER", award.getAwardNumber());
					fieldMap.put("ACCOUNT_NUMBER", award.getAccountNumber());
					fieldMap.put("TITLE", award.getTitle());
					fieldMap.put("SPONSOR", award.getSponsor());
					fieldMap.put("PI", award.getPi());
					fieldMap.put("UNIT_NAME", award.getUnitName());
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			//logger.info("Award DashBoardDetailsMap : " + dashBoardDetailsMap);
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = dashBoardDetailsMap.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 10) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 20) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = dashBoardDetailsMap.size();
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForAward", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForProposal1(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			dashBoardProfile.setTotalServiceRequest(getDashBoardCount(personDTO, requestType, pageNumber));
			Session session = hibernateTemplate.getSessionFactory().openSession();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
			if (oracledb.equals("Y")) {
				ResultSet resultSet = null;
				String aProcedureName = "GET_PROPOSALS_FOR_DASHBOARD";
				String functionCall = "{call " + aProcedureName + "(?, ?, ?, ?, ?, ?, ?)}";
				Connection aConnection = ((SessionImpl) session).connection();
				CallableStatement callstm = null;
				callstm = aConnection.prepareCall(functionCall);

				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, personDTO.getPersonID());
				callstm.setLong(3, pageNumber - 30 + 1);
				callstm.setLong(4, pageNumber);
				callstm.setString(5, sortBy);
				callstm.setString(6, reverse);
				callstm.setString(7, "");
				callstm.execute();
				resultSet = (ResultSet) callstm.getObject(1);
				System.out.println("resultSet : " + resultSet);
				java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
				int colCount = metaData.getColumnCount();
				String colName = null;
				Object colValue = null;
				// ArrayList<HashMap<String,Object>> alResSet = new
				// ArrayList<>();
				List<String> fields = new ArrayList<String>();
				fields.add("PROPOSAL_NUMBER");
				fields.add("TITLE");
				fields.add("LEAD_UNIT");
				fields.add("SPONSOR");
				fields.add("DEADLINE_DATE");
				fields.add("STATUS");
				while (resultSet.next()) {
					HashMap<String, Object> htRow = new HashMap<>();
					for (int i = 1; i <= colCount; i++) {
						colName = metaData.getColumnName(i);
						// int colDatatype = metaData.getColumnType(i);
						colValue = resultSet.getObject(i);
						if (fields.contains(colName)) {
							htRow.put(colName, colValue);
						}
					}
					// alResSet.add(htRow);
					dashBoardDetailsMap.add(htRow);
				}
				// System.out.println("alResSet : " + alResSet);
				System.out.println("dashBoardDetailsMap : " + dashBoardDetailsMap);

			} else {
				Query query = session.createSQLQuery(
						"CALL GET_PROPOSALS_FOR_DASHBOARD(:av_person_id,:av_from_row,:av_to_row,:av_sort_column,:av_sort_order,:av_search_field)");
				query.setString("av_person_id", personDTO.getPersonID());
				query.setLong("av_from_row", pageNumber - 30 + 1);
				query.setLong("av_to_row", pageNumber);
				query.setString("av_sort_column", sortBy);
				query.setString("av_sort_order", reverse);
				query.setString("av_search_field", null);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<Object> proposals = query.list();
				System.out.println("\n proposals : " + proposals);
				for (Object proposal : proposals) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					Map row = (Map) proposal;
					fieldMap.put("PROPOSAL_NUMBER", row.get("PROPOSAL_NUMBER"));
					fieldMap.put("TITLE", row.get("TITLE"));
					fieldMap.put("LEAD_UNIT", row.get("LEAD_UNIT"));
					fieldMap.put("SPONSOR", row.get("SPONSOR"));
					fieldMap.put("DEADLINE_DATE", row.get("DEADLINE_DATE"));
					fieldMap.put("STATUS", row.get("STATUS"));
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = dashBoardDetailsMap.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 10) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 20) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = dashBoardDetailsMap.size();
			// ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new
			// ArrayList<HashMap<String, Object>>();

			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method getDashBoardDataForProposal", e);
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForProposal(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			logger.info("getDashBoardDataForProposal");
			dashBoardProfile.setTotalServiceRequest(getDashBoardCount(personDTO, requestType, pageNumber));
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(ProposalView.class);
			if (sortBy.isEmpty()) {
				criteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				criteria.addOrder(Order.desc(sortBy));
			}
			criteria.setFirstResult(pageNumber - 30 + 1);
			criteria.setMaxResults(pageNumber);
			List<ProposalView> proposals = criteria.list();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
			// List<ProposalView> proposals =
			// hibernateTemplate.loadAll(ProposalView.class);
			if (proposals != null && !proposals.isEmpty()) {
				for (ProposalView proposal : proposals) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					fieldMap.put("PROPOSAL_NUMBER", proposal.getProposalNumber());
					fieldMap.put("TITLE", proposal.getTitle());
					fieldMap.put("LEAD_UNIT", proposal.getLeadUnit());
					fieldMap.put("SPONSOR", proposal.getSponsor());
					fieldMap.put("DEADLINE_DATE", proposal.getDeadLinedate());
					fieldMap.put("STATUS", proposal.getStatus());
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			//logger.info("Proposal DashBoardDetailsMap" + dashBoardDetailsMap);
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = proposals.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 10) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 20) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = proposals.size();
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForProposal", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getProtocolDashboardData1(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			Integer dashboardCount = getDashBoardCount(personDTO, requestType, pageNumber);
			System.out.println("\n\n dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount);
			Session session = hibernateTemplate.getSessionFactory().openSession();
			Query query = null;
			if (oracledb.equals("Y")) {
				query = session.getNamedQuery("getIRBDashboard");
			} else {
				query = session.createSQLQuery(
						"CALL GET_IRB_FOR_DASHBOARD(:av_person_id,:av_from_row,:av_to_row,:av_sort_column,:av_sort_order,:av_search_field)");
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			}
			query.setString("av_person_id", personDTO.getPersonID());
			query.setLong("av_from_row", pageNumber - 30);
			query.setLong("av_to_row", pageNumber);
			query.setString("av_sort_column", "update_timestamp");
			query.setString("av_sort_order", "");
			query.setString("av_search_field", "");
			List<Object> objects = query.list();
			System.out.println("\n protocols : " + objects);
			if (objects != null && !objects.isEmpty()) {
				List<Integer> pageNumbers = new ArrayList<Integer>();
				Integer countOfRecords = objects.size();
				Integer startCountCal = ((pageNumber - 30) + 10) / 10;
				if (countOfRecords > 0) {
					pageNumbers.add(startCountCal);
				}
				if (countOfRecords > 10) {
					startCountCal = startCountCal + 1;
					pageNumbers.add(startCountCal);
				}
				if (countOfRecords > 20) {
					startCountCal = startCountCal + 1;
					pageNumbers.add(startCountCal);
				}
				if (pageNumbers.size() != 1) {
					dashBoardProfile.setPageNumbers(pageNumbers);
				}
				Integer serviceRequestCount = objects.size();
				ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
				if (oracledb.equals("Y")) {
					for (Object object : objects) {
						Protocol protocol = (Protocol) object;
						HashMap<String, Object> fieldMap = new HashMap<String, Object>();
						fieldMap.put("PROTOCOL_NUMBER", protocol.getProtocolNumber());
						fieldMap.put("TITLE", protocol.getTitle());
						fieldMap.put("LEAD_UNIT", "MEDICINE DEPT");
						fieldMap.put("PROTOCOL_TYPE", protocol.getProtocolType().getDescription());
						fieldMap.put("STATUS", protocol.getProtocolStatus().getDescription());
						dashBoardDetailsMap.add(fieldMap);
					}
				} else {
					for (Object protocol : objects) {
						HashMap<String, Object> fieldMap = new HashMap<String, Object>();
						Map row = (Map) protocol;
						fieldMap.put("PROTOCOL_NUMBER", row.get("PROTOCOL_NUMBER"));
						fieldMap.put("TITLE", row.get("TITLE"));
						fieldMap.put("LEAD_UNIT", row.get("LEAD_UNIT"));
						fieldMap.put("PROTOCOL_TYPE", row.get("PROTOCOL_TYPE"));
						fieldMap.put("STATUS", row.get("STATUS"));
						dashBoardDetailsMap.add(fieldMap);
					}
				}
				dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
				dashBoardProfile.setServiceRequestCount(serviceRequestCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method getProtocolDashboardData", e);
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getProtocolDashboardData(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			logger.info("getProtocolDashboardData");
			Integer dashboardCount = getDashBoardCount(personDTO, requestType, pageNumber);
			logger.info("\n\n dashboardCount : " + dashboardCount);
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(ProtocolView.class);
			if (sortBy.isEmpty()) {
				criteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				criteria.addOrder(Order.desc(sortBy));
			}
			criteria.setFirstResult(pageNumber - 30 + 1);
			criteria.setMaxResults(pageNumber);

			List<ProtocolView> protocols = criteria.list();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
			if (protocols != null && !protocols.isEmpty()) {
				for (ProtocolView protocol : protocols) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					fieldMap.put("PROTOCOL_NUMBER", protocol.getProtocolNumber());
					fieldMap.put("TITLE", protocol.getTitle());
					fieldMap.put("LEAD_UNIT", protocol.getLeadUnit());
					fieldMap.put("PROTOCOL_TYPE", protocol.getProtocolType());
					fieldMap.put("STATUS", protocol.getStatus());
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			//logger.info("Protocol DashBoardDetailsMap" + dashBoardDetailsMap);
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = dashBoardDetailsMap.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 10) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 20) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = dashBoardDetailsMap.size();
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);
		} catch (Exception e) {
			logger.error("Error in method getProtocolDashboardData", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForIacuc1(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			dashBoardProfile.setTotalServiceRequest(getDashBoardCount(personDTO, requestType, pageNumber));
			Session session = hibernateTemplate.getSessionFactory().openSession();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
			if (oracledb.equals("Y")) {
				ResultSet resultSet = null;
				String aProcedureName = "GET_IACUC_FOR_DASHBOARD";
				String functionCall = "{call " + aProcedureName + "(?, ?, ?, ?, ?, ?, ?)}";
				Connection aConnection = ((SessionImpl) session).connection();
				CallableStatement callstm = null;
				callstm = aConnection.prepareCall(functionCall);

				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, personDTO.getPersonID());
				callstm.setLong(3, pageNumber - 30 + 1);
				callstm.setLong(4, pageNumber);
				callstm.setString(5, sortBy);
				callstm.setString(6, reverse);
				callstm.setString(7, "");
				callstm.execute();
				resultSet = (ResultSet) callstm.getObject(1);
				System.out.println("resultSet : " + resultSet);
				java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
				int colCount = metaData.getColumnCount();
				String colName = null;
				Object colValue = null;
				// ArrayList<HashMap<String,Object>> alResSet = new
				// ArrayList<>();
				List<String> fields = new ArrayList<String>();
				fields.add("PROTOCOL_NUMBER");
				fields.add("TITLE");
				fields.add("LEAD_UNIT");
				fields.add("PROTOCOL_TYPE");
				fields.add("STATUS");
				while (resultSet.next()) {
					HashMap<String, Object> htRow = new HashMap<>();
					for (int i = 1; i <= colCount; i++) {
						colName = metaData.getColumnName(i);
						// int colDatatype = metaData.getColumnType(i);
						colValue = resultSet.getObject(i);
						if (fields.contains(colName)) {
							htRow.put(colName, colValue);
						}
					}
					// alResSet.add(htRow);
					dashBoardDetailsMap.add(htRow);
				}
				// System.out.println("alResSet : " + alResSet);
				System.out.println("dashBoardDetailsMap : " + dashBoardDetailsMap);
			} else {
				Query query = session.createSQLQuery(
						"CALL GET_IACUC_FOR_DASHBOARD(:av_person_id,:av_from_row,:av_to_row,:av_sort_column,:av_sort_order,:av_search_field)");
				query.setString("av_person_id", personDTO.getPersonID());
				query.setLong("av_from_row", pageNumber - 30 + 1);
				query.setLong("av_to_row", pageNumber);
				query.setString("av_sort_column", sortBy);
				query.setString("av_sort_order", reverse);
				query.setString("av_search_field", "");
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<Object> iacuc_protocols = query.list();
				System.out.println("\n iacuc : " + iacuc_protocols);
				for (Object iacuc : iacuc_protocols) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					Map row = (Map) iacuc;
					fieldMap.put("PROTOCOL_NUMBER", row.get("PROTOCOL_NUMBER"));
					fieldMap.put("TITLE", row.get("TITLE"));
					fieldMap.put("LEAD_UNIT", row.get("LEAD_UNIT"));
					fieldMap.put("PROTOCOL_TYPE", row.get("PROTOCOL_TYPE"));
					fieldMap.put("STATUS", row.get("STATUS"));
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = dashBoardDetailsMap.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 10) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 20) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = dashBoardDetailsMap.size();
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method getDashBoardDataForAward", e);
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForIacuc(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			logger.info("getDashBoardDataForIacuc");
			dashBoardProfile.setTotalServiceRequest(getDashBoardCount(personDTO, requestType, pageNumber));
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(IacucView.class);
			if (sortBy.isEmpty()) {
				criteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				criteria.addOrder(Order.desc(sortBy));
			}
			criteria.setFirstResult(pageNumber - 30 + 1);
			criteria.setMaxResults(pageNumber);
			List<IacucView> iacucProtocols = criteria.list();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
			if (iacucProtocols != null && !iacucProtocols.isEmpty()) {
				for (IacucView iacucProtocol : iacucProtocols) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					fieldMap.put("PROTOCOL_NUMBER", iacucProtocol.getProtocolNumber());
					fieldMap.put("TITLE", iacucProtocol.getTitle());
					fieldMap.put("LEAD_UNIT", iacucProtocol.getLeadUnit());
					fieldMap.put("PROTOCOL_TYPE", iacucProtocol.getProtocolType());
					fieldMap.put("STATUS", iacucProtocol.getStatus());
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			//logger.info("IACUC DashBoardDetailsMap" + dashBoardDetailsMap);
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = dashBoardDetailsMap.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 10) {
				pageNumbers.add(startCountCal);
				startCountCal = startCountCal + 1;
			}
			if (countOfRecords > 20) {
				pageNumbers.add(startCountCal);
				startCountCal = startCountCal + 1;
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = dashBoardDetailsMap.size();
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForIacuc");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForDisclosures1(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			dashBoardProfile.setTotalServiceRequest(getDashBoardCount(personDTO, requestType, pageNumber));
			Session session = hibernateTemplate.getSessionFactory().openSession();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
			if (oracledb.equals("Y")) {
				ResultSet resultSet = null;
				String aProcedureName = "GET_AWARDS_FOR_DASHBOARD";
				String functionCall = "{call " + aProcedureName + "(?, ?, ?, ?, ?, ?, ?)}";
				Connection aConnection = ((SessionImpl) session).connection();
				CallableStatement callstm = null;
				callstm = aConnection.prepareCall(functionCall);

				callstm.registerOutParameter(1, OracleTypes.CURSOR);
				callstm.setString(2, personDTO.getPersonID());
				callstm.setLong(3, pageNumber - 30 + 1);
				callstm.setLong(4, pageNumber);
				callstm.setString(5, sortBy);
				callstm.setString(6, reverse);
				callstm.setString(7, "");
				callstm.execute();
				resultSet = (ResultSet) callstm.getObject(1);
				System.out.println("resultSet : " + resultSet);
				java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
				int colCount = metaData.getColumnCount();
				String colName = null;
				Object colValue = null;
				// ArrayList<HashMap<String,Object>> alResSet = new
				// ArrayList<>();
				List<String> fields = new ArrayList<String>();
				fields.add("COI_DISCLOSURE_NUMBER");
				fields.add("FULL_NAME");
				fields.add("DISCLOSURE_DISPOSITION");
				fields.add("DISCLOSURE_STATUS");
				fields.add("MODULE_ITEM_KEY");
				fields.add("EXPIRATION_DATE");
				while (resultSet.next()) {
					HashMap<String, Object> htRow = new HashMap<>();
					for (int i = 1; i <= colCount; i++) {
						colName = metaData.getColumnName(i);
						// int colDatatype = metaData.getColumnType(i);
						colValue = resultSet.getObject(i);
						if (fields.contains(colName)) {
							htRow.put(colName, colValue);
						}
					}
					// alResSet.add(htRow);
					dashBoardDetailsMap.add(htRow);
				}
				// System.out.println("alResSet : " + alResSet);
				System.out.println("dashBoardDetailsMap : " + dashBoardDetailsMap);
			} else {
				Query query = session.createSQLQuery(
						"CALL GET_DISCLOSURE_FOR_DASHBOARD(:av_person_id,:av_from_row,:av_to_row,:av_sort_column,:av_sort_order,:av_search_field)");
				query.setString("av_person_id", personDTO.getPersonID());
				query.setLong("av_from_row", pageNumber - 30 + 1);
				query.setLong("av_to_row", pageNumber);
				query.setString("av_sort_column", sortBy);
				query.setString("av_sort_order", reverse);
				query.setString("av_search_field", "");
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<Object> disclosures = query.list();
				System.out.println("\n disclosures : " + disclosures);
				for (Object disclosure : disclosures) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					Map row = (Map) disclosure;
					fieldMap.put("COI_DISCLOSURE_NUMBER", row.get("COI_DISCLOSURE_NUMBER"));
					fieldMap.put("FULL_NAME", row.get("FULL_NAME"));
					fieldMap.put("DISCLOSURE_DISPOSITION", row.get("DISCLOSURE_DISPOSITION"));
					fieldMap.put("DISCLOSURE_STATUS", row.get("DISCLOSURE_STATUS"));
					fieldMap.put("MODULE_ITEM_KEY", row.get("MODULE_ITEM_KEY"));
					fieldMap.put("EXPIRATION_DATE", row.get("EXPIRATION_DATE"));
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = dashBoardDetailsMap.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 10) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 20) {
				startCountCal = startCountCal + 1;
				pageNumbers.add(startCountCal);
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = dashBoardDetailsMap.size();
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method getDashBoardDataForAward", e);
		}
		return dashBoardProfile;
	}

	@Override
	public DashBoardProfile getDashBoardDataForDisclosures(PersonDTO personDTO, String requestType, Integer pageNumber,
			String sortBy, String reverse) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			logger.info("getDashBoardDataForDisclosures");
			dashBoardProfile.setTotalServiceRequest(getDashBoardCount(personDTO, requestType, pageNumber));
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria criteria = session.createCriteria(DisclosureView.class);
			if (sortBy.isEmpty()) {
				criteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				criteria.addOrder(Order.desc(sortBy));
			}
			criteria.setFirstResult(pageNumber - 30 + 1);
			criteria.setMaxResults(pageNumber);
			List<DisclosureView> disclosures = criteria.list();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();
			if (disclosures != null && !disclosures.isEmpty()) {
				for (DisclosureView disclosure : disclosures) {
					HashMap<String, Object> fieldMap = new HashMap<String, Object>();
					fieldMap.put("COI_DISCLOSURE_NUMBER", disclosure.getCoiDisclosureNumber());
					fieldMap.put("FULL_NAME", disclosure.getFullName());
					fieldMap.put("DISCLOSURE_DISPOSITION", disclosure.getDisclosureDisposition());
					fieldMap.put("DISCLOSURE_STATUS", disclosure.getDisclosureStatus());
					fieldMap.put("MODULE_ITEM_KEY", disclosure.getModuleItemKey());
					fieldMap.put("EXPIRATION_DATE", disclosure.getExpirationDate());
					dashBoardDetailsMap.add(fieldMap);
				}
			}
			//logger.info("Disclosure DashBoardDetailsMap" + dashBoardDetailsMap);
			List<Integer> pageNumbers = new ArrayList<Integer>();
			Integer countOfRecords = disclosures.size();
			Integer startCountCal = ((pageNumber - 30) + 10) / 10;
			if (countOfRecords > 0) {
				pageNumbers.add(startCountCal);
			}
			if (countOfRecords > 20) {
				pageNumbers.add(startCountCal);
				startCountCal = startCountCal + 1;
			}
			if (countOfRecords > 30) {
				pageNumbers.add(startCountCal);
				startCountCal = startCountCal + 1;
			}
			if (pageNumbers.size() != 1) {
				dashBoardProfile.setPageNumbers(pageNumbers);
			}
			Integer serviceRequestCount = disclosures.size();
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
			dashBoardProfile.setServiceRequestCount(serviceRequestCount);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForDisclosures");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public Integer getDashBoardCount(PersonDTO personDTO, String requestType, Integer pageNumber) {
		Integer dashBoardCount = null;
		try {
			if (pageNumber == 30) {
				Session session = hibernateTemplate.getSessionFactory().openSession();
				Query query = null;
				if (requestType.equals("AWARD")) {
					query = session.createQuery("select COUNT(*) from AwardView");
					dashBoardCount = (int) (long) query.uniqueResult();
					logger.info("AWARD dashBoardCount : " + dashBoardCount);
				}
				if (requestType.equals("PROPOSAL")) {
					query = session.createQuery("select count(*) from ProposalView");
					dashBoardCount = (int) (long) query.uniqueResult();
					logger.info("PROPOSAL dashBoardCount : " + dashBoardCount);
				}
				if (requestType.equals("IRB")) {
					query = session.createQuery("select count(*) from ProtocolView");
					dashBoardCount = (int) (long) query.uniqueResult();
					logger.info("IRB dashBoardCount : " + dashBoardCount);
				}
				if (requestType.equals("IACUC")) {
					query = session.createQuery("select count(*) from IacucView");
					dashBoardCount = (int) (long) query.uniqueResult();
					logger.info("IACUC dashBoardCount : " + dashBoardCount);
				}
				if (requestType.equals("DISCLOSURE")) {
					query = session.createQuery("select count(*) from DisclosureView");
					dashBoardCount = (int) (long) query.uniqueResult();
					logger.info("DISCLOSURE dashBoardCount : " + dashBoardCount);
				}
			}
		} catch (Exception e) {
			logger.error("Error in method getDashBoardCount");
			e.printStackTrace();
		}
		return dashBoardCount;
	}

	public String getSearchData(PersonDTO personDTO, String tabIndex, String inputData) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			ArrayList<HashMap<String, Object>> dashBoardDetailsMap = new ArrayList<HashMap<String, Object>>();

			if (tabIndex.equals("AWARD")) {
				Criteria criteria = session.createCriteria(AwardView.class);
				Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.like("awardNumber", "%" + inputData + "%"));
				or.add(Restrictions.like("accountNumber", "%" + inputData + "%"));
				or.add(Restrictions.like("title", "%" + inputData + "%"));
				or.add(Restrictions.like("sponsor", "%" + inputData + "%"));
				or.add(Restrictions.like("unitName", "%" + inputData + "%"));
				// or.add(Restrictions.eq("description","%"+inputData+"%"));
				criteria.add(or);
				List<AwardView> awards = criteria.list();
				if (awards != null && !awards.isEmpty()) {
					for (AwardView award : awards) {
						HashMap<String, Object> fieldMap = new HashMap<String, Object>();
						fieldMap.put("AWARD_NUMBER", award.getAwardNumber());
						fieldMap.put("ACCOUNT_NUMBER", award.getAccountNumber());
						fieldMap.put("TITLE", award.getTitle());
						fieldMap.put("SPONSOR", award.getSponsor());
						fieldMap.put("PI", award.getPi());
						fieldMap.put("UNIT_NAME", award.getUnitName());
						dashBoardDetailsMap.add(fieldMap);
					}
				}
				//logger.info("dashBoardDetailsMap :" + dashBoardDetailsMap);
			}
			if (tabIndex.equals("PROPOSAL")) {
				Criteria criteria = session.createCriteria(ProposalView.class);
				Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.eq("proposalNumber", "%" + inputData + "%"));
				or.add(Restrictions.eq("title", "%" + inputData + "%"));
				or.add(Restrictions.eq("sponsor", "%" + inputData + "%"));
				or.add(Restrictions.eq("leadUnit", "%" + inputData + "%"));
				// or.add(Restrictions.eq("description","%"+inputData+"%"));
				criteria.add(or);
				List<ProposalView> proposals = criteria.list();
				if (proposals != null && !proposals.isEmpty()) {
					for (ProposalView proposal : proposals) {
						HashMap<String, Object> fieldMap = new HashMap<String, Object>();
						fieldMap.put("PROPOSAL_NUMBER", proposal.getProposalNumber());
						fieldMap.put("TITLE", proposal.getTitle());
						fieldMap.put("LEAD_UNIT", proposal.getLeadUnit());
						fieldMap.put("SPONSOR", proposal.getSponsor());
						fieldMap.put("DEADLINE_DATE", proposal.getDeadLinedate());
						fieldMap.put("STATUS", proposal.getStatus());
						dashBoardDetailsMap.add(fieldMap);
					}
				}
				//logger.info("dashBoardDetailsMap :" + dashBoardDetailsMap);
			}
			if (tabIndex.equals("IRB")) {
				Criteria criteria = session.createCriteria(ProposalView.class);
				Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.eq("protocolNumber", "%" + inputData + "%"));
				or.add(Restrictions.eq("title", "%" + inputData + "%"));
				or.add(Restrictions.eq("leadUnit", "%" + inputData + "%"));
				// or.add(Restrictions.eq("description","%"+inputData+"%"));
				criteria.add(or);
				List<ProtocolView> protocols = criteria.list();
				if (protocols != null && !protocols.isEmpty()) {
					for (ProtocolView protocol : protocols) {
						HashMap<String, Object> fieldMap = new HashMap<String, Object>();
						fieldMap.put("PROTOCOL_NUMBER", protocol.getProtocolNumber());
						fieldMap.put("TITLE", protocol.getTitle());
						fieldMap.put("LEAD_UNIT", protocol.getLeadUnit());
						fieldMap.put("PROTOCOL_TYPE", protocol.getProtocolType());
						fieldMap.put("STATUS", protocol.getStatus());
						dashBoardDetailsMap.add(fieldMap);
					}
				}
				//logger.info("dashBoardDetailsMap :" + dashBoardDetailsMap);
			}
			if (tabIndex.equals("IACUC")) {
				Criteria criteria = session.createCriteria(ProposalView.class);
				Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.eq("protocolNumber", "%" + inputData + "%"));
				or.add(Restrictions.eq("title", "%" + inputData + "%"));
				or.add(Restrictions.eq("leadUnit", "%" + inputData + "%"));
				// or.add(Restrictions.eq("description","%"+inputData+"%"));
				criteria.add(or);
				List<IacucView> iacucProtocols = criteria.list();
				if (iacucProtocols != null && !iacucProtocols.isEmpty()) {
					for (IacucView iacucProtocol : iacucProtocols) {
						HashMap<String, Object> fieldMap = new HashMap<String, Object>();
						fieldMap.put("PROTOCOL_NUMBER", iacucProtocol.getProtocolNumber());
						fieldMap.put("TITLE", iacucProtocol.getTitle());
						fieldMap.put("LEAD_UNIT", iacucProtocol.getLeadUnit());
						fieldMap.put("PROTOCOL_TYPE", iacucProtocol.getProtocolType());
						fieldMap.put("STATUS", iacucProtocol.getStatus());
						dashBoardDetailsMap.add(fieldMap);
					}
				}
				//logger.info("dashBoardDetailsMap :" + dashBoardDetailsMap);
			}
			if (tabIndex.equals("DISCLOSURE")) {
				Criteria criteria = session.createCriteria(ProposalView.class);
				Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.eq("coiDisclosureNumber", "%" + inputData + "%"));
				or.add(Restrictions.eq("fullName", "%" + inputData + "%"));
				or.add(Restrictions.eq("moduleItemKey", "%" + inputData + "%"));
				or.add(Restrictions.eq("expirationDate", "%" + inputData + "%"));
				// or.add(Restrictions.eq("description","%"+inputData+"%"));
				criteria.add(or);
				List<DisclosureView> disclosures = criteria.list();
				if (disclosures != null && !disclosures.isEmpty()) {
					for (DisclosureView disclosure : disclosures) {
						HashMap<String, Object> fieldMap = new HashMap<String, Object>();
						fieldMap.put("COI_DISCLOSURE_NUMBER", disclosure.getCoiDisclosureNumber());
						fieldMap.put("FULL_NAME", disclosure.getFullName());
						fieldMap.put("DISCLOSURE_DISPOSITION", disclosure.getDisclosureDisposition());
						fieldMap.put("DISCLOSURE_STATUS", disclosure.getDisclosureStatus());
						fieldMap.put("MODULE_ITEM_KEY", disclosure.getModuleItemKey());
						fieldMap.put("EXPIRATION_DATE", disclosure.getExpirationDate());
						dashBoardDetailsMap.add(fieldMap);
					}
				}
				//logger.info("dashBoardDetailsMap :" + dashBoardDetailsMap);
			}
			dashBoardProfile.setDashBoardDetailMap(dashBoardDetailsMap);
		} catch (Exception e) {
			logger.error("Error in methord getDashBoardData");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

}
