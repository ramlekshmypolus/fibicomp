package com.polus.fibicomp.report.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
import com.polus.fibicomp.proposal.pojo.Proposal;

@Transactional
@Service(value = "reportDao")
public class ReportDaoImpl implements ReportDao {

	protected static Logger logger = Logger.getLogger(ReportDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public Integer fetchApplicationCountByGrantCallId(Integer grantCallId) {
		List<Integer> proposalStatus = new ArrayList<Integer>();
		proposalStatus.add(2);
		proposalStatus.add(4);
		proposalStatus.add(5);
		proposalStatus.add(8);
		proposalStatus.add(9);
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Proposal.class);
		criteria.add(Restrictions.like("grantCallId", grantCallId));
		criteria.add(Restrictions.in("statusCode", proposalStatus));
		Integer proposalCount = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
		return proposalCount;
	}

	@Override
	public List<GrantCall> fetchOpenGrantIds() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCall.class);
		criteria.add(Restrictions.like("grantStatusCode",Constants.GRANT_CALL_STATUS_CODE_OPEN));
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantCallId"), "grantCallId");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCall.class));
		criteria.addOrder(Order.asc("grantCallId"));
		@SuppressWarnings("unchecked")
		List<GrantCall> grantIds = criteria.list();
		return grantIds;
	}
}
