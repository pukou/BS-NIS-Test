package com.bsoft.nis.domain.core.synchron.rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:同步目标(IENR_TBMB)
 * Created: dragon
 * Date： 2016/12/28.
 */
public class SynchronMission implements Serializable{
    private static final long serialVersionUID = -1601245688743915325L;
    /**
     * 目标序号
     */
    public Long MBXH;
    /**
     * 同步序号
     */
    public Long TBXH;
    /**
     * 目标项目
     */
    public Long MBXM;
    /**
     * 目标项目名称
     */
    public String MBXMMC;
    /**
     * 排列顺序
     */
    public Integer PLSX;
    /**
     * 作废标识
     */
    public Integer ZFBZ;

    /**
     * 目标来源项目
     */
    public List<SynchronSource> missionSources = new ArrayList<>();
}
