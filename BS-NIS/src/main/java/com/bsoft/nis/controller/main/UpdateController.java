package com.bsoft.nis.controller.main;

import com.bsoft.nis.domain.update.UpdateInfo;
import com.bsoft.nis.pojo.exchange.Response;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Description: APP更新
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-02-27
 * Time: 16:56
 * Version:
 */
@Controller
public class UpdateController {
    private Log logger = LogFactory.getLog(UpdateController.class);

    @RequestMapping(value = "/")
    public String getMainPatientPage() {
        return "update/update";
    }


    /**
     * 检测版本信息
     *
     * @param appname 应用名称
     * @param request
     * @return 如有新的应用版本，则返回 Response.UpdateInfoVo
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/mobile/update/getVersionInfo", method = RequestMethod.GET)
    public
    @ResponseBody
    Response<UpdateInfo> getVersionInfo(@RequestParam(value = "appname") String appname,
                                        HttpServletRequest request) throws IOException {
          /*
        升级编号【56010047】============================================= start
        部分情况下，在线更新报错问题   PDA端DowloadReceiver下面的queryAndOpen报Intent异常
        ================= Classichu 2017/10/16 10:32
        */

        Response<UpdateInfo> bizResponse = new Response<>();
        try {
            appname = appname.toLowerCase();//统一
            String desFilePath = request.getServletContext().getRealPath("") + File.separator + "NIS-UPDATE" + File.separator + appname + File.separator + "description.json";
            //String desFilePath = request.getSession().getServletContext().getRealPath("")+ File.separator + "NIS-UPDATE" + File.separator + appname.toLowerCase() + File.separator + "description.json";
            byte[] encoded = Files.readAllBytes(Paths.get(desFilePath));
            String jsonStr = new String(encoded, "utf-8");
            Gson gson = new Gson();
            logger.error("开始解析JSON:" + jsonStr);
            UpdateInfo updateInfo = gson.fromJson(jsonStr, UpdateInfo.class);
            String path = request.getContextPath();
           /* String basePath = request.getScheme() + "://"
                    + request.getRequestURI() + ":" + request.getServerPort()
                    + path + "/";*/

            String requestUrl = request.getRequestURL().toString();//  http://192.168.1.125:8081/NIS/mobile/update/getVersionInfo
            String requestUri = request.getRequestURI();//   /NIS/mobile/update/getVersionInfo
            String basePath = "";
            logger.error(requestUrl);
            basePath = requestUrl.substring(0, requestUrl.indexOf(requestUri)) + path;
            String url = basePath + "/NIS-UPDATE/" + appname + "/" + updateInfo.FileName;
            logger.error(url);
            updateInfo.Url = url;
            bizResponse.ReType = 0;
            bizResponse.Data = updateInfo;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            bizResponse.ReType = -1;
            bizResponse.Msg = "请求失败：解析错误";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            bizResponse.ReType = -1;
            bizResponse.Msg = "请求失败：解析错误";
        }
        /* =============================================================== end */
        return bizResponse;
    }
}
