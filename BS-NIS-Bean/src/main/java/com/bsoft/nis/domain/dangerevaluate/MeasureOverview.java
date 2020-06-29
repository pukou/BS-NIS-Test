package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 风险措施
 * User: 苏泽雄
 * Date: 16/12/9
 * Time: 10:45:42
 */
public class MeasureOverview implements Serializable {

	private static final long serialVersionUID = -5051074434767278824L;

	// 措施单号
	public String CSDH;

	// 评估单号
	public String PGDH;

	// 措施单名称
	public String CSDMC;

	// 评估类型
	public String PGLX;

	// 风险措施记录数据
	public List<SimMeasureRecord> RECOORD;
}
