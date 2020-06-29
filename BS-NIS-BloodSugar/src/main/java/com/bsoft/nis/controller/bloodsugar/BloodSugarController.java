package com.bsoft.nis.controller.bloodsugar;

import com.bsoft.nis.domain.bloodsugar.BloodSugar;
import com.bsoft.nis.domain.bloodsugar.PersonBloodSugar;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.BloodSugarMainService;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: 血糖治疗（new）控制器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-05-22
 * Time: 13:32
 * Version:
 */
@Controller
public class BloodSugarController {

    @Autowired
    BloodSugarMainService service;

    @RequestMapping(value = "/bloodsugar")
    public String getMainPatientPage() {
        return "bloodsugar/bloodsugar";
    }

    /**
     * 获取测量时点列表
     *
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodsugar/get/getClsdList")
    public
    @ResponseBody
    Response<List<String>> getClsdList() {
        Response<List<String>> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.getClsdList();
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
     * 获取血糖治疗列表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param zyh   住院号
     * @param jgid  机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodsugar/get/getBloodSugarList")
    public
    @ResponseBody
    Response<List<BloodSugar>> getBloodSugarList(@RequestParam(value = "start") String start,
                                                 @RequestParam(value = "end") String end,
                                                 @RequestParam(value = "zyh") String zyh,
                                                 @RequestParam(value = "jgid") String jgid) {
        Response<List<BloodSugar>> response = new Response<>();
        BizResponse<BloodSugar> bizResponse = new BizResponse<>();

        bizResponse = service.getBloodSugarList(start, end, zyh, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    @RequestMapping(value = "auth/mobile/bloodsugar/get/getNeedGetBloodSugarList")
    public
    @ResponseBody
    Response<List<PersonBloodSugar>> getNeedGetBloodSugarList(@RequestParam(value = "clsd") String clsd,
                                                              @RequestParam(value = "brbq") String brbq,
                                                        //可选=====
                                                              @RequestParam(value = "nowZyh") String nowZyh,
                                                              @RequestParam(value = "preClsdArrStr") String preClsdArrStr,
                                                              //可选=====
                                                              @RequestParam(value = "jgid") String jgid) {
        Response<List<PersonBloodSugar>> response = new Response<>();
        BizResponse<PersonBloodSugar> bizResponse = new BizResponse<>();
        bizResponse = service.getNeedGetBloodSugarList(clsd, brbq, jgid);
        //该病人 如果有的话   该时点之前的时点
        String needTXclsd = "";
        if (!TextUtils.isEmpty(nowZyh) && !TextUtils.isEmpty(preClsdArrStr)) {
            //
            String[] preClsdArr;
            if (preClsdArrStr.contains(",")) {
                preClsdArr = preClsdArrStr.split(",");
            } else {
                preClsdArr = new String[]{preClsdArrStr};
            }
            List<String> preClsdList = new ArrayList<>(Arrays.asList(preClsdArr));
            //
            StringBuilder needTXclsd_Sb = new StringBuilder();
            //
            if (!preClsdList.isEmpty()) {
                    ////
                    //之前的所有时点
//                    boolean preClsdList_HasNeedGet = false;
                    BizResponse<PersonBloodSugar> bizResponseTemp = service.getNeedGetBloodSugarListArr(preClsdList, brbq, jgid);
                    if (bizResponseTemp.isSuccess && bizResponseTemp.datalist != null) {
                        for (PersonBloodSugar personBloodSugar : bizResponseTemp.datalist) {
                            if (personBloodSugar.ZYH.equals(nowZyh)) {
//                                preClsdList_HasNeedGet = true;
                                needTXclsd_Sb.append(personBloodSugar.SDMC);
                                needTXclsd_Sb.append(",");
                            }
                        }
                    }
                    ////
            }
            //
            if (needTXclsd_Sb.toString().endsWith(",")) {
                needTXclsd_Sb.delete(needTXclsd_Sb.lastIndexOf(","), needTXclsd_Sb.length());
            }
            needTXclsd = needTXclsd_Sb.toString();
            //
        }
        if (bizResponse.isSuccess) {
            response.ReType = 0;
            if (TextUtils.isEmpty(needTXclsd)) {
                response.Msg = bizResponse.message;
            } else {
                response.Msg = "该病人遗漏的时点：\n" + needTXclsd;
            }
        } else {
            response.ReType = -1;
            response.Msg = bizResponse.message;
        }
        response.Data = bizResponse.datalist;
        return response;
    }


    @RequestMapping(value = "auth/mobile/bloodsugar/get/getNeedGetBloodSugarListArr")
    public
    @ResponseBody
    Response<List<PersonBloodSugar>> getNeedGetBloodSugarListArr(@RequestParam(value = "clsds") String clsds,
                                                                 @RequestParam(value = "brbq") String brbq,
                                                                 @RequestParam(value = "jgid") String jgid) {
        Response<List<PersonBloodSugar>> response = new Response<>();
        BizResponse<PersonBloodSugar> bizResponse = new BizResponse<>();
        if (clsds == null) {
            response.ReType = -1;
            response.Msg = "入参有误";
            return response;
        }
        String[] clsdArr;
        if (clsds.contains(",")) {
            clsdArr = clsds.split(",");
        } else {
            clsdArr = new String[]{clsds};
        }
        List<String> clsdList = new ArrayList<>(Arrays.asList(clsdArr));
//        List<PersonBloodSugar> personBloodSugarList = new ArrayList<>();
        bizResponse = service.getNeedGetBloodSugarListArr(clsdList, brbq, jgid);
       /* if (bizResponse.isSuccess && bizResponse.datalist != null) {
            for (PersonBloodSugar bloodSugar : bizResponse.datalist) {
                if ((i, brbq, jgid, bloodSugar.ZYH, clsdList)) {
                    //说名 该测量点有遗漏
                    bloodSugar.TXBZ = "1";
                } else {
                    bloodSugar.TXBZ = "0";
                }
                personBloodSugarList.add(bloodSugar);
            }

        }*/
        /*for (PersonBloodSugar personBloodSugar : personBloodSugarList) {
            List<PersonBloodSugar> nowPersnList = findTheSickList(personBloodSugarList, personBloodSugar.ZYH);
            for (int i = 0; i < nowPersnList.size(); i++) {
                PersonBloodSugar temp = nowPersnList.get(i);
                if (temp.SDMC.equals(personBloodSugar.SDMC)) {
                    //找到当前时点
                    //如果下一个是遗漏的，
                    if (i < nowPersnList.size() - 1) {
                        PersonBloodSugar personBloodSugarNext = nowPersnList.get(i + 1);
                        if ("1".equals(personBloodSugarNext.TXBZ)) {
                            //那么说明这个也是遗漏的
                            personBloodSugar.TXBZ = "1";
                        }
                    }
                }
            }
        }*/
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

