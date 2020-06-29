package com.bsoft.nis.controller.nurserecord;

import com.bsoft.nis.domain.nurserecord.db.Structure;
import com.bsoft.nis.domain.nurserecord.db.Template;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.nurserecord.NurseRecordConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Describtion:护理记录基础配置控制器
 * Created: dragon
 * Date： 2016/10/20.
 */
@Controller
public class NurseRecordConfigController {

    @Autowired
    NurseRecordConfigService service;

    /**
     * 获取护理记录类别列表
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/lblb")
    public @ResponseBody
    Response<List<Structure>> getNurseRecordStructureList(@RequestParam(value = "bqid") String bqid,
                                                          @RequestParam(value = "jgid") String jgid,
                                                          @RequestParam(value = "sysType",required = false) int sysType){
        Response<List<Structure>> response = new Response<>();
        BizResponse<Structure> bizResponse = new BizResponse<>();

        bizResponse = service.getNurseRecordStructureList(bqid,jgid,sysType);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.datalist;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 获取护理结构列表(enr_jg01)
     * @param bqid
     * @param lbbh
     * @param jgid
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/get/jglb")
    public @ResponseBody
    Response<List<Template>> getNurseRecordTemplateList(@RequestParam(value = "bqid",required = false) String bqid,
                                                        @RequestParam(value = "lbbh",required = false) String lbbh,
                                                        @RequestParam(value = "jgid") String jgid,
                                                        @RequestParam(value = "sysType",required = false) int sysType){
        Response<List<Template>> response = new Response<>();
        BizResponse<Template> bizResponse = new BizResponse<>();

        bizResponse = service.getNurseRecordTemplateList(bqid,lbbh,jgid,sysType);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.datalist;
        response.Msg = bizResponse.message;
        return response;
    }
}
