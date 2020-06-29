/**   
 * @Title: CheckItem.java 
 * @Package com.bsoft.mob.ienr.model.evaluate 
 * @Description: TODO(鐢ㄤ竴鍙ヨ瘽鎻忚堪璇ユ枃浠跺仛浠?涔?) 
 * @author 鍚曡嚜鑱?  lvzc@bsoft.com.cn
 * @date 2015-12-17 涓嬪崍3:09:42 
 * @version V1.0   
 */
package com.bsoft.nis.domain.evaluation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @ClassName: CheckItem
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 吕自聪 lvzc@bsoft.com.cn
 * @date 2015-12-17 下午3:09:42
 * 
 */
public class CheckItem {
	public int XMID = -1;

	public String XMMC;

	public String TXBZ;

	@JsonProperty(value = "ItemChild")
	public List<ItemChild> ITEM;
}
