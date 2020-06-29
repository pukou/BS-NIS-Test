package com.bsoft.nis.controller.evaluation.v56_update1;

import com.bsoft.nis.domain.core.cached.Map2;
import com.bsoft.nis.domain.evaluation.evalnew.*;
import com.bsoft.nis.domain.evaluation.nursingeval.*;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.evaluation.v56_update1.EvaluationUpdate1MainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * description:重新设计护理评估单：
 * 1.支持无限级项目
 * 2.支持自伸缩
 * create by: dragon xinghl@bsoft.com.cn
 * create time:2017/11/20 11:19
 * since:5.6 update1
 */
@Controller
@Api(tags = "护理评估(V5.6-update1)")
public class EvaluationUpdate1Controller {

    @Autowired
    EvaluationUpdate1MainService service;

    /**
     * 加载评估单样式
     *
     * @param jgid
     * @param styleId
     * @param version
     * @param zyh
     * @return
     */
    @RequestMapping(value = "mobile/evaluation/v56_update1/get/style")
    public @ResponseBody
    Response<List<EvalutionStyle>> getEvalutionStyleSingleByPrimaryKey(@RequestParam(value = "JGID") String jgid,
                                                                       @RequestParam(value = "YSXH") Long styleId,
                                                                       @RequestParam(value = "BBH") Integer version,
                                                                       @RequestParam(value = "ZYH") String zyh) {
        Response<List<EvalutionStyle>> response = new Response<>();
        BizResponse<EvalutionStyle> bizResponse;
        bizResponse = service.getEvalutionStyleSingleByPrimaryKey(jgid, styleId, version, zyh);
        response.Data = bizResponse.datalist;
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    @RequestMapping(value = "auth/mobile/evaluation/GetNursingEvaluation_V56Update1")
    public @ResponseBody
    Response<KeyValue<List<NursingEvaluateStyte>, NursingEvaluateRecord>> GetNursingEvaluation_V56Update1(@RequestParam(value = "jlxh") String jlxh) {
        Response<KeyValue<List<NursingEvaluateStyte>, NursingEvaluateRecord>> response = new Response<>();
        /////
        BizResponse<Map> bizResponse;
        bizResponse = service.getEvalutionRecordByPrimaryKey(jlxh);
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
            //
            Map map = bizResponse.data;
            List<EvalutionStyle> evalutionStyleList = (List<EvalutionStyle>) map.get("style");
            EvalutionRecord record = (EvalutionRecord) map.get("record");
            ////
            List<NursingEvaluateStyte> nursingEvaluateStyteList = parseToData_NursingEvaluateStyte(evalutionStyleList);
            NursingEvaluateRecord nursingEvaluateRecord = parseToData_NursingEvaluateRecord(record);
            ////
            response.Data = KeyValue.create(nursingEvaluateStyteList, nursingEvaluateRecord);
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    @RequestMapping(value = "auth/mobile/evaluation/post/SaveNursingEvaluation_V56Update1")
    public @ResponseBody
    Response<NursingEvaluateRecord> SaveNursingEvaluation_V56Update1(@RequestBody NursingEvaluateRecord record) {
        Response<NursingEvaluateRecord> response = new Response<>();
        ///
        EvalutionRecord er = parseToData_EvalutionRecord(record);
        Response<EvalutionRecord> evalutionRecordResponse = commitEvalutionData(er);
        response.Data = parseToData_NursingEvaluateRecord(evalutionRecordResponse.Data);
        response.Msg = evalutionRecordResponse.Msg;
        response.ReType = evalutionRecordResponse.ReType;
        return response;
    }


    @RequestMapping(value = "auth/mobile/evaluation/GetNewNursingEvaluation_V56Update1")
    public @ResponseBody
    Response<KeyValue<List<NursingEvaluateStyte>, Map<String, String>>> GetNewNursingEvaluation_V56Update1(@RequestParam(value = "jgid") String jgid,
                                                                                                           @RequestParam(value = "ysxh") Long ysxh,
                                                                                                           @RequestParam(value = "brbq") String brbq,
                                                                                                           @RequestParam(value = "bbh") Integer bbh,
                                                                                                           @RequestParam(value = "zyh") String zyh) {
        Response<KeyValue<List<NursingEvaluateStyte>, Map<String, String>>> response = new Response<>();
        /////////
        BizResponse<EvalutionStyle> bizResponse = service.getEvalutionStyleSingleByPrimaryKey(jgid, ysxh, bbh, zyh);
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
            //
            List<EvalutionStyle> evalutionStyleList = bizResponse.datalist;
            ////
            List<NursingEvaluateStyte> nursingEvaluateStyteList = parseToData_NursingEvaluateStyte(evalutionStyleList);
            if (nursingEvaluateStyteList.size() > 0 && "1".equals(nursingEvaluateStyteList.get(0).DYWD)) {
                //单一文本  记录多于一条
                Response<List<EvalutionRecord>> responseTemp = getEvaluationRecordList(zyh, brbq, jgid);
                if (responseTemp.ReType == 0 && responseTemp.Data != null && responseTemp.Data.size() > 0) {
                    response.ReType = -1;
                    response.Msg = "表单只能有一份";
                    return response;
                }
            }
            //
            List<Map2> baseInfo = bizResponse.datalist.get(0).getBaseInfo();
            Map<String, String> stringMap = null;
            if (baseInfo != null) {
                stringMap = new HashMap<>();
                for (Map2 map2 : baseInfo) {
                    stringMap.put(map2.key, map2.value);
                }
            }
            ////
            response.Data = KeyValue.create(nursingEvaluateStyteList, stringMap);
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    /**
     * 根据记录序号获取记录及记录所用模板数据
     *
     * @param jlxh
     * @return
     */
    @RequestMapping(value = "mobile/evaluation/v56_update1/get/record")
    @ApiOperation(httpMethod = "GET", value = "根据评估序号获取评估模板及评估详细记录数据", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Response<Map> getEvalutionRecordByPrimaryKey(@ApiParam(name = "JLXH",required = true,value = "评估记录值(IENR_PGJL)") @RequestParam(value = "JLXH") String jlxh) {
        Response<Map> response = new Response<>();
        BizResponse<Map> bizResponse;
        bizResponse = service.getEvalutionRecordByPrimaryKey(jlxh);
        response.Data = bizResponse.data;
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
     * 签名
     *
     * @param mode
     * @param jlxh
     * @param signwho
     * @param usercode
     * @param username
     * @return
     */
    @RequestMapping(value = {"mobile/evaluation/v56_update1/sign", "auth/mobile/evaluation/SignNursingEvaluation_V56Update1"})
    public @ResponseBody
    Response<String> signEvalutionRecord(@RequestParam(value = "mode") String mode,
                                         @RequestParam(value = "jlxh") String jlxh,
                                         @RequestParam(value = "signWho") String signwho,
                                         @RequestParam(value = "signUserCode") String usercode,
                                         @RequestParam(value = "signUserName") String username) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse;
        bizResponse = service.signEvalutionRecord(mode, jlxh, signwho, usercode, username);
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
            response.Data = "sign".equals(mode) ? "签名成功" : "取消签名成功";
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    @RequestMapping(value = "auth/mobile/evaluation/GetNursingEvaluationStyleList_V56Update1")
    public @ResponseBody
    Response<List<NursingEvaluateStyte>> GetNursingEvaluationStyleList_V56Update1(@RequestParam(value = "zyh") String zyh,
                                                                                  @RequestParam(value = "brbq") String brbq,
                                                                                  @RequestParam(value = "jgid") String jgid) {
        Response<List<NursingEvaluateStyte>> response = new Response<>();
        Response<List<EvalutionStyle>> responseTemp = getEvalutionStyleList(jgid);
        List<NursingEvaluateStyte> nursingEvaluateStyteList = new ArrayList<>();
        if (responseTemp.ReType == 0) {
            List<EvalutionStyle> evalutionStyleList = responseTemp.Data;
            nursingEvaluateStyteList.addAll(parseToData_NursingEvaluateStyte(evalutionStyleList));
        }
        response.Msg = responseTemp.Msg;
        response.ReType = responseTemp.ReType;
        response.Data = nursingEvaluateStyteList;
        return response;
    }

    @RequestMapping(value = "auth/mobile/evaluation/GetNursingEvaluationRecordList_V56Update1")
    public @ResponseBody
    Response<List<NursingEvaluateRecord>> GetNursingEvaluationRecordList_V56Update1(@RequestParam(value = "zyh") String zyh,
                                                                                    @RequestParam(value = "brbq") String brbq,
                                                                                    @RequestParam(value = "jgid") String jgid) {
        Response<List<NursingEvaluateRecord>> response = new Response<>();
        Response<List<EvalutionRecord>> responseTemp = getEvaluationRecordList(zyh, brbq, jgid);
        List<NursingEvaluateRecord> nursingEvaluateRecordList = new ArrayList<>();
        if (responseTemp.ReType == 0) {
            List<EvalutionRecord> ls = responseTemp.Data;
            for (EvalutionRecord evalutionRecord : ls) {
                NursingEvaluateRecord nursingEvaluateRecord = parseToData_NursingEvaluateRecord(evalutionRecord);
                nursingEvaluateRecord.YSMC = getYSMCByYSXH(nursingEvaluateRecord.YSXH, jgid);
                nursingEvaluateRecordList.add(nursingEvaluateRecord);
            }
        }
        response.Msg = responseTemp.Msg;
        response.ReType = responseTemp.ReType;
        response.Data = nursingEvaluateRecordList;
        return response;
    }

    /**
     * 通过配置的sql语句获取下拉框数据
     *
     * @param dataSource
     * @param sql
     * @return
     */
    @RequestMapping(value = "mobile/evaluation/v56_update1/get/combo/datas")
    public @ResponseBody
    List<ComboUi> getComboboxDatas(@RequestParam(value = "DataSource") String dataSource,
                                   @RequestParam(value = "Sql") String sql) {
        List<ComboUi> datas = new ArrayList<>();
        datas = service.getComboboxDatas(dataSource, sql);
        return datas;
    }

    private EvalutionRecord parseToData_EvalutionRecord(NursingEvaluateRecord nursingEvaluateRecord) {
        if (nursingEvaluateRecord == null) {
            return null;
        }
        EvalutionRecord record = new EvalutionRecord();
        record.setJLXH(!TextUtils.isBlank(nursingEvaluateRecord.JLXH) ? Long.valueOf(nursingEvaluateRecord.JLXH) : 0);
        record.setZYH(!TextUtils.isBlank(nursingEvaluateRecord.ZYH) ? Long.valueOf(nursingEvaluateRecord.ZYH) : 0);
        record.setBRBQ(!TextUtils.isBlank(nursingEvaluateRecord.BRBQ) ? Integer.valueOf(nursingEvaluateRecord.BRBQ) : 0);
        record.setYSXH(!TextUtils.isBlank(nursingEvaluateRecord.YSXH) ? Long.valueOf(nursingEvaluateRecord.YSXH) : 0);
        record.setBBH(!TextUtils.isBlank(nursingEvaluateRecord.BBH) ? Integer.valueOf(nursingEvaluateRecord.BBH) : 0);
        record.setYSLX(!TextUtils.isBlank(nursingEvaluateRecord.YSLX) ? Long.valueOf(nursingEvaluateRecord.YSLX) : 0);
        record.setTXSJ(!TextUtils.isBlank(nursingEvaluateRecord.TXSJ) ? parseToData_Date(nursingEvaluateRecord.TXSJ) : null);
        record.setTXGH(!TextUtils.isBlank(nursingEvaluateRecord.TXGH) ? String.valueOf(nursingEvaluateRecord.TXGH) : null);
        record.setTXXM(!TextUtils.isBlank(nursingEvaluateRecord.TXXM) ? String.valueOf(nursingEvaluateRecord.TXXM) : null);
        record.setJLSJ(!TextUtils.isBlank(nursingEvaluateRecord.JLSJ) ? parseToData_Date(nursingEvaluateRecord.JLSJ) : null);
        record.setQMGH(!TextUtils.isBlank(nursingEvaluateRecord.QMGH) ? String.valueOf(nursingEvaluateRecord.QMGH) : null);
        record.setQMSJ(!TextUtils.isBlank(nursingEvaluateRecord.QMSJ) ? parseToData_Date(nursingEvaluateRecord.QMSJ) : null);
        record.setQMXM(!TextUtils.isBlank(nursingEvaluateRecord.QMXM) ? String.valueOf(nursingEvaluateRecord.QMXM) : null);
        record.setSYZT(!TextUtils.isBlank(nursingEvaluateRecord.SYZT) ? Short.valueOf(nursingEvaluateRecord.SYZT) : 0);
        record.setSYGH(!TextUtils.isBlank(nursingEvaluateRecord.SYGH) ? String.valueOf(nursingEvaluateRecord.SYGH) : null);
        record.setSYSJ(!TextUtils.isBlank(nursingEvaluateRecord.SYSJ) ? parseToData_Date(nursingEvaluateRecord.SYSJ) : null);
        record.setSYXM(!TextUtils.isBlank(nursingEvaluateRecord.SYXM) ? String.valueOf(nursingEvaluateRecord.SYXM) : null);
        record.setDYCS(!TextUtils.isBlank(nursingEvaluateRecord.DYCS) ? Short.valueOf(nursingEvaluateRecord.DYCS) : 0);
        record.setZFBZ(!TextUtils.isBlank(nursingEvaluateRecord.ZFBZ) ? Short.valueOf(nursingEvaluateRecord.ZFBZ) : 0);
        record.setJGID(!TextUtils.isBlank(nursingEvaluateRecord.JGID) ? Integer.valueOf(nursingEvaluateRecord.JGID) : 0);
        record.setPGNR(!TextUtils.isBlank(nursingEvaluateRecord.PGNR) ? String.valueOf(nursingEvaluateRecord.PGNR) : null);
        record.setStatus(!TextUtils.isBlank(nursingEvaluateRecord.Status) ? String.valueOf(nursingEvaluateRecord.Status) : null);
        List<NursingEvaluateRecordDetail> nursingEvaluateRecordDetailList = nursingEvaluateRecord.detailList;
        List<EvalutionRecordDetail> detailList = new ArrayList<>();
        for (NursingEvaluateRecordDetail nursingEvaluateRecordDetail : nursingEvaluateRecordDetailList) {
            EvalutionRecordDetail detail = new EvalutionRecordDetail();
            detail.setJLXH(!TextUtils.isBlank(nursingEvaluateRecordDetail.JLXH) ? Long.valueOf(nursingEvaluateRecordDetail.JLXH) : 0);
            detail.setMXXH(!TextUtils.isBlank(nursingEvaluateRecordDetail.MXXH) ? Long.valueOf(nursingEvaluateRecordDetail.MXXH) : 0);
            detail.setJLXH(!TextUtils.isBlank(nursingEvaluateRecordDetail.JLXH) ? Long.valueOf(nursingEvaluateRecordDetail.JLXH) : 0);
            detail.setXMXH(!TextUtils.isBlank(nursingEvaluateRecordDetail.XMXH) ? Long.valueOf(nursingEvaluateRecordDetail.XMXH) : 0);
            detail.setSJXM(!TextUtils.isBlank(nursingEvaluateRecordDetail.SJXM) ? Long.valueOf(nursingEvaluateRecordDetail.SJXM) : 0);
            detail.setSJXMMC(!TextUtils.isBlank(nursingEvaluateRecordDetail.SJXMMC) ? String.valueOf(nursingEvaluateRecordDetail.SJXMMC) : null);
            detail.setXMNR(!TextUtils.isBlank(nursingEvaluateRecordDetail.XMNR) ? String.valueOf(nursingEvaluateRecordDetail.XMNR) : null);
            detail.setDZLX(!TextUtils.isBlank(nursingEvaluateRecordDetail.DZLX) ? Short.valueOf(nursingEvaluateRecordDetail.DZLX) : 0);
            detail.setDZBDJL(!TextUtils.isBlank(nursingEvaluateRecordDetail.DZBDJL) ? Long.valueOf(nursingEvaluateRecordDetail.DZBDJL) : 0);
            detail.setKJLX(!TextUtils.isBlank(nursingEvaluateRecordDetail.KJLX_Web) ? Short.valueOf(nursingEvaluateRecordDetail.KJLX_Web) : 0);
            detail.setStatus(!TextUtils.isBlank(nursingEvaluateRecordDetail.Status) ? String.valueOf(nursingEvaluateRecordDetail.Status) : null);
            detailList.add(detail);
        }
        record.setDetails(detailList);
        return record;
    }

    private Date parseToData_Date(String sjStr) {
        Date date = null;
        if (!TextUtils.isBlank(sjStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(sjStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    private String parseToData_DateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = sdf.format(date);
        return timeStr;
    }

    private NursingEvaluateRecord parseToData_NursingEvaluateRecord(EvalutionRecord record) {
        if (record == null) {
            return null;
        }
        NursingEvaluateRecord nursingEvaluateRecord = new NursingEvaluateRecord();
        nursingEvaluateRecord.JLXH = record.getJLXH() != null ? String.valueOf(record.getJLXH()) : null;
        nursingEvaluateRecord.ZYH = record.getZYH() != null ? String.valueOf(record.getZYH()) : null;
        nursingEvaluateRecord.BRBQ = record.getBRBQ() != null ? String.valueOf(record.getBRBQ()) : null;
        nursingEvaluateRecord.YSXH = record.getYSXH() != null ? String.valueOf(record.getYSXH()) : null;
        nursingEvaluateRecord.BBH = record.getBBH() != null ? String.valueOf(record.getBBH()) : null;
        nursingEvaluateRecord.YSLX = record.getYSLX() != null ? String.valueOf(record.getYSLX()) : null;
        nursingEvaluateRecord.TXSJ = record.getTXSJ() != null ? parseToData_DateStr(record.getTXSJ()) : null;
        nursingEvaluateRecord.TXGH = record.getTXGH() != null ? String.valueOf(record.getTXGH()) : null;
        nursingEvaluateRecord.TXXM = record.getTXXM() != null ? String.valueOf(record.getTXXM()) : null;
        nursingEvaluateRecord.JLSJ = record.getJLSJ() != null ? parseToData_DateStr(record.getJLSJ()) : null;
        nursingEvaluateRecord.QMGH = record.getQMGH() != null ? String.valueOf(record.getQMGH()) : null;
        nursingEvaluateRecord.QMSJ = record.getQMSJ() != null ? parseToData_DateStr(record.getQMSJ()) : null;
        nursingEvaluateRecord.QMXM = record.getQMXM() != null ? String.valueOf(record.getQMXM()) : null;
        nursingEvaluateRecord.SYZT = record.getSYZT() != null ? String.valueOf(record.getSYZT()) : null;
        nursingEvaluateRecord.SYGH = record.getSYGH() != null ? String.valueOf(record.getSYGH()) : null;
        nursingEvaluateRecord.SYXM = record.getSYXM() != null ? String.valueOf(record.getSYXM()) : null;
        nursingEvaluateRecord.SYSJ = record.getSYSJ() != null ? parseToData_DateStr(record.getSYSJ()) : null;
        nursingEvaluateRecord.DYCS = record.getDYCS() != null ? String.valueOf(record.getDYCS()) : null;
        nursingEvaluateRecord.ZFBZ = record.getZFBZ() != null ? String.valueOf(record.getZFBZ()) : null;
        nursingEvaluateRecord.JGID = record.getJGID() != null ? String.valueOf(record.getJGID()) : null;
        nursingEvaluateRecord.PGNR = record.getPGNR() != null ? String.valueOf(record.getPGNR()) : null;
        nursingEvaluateRecord.Status = record.getStatus() != null ? String.valueOf(record.getStatus()) : null;
        List<EvalutionRecordDetail> detailList = record.getDetails();
        List<NursingEvaluateRecordDetail> nursingEvaluateRecordDetailList = new ArrayList<>();
        for (EvalutionRecordDetail evalutionRecordDetail : detailList) {
            NursingEvaluateRecordDetail nursingEvaluateRecordDetail = parseToData_NursingEvaluateRecordDetail(evalutionRecordDetail);
            nursingEvaluateRecordDetailList.add(nursingEvaluateRecordDetail);
        }
        nursingEvaluateRecord.detailList = nursingEvaluateRecordDetailList;
        return nursingEvaluateRecord;
    }

    private NursingEvaluateRecordDetail parseToData_NursingEvaluateRecordDetail(EvalutionRecordDetail evalutionRecordDetail) {
        NursingEvaluateRecordDetail nursingEvaluateRecordDetail = new NursingEvaluateRecordDetail();
        nursingEvaluateRecordDetail.MXXH = evalutionRecordDetail.getMXXH() != null ? String.valueOf(evalutionRecordDetail.getMXXH()) : null;
        nursingEvaluateRecordDetail.JLXH = evalutionRecordDetail.getJLXH() != null ? String.valueOf(evalutionRecordDetail.getJLXH()) : null;
        nursingEvaluateRecordDetail.XMXH = evalutionRecordDetail.getXMXH() != null ? String.valueOf(evalutionRecordDetail.getXMXH()) : null;
        nursingEvaluateRecordDetail.SJXM = evalutionRecordDetail.getSJXM() != null ? String.valueOf(evalutionRecordDetail.getSJXM()) : null;
        nursingEvaluateRecordDetail.SJXMMC = evalutionRecordDetail.getSJXMMC() != null ? String.valueOf(evalutionRecordDetail.getSJXMMC()) : null;
        nursingEvaluateRecordDetail.XMNR = evalutionRecordDetail.getXMNR() != null ? String.valueOf(evalutionRecordDetail.getXMNR()) : null;
        nursingEvaluateRecordDetail.DZLX = evalutionRecordDetail.getDZLX() != null ? String.valueOf(evalutionRecordDetail.getDZLX()) : null;
        nursingEvaluateRecordDetail.DZBDJL = evalutionRecordDetail.getDZBDJL() != null ? String.valueOf(evalutionRecordDetail.getDZBDJL()) : null;
        nursingEvaluateRecordDetail.KJLX_Web = evalutionRecordDetail.getKJLX() != null ? String.valueOf(evalutionRecordDetail.getKJLX()) : null;
        nursingEvaluateRecordDetail.Status = evalutionRecordDetail.getStatus() != null ? String.valueOf(evalutionRecordDetail.getStatus()) : null;
        return nursingEvaluateRecordDetail;
    }


    private List<NursingEvaluateStyte> parseToData_NursingEvaluateStyte(List<EvalutionStyle> evalutionStyleList) {
        List<NursingEvaluateStyte> nursingEvaluateStyteList = new ArrayList<>();
        for (EvalutionStyle evalutionStyle : evalutionStyleList) {
            NursingEvaluateStyte nursingEvaluateStyte = new NursingEvaluateStyte();
            nursingEvaluateStyte.YSXH = evalutionStyle.getYSXH() != null ? String.valueOf(evalutionStyle.getYSXH()) : null;
            nursingEvaluateStyte.BBH = evalutionStyle.getBBH() != null ? String.valueOf(evalutionStyle.getBBH()) : null;
            nursingEvaluateStyte.YSLX = evalutionStyle.getYSLX() != null ? String.valueOf(evalutionStyle.getYSLX()) : null;
            nursingEvaluateStyte.YSMC = evalutionStyle.getYSMC() != null ? String.valueOf(evalutionStyle.getYSMC()) : null;
            nursingEvaluateStyte.PYDM = evalutionStyle.getPYDM() != null ? String.valueOf(evalutionStyle.getPYDM()) : null;
            nursingEvaluateStyte.WBDM = evalutionStyle.getWBDM() != null ? String.valueOf(evalutionStyle.getWBDM()) : null;
            nursingEvaluateStyte.PLSX = evalutionStyle.getPLSX() != null ? String.valueOf(evalutionStyle.getPLSX()) : null;
            nursingEvaluateStyte.ZTBZ = evalutionStyle.getZTBZ() != null ? String.valueOf(evalutionStyle.getZTBZ()) : null;
            nursingEvaluateStyte.QYBQ = evalutionStyle.getQYBQ() != null ? String.valueOf(evalutionStyle.getQYBQ()) : null;
            nursingEvaluateStyte.DYWD = evalutionStyle.getDYWD() != null ? String.valueOf(evalutionStyle.getDYWD()) : null;
            nursingEvaluateStyte.XML = evalutionStyle.getXML() != null ? String.valueOf(evalutionStyle.getXML()) : null;
            nursingEvaluateStyte.JGID = evalutionStyle.getJGID() != null ? String.valueOf(evalutionStyle.getJGID()) : null;
            nursingEvaluateStyte.SJHQFS = evalutionStyle.getSJHQFS() != null ? String.valueOf(evalutionStyle.getSJHQFS()) : null;

            //
            List<NursingEvaluateItem> nursingEvaluateItemList = new ArrayList<>();
            //顶级控件类型"0"
            containerParseNursingEvaluateItem("0", "", nursingEvaluateItemList, evalutionStyle.getProjects());
            nursingEvaluateStyte.itemList = nursingEvaluateItemList;
            nursingEvaluateStyteList.add(nursingEvaluateStyte);
        }
        return nursingEvaluateStyteList;
    }

    private void containerParseNursingEvaluateItem(String KJLX, String SJXMMC, List<NursingEvaluateItem> nursingEvaluateItemList_container,
                                                   List<EvalutionStyleProject> evalutionStyleProjectList) {
        if (evalutionStyleProjectList == null || evalutionStyleProjectList.isEmpty()) {
            return;
        }
        for (EvalutionStyleProject evalutionStyleProject : evalutionStyleProjectList) {
            NursingEvaluateItem nursingEvaluateItem = new NursingEvaluateItem();
            nursingEvaluateItem.XMXH = evalutionStyleProject.getYSXH() != null ? String.valueOf(evalutionStyleProject.getXMXH()) : null;
            nursingEvaluateItem.BBH = evalutionStyleProject.getBBH() != null ? String.valueOf(evalutionStyleProject.getBBH()) : null;
            nursingEvaluateItem.YSXH = evalutionStyleProject.getYSXH() != null ? String.valueOf(evalutionStyleProject.getYSXH()) : null;
            nursingEvaluateItem.PLSX = evalutionStyleProject.getPLSX() != null ? String.valueOf(evalutionStyleProject.getPLSX()) : null;
            nursingEvaluateItem.GLXM = evalutionStyleProject.getGLXM() != null ? String.valueOf(evalutionStyleProject.getGLXM()) : null;
            nursingEvaluateItem.XMMC = evalutionStyleProject.getXMMC() != null ? String.valueOf(evalutionStyleProject.getXMMC()) : null;
            nursingEvaluateItem.PYDM = evalutionStyleProject.getPYDM() != null ? String.valueOf(evalutionStyleProject.getPYDM()) : null;
            nursingEvaluateItem.WBDM = evalutionStyleProject.getWBDM() != null ? String.valueOf(evalutionStyleProject.getWBDM()) : null;
            nursingEvaluateItem.QZWB = evalutionStyleProject.getQZWB() != null ? String.valueOf(evalutionStyleProject.getQZWB()) : null;
            nursingEvaluateItem.HZWB = evalutionStyleProject.getHZWB() != null ? String.valueOf(evalutionStyleProject.getHZWB()) : null;
            nursingEvaluateItem.XSCD = evalutionStyleProject.getXSCD() != null ? String.valueOf(evalutionStyleProject.getXSCD()) : null;
            nursingEvaluateItem.XMLB = evalutionStyleProject.getXMLB() != null ? String.valueOf(evalutionStyleProject.getXMLB()) : null;
            nursingEvaluateItem.XJKJLX = evalutionStyleProject.getXJKJLX() != null ? String.valueOf(evalutionStyleProject.getXJKJLX()) : null;
            nursingEvaluateItem.SJLX = evalutionStyleProject.getSJLX() != null ? String.valueOf(evalutionStyleProject.getSJLX()) : null;
            nursingEvaluateItem.XMKZ = evalutionStyleProject.getXMKZ() != null ? String.valueOf(evalutionStyleProject.getXMKZ()) : null;
            nursingEvaluateItem.SJGS = evalutionStyleProject.getSJGS() != null ? String.valueOf(evalutionStyleProject.getSJGS()) : null;
            nursingEvaluateItem.SJXX = evalutionStyleProject.getSJXX() != null ? String.valueOf(evalutionStyleProject.getSJXX()) : null;
            nursingEvaluateItem.SJSX = evalutionStyleProject.getSJSX() != null ? String.valueOf(evalutionStyleProject.getSJSX()) : null;
            nursingEvaluateItem.XMZH = evalutionStyleProject.getXMZH() != null ? String.valueOf(evalutionStyleProject.getXMZH()) : null;
            nursingEvaluateItem.HHBZ = evalutionStyleProject.getHHBZ() != null ? String.valueOf(evalutionStyleProject.getHHBZ()) : null;
            nursingEvaluateItem.SJXM = evalutionStyleProject.getSJXM() != null ? String.valueOf(evalutionStyleProject.getSJXM()) : null;
            nursingEvaluateItem.XMJB = evalutionStyleProject.getXMJB() != null ? String.valueOf(evalutionStyleProject.getXMJB()) : null;
            nursingEvaluateItem.XJZK = evalutionStyleProject.getXJZK() != null ? String.valueOf(evalutionStyleProject.getXJZK()) : null;
            nursingEvaluateItem.XJHC = evalutionStyleProject.getXJHC() != null ? String.valueOf(evalutionStyleProject.getXJHC()) : null;
            nursingEvaluateItem.XMBM = evalutionStyleProject.getXMBM() != null ? String.valueOf(evalutionStyleProject.getXMBM()) : null;
            nursingEvaluateItem.PDAXS = evalutionStyleProject.getPDAXS() != null ? String.valueOf(evalutionStyleProject.getPDAXS()) : null;
            nursingEvaluateItem.XGBZ = evalutionStyleProject.getXGBZ() != null ? String.valueOf(evalutionStyleProject.getXGBZ()) : null;
            nursingEvaluateItem.ZDXM = evalutionStyleProject.getZDXM() != null ? String.valueOf(evalutionStyleProject.getZDXM()) : null;
            nursingEvaluateItem.BTXM = evalutionStyleProject.getBTXM() != null ? String.valueOf(evalutionStyleProject.getBTXM()) : null;
            nursingEvaluateItem.ZXBZ = evalutionStyleProject.getZXBZ() != null ? String.valueOf(evalutionStyleProject.getZXBZ()) : null;
            //# nursingEvaluateItem.XMQZ = evalutionStyleProject.getXMQZ() != null ? String.valueOf(evalutionStyleProject.getXMQZ()) : null;
            nursingEvaluateItem.KJLX = KJLX;
            nursingEvaluateItem.SJXMMC = SJXMMC;
            nursingEvaluateItem.XMXXKeyValueList = parseXMXXFromXMKZ(nursingEvaluateItem.XMKZ);
            //迭代
            nursingEvaluateItem.childItems = new ArrayList<>();
            containerParseNursingEvaluateItem(nursingEvaluateItem.XJKJLX, nursingEvaluateItem.XMMC, nursingEvaluateItem.childItems, evalutionStyleProject.getChildren());
            //
            nursingEvaluateItemList_container.add(nursingEvaluateItem);

        }
    }


    @RequestMapping(value = "mobile/evaluation/v56_update1/get/recordList")
    public @ResponseBody
    Response<List<EvalutionRecord>> getEvaluationRecordList(@RequestParam(value = "zyh") String zyh,
                                                            @RequestParam(value = "brbq") String brbq,
                                                            @RequestParam(value = "jgid") String jgid) {
        Response<List<EvalutionRecord>> response = new Response<>();
        BizResponse<List<EvalutionRecord>> bizResponse;
        bizResponse = service.getRecordList(Long.valueOf(zyh), Integer.valueOf(brbq), Integer.valueOf(jgid));
        response.Data = bizResponse.data;
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    @RequestMapping(value = "mobile/evaluation/v56_update1/get/styleList")
    public @ResponseBody
    Response<List<EvalutionStyle>> getEvalutionStyleList(@RequestParam(value = "jgid") String jgid) {
        Response<List<EvalutionStyle>> response = new Response<>();
        BizResponse<List<EvalutionStyle>> bizResponse;
        bizResponse = service.getStyleList(Integer.valueOf(jgid));
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
            response.Data = bizResponse.data;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    public String getYSMCByYSXH(String ysxh, String jgid) {
        String YSMC = null;
        Response<List<EvalutionStyle>> response = getEvalutionStyleListByYSXH(ysxh, jgid);
        if (response.Data != null && response.Data.size() > 0) {
            YSMC = response.Data.get(0).getYSMC();
        }
        return YSMC;
    }

    public Response<List<EvalutionStyle>> getEvalutionStyleListByYSXH(String ysxh, String jgid) {
        Response<List<EvalutionStyle>> response = new Response<>();
        BizResponse<List<EvalutionStyle>> bizResponse;
        bizResponse = service.selectStyleByPrimaryKey(Long.valueOf(ysxh), Integer.valueOf(jgid));
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
            response.Data = bizResponse.data;

        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }


    /**
     * 提交评估数据
     *
     * @param record
     * @return
     */
    @RequestMapping(value = "mobile/evaluation/v56_update1/commit/record")
    public @ResponseBody
    Response<EvalutionRecord> commitEvalutionData(@RequestBody EvalutionRecord record) {
        Response<EvalutionRecord> response = new Response<>();
        BizResponse<EvalutionRecord> bizResponse = new BizResponse<>();

        if (record.getJLXH().equals(0L)) {
            bizResponse = service.addEvalutionRecord(record);
        } else {
            bizResponse = service.updateEvalutionRecord(record);
        }

        if (bizResponse.isSuccess) {
            response.Data = bizResponse.data;
            response.Msg = bizResponse.message;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }

    private List<KeyValue<String, String>> parseXMXXFromXMKZ(String XMKZ) {
        if (TextUtils.isEmpty(XMKZ)) {
            return null;
        }
        List<KeyValue<String, String>> keyValueList = new ArrayList<>();
        // 下拉选择数据来源：
        // 1:以固定格式保存在XMKZ中 @名称1，代码1；名称2，代码2
        // 2:通过配置SQL语句获取 SQL格式：$SQLMOBHIS:SELECT XMMC,XMDM FROM XXX
        // 确定本地加载数据还是远程加载
        // 切分数据
        if (XMKZ.startsWith("@")) {
            XMKZ = XMKZ.replace("@", "");
            String[] XMKZArr = XMKZ.split(";");
            if (XMKZArr.length > 0) {
                for (String XMKZTemp : XMKZArr) {
                    String[] XMKZTempArr = XMKZTemp.split(",");
                    if (XMKZTempArr.length == 2) {
                        String XMMC = XMKZTempArr[0];
                        String XMDM = XMKZTempArr[1];
                        keyValueList.add(KeyValue.create(XMDM, XMMC));
                    }
                }
            }
        } else if (XMKZ.startsWith("$")) {
            XMKZ = XMKZ.replace("$", "");
            String[] XMKZArr = XMKZ.split(":");
            if (XMKZArr.length == 2) {
                String dataSource = XMKZArr[0];
                String sql = XMKZArr[1];
                List<ComboUi> datas = getComboboxDatas(dataSource, sql);
                for (ComboUi data : datas) {
                    keyValueList.add(KeyValue.create(data.getXMDM(), data.getXMMC()));
                }

            }

        }

        return keyValueList;
    }

    /**
     * 获取基本信息
     *
     * @param zyh
     * @return
     */
    @RequestMapping(value = "mobile/evaluation/v56_update1/get/baseinfo")
    public @ResponseBody
    Response<List<Map2>> getPatientInfo(String zyh) {
        Response<List<Map2>> response = new Response<>();
        BizResponse<Map2> bizResponse = new BizResponse<>();

        bizResponse = service.getPatientInfo(zyh);
        response.Data = bizResponse.datalist;
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
     * 获取评估项目关联数据
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "auth/mobile/evaluation/post/GetRelationData_V56Update1")
    public @ResponseBody
    Response<NursingEvaluateRecord> GetRelationData_V56Update1(@RequestBody List<RelationDataParam> params) {

        Response<NursingEvaluateRecord> response = new Response<>();
        NursingEvaluateRecord nursingEvaluateRecord = new NursingEvaluateRecord();
        nursingEvaluateRecord.detailList = new ArrayList<>();
        Response<List<EvalutionRecordDetail>> responseTemp = getRelationDataParam(params);
        if (responseTemp.ReType == 0) {
            List<EvalutionRecordDetail> ls = responseTemp.Data;
            for (EvalutionRecordDetail detail : ls) {
                NursingEvaluateRecordDetail detailNew = parseToData_NursingEvaluateRecordDetail(detail);
                nursingEvaluateRecord.detailList.add(detailNew);
            }
        }
        response.Msg = responseTemp.Msg;
        response.ReType = responseTemp.ReType;
        response.Data = nursingEvaluateRecord;
        return response;
    }

    /**
     * 获取评估项目关联数据
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "mobile/evaluation/v56_update1/get/relationdata")
    public @ResponseBody
    Response<List<EvalutionRecordDetail>> getRelationDataParam(@RequestBody List<RelationDataParam> params) {
        Response<List<EvalutionRecordDetail>> response = new Response<>();
        BizResponse<EvalutionRecordDetail> bizResponse = new BizResponse<>();

        bizResponse = service.getRelationData(params);
        response.Data = bizResponse.datalist;
        if (bizResponse.isSuccess) {
            response.Msg = bizResponse.message;
            response.ReType = 0;
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        return response;
    }
}
