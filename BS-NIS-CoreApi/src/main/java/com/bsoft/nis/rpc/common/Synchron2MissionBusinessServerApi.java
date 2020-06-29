package com.bsoft.nis.rpc.common;

import com.bsoft.nis.domain.synchron.OutArgument;
import com.bsoft.nis.domain.synchron.SyncResult;
import com.bsoft.nis.pojo.exchange.Response;
import ctd.util.annotation.RpcService;

/**
 * Describtion:同步服务调用的写入目标业务接口
 * Created: dragon
 * Date： 2017/1/11.
 */
public interface Synchron2MissionBusinessServerApi {
    @RpcService
    Response<SyncResult> synchron2MissionBusiness(OutArgument outArgument);
	@RpcService
	Response<SyncResult> synchron2MissionBusinessDel(OutArgument outArgument);
}
