package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 风险因子
 * User: 苏泽雄
 * Date: 16/12/2
 * Time: 15:39:31
 */
public class DEFactor implements Serializable {

	private static final long serialVersionUID = 2877038741056327826L;

	// 风险因子
	public String FXYZ;

	// 评估单号
	public String PGDH;

	// 因子描述
	public String YZMS;

	// 单选标识  0:多选  1:单选
	public String DXBZ;
	//必选标志
	public String BXBZ;

	// 因子评分
	public List<FactorGoal> YZPF;
}
