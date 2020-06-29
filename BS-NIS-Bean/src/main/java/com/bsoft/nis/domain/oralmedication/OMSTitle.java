package com.bsoft.nis.domain.oralmedication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjiaroro on 2017/2/16.
 * 首部信息
 */
public class OMSTitle {

    public String patientid;//住院号

    public String areaid; //病区

    public String bedid;//床号

    public String plantime;//口服时间

    public String planpoint="0";//口服时点(默认为0)

    public String autoid;//口服ID（同一病人一顿药的识别ID）

    public String packnum;//包药袋数

    public String packtime;//包药时间

    public List<OMSPackage> packages = new ArrayList<>();//药袋信息

    public String bzxx;

    public Long KFDH;

    public String dbtype;
}
