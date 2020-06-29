package com.bsoft.nis.domain.expense;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/27.
 * @类说明 费用汇总
 */
public class ExpenseVo implements Serializable {
    private static final long serialVersionUID = 6366871005309174152L;
    /**
     * 收费名称
     */
    public String SFMC;
    /**
     * 总费用
     */
    public String ZJJE;
    /**
     * 自负金额
     */
    public String ZFJE;
}
