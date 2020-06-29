package com.bsoft.nis.adviceexecute.Preprocessing;

import com.bsoft.nis.adviceexecute.ModelManager.*;
import com.bsoft.nis.domain.adviceexecute.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 护理治疗类型预处理
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-23
 * Time: 16:50
 * Version:
 */
@Component
public class NursePreprocessing {

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    PlanInfoManager planInfoManager;//医嘱计划管理器

    @Autowired
    AdviceBqyzInfoManager adviceBqyzInfoManager;//病区医嘱管理器

    @Autowired
    AdviceYzbInfoManager adviceYzbInfoManager;//医嘱本管理器

    @Autowired
    TransfuseInfoManager transfuseInfoManager;//输液单管理器

    @Autowired
    MessageManager messageManager;//消息生成管理器

    private Log logger = LogFactory.getLog(TransfusePreprocessing.class);

    /**
     * 计划提交预处理
     *
     * @param gslx            归属类型
     * @param zyh             住院号
     * @param yhid            用户id
     * @param planArgInfoList
     * @param jgid            机构id
     * @return
     */
    public List<ExecuteArg> Preprocessing(String gslx, String zyh, String yhid, List<PlanArgInfo> planArgInfoList, String jgid) {
        List<ExecuteArg> ealist = null;
        try {
            ealist = new ArrayList<>();
            List<String> del = new ArrayList<>();
            for (PlanArgInfo planArgInfo : planArgInfoList) {
                PlanInfo planInfo = planInfoManager.getPlanInfoByJhh(planArgInfo.JHH, jgid);
                if (planInfo.GSLX.equals(gslx) && !del.contains(planInfo.JHH)) {
                    List<AdviceBqyzInfo> adviceBqyzInfoList = new ArrayList<>();
                    AdviceBqyzInfo adviceBqyzInfo = adviceBqyzInfoManager.getAdviceBqyzInfo(zyh,planInfo.YZXH, jgid);
                    adviceBqyzInfoList.add(adviceBqyzInfo);
                    List<PlanInfo> planInfoList = new ArrayList<>();
                    planInfoList.add(planInfo);
                    if (parameterManager.getParameterMap().get(jgid).CheckYzb&&! TextUtils.isEmpty(planInfo.YSYZBH)) {
                        List<AdviceYzbInfo> adviceYzbInfoList = new ArrayList<>();
                        AdviceYzbInfo adviceYzbInfo = adviceYzbInfoManager.getAdviceYzbInfo(zyh,planInfo.YSYZBH, jgid);
                        adviceYzbInfoList.add(adviceYzbInfo);
                        ealist.add(new ExecuteArg(planInfoList, adviceBqyzInfoList, adviceYzbInfoList, zyh, null, planArgInfo.FYXH, null, yhid, true, jgid));

                    } else {
                        ealist.add(new ExecuteArg(planInfoList, adviceBqyzInfoList, zyh, null, planArgInfo.FYXH, null, yhid, true, jgid));
                    }
                    del.add(planInfo.JHH);
                }
            }
            //清理
            for (int i = 0; i < planArgInfoList.size(); i++) {
                if (del.contains(planArgInfoList.get(i).JHH)) {
                    planArgInfoList.remove(i);
                    i--;
                    continue;
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ealist;
    }

}