   /* private List<PersonBloodSugar> findTheSickList(List<PersonBloodSugar> personBloodSugarListAll, String zyh) {
        List<PersonBloodSugar> personBloodSugarList = new ArrayList<>();
        for (PersonBloodSugar personBloodSugar : personBloodSugarListAll) {
            if (zyh.equals(personBloodSugar.ZYH)) {
                personBloodSugarList.add(personBloodSugar);
            }
        }
        return personBloodSugarList;
    }
*/



    /**
     * 新增血糖治疗数据
     *
     * @param zyh  住院号
     * @param sxbq 书写病区
     * @param brch 病人床号
     * @param sxsj 书写时间
     * @param sxgh 书写工号
     * @param cjgh 创建工号
     * @param clsd 测量时点
     * @param clz  测量值
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodsugar/get/addBloodSugar")
    public
    @ResponseBody
    Response<String> addBloodSugar(@RequestParam(value = "zyh") String zyh,
                                   @RequestParam(value = "sxbq") String sxbq,
                                   @RequestParam(value = "brch") String brch,
                                   @RequestParam(value = "sxsj") String sxsj,
                                   @RequestParam(value = "sxgh") String sxgh,
                                   @RequestParam(value = "cjgh") String cjgh,
                                   @RequestParam(value = "clsd") String clsd,
                                   @RequestParam(value = "clz") String clz,
                                   @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.addBloodSugar(zyh, sxbq, brch, sxsj, sxgh, cjgh, clsd, clz, jgid);
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
     * 修改血糖治疗数据
     *
     * @param jlxh 记录序号
     * @param clsd 测量时点
     * @param sxsj 书写时间
     * @param clz  测量值
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodsugar/get/editBloodSugar")
    public
    @ResponseBody
    Response<String> editBloodSugar(@RequestParam(value = "jlxh") String jlxh,
                                    @RequestParam(value = "clsd") String clsd,
                                    @RequestParam(value = "sxsj") String sxsj,
                                    @RequestParam(value = "clz") String clz) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.editBloodSugar(jlxh, clsd, sxsj, clz);
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
     * 删除血糖治疗数据
     *
     * @param jlxh 记录序号
     * @return
     */
    @RequestMapping(value = "auth/mobile/bloodsugar/get/deleteBloodSugar")
    public
    @ResponseBody
    Response<String> deleteBloodSugar(@RequestParam(value = "jlxh") String jlxh) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.deleteBloodSugar(jlxh);
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
