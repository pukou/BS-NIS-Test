package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description:
 * User: 苏泽雄
 * Date: 16/12/12
 * Time: 16:58:22
 */
public class PERecordVo implements Serializable {

	private static final long serialVersionUID = 3518430218406002795L;

	// 记录项目
	public String JLXM;

	// 机构ID
	public String JGID;

	// 住院号
	public String ZYH;

	// 评估序号
	public String PGXH;

	// 项目序号
	public String XMXH;

	// 选项序号
	public String XXXH;

	// 项目取值
	public String XMQZ;
}
