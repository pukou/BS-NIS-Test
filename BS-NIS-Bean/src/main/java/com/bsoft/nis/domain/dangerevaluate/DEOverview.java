package com.bsoft.nis.domain.dangerevaluate;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 风险评估表单类型
 * User: 苏泽雄
 * Date: 16/12/1
 * Time: 16:46:26
 */
public class DEOverview implements Serializable {

	private static final long serialVersionUID = -1979013995622020163L;

	// 评估单号
	public String PGDH;

	// 评估单名称
	public String PGDMC;

	// 评估描述：上次（）分，*度危险，填写频率，已是第几天
	public String PGMS;

	// 填写计划：0：今日不许填写，1：今日可能要填写，2：今日必须填写
	public String TXJH;

	// 评估类型
	public String PGLX;

	// 评估记录
	public List<SimDERecord> PGJL;

	// 提醒日期
	public String TXRQ;
}
