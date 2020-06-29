package com.bsoft.nis.advicesplit;

import com.bsoft.nis.core.spring.SpringContextUtil;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.rpc.adviceplan.AdvicePlanServerApi;
import ctd.util.annotation.RpcService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Describtion:医嘱拆分RPC服务提供者
 * Created: dragon
 * Date： 2017/1/20.
 */
public class AdvicePlanRpcServerProvider implements AdvicePlanServerApi{

    private Log logger = LogFactory.getLog(AdvicePlanRpcServerProvider.class);

    @RpcService(timeout = 60)
    @Override
    public BizResponse<String> excuteSplitForOnePatient(String zyh, String jgid, String ksrq, String jsrq) {
        BizResponse<String> response = new BizResponse<>();
        AdviceSplitExcutor excutor = SpringContextUtil.getBean(AdviceSplitExcutor.class);
        if (excutor == null){
            response.isSuccess = false;
            response.message = "ApplicationContext get [AdviceSplitExcutor.class] fail from Spring!";
            return response;
        }
        try{
            response = excutor.excuteSplitForOnePatient(zyh,jgid,ksrq,jsrq);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

        return response;
    }
}
