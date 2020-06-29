package com.bsoft.nis.controller.expense;

import com.bsoft.nis.domain.expense.ExpenseRespose;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.expense.ExpenseMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 费用控制器
 * Created by Administrator on 2016/10/10.
 */
@Controller
public class ExpenseController {

    @Autowired
    ExpenseMainService service;


    @RequestMapping(value = "auth/mobile/expense/GetCharge")
    public @ResponseBody
    Response<ExpenseRespose> GetCharge(@RequestParam(value = "zyh")String zyh,
                                       @RequestParam(value = "start")String start,
                                       @RequestParam(value = "end")String end,
                                       @RequestParam(value = "jgid")String jgid){
        Response<ExpenseRespose> response = new Response<>();
        BizResponse<ExpenseRespose> bizResponse = new BizResponse<ExpenseRespose>();
        bizResponse = service.GetCharge(zyh,start,end,jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }


    @RequestMapping(value = "auth/mobile/expense/GetDetailOneDay")
    public @ResponseBody
    Response<ExpenseRespose> GetDetailOneDay(@RequestParam(value = "zyh")String zyh,
                                             @RequestParam(value = "start")String start,
                                             @RequestParam(value = "end")String end,
                                             @RequestParam(value = "jgid")String jgid){
        Response<ExpenseRespose> response = new Response<>();
        BizResponse<ExpenseRespose> bizResponse = new BizResponse<ExpenseRespose>();
        bizResponse = service.GetDetailOneDay(zyh, start, end, jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }
}
