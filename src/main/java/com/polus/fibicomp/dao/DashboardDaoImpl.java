package com.polus.fibicomp.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
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
import com.polus.fibicomp.view.MobileProposalView;
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
		List<ResearchSummaryPieChart> summaryProposalDonutChart = new ArrayList<ResearchSummaryPieChart>();
		List<ResearchSummaryPieChart> summaryAwardDonutChart = new ArrayList<ResearchSummaryPieChart>();
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
			summaryProposalDonutChart = getSummaryInProgressProposalDonutChart(person_id, summaryProposalDonutChart);
			logger.info("summaryProposalDonutChart : " + summaryProposalDonutChart);
			summaryAwardDonutChart = getSummaryAwardedProposalDonutChart(person_id, summaryAwardDonutChart);
			logger.info("summaryAwardDonutChart : " + summaryAwardDonutChart);

			dashBoardProfile.setExpenditureVolumes(expenditureVolumeChart);
			dashBoardProfile.setSummaryViews(summaryTable);
			dashBoardProfile.setSummaryAwardPieChart(summaryAwardPiechart);
			dashBoardProfile.setSummaryProposalPieChart(summaryProposalPiechart);
			dashBoardProfile.setSummaryProposalDonutChart(summaryProposalDonutChart);
			dashBoardProfile.setSummaryAwardDonutChart(summaryAwardDonutChart);
		} catch (Exception e) {
			logger.error("Error in method getDashBoardResearchSummary");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	public List<ExpenditureVolume> getExpenditureVolumeChart(String person_id,
			List<ExpenditureVolume> expenditureVolumeChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query expenditureVolume = null;
		if (oracledb.equals("Y")) {
			expenditureVolume = session.createSQLQuery(
					"SELECT to_char(t4.start_date, 'yyyy') AS BUDGET_PERIOD, Sum(T4.total_direct_cost) AS Direct_Cost, Sum(T4.total_indirect_cost) AS FA FROM eps_proposal t1 INNER JOIN eps_proposal_budget_ext t2 ON t1.proposal_number = t2.proposal_number INNER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN budget_periods t4 ON t3.budget_id = t4.budget_id WHERE t1.owned_by_unit IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Proposal' AND person_id = :person_id) GROUP BY TO_CHAR(t4.start_date, 'yyyy') ORDER BY To_Number(TO_CHAR(t4.start_date, 'yyyy'))");
		} else {
			expenditureVolume = session.createSQLQuery(
					"SELECT year(t4.start_date) AS BUDGET_PERIOD, Sum(T4.total_direct_cost) AS Direct_Cost, Sum(T4.total_indirect_cost) AS FA FROM eps_proposal t1 INNER JOIN eps_proposal_budget_ext t2 ON t1.proposal_number = t2.proposal_number INNER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN budget_periods t4 ON t3.budget_id = t4.budget_id WHERE t1.owned_by_unit IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Proposal' AND person_id = :person_id) GROUP BY year(t4.start_date) ORDER BY year(t4.start_date)");
		}
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
				"select 'In Progress Proposal' as In_Progress_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=1 and  t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
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
				"select t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION as sponsor_type, count(1) from AWARD t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.sequence_number in(select max(sequence_number) from AWARD where AWARD_NUMBER=t1.AWARD_NUMBER) and t1.LEAD_UNIT_NUMBER in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Award' and person_id = :person_id) group by t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION");
		query.setString("person_id", person_id);
		return summaryAwardPiechart = query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryProposalPieChart(String person_id,
			List<ResearchSummaryPieChart> summaryProposalPiechart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t2.SPONSOR_TYPE_CODE,t3.DESCRIPTION as sponsor_type,count(1) from eps_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code inner join sponsor_type t3 on t2.SPONSOR_TYPE_CODE=t3.SPONSOR_TYPE_CODE where t1.OWNED_BY_UNIT in(select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) group by t2.SPONSOR_TYPE_CODE, t3.DESCRIPTION");
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

		Conjunction and = Restrictions.conjunction();
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
				and.add(Restrictions.like("accountNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("unitName", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("sponsor", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("fullName", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

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

		Conjunction and = Restrictions.conjunction();
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
				and.add(Restrictions.like("proposalNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("sponsor", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

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

		Conjunction and = Restrictions.conjunction();
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
				and.add(Restrictions.like("protocolNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("protocolType", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

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

		Conjunction and = Restrictions.conjunction();
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
				and.add(Restrictions.like("protocolNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("protocolType", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

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

		Conjunction and = Restrictions.conjunction();
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
				and.add(Restrictions.like("coiDisclosureNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("fullName", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("disclosureDisposition", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("moduleItemKey", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
				countCriteria.add(Restrictions.eq("personId", personId));
			}

			searchCriteria.add(and);
			countCriteria.add(and);

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

	@SuppressWarnings("unchecked")
	@Override
	public String getAwardBySponsorTypes(String personId, String sponsorCode) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<AwardView> awardBySponsorTypes = new ArrayList<AwardView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query awardList = session.createSQLQuery(
					"SELECT t1.sequence_number, t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t2.sponsor_name, t3.full_name AS PI FROM award t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN award_persons t3 ON t1.award_id = t3.award_id AND t3.contact_role_code = 'PI' WHERE t2.sponsor_type_code = :sponsorCode and t1.sequence_number in(select max(sequence_number) from  AWARD where AWARD_NUMBER=t1.AWARD_NUMBER) AND t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE perm_nm = 'View Award' AND person_id = :personId)");
			awardList.setString("personId", personId)
					 .setString("sponsorCode", sponsorCode);
			awardBySponsorTypes = awardList.list();
			logger.info("awardsBySponsorTypes : " + awardBySponsorTypes);
			dashBoardProfile.setAwardViews(awardBySponsorTypes);
		} catch (Exception e) {
			logger.error("Error in method getPieChartAwardbySponsorTypes");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getProposalBySponsorTypes(String personId, String sponsorCode) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> proposalBySponsorTypes = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalList = session.createSQLQuery(
					"SELECT t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t2.sponsor_name, t4.DESCRIPTION as Proposal_Type, t3.full_name AS PI FROM eps_proposal t1 INNER JOIN sponsor t2 ON t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN eps_prop_person t3 ON t1.proposal_number = t3.proposal_number AND t3.prop_person_role_id = 'PI' INNER JOIN proposal_type t4 ON t1.PROPOSAL_TYPE_CODE=t4.PROPOSAL_TYPE_CODE WHERE t2.sponsor_type_code = :sponsorCode AND t1.owned_by_unit IN(SELECT DISTINCT unit_number FROM   mitkc_user_right_mv WHERE  perm_nm = 'View Proposal' AND person_id = :personId)");
			proposalList.setString("personId", personId)
						.setString("sponsorCode", sponsorCode);
			proposalBySponsorTypes = proposalList.list();
			logger.info("proposalsBySponsorTypes : " + proposalBySponsorTypes);
			dashBoardProfile.setProposalViews(proposalBySponsorTypes);
		} catch (Exception e) {
			logger.error("Error in method getPieChartAwardbySponsorTypes");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashBoardProfile getProposalsInProgress(String personId) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> inProgressProposals = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query progressProposalList = session.createSQLQuery(
					"select t1.document_number, t1.proposal_number, t1.title, t5.sponsor_name, t3.TOTAL_COST, t4.full_name AS PI ,t1.OWNED_BY_UNIT as unit_number,t6.UNIT_NAME from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' LEFT OUTER JOIN eps_prop_person t4 ON t1.proposal_number = t4.proposal_number AND t4.prop_person_role_id = 'PI' INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.OWNED_BY_UNIT= t6.UNIT_NUMBER where t1.status_code=1 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :personId )");
			progressProposalList.setString("personId", personId);
			List<Object[]> proposals = progressProposalList.list();
			for (Object[] proposal : proposals) {
				ProposalView proposalView = new ProposalView();
				proposalView.setDocumentNumber(proposal[0].toString());
				proposalView.setProposalNumber(proposal[1].toString());
				proposalView.setTitle(proposal[2].toString());
				proposalView.setSponsor(proposal[3].toString());
				proposalView.setTotalCost(proposal[4].toString());
				proposalView.setFullName(proposal[5].toString());
				proposalView.setDeptName(proposal[7].toString());
				inProgressProposals.add(proposalView);
			}
			logger.info("getProposalsInProgress : " + inProgressProposals);
			dashBoardProfile.setProposalViews(inProgressProposals);
		} catch (Exception e) {
			logger.error("Error in method getProposalsInProgress");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashBoardProfile getSubmittedProposals(String personId) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> submittedProposals = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query subproposalList = session.createSQLQuery(
					"select t1.document_number, t1.proposal_number, t1.title, t5.sponsor_name, t3.TOTAL_COST, t4.full_name AS PI, t1.OWNED_BY_UNIT as unit_number,t6.UNIT_NAME from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' LEFT OUTER JOIN eps_prop_person t4 ON t1.proposal_number = t4.proposal_number AND t4.prop_person_role_id = 'PI' INNER JOIN sponsor t5 ON t1.sponsor_code = t5.sponsor_code inner join unit t6 on  t1.OWNED_BY_UNIT= t6.UNIT_NUMBER where t1.status_code=5 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :personId)");
			subproposalList.setString("personId", personId);
			List<Object[]> subProposals = subproposalList.list();
			for (Object[] proposal : subProposals) {
				ProposalView proposalView = new ProposalView();
				proposalView.setDocumentNumber(proposal[0].toString());
				proposalView.setProposalNumber(proposal[1].toString());
				proposalView.setTitle(proposal[2].toString());
				proposalView.setSponsor(proposal[3].toString());
				proposalView.setTotalCost(proposal[4].toString());
				proposalView.setFullName(proposal[5].toString());
				proposalView.setDeptName(proposal[7].toString());
				submittedProposals.add(proposalView);
			}
			logger.info("SubmittedProposals : " + submittedProposals);
			dashBoardProfile.setProposalViews(submittedProposals);
		} catch (Exception e) {
			logger.error("Error in method getSubmittedProposals");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DashBoardProfile getActiveAwards(String personId) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<AwardView> activeAwards = new ArrayList<AwardView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query activeAwardList = session.createSQLQuery(
					"SELECT t1.award_id, t1.document_number, t1.award_number, t1.account_number, t1.title, t4.sponsor_name, t5.full_name  AS PI, t3.total_cost AS total_amount FROM award t1 INNER JOIN award_budget_ext t2 ON t1.award_id = t2.award_id INNER JOIN budget t3 ON t2.budget_id = t3.budget_id AND t3.final_version_flag = 'Y' INNER JOIN sponsor t4 ON t1.sponsor_code = t4.sponsor_code LEFT OUTER JOIN award_persons t5 ON t1.award_id = t5.award_id AND t5.contact_role_code = 'PI' WHERE t1.award_sequence_status = 'ACTIVE' AND t1.lead_unit_number IN(SELECT DISTINCT unit_number FROM mitkc_user_right_mv WHERE  perm_nm = 'View Award' AND person_id = :personId)");
			activeAwardList.setString("personId", personId);
			List<Object[]> activeAwardsList = activeAwardList.list();
			for(Object[] award: activeAwardsList){
				AwardView awardView = new AwardView();
				awardView.setAwardId(Integer.valueOf(award[0].toString()));
				awardView.setDocumentNumber(award[1].toString());
				awardView.setAwardNumber(award[2].toString());
				if (award[3] != null) {
					awardView.setAccountNumber(award[3].toString());
				}
				awardView.setTitle(award[4].toString());
				if(award[5] != null){
					awardView.setSponsor(award[5].toString());
				}
				if(award[6] != null){
					awardView.setFullName(award[6].toString());
				}
				if(award[7] != null){
					awardView.setTotal_cost(award[7].toString());
				}
				activeAwards.add(awardView);
			}
			logger.info("Active Awards : " + activeAwards);
			dashBoardProfile.setAwardViews(activeAwards);
		} catch (Exception e) {
			logger.error("Error in method getActiveAwards");
			e.printStackTrace();
		}
		return dashBoardProfile;
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryAwardedProposalDonutChart(String person_id,
			List<ResearchSummaryPieChart> summaryAwardDonutChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from proposal t1 inner join SPONSOR t2 on t1.sponsor_code = t2.sponsor_code where t1.status_code = 2 and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id ) group by t1.sponsor_code,t2.SPONSOR_NAME");
		query.setString("person_id", person_id);
		return summaryAwardDonutChart = query.list();
	}

	@SuppressWarnings("unchecked")
	public List<ResearchSummaryPieChart> getSummaryInProgressProposalDonutChart(String person_id,
			List<ResearchSummaryPieChart> summaryProposalDonutChart) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select t1.sponsor_code,t2.SPONSOR_NAME as sponsor,count(1) as count from eps_proposal t1 inner join SPONSOR t2 on t1.sponsor_code=t2.sponsor_code where t1.status_code=1 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :person_id) group by t1.sponsor_code,t2.SPONSOR_NAME");
		query.setString("person_id", person_id);
		return summaryProposalDonutChart = query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getInProgressProposalsBySponsorExpanded(String personId, String sponsorCode) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> inProgressProposal = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalQuery = session.createSQLQuery(
					"select t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t4.full_name AS PI, t1.PROPOSAL_TYPE_CODE, t5.DESCRIPTION as Proposal_Type, t7.TOTAL_COST as BUDGET from eps_proposal t1 LEFT OUTER JOIN eps_prop_person t4 ON t1.proposal_number = t4.proposal_number AND t4.prop_person_role_id = 'PI' INNER JOIN proposal_type t5 ON t1.PROPOSAL_TYPE_CODE=t5.PROPOSAL_TYPE_CODE LEFT OUTER JOIN eps_proposal_budget_ext t6 ON t1.proposal_number = t6.proposal_number LEFT OUTER JOIN budget t7 ON t6.budget_id = t7.budget_id and t7.final_version_flag = 'Y' where t1.status_code=1 and t1.sponsor_code = :sponsorCode and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId )");
			proposalQuery.setString("personId", personId)
						.setString("sponsorCode", sponsorCode);
			inProgressProposal = proposalQuery.list();
			logger.info("inProgressProposal : " + inProgressProposal);
			dashBoardProfile.setProposalViews(inProgressProposal);
		} catch (Exception e) {
			logger.error("Error in method getInProgressProposalsBySponsor");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAwardedProposalsBySponsorExpanded(String personId, String sponsorCode) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		List<ProposalView> awardedProposal = new ArrayList<ProposalView>();
		try {
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Query proposalQuery = session.createSQLQuery(
					"select t1.DOCUMENT_NUMBER, t1.proposal_number, t1.title, t4.full_name AS PI, t1.ACTIVITY_TYPE_CODE, t5.DESCRIPTION as ACTIVITY_TYPE, T1.PROPOSAL_TYPE_CODE, T6.description as PROPOSAL_TYPE, t1.sponsor_code from proposal t1 INNER JOIN SPONSOR t2 on t1.sponsor_code = t2.sponsor_code LEFT OUTER JOIN PROPOSAL_PERSONS t4 ON t1.proposal_id = t4.proposal_id AND t4.CONTACT_ROLE_CODE = 'PI' INNER JOIN ACTIVITY_TYPE t5 on t1.ACTIVITY_TYPE_CODE = t5.ACTIVITY_TYPE_CODE INNER JOIN PROPOSAL_TYPE t6 on T1.PROPOSAL_TYPE_CODE = T6.PROPOSAL_TYPE_CODE where  t1.status_code = 2 and t1.PROPOSAL_SEQUENCE_STATUS='ACTIVE' and t1.sponsor_code = :sponsorCode and t1.LEAD_UNIT_NUMBER in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM = 'View Proposal' and person_id = :personId ) ");
			proposalQuery.setString("personId", personId)
						.setString("sponsorCode", sponsorCode);
			awardedProposal = proposalQuery.list();
			logger.info("awardedProposal : " + awardedProposal);
			dashBoardProfile.setProposalViews(awardedProposal);
		} catch (Exception e) {
			logger.error("Error in method getAwardedProposalsBySponsor");
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getFibiSummaryTable(String personId, List<Object[]> summaryTable) {

		List<Object[]> subPropCount = null;
		List<Object[]> inPropCount = null;

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query submittedProposal = session.createSQLQuery(
				"select 'Submitted Proposal' as Submitted_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=5 and t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
		submittedProposal.setString("person_id", personId);
		subPropCount = submittedProposal.list();
		if (subPropCount != null && !subPropCount.isEmpty()) {
			summaryTable.addAll(subPropCount);
		}

		Query inprogressProposal = session.createSQLQuery(
				"select 'In Progress Proposal' as In_Progress_Proposal, count(t1.proposal_number) as count,sum(t3.TOTAL_COST) as total_amount from eps_proposal t1 inner join eps_proposal_budget_ext t2 on t1.proposal_number=t2.proposal_number inner join budget t3 on t2.budget_id=t3.budget_id and t3.final_version_flag='Y' where t1.status_code=1 and  t1.OWNED_BY_UNIT in( select distinct UNIT_NUMBER from MITKC_USER_RIGHT_MV where PERM_NM ='View Proposal' and person_id = :person_id )");
		inprogressProposal.setString("person_id", personId);
		inPropCount = inprogressProposal.list();
		if (inPropCount != null && !inPropCount.isEmpty()) {
			summaryTable.addAll(inPropCount);
		}

		return summaryTable;	
	}

	@Override
	public List<MobileProposalView> getProposalsByParams(CommonVO vo) {
		String property1 = vo.getProperty1();
		String property2 = vo.getProperty2();
		String property3 = vo.getProperty3();
		String property4 = vo.getProperty4();
		String personId = vo.getPersonId();
		List<MobileProposalView> proposalViews = null;

		Conjunction and = Restrictions.conjunction();
		try {
			logger.info("---------- getProposalsByParams ------------");
			Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
			Criteria searchCriteria = session.createCriteria(ProposalView.class);
			searchCriteria.addOrder(Order.desc("updateTimeStamp"));
			if (property1 != null && !property1.isEmpty()) {
				and.add(Restrictions.like("proposalNumber", "%" + property1 + "%").ignoreCase());
			}
			if (property2 != null && !property2.isEmpty()) {
				and.add(Restrictions.like("title", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				and.add(Restrictions.like("leadUnit", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				and.add(Restrictions.like("sponsor", "%" + property4 + "%").ignoreCase());
			}
			if (personId != null && !personId.isEmpty()) {
				searchCriteria.add(Restrictions.eq("personId", personId));
			}
			searchCriteria.add(and);

			@SuppressWarnings("unchecked")
			List<ProposalView> proposals = searchCriteria.list();
			if (proposals != null && !proposals.isEmpty()) {
				proposalViews = new ArrayList<MobileProposalView>();
				for (ProposalView proposal : proposals) {
					MobileProposalView mobileProposal = new MobileProposalView();
					mobileProposal.setDocumentNo(proposal.getDocumentNumber());
					mobileProposal.setLeadUnit(proposal.getLeadUnit());
					mobileProposal.setLeadUnitNo(proposal.getLeadUnitNumber());
					mobileProposal.setPi(proposal.getPersonId());
					mobileProposal.setProposalNo(proposal.getProposalNumber());
					mobileProposal.setSponsor(proposal.getSponsor());
					mobileProposal.setStatus(proposal.getStatus());
					mobileProposal.setTitle(proposal.getTitle());
					mobileProposal.setVersionNo(String.valueOf(proposal.getVersionNumber()));
					mobileProposal.setCertified(proposal.isCertified());
					mobileProposal.setCertificationRequired(proposal.isCertificationRequired());
					mobileProposal.setProposalPersonRoleId(proposal.getProposalPersonRoleId());
					mobileProposal.setRoleName(proposal.getRoleName());
					proposalViews.add(mobileProposal);
				}
			}
		} catch (Exception e) {
			logger.error("Error in method getDashBoardDataForProposal", e);
			e.printStackTrace();
		}
		return proposalViews;
	}

}
