package com.polus.fibicomp.report.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.grantcall.pojo.GrantCallType;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.pojo.ProtocolType;
import com.polus.fibicomp.proposal.pojo.Proposal;
import com.polus.fibicomp.report.vo.ReportVO;
import com.polus.fibicomp.view.AwardView;

@Transactional
@Service(value = "reportDao")
public class ReportDaoImpl implements ReportDao {

	protected static Logger logger = Logger.getLogger(ReportDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<GrantCall> fetchOpenGrantIds() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCall.class);
		criteria.add(Restrictions.like("grantStatusCode",Constants.GRANT_CALL_STATUS_CODE_OPEN));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantCallId"), "grantCallId");
		projList.add(Projections.property("grantCallName"), "grantCallName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCall.class));
		criteria.addOrder(Order.asc("grantCallId"));
		@SuppressWarnings("unchecked")
		List<GrantCall> grantIds = criteria.list();
		return grantIds;
	}

	@Override
	public ReportVO fetchApplicationByGrantCallId(ReportVO reportVO) {
		Integer grantCallId = reportVO.getGrantCallId();
		List<Integer> proposalStatus = new ArrayList<Integer>();
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_APPROVAL_INPROGRESS);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_APPROVED);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_SUBMITTED);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_REVIEW_INPROGRESS);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_REVISION_REQUESTED);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_ENDORSEMENT);
		proposalStatus.add(Constants.PROPOSAL_STATUS_CODE_AWARDED);

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.add(Restrictions.eq("grantCallId", grantCallId));
		criteria.add(Restrictions.in("statusCode", proposalStatus));
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();	
		if (proposals != null && !proposals.isEmpty()) {
			reportVO.setProposalCount(proposals.size());
			reportVO.setProposals(proposals);
		}
		return reportVO;
	}

	@Override
	public Long fetchApplicationsCountByGrantCallType(Integer grantCallTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.createAlias("grantCall", "grantCall");
		criteria.add(Restrictions.eq("grantCall.grantTypeCode", grantCallTypeCode));
		Long applicationsCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return applicationsCount;
	}

	@Override
	public Long fetchProtocolsCountByProtocolType(String protocolTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Protocol.class);
		criteria.add(Restrictions.eq("protocolTypeCode", protocolTypeCode));
		criteria.add(Restrictions.eq("protocolStatusCode", Constants.PROTOCOL_SATUS_CODE_ACTIVE_OPEN_TO_ENTROLLMENT));
		Long protocolsCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return protocolsCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProtocolType> fetchAllProtocolTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolType.class);
		List<ProtocolType> protocolTypes = criteria.list();
		return protocolTypes;
	}

	@Override
	public List<GrantCallType> fetchAllGrantCallTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCallType.class);
		@SuppressWarnings("unchecked")
		List<GrantCallType> grantCallTypes = criteria.list();
		return grantCallTypes;
	}

	@Override
	public List<Integer> fetchIPByGrantTypeCode(Integer grantTypeCode) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.add(Restrictions.eq("grantTypeCode", grantTypeCode));
		criteria.add(Restrictions.eq("statusCode", Constants.PROPOSAL_STATUS_CODE_AWARDED));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("ipNumber"), "ipNumber");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Proposal.class));
		criteria.addOrder(Order.asc("ipNumber"));
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();
		if (proposals != null && !proposals.isEmpty()) {
			List<String> ipNumbers = new ArrayList<String>();
			String ipNumber = null;
			for (Proposal proposal : proposals) {
				ipNumber = proposal.getIpNumber();
				ipNumbers.add(ipNumber);
			}
			if (ipNumbers != null && !ipNumbers.isEmpty()) {
				Query query = session.createSQLQuery("select PROPOSAL_ID from PROPOSAL where PROPOSAL_NUMBER in (:ids)");
				query.setParameterList("ids", ipNumbers);
				@SuppressWarnings("unchecked")
				List<Integer> proposalId = query.list();
				return proposalId;
			}
		}
		return new ArrayList<Integer>();
	}

	@Override
	public Long fetchAwardCountByGrantType(List<Integer> proposalId) {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery("select count(AWARD_ID) from AWARD_FUNDING_PROPOSALS where PROPOSAL_ID in (:ids)");
		query.setParameterList("ids", proposalId);
		BigDecimal bigDecimal = (BigDecimal) query.uniqueResult();
		Long count = bigDecimal.longValue();
		return count;
	}

	@Override
	public ReportVO fetchAwardByGrantCallId(ReportVO reportVO) {
		Integer grantCallId = reportVO.getGrantCallId();
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.add(Restrictions.eq("grantCallId", grantCallId));
		criteria.add(Restrictions.eq("statusCode", Constants.PROPOSAL_STATUS_CODE_AWARDED));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("ipNumber"), "ipNumber");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Proposal.class));
		@SuppressWarnings("unchecked")
		List<Proposal> proposals = criteria.list();
		if (proposals != null && !proposals.isEmpty()) {
			List<String> ipNumbers = new ArrayList<String>();
			String ipNumber = null;
			for (Proposal proposal : proposals) {
				ipNumber = proposal.getIpNumber();
				ipNumbers.add(ipNumber);
			}
			if (ipNumbers != null && !ipNumbers.isEmpty()) {
				Query query = session.createSQLQuery(
						"select AWARD_ID from AWARD_FUNDING_PROPOSALS where PROPOSAL_ID in (select PROPOSAL_ID from PROPOSAL where PROPOSAL_NUMBER in (:ids))");
				query.setParameterList("ids", ipNumbers);
				@SuppressWarnings("unchecked")
				List<BigDecimal> awardId = query.list();
				if (awardId != null && !awardId.isEmpty()) {
					List<Integer> awardIds = new ArrayList<Integer>();
					for (BigDecimal val : awardId) {
						awardIds.add(val.intValue());
					}
					Criteria searchCriteria = session.createCriteria(AwardView.class);
					searchCriteria.add(Restrictions.in("awardId", awardIds));/*
					searchCriteria.add(Restrictions.eq("personId", reportVO.getPersonId()));*/
					@SuppressWarnings("unchecked")
					List<AwardView> awardList = searchCriteria.list();
					if (awardList != null && !awardList.isEmpty()) {
						reportVO.setAwardCount(awardList.size());
						reportVO.setAwards(awardList);
					}
				}
			}
		}
		return reportVO;
	}

	@Override
	public ReportVO fetchExpenditureByAward(ReportVO reportVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
