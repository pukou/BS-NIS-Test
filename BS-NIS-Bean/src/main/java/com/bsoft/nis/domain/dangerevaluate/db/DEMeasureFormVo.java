package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description: 风险措施单(IENM_FXCSD)
 * User: 苏泽雄
 * Date: 16/12/6
 * Time: 17:38:04
 */
public class DEMeasureFormVo implements Serializable {

	private static final long serialVersionUID = -4439421656638492423L;

	// 措施单号
	public String CSDH;

	// 评估单号
	public String PGDH;

	// 措施单名称
	public String CSDMC;

	// 启用时间
	public String QYSJ;

	// 启用标志
	public String QYBZ;

	// 作废标志
	public String ZFBZ;

	// 机构ID
	public String JGID;

	// 每页打印行数
	public String MYHS;

	// 是否启用评价  0 不启用评价  1 启用评价
	public String SFPJ;

	// 风险分向  0 表示分数越高，措施项目越多  1 表示分数越低，措施项目越多
	public String FXFX;
}
