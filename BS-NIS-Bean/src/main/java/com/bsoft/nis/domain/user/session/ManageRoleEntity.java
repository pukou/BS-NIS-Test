package com.bsoft.nis.domain.user.session;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 角色机构信息
 * @author hp
 *
 */
public class ManageRoleEntity {
	private String roleid ;
	private String roleName ;
	private String manageUnitId ;
	private String manageUnitName ;
	private String displayName ;
	private String ref ;
	
	@JsonProperty(value="roleid")
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	
	@JsonProperty(value="roleName")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@JsonProperty(value="manageUnitId")
	public String getManageUnitId() {
		return manageUnitId;
	}
	public void setManageUnitId(String manageUnitId) {
		this.manageUnitId = manageUnitId;
	}
	
	@JsonProperty(value="manageUnitName")
	public String getManageUnitName() {
		return manageUnitName;
	}
	public void setManageUnitName(String manageUnitName) {
		this.manageUnitName = manageUnitName;
	}
	
	@JsonProperty(value="displayName")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@JsonProperty(value="ref")
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
}
