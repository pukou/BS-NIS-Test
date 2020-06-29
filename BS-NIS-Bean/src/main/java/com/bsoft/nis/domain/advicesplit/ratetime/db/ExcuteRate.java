package com.bsoft.nis.domain.advicesplit.ratetime.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:执行频次(ENR_ZXPC)
 * Created: dragon
 * Date： 2016/12/9.
 */
public class ExcuteRate implements Serializable {
    private static final long serialVersionUID = 6625863664133710980L;

    public Long PCBS;

    public Integer GLLB;

    public Long GLBS;

    public String PCBM;

    public String KSDM;

    public String JGID;

    public Integer PCZQ;

    public List<ExcuteTime> excuteTimes = new ArrayList<>();
}
