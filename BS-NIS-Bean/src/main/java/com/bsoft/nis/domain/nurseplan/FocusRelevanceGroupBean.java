package com.bsoft.nis.domain.nurseplan;

import com.bsoft.nis.domain.dangerevaluate.FXPGJLBean;
import com.bsoft.nis.domain.dangerevaluate.HLJHJLBean;

import java.util.List;

/**
 * Created by Classichu on 2017/5/4.
 * 焦点关联数据  parent bean
 */
public class FocusRelevanceGroupBean {
    public String PZBH;
    public String DMMC;
    public List<JD_GL_SMTZ_Bean> JD_GL_SMTZ_BeanList;
    public List<FXPGJLBean> FXPGJLBeanList;
    public List<HLJHJLBean> HLJHJLBeanList;
    public List<JYXM_PATIENTINFO_Bean> JYXM_PATIENTINFO_BeanList;
}
