package com.polus.fibicomp.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.pojo.ActionItem;
import com.polus.fibicomp.pojo.DashBoardProfile;
import com.polus.fibicomp.view.AwardView;
import com.polus.fibicomp.view.DisclosureView;
import com.polus.fibicomp.view.ExpenditureVolume;
import com.polus.fibicomp.view.IacucView;
import com.polus.fibicomp.view.ProposalView;
import com.polus.fibicomp.view.ProtocolView;
import com.polus.fibicomp.view.ResearchSummaryPieChart;
import com.polus.fibicomp.view.ResearchSummaryView;
import com.polus.fibicomp.vo.CommonVO;

@Transactional
@Service(value = "dashboardDao")
@PropertySource("classpath:application.properties")
public class DashboardDaoImpl implements DashboardDao {

	protected static Logger logger = Logger.getLogger(DashboardDaoImpl.class.getName());

	@Value("${oracledb}")
	private String oracledb;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public String getDashBoardResearchSummary(String person_id) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ExpenditureVolume> expenditureVolumeChart = new ArrayList<ExpenditureVolume>();
		List<ResearchSummaryView> summaryTable = new ArrayList<ResearchSummaryView>();
		List<ResearchSummaryPieChart> summaryAwardPiechart = new ArrayList<ResearchSummaryPieChart>();
		List<ResearchSummaryPieChart> summaryProposalPiechart = new ArrayList<ResearchSummaryPieChart>();
		try {
			logger.info("---------- getDashBoardResearchSummary -----------");
			expenditureVolumeChart = getExpenditureVolumeChart(person_id, expenditureVolumeChart);
			logger.info("expenditureVolumeChart : " + expenditureVolumeChart);
			summaryTable = getSummaryTable(person_id, summaryTable);
			logger.info("summaryTable : " + summaryTable);
			summaryAwardPiechart = getSummaryAwardPieChart(person_id, summaryAwardPiechart);
			logger.info("summaryAwardPiechart : " + summaryAwardPiechart);
			summaryProposalPiechart = getSummaryProposalPieChart(person_id, summaryProposalPiechart);
			logger.info("summaryProposalPiechart : " + summaryProposalPiechart);

			dashBoardProfile.setExpenditureVolumes(expenditureVolumeChart);
			dashBoardProfile.setSummaryViews(summaryTable);
			dashBoardProfile.setSummaryAwardPieChart(summaryAwardPiechart);
			dashBoardProfile.setSummaryProposalPieChart(summaryProposalPiechart);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardResearchSummary");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	private List<ExpenditureVolume> getExpenditureVolumeChart(String person_id,
			List<ExpenditureVolume> expenditureVolumeChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query expenditureVolume = session.createSQLQuery(
				"SELECT To_char(t4.start_date, 'yyyy') AS BUDGET_PERIOD, Sum(T4.total_direct_cost) AS Direct_Cost, Sum(T4.total_indirect_cost) AS FA FROM eps_proposal t1 INNER JOIN eps_proposal_budget_ext t2 ON t1.proposal_number = t2.proposal_number INNER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN budget_periods t4 ON t3.budget_id = t4.budget_id WHERE  t1.owned_by_unit IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Proposal' AND person_id = :person_id) GROUP  BY TO_CHAR(t4.start_date, 'yyyy') ORDER BY To_Number(TO_CHAR(t4.start_date, 'yyyy'))");
		expenditureVolume.setString("person_id", person_id);
		expenditureVolumeChart = expenditureVolume.list();
		return expenditureVolumeChart;
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryView> getSummaryTable(String person_id, List<ResearchSummaryView> summaryTable) {
		List<ResearchSummaryView> subPropCount = null;
		List<ResearchSummaryView> inPropCount = null;
		List<ResearchSummaryView> activeAwardsCount = null;

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query submittedProposal = session.createSQLQuery(
				"select 'Submitted Proposal' as Submitted_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=5 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
		submittedProposal.setString("person_id", person_id);
		subPropCount = submittedProposal.list();
		if (subPropCount != null && !subPropCount.isEmpty()) {
			summaryTable.addAll(subPropCount);
		}

		Query inprogressProposal = session.createSQLQuery(
				"select 'Inprogress Proposal' as In_Progress_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=1 and  t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
		inprogressProposal.setString("person_id", person_id);
		inPropCount = inprogressProposal.list();
		if (inPropCount != null && !inPropCount.isEmpty()) {
			summaryTable.addAll(inPropCount);
		}

		Query activeAwards = session.createSQLQuery(
				"select 'Active Award' as Active_Award, count(t1.award_id),sum(t3.TOTAL_COST) as total_amount from AWARD t1 inner join AWARD_BUDGET_EXT t2 on t1.award_id=t2.award_id inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.award_sequence_status = 'ACTIVE' and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Award' and person_id = :person_id )");
		activeAwards.setString("person_id", person_id);
		activeAwardsCount = activeAwards.list();
		if (activeAwardsCount != null && !activeAwardsCount.isEmpty()) {
			summaryTable.addAll(activeAwardsCount);
		}

		return summaryTable;
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryAwardPieChart(String person_id,
			List<ResearchSummaryPieChart> summaryAwardPiechart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION as sponsor_type,count(1) from AWARD t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Award' and person_id = :person_id ) group by t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION");
		query.setString("person_id", person_id);
		return summaryAwardPiechart = query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryProposalPieChart(String person_id,
			List<ResearchSummaryPieChart> summaryProposalPiechart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION as sponsor_type,count(1) from eps_proposal t1  inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id ) group by t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION");
		query.setString("person_id", person_id);
		return summaryProposalPiechart = query.list();
	}

	public DashBoardProfile getDashBoardDataForAward(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("-------- getDashBoardDataForAward ---------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(AwardView.class);
			Criteria countCriteria = session.createCriteria(AwardView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				or.add(Restrictions.like("accountNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {				
				or.add(Restrictions.like("unitName", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				or.add(Restrictions.like("sponsor", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				or.add(Restrictions.like("fullName", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<AwardView> awards = searchCriteria.list();
			dashBoardProfile.setAwardViews(awards);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForAward", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForProposal(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("---------- getDashBoardDataForProposal ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(ProposalView.class);
			Criteria countCriteria = session.createCriteria(ProposalView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				or.add(Restrictions.like("proposalNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				or.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				or.add(Restrictions.like("sponsor", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				or.add(Restrictions.like("leadUnit", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<ProposalView> proposals = searchCriteria.list();
			dashBoardProfile.setProposalViews(proposals);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForProposal", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getProtocolDashboardData(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("--------- getProtocolDashboardData -----------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(ProtocolView.class);
			Criteria countCriteria = session.createCriteria(ProtocolView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				or.add(Restrictions.like("protocolNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				or.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				or.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				or.add(Restrictions.like("protocolType", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<ProtocolView> protocols = searchCriteria.list();
			dashBoardProfile.setProtocolViews(protocols);
		} catch (Exception e) {
			logger.error("Error in method getProtocolDashboardData", e);
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public DashBoardProfile getDashBoardDataForIacuc(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("---------- getDashBoardDataForIacuc -----------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(IacucView.class);
			Criteria countCriteria = session.createCriteria(IacucView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				or.add(Restrictions.like("protocolNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				or.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				or.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				or.add(Restrictions.like("protocolType", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<IacucView> iacucProtocols = searchCriteria.list();
			dashBoardProfile.setIacucViews(iacucProtocols);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForIacuc");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@Override
	public DashBoardProfile getDashBoardDataForDisclosures(CommonVO vo) {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		Integer pageNumber = vo.getPageNumber();
		String sortBy = vo.getSortBy();
		String reverse = vo.getReverse();
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		Integer currentPage = vo.getCurrentPage();
		String personId = vo.getPersonId();

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("----------- getDashBoardDataForDisclosures ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(DisclosureView.class);
			Criteria countCriteria = session.createCriteria(DisclosureView.class);
			if (sortBy.isEmpty() || reverse.isEmpty()) {
				searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			} else {
				if (reverse.equals("DESC")) {
					searchCriteria.addOrder(Order.desc(sortBy));
				} else {
					searchCriteria.addOrder(Order.asc(sortBy));
				}
			}
			if (property1 != null && !property1.isEmpty()) {
				or.add(Restrictions.like("coiDisclosureNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				or.add(Restrictions.like("fullName", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				or.add(Restrictions.like("disclosureDisposition", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				or.add(Restrictions.like("moduleItemKey", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
			logger.info("dashboardCount : " + dashboardCount);
			dashBoardProfile.setTotalServiceRequest(dashboardCount.intValue());

			int count = pageNumber * (currentPage - 1);
			searchCriteria.setFirstResult(count);
			searchCriteria.setMaxResults(pageNumber);
			@SuppressWarnings("unchecked")
			List<DisclosureView> disclosures = searchCriteria.list();
			dashBoardProfile.setDisclosureViews(disclosures);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForDisclosures");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	public Integer getDashBoardCount(String requestType, Integer pageNumber) {
		Integer dashBoardCount = null;
		try {
			if (pageNumber == 10) {
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ActionItem> getUserNotification(String principalId) {
		List<ActionItem> actionLists = null;

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ActionItem.class);
		criteria.add(Restrictions.eq("principalId", principalId));
		actionLists = criteria.list();
		return actionLists;
	}
}
