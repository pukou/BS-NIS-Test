package com.bsoft.nis.controller.nurserecord;

import com.bsoft.nis.domain.nurserecord.*;
import com.bsoft.nis.domain.nurserecord.db.DrugMedicalAdviceRefContent;
import com.bsoft.nis.domain.nurserecord.db.RefContent;
import com.bsoft.nis.domain.nurserecord.db.RefContentClassification;
import com.bsoft.nis.domain.nurserecord.db.SignRefContent;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.nurserecord.NurseRecordWriteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:护理记录书写
 * Created: dragon
 * Date： 2016/10/20.
 */
@Controller
public class NurseRecordWriteController {

    @Autowired
    NurseRecordWriteService service;  // 护理记录书写服务

    /**
     * 根据记录编号获取护理记录控件列表
     * @param zyh
     * @param jlbh
     * @param jgid
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/controlls/jlbh")
    public @ResponseBody
    Response<List<StuctrueResponse>> getCtrlListByJlbh(@RequestParam(value = "zyh") String zyh,
                                                       @RequestParam(value = "jlbh") String jlbh,
                                                       @RequestParam(value = "jgid") String jgid,
                                                       @RequestParam(value = "sysType") int sysType){
        Response<List<StuctrueResponse>> response = new Response<>();
        BizResponse<List<StuctrueResponse>> bizResponse = new BizResponse<>();

        bizResponse = service.getCtrlListByJlbh(zyh,jlbh,jgid,sysType);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 根据结构编号获取护理记录控件列表
     * @param zyh
     * @param jgbh
     * @param jgid
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/controlls/jgbh")
    public @ResponseBody
    Response<List<StuctrueResponse>> getCtrlListByJgbh(@RequestParam(value = "zyh") String zyh,
                                                       @RequestParam(value = "jgbh") String jgbh,
                                                       @RequestParam(value = "jgid") String jgid,
                                                       @RequestParam(value = "sysType",required = false) int sysType){
        Response<List<StuctrueResponse>> response = new Response<>();
        BizResponse<List<StuctrueResponse>> bizResponse = new BizResponse<>();

        bizResponse = service.getCtrlListByJgbh(jgbh, zyh, jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 获取护理记录列表树（供查询历史记录用）
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/record/content")
    public @ResponseBody
    Response<List<NRTree>> getNRTree(@RequestParam(value = "zyh") String zyh,
                                     @RequestParam(value = "jgid") String jgid){
        Response<List<NRTree>> response = new Response<>();
        BizResponse<List<NRTree>> bizResponse = new BizResponse<>();
        bizResponse = service.getNRTreeByMblx(zyh, null, jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }
    @RequestMapping(value = "auth/mobile/nurserecord/get/getlastXMData")
    public @ResponseBody
    Response<List<LastDataBean>> getlastXMData(@RequestParam(value = "zyh") String zyh,
                                               @RequestParam(value = "xmbh") String xmbh,
                                               @RequestParam(value = "hsgh") String hsgh,
                                               @RequestParam(value = "jgid") String jgid,
                                               @RequestParam(value = "sysType") int sysType){
        Response<List<LastDataBean>> response = new Response<>();
        BizResponse<LastDataBean> bizResponse = new BizResponse<>();
//        zyh=null;//协和 不限定病人
        hsgh=null;//协和 取病人该项目最近3次的记录
        bizResponse = service.getlastXMData(zyh,xmbh,hsgh,jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        if (bizResponse.datalist!=null&&bizResponse.datalist.size()>0){
            List<LastDataBean> datalistNew=new ArrayList<>();
            int count=0;
            for (LastDataBean lastDataBean : bizResponse.datalist) {
                if (!StringUtils.isBlank(lastDataBean.XMQZ)){
                    if (count>=3){//最多取3条
                        break;
                    }
                    count++;
                    datalistNew.add(lastDataBean);
                }
            }
            response.Data = datalistNew;
            response.Msg = bizResponse.message;
        }else {
            response.Msg = "项目无历史数据";
        }

        return response;
    }

    /**
     * 获取护理记录列表树(供查询历史记录用)
     * @param zyh
     * @param mblx
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/record/content/mblx")
    public @ResponseBody
    Response<List<NRTree>> getNRTreeByMblx(String zyh,String mblx,String jgid){
        Response<List<NRTree>> response = new Response<>();
        BizResponse<List<NRTree>> bizResponse = new BizResponse<>();

        bizResponse = service.getNRTreeByMblx(zyh,mblx,jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 护理记录保存(用户ID由前端传入)
     * @param record
     * @return
     */
    @RequestMapping(value = {"auth/mobile/nurserecord/save/record", "mobile/nurserecord/save/record"})
    public @ResponseBody
    Response<String> saveNurseRecord(@RequestBody NRRecordRequest record){
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.saveNurseRecord(record);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 护理记录更新
     * @param record
     * @return
     */
    @RequestMapping(value = {"auth/mobile/nurserecord/update/record", "mobile/nurserecord/update/record"},method = RequestMethod.POST)
    public @ResponseBody
    Response<String> updateNurseRecord(@RequestBody NRRecordRequest record){
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.updateNurseRecord(record);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 删除护理记录数据
     * @param zyh
     * @param jlbh
     * @param yhid
     * @param yhxm
     * @param ip
     * @param hostname
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/delete/record")
    public @ResponseBody
    Response<String> deleteNurseRecord(@RequestParam(value = "zyh") String zyh,
                                       @RequestParam(value = "jlbh") String jlbh,
                                       @RequestParam(value = "yhid") String yhid,
                                       @RequestParam(value = "yhxm") String yhxm,
                                       @RequestParam(value = "ip",required = false) String ip,
                                       @RequestParam(value = "hostname",required = false) String hostname,
                                       @RequestParam(value = "jgid") String jgid){
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.deleteNurseRecord(zyh, jlbh, yhid, yhxm, ip, hostname, jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 护理记录签名
     * @param data
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/signname/record")
    public @ResponseBody
    Response<String> signNameNurseRecord(@RequestBody SignatureDataRequest data){
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.signNameNurseRecord(data);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 获取病人过敏药物，用于引用
     * @param zyh
     * @param startime
     * @param endtime
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/ref/drug/medical/advices")
    public @ResponseBody
    Response<List<DrugMedicalAdviceRefContent>> getGrugMedicalAdvices(@RequestParam(value = "zyh") String zyh,
                                                                      @RequestParam(value = "startime") String startime,
                                                                      @RequestParam(value = "endtime") String endtime,
                                                                      @RequestParam(value = "jgid") String jgid){
        Response<List<DrugMedicalAdviceRefContent>> response = new Response<>();
        BizResponse<List<DrugMedicalAdviceRefContent>> bizResponse = new BizResponse<>();

        bizResponse = service.getGrugMedicalAdvices(zyh, startime, endtime, jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 获取病人手术，用于引用
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/ref/operations")
    public @ResponseBody
    Response<List<RefContent>> getOperationRefs(@RequestParam(value = "zyh") String zyh,
                                                @RequestParam(value = "jgid") String jgid){
        Response<List<RefContent>> response = new Response<>();
        BizResponse<List<RefContent>> bizResponse = new BizResponse<>();

        bizResponse = service.getOperationRefs(zyh, jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 获取病人体征，用于引用
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/ref/lifesigns")
    public @ResponseBody
    Response<List<SignRefContent>> getLifeSignRefs(@RequestParam(value = "zyh") String zyh,
                                               @RequestParam(value = "starttime") String startime,
                                               @RequestParam(value = "endtime") String endtime,
                                               @RequestParam(value = "jgid") String jgid){
        Response<List<SignRefContent>> response = new Response<>();
        BizResponse<List<SignRefContent>> bizResponse = new BizResponse<>();

        bizResponse = service.getLifeSignRefs(zyh, startime, endtime, jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 获取特殊符号，用于引用
     * @param dmlb
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/ref/others")
    public @ResponseBody
    Response<List<RefContentClassification>> getOtherRefs(@RequestParam(value = "dmlb") String dmlb){
        Response<List<RefContentClassification>> response = new Response<>();
        BizResponse<List<RefContentClassification>> bizResponse = new BizResponse<>();

        bizResponse = service.getOtherRefs(dmlb);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 获取生命体征/风险评估分页数据------引用体征数据
     * @param zyh
     * @param yslx
     * @param yskz
     * @param pageIndex
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/association")
    public @ResponseBody
    Response<Association> getAssociation(String zyh,String yslx,String yskz,int pageIndex,String jgid){
        Response<Association> response = new Response<>();
        BizResponse<Association> bizResponse = new BizResponse<>();

        response.Data = service.getAssociation(zyh,yslx,yskz,pageIndex,jgid);
        response.ReType = 0;
        response.Msg = bizResponse.message;
        return response;
    }


}
