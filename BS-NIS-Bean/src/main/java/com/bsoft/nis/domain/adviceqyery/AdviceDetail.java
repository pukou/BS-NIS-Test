package com.bsoft.nis.domain.adviceqyery;

import java.io.Serializable;

/**
 * Description:
 * User: 苏泽雄
 * Date: 17/1/9
 * Time: 14:53:51
 */
public class AdviceDetail implements Serializable {

	private static final long serialVersionUID = -7927291439192425396L;

	// 归属类型
	public String GSLX;

	// 执行状态  0 非  1 已执行  2 running  4 暂停  5 拒绝
	public int ZXZT;

	// 计划时间
	public String JHSJ;

	// 开始时间
	public String KSSJ;

	// 开始工号
	public String KSGH;

	// 结束工号
	public String JSGH;

	// 结束时间
	public String JSSJ;
}
