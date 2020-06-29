package com.bsoft.nis.controller.adviceexecute;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.adviceexecute.PlanAndTransfusion;
import com.bsoft.nis.domain.adviceexecute.RequestBodyInfo;
import com.bsoft.nis.domain.adviceexecute.ResponseBody.ResponseBodyInfo;
import com.bsoft.nis.domain.adviceexecute.ResponseModel.AdvicePlanData;
import com.bsoft.nis.domain.adviceqyery.TransfusionPatrolRecord;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.adviceexecute.AdviceExecuteMainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 16:56
 * Version:
 */
@Controller
public class AdviceExecuteController {

    @Autowired
    AdviceExecuteMainService service;

    @Autowired
    DateTimeService dtService;

    @RequestMapping(value = "/advice")
    public String getMainPatientPage() {
        return "advice/adviceexecute";
    }

    /**
     * 获取计划列表
     *
     * @param zyh
     * @return
     */
    @RequestMapping(value = {"auth/mobile/advice/get/GetPlanList","mobile/advice/get/GetPlanList"})
    public
    @ResponseBody
    Response<AdvicePlanData> GetPlanList(@RequestParam(value = "zyh") String zyh, @RequestParam(value = "today") String today,
                                         @RequestParam(value = "gslx") String gslx, @RequestParam(value = "jgid") String jgid) {
        Response<AdvicePlanData> response = new Response<>();
        if(allowSplit(today)) {
            BizResponse<AdvicePlanData> bizResponse = service.GetPlanList(zyh, today, gslx, jgid);
            if (bizResponse.isSuccess) {
                response.ReType = 0;
            } else {
                response.ReType = -1;
            }
            response.Msg = bizResponse.message;
            response.Data = bizResponse.data;
        } else {
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

    /*
       升级编号【56010053】============================================= start
       多瓶超过2瓶转接瓶后提示选择接哪瓶的问题
       ================= Classichu 2017/11/14 16:25

       */
    @RequestMapping(value = "auth/mobile/advice/get/getTransfusionInfoListByZyh4TransfuseExecut")
    public
    @ResponseBody
    Response<List<PlanAndTransfusion>> getTransfusionInfoListByZyh4TransfuseExecut(@RequestParam(value = "zyh") String zyh,
                                                                                   @RequestParam(value = "jgid") String jgid, @RequestParam(value = "syrq") String syrq) {
        Response<List<PlanAndTransfusion>> response = new Response<>();
        BizResponse<PlanAndTransfusion> bizResponse = service.getTransfusionInfoListByZyh4TransfuseExecut(zyh, jgid, syrq);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    @RequestMapping(value = "auth/mobile/advice/get/getTransfusionInfoListByZyh4TransfuseExecutAll")
    public
    @ResponseBody
    Response<List<PlanAndTransfusion>> getTransfusionInfoListByZyh4TransfuseExecutAll(@RequestParam(value = "zyh") String zyh,
                                                                                      @RequestParam(value = "jgid") String jgid, @RequestParam(value = "syrq") String syrq) {
        Response<List<PlanAndTransfusion>> response = new Response<>();
        BizResponse<PlanAndTransfusion> bizResponse = service.getTransfusionInfoListByZyh4TransfuseExecutAll(zyh, jgid, syrq);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }


    @RequestMapping(value = "auth/mobile/advice/get/getTransfusionInfoByBarcode4TransfuseExecut")
    public
    @ResponseBody
    Response<PlanAndTransfusion> getTransfusionInfoByBarcode4TransfuseExecut(@RequestParam(value = "barcode") String barcode,
                                                                             @RequestParam(value = "prefix") String prefix, @RequestParam(value = "jgid") String jgid) {
        Response<PlanAndTransfusion> response = new Response<>();
        BizResponse<PlanAndTransfusion> bizResponse = service.getTransfusionInfoByBarcode4TransfuseExecut(barcode, prefix, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }
    /* =============================================================== end */

    /**
     * 执行计划（手动）
     *
     * @param requestBodyInfo
     * @return
     */
    @Deprecated
    @RequestMapping(value = "auth/mobile/advice/post/HandExecut")
    public
    @ResponseBody
    Response<ResponseBodyInfo> HandExecut(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse = new BizResponse<>();

        bizResponse = service.HandExecut(requestBodyInfo);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }

    @RequestMapping(value = "auth/mobile/advice/post/HandExecutNew")
    public
    @ResponseBody
    Response<ResponseBodyInfo> HandExecutNew(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse = new BizResponse<>();

        bizResponse = service.HandExecutNew(requestBodyInfo);
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
     * 执行计划（扫描）
     *
     * @param requestBodyInfo
     * @return
     */
    @Deprecated
    @RequestMapping(value = "auth/mobile/advice/post/ScanExecut")
    public
    @ResponseBody
    Response<ResponseBodyInfo> ScanExecut(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse = new BizResponse<>();

        bizResponse = service.ScanExecut(requestBodyInfo);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }

    @RequestMapping(value = "auth/mobile/advice/post/ScanExecutNew")
    public
    @ResponseBody
    Response<ResponseBodyInfo> ScanExecutNew(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse = new BizResponse<>();

        bizResponse = service.ScanExecutNew(requestBodyInfo);
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
     * 口服药执行
     *
     * @param requestBodyInfo
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/post/OralMedicationExecut")
    public
    @ResponseBody
    Response<ResponseBodyInfo> OralMedicationExecut(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse = new BizResponse<>();

        bizResponse = service.OralMedicationExecut(requestBodyInfo);
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
     * 拒绝执行
     *
     * @param requestBodyInfo
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/post/RefuseExecut")
    public
    @ResponseBody
    Response<ResponseBodyInfo> RefuseExecut(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse = new BizResponse<>();

        bizResponse = service.RefuseExecut(requestBodyInfo);
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
     * 输液暂停
     *
     * @param requestBodyInfo
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/post/TransfuseStop")
    @ResponseBody
    public Response<ResponseBodyInfo> TransfuseStop(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        bizResponse = service.TransfuseStop(requestBodyInfo);
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
     * 输液结束
     *
     * @param requestBodyInfo
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/post/TransfuseEnd")
    @ResponseBody
    public Response<ResponseBodyInfo> TransfuseEnd(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        bizResponse = service.TransfuseEnd(requestBodyInfo);
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
     * 继续输液
     *
     * @param requestBodyInfo
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/post/TransfuseContinue")
    @ResponseBody
    public Response<ResponseBodyInfo> TransfuseContinue(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        bizResponse = service.TransfuseContinue(requestBodyInfo);
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
     * 输液非常规执行(并行接瓶)
     *
     * @param requestBodyInfo
     * @return
     */
    @Deprecated
    @RequestMapping(value = "auth/mobile/advice/post/TransfuseExecut")
    @ResponseBody
    public Response<ResponseBodyInfo> TransfuseExecut(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        bizResponse = service.TransfuseExecut(requestBodyInfo);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }

    @RequestMapping(value = "auth/mobile/advice/post/TransfuseExecutNew")
    @ResponseBody
    public Response<ResponseBodyInfo> TransfuseExecutNew(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        bizResponse = service.TransfuseExecutNew(requestBodyInfo);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }

    @RequestMapping(value = "auth/mobile/advice/post/KFExecutCancelEnd2Start")
    @ResponseBody
    public Response<ResponseBodyInfo> KFExecutCancelEnd2Start(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        if (requestBodyInfo.QRDH == null) {
            String jhh = requestBodyInfo.core;
            BizResponse<String> response1 = service.getOralInfoListByZyh4Cancel(jhh, requestBodyInfo.JGID);
            if (response1.isSuccess) {
                requestBodyInfo.QRDH = response1.data;
            } else {
                response.ReType = -1;
                response.Msg = "qrdh 为空";
                return response;
            }
        }
        bizResponse = service.KFExecutCancelStart(requestBodyInfo);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }
    @RequestMapping(value = "auth/mobile/advice/post/ZSExecutCancelEnd2Start")
    @ResponseBody
    public Response<ResponseBodyInfo> ZSExecutCancelEnd2Start(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        if (requestBodyInfo.QRDH == null) {
            String jhh = requestBodyInfo.core;
            BizResponse<String> response1 = service.getInjectionInfoListByZyh4Cancel(jhh, requestBodyInfo.JGID);
            if (response1.isSuccess) {
                requestBodyInfo.QRDH = response1.data;
            } else {
                response.ReType = -1;
                response.Msg = "qrdh 为空";
                return response;
            }
        }
        bizResponse = service.ZSExecutCancelStart(requestBodyInfo);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }


    @RequestMapping(value = "auth/mobile/advice/post/TransfuseExecutCancelEnd2Start")
    @ResponseBody
    public Response<ResponseBodyInfo> TransfuseExecutCancelEnd2Start(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        bizResponse = service.TransfuseExecutCancelStart(requestBodyInfo);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }

    @RequestMapping(value = "auth/mobile/advice/post/TransfuseExecutCancelEnd2Ing")
    @ResponseBody
    public Response<ResponseBodyInfo> TransfuseExecutCancelEnd2Ing(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<ResponseBodyInfo> bizResponse;

        bizResponse = service.TransfuseExecutCancelEnd(requestBodyInfo);
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
     * 输液滴速记录
     *
     * @param record
     * @return
     */
    @RequestMapping(value = "auth/mobile/advice/post/DropSpeedInput")
    @ResponseBody
    public Response<ResponseBodyInfo> DropSpeedInput(@RequestBody TransfusionPatrolRecord record) {
        Response<ResponseBodyInfo> response = new Response<>();
        BizResponse<String> bizResponse;

        bizResponse = service.DropSpeedInput(record);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
            ResponseBodyInfo responseBodyInfo = new ResponseBodyInfo();
            responseBodyInfo.Message = StringUtils.isBlank(bizResponse.data) ? "记录成功" : bizResponse.data;
            responseBodyInfo.TableName = "SHOW";
            response.Data = responseBodyInfo;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        return response;
    }


    @RequestMapping(value = "auth/mobile/advice/post/FJXH_RealDoSync")
    public
    @ResponseBody
    Response<ResponseBodyInfo> FJXH_RealDoSync(@RequestBody RequestBodyInfo requestBodyInfo) {
        Response<ResponseBodyInfo> response = new Response<>();
        Response<ResponseBodyInfo> bizResponse = new Response<>();

        bizResponse = service.FJXH_RealDoSync(requestBodyInfo.inArgument);
        if (bizResponse.ReType == 1) {
            //1 成功
            response.ReType = 0;
        } else if (bizResponse.ReType == 2) {
            //2 用户选择
            response.ReType = -777;
        } else {
            // 0 或 其他  代表失败
            response.ReType = -888;
        }
        bizResponse.Data.TableName = "RE";
        response.Msg = bizResponse.Msg;
        response.Data = bizResponse.Data;
        return response;
    }
}
