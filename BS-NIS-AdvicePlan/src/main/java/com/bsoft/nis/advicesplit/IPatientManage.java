package com.bsoft.nis.advicesplit;

import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.advicesplit.exception.CommitException;
import com.bsoft.nis.advicesplit.exception.NoPatientException;
import com.bsoft.nis.advicesplit.exception.SplitException;
import com.bsoft.nis.advicesplit.exception.WrapperException;
import com.bsoft.nis.pojo.exchange.BizResponse;

import java.util.List;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/12/2.
 */
public interface IPatientManage {

    /**
     * 准备医嘱拆分数据 根据病人住院号
     * @param zyh
     * @return
     */
    void praparingAdviceSplitDataFromPatient(String zyh,String jgid,String ksrq,String jsrq) throws WrapperException,NoPatientException;

    /**
     * 准备医嘱拆分数据 根据科室代码
     * @param bqdm
     * @return
     */
    void praparingAdviceSplitDataFromDept(String bqdm,String jgid,String ksrq,String jsrq) throws WrapperException,NoPatientException;
    /**
     * 获取病人池中病人列表
     * @return
     */
    List<PatientArgs> getExcutingPatients();

    /**
     * 执行病人医嘱拆分
     * @return
     */
    void excuteSplit() throws SplitException;

    /**
     * 执行病人医嘱拆分
     * @return
     */
    void excuteSplit(PatientArgs patientArg) throws SplitException;

    /**
     * 结果提交
     * @return
     */
    void commit() throws CommitException;

    /**
     * 结果提交
     * @return
     */
    void commit(PatientArgs patientArg) throws CommitException;

}
