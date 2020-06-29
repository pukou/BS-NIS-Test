package com.bsoft.nis.domain.evaluation.dynamic;

import com.bsoft.nis.domain.evaluation.AllowSave;
import com.bsoft.nis.domain.evaluation.EvaluateCheckForm;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

//对应护理评估BDYS类
public class Form {

	public String ID;

	public String NText;

	public String IsScored;

	public String Score;

	public String YSXH;

	public String TXGH;
	//add 2018-5-9 16:42:15
	public String TXSJ;

	public String YSLX;

	public String QMGH;

	public String SYZT;
	
	public String Dzlx;
	
	public String Dzbd;
	
	public String Dzxm;
	
	public String Dzbdlx;
	
	public String Btbz;
	
	/**
	 * 0:内部，1：emr（外部）
	 */
	public String LYBS;

	@JsonProperty(value = "Classification")
	public List<Classification> clazzs;

	/**
	 * 本地属性
	 */
	public boolean globalSign = false;

	@JsonProperty(value = "AllowSave")
	public AllowSave save = new AllowSave();

	public EvaluateCheckForm checkForm;

	public boolean modFlag = false;
}
