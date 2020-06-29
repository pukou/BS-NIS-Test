package com.bsoft.nis.rpc.adviceplan;

import com.bsoft.nis.pojo.exchange.BizResponse;
import ctd.util.annotation.RpcService;

/**
 * Describtion：医嘱计划接口
 * Created: dragon
 * Date： 2017/1/20.
 */
public interface AdvicePlanServerApi {
    @RpcService
    BizResponse<String> excuteSplitForOnePatient(String zyh,String jgid,String ksrq,String jsrq);
}
