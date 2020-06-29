package com.bsoft.nis.controller.handover;

import com.bsoft.nis.domain.handover.HandOverForm;
import com.bsoft.nis.domain.handover.HandOverRecord;
import com.bsoft.nis.domain.handover.RelativeItem;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.handover.HandOverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 交接单控制器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-02-15
 * Time: 10:34
 * Version:
 */
@Controller
public class HandOverController {

    @Autowired
    HandOverService service;

    @RequestMapping(value = "/handover")
    public String getMainPatientPage() {
        return "handover/handover";
    }

    /**
     * 获取交接单模板列表及其记录数量
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/handover/get/getHandOverList")
    public
    @ResponseBody
    Response<List<HandOverForm>> getHandOverList(@RequestParam(value = "zyh") String zyh,
                                                 @RequestParam(value = "bqid") String bqid,
                                                 @RequestParam(value = "jgid") String jgid) {
        Response<List<HandOverForm>> response = new Response<>();
        BizResponse<HandOverForm> bizResponse = new BizResponse<>();

        bizResponse = service.getHandOverList(zyh, bqid, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    /**
     * 获取交接单记录数据
     *
     * @param jlxh 记录序号
     * @param ysxh 样式序号
     * @return
     */
    @RequestMapping(value = "auth/mobile/handover/get/getHandOverRecord")
    public
    @ResponseBody
    Response<HandOverRecord> getHandOverRecord(@RequestParam(value = "jlxh") String jlxh,
                                               @RequestParam(value = "ysxh") String ysxh,
                                               @RequestParam(value = "zyh") String zyh,
                                               @RequestParam(value = "txsj") String txsj,
                                               @RequestParam(value = "jgid") String jgid) {
        Response<HandOverRecord> response = new Response<>();
        BizResponse<HandOverRecord> bizResponse = new BizResponse<>();

        bizResponse = service.getHandOverRecord(jlxh, ysxh, zyh, txsj, jgid);
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
     * 删除交接单记录数据
     *
     * @param jlxh 记录序号
     * @return
     */
    @RequestMapping(value = "auth/mobile/handover/get/delHandOverRecord")
    public
    @ResponseBody
    Response<String> delHandOverRecord(@RequestParam(value = "jlxh") String jlxh) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.delHandOverRecord(jlxh);
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
     * 发起方发送交接单记录数据
     *
     * @param jlxh 记录序号
     * @return
     */
    @RequestMapping(value = "auth/mobile/handover/get/sendHandOverRecord")
    public
    @ResponseBody
    Response<String> sendHandOverRecord(@RequestParam(value = "jlxh") String jlxh) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.sendHandOverRecord(jlxh);
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
     * 保存交接单记录数据
     *
     * @param handOverRecord
     * @return
     */
    @RequestMapping(value = {"auth/mobile/handover/post/saveHandOverRecord", "mobile/handover/post/saveHandOverRecord"})
    public
    @ResponseBody
    Response<HandOverRecord> saveHandOverRecord(@RequestBody HandOverRecord handOverRecord) {
        Response<HandOverRecord> response = new Response<>();
        BizResponse<HandOverRecord> bizResponse = new BizResponse<>();

        bizResponse = service.saveHandOverRecord(handOverRecord);
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
     * 获取对照项目的数据
     *
     * @param zyh
     * @param dzlx
     * @param txsj
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/handover/get/getRelativeData")
    public
    @ResponseBody
    Response<List<RelativeItem>> getRelativeData(@RequestParam(value = "zyh") String zyh,
                                                 @RequestParam(value = "dzlx") String dzlx,
                                                 @RequestParam(value = "txsj") String txsj,
                                                 @RequestParam(value = "jgid") String jgid) {
        Response<List<RelativeItem>> response = new Response<>();
        BizResponse<RelativeItem> bizResponse = service.GetRelativeData(zyh, dzlx, txsj, jgid);

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
     * 获取交接单记录数据 - 批量用
     *
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/handover/get/getHandOverRecordList")
    public
    @ResponseBody
    Response<List<HandOverRecord>> getHandOverRecordList(@RequestParam(value = "bqid") String bqid,
                                                         @RequestParam(value = "jgid") String jgid) {
        Response<List<HandOverRecord>> response = new Response<>();
        BizResponse<HandOverRecord> bizResponse = new BizResponse<>();

        bizResponse = service.getHandOverRecordList(bqid, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    /**
     * 获取交接单记录数据 - 批量用
     *
     * @param ssks 手术科室
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/handover/get/getHandOverRecordListBySSKS")
    public
    @ResponseBody
    Response<List<HandOverRecord>> getHandOverRecordListByJSBQ(@RequestParam(value = "ssks") String ssks,
                                                         @RequestParam(value = "jgid") String jgid) {
        Response<List<HandOverRecord>> response = new Response<>();
        BizResponse<HandOverRecord> bizResponse = new BizResponse<>();

        bizResponse = service.getHandOverRecordListBySSKS(ssks, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    /**
     * 接收交接单记录数据
     *
     * @param handOverRecord
     * @return
     */
    @RequestMapping(value = {"auth/mobile/handover/post/receiveHandOverRecord", "mobile/handover/post/receiveHandOverRecord"})
    public
    @ResponseBody
    Response<HandOverRecord> receiveHandOverRecord(@RequestBody HandOverRecord handOverRecord) {
        Response<HandOverRecord> response = new Response<>();
        BizResponse<HandOverRecord> bizResponse = new BizResponse<>();

        bizResponse = service.receiveHandOverRecord(handOverRecord);
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
