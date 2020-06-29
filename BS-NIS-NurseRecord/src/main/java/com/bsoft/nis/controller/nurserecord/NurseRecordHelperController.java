package com.bsoft.nis.controller.nurserecord;

import com.bsoft.nis.domain.nurserecord.db.ContentCategory;
import com.bsoft.nis.domain.nurserecord.db.HelpLeaf;
import com.bsoft.nis.domain.nurserecord.db.HelpTree;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.nurserecord.NurseRecordHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Describtion:护理记录助手控制器
 * Created: dragon
 * Date： 2016/11/23.
 */
@Controller
public class NurseRecordHelperController {

    @Autowired
    NurseRecordHelperService service;

    /**
     * 获取护理记录助手目录
     * @param ysbh
     * @param xmbh
     * @param jgbh
     * @param ygdm
     * @param bqid
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/help/get/content")
    public @ResponseBody
    Response<List<HelpTree>> getHelpContent(@RequestParam(value = "ysbh") String ysbh,
                                                   @RequestParam(value = "xmbh") String xmbh,
                                                   @RequestParam(value = "jgbh") String jgbh,
                                                   @RequestParam(value = "ygdm") String ygdm,
                                                   @RequestParam(value = "bqid") String bqid,
                                                   @RequestParam(value = "jgid") String jgid){
        BizResponse<List<HelpTree>> bizResponse = new BizResponse<>();
        Response<List<HelpTree>> response = new Response<>();

        bizResponse = service.getHelpContent(ysbh,xmbh,jgbh,ygdm,bqid,jgid);
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
     * 获取护理记录助手内容
     * @param ysbh
     * @param xmbh
     * @param jgbh
     * @param ygdm
     * @param bqid
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurserecord/help/get/help")
    public @ResponseBody
    Response<List<HelpLeaf>> getHelp(@RequestParam(value = "ysbh") String ysbh,
                                            @RequestParam(value = "xmbh") String xmbh,
                                            @RequestParam(value = "jgbh") String jgbh,
                                            @RequestParam(value = "mlbh") String mlbh,
                                            @RequestParam(value = "ygdm") String ygdm,
                                            @RequestParam(value = "bqid") String bqid,
                                            @RequestParam(value = "jgid") String jgid){
        BizResponse<List<HelpLeaf>> bizResponse = new BizResponse<>();
        Response<List<HelpLeaf>> response = new Response<>();

        bizResponse = service.getHelperApi(ysbh, xmbh, jgbh,mlbh, ygdm, bqid, jgid);
        if (bizResponse.isSuccess){
            response.ReType = 0;
        }else{
            response.ReType = -1;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }
}
