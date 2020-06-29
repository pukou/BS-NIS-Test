package com.bsoft.nis.domain.visit;

import java.io.Serializable;

/**
 * Created by king on 2016/11/18.
 */
public class CheckState implements Serializable{

    /**
     * 巡视情况标识
     */
    public int DYXH;

    /**
     * 巡视情况名称
     */
    public String DYMS;

    /**
     * 作废标志,0正常，1作废
     */
    public int ZFBZ;
}
