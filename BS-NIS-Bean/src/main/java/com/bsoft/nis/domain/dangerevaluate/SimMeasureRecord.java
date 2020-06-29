package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;

/**
 * Description: 风险措施记录简略信息
 * User: 苏泽雄
 * Date: 16/12/9
 * Time: 10:46:13
 */
public class SimMeasureRecord implements Serializable {

	private static final long serialVersionUID = 2808729156476301555L;

	// 记录序号
	public String JLXH;

	// 表单序号
	public String BDXH;

	// 评估序号
	public String PGXH;

	// 措施工号
	public String CSGH;

	// 措施项目
	public String CSXM;

	// 措施时间
	public String CSSJ;

	// 护士长签名
	public String HSZQM;
	//ADd 2018-5-5 17:18:03
	public String CSPJ;
//	public String ZGQK;
	public String SFPJ;
}
