package com.bsoft.nis.domain.visit;

import java.util.List;

/**
 * Created by king on 2016/11/18.
 */
public class VisitCount {

    //已巡视病人
    public List<VisitPerson> yjxs;

    //需巡视病人
    public List<VisitPerson> xyxs;

    //巡视情况
    public List<CheckState> xsqk;

}
