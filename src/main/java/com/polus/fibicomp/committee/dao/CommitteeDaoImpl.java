package com.polus.fibicomp.committee.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polus.fibicomp.committee.pojo.Committee;
import com.polus.fibicomp.committee.pojo.CommitteeType;
import com.polus.fibicomp.committee.pojo.ProtocolReviewType;
import com.polus.fibicomp.committee.pojo.ResearchArea;
import com.polus.fibicomp.pojo.Unit;

@Transactional
@Service(value = "committeeDao")
public class CommitteeDaoImpl implements CommitteeDao {

	protected static Logger logger = Logger.getLogger(CommitteeDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public List<ProtocolReviewType> fetchAllReviewType() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProtocolReviewType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("reviewTypeCode"), "reviewTypeCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProtocolReviewType.class));
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ProtocolReviewType> reviewTypes = criteria.list();
		return reviewTypes;
	}

	@Override
	public List<Unit> fetchAllHomeUnits() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Unit.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("unitNumber"), "unitNumber");
		projList.add(Projections.property("unitName"), "unitName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Unit.class));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("unitName"));
		@SuppressWarnings("unchecked")
		List<Unit> units = criteria.list();
		return units;
	}

	@Override
	public List<ResearchArea> fetchAllResearchAreas() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ResearchArea.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("researchAreaCode"), "researchAreaCode");
		projList.add(Projections.property("description"), "description");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ResearchArea.class));
		criteria.add(Restrictions.eq("active", true));
		criteria.addOrder(Order.asc("description"));
		@SuppressWarnings("unchecked")
		List<ResearchArea> researchAreas = criteria.list();
		return researchAreas;
	}

	@Override
	public CommitteeType fetchCommitteeType(Integer committeeTypeCode) {
		CommitteeType committeeType = null;
		committeeType = hibernateTemplate.get(CommitteeType.class, committeeTypeCode);
		return committeeType;
	}

	@Override
	public Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getTime();
	}

	@Override
	public Timestamp getCurrentTimestamp() {
		return new Timestamp(this.getCurrentDate().getTime());
	}

	@Override
	public String convertObjectToJSON(Object object) {
		String response = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			response = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public Committee saveCommittee(Committee committee) {
		hibernateTemplate.saveOrUpdate(committee);
		return committee;
	}

}
