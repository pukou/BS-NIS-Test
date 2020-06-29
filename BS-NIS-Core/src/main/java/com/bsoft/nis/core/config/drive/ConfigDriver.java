package com.bsoft.nis.core.config.drive;

import com.bsoft.nis.core.config.bean.flow.Flow;
import com.bsoft.nis.core.config.bean.flow.FlowGroup;
import com.bsoft.nis.core.config.context.ConfigContainer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.Iterator;

/**
 * Describtion:配置文件加载驱动器
 * Created: dragon
 * Date： 2016/11/10.
 */
public class ConfigDriver extends HttpServlet{

    private Log logger = LogFactory.getLog(ConfigDriver.class);
    private String CONFIG_RESOURCEPATH ;
    private final String CONFIG_FILENAME = "webconfig.xml";

    @Override
    public void init() throws ServletException {
        super.init();
        CONFIG_RESOURCEPATH = this.getClass().getClassLoader().getResource("")
                .getPath();

        logger.info("正在初始化系统配置数据...");
        loadConfig();
        logger.info("已完成初始化系统配置数据...");
    }

    /**
     * 加载配置并解析
     */
    public void loadConfig(){
        File file = new File(CONFIG_RESOURCEPATH + CONFIG_FILENAME);
        SAXReader sr = new SAXReader();

        Boolean is = file.exists();
        try{
            Document doc = sr.read(file);
            Element root = doc.getRootElement();
            // 解析系统级配置
            parseSystemConfig(root);
            // 解析流程组
            parseFlowGroups(root);
        }catch (DocumentException e){
            logger.error("系统配置数据加载错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解析系统配置
     * @param root
     */
    public void parseSystemConfig(Element root){
        Element sys = root.element("system");
        // 遍历系统配置
        for (Iterator it = sys.elementIterator();it.hasNext();){
            Element element = (Element)it.next();
            String key = element.getName();
            String value = element.attributeValue("value");
            ConfigContainer.system.put(key,value);
        }
    }

    /**
     * 解析流程组
     * @param root
     */
    public void parseFlowGroups(Element root){
        Element flowGroupsElement = root.element("flowGroups");
        // 遍历流程组
        for (Iterator itGroup = flowGroupsElement.elementIterator();itGroup.hasNext();){
            Element groupElement = (Element)itGroup.next();
            String id = groupElement.attributeValue("id");
            String remark = groupElement.attributeValue("remark");

            if (StringUtils.isEmpty(id)) continue;
            FlowGroup flowGroup = new FlowGroup(id,remark);

            // 流程组中流程组装
            for (Iterator itFlow = groupElement.elementIterator();itFlow.hasNext();){
                Element flowElement = (Element)itFlow.next();
                String flowName = flowElement.attributeValue("flowName");
                String flowHandlerClass = flowElement.attributeValue("flowHandlerClass");
                String flowRemark = flowElement.attributeValue("flowRemark");
                if (StringUtils.isEmpty(flowHandlerClass)) continue;

                Flow flow = new Flow(flowName,flowHandlerClass,flowRemark);
                flowGroup.flows.add(flow);
            }

            if (flowGroup.flows.size() > 0) ConfigContainer.flowGroups.add(flowGroup);
        }
    }
}
