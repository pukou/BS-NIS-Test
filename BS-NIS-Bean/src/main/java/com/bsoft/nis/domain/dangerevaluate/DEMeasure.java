package com.bsoft.nis.domain.dangerevaluate;

import com.bsoft.nis.domain.synchron.SelectResult;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 风险评估措施
 * User: 苏泽雄
 * Date: 16/12/5
 * Time: 10:30:01
 */
public class DEMeasure implements Serializable {

	private static final long serialVersionUID = -1993293409460058708L;

	// 风险措施
	public MeasureRecord RECORD;

	// 措施评价
	public List<DEEvaluate> EVALUATE;

	// 判断同步结果，是否需要处理
	public boolean IsSync = false;

	// 同步数据
	public SelectResult SyncData;
}
