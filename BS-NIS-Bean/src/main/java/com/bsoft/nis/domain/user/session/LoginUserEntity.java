package com.bsoft.nis.domain.user.session;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 登录用户的信息
 * @author hp
 *
 */
public class LoginUserEntity {
	private String cardtype ;
	private String remark ;
	private String roleName ;
	private String personName ;
	private String jobpost ;
	private String narcoticright ;
	private String userId ;
	private String userName ;
	private String gender ;
	private String personId ;
	private String psychotropicright ;
	private String organName ;
	private String educationbackground ;
	private String roleId ;
	private String iscancel ;
	private String majorjob ;
	private String expertcost ;
	private String hometown ;
	private String joininwork ;
	private String organizCode ;
	private String email ;
	private String lastModifyDate ;
	private String manageUnitName ;
	private String logoff ;
	private String mobile ;
	private String organId ;
	private String birthday ;
	private String officeCode ;
	private String operationtype ;
	private String isexpert ;
	private String majorqualify ;
	private String education ;
	private String id ;
	private String manageUnitId ;
	private String wbCode ;
	private String domain ;
	private String lastLoginTime ;
	private String ethnic ;
	private String cardnum ;
	private String operationscope ;
	private String majorname ;
	private String address ;
	private String displayName ;
	private String prescriberight ;
	
	private UserRoleEntity role ;
	private ManageUnitEntity manageUnit ;
	
	@JsonProperty(value="cardtype")
	public String getCardtype() {
		return cardtype;
	}
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	
	@JsonProperty(value="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JsonProperty(value="roleName")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@JsonProperty(value="personName")
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@JsonProperty(value="jobpost")
	public String getJobpost() {
		return jobpost;
	}
	public void setJobpost(String jobpost) {
		this.jobpost = jobpost;
	}
	
	@JsonProperty(value="narcoticright")
	public String getNarcoticright() {
		return narcoticright;
	}
	public void setNarcoticright(String narcoticright) {
		this.narcoticright = narcoticright;
	}
	
	@JsonProperty(value="userId")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonProperty(value="userName")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@JsonProperty(value="gender")
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@JsonProperty(value="personId")
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	@JsonProperty(value="psychotropicright")
	public String getPsychotropicright() {
		return psychotropicright;
	}
	public void setPsychotropicright(String psychotropicright) {
		this.psychotropicright = psychotropicright;
	}
	
	@JsonProperty(value="organName")
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	
	@JsonProperty(value="educationbackground")
	public String getEducationbackground() {
		return educationbackground;
	}
	public void setEducationbackground(String educationbackground) {
		this.educationbackground = educationbackground;
	}
	
	@JsonProperty(value="roleId")
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@JsonProperty(value="iscancel")
	public String getIscancel() {
		return iscancel;
	}
	public void setIscancel(String iscancel) {
		this.iscancel = iscancel;
	}
	
	@JsonProperty(value="majorjob")
	public String getMajorjob() {
		return majorjob;
	}
	public void setMajorjob(String majorjob) {
		this.majorjob = majorjob;
	}
	
	@JsonProperty(value="expertcost")
	public String getExpertcost() {
		return expertcost;
	}
	public void setExpertcost(String expertcost) {
		this.expertcost = expertcost;
	}
	
	@JsonProperty(value="hometown")
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	
	@JsonProperty(value="joininwork")
	public String getJoininwork() {
		return joininwork;
	}
	public void setJoininwork(String joininwork) {
		this.joininwork = joininwork;
	}
	
	@JsonProperty(value="organizCode")
	public String getOrganizCode() {
		return organizCode;
	}
	public void setOrganizCode(String organizCode) {
		this.organizCode = organizCode;
	}
	
	@JsonProperty(value="email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonProperty(value="lastModifyDate")
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	
	@JsonProperty(value="manageUnitName")
	public String getManageUnitName() {
		return manageUnitName;
	}
	public void setManageUnitName(String manageUnitName) {
		this.manageUnitName = manageUnitName;
	}
	
	@JsonProperty(value="logoff")
	public String getLogoff() {
		return logoff;
	}
	public void setLogoff(String logoff) {
		this.logoff = logoff;
	}
	
	@JsonProperty(value="mobile")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@JsonProperty(value="organId")
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	
	@JsonProperty(value="birthday")
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	@JsonProperty(value="officeCode")
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	
	@JsonProperty(value="operationtype")
	public String getOperationtype() {
		return operationtype;
	}
	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}
	
	@JsonProperty(value="isexpert")
	public String getIsexpert() {
		return isexpert;
	}
	public void setIsexpert(String isexpert) {
		this.isexpert = isexpert;
	}
	
	@JsonProperty(value="majorqualify")
	public String getMajorqualify() {
		return majorqualify;
	}
	public void setMajorqualify(String majorqualify) {
		this.majorqualify = majorqualify;
	}
	
	@JsonProperty(value="education")
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	
	@JsonProperty(value="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty(value="manageUnitId")
	public String getManageUnitId() {
		return manageUnitId;
	}
	public void setManageUnitId(String manageUnitId) {
		this.manageUnitId = manageUnitId;
	}
	
	@JsonProperty(value="wbCode")
	public String getWbCode() {
		return wbCode;
	}
	public void setWbCode(String wbCode) {
		this.wbCode = wbCode;
	}
	
	@JsonProperty(value="domain")
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@JsonProperty(value="lastLoginTime")
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	@JsonProperty(value="ethnic")
	public String getEthnic() {
		return ethnic;
	}
	public void setEthnic(String ethnic) {
		this.ethnic = ethnic;
	}
	
	@JsonProperty(value="cardnum")
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	
	@JsonProperty(value="operationscope")
	public String getOperationscope() {
		return operationscope;
	}
	public void setOperationscope(String operationscope) {
		this.operationscope = operationscope;
	}
	
	@JsonProperty(value="majorname")
	public String getMajorname() {
		return majorname;
	}
	public void setMajorname(String majorname) {
		this.majorname = majorname;
	}
	
	@JsonProperty(value="address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@JsonProperty(value="displayName")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	@JsonProperty(value="prescriberight")
	public String getPrescriberight() {
		return prescriberight;
	}
	public void setPrescriberight(String prescriberight) {
		this.prescriberight = prescriberight;
	}
	
	@JsonProperty(value="role")
	public UserRoleEntity getRole() {
		return role;
	}
	public void setRole(UserRoleEntity role) {
		this.role = role;
	}
	
	@JsonProperty(value="manageUnit")
	public ManageUnitEntity getManageUnit() {
		return manageUnit;
	}
	public void setManageUnit(ManageUnitEntity manageUnit) {
		this.manageUnit = manageUnit;
	}
	
}
