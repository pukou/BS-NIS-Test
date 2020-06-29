package com.bsoft.nis.domain.advicesplit.advice.db;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Describtion:病区医嘱(V_MOB_HIS_BQYZ)
 * Created: dragon
 * Date： 2016/12/8.
 */
public class Advice implements Serializable{
	/*
	QRSJ, JLXH, YFSB, YZMC, YCJL, YPXH, YCSL, SYPC, YPYF, KZSJ, TZSJ,
				   KZYS, TZYS, YPDJ, BZXX, YZZH, LSYZ, XMLX, YPLX, LSBZ, ZFBZ
	 */
    private static final long serialVersionUID = 3217957455537994929L;
    public String JGID;
    public String JLXH;
    public String ZYH;
    public String YEPB;
    public String YEWYH;
    public String YZMC;
    public String YPXH;
    public String XMLX;
    public String YPLX;
    public String YCJL;
    public String JLDW;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date KZSJ;
    public String KZYS;
    public String QRSJ;
    public String SYBZ;
    public String YPDJ;
    public String YPYF;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date TZSJ;
    public String SYPC;
    public String TZYS;
    public String YZZH;
    public String YZZX;
    public String YZPX;
    public String FYSX;
    public String LSYZ;
    public String LSBZ;
    public String YZPB;
    public String ZFBZ;
    public String FYFS;
    public String ZXSJ;
    public String YSBZ;
    public String YSTJ;
    public String YSYZBH;
    public String PSPB;
    public String PSJG;
    public String SRCS;
    public String YZZT;
    public String FHBZ;
    public String FHGH;
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    public Date FHSJ;
    public Date TZFHSJ;
    public String TZFHBZ;
    public String MRCS;
    public String YPCD;

    public String JFBZ;
    /**
     * 数量单位
     */
    public String SLDW;
    /**
     * 费用归并
     */
    public String FYGB;
    /**
     * 双人核对标识
     */
    public String SRHDBZ;

    public String YCSL;

    /**
     * 病人病区
     */
    public String BRBQ;
}
