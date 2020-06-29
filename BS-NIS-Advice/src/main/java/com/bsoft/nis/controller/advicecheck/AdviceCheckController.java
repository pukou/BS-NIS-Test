package com.bsoft.nis.controller.advicecheck;

import com.bsoft.nis.domain.advicecheck.AdviceCheckList;
import com.bsoft.nis.domain.advicecheck.CheckDetail;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.advicecheck.AdviceCheckMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by king on 2016/11/28.
 */
@Controller
public class AdviceCheckController {

    @Autowired
    AdviceCheckMainService service;


    /**
     * 获取加药摆药核对列表
     *
     * @param brbq   病区代码
     * @param syrq   输液日期
     * @param gslx   归属类型
     * @param type   类型
     * @param status 状态
     * @param jgid   机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/advicecheck/get/GetDosingCheckList")
    public
    @ResponseBody
    Response<AdviceCheckList> getDosingCheckList(@RequestParam(value = "bqdm") String brbq,
                                                 @RequestParam(value = "syrq") String syrq,
                                                 @RequestParam(value = "gslx") String gslx,
                                                 @RequestParam(value = "type") String type,
                                                 @RequestParam(value = "status") String status,
                                                 @RequestParam(value = "jgid") String jgid) {
        Response<AdviceCheckList> response = new Response<>();
        BizResponse<AdviceCheckList> bizResponse;

        bizResponse = service.getDosingCheckList(brbq, syrq, gslx, type, status, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;

    }

    /**
     * 扫描执行加药/摆药核对
     *
     * @param tmbh    条码编号
     * @param prefix  条码前缀
     * @param userId  用户id
     * @param isCheck 执行核对或显示明细
     * @param type    核对类型
     * @param jgid    机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/advicecheck/get/ScanExecuteDoskingCheck")
    public
    @ResponseBody
    Response<CheckDetail> scanExecuteDoskingCheck(@RequestParam(value = "tmbh") String tmbh,
                                                  @RequestParam(value = "prefix") String prefix,
                                                  @RequestParam(value = "userId") String userId,
                                                  @RequestParam(value = "isCheck") boolean isCheck,
                                                  @RequestParam(value = "type") String type,
                                                  @RequestParam(value = "jgid") String jgid) {
        Response<CheckDetail> response = new Response<>();
        BizResponse<CheckDetail> bizResponse;

        bizResponse = service.scanExecuteDoskingCheck(tmbh, prefix, userId, isCheck, type, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;
    }


    /**
     * 手动执行加药/摆药核对
     *
     * @param sydh
     * @param gslx
     * @param userId
     * @param isCheck
     * @param type
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/advicecheck/get/HandExecuteDoskingCheck")
    public
    @ResponseBody
    Response<CheckDetail> handExecuteDoskingCheck(@RequestParam(value = "sydh") String sydh,
                                                  @RequestParam(value = "gslx") String gslx,
                                                  @RequestParam(value = "userId") String userId,
                                                  @RequestParam(value = "isCheck") boolean isCheck,
                                                  @RequestParam(value = "type") String type,
                                                  @RequestParam(value = "jgid") String jgid) {
        Response<CheckDetail> response = new Response<>();
        BizResponse<CheckDetail> bizResponse;

        bizResponse = service.handExecuteDoskingCheck(sydh, gslx, userId, isCheck, type, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;
    }


}
