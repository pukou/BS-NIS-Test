/**   
* @Title: CheckForm.java 
* @Package com.bsoft.mob.ienr.model.evaluate 
* @Description: TODO(用一句话描述该文件做�?�?) 
* @author 吕自�?  lvzc@bsoft.com.cn
* @date 2015-12-17 下午3:17:46 
* @version V1.0   
*/ 
package com.bsoft.nis.domain.evaluation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/** 
 * @ClassName: CheckForm 
 * @Description: TODO(������һ�仰��������������)
 * @author ���Դ� lvzc@bsoft.com.cn
 * @date 2015-12-17 ����3:17:46
 *  
 */
public class EvaluateCheckForm {
	@JsonProperty(value = "CheckCls")
	public List<CheckCls> CLS;
}
