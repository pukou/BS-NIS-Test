package com.bsoft.nis.controller.healthguid;

import com.bsoft.nis.domain.healthguid.HealthGuid;
import com.bsoft.nis.domain.healthguid.HealthGuidData;
import com.bsoft.nis.domain.healthguid.HealthGuidEvaluateData;
import com.bsoft.nis.domain.healthguid.HealthGuidSaveData;
import com.bsoft.nis.service.healthguid.HealthGuidMainService;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: 健康教育控制器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Controller
public class HealthGuidController {

    @Autowired
    HealthGuidMainService service;

    @RequestMapping(value = "/healthguid")
    public String getMainPatientPage() {
        return "healthguid/healthguid";
    }

    /**
     * 获取病区健康宣教列表及其记录数量
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/healthguid/get/getHealthGuidList")
    public
    @ResponseBody
    Response<List<HealthGuid>> getHealthGuidList(@RequestParam(value = "zyh") String zyh,
                                                 @RequestParam(value = "bqid") String bqid,
                                                 @RequestParam(value = "jgid") String jgid) {
        Response<List<HealthGuid>> response = new Response<>();
        BizResponse<HealthGuid> bizResponse = new BizResponse<>();

        bizResponse = service.getHealthGuidList(zyh, bqid, jgid);
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
     * 获取具体的某一条宣教单/宣教归类
     *
     * @param lxbh     样式序号/归类序号
     * @param xh       IENR_JKXJJL主键
     * @param type     1：表单 2：分类
     * @param operType 操作类型 1：添加 2：修改 9：其他模块引用健康宣教模块
     * @param jgid     机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/healthguid/get/getHealthGuidData")
    public
    @ResponseBody
    Response<HealthGuidData> getHealthGuidData(@RequestParam(value = "lxbh") String lxbh,
                                               @RequestParam(value = "xh") String xh,
                                               @RequestParam(value = "type") String type,
                                               @RequestParam(value = "operType") String operType,
                                               @RequestParam(value = "jgid") String jgid) {
        Response<HealthGuidData> response = new Response<>();
        BizResponse<HealthGuidData> bizResponse = new BizResponse<>();

        bizResponse = service.getHealthGuidData(lxbh, xh, type, operType, jgid);
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
     * 保存数据 新增/修改
     *
     * @param healthGuidSaveData
     * @return
     */
    @RequestMapping(value = {"auth/mobile/healthguid/post/saveHealthGuidData", "mobile/healthguid/post/saveHealthGuidData"})
    public
    @ResponseBody
    Response<HealthGuidData> saveHealthGuidData(@RequestBody HealthGuidSaveData healthGuidSaveData) {
        Response<HealthGuidData> response = new Response<>();
        BizResponse<HealthGuidData> bizResponse = new BizResponse<>();

        bizResponse = service.saveHealthGuidData(healthGuidSaveData);
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
     * 删除数据
     *
     * @param jlxh 记录序号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/healthguid/get/deleteHealthGuidData")
    public
    @ResponseBody
    Response<String> deleteHealthGuidData(@RequestParam(value = "jlxh") String jlxh,
                                          @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.deleteHealthGuidData(jlxh, jgid);
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
     * 获取具体的某一条宣教单
     * <p>
     * 归类类型新增专用：通过归类类型先找到对应的样式序号然后进行新增
     *
     * @param lxbh 归类序号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/healthguid/get/getHealthGuidDataSpecial")
    public
    @ResponseBody
    Response<HealthGuidData> getHealthGuidDataSpecial(@RequestParam(value = "lxbh") String lxbh,
                                                      @RequestParam(value = "jgid") String jgid) {
        Response<HealthGuidData> response = new Response<>();
        BizResponse<HealthGuidData> bizResponse = new BizResponse<>();

        bizResponse = service.getHealthGuidDataSpecial(lxbh, jgid);
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
     * 获取备注信息
     *
     * @param xmxh 项目序号
     * @return
     */
    @RequestMapping(value = "auth/mobile/healthguid/get/getHealthGuidRemark")
    public
    @ResponseBody
    Response<String> getHealthGuidRemark(@RequestParam(value = "xmxh") String xmxh) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.getHealthGuidRemark(xmxh);
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
     * 签名
     *
     * @param jlxh 记录序号
     * @param qmgh 签名工号
     * @param glxh 关联序号
     * @param gllx 关联类型
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "mobile/healthguid/get/signatureHealthGuid")
    public
    @ResponseBody
    Response<HealthGuidData> signatureHealthGuid(@RequestParam(value = "jlxh") String jlxh,
                                                 @RequestParam(value = "qmgh") String qmgh,
                                                 @RequestParam(value = "glxh") String glxh,
                                                 @RequestParam(value = "gllx") String gllx,
                                                 @RequestParam(value = "jgid") String jgid) {
        Response<HealthGuidData> response = new Response<>();
        BizResponse<HealthGuidData> bizResponse = new BizResponse<>();

        bizResponse = service.signatureHealthGuid(jlxh, qmgh, glxh, gllx, jgid);
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
     * 取消签名
     *
     * @param jlxh 记录序号
     * @param glxh 关联序号
     * @param gllx 关联类型
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "mobile/healthguid/get/cancleSignatureHealthGuid")
    public
    @ResponseBody
    Response<HealthGuidData> cancleSignatureHealthGuid(@RequestParam(value = "jlxh") String jlxh,
                                                       @RequestParam(value = "glxh") String glxh,
                                                       @RequestParam(value = "gllx") String gllx,
                                                       @RequestParam(value = "jgid") String jgid) {
        Response<HealthGuidData> response = new Response<>();
        BizResponse<HealthGuidData> bizResponse = new BizResponse<>();

        bizResponse = service.cancleSignatureHealthGuid(jlxh, glxh, gllx, jgid);
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
     * 获取具体的某一条宣教单/宣教归类 独立评价项目
     *
     * @param lxbh 样式序号/归类序号
     * @param xh   IENR_JKXJJL主键
     * @param type 1：表单 2：分类
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/healthguid/get/getRealHealthGuidEvaluateData")
    public
    @ResponseBody
    Response<HealthGuidEvaluateData> getRealHealthGuidEvaluateData(@RequestParam(value = "lxbh") String lxbh,
                                                                   @RequestParam(value = "xh") String xh,
                                                                   @RequestParam(value = "type") String type,
                                                                   @RequestParam(value = "jgid") String jgid) {
        Response<HealthGuidEvaluateData> response = new Response<>();
        BizResponse<HealthGuidEvaluateData> bizResponse = new BizResponse<>();

        bizResponse = service.getRealHealthGuidEvaluateData(lxbh, xh, type, jgid);
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
     * 保存数据 评价/取消评价 独立评价用
     *
     * @param healthGuidEvaluateData 数据对象
     * @return
     */
    @RequestMapping(value = "auth/mobile/healthguid/post/saveHealthGuidEvaluateData")
    public
    @ResponseBody
    Response<String> saveHealthGuidEvaluateData(@RequestBody HealthGuidEvaluateData healthGuidEvaluateData) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.saveHealthGuidEvaluateData(healthGuidEvaluateData);
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
