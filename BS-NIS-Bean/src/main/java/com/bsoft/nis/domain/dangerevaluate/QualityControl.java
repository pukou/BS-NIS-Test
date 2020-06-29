package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;

/**
 * Description: 质控规则
 * User: 苏泽雄
 * Date: 16/12/2
 * Time: 15:37:09
 */
public class QualityControl implements Serializable {

	private static final long serialVersionUID = -5833703895055591157L;

	// 质控描述
	public String ZKMS;

	// 分值上限
	public int FZSX;

	// 分值下限
	public int FZXX;

	// 措施标志  0:不需要提示填写措施  1:需要提示填写措施
	public String CSBZ;
}
