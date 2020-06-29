package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;

/**
 * Description: 风险措施项目
 * User: 苏泽雄
 * Date: 16/12/5
 * Time: 10:37:20
 */
public class MeasureItem implements Serializable {

	private static final long serialVersionUID = -7043338153884236256L;

	// 记录项目
	public String JLXM;

	// 措施序号
	public String CSXH;

	// 自定义标志  0 否  1 是
	public String ZDYBZ;

	// 项目内容
	public String XMNR;

	// 组名称
	public String ZMC;

	// 是否选择
	public boolean SELECT;

	// 必填标识
	public boolean BTBZ;

	// 项目组号
	public String XMZH;
}
