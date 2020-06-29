/**   
 * @Title: DiagnosticBasis.java 
 * @Package com.bsoft.mob.ienr.model.nurseplan 
 * @Description: 护理计划问题诊断依据 
 * @author 吕自聪  lvzc@bsoft.com.cn
 * @date 2016-5-16 下午1:35:11 
 * @version V1.0   
 */
package com.bsoft.nis.domain.nurseplan;

/**
 * Description: 问题诊断依据
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-15
 * Time: 14:32
 * Version:
 */
public class DiagnosticBasis {

	public String JLZD;
	
	public String ZDXH;

	public String ZDMS;//描述

	public String ZDYBZ;//是否自定义 0否 1 是

	public boolean SELECTED;

	public boolean MODIFIED;
}
