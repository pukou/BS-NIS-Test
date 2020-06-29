package com.bsoft.nis.domain.office;

import com.bsoft.nis.domain.BaseVo;

/**
 * Created by Administrator on 2016/10/12.
 */
public class AreaVo extends BaseVo {
    private static final long serialVersionUID = -1L;

    /**
     * 员工代码
     */
    public String YGDM;
    /**
     * 科室代码
     */
    public String KSDM;
    /**
     * 是否选择
     */
    public int MRBZ;
    /**
     * 科室名称
     */
    public String KSMC;

    public AreaVo(){
    }

    public AreaVo(String KSDM){
        this.KSDM=KSDM;
    }

    @Override
    public boolean equals(Object o) {
        return KSDM.equals(((AreaVo)o).KSDM);
    }
}
