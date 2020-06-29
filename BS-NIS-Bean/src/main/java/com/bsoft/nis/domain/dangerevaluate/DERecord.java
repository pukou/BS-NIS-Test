package com.bsoft.nis.domain.dangerevaluate;

import com.bsoft.nis.domain.synchron.SelectResult;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 风险记录
 * User: 苏泽雄
 * Date: 16/12/2
 * Time: 9:34:34
 */
public class DERecord implements Serializable {

	private static final long serialVersionUID = 3363537075998314335L;

	// 评估序号
	public String PGXH;

	// 评估单号
	public String PGDH;

	// 病人床号
	public String BRCH;

	// 病人姓名
	public String BRXM;

	// 表单名称
	public String BDMC;

	// 评估时间
	public String PGSJ;

	// 评估总分
	public String PGZF;

	// 评估工号 员工代码
	public String PGGH;

	// 评估护士 姓名
	public String PGHS;

	// 质控描述
	public String ZKMS;

	// 质控规则
	public List<QualityControl> ZKGZ;

	// 风险因子
	public List<DEFactor> FXYZ;

	// 评估类型
	public String PGLX;

	// 护士长签名
	public String HSZQM;

	// 判断同步结果，是否需要处理
	public boolean IsSync = false;

	// 同步数据
	public SelectResult SyncData;
}
