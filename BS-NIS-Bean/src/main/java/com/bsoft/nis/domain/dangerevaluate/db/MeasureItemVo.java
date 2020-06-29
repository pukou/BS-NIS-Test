package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description: 风险措施项目(IENR_FXCSXM)
 * User: 苏泽雄
 * Date: 16/12/12
 * Time: 2:01:13
 */
public class MeasureItemVo implements Serializable {

	private static final long serialVersionUID = -2388577775029680876L;

	// 记录项目
	public String JLXM;

	// 记录序号
	public String JLXH;

	// 措施序号
	public String CSXH;

	// 自定义标志
	public String ZDYBZ;

	// 项目内容
	public String XMNR;
}
