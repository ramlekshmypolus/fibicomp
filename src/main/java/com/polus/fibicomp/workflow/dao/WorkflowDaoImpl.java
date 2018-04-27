package com.polus.fibicomp.workflow.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(value = "workflowDao")
public class WorkflowDaoImpl implements WorkflowDao {

	protected static Logger logger = Logger.getLogger(WorkflowDaoImpl.class.getName());

}
