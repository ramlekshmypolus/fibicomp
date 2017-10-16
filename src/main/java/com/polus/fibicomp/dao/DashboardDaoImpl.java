package com.polus.fibicomp.dao;

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
import com.polus.fibicomp.view.IacucView;
import com.polus.fibicomp.view.ProposalView;
import com.polus.fibicomp.view.ProtocolView;
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

	public String getDashBoardResearchSummary(String userName) throws Exception {
		DashBoardProfile dashBoardProfile = new DashBoardProfile();
		try {
			logger.info("getDashBoardResearchSummary");
			List<ResearchSummaryView> summaryViews = hibernateTemplate.loadAll(ResearchSummaryView.class);
			dashBoardProfile.setSummaryViews(summaryViews);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in method getDashBoardResearchSummary");
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dashBoardProfile);
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

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("getDashBoardDataForAward");
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
				or.add(Restrictions.like("sponsor", "%" + property2 + "%").ignoreCase());
			}
			if (property3 != null && !property3.isEmpty()) {
				or.add(Restrictions.like("unitName", "%" + property3 + "%").ignoreCase());
			}
			if (property4 != null && !property4.isEmpty()) {
				or.add(Restrictions.like("pi", "%" + property4 + "%").ignoreCase());
			}

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
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

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("getDashBoardDataForProposal");
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

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
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

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("getProtocolDashboardData");
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

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
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

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("getDashBoardDataForIacuc");
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

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
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

		Disjunction or = Restrictions.disjunction();
		try {
			logger.info("getDashBoardDataForDisclosures");
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

			searchCriteria.add(or);
			countCriteria.add(or);

			Long dashboardCount = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
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
		/*
		 * DetachedCriteria maxQuery = DetachedCriteria.forClass(ActionItem.class);
		 * maxQuery.add(Restrictions.eq("principalId", principalId));
		 * maxQuery.setProjection(Projections.max("dateAssigned"));
		 */

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();

		// Query query = session.getNamedQuery("getUserNotification");
		/*
		 * String q =
		 * "FROM ActionItem t1 WHERE t1.id IN(SELECT id FROM(SELECT ai.documentId, MAX(ai.id) AS id FROM ActionItem ai WHERE ai.principalId = :principalId GROUP BY ai.documentId))"
		 * ; Query query = session.createQuery(q); actionLists = query.list();
		 */

		Criteria criteria = session.createCriteria(ActionItem.class);
		criteria.add(Restrictions.eq("principalId", principalId));

		/*
		 * Criteria criteria = session.createCriteria(ActionItem.class);
		 * criteria.add(Restrictions.eq("principalId", principalId));
		 * criteria.addOrder(Order.asc("documentId"));
		 * criteria.add(Property.forName("dateAssigned").eq(maxQuery));
		 */
		actionLists = criteria.list();
		if (actionLists != null && !actionLists.isEmpty()) {
			return actionLists;
		}
		return actionLists;
	}
}
