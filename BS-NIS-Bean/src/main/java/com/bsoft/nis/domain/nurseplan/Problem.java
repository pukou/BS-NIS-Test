package com.bsoft.nis.domain.nurseplan;

import com.bsoft.nis.domain.synchron.SelectResult;

import java.util.List;

/**
 * Description: 问题
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-15
 * Time: 14:32
 * Version:
 */
public class Problem {

	public String JLWT;

	public String WTXH;

	public String GLXH;

	public String WTMS;
	
	public String WTLX;
	
	public String XGYS;

	public String YSWS;

	public String KSSJ;

	public List<RelevantFactor> XGYSList;

	public List<DiagnosticBasis> ZDYJ;

	public List<Goal> JHMB;

	public List<Measure> JHCS;

	public List<Measure> JHCSTemplate;

	public String PJZT;

	public boolean IsSync = false;

	public SelectResult SyncData;
}
