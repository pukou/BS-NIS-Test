package com.bsoft.nis.controller.outcontrol;

import com.bsoft.nis.domain.outcontrol.OutControl;
import com.bsoft.nis.domain.outcontrol.OutControlSaveData;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.outcontrol.OutControllerBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 外出管理控制器
 * Created by king on 2016/11/16.
 */
@Controller
public class OutControlBaseController {

    @Autowired
    OutControllerBaseService service;

    /**
     * 获取巡视记录，巡视病人
     *
     * @param zyh     住院号
     * @param brbq    病人病区
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/outcontrol/get/GetOutPatientByZyh")
    public
    @ResponseBody
    Response<List<OutControl>> getOutPatientByZyh(@RequestParam(value = "zyh") String zyh,
                                                  @RequestParam(value = "brbq") String brbq,
                                                  @RequestParam(value = "jgid") String jgid,
                                                  @RequestParam(value = "sysType") String sysType) {
        Response<List<OutControl>> response = new Response<>();
        BizResponse<OutControl> bizResponse;

        bizResponse = service.getOutPatientByZyh(zyh, brbq, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;

    }
    /*升级编号【56010038】============================================= start
                外出管理PDA上只有登记功能，查询需要找到具体的人再查询，不太方便，最好能有一个查询整个病区外出病人的列表
            ================= classichu 2018/3/7 19:49
            */
    /**
     * 获取病区所有外出病人列表
     *
     * @param brbq    病人病区
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/outcontrol/get/getAllOutPatients")
    public
    @ResponseBody
    Response<List<OutControl>> getAllOutPatients(@RequestParam(value = "brbq") String brbq,
                                                @RequestParam(value = "jgid") String jgid,
                                                @RequestParam(value = "sysType") String sysType) {
        Response<List<OutControl>> response = new Response<>();
        BizResponse<OutControl> bizResponse;

        bizResponse = service.getAllOurPatients(brbq, jgid);

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
     * 获取病人当前外出状态
     *
     * @param zyh     住院号
     * @param brbq    病人病区
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/outcontrol/get/GetPatientStatus")
    public
    @ResponseBody
    Response<List<OutControl>> getPatientStatus(@RequestParam(value = "zyh") String zyh,
                                                @RequestParam(value = "brbq") String brbq,
                                                @RequestParam(value = "jgid") String jgid,
                                                @RequestParam(value = "sysType") String sysType) {
        Response<List<OutControl>> response = new Response<>();
        BizResponse<OutControl> bizResponse;

        bizResponse = service.getPatientStatus(zyh, brbq, jgid);

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
     * 外出登记
     *
     * @param outControlSaveData
     * @return
     */
    @RequestMapping(value = "auth/mobile/outcontrol/post/RegisterOutPatient")
    public
    @ResponseBody
    Response<String> registerOutPatient(@RequestBody OutControlSaveData outControlSaveData) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse;

        bizResponse = service.registerOutPatient(outControlSaveData);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;

        return response;

    }

    /**
     * 回床登记
     *
     * @param jlxh    记录序号
     * @param hcdjsj  回床登记时间
     * @param hcdjhs  回床登记护士
     * @param jgid    机构id
     * @param sysType
     * @return
     */
    @RequestMapping(value = "auth/mobile/outcontrol/get/RegisterBackToBed")
    public
    @ResponseBody
    Response<String> registerBackToBed(@RequestParam(value = "jlxh") Long jlxh,
                                       @RequestParam(value = "hcdjsj") String hcdjsj,
                                       @RequestParam(value = "hcdjhs") String hcdjhs,
                                       @RequestParam(value = "jgid") String jgid,
                                       @RequestParam(value = "sysType") String sysType) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse;

        bizResponse = service.registerBackToBed(jlxh, hcdjsj, hcdjhs, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;

        return response;
    }


}
