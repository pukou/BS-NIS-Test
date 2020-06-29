package com.bsoft.nis.controller.traditional;

import com.bsoft.nis.domain.adviceexecute.PlanArgInfo;
import com.bsoft.nis.domain.adviceexecute.RequestBodyInfo;
import com.bsoft.nis.domain.adviceexecute.ResponseBody.ResponseBodyInfo;
import com.bsoft.nis.domain.traditional.JSJL;
import com.bsoft.nis.domain.traditional.SHFF_HLJS;
import com.bsoft.nis.domain.traditional.Traditional_HLFA;
import com.bsoft.nis.domain.traditional.Traditional_SHJS;
import com.bsoft.nis.domain.traditional.Traditional_ZZFJ;
import com.bsoft.nis.domain.traditional.Traditional_ZZJL;
import com.bsoft.nis.domain.traditional.ZZJL_PF;
import com.bsoft.nis.domain.traditional._HLJS;
import com.bsoft.nis.domain.traditional._SHFF;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.adviceexecute.AdviceExecuteMainService;
import com.bsoft.nis.service.traditional.TraditionalNursingBaseService;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Classichu on 2017/11/16.
 * 中医护理
 */
@Controller
public class TraditionalNursingBaseController {

    @Autowired
    TraditionalNursingBaseService service;
    @Autowired
    AdviceExecuteMainService adviceExecuteMainService;

