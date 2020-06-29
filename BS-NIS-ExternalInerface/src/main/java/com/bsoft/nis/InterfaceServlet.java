package com.bsoft.nis;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.InputStream;
import java.util.Properties;

/**
 * 接口Servlet,用于接口信息的初始化及启动
 * Created by mengdw on 2019/1/3.
 * 说明：需要在web.xml文件中加入<servlet></servlet>内容
 <!-- 配置InterfaceServlet信息-->
 <servlet>
 <servlet-name>InterfaceServlet</servlet-name>
 <servlet-class>com.bsoft.nis.InterfaceServlet</servlet-class>
 <load-on-startup>2</load-on-startup>
 </servlet>

 从WEB-INF\classes\webservice.properties文件oral.ws.port中读取webservice发布的端口
 如果没有配置文件或者oral.ws.port信息，默认端口为9355
 */
//com.bsoft.nis.InterfaceServlet
public class InterfaceServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();

        int port = 9355;

        try {
            //Oral.WebService端口配置在webservice.properties文件oral.ws.port中
            InputStream is = this.getServletContext().getClassLoader().getResourceAsStream("webservice.properties");
            if(is != null) {
                Properties properties = new Properties();
                properties.load(is);
                String portStr = properties.getProperty("oral.ws.port");
                if (portStr == null || "".equals(portStr)) portStr = "9355";

                port = Integer.parseInt(portStr);
            }
        }catch (Exception ex){}

        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());

        System.out.println("正在发布包药机服务 ... ");
        com.bsoft.nis.oral.ws.OralService.startOralService(context, port);
        System.out.println("包药机服务发布完成 ... ");
    }
}
