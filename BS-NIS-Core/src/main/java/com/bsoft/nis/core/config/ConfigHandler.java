package com.bsoft.nis.core.config;

import com.bsoft.nis.core.config.bean.flow.Flow;
import com.bsoft.nis.core.config.bean.flow.FlowGroup;
import com.bsoft.nis.core.config.context.ConfigContainer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:配置处理器
 * Created: dragon
 * Date： 2016/11/10.
 */
public class ConfigHandler {

    /**
     * 根据配置名获取配置值
     * @param name
     * @return
     */
    public static String getSystemConfig(String name){
        String retStr;
        if (ConfigContainer.system.containsKey(name)){
            retStr = String.valueOf(ConfigContainer.system.get(name));
        }else{
            retStr = null;
        }
        return retStr;
    }

    /**
     * 根据配置的流程组ID获取整个流程组
     * @param groupId
     * @return
     */
    public static List<Flow> getFlowsByFlowGroupId(String groupId){
        FlowGroup flowGroup = null;
        List<Flow> flows = new ArrayList<>();
        List<FlowGroup> flowGroups = ConfigContainer.flowGroups;
        for (FlowGroup flowGroup1:flowGroups){
            if (flowGroup1.id.equals(groupId)){
                flowGroup = flowGroup1;
            }
        }

        if (flowGroup != null) flows = flowGroup.flows;
        return flows;
    }
}
