package com.polus.fibicomp.schedule.dao;

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

import com.polus.fibicomp.committee.pojo.CommitteeSchedule;
import com.polus.fibicomp.committee.pojo.ProtocolSubmission;
import com.polus.fibicomp.committee.pojo.ScheduleActItemType;

@Transactional
@Service(value = "scheduleDao")
public class ScheduleDaoImpl implements ScheduleDao {

	protected static Logger logger = Logger.getLogger(ScheduleDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<ProtocolSubmission> fetchProtocolSubmissionByIds(Integer scheduleId, String committeeId) {
		List<ProtocolSubmission> protocolSubmissions = null;
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolSubmission.class);
		criteria.add(Restrictions.eq("scheduleId", scheduleId));
		criteria.add(Restrictions.eq("committeeId", committeeId));
		criteria.add(Restrictions.eq("protocolActive", true));
		//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		protocolSubmissions = criteria.list();
		return protocolSubmissions;
	}

	@Override
	public List<ScheduleActItemType> fetchAllScheduleActItemType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ScheduleActItemType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("scheduleActItemTypecode"), "scheduleActItemTypecode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ScheduleActItemType.class));
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ScheduleActItemType> scheduleActItemTypes = criteria.list();
		return scheduleActItemTypes;
	}

	@Override
	public CommitteeSchedule updateCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		hibernateTemplate.saveOrUpdate(committeeSchedule);
		return committeeSchedule;
	}

}
