package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 风险措施记录
 * User: 苏泽雄
 * Date: 16/12/5
 * Time: 10:34:38
 */
public class MeasureRecord implements Serializable {

	private static final long serialVersionUID = -2440009641917024298L;

	// 记录序号
	public String JLXH;

	// 表单序号-措施单号
	public String BDXH;

	// 评估序号
	public String PGXH;

	// 措施时间
	public String CSSJ;

	// 措施工号
	public String CSGH;

	// 护士姓名
	public String HSXM;

	// 护士长签名
	public String HSZQM;

	// 护士长姓名
	public String HSZXM;

	// 护士长签名时间
	public String HSZQMSJ;

	// 措施评价
	public String CSPJ;

	// 病人病区
	public String BRBQ;

	// 措施项目
	public List<MeasureItem> CSXM;

	// 是否启用评价
	public String SFPJ;
	//ADD 2018-5-2 22:39:17
//	public String ZGQK;

	public boolean CustomIsSync;
}
