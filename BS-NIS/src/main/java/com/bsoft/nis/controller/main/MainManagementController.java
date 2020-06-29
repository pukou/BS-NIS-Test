package com.bsoft.nis.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * description:移动护理后台管理系统：
 * create by: dragon xinghl@bsoft.com.cn
 * create time:2017/11/20 14:48
 * since:5.6 update1
 */
@Controller
public class MainManagementController {
    @RequestMapping(value = "main/page")
    public String getMainManagementPage() {
        return "main";
    }

    @RequestMapping(value = "evalution/main/page")
    public String getEvalutionPage() {
        return "evalution/evaluate";
    }
}
