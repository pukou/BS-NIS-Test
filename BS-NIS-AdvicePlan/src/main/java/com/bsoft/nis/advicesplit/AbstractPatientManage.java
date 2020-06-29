package com.bsoft.nis.advicesplit;

import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.advicesplit.exception.SplitException;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.pojo.exchange.BizResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Describtion:病人拆分管理器
 * <p>
 *     1.excuteSplit 单病人拆分
 *     2.batchExcuteSplit 批量拆分
 * </p>
 *
 * Created: dragon
 * Date： 2016/12/2.
 */
public abstract class AbstractPatientManage implements IPatientManage {

    /**
     * Patients pool excuting (正在执行的病人pool)
     * 待拆分医嘱的病人列表,所有线程公用
     */
    public static List<PatientArgs> excutingPatients = new ArrayList<>();

    /**
     * 正在执行医嘱拆分的病人
     */
    public static PatientArgs excutingPatient = null;

    @Override
    public List<PatientArgs> getExcutingPatients() {
        return excutingPatients;
    }


    /**
     * 释放病人池中病人,执行完一个病人医嘱拆分后，释放病人池中该病人
     * 加锁，排队释放
     */
    public void releaseOnePatientFromPool(PatientArgs arg){
        synchronized (excutingPatients){
            Iterator<PatientArgs> it = excutingPatients.iterator();
            while (it.hasNext()){
                PatientArgs patientArgs = it.next();
                if (patientArgs != null){
                    if (patientArgs.ZYH.equals(arg.ZYH)){
                        it.remove();
                    }
                }
            }
        }
    }

    /**
     * 往Pool中添加正在进行拆分的病人
     * 加锁，排队增加
     * @param arg
     */
    public void addOnePatientIntoPool(PatientArgs arg){
        synchronized (excutingPatients){
            excutingPatients.add(arg);
        }
    }

    /**
     * 判断病人是否正在执行拆分
     * 加锁，排队操作
     * @param arg
     */
    public Boolean exsitInPool(PatientArgs arg){
        synchronized (excutingPatients){
            Boolean isExsit = false;
            for (PatientArgs patientArgs:excutingPatients){
                if (patientArgs != null){
                    if (patientArgs.ZYH.equals(arg.ZYH)){
                        isExsit = true;
                    }
                }
            }
            return isExsit;
        }
    }

}
