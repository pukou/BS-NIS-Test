package com.bsoft.nis.advicesplit.commit;

import com.bsoft.nis.advicesplit.AdviceSplitExcutor;
import com.bsoft.nis.advicesplit.args.ExcuteResults;
import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.advicesplit.exception.CommitException;
import com.bsoft.nis.pojo.exchange.BizResponse;

/**
 * Describtion:拆分结果提交
 * Created: dragon
 * Date： 2016/12/8.
 */
public interface ResultCommitInterface {

    void init(AdviceSplitExcutor excutor);

    void commit(PatientArgs patientArgs) throws CommitException;
}
