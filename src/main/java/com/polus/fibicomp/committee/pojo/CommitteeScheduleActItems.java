package com.polus.fibicomp.committee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "FIBI_COMM_SCHEDULE_ACT_ITEMS")
public class CommitteeScheduleActItems implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COMM_SCHEDULE_ACT_ITEMS_ID")
	private Integer commScheduleActItemsId;

	@Column(name = "SCHEDULE_ID")
	private Integer scheduleId;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_FIBI_COMM_SCH_ACT_ITEMS"), name = "SCHEDULE_ID", referencedColumnName = "SCHEDULE_ID", insertable = false, updatable = false)
	private CommitteeSchedule committeeSchedule;

	@Column(name = "ACTION_ITEM_NUMBER")
	private Integer actionItemNumber;

	@Column(name = "SCHEDULE_ACT_ITEM_TYPE_CODE")
	private Integer scheduleActItemTypecode;

	@Column(name = "ITEM_DESCTIPTION")
	private String itemDesctiption;

	@Column(name = "UPDATE_TIMESTAMP")
	private Timestamp updateTimestamp;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	@Version
	@Column(name = "VER_NBR", length = 8)
	private Integer versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true)
	private String objectId;

	public Integer getCommScheduleActItemsId() {
		return commScheduleActItemsId;
	}

	public void setCommScheduleActItemsId(Integer commScheduleActItemsId) {
		this.commScheduleActItemsId = commScheduleActItemsId;
	}

	public CommitteeSchedule getCommitteeSchedule() {
		return committeeSchedule;
	}

	public void setCommitteeSchedule(CommitteeSchedule committeeSchedule) {
		this.committeeSchedule = committeeSchedule;
	}

	public Integer getActionItemNumber() {
		return actionItemNumber;
	}

	public void setActionItemNumber(Integer actionItemNumber) {
		this.actionItemNumber = actionItemNumber;
	}

	public Integer getScheduleActItemTypecode() {
		return scheduleActItemTypecode;
	}

	public void setScheduleActItemTypecode(Integer scheduleActItemTypecode) {
		this.scheduleActItemTypecode = scheduleActItemTypecode;
	}

	public String getItemDesctiption() {
		return itemDesctiption;
	}

	public void setItemDesctiption(String itemDesctiption) {
		this.itemDesctiption = itemDesctiption;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
}
