/**   
 * @Title: SpinnerDataInfo.java
 * @Package com.bsoft.mob.ienr.dynamicui.evaluate 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 吕自聪  lvzc@bsoft.com.cn
 * @date 2015-12-21 下午4:22:25 
 * @version V1.0   
 */
package com.bsoft.nis.domain.evaluation.dynamic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SpinnerDataInfo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 吕自聪 lvzc@bsoft.com.cn
 * @date 2015-12-21 下午4:22:25
 * 
 */
public class SpinnerDataInfo {
	@JsonProperty(value = "DropData")
	public List<DropData> datas = new ArrayList<>();

	public int ID = -1;

	public String Text;

	public String ParentID;

	public String Value;

	public String ValueType;

	public String NewLine;

	public String CtrlType = "SpinnerDataInfo";

	public String Font;

	public String IsScored;

	public String Score;

	public String GroupId;

	public int IsSelected;

	public String FrontId;

	public String PostpositionId;

	public String Jfgz;

	public String Xxdj;

	public String Dzlx;

	public String Dzbd;

	public String Dzxm;

	public String Dzbdlx;

	public String Btbz;
}
