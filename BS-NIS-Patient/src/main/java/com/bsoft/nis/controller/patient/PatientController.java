package com.bsoft.nis.controller.patient;

import com.bsoft.nis.domain.patient.BCRYBean;
import com.bsoft.nis.domain.patient.BCSZBean;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.domain.patient.PatientDetailResponse;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.patient.PatientMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 病人控制器
 * Created by Administrator on 2016/10/10.
 */
@Controller
public class PatientController {

    @Autowired
    PatientMainService service;

    @RequestMapping(value = "/main")
    public String getMainPatientPage() {
        return "patient/patient";
    }

    @RequestMapping(value = "/get/zyhm")

    public
    @ResponseBody
    Response<Patient> getPatientByZyhm(@RequestParam(value = "zyhm") String zyhm) {
        Response<Patient> response = new Response<Patient>();
        BizResponse<Patient> bizResponse = new BizResponse<Patient>();

        return response;
    }

    /**
     * 根据住院号获取病人
     *
     * @param zyh
     * @return
     */
    public
    @ResponseBody
    Response<Patient> getPatientByZyh(@RequestParam(value = "zyh") String zyh) {
        Response<Patient> response = new Response<Patient>();
        BizResponse<Patient> bizResponse = new BizResponse<Patient>();

        bizResponse = service.getPatientByZyh(zyh);
        if (bizResponse.isSuccess) {
            response.ReType = 1;
        } else {
            response.ReType = 0;
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;
        return response;
    }

    /**
     * 获取病人列表
     *
     * @param bqid      病区代码
     * @param type      0病区病人;1体温单 ;2 医嘱;3口服单;4注射单;5 输液单;6血糖
     * @param starttime
     * @param endtime
     * @param hsgh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/patient/get/list")
    public
    @ResponseBody
    Response<List<SickPersonVo>> getPatientList(@RequestParam(value = "bqid") String bqid,
                                                @RequestParam(value = "type") int type,
                                                @RequestParam(value = "starttime", required = false) int starttime,
                                                @RequestParam(value = "endtime", required = false) int endtime,
                                                @RequestParam(value = "hsgh", required = false) String hsgh,
                                                @RequestParam(value = "jgid") String jgid) {
        Response<List<SickPersonVo>> response = new Response<>();
        BizResponse<SickPersonVo> bizResponse = new BizResponse<>();

        bizResponse = service.getPatientList(bqid, type, starttime, endtime, hsgh, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

/*    @RequestMapping(value = "auth/mobile/patient/get/group_ry_list")
    public
    @ResponseBody
    Response<List<BCRYBean>> getGroupRYList(@RequestParam(value = "bqid") String bqid,
                                          @RequestParam(value = "hsgh", required = false) String hsgh,
                                          @RequestParam(value = "jgid") String jgid) {
        Response<List<BCRYBean>> response = new Response<>();
        BizResponse<BCRYBean> bizResponse = new BizResponse<>();

        bizResponse = service.getGroupRYList(bqid,hsgh, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }*/

    @RequestMapping(value = "auth/mobile/patient/get/group_cfg_list")
    public
    @ResponseBody
    Response<List<BCSZBean>> getGroupCfgList(@RequestParam(value = "ygdm") String ygdm,
                                             @RequestParam(value = "bqdm") String bqdm,
                                             @RequestParam(value = "jgid") String jgid) {
        Response<List<BCSZBean>> response = new Response<>();
        BizResponse<BCSZBean> bizResponse = new BizResponse<>();

        bizResponse = service.getGroupCfgList(bqdm,ygdm, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }
    @RequestMapping(value = "auth/mobile/patient/post/group_ry_list_save")
    @ResponseBody
    public Response<String> saveGroupRY(@RequestBody List<BCRYBean> items,
                                        @RequestParam(value = "ygdm") String ygdm,
                                        @RequestParam(value = "bqdm") String bqdm) {
        Response<String> response = new Response<>();
        BizResponse<String> biz;
        biz = service.saveGroupRYList(items,ygdm,bqdm);

        response.ReType = biz.isSuccess ? 0 : -1;
        response.Data = biz.data;
        response.Msg = biz.message;

        return response;
    }
    /**
     * 获取病人详情
     *
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/patient/get/detail")
    public
    @ResponseBody
    Response<PatientDetailResponse> getPatientDetail(@RequestParam(value = "zyh") String zyh,
                                                     @RequestParam(value = "jgid") String jgid) {
        Response<PatientDetailResponse> response = new Response<>();
        BizResponse<PatientDetailResponse> bizResponse = new BizResponse<>();

        bizResponse = service.getPatientDetail(zyh, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }
/*升级编号【56010038】============================================= start
                         外出管理PDA上只有登记功能，查询需要找到具体的人再查询，不太方便，最好能有一个查询整个病区外出病人的列表
            ================= classichu 2018/3/7 20:03
            */
    /**
     * 获取病人详情
     *
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/patient/get/getPatientForHand")
    public
    @ResponseBody
    Response<SickPersonVo> getPatientForHand(@RequestParam(value = "zyh") String zyh,
                                      @RequestParam(value = "jgid") String jgid) {
        Response<SickPersonVo> response = new Response<>();
        BizResponse<SickPersonVo> bizResponse = new BizResponse<>();

        bizResponse = service.getPatientForHand(zyh, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;
        return response;
    }
/* =============================================================== end */

    /**
     * 获取病人详情
     *
     * @param prefix
     * @param barcode
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/patient/get/getPatientForScan")
    public
    @ResponseBody
    Response<SickPersonVo> getPatientForScan(@RequestParam(value = "prefix") String prefix,
                                             @RequestParam(value = "barcode") String barcode,
                                             @RequestParam(value = "jgid") String jgid) {
        Response<SickPersonVo> response = new Response<>();
        BizResponse<SickPersonVo> bizResponse = new BizResponse<>();

        bizResponse = service.getPatientForScan(prefix, barcode, jgid);
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
     * 检查病人有无绑定rfid
     *
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/patient/get/getRFID")
    public
    @ResponseBody
    Response<String> getRFID(@RequestParam(value = "zyh") String zyh,
                             @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.getRFID(zyh, jgid);
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
     * 给病人绑定rfid
     *
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/patient/get/patientBindRFID")
    public
    @ResponseBody
    Response<String> patientBindRFID(@RequestParam(value = "zyh") String zyh,
                                     @RequestParam(value = "sbid") String sbid,
                                     @RequestParam(value = "yhid") String yhid,
                                     @RequestParam(value = "bqid") String bqid,
                                     @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.patientBindRFID(zyh, sbid, yhid, bqid, jgid);
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
     * 给病人取消绑定rfid
     *
     * @param zyh
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/patient/get/patientUnBindRFID")
    public
    @ResponseBody
    Response<String> patientUnBindRFID(@RequestParam(value = "zyh") String zyh,
                                       @RequestParam(value = "sbid") String sbid,
                                       @RequestParam(value = "shbs") String shbs,
                                       @RequestParam(value = "yhid") String yhid,
                                       @RequestParam(value = "bqid") String bqid,
                                       @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.patientUnBindRFID(zyh, sbid, shbs, yhid, bqid, jgid);
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
