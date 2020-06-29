package com.bsoft.nis.domain.patient.db;

import com.bsoft.nis.domain.BaseVo;

/**
 * Describtion:总费用
 * Created: dragon
 * Date： 2016/10/19.
 */
public class ExpenseTotal extends BaseVo{
    private static final long serialVersionUID = -7691320924608291774L;
    /**
     * 总费用
     */
    public String ZJJE;
    /**
     * 自负金额
     */
    public String ZFJE;
    /**
     * 交款金额
     */
    public String JKJE;
    /**
     * 费用余额
     */
    public String FYYE;
}
