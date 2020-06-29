package com.bsoft.nis.domain.bloodglucose.db;

import java.io.Serializable;

/**
 * Description:
 * User: 苏泽雄
 * Date: 17/1/4
 * Time: 11:22:20
 */
public class BGRecordVo implements Serializable {

	private static final long serialVersionUID = -3145788001600245186L;

	// 记录序号
	public String JLXH;

	// 创建工号
	public String JLGH;

	// 住院号
	public String ZYH;

	// 病人姓名
	public String BRXM;

	// 科室代码
	public String KSDM;

	// 病人病区
	public String BRBQ;

	// 病人床号
	public String BRCH;

	// 计划日期
	public String JHRQ;

	// 创建时间
	public String JLSJ;

	// 胰岛素名称
	public String YDSMC;

	// 医嘱序号
	public String YZBXH;

	// 机构ID
	public String JGID;

	// 数据库类型
	public String dbtype;
}
