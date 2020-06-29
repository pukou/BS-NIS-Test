package com.bsoft.nis.domain.adviceexecute;

import java.io.Serializable;

/**
 * Description: 费用规则对象
 * User: 苏泽雄
 * Date: 17/1/13
 * Time: 10:59:41
 */
public class FeeRule implements Serializable {

	private static final long serialVersionUID = -5998408713920880087L;

	// 费用序号
	public String FYXH;

	// 规则序号
	public String GZBH;

	// 药品用法
	public String YPYF;

	// 计费类型  1 一天内前N次收费  2 按次计费  3 按天收费  4 按小时收费
	public String JFLX;

	// 费用限制
	public String FYXZ;

	// 计费单元
	public String JFDY;

	// 单元单位  1 次  2 天  3 时  4 元
	public String DYDW;

	// 病人类型  1 成人  2 儿童
	public String BRLX;

	// 机构ID
	public String JGID;

	// 优先级别
	public String YXJB;

	// 计费组号
	public String JFZH;

	// 作废标志  0 正常  1 作废
	public String ZFBZ;
}
