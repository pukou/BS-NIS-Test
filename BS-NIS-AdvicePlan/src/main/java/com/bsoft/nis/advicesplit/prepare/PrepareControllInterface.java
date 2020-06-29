package com.bsoft.nis.advicesplit.prepare;

import com.bsoft.nis.advicesplit.AdviceSplitExcutor;
import com.bsoft.nis.advicesplit.args.DeptArgs;
import com.bsoft.nis.advicesplit.exception.NoPatientException;
import com.bsoft.nis.advicesplit.exception.WrapperException;
import com.bsoft.nis.pojo.exchange.BizResponse;

/**
 * Describtion:准备器接口
 * Created: dragon
 * Date： 2016/12/8.
 */
public interface PrepareControllInterface {

    void init(AdviceSplitExcutor excutor);

    void prepareWrapper(String id,Boolean isPatient,String jgid,String ksrq,String jsrq) throws WrapperException,NoPatientException;
}