    /**
     * 获取
     *
     * @param zyh  住院号
     * @param brbq 病人病区
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/traditional/get/getZYZZJL")
    //@RequestMapping(value = {"auth/mobile/traditional/get/getZYZZJL","mobile/traditional/get/getZYZZJL"})
    public
    @ResponseBody
    Response<List<Traditional_ZZJL>> getZYZZJL(@RequestParam(value = "zyh") String zyh,
                                               @RequestParam(value = "brbq") String brbq,
                                               @RequestParam(value = "jgid") String jgid) {
        Response<List<Traditional_ZZJL>> response = new Response<>();
        BizResponse<Traditional_ZZJL> bizResponse;

        bizResponse = service.getZYZZJL(zyh, brbq, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;

    }

    @RequestMapping(value = "auth/mobile/traditional/get/getZYZZFJ")
    public
    @ResponseBody
    Response<List<Traditional_ZZFJ>> getZYZZFJ(@RequestParam(value = "zzbh") String zzbh) {
        Response<List<Traditional_ZZFJ>> response = new Response<>();
        BizResponse<Traditional_ZZFJ> bizResponse;

        bizResponse = service.getZYZZFJ(zzbh);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;

    }

    @RequestMapping(value = "auth/mobile/traditional/get/getZYSHJSJL")
    public
    @ResponseBody
    Response<List<JSJL>> getZYSHJSJL(@RequestParam(value = "zyh") String zyh, @RequestParam(value = "jgid") String jgid) {
        Response<List<JSJL>> response = new Response<>();
        BizResponse<JSJL> bizResponse;

        bizResponse = service.getZYSHJSJL(zyh, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;

    }


    @RequestMapping(value = "auth/mobile/traditional/get/getZY_HLFA")
    public
    @ResponseBody
    Response<List<Traditional_HLFA>> getZY_HLFA(@RequestParam(value = "jgid") String jgid) {
        Response<List<Traditional_HLFA>> response = new Response<>();
        BizResponse<Traditional_HLFA> bizResponse;

        bizResponse = service.getZY_HLFA(jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;

    }


    @RequestMapping(value = "auth/mobile/traditional/get/getSHFF_HLJS")
    public
    @ResponseBody
    Response<SHFF_HLJS> getSHFF_HLJS(@RequestParam(value = "zyh") String zyh, @RequestParam(value = "brbq") String brbq, @RequestParam(value = "jgid") String jgid) {
        Response<SHFF_HLJS> response = new Response<>();
        SHFF_HLJS shff_hljs = new SHFF_HLJS();
        shff_hljs.hljsList = new ArrayList<>();
        shff_hljs.shffList = new ArrayList<>();
        //当前存在的主要症状记录
        BizResponse<Traditional_ZZJL> zzjlBizResponse = service.getZYZZJL(zyh, brbq, jgid);
        if (zzjlBizResponse.isSuccess) {
            List<Traditional_ZZJL> zzjlList = zzjlBizResponse.datalist;
            for (Traditional_ZZJL zzjl : zzjlList) {
                //
                //查询医嘱计划相关的数据 护理技术
                BizResponse<_HLJS> hljsBizResponse = service.getZY_YZJHbyZZBH(zyh, jgid, zzjl.ZZBH);
                if (hljsBizResponse.isSuccess) {
                    //
                    List<_HLJS> hljsList = hljsBizResponse.datalist;
                    for (_HLJS hljs : hljsList) {
                        hljs.code = zzjl.ZZBH;
                        hljs.name = zzjl.ZZMC;
                        hljs.FAJL = zzjl.FAJL;
                        hljs.ZZJL = zzjl.ZZJL;
                        shff_hljs.hljsList.add(hljs);
                    }
                }
                //
                _SHFF shff = new _SHFF();
                shff.code = zzjl.ZZBH;
                shff.name = zzjl.ZZMC;
                shff.ZZBH = zzjl.ZZBH;
                shff.ZZMC = zzjl.ZZMC;
                shff.FAJL = zzjl.FAJL;
                shff.ZZJL = zzjl.ZZJL;
                shff.shffCheckList = new ArrayList<>();
                //获取施护方法
                BizResponse<Traditional_SHJS> bizResponse1 = service.getZY_SHJS(zzjl.ZZBH);
                if (bizResponse1.isSuccess) {
                    for (Traditional_SHJS shjs : bizResponse1.datalist) {
                        _SHFF.SHFF_Check check = new _SHFF.SHFF_Check();
                        check.code = shjs.JSBH;
                        check.name = shjs.FFMC;
                        check.JSBH = shjs.JSBH;
                        check.FFMC = shjs.FFMC;
                        check.JCXMH = shjs.JCXMH;
                        check.BZXX = shjs.BZXX;
                        check.XMLB = shjs.XMLB;
                        check.XGBZ = shjs.XGBZ;
                        check.editable = !"0".equals(shjs.XGBZ) ? "1" : "0";
                        shff.shffCheckList.add(check);
                    }
                }
                //
                shff_hljs.shffList.add(shff);
            }
        }

        Collections.sort(shff_hljs.hljsList, new Comparator<_HLJS>() {
            @Override
            public int compare(_HLJS o1, _HLJS o2) {
                int leftJhh = Integer.valueOf(o1.JHH);
                int rightJhh = Integer.valueOf(o2.JHH);
                return leftJhh - rightJhh;
            }
        });
        //
        response.ReType = 0;
        response.Msg = zzjlBizResponse.message;
        response.Data = shff_hljs;

        return response;

    }

    @RequestMapping(value = "auth/mobile/traditional/post/saveZYPJInfo")
    public
    @ResponseBody
    Response<String> saveZYPJInfo(@RequestBody ZZJL_PF zzjl_pf) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.saveZYPJInfo(zzjl_pf);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }

    @RequestMapping(value = "auth/mobile/traditional/post/saveSHFF")
    public
    @ResponseBody
    Response<String> saveSHFF(@RequestBody List<JSJL> jsjlList_shff) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();
        /**插入施护技术记录表*/
        for (JSJL jsjl : jsjlList_shff) {
            bizResponse = service.saveZY_JSJL(jsjl);
        }

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }

    @RequestMapping(value = "auth/mobile/traditional/post/saveHLJS")
    public
    @ResponseBody
    Response<String> saveHLJS(@RequestBody List<JSJL> jsjlList_hljs) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        /**医嘱相关的操作*/
        List<PlanArgInfo> planArgInfoList = new ArrayList<>();
        String zyh = jsjlList_hljs.get(0).ZYH;
        String yhid = jsjlList_hljs.get(0).CZGH;
        String jgid = jsjlList_hljs.get(0).JGID;
        for (JSJL jsjl : jsjlList_hljs) {
            if (!isJHH_HasAdded(planArgInfoList, jsjl.JHH)) {
                PlanArgInfo planArgInfo = new PlanArgInfo();
                planArgInfo.JHH = jsjl.JHH;
                // planArgInfo.GSLX = jsjl.GSLX;
                planArgInfoList.add(planArgInfo);
            }
        }
        //中医护理属于 护理治疗类  GSLX=1
        RequestBodyInfo info = new RequestBodyInfo();
        info.ZYH = zyh;
        info.YHID = yhid;
        info.SYBX = false;
        info.JYSJ = false;
        info.PlanArgInfoList = planArgInfoList;
        info.JGID = jgid;
        //调用医嘱计划执行
        BizResponse<ResponseBodyInfo> bizResponseYZ= adviceExecuteMainService.HandExecut(info);
        if (bizResponseYZ.isSuccess){

            /**插入施护技术记录表*/
            boolean isSuccess = true;
            for (JSJL jsjl : jsjlList_hljs) {
                bizResponse = service.saveZY_JSJL(jsjl);
                if (!bizResponse.isSuccess) {
                    isSuccess = false;
                }
            }
            //
            if (isSuccess) {
                response.ReType = 0;
                response.Msg = "插入施护技术记录表成功";
            } else {
                response.ReType = -1;
                response.Msg = "插入施护技术记录表失败:"+bizResponse.message;
            }
            response.Data = bizResponse.data;
            return response;
        }
        response.ReType = -1;
        response.Msg = "医嘱相关的操作失败";
        response.Data = "";
        return response;
    }

    private boolean isJHH_HasAdded(List<PlanArgInfo> planArgInfoList, String jhh) {
        if (TextUtils.isEmpty(jhh) || planArgInfoList == null || planArgInfoList.isEmpty()) {
            return false;
        }
        for (PlanArgInfo planArgInfo : planArgInfoList) {
            if (jhh.equals(planArgInfo.JHH)) {
                return true;
            }
        }
        return false;
    }
}
