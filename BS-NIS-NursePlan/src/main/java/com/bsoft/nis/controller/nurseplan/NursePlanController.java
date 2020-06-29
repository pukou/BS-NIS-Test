package com.bsoft.nis.controller.nurseplan;

import com.bsoft.nis.domain.nurseplan.*;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.nurseplan.NursePlanMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 护理计划控制器
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Controller
public class NursePlanController {

    @Autowired
    NursePlanMainService service;

    @RequestMapping(value = "/nurseplan")
    public String getMainPatientPage() {
        return "nurseplan/nurseplan";
    }

    /**
     * 获取病区护理计划列表及其记录数量
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/getPlanList")
    public
    @ResponseBody
    Response<List<Plan>> getPlanList(@RequestParam(value = "zyh") String zyh,
                                     @RequestParam(value = "bqid") String bqid,
                                     @RequestParam(value = "jgid") String jgid) {
        Response<List<Plan>> response = new Response<>();
        BizResponse<Plan> bizResponse = new BizResponse<>();

        bizResponse = service.getPlanList(zyh, bqid, jgid);
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
     * 获取病区护理焦点列表及其记录数量
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/getFocusList")
    public
    @ResponseBody
    Response<List<Plan>> getFocusList(@RequestParam(value = "zyh") String zyh,
                                      @RequestParam(value = "bqid") String bqid,
                                      @RequestParam(value = "jgid") String jgid) {
        Response<List<Plan>> response = new Response<>();
        BizResponse<Plan> bizResponse = new BizResponse<>();

        bizResponse = service.getFocusList(zyh, bqid, jgid);
        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;
        return response;
    }

    @RequestMapping(value = "auth/mobile/nurseplan/get/getFocusRelevanceGroupList")
    public
    @ResponseBody
    Response<List<FocusRelevanceGroupBean>> getFocusRelevanceGroupList(String zyh,String bqdm,String jgid,boolean isqueryedited) {
        Response<List<FocusRelevanceGroupBean>> response = new Response<>();
        BizResponse<FocusRelevanceGroupBean> bizResponse = new BizResponse<>();

        bizResponse = service.getFocusRelevanceGroupList(zyh,bqdm,jgid,isqueryedited);
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
     * 获取新的问题及其目标，评价，措施，相关因素，诊断依据
     * 其中目标跟相关因素只有护理计划存在，焦点不包含
     *
     * @param wtxh 问题序号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/getNewProblem")
    public
    @ResponseBody
    Response<Problem> getNewProblem(@RequestParam(value = "wtxh") String wtxh,
                                    @RequestParam(value = "jgid") String jgid) {
        Response<Problem> response = new Response<>();
        BizResponse<Problem> bizResponse = new BizResponse<>();

        bizResponse = service.getNewProblem(wtxh, jgid);
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
     * 保存护理计划问题
     *
     * @param problemSaveData 护理计划保存数据对象
     * @return
     */
    @RequestMapping(value = {"auth/mobile/nurseplan/post/saveNursePlanProblem", "mobile/nurseplan/post/saveNursePlanProblem"})
    public
    @ResponseBody
    Response<List<Problem>> saveNursePlanProblem(@RequestBody ProblemSaveData problemSaveData) {
        Response<List<Problem>> response = new Response<>();
        BizResponse<Problem> bizResponse = new BizResponse<>();

        bizResponse = service.saveNursePlanData(problemSaveData);
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
     * 保存护理焦点问题
     *
     * @param problemSaveData 护理计划保存数据对象
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/post/saveNurseFocusProblem")
    public
    @ResponseBody
    Response<List<Problem>> saveNurseFocusProblem(@RequestBody ProblemSaveData problemSaveData) {
        Response<List<Problem>> response = new Response<>();
        BizResponse<Problem> bizResponse = new BizResponse<>();

        bizResponse = service.saveNurseFocusData(problemSaveData);
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
     * 删除护理计划问题
     *
     * @param jlwt 记录问题
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/deleteNursePlanProblem")
    public
    @ResponseBody
    Response<String> deleteNursePlanProblem(@RequestParam(value = "jlwt") String jlwt, @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.deleteNursePlanProblem(jlwt, jgid);
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
     * 删除护理焦点问题
     *
     * @param jlwt 记录问题
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/deleteNurseFocusProblem")
    public
    @ResponseBody
    Response<String> deleteNurseFocusProblem(@RequestParam(value = "jlwt") String jlwt, @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.deleteNurseFocusProblem(jlwt, jgid);
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
     * 结束护理计划问题
     *
     * @param jlwt 记录问题
     * @param glxh 归类序号
     * @param wtxh 问题序号
     * @param zyh  住院号
     * @param yhid 用户id
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/terminateNursePlanProblem")
    public
    @ResponseBody
    Response<List<Problem>> terminateNursePlanProblem(@RequestParam(value = "jlwt") String jlwt, @RequestParam(value = "glxh") String glxh,
                                                      @RequestParam(value = "wtxh") String wtxh, @RequestParam(value = "zyh") String zyh,
                                                      @RequestParam(value = "yhid") String yhid, @RequestParam(value = "jgid") String jgid) {
        Response<List<Problem>> response = new Response<>();
        BizResponse<Problem> bizResponse = new BizResponse<>();

        bizResponse = service.terminateNursePlanProblem(jlwt, glxh, wtxh, zyh, yhid, jgid);
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
     * 结束护理焦点问题
     *
     * @param jlwt 记录问题
     * @param glxh 归类序号
     * @param wtxh 问题序号
     * @param zyh  住院号
     * @param yhid 用户id
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/terminateNurseFocusProblem")
    public
    @ResponseBody
    Response<List<Problem>> terminateNurseFocusProblem(@RequestParam(value = "jlwt") String jlwt, @RequestParam(value = "glxh") String glxh,
                                                       @RequestParam(value = "wtxh") String wtxh, @RequestParam(value = "zyh") String zyh,
                                                       @RequestParam(value = "yhid") String yhid, @RequestParam(value = "jgid") String jgid) {
        Response<List<Problem>> response = new Response<>();
        BizResponse<Problem> bizResponse = new BizResponse<>();

        bizResponse = service.terminateNurseFocusProblem(jlwt, glxh, wtxh, zyh, yhid, jgid);
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
     * 获取已记录的问题列表
     *
     * @param wtxh 问题序号
     * @param glxh 归类序号
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/getPlanProblemList")
    public
    @ResponseBody
    Response<List<Problem>> getPlanProblemList(@RequestParam(value = "wtxh") String wtxh, @RequestParam(value = "glxh") String glxh,
                                               @RequestParam(value = "zyh") String zyh, @RequestParam(value = "jgid") String jgid) {
        Response<List<Problem>> response = new Response<>();
        BizResponse<Problem> bizResponse = new BizResponse<>();

        bizResponse = service.getPlanProblemList(wtxh, glxh, zyh, jgid);
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
     * 获取已记录的问题列表
     *
     * @param wtxh 问题序号
     * @param glxh 归类序号
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/getFocusProblemList")
    public
    @ResponseBody
    Response<List<Problem>> getFocusProblemList(@RequestParam(value = "wtxh") String wtxh, @RequestParam(value = "glxh") String glxh,
                                                @RequestParam(value = "zyh") String zyh, @RequestParam(value = "jgid") String jgid) {
        Response<List<Problem>> response = new Response<>();
        BizResponse<Problem> bizResponse = new BizResponse<>();

        bizResponse = service.getFocusProblemList(wtxh, glxh, zyh, jgid);
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
     * 获取评价列表(记录及评价项目)
     *
     * @param jlwt 记录问题
     * @param wtxh 问题序号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/getPlanEvaluateList")
    public
    @ResponseBody
    Response<EvaluateAndRecord> getPlanEvaluateList(@RequestParam(value = "jlwt") String jlwt, @RequestParam(value = "wtxh") String wtxh,
                                                    @RequestParam(value = "jgid") String jgid) {
        Response<EvaluateAndRecord> response = new Response<>();
        BizResponse<EvaluateAndRecord> bizResponse = new BizResponse<>();

        bizResponse = service.getPlanEvaluateList(jlwt, wtxh, jgid);
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
     * 获取评价列表(记录及评价项目)
     *
     * @param jlwt 记录问题
     * @param wtxh 问题序号
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/getFocusEvaluateList")
    public
    @ResponseBody
    Response<EvaluateAndRecord> getFocusEvaluateList(@RequestParam(value = "jlwt") String jlwt, @RequestParam(value = "wtxh") String wtxh,
                                                     @RequestParam(value = "jgid") String jgid) {
        Response<EvaluateAndRecord> response = new Response<>();
        BizResponse<EvaluateAndRecord> bizResponse = new BizResponse<>();

        bizResponse = service.getFocusEvaluateList(jlwt, wtxh, jgid);
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
     * 保存问题评价
     *
     * @param problemEvaluateSaveData
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/post/savePlanProblemEvaluate")
    public
    @ResponseBody
    Response<EvaluateAndRecord> savePlanProblemEvaluate(@RequestBody ProblemEvaluateSaveData problemEvaluateSaveData) {
        Response<EvaluateAndRecord> response = new Response<>();
        BizResponse<EvaluateAndRecord> bizResponse = new BizResponse<>();

        bizResponse = service.savePlanProblemEvaluate(problemEvaluateSaveData);
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
     * 保存问题评价
     *
     * @param problemEvaluateSaveData
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/post/saveFocusProblemEvaluate")
    public
    @ResponseBody
    Response<EvaluateAndRecord> saveFocusProblemEvaluate(@RequestBody ProblemEvaluateSaveData problemEvaluateSaveData) {
        Response<EvaluateAndRecord> response = new Response<>();
        BizResponse<EvaluateAndRecord> bizResponse = new BizResponse<>();

        bizResponse = service.saveFocusProblemEvaluate(problemEvaluateSaveData);
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
     * 删除问题评价
     *
     * @param jlwt 记录问题
     * @param jlpj 记录评价
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/deletePlanProblemEvaluate")
    public
    @ResponseBody
    Response<String> deletePlanProblemEvaluate(@RequestParam(value = "jlwt") String jlwt, @RequestParam(value = "jlpj") String jlpj,
                                               @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.deletePlanProblemEvaluate(jlwt, jlpj, jgid);
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
     * 删除问题评价
     *
     * @param jlwt 记录问题
     * @param jlpj 记录评价
     * @param jgid 机构id
     * @return
     */
    @RequestMapping(value = "auth/mobile/nurseplan/get/deleteFocusProblemEvaluate")
    public
    @ResponseBody
    Response<String> deleteFocusProblemEvaluate(@RequestParam(value = "jlwt") String jlwt, @RequestParam(value = "jlpj") String jlpj,
                                                @RequestParam(value = "jgid") String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.deleteFocusProblemEvaluate(jlwt, jlpj, jgid);
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
