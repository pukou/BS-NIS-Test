package com.bsoft.nis.controller.lifesign;

import com.bsoft.nis.domain.clinicalevent.ClinicalEventSaveData;
import com.bsoft.nis.domain.clinicalevent.ClinicalEventType;
import com.bsoft.nis.domain.lifesign.*;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.lifesign.LifeSignMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: 生命体征控制器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Controller
public class LifeSignController {

    @Autowired
    LifeSignMainService service;

    @RequestMapping(value = "/lifesign")
    public String getMainPatientPage() {
        return "lifesign/lifesign";
    }

    /**
     * 获取生命体征控件json
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/getLifeSignTypeItemList")
    public
    @ResponseBody
    Response<List<LifeSignTypeItem>> getLifeSignTypeItemList(@RequestParam(value = "zyh") String zyh,
                                                             @RequestParam(value = "bqid") String bqid,
                                                             @RequestParam(value = "jgid") String jgid) {
        Response<List<LifeSignTypeItem>> response = new Response<>();
        BizResponse<LifeSignTypeItem> bizResponse = new BizResponse<>();

        bizResponse = service.getLifeSignTypeItemList(zyh, bqid, jgid);
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
     * 获取生命体征(活动)控件json
     *
     * @param srxh 输入序号
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/getLifeSignItem")
    public
    @ResponseBody
    Response<LifeSignInputItem> getLifeSignItem(@RequestParam(value = "srxh") String srxh,
                                                @RequestParam(value = "zyh") String zyh,
                                                @RequestParam(value = "jgid") String jgid) {
        Response<LifeSignInputItem> response = new Response<>();
        BizResponse<LifeSignInputItem> bizResponse = new BizResponse<>();

        bizResponse = service.getLifeSignItem(srxh, zyh, jgid);
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
     * 保存生命体征数据
     *
     * @param lifeSignSaveData 要保存的数据
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/post/saveLifeSignData")
    public
    @ResponseBody
    Response<LifeSignSync> saveLifeSignData(@RequestBody LifeSignSaveData lifeSignSaveData) {
        Response<LifeSignSync> response = new Response<>();
        BizResponse<LifeSignSync> bizResponse = new BizResponse<>();

        bizResponse = service.saveLifeSignData(lifeSignSaveData);
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
     * 获取生命体征历史记录
     *
     * @param start 开始日期
     * @param end   结束日期
     * @param zyh   住院号
     * @param jgid  机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/getLifeSignHistoryData")
    public
    @ResponseBody
    Response<LifeSignHistoryData> getLifeSignHistoryData(@RequestParam(value = "start") String start,
                                                         @RequestParam(value = "end") String end,
                                                         @RequestParam(value = "zyh") String zyh,
                                                         @RequestParam(value = "jgid") String jgid) {
        Response<LifeSignHistoryData> response = new Response<>();
        BizResponse<LifeSignHistoryData> bizResponse = new BizResponse<>();

        bizResponse = service.getLifeSignHistoryData(start, end, zyh, jgid);
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
     * 删除(作废)生命体征数据
     *
     * @param cjh 采集号
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/deleteLifeSignHistoryData")
    public
    @ResponseBody
    Response<String> deleteLifeSignHistoryData(@RequestParam(value = "cjh") String cjh,
                                               @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.deleteLifeSignHistoryData(cjh, jgid);
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
     * 删除(作废)生命体征数据
     *
     * @param cjh 采集号
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/updateLifeSignHistoryData")
    public
    @ResponseBody
    Response<String> updateLifeSignHistoryData(@RequestParam(value = "cjh") String cjh,
                                               @RequestParam(value = "value") String value,
                                               @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.updateLifeSignHistoryData(cjh, value,jgid);
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
     * 获取生命体征复测历史数据
     *
     * @param tzxm 体征项目
     *             1 体温 502 疼痛
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/getLifeSignDoubleCheckHistoryData")
    public
    @ResponseBody
    Response<LifeSignDoubleCheckHistoryData> getLifeSignDoubleCheckHistoryData(@RequestParam(value = "tzxm") String tzxm,
                                                                               @RequestParam(value = "zyh") String zyh,
                                                                               @RequestParam(value = "jgid") String jgid) {
        Response<LifeSignDoubleCheckHistoryData> response = new Response<>();
        BizResponse<LifeSignDoubleCheckHistoryData> bizResponse = new BizResponse<>();

        bizResponse = service.getLifeSignDoubleCheckHistoryData(tzxm, zyh, jgid);
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
     * 获取生命体征采集时刻表
     *
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/getTimePointList")
    public
    @ResponseBody
    Response<List<LifeSignTimeEntity>> getTimePointList(@RequestParam(value = "bqid") String bqid, @RequestParam(value = "jgid") String jgid) {
        Response<List<LifeSignTimeEntity>> response = new Response<>();
        BizResponse<LifeSignTimeEntity> bizResponse = new BizResponse<>();

        bizResponse = service.getTimePointList(bqid, jgid);
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
     * 获获取临床事件类型列表
     *
     * @param zyh  住院号
     * @param yhid 用户id
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/getClinicalEventTypeList")
    public
    @ResponseBody
    Response<List<ClinicalEventType>> getClinicalEventTypeList(@RequestParam(value = "zyh") String zyh, @RequestParam(value = "yhid") String yhid,
                                                               @RequestParam(value = "bqid") String bqid, @RequestParam(value = "jgid") String jgid) {
        Response<List<ClinicalEventType>> response = new Response<>();
        BizResponse<ClinicalEventType> bizResponse = new BizResponse<>();

        bizResponse = service.getClinicalEventTypeList(zyh, yhid, bqid, jgid);
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
     * 保存临床事件
     *
     * @param clinicalEventSaveData 要保存的数据
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/post/saveClinicalEventData")
    public
    @ResponseBody
    Response<List<ClinicalEventType>> saveClinicalEventData(@RequestBody ClinicalEventSaveData clinicalEventSaveData) {
        Response<List<ClinicalEventType>> response = new Response<>();
        BizResponse<ClinicalEventType> bizResponse = new BizResponse<>();

        bizResponse = service.saveClinicalEventData(clinicalEventSaveData);
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
     * 删除临床事件
     *
     * @param sjxh 事件序号
     * @param zyh  住院号
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/get/deleteClinicalEventData")
    public
    @ResponseBody
    Response<List<ClinicalEventType>> deleteClinicalEventData(@RequestParam(value = "sjxh") String sjxh, @RequestParam(value = "zyh") String zyh,
                                                              @RequestParam(value = "bqid") String bqid, @RequestParam(value = "yhid") String yhid,
                                                              @RequestParam(value = "jgid") String jgid) {
        Response<List<ClinicalEventType>> response = new Response<>();
        BizResponse<ClinicalEventType> bizResponse = new BizResponse<>();

        bizResponse = service.deleteClinicalEventData(sjxh, zyh, yhid, bqid, jgid);
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
     * 获取病人列表
     * @param bqid
     * @param start
     * @param end
     * @param type
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/lifesign/GetPatientList")
    public
    @ResponseBody
    Response<List<SickPersonVo>> getPatientList(@RequestParam(value = "bqid") String bqid, @RequestParam(value = "start") String start,
                                                @RequestParam(value = "end") String end, @RequestParam(value = "type") String type,
                                                @RequestParam(value = "jgid") String jgid) {
        Response<List<SickPersonVo>> response = new Response<>();
        BizResponse<SickPersonVo> bizResponse = new BizResponse<>();

        bizResponse = service.getPatientList(bqid, start, end, type, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    @RequestMapping(value = "auth/mobile/lifesign/get/getLifeSignHistoryInfo")
    public
    @ResponseBody
    Response<List<LifeSignHistoryInfo>> getLifeSignHistoryInfo(@RequestParam(value = "zyh") String zyh,
                                                @RequestParam(value = "xmh") String xmh,
                                                @RequestParam(value = "jgid") String jgid) {
        Response<List<LifeSignHistoryInfo>> response = new Response<>();

        BizResponse<List<LifeSignHistoryInfo>> bizRes = service.getLifeSignHistoryInfo(zyh, xmh);
        if(bizRes.isSuccess){
            response.ReType = 0;
            response.Data = bizRes.data;
        }else{
            response.ReType = -1;
            response.Msg = bizRes.message;
        }

        return response;
    }
}
