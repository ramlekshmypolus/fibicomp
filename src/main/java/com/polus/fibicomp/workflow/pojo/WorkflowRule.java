package com.polus.fibicomp.workflow.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "MITKC_WORKFLOW_STATUS")
public class WorkflowRule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "ruleIdGenerator", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "1"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "ruleIdGenerator")
	@Column(name = "RULE_ID")
	private Integer ruleId;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "RULE_EVAL_ORDER")
	private Integer ruleEvaluationOrder;

	@Column(name = "RULE_EVAL_FUNCTION_NM")
	private String ruleEvalFunctionName;

	@Column(name = "MAP_ID")
	private Integer mapId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK1_MITKC_WORKFLOW_RULE"), name = "MAP_ID", referencedColumnName = "MAP_ID", insertable = false, updatable = false)
	private WorkflowMap workflowMap;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimeStamp;

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRuleEvaluationOrder() {
		return ruleEvaluationOrder;
	}

	public void setRuleEvaluationOrder(Integer ruleEvaluationOrder) {
		this.ruleEvaluationOrder = ruleEvaluationOrder;
	}

	public String getRuleEvalFunctionName() {
		return ruleEvalFunctionName;
	}

	public void setRuleEvalFunctionName(String ruleEvalFunctionName) {
		this.ruleEvalFunctionName = ruleEvalFunctionName;
	}

	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}

	public WorkflowMap getWorkflowMap() {
		return workflowMap;
	}

	public void setWorkflowMap(WorkflowMap workflowMap) {
		this.workflowMap = workflowMap;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Timestamp getUpdateTimeStamp() {
		return updateTimeStamp;
	}

	public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}
}
