package com.bsoft.nis.domain.user.session;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 登录用户的机构信息
 * @author hp
 *
 */
public class ManageUnitEntity {
	private String id ;
	private String name ;
	private String type ;
	private String ref ;
	private String pyCode ;
	private String lastModify ;
	private String hisOrganizationCode ;
	
	@JsonProperty(value="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty(value="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty(value="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@JsonProperty(value="ref")
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	@JsonProperty(value="pyCode")
	public String getPyCode() {
		return pyCode;
	}
	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}
	
	@JsonProperty(value="lastModify")
	public String getLastModify() {
		return lastModify;
	}
	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}
	
	@JsonProperty(value="hisOrganizationCode")
	public String getHisOrganizationCode() {
		return hisOrganizationCode;
	}
	public void setHisOrganizationCode(String hisOrganizationCode) {
		this.hisOrganizationCode = hisOrganizationCode;
	}
}
