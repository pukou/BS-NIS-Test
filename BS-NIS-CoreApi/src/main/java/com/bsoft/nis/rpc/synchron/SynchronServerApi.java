package com.bsoft.nis.rpc.synchron;

import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.pojo.exchange.Response;
import ctd.util.annotation.RpcService;

/**
 * Describtion:同步服务接口
 * Created: dragon
 * Date： 2017/1/10.
 */
public interface SynchronServerApi {
    @RpcService
    Response<SelectResult> synchron(InArgument inArgument);
    @RpcService
    Response<String> synchronRepeat(SelectResult selectResult);
	@RpcService
	Response<String> DeleSyncData(InArgument inArgument);

}
