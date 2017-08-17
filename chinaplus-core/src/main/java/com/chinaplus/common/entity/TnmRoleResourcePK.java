package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TNM_ROLE_RESOURCE database table.
 * 
 */
@Embeddable
public class TnmRoleResourcePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ROLE_ID")
	private Integer roleId;

	@Column(name="SYS_SCREEN_ID")
	private Integer sysScreenId;

	public TnmRoleResourcePK() {
	}
	public Integer getRoleId() {
		return this.roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getSysScreenId() {
		return this.sysScreenId;
	}
	public void setSysScreenId(Integer sysScreenId) {
		this.sysScreenId = sysScreenId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TnmRoleResourcePK)) {
			return false;
		}
		TnmRoleResourcePK castOther = (TnmRoleResourcePK)other;
		return 
			(this.roleId == castOther.roleId)
			&& (this.sysScreenId == castOther.sysScreenId);
	}

	public int hashCode() {
		final Integer prime = 31;
		Integer hash = 17;
		hash = hash * prime + this.roleId;
		hash = hash * prime + this.sysScreenId;
		
		return hash;
	}
}