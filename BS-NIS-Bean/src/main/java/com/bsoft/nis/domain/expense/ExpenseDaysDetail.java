package com.bsoft.nis.domain.expense;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/27.
 * 按天获取到的项目明细
 */
public class ExpenseDaysDetail implements Serializable {
    private static final long serialVersionUID = -8829454911347245790L;
    /**
     * 费用单价
     */
    public String FYDJ;
    /**
     * 费用名称
     */
    public String FYMC;
    /**
     * 费用数量
     */
    public String FYSL;
    /**
     * 总金额
     */
    public String ZJJE;

}
