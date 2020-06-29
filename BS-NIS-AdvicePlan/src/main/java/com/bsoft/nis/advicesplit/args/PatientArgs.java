package com.bsoft.nis.advicesplit.args;

import com.bsoft.nis.domain.advicesplit.advice.AdviceCom;
import com.bsoft.nis.domain.advicesplit.advice.db.MonitoreAdvice;
import com.bsoft.nis.domain.patient.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:待拆分病人信息（包装类）
 * 1.当前病人有效医嘱
 * 2.当前病人监控医嘱
 * 3.当前病人计划类型 所在病区定制的科室计划类型
 * 4.当前病人拆分结果 待提交的拆分结果
 * Created: dragon
 * Date： 2016/12/1.
 */
public class PatientArgs extends Patient{
    public PatientArgs(){

    }
    public PatientArgs(Patient patient){
        this.BRBQ = patient.BRBQ;
        this.ZYH = patient.ZYH;
        this.userId = patient.userId;
        this.userName = patient.userName;
        this.BRCH = patient.BRCH;
        this.BRKS = patient.BRKS;
        this.BRXM = patient.BRXM;
        this.CSNY = patient.CSNY;
    }

    /**
     * 当前病人有效医嘱（待拆分医嘱）
     */
    public List<AdviceCom> avalibleAdvices = new ArrayList<>();
    /**
     * 监控医嘱
     */
    public List<MonitoreAdvice> monitoreAdvices = new ArrayList<>();
    /**
     * 计划类型
     */
    public List<PlanTypeArgs> planTypes = new ArrayList<>();

    /**
     * 拆分结果
     */
    public ExcuteResults excuteResult = new ExcuteResults();

}
