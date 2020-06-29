package com.bsoft.nis.domain.dangerevaluate;


import com.bsoft.nis.domain.nurseplan.CSMS_Bean;
import com.bsoft.nis.domain.nurseplan.ZDMS_Bean;

import java.util.List;

/**
 * 2017-5-5 12:07:54 护理计划记录
 */
public class HLJHJLBean {
	public String JLWT;
	public String WTXH;
	public String GLXH;
	public String GLLX;
	public String XGYS;
	public String WTMS;
	public String JHWTXH;
	public String KSSJ;
	//ADD
	public List<ZDMS_Bean> ZDMS_List;
	public List<CSMS_Bean> CSMS_List;

}
