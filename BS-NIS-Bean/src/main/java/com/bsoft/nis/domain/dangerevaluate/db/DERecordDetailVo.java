package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description: 风险评估明细(IENR_FXPGMX)
 * User: 苏泽雄
 * Date: 16/12/9
 * Time: 9:39:11
 */
public class DERecordDetailVo implements Serializable {

	private static final long serialVersionUID = 8644123015065071819L;

	// 明细序号
	public String MXXH;

	// 评估序号
	public String PGXH;

	// 风险因子
	public String FZYZ;

	// 分值序号
	public String FZXH;

	// 评分分值
	public String PFFZ;
}
