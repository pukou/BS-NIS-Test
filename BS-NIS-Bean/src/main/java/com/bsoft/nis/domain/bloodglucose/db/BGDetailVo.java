package com.bsoft.nis.domain.bloodglucose.db;

import com.bsoft.nis.domain.bloodglucose.BloodGlucoseDetail;

import java.io.Serializable;

/**
 * Description: 血糖记录明细(IENR_XTJLMX)
 * User: 苏泽雄
 * Date: 16/12/27
 * Time: 10:38:08
 */
public class BGDetailVo implements Serializable {

	private static final long serialVersionUID = 3716032406032378942L;

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

	// 状态标志  0 未执行  1 已执行
	public String ZTBZ;

	// 数据库类型
	public String dbtype;

	public BGDetailVo() {

	}

	// 将BloodGlucoseDetail转化为BGDetailVo
	public BGDetailVo(BloodGlucoseDetail detail) {
		this.MXXH = detail.MXXH;
		this.JLXH = detail.JLXH;
		this.XMXH = detail.XMXH;
		this.XMLX = detail.XMLX;
		this.XMDW = detail.XMDW;
		this.XMNR = detail.XMNR;
		this.JHNR = detail.JHNR;
		this.JHBZ = detail.JHBZ;
		this.JLSJ = detail.JLSJ;
		this.JLGH = detail.JLGH;
		this.ZTBZ = detail.ZTBZ;
	}
}
