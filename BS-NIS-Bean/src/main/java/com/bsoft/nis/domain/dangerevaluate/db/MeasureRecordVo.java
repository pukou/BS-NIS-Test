package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description: 风险措施记录(IENR_FXCSJL)
 * User: 苏泽雄
 * Date: 16/12/12
 * Time: 1:57:54
 */
public class MeasureRecordVo implements Serializable {

	private static final long serialVersionUID = 1067105334188752915L;

	// 记录序号
	public String JLXH;

	// 住院号
	public String ZYH;

	// 病人病区
	public String BRBQ;

	// 措施单号
	public String BDXH;

	// 评估序号
	public String PGXH;

	// 措施评价
	public String CSPJ;

	// 创建时间
	public String CJSJ;

	// 创建工号
	public String CJGH;

	// 措施时间
	public String CSSJ;

	// 措施工号
	public String CSGH;

	// 护士长签名
	public String HSZQM;

	// 护士长签名时间
	public String HSZQMSJ;

	// 机构ID
	public String JGID;

	// 状态标志  0 作废  1 新增
	public String ZTBZ;
	//ADD 2018-5-4 19:34:53
//	public String ZGQK;

	/**
	 * 外部属性
	 */
	// 数据库类型  用于执行SQL语句时区分数据库类型
	public String dbtype;
}
