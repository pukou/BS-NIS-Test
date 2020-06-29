package com.bsoft.nis.core.datasource.drive;

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
import java.util.List;

/**
 * Describtion:配置文件加载驱动器
 * Created: dragon
 * Date： 2016/11/10.
 */
public class DatabaseDriver extends HttpServlet{

    private Log logger = LogFactory.getLog(DatabaseDriver.class);
    private String CONFIG_RESOURCEPATH ;
    private final String MYBATIS_FILENAME = "spring-mybatis.xml";
    private String DBCONFIG_FILENAME = "jdbc_localhost.properties";

    @Override
    public void init() throws ServletException {
        super.init();
        CONFIG_RESOURCEPATH = this.getClass().getClassLoader().getResource("")
                .getPath();

        logger.info("正在初始化基础数据...");
        loadConfig();
        logger.info("已完成初始化基础数据...");
    }

    /**
     * 加载配置并解析
     */
    public void loadConfig(){
        File file = new File(CONFIG_RESOURCEPATH + MYBATIS_FILENAME);
        SAXReader sr = new SAXReader();

        Boolean is = file.exists();
        try{
            Document doc = sr.read(file);
            Element root = doc.getRootElement();
            // get config file name
            getConfigFileName(root);

            // load database config properties
            loadDatabaseProperties();

        }catch (DocumentException e){
            logger.error("系统配置数据加载错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解析系统级配置
     */
    public void getConfigFileName(Element root){
        List<Element> syss = root.elements("property-placeholder");
        if (syss.size()>0){
            Element element = syss.get(0);
            String dbfile = element.attributeValue("location");
            if (StringUtils.isEmpty(dbfile)) return ;
            String[] strings = dbfile.split(":");
            if (strings.length<=0) return ;
            DBCONFIG_FILENAME = strings[1];
        }
        // 遍历系统配置
       /* for (Iterator it = root.elementIterator();it.hasNext();){
            Element element = (Element)it.next();
            if (element.getName().equals(""));
            String key = element.getName();
            String value = element.getStringValue();
            ConfigContainer.system.put(key,value);
        }*/
    }

    public void loadDatabaseProperties(){

    }
}
