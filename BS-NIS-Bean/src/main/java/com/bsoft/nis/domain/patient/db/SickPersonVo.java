package com.bsoft.nis.domain.patient.db;


import com.bsoft.nis.domain.BaseVo;
import com.bsoft.nis.domain.patient.State;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @version 创建时间：2013-12-3 下午4:27:25
 * @类说明 病人信息对象
 */
public class SickPersonVo extends BaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 住院号
	 */
	public String ZYH;
	/**
	 * 住院号码
	 */
	public String ZYHM;
	/**
	 * 病人姓名
	 */
	public String BRXM;
	/**
	 * 病人性别
	 */
	public int BRXB;
	/**
	 * 出生年月
	 */
	public String CSNY;
	/**
	 * 病人床号
	 */
	public String BRCH;
	/**
	 * 护理级别,默认没有级别
	 */
	public int HLJB = -1;
	/**
	 * 病人年龄
	 */
	public String BRNL;
	/**
	 * 
	 */
	public String RFID;

	/**
	 * 入院日期
	 */
	public String RYRQ;

	/******* 标本采集病人列表特有 ********/
	/**
	 * 病人病区
	 */
	public String BRBQ;

	/**
	 * 发放状态 0 没有发放记录 1 全部已发放 2 部分发放部分未发放
	 */
	public int FFZT;

	/**
	 * 机构ID
	 */
	public String JGID;

	/******* 体征采集病人列表特有 ********/
	/**
	 * 体征项目,格式7,5,6
	 */
	public String TZXM;

	/**
	 * 病人状态
	 */
	public String BRZT;

	/*
	升级编号【56010014】============================================= start
	今日出院的病人，系统会提早出院，但是该病人当天的药品需要执行完，目前PDA无法处理该类病人的医嘱执行。（需要将当日出院的病人也显示在病人类别中，并标识）
	================= Classichu 2017/10/11 16:57
	6.
	*/
	/**
	 * 出院判别
	 */
	public String CYPB;
	public String ZZYS;
	public String BRXX;
	public String BRZDLB;
	public String BRZDMC;
	public String LCLJ;
	/* =============================================================== end */
	public ArrayList<State> state;
	public List<RecondBean> recondBeanList4Sicker;
	public List<ZKbean> zKbeanList4Sicker;
	public boolean hasGMYP;
	public int dcjCount;

	/**
	 * 增加显示床号
	 */
	public String XSCH;
	@Override
	public boolean equals(Object o) {
		return this.ZYH.equals(((SickPersonVo) o).ZYH);
	}

}
