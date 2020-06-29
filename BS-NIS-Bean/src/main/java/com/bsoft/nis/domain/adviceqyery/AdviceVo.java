package com.bsoft.nis.domain.adviceqyery;

import java.io.Serializable;

/**
 * Description:
 * User: 苏泽雄
 * Date: 17/1/6
 * Time: 15:32:42
 */
public class AdviceVo implements Serializable {

	private static final long serialVersionUID = 2996620620552804435L;

	// 记录序号（医嘱序号）
	public String JLXH;

	// 药房识别
	public String YFSB;

	// 医嘱名称
	public String YZMC;

	// 药品序号
	public String YPXH;

	// 开始时间
	public String KZSJ;

	// 停嘱时间
	public String TZSJ;

	// 药品单价
	public String YPDJ;

	// 备注信息
	public String BZXX;

	// 医嘱组号
	public String YZZH;

	// 临时医嘱
	public int LSYZ;

	// 项目类型
	public String XMLX;

	// 药品类型
	public String YPLX;

	// 数量单位
	public String SLDW;

	// 剂量单位
	public String JLDW;

	// 使用频次名称
	public String SYPCMC;

	// 药品用法名称
	public String YPYFMC;

	// 开嘱医生名称
	public String KZYSMC;

	// 停嘱医生名称
	public String TZYSMC;

	// 医嘱大类
	public String YZDL;

	// 一次剂量描述  去掉小数点后面的0
	public String YCJLMS;

	// 一次数量描述  去掉小数点后面的0
	public String YCSLMS;

	// 文本颜色  true 新开(蓝色)  false 新停(红色)  空 两者均不是
	public String TEXTCOLOR;

	// 无效标志
	public String WXBZ;
}
