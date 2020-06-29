package com.bsoft.nis.domain.nurseplan;

import com.bsoft.nis.domain.synchron.SelectResult;

import java.util.List;

/**
 * @ClassName: EvaluateAndRecord
 * @Description: 计划评价及记录
 * @author 吕自聪 lvzc@bsoft.com.cn
 * @date 2015-12-4 下午2:54:40
 * 
 */
public class EvaluateAndRecord {

	public List<Evaluate> PJLS;

	public List<Evaluate> PJXM;

	public boolean IsSync = false;

	public SelectResult SyncData;
}
