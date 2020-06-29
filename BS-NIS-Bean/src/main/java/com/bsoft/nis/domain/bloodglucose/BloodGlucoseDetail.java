package com.bsoft.nis.domain.bloodglucose;

import java.io.Serializable;

/**
 * Description: 血糖记录明细
 * User: 苏泽雄
 * Date: 16/12/23
 * Time: 17:34:03
 */
public class BloodGlucoseDetail implements Serializable {

	private static final long serialVersionUID = -9107849858015618849L;

	// 明细序号
	public String MXXH;

	// 记录序号
	public String JLXH;

	// 项目序号  MOB_XTPZ  血糖 DMLB=458  胰岛素 DMLB=459
	public String XMXH;

	// 项目类型  1 血糖记录  2 胰岛素
	public String XMLX;

	// 项目单位
	public String XMDW;

	// 项目内容
	public String XMNR;

	// 计划内容  只要是针对胰岛素计划使用的剂量
	public String JHNR;

	// 计划标志  0 否  1 是
	public String JHBZ;

	// 记录时间
	public String JLSJ;

	// 记录工号
	public String JLGH;

	// 记录护士姓名
	public String JLXM;

	// 状态标志  0 未执行  1 已执行
	public String ZTBZ;

	// 计划日期
	public String JHRQ;

	// 胰岛素名称
	public String YDSMC;
}
