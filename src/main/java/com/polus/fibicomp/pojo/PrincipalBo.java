package com.polus.fibicomp.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import com.polus.fibicomp.util.JpaCharBooleanConversion;

@Entity
@Table(name = "KRIM_PRNCPL_T")
public class PrincipalBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRINCIPAL")
	@SequenceGenerator(name = "SEQ_PRINCIPAL", sequenceName = "KRIM_PRNCPL_ID_S", allocationSize = 50)
	@Column(name = "PRNCPL_ID")
	private String principalId;

	@Column(name = "PRNCPL_NM")
	private String principalName;

	@Column(name = "ENTITY_ID")
	private String entityId;

	@Column(name = "PRNCPL_PSWD")
	private String password;

	@Column(name = "ACTV_IND")
	@Convert(converter = JpaCharBooleanConversion.class)
	private boolean active;

	@Version
	@Column(name = "VER_NBR", length = 8)
	protected Long versionNumber;

	@Column(name = "OBJ_ID", length = 36, unique = true, nullable = false)
	protected String objectId;

	public String getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Long versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
