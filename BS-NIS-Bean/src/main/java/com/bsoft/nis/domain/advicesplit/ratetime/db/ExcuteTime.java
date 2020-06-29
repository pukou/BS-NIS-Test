package com.bsoft.nis.domain.advicesplit.ratetime.db;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Describtion:执行时间(ENR_ZXSJ)
 * Created: dragon
 * Date： 2016/12/9.
 */
public class ExcuteTime implements Serializable,Comparable<ExcuteTime>{
    private static final long serialVersionUID = 3598610861612657908L;
    public Long SJBS;

    public Long PCBS;

    public Integer ZXZQ;

    public Integer SJBH;

    public String SJMC;

    public String KSSJ;

    public String JSSJ;

    public String ZXRC;

    /**
     * 按天发药(HIS中GY_SYPC属性)
     */
    public Integer ATFY;


    @Override
    public int compareTo(ExcuteTime o) {
        if (this == o){
            return 0;
        }
        ExcuteTime excuteTime = (ExcuteTime)o;
        LocalTime kssj ;
        LocalTime nextkssj;
        if (StringUtils.isEmpty(this.KSSJ) || StringUtils.isEmpty(excuteTime.KSSJ))
            return 0;

        //不先按执行周期(ZXZQ)排序, tiw的可能会拆分错误
        if(this.ZXZQ < excuteTime.ZXZQ){
            return -1;
        }else {
            kssj = LocalTime.parse(this.KSSJ);
            nextkssj = LocalTime.parse(excuteTime.KSSJ);
            if (kssj.isAfter(nextkssj)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
