package com.bsoft.nis.domain.advicecheck;

import java.io.Serializable;
import java.util.List;

/**
 * Created by king on 2016/11/30.
 */
public class CheckDetail  implements Serializable {

    public List<AdviceFormDetail> adviceFormDetails;

    public String Msg;

    public String IsFalse = "IsTrue";
}
