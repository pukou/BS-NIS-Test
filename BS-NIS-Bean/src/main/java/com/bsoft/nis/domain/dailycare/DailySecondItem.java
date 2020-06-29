package com.bsoft.nis.domain.dailycare;

import java.io.Serializable;

/**
 * Created by king on 2016/10/28.
 */
public class DailySecondItem implements Serializable{

    private static final long serialVersionUID = 9081259405051039729L;
    /**
     * 一级序号
     */
    public String LBBS;

    /**
     * 序号
     */
    public String XMBS;
    /**
     * 名称
     */
    public String XMMC;

    /**
     * 是否选中
     */
    public boolean checked;
}
