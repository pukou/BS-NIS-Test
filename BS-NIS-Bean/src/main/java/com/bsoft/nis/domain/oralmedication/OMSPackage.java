package com.bsoft.nis.domain.oralmedication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjiaroro on 2017/2/16.
 * 一包药
 */
public class OMSPackage {

    public String packageid;//药袋ID(每袋药的识别ID)

    public String packagecode;//药袋条码(每袋药的识别ID)

    public String packageindex;//药袋数

    public List<OMSTablet> tablets = new ArrayList<>();

    public Long KFMX;

    public String KFDH;


}
