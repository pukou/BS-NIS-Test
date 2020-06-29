package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description: 风险评估质控(IENR_FXPGZK)
 * User: 苏泽雄
 * Date: 16/12/2
 * Time: 9:53:37
 */
public class DEQualityControlVo implements Serializable {

	private static final long serialVersionUID = -5308214672332049996L;

	// 质控序号
	public String ZKXH;

	// 评估单号
	public String PGDH;

	// 质控描述
	public String ZKMS;

	// 风险程度  MOB_XTPZ DMLB = 454
	public String FXCD;

	// 分值上限  分值<分值下限
	public Integer FZSX;

	// 分值下限  分值>=分值下限
	public Integer FZXX;

	// 质控周期
	public String ZKZQ;

	// 措施标志  0:不需要提示填写措施  1:需要提示填写措施
	public String CSBZ;

	// 周期频次
	public String ZQPC;

	// 启用标志
	public String QYBZ;

	// 启用时间
	public String QYSJ;

	// 作废标志
	public String ZFBZ;
}
