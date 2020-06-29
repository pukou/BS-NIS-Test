package com.bsoft.nis.advicesplit.commit;

import com.bsoft.nis.advicesplit.AdviceSplitExcutor;
import com.bsoft.nis.advicesplit.args.ExcuteResults;
import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.advicesplit.exception.CommitException;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.advicesplit.AdviceSplitService;
import com.bsoft.nis.service.advicesplit.support.AdviceSplitServiceSup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Describtion:拆分结果提交器
 * Created: dragon
 * Date： 2016/12/8.
 */
@Component
@Scope(value = "prototype")
public class ResultCommitControll extends RouteDataSourceService implements ResultCommitInterface{

    private Log logger = LogFactory.getLog(ResultCommitControll.class);
    AdviceSplitExcutor excutor;

    @Autowired
    AdviceSplitServiceSup splitService;

    @Override
    public void init(AdviceSplitExcutor excutor) {
        this.excutor = excutor;
    }

    /**
     * 医嘱拆分计划提交
     * @param patientArgs
     * @throws CommitException
     */
    @Override
    public void commit(PatientArgs patientArgs) throws CommitException{
        ExcuteResults results = patientArgs.excuteResult;

        try{
            keepOrRoutingDateSource(DataSource.MOB);
            splitService.commitAdvicePlans(results);
        }catch (Exception e){
            logger.error("住院号:"+patientArgs.ZYH +"-姓名:"+patientArgs.BRXM +"医嘱计划提交失败:" + patientArgs.excuteResult.insertPlans.size() +"条计划欲插入；"  + e.getMessage(),e);
        }
    }
}
