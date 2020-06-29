package com.bsoft.nis.common.controller.main;

import com.bsoft.nis.pojo.exchange.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 安全异常处理器
 * Created by Administrator on 2016/10/13.
 */
@Controller
public class OauthExceptionHandler {

    /**
     * session失效
     * @return
     */
    @RequestMapping(value = "handle/login/invalid")
    public @ResponseBody
    Response<String> handleOauthException(){
        Response<String> response = new Response<>();
        response.ReType = 100; // session失效
        response.Msg = "登录过期";
        return response;
    }
}
