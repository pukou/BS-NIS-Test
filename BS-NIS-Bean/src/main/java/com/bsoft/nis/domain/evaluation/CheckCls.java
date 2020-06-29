/**   
 * @Title: CheckCls.java 
 * @Package com.bsoft.mob.ienr.model.evaluate 
 * @Description: TODO(用一句话描述该文件做�?�?) 
 * @author 吕自�?  lvzc@bsoft.com.cn
 * @date 2015-12-17 下午3:10:40 
 * @version V1.0   
 */
package com.bsoft.nis.domain.evaluation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @ClassName: CheckCls
 * @Description: TODO
 * @author ���Դ� lvzc@bsoft.com.cn
 * @date 2015-12-17 ����3:10:40
 * 
 */
public class CheckCls {
	public int FLID = -1;

	@JsonProperty(value = "CheckItem")
	public List<CheckItem> ITEMS;
}
