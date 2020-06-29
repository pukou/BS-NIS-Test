package com.bsoft.nis.domain.adviceqyery.db;

import com.bsoft.nis.domain.adviceqyery.TransfusionPatrolRecord;

import java.io.Serializable;

/**
 * Description: 输液巡视记录
 * User: 苏泽雄
 * Date: 17/1/20
 * Time: 14:47:55
 */
public class TransfusionPatrolRecordVo implements Serializable {

	private static final long serialVersionUID = 5641821334409057286L;

	// 巡视标识
	public String XSBS;

	// 输液单号
	public String SYDH;

	// 巡视时间
	public String XSSJ;

	// 巡视工号
	public String XSGH;

	// 输液滴速
	public String SYDS;

	// 输液反应  IENR_CYDY.DYXH
	public String SYFY;

	// 机构ID
	public String JGID;

	// 数据库类型
	public String dbtype;

	public TransfusionPatrolRecordVo(TransfusionPatrolRecord record) {
		this.SYDH = record.SYDH;
		this.XSSJ = record.XSSJ;
		this.XSGH = record.XSGH;
		this.SYDS = record.SYDS;
		this.SYFY = record.SYFY;
		this.JGID = record.JGID;
	}
}
