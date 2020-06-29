package com.bsoft.nis.domain.adviceqyery.db;

import java.io.Serializable;

/**
 * Description:
 * User: 苏泽雄
 * Date: 17/1/9
 * Time: 18:34:48
 */
public class TransfusionInfoVoTemp implements Serializable {

	private static final long serialVersionUID = -6690096770980811793L;

	// 记录序号（医嘱序号）
	public String YZXH;
	// 输液单号
	public String SYDH;
	// 一次剂量
	public String YCJL;
	// 剂量单位
	public String JLDW;
	// 一次数量
	public String YCSL;
	// 数量单位
	public String SLDW;

	// 开始时间
	public String KSSJ;
}
