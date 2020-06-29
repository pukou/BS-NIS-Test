package com.bsoft.nis.core.config.context;

import com.bsoft.nis.core.config.bean.flow.FlowGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置容器
 * Describtion:
 * Created: dragon
 * Date： 2016/11/10.
 */
public class ConfigContainer {

    /**
     * 系统级配置，以KEY-VALUE形式存储
     */
    public static Map system = new HashMap();

    public static List<FlowGroup> flowGroups = new ArrayList<>();

}
