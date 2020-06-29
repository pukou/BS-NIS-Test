package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;

/**
 * Description: 因子评分
 * User: 苏泽雄
 * Date: 16/12/2
 * Time: 15:41:14
 */
public class FactorGoal implements Serializable {

	private static final long serialVersionUID = -5604917722504533982L;

	// 明细序号 风险评估明细表主键
	public String MXXH;

	// 分值序号 风险评估因子表主键
	public int FZXH;

	// 风险因子
	public String FXYZ;

	// 输入标志 是选择还是手工输入类型  0:非输入  1:输入
	public String SRBZ;

	// 评分分值
	public String PFFZ;

	// 分值描述
	public String FZMS;

	// 分值上限
	public String FZSX;

	// 分值下限
	public String FZXX;

	// 备注信息
	public String BZXX;

	// 是否选择
	public boolean SELECT;
}
