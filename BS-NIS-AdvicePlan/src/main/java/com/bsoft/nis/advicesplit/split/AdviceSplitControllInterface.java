package com.bsoft.nis.advicesplit.split;

import com.bsoft.nis.advicesplit.AdviceSplitExcutor;
import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.advicesplit.exception.SplitException;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.pojo.exchange.BizResponse;

/**
 * Describtion:医嘱拆分器
 * Created: dragon
 * Date： 2016/12/8.
 */
public interface AdviceSplitControllInterface {

    void init(AdviceSplitExcutor excutor);

    void split(PatientArgs patientWrapper) throws SplitException;
}
