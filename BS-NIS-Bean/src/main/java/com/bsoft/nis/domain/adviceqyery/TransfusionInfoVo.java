package com.bsoft.nis.domain.adviceqyery;

import java.io.Serializable;

/**
 * Description: 输液明细
 * User: 苏泽雄
 * Date: 17/1/9
 * Time: 16:11:33
 */
public class TransfusionInfoVo implements Serializable{

	private static final long serialVersionUID = -4695587858481514714L;

	// 记录序号（医嘱序号）
	public String YZXH;
	// 输液单号
	public String SYDH;
	// 医嘱名称
	public String YZMC;
	// 剂量信息
	public String JLXX;
	// 数量信息
	public String SLXX;
}
