package com.polus.fibicomp.proposal.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.fibicomp.constants.Constants;
import com.polus.fibicomp.grantcall.pojo.GrantCall;
import com.polus.fibicomp.pojo.ProposalPersonRole;
import com.polus.fibicomp.pojo.Protocol;
import com.polus.fibicomp.proposal.pojo.ProposalAttachmentType;
import com.polus.fibicomp.proposal.pojo.ProposalCategory;
import com.polus.fibicomp.proposal.pojo.ProposalResearchType;
import com.polus.fibicomp.proposal.pojo.ProposalStatus;

@Transactional
@Service(value = "proposalDao")
public class ProposalDaoImpl implements ProposalDao {

	protected static Logger logger = Logger.getLogger(ProposalDaoImpl.class.getName());

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public ProposalStatus fetchStatusByStatusCode(Integer statusCode) {
		return hibernateTemplate.get(ProposalStatus.class, statusCode);
	}

	@Override
	public List<ProposalCategory> fetchAllCategories() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalCategory.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("categoryCode"), "categoryCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalCategory.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalCategory> categories = criteria.list();
		return categories;
	}

	@Override
	public List<Protocol> fetchAllProtocols() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Protocol.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("protocolNumber"), "protocolNumber");
		projList.add(Projections.property("expirationDate"), "expirationDate");
		projList.add(Projections.property("protocolStatusCode"), "protocolStatusCode");
		projList.add(Projections.property("protocolStatus"), "protocolStatus");
		projList.add(Projections.property("approvalDate"), "approvalDate");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(Protocol.class));
		criteria.addOrder(Order.asc("updateTimestamp"));
		@SuppressWarnings("unchecked")
		List<Protocol> protocols = criteria.list();
		return protocols;
	}

	@Override
	public List<ProposalAttachmentType> fetchAllProposalAttachmentTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalAttachmentType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("attachmentTypeCode"), "attachmentTypeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalAttachmentType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalAttachmentType> attachmentTypes = criteria.list();
		return attachmentTypes;
	}

	@Override
	public List<GrantCall> fetchAllGrantCalls() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(GrantCall.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("grantCallId"), "grantCallId");
		projList.add(Projections.property("grantCallName"), "grantCallName");
		projList.add(Projections.property("sponsor"), "sponsor");
		projList.add(Projections.property("fundingSourceType"), "fundingSourceType");
		//projList.add(Projections.property("grantCallName"), "grantCallName");
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GrantCall.class));
		criteria.addOrder(Order.asc("grantCallName"));
		@SuppressWarnings("unchecked")
		List<GrantCall> grantCalls = criteria.list();
		return grantCalls;
	}

	@Override
	public List<ProposalPersonRole> fetchAllProposalPersonRoles() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalPersonRole.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("id"), "id");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalPersonRole.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalPersonRole> proposalPersonRoles = criteria.list();
		return proposalPersonRoles;
	}

	@Override
	public List<ProposalResearchType> fetchAllProposalResearchTypes() {
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ProposalResearchType.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("researchTypeCode"), "researchTypeCode");
		projList.add(Projections.property(Constants.DESCRIPTION), Constants.DESCRIPTION);
		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(ProposalResearchType.class));
		criteria.addOrder(Order.asc(Constants.DESCRIPTION));
		@SuppressWarnings("unchecked")
		List<ProposalResearchType> proposalResearchTypes = criteria.list();
		return proposalResearchTypes;
	}

}
