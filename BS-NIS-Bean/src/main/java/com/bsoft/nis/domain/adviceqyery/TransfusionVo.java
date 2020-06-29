package com.bsoft.nis.domain.adviceqyery;

import java.io.Serializable;

/**
 * Description: 输液单
 * User: 苏泽雄
 * Date: 17/1/9
 * Time: 16:10:50
 */
public class TransfusionVo implements Serializable {

	private static final long serialVersionUID = 762791068970317314L;

	// 输液单号
	public String SYDH;
	// 条码编号
	public String TMBH;
	// 输液时间
	public String SYSJ;
	// 开始时间
	public String KSSJ;
	// 开始工号（人名）
	public String KSGH;
	// 结束时间
	public String JSSJ;
	// 结束工号（人名）
	public String JSGH;
	// 输液状态
	public int SYZT;
	// 平均滴速
	public String PJDS;
	// 住院号
	public String ZYH;
}
