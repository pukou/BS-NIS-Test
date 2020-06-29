package com.bsoft.nis.domain.evaluation.dynamic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
//对应护理评估YSFL类
public class 	Classification {

	public Integer ID = 0;

	public String NText;

	public String IsScored;

	public String Score;

	public String HSQM1;

	public String HSQM2;

	public String Dzlx;

	public String Dzbd;

	public String Dzxm;

	public String Dzbdlx;

	public String Btbz;

	/**
	 * 独立标志，0或“”混合签，1独立签名
	 */
	public String DLBZ;

	/**
	 * 1 双签，非1 单签
	 */
	public String QMBZ;

	@JsonProperty(value = "ItemNode")
	public List<ItemNode> itemNodes;

	public String FLLX ="0";
	public String XSFLLX = "0";
	public boolean modFlag = false;

}
