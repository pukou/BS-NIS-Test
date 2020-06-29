package com.bsoft.nis.domain.lifesign;

import com.bsoft.nis.domain.synchron.SelectResult;

import java.io.Serializable;
import java.util.List;

public class LifeSignSync implements Serializable {

	private static final long serialVersionUID = -6347697370571502639L;
	public String TeamID;
	public String JLBH;
	public Boolean IsSync = false;
	public SelectResult SyncData;
	//add by louis  存放保存的体征李列表 2017年6月6日13:39:34
	public List<LifeSignRealSaveDataItem> mLifeSignRealSaveDataItemList;
}
