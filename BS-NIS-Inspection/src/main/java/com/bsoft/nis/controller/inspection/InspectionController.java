package com.bsoft.nis.controller.inspection;

import com.bsoft.nis.domain.inspection.*;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.inspection.InspectionMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 检验检查查询控制器
 * Created by Administrator on 2016/10/10.
 */
@Controller
public class InspectionController {

    @Autowired
    InspectionMainService service;

    /**
     * 获取检验结果列表
     *
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/GetInspectionList")
    public
    @ResponseBody
    Response<List<InspectionVo>> GetInspectionList(@RequestParam(value = "zyh") String zyh,
                                                   @RequestParam(value = "jgid") String jgid) {
        Response<List<InspectionVo>> response = new Response<>();
        BizResponse<InspectionVo> bizResponse = new BizResponse<InspectionVo>();
        bizResponse = service.GetInspectionList(zyh, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }
    /*
              升级编号【56010025】============================================= start
              检验检查：检验List项目数据趋势图，项目分类查看
              ================= Classichu 2017/10/18 9:34
              */
    @RequestMapping(value = "auth/mobile/inspection/GetInspectionXMBeanList")
    public
    @ResponseBody
    Response<List<InspectionXMBean>> GetInspectionXMBeanList(@RequestParam(value = "xmid") String xmid, @RequestParam(value = "zyh") String zyh,
                                                             @RequestParam(value = "jgid") String jgid) {
        Response<List<InspectionXMBean>> response = new Response<>();
        BizResponse<InspectionXMBean> bizResponse = new BizResponse<>();
        bizResponse = service.GetInspectionXMBeanList(xmid,zyh, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }
       /* =============================================================== end */

    /**
     * 获取检验详细信息
     *
     * @param inspectionNumber
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/GetInspectionDetail")
    public
    @ResponseBody
    Response<List<InspectionDetailVo>> GetInspectionDetail(@RequestParam(value = "inspectionNumber") String inspectionNumber,
                                                           @RequestParam(value = "jgid") String jgid) {
        Response<List<InspectionDetailVo>> response = new Response<>();
        BizResponse<InspectionDetailVo> bizResponse = new BizResponse<InspectionDetailVo>();
        bizResponse = service.GetInspectionDetail(inspectionNumber, jgid);
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
     * 获取检查结果列表
     *
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/GetExamineResultList")
    public
    @ResponseBody
    Response<List<ExamineVo>> GetExamineResultList(@RequestParam(value = "zyh") String zyh,
                                                   @RequestParam(value = "jgid") String jgid) {
        Response<List<ExamineVo>> response = new Response<>();
        BizResponse<ExamineVo> bizResponse = new BizResponse<ExamineVo>();
        bizResponse = service.GetExamineResultList(zyh, jgid);
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
     * 获取检查明细
     * 当type  1：ris，0：uis
     *
     * @param examineNumber
     * @param type
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/inspection/GetExamineResultDetail")
    public
    @ResponseBody
    Response<List<ExamineDetailVo>> GetExamineResultDetail(@RequestParam(value = "examineNumber") String examineNumber,
                                                           @RequestParam(value = "type") String type,
                                                           @RequestParam(value = "jgid") String jgid) {
        Response<List<ExamineDetailVo>> response = new Response<>();
        BizResponse<ExamineDetailVo> bizResponse = new BizResponse<ExamineDetailVo>();
        bizResponse = service.GetExamineResultDetail(examineNumber, type, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }
}
