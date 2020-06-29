package com.bsoft.nis.controller.advicesplit;

import com.bsoft.nis.advicesplit.AdviceSplitExcutor;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.advicesplit.advice.UserCom;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Describtion:医嘱拆分拆分控制器
 * Created: dragon
 * Date： 2016/12/8.
 */
@Controller
@Scope(value = "prototype")
public class AdviceSplitController {

    @Autowired
    AdviceSplitExcutor excutor;

    @Autowired
    DateTimeService     dtService;

    /**
     * 医嘱拆分（单病人拆分）
     * @param zyh
     * @param jgid
     * @param ksrq
     * @param jsrq
     * @return
     */
    @RequestMapping(value = "mobile/advice/split/from/one/patient")
    public @ResponseBody
    Response<String> adviceSplitFromOnePatient(@RequestParam(value = "zyh",required = false) String zyh,
                                               @RequestParam(value = "jgid") String jgid,
                                               @RequestParam(value = "ksrq") String ksrq,
                                               @RequestParam(value = "jsrq") String jsrq){
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();
        if(allowSplit(jsrq)) {
            bizResponse = excutor.excuteSplitForOnePatient(zyh, jgid, ksrq, jsrq);
            if (bizResponse.isSuccess) {
                response.ReType = 0;
            } else {
                response.ReType = -1;
            }
        /*// TODO 删除 ：调用同步服务
        try {
            Response<String> response1 = Client.rpcInvoke("nis-synchron.synchronRpcServerProvider","synchron",new InArgument());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }*/
            response.Data = bizResponse.data;
            response.Msg = bizResponse.message;
        }else{
            response.ReType = -1;
            response.Msg = "只允许拆分1周内的数据,请检查日期!";
        }
        return response;
    }
    /**
     * 医嘱拆分（单科室拆分）
     * @param ksdm
     * @param jgid
     * @param ksrq
     * @param jsrq
     * @return
     */
    @RequestMapping(value = "mobile/advice/split/from/one/dept")
    public @ResponseBody
    Response<String> adviceSplitFromOneDept(@RequestParam(value = "ksdm") String ksdm,
                                               @RequestParam(value = "jgid") String jgid,
                                               @RequestParam(value = "ksrq") String ksrq,
                                               @RequestParam(value = "jsrq") String jsrq){
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse ;
        if(allowSplit(jsrq)) {
            bizResponse = excutor.excuteSplitForOneDept(ksdm, jgid, ksrq, jsrq);
            if (bizResponse.isSuccess) {
                response.ReType = 0;
            } else {
                response.ReType = -1;
            }
            response.Data = bizResponse.data;
            response.Msg = bizResponse.message;
        }else{
            response.ReType = -1;
            response.Msg = "只允许拆分1周内的数据,请检查日期!";
        }
        return response;
    }

    /**
     * jsrq 在当前日期7天后以内(含7天),则返加true,否则返回false
     * @param jsrq
     * @return
     */
    private boolean allowSplit(String jsrq){
        String curDateStr = dtService.getNowDateStr(DataSource.MOB);

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dtJSRQ = LocalDate.parse(jsrq, formater);
        LocalDate dtCur = LocalDate.parse(curDateStr, formater);

        dtCur = dtCur.plusDays(7);
        if(dtJSRQ.isAfter(dtCur)){
            return false;
        }

        return true;
    }

    /**
     * TODO 删除
     * @param userCom
     * @return
     */

    @RequestMapping(value = "mobile/post/pb/get")
    public @ResponseBody
    Response<UserCom> pb(@RequestBody UserCom userCom){
        Response<UserCom> response = new Response<>();
        response.Data = userCom;
        response.Msg = "hello";
        return response;
    }

}
