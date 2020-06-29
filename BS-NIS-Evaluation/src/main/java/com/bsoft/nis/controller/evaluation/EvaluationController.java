package com.bsoft.nis.controller.evaluation;

import com.bsoft.nis.domain.evaluation.EvaluateFormItem;
import com.bsoft.nis.domain.evaluation.EvaluateRecordItem;
import com.bsoft.nis.domain.evaluation.EvaluateResponse;
import com.bsoft.nis.domain.evaluation.EvaluateSaveRespose;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.evaluation.EvaluationMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 */
@Controller
public class EvaluationController {
    @Autowired
    EvaluationMainService service;

    /**
     * 获取评估单记录
     *
     * @param start
     * @param end
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/evaluation/GetEvaluationList")
    public @ResponseBody
    Response<List<EvaluateRecordItem>> GetEvaluationList(@RequestParam(value = "start") String start,
                                                                @RequestParam(value = "end") String end,
                                                                @RequestParam(value = "zyh") String zyh,
                                                                @RequestParam(value = "jgid") String jgid) {
        BizResponse<EvaluateRecordItem> bizResponse = new BizResponse<>();
        Response<List<EvaluateRecordItem>> response = new Response<>();
        bizResponse = service.GetEvaluationList(start, end, zyh, jgid);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.datalist;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;

    }


    /**
     * 获取病区评估单列表
     *
     * @param bqdm
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/evaluation/GetNewEvaluationList")
    public
    @ResponseBody
    Response<List<EvaluateRecordItem>> GetNewEvaluationList(@RequestParam(value = "bqdm") String bqdm,
                                                            @RequestParam(value = "jgid") String jgid,@RequestParam(value = "zyh") String zyh) {

        Response<List<EvaluateRecordItem>> response = new Response<>();
        BizResponse<EvaluateRecordItem> bizResponse = new BizResponse<>();
        bizResponse = service.GetNewEvaluationList(bqdm, jgid,zyh);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.datalist;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    /**
     * 通过样式类型获取病区评估单列表
     *
     * @param yslx
     * @param bqdm
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/evaluation/GetNewEvaluationListForYslx")
    public
    @ResponseBody
    Response<List<EvaluateRecordItem>> GetNewEvaluationListForYslx(
            @RequestParam(value = "yslx") String yslx,
            @RequestParam(value = "bqdm") String bqdm,
            @RequestParam(value = "jgid") String jgid) {
        Response<List<EvaluateRecordItem>> response = new Response<>();
        BizResponse<EvaluateRecordItem> bizResponse = new BizResponse<>();
        bizResponse = service.GetNewEvaluationListForYslx(yslx, bqdm, jgid);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.datalist;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    @RequestMapping(value = "auth/mobile/evaluation/getSJHQFS")
    public
    @ResponseBody
    Response<String> getSJHQFS(@RequestParam(value = "yslx") String yslx) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();
        bizResponse = service.getSJHQFS(yslx);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    /**
     * 获取一张评估记录
     *
     * @param jlxh
     * @param ysxh
     * @param lybs
     * @param txsj
     * @return
     */
    @RequestMapping(value = "auth/mobile/evaluation/GetEvaluation")
    public
    @ResponseBody
    Response<EvaluateFormItem> GetEvaluation(
            @RequestParam(value = "jlxh") String jlxh,
            @RequestParam(value = "ysxh") String ysxh,
            @RequestParam(value = "lybs") String lybs,
            @RequestParam(value = "txsj") String txsj,
            @RequestParam(value = "jgid") String jgid) {
        Response<EvaluateFormItem> response = new Response<>();
        BizResponse<EvaluateFormItem> bizResponse = new BizResponse<>();
        if (jlxh == null || jlxh.equals("")) {
            response.ReType = -1;
            response.Msg = "单号为空";
            return response;
        }
        //入参lybs(0：新版本评估单，1：emr评估单（新版本去掉1的处理）)
//        if (lybs == null || lybs.equals("0")) {
            bizResponse = service.GetExistEvaluation(jlxh, txsj, jgid,null);
//        } else {
//            response.ReType = -1;
//            response.Msg = "新版本不支持emr评估单";
//            return response;
//        }
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    /**
     * 获取新的评估单
     *
     * @param bqdm
     * @param zyh
     * @param ysxh
     * @param txsj
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/evaluation/GetNewEvaluation")
    public
    @ResponseBody
    Response<EvaluateFormItem> GetNewEvaluation(
            @RequestParam(value = "isZKNotCheckBQ") boolean isZKNotCheckBQ,
            @RequestParam(value = "isNewPage") boolean isNewPage,
            @RequestParam(value = "bqdm") String bqdm,
            @RequestParam(value = "zyh") String zyh,
            @RequestParam(value = "ysxh") String ysxh,
            @RequestParam(value = "txsj") String txsj,
            @RequestParam(value = "jgid") String jgid) {
        Response<EvaluateFormItem> response = new Response<>();
        BizResponse<EvaluateFormItem> bizResponse = new BizResponse<>();
        bizResponse = service.GetNewEvaluation(isZKNotCheckBQ,isNewPage,bqdm, zyh, ysxh, txsj, jgid);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    /**
     * 保存评估单
     */
    @RequestMapping(method = RequestMethod.POST,value = {"auth/mobile/evaluation/post/SaveEvaluation", "mobile/evaluation/post/SaveEvaluation"})
    public
    @ResponseBody
    Response<EvaluateFormItem> SaveEvaluation(@RequestBody EvaluateSaveRespose parms) {
        Response<EvaluateFormItem> response = new Response<>();
        BizResponse<EvaluateFormItem> bizResponse = new BizResponse<>();
        bizResponse = service.SaveEvaluation(parms);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    /**
     * 作废评估单
     */
    @RequestMapping(value = "auth/mobile/evaluation/CancelEvaluation")
    public
    @ResponseBody
    Response<EvaluateResponse> CancelEvaluation(@RequestParam(value = "jlxh") String jlxh,
                                                @RequestParam(value = "jgid") String jgid) {
        Response<EvaluateResponse> response = new Response<>();
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        bizResponse = service.CancelEvaluation(jlxh, jgid);
        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.Data = bizResponse.data;
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    /**
     * 签名
     */
    @RequestMapping(value = "auth/mobile/evaluation/EvaluationSignature")
    public
    @ResponseBody
    Response<EvaluateResponse> EvaluationSignature(
            @RequestParam(value = "jlxh") String jlxh,
            @RequestParam(value = "ysxh") String ysxh,
            @RequestParam(value = "ysfl") String ysfl,
            @RequestParam(value = "lybs") String lybs,
            @RequestParam(value = "hsqm1") String hsqm1,
            @RequestParam(value = "hsqm2") String hsqm2,
            @RequestParam(value = "dlbz") String dlbz,
            @RequestParam(value = "qmbz") String qmbz) {
        Response<EvaluateResponse> response = new Response<>();
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        bizResponse = service.EvaluationSignature(jlxh, ysxh, ysfl, lybs, hsqm1, hsqm2, dlbz, qmbz);
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    /**
     * 取消签名
     */
    @RequestMapping(value = "auth/mobile/evaluation/CancelEvaluationSignature")
    public @ResponseBody
    Response<EvaluateResponse> CancelEvaluationSignature(
            @RequestParam(value = "jlxh") String jlxh,
            @RequestParam(value = "ysxh") String ysxh,
            @RequestParam(value = "ysfl") String ysfl,
            @RequestParam(value = "lybs") String lybs,
            @RequestParam(value = "hsqm1") String hsqm1,
            @RequestParam(value = "hsqm2") String hsqm2,
            @RequestParam(value = "dlbz") String dlbz,
            @RequestParam(value = "qmbz") String qmbz,
            @RequestParam(value = "jgid") String jgid) {
        Response<EvaluateResponse> response = new Response<>();
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        bizResponse = service.CancelEvaluationSignature(jlxh, ysxh, ysfl, lybs, hsqm1, hsqm2, dlbz, qmbz);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
            response.Data = bizResponse.data;
            response.Msg = bizResponse.message;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    /**
     * 审核
     */
    @RequestMapping(value = "auth/mobile/evaluation/EvaluationReview")
    public @ResponseBody
    Response<EvaluateResponse> EvaluationReview(
            @RequestParam(value = "jlxh") String jlxh,
            @RequestParam(value = "sygh") String sygh,
            @RequestParam(value = "jgid") String jgid) {
        Response<EvaluateResponse> response = new Response<>();
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        bizResponse = service.EvaluationReview(jlxh, sygh, jgid);
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    /**
     * 取消审核
     */
    @RequestMapping(value = "auth/mobile/evaluation/CancelEvaluationReview")
    public @ResponseBody
    Response<EvaluateResponse> CancelEvaluationReview(
            @RequestParam(value = "jlxh") String jlxh,
            @RequestParam(value = "sygh") String sygh) {
        Response<EvaluateResponse> response = new Response<>();
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        bizResponse = service.CancelEvaluationReview(jlxh, sygh);
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    /**
     * 获取对照项目的数据
     * @param zyh
     * @param ysxh
     * @param dzlx
     * @param bqdm
     * @param txsj
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/evaluation/GetRelativeData")
    public
    @ResponseBody
    Response<EvaluateFormItem> GetRelativeData(
            @RequestParam(value = "zyh") String zyh,
            @RequestParam(value = "ysxh") String ysxh,
            @RequestParam(value = "dzlx") String dzlx,
            @RequestParam(value = "bqdm") String bqdm,
            @RequestParam(value = "txsj") String txsj,
            @RequestParam(value = "jgid") String jgid) {
        Response<EvaluateFormItem> response = new Response<>();
        BizResponse<EvaluateFormItem> bizResponse = service.CreateRelativeEvaluation(zyh, ysxh, dzlx,bqdm,txsj,jgid);

        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }
}
