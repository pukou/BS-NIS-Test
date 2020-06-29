package com.bsoft.nis.domain.user.session;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 登录用户的角色信息
 * @author hp
 *
 */
public class UserRoleEntity {
	private String id ;
	private String name ;
	private String pageCount ;
	private String type ;
	private String lastModify ;
	
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
	
	@JsonProperty(value="pageCount")
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	
	@JsonProperty(value="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@JsonProperty(value="lastModify")
	public String getLastModify() {
		return lastModify;
	}
	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}
}
