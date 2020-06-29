package com.bsoft.nis.domain.dangerevaluate.db;

import java.io.Serializable;

/**
 * Description: 风险评估措施(IENR_FXPGCS)
 * User: 苏泽雄
 * Date: 16/12/7
 * Time: 11:30:02
 */
public class DEMeasureItemVo implements Serializable {

	private static final long serialVersionUID = -6963275551425024650L;

	// 措施序号
	public String CSXH;

	// 措施单号
	public String CSDH;

	// 项目内容
	public String XMNR;

	// 排列顺序
	public String PLSX;

	// 备注信息
	public String BZXX;

	// 独立项目  0 不独立  1 独立
	public String DLXM;

	// 组名称
	public String ZMC;

	// 项目组号
	public String XMZH;

	// 分值上线   分值<分值上线
	public String FZSX;

	// 分值下线   分值>=分值下线
	public String FZXX;

	// 作废标志
	public String ZFBZ;


	/**
	 * 获取措施记录时会用到的外部属性
 	 */
	// IENM_FXCSXM的JLXH
	public String JLXH;
	// IENM_FXCSXM的JLXM
	public String JLXM;
	// 自定义标志  0 否  1 是
	public String ZDYBZ;
}
