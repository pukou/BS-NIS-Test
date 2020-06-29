package com.bsoft.nis.domain.adviceqyery;

import java.io.Serializable;

/**
 * Description: 输液巡视记录
 * User: 苏泽雄
 * Date: 17/1/20
 * Time: 14:47:55
 */
public class TransfusionPatrolRecord implements Serializable {

	private static final long serialVersionUID = 5641821334409057286L;

	// 输液单号
	public String SYDH;

	// 巡视时间
	public String XSSJ;

	// 巡视工号
	public String XSGH;

	// 输液滴速
	public String SYDS;

	// 输液反应  IENR_CYDY.DYXH
	public String SYFY;

	// 机构ID
	public String JGID;

	// 巡视姓名
	public String XSXM;

	// 输液反应名称
	public String FYMC;
}
