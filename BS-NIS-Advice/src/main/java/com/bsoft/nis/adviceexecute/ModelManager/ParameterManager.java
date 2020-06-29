package com.bsoft.nis.adviceexecute.ModelManager;

import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.adviceexecute.Parameter;
import com.bsoft.nis.domain.user.db.Agency;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.UserMainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 用户参数管理
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 13:47
 * Version:
 */
@Component
public class ParameterManager {

    @Autowired
    SystemParamService systemParamService;//用户参数服务

    @Autowired
    UserMainService service; // 用户服务

    private Map<String, Parameter> parameterMap;//参数对象Map

    public Map<String, Parameter> getParameterMap() {
        if (this.parameterMap == null) {
            InitParameter();
        }
        return this.parameterMap;
    }

    public void InitParameter() {
        boolean isUpdate = checkUpdate();
        if (isUpdate) {
            parameterMap = new HashMap();
            BizResponse<List> bizResponse = service.getAgency(null);
            if (bizResponse.isSuccess) {
                List<Agency> list = bizResponse.data;
                for (Agency item : list) {
                    InitParameter(item.getJGID(), isUpdate);
                }
            }
        }
    }

    /**
     * 根据机构号初始化对应机构的用户参数
     *
     * @param jgid
     * @param isUpdate
     * @return
     */
    public void InitParameter(String jgid, boolean isUpdate) {
        if (!isUpdate) {
            isUpdate = checkUpdate();
        }
        if (isUpdate) {
            Parameter parameter = getParameter(jgid);
            if (parameter != null) {
                parameterMap.put(jgid, parameter);
            }
        }
    }

    private boolean checkUpdate() {
        boolean isUpdate = false;
        if (parameterMap != null) {
            Long now = System.currentTimeMillis();
            Long oldTimeStamp = new Long(0);
            for (Map.Entry<String, Parameter> item : parameterMap.entrySet()) {
                oldTimeStamp = item.getValue().TimeStamp;
                break;
            }
            if ((now - oldTimeStamp) / (1000 * 60) > 30) {
                isUpdate = true;
            }
        } else {
            isUpdate = true;
        }
        return isUpdate;
    }

    private Parameter getParameter(String jgid) {
        Parameter parameter = null;
        if (!StringUtils.isBlank(jgid)) {
            parameter = new Parameter();
            parameter.TimeStamp = System.currentTimeMillis();
            List<String> listPrefixCaseSensitive = systemParamService.getUserParams("1", "IENR", "IENR_A_PRE_CSEN", jgid, DataSource.MOB).datalist;
            if (listPrefixCaseSensitive != null && listPrefixCaseSensitive.size() > 0) {
                parameter.PrefixCaseSensitive = listPrefixCaseSensitive.get(0).equals("1");
            } else {
                parameter.PrefixCaseSensitive = false;
            }

            List<String> listOralUsePrefix = systemParamService.getUserParams("1", "IENR", "IENR_A_O_UPREFIX", jgid, DataSource.MOB).datalist;
            if (listOralUsePrefix != null && listOralUsePrefix.size() > 0) {
                parameter.OralUsePrefix = listOralUsePrefix.get(0).equals("1");
            } else {
                parameter.OralUsePrefix = true;//默认true
            }
            List<String> listTransfusionUsePrefix = systemParamService.getUserParams("1", "IENR", "IENR_A_T_UPREFIX", jgid, DataSource.MOB).datalist;
            if (listTransfusionUsePrefix != null && listTransfusionUsePrefix.size() > 0) {
                parameter.TransfusionUsePrefix = listTransfusionUsePrefix.get(0).equals("1");
            } else {
                parameter.TransfusionUsePrefix = true;//默认true
            }
            List<String> listInjectionUsePrefix = systemParamService.getUserParams("1", "IENR", "IENR_A_I_UPREFIX", jgid, DataSource.MOB).datalist;
            if (listInjectionUsePrefix != null && listInjectionUsePrefix.size() > 0) {
                parameter.InjectionUsePrefix = listInjectionUsePrefix.get(0).equals("1");
            } else {
                parameter.InjectionUsePrefix = true;//默认true
            }

            List<String> listOralJoinPlanByTime = systemParamService.getUserParams("1", "IENR", "IENR_A_O_BTIME", jgid, DataSource.MOB).datalist;
            if (listOralJoinPlanByTime != null && listOralJoinPlanByTime.size() > 0) {
                parameter.OralJoinPlanByTime = listOralJoinPlanByTime.get(0).equals("1");
            } else {
                parameter.OralJoinPlanByTime = true;//默认true
            }
            List<String> listTransfusionJoinPlanByTime = systemParamService.getUserParams("1", "IENR", "IENR_A_T_BTIME", jgid, DataSource.MOB).datalist;
            if (listTransfusionJoinPlanByTime != null && listTransfusionJoinPlanByTime.size() > 0) {
                parameter.TransfusionJoinPlanByTime = listTransfusionJoinPlanByTime.get(0).equals("1");
            } else {
                parameter.TransfusionJoinPlanByTime = true;//默认true
            }
            List<String> listInjectionJoinPlanByTime = systemParamService.getUserParams("1", "IENR", "IENR_A_I_BTIME", jgid, DataSource.MOB).datalist;
            if (listInjectionJoinPlanByTime != null && listInjectionJoinPlanByTime.size() > 0) {
                // 0 是时间编号  1 是时间
                parameter.InjectionJoinPlanByTime = listInjectionJoinPlanByTime.get(0).equals("1");
            } else {
                parameter.InjectionJoinPlanByTime = true;//默认 是时间
            }

            List<String> listOralHandExcuteCheckKfd = systemParamService.getUserParams("1", "IENR", "IENR_A_O_HECKKFD", jgid, DataSource.MOB).datalist;
            if (listOralHandExcuteCheckKfd != null && listOralHandExcuteCheckKfd.size() > 0) {
                parameter.OralHandExcuteCheckKfd = listOralHandExcuteCheckKfd.get(0).equals("1");
            } else {
                parameter.OralHandExcuteCheckKfd = true;//默认true
            }
//            List<String> listTransfusionHandExcuteCheckSyd = systemParamService.getUserParams("1", "IENR", "IENR_A_T_HECKSYD", jgid, DataSource.MOB).datalist;
//            if(listTransfusionHandExcuteCheckSyd != null && listTransfusionHandExcuteCheckSyd.size() > 0) {
//                parameter.TransfusionHandExcuteCheckSyd = listTransfusionHandExcuteCheckSyd.get(0).equals("1");
//            } else {
//                parameter.TransfusionHandExcuteCheckSyd = true;//默认true
//            }
            parameter.TransfusionHandExcuteCheckSyd = true;//由于输液特殊性必需要核对
            List<String> listInjectionHandExcuteCheckZsd = systemParamService.getUserParams("1", "IENR", "IENR_A_I_HECKZSD", jgid, DataSource.MOB).datalist;
            if (listInjectionHandExcuteCheckZsd != null && listInjectionHandExcuteCheckZsd.size() > 0) {
                parameter.InjectionHandExcuteCheckZsd = listInjectionHandExcuteCheckZsd.get(0).equals("1");
            } else {
                parameter.InjectionHandExcuteCheckZsd = true;//默认true
            }

            List<String> listInjectionDosingCheck = systemParamService.getUserParams("1", "IENR", "IENR_ZSYZ_JYHD", jgid, DataSource.MOB).datalist;
            if (listInjectionDosingCheck != null && listInjectionDosingCheck.size() > 0) {
                parameter.InjectionDosingCheck = listInjectionDosingCheck.get(0).equals("1");
            } else {
                parameter.InjectionDosingCheck = false;//默认 false 2018-6-7 14:20:20
            }
            List<String> listTransfusionDosingCheck = systemParamService.getUserParams("1", "IENR", "IENR_SYYZ_JYHD", jgid, DataSource.MOB).datalist;
            if (listTransfusionDosingCheck != null && listTransfusionDosingCheck.size() > 0) {
                parameter.TransfusionDosingCheck = listTransfusionDosingCheck.get(0).equals("1");
            } else {
                parameter.TransfusionDosingCheck = false;//默认 false 2018-6-7 14:20:20
            }

            List<String> listOpenCharge = systemParamService.getUserParams("1", "IENR", "IENR_A_OPCHARGE", jgid, DataSource.MOB).datalist;
            if (listOpenCharge != null && listOpenCharge.size() > 0) {
                parameter.OpenCharge = listOpenCharge.get(0).equals("1");
            } else {
                parameter.OpenCharge = false;//默认 false 2018-6-7 14:20:20
            }

            List<String> listCheckYzb = systemParamService.getUserParams("1", "IENR", "IENR_A_CK_YZB", jgid, DataSource.MOB).datalist;
            if (listCheckYzb != null && listCheckYzb.size() > 0) {
                parameter.CheckYzb = listCheckYzb.get(0).equals("1");
            } else {
                parameter.CheckYzb = false;//默认 false 2018-6-7 14:20:20
            }
            List<String> listUpdateYzb = systemParamService.getUserParams("1", "IENR", "IENR_A_UPYZB", jgid, DataSource.MOB).datalist;
            if (listUpdateYzb != null && listUpdateYzb.size() > 0) {
                parameter.UpdateYzb = listUpdateYzb.get(0).equals("1");
            } else {
                parameter.UpdateYzb = false;//默认 false 2018-6-7 14:20:20
            }
            List<String> listUpdateBqyz = systemParamService.getUserParams("1", "IENR", "IENR_A_UPBQYZ", jgid, DataSource.MOB).datalist;
            if (listUpdateBqyz != null && listUpdateBqyz.size() > 0) {
                parameter.UpdateBqyz = listUpdateBqyz.get(0).equals("1");
            } else {
                parameter.UpdateBqyz = false;//默认 false 2018-6-7 14:20:20
            }
            List<String> listUpdateBQyzjh = systemParamService.getUserParams("1", "IENR", "IENR_YTHHL_GXBQYZJH", jgid, DataSource.MOB).datalist;
            if (listUpdateBQyzjh != null && listUpdateBQyzjh.size() > 0) {
                parameter.UpdateBQyzjh = listUpdateBQyzjh.get(0).equals("1");
            } else {
                parameter.UpdateBQyzjh = false;//默认 false 2018-6-7 14:20:20
            }

            List<String> listDoubleCheck = systemParamService.getUserParams("1", "IENR", "IENR_A_DC", jgid, DataSource.MOB).datalist;
            if (listDoubleCheck != null && listDoubleCheck.size() > 0) {
                parameter.DoubleCheck = listDoubleCheck.get(0).equals("1");
            } else {
                parameter.DoubleCheck =false;//默认 false 2018-6-7 14:20:20
            }
            List<String> listDoubleCheckTimeOut = systemParamService.getUserParams("1", "IENR", "IENR_A_DC_TOUT", jgid, DataSource.MOB).datalist;
            if (listDoubleCheckTimeOut != null && listDoubleCheckTimeOut.size() > 0) {
                parameter.DoubleCheckTimeOut = Integer.parseInt(listDoubleCheckTimeOut.get(0));
            } else {
                parameter.DoubleCheckTimeOut = 300;//秒
            }

            List<String> listChildAge = systemParamService.getUserParams("1", "IENR", "IENR_CHILDAGE", jgid, DataSource.MOB).datalist;
            if (listChildAge != null && listChildAge.size() > 0) {
                parameter.ChildAge = Integer.parseInt(listChildAge.get(0));
            } else {
                parameter.ChildAge = 12;  //默认 12  2018-6-7 14:20:20
            }

            List<String> listOralUpdate = systemParamService.getUserParams("1", "IENR", "IENR_A_O_UPDATE", jgid, DataSource.MOB).datalist;
            if (listOralUpdate != null && listOralUpdate.size() > 0) {
                parameter.OralUpdate = listOralUpdate.get(0).equals("1");
            } else {
                parameter.OralUpdate = true;
            }
            List<String> listTransfusionUpdate = systemParamService.getUserParams("1", "IENR", "IENR_A_T_UPDATE", jgid, DataSource.MOB).datalist;
            if (listTransfusionUpdate != null && listTransfusionUpdate.size() > 0) {
                parameter.TransfusionUpdate = listTransfusionUpdate.get(0).equals("1");
            } else {
                parameter.TransfusionUpdate = true;
            }
            List<String> listInjectionUpdate = systemParamService.getUserParams("1", "IENR", "IENR_A_I_UPDATE", jgid, DataSource.MOB).datalist;
            if (listInjectionUpdate != null && listInjectionUpdate.size() > 0) {
                parameter.InjectionUpdate = listInjectionUpdate.get(0).equals("1");
            } else {
                parameter.InjectionUpdate = true;
            }

            List<String> listIntegrated = systemParamService.getUserParams("1", "IENR", "IENR_YTHHL_QRYTHBQ", jgid, DataSource.MOB).datalist;
            if (listIntegrated != null && listIntegrated.size() > 0) {
                parameter.Integrated = listIntegrated.get(0);
            } else {
                parameter.Integrated = "0";//默认 0
            }

            List<String> listOralAllowHandExcute = systemParamService.getUserParams("1", "IENR", "IENR_A_O_HANDEXC", jgid, DataSource.MOB).datalist;
            if (listOralAllowHandExcute != null && listOralAllowHandExcute.size() > 0) {
                parameter.OralAllowHandExcute = listOralAllowHandExcute.get(0).equals("1");
            } else {
                parameter.OralAllowHandExcute = true;
            }
            List<String> listTransfusionAllowHandExcute = systemParamService.getUserParams("1", "IENR", "IENR_A_T_HANDEXC", jgid, DataSource.MOB).datalist;
            if (listTransfusionAllowHandExcute != null && listTransfusionAllowHandExcute.size() > 0) {
                parameter.TransfusionAllowHandExcute = listTransfusionAllowHandExcute.get(0).equals("1");
            } else {
                parameter.TransfusionAllowHandExcute = true;
            }
            List<String> listInjectionAllowHandExcute = systemParamService.getUserParams("1", "IENR", "IENR_A_I_HANDEXC", jgid, DataSource.MOB).datalist;
            if (listInjectionAllowHandExcute != null && listInjectionAllowHandExcute.size() > 0) {
                parameter.InjectionAllowHandExcute = listInjectionAllowHandExcute.get(0).equals("1");
            } else {
                parameter.InjectionAllowHandExcute = true;
            }

            List<String> listDropSpeedConversion = systemParamService.getUserParams("1", "IENR", "ENR_INTRASPEC", jgid, DataSource.MOB).datalist;
            parameter.DropSpeedConversion = Float.parseFloat((listDropSpeedConversion != null && listDropSpeedConversion.size() > 0) ? listDropSpeedConversion.get(0) : "20");
            parameter.DropSpeedUnit = new ArrayList<>();
            List<String> dsdwList = systemParamService.getUserParams("1", "IENR", "ENR_INTRADOSUNIT", jgid, DataSource.MOB).datalist;
            if (dsdwList != null && dsdwList.size() > 0) {
                String[] strArray = dsdwList.get(0).split(",");
                for (String item : strArray) {
                    if (!StringUtils.isBlank(item)) {
                        parameter.DropSpeedUnit.add(item.toLowerCase());
                    }
                }
            }
            List<String> listSyncNuserRecord = systemParamService.getUserParams("1", "IENR", "IENR_YZZX_NRXRHLJL", jgid, DataSource.MOB).datalist;
            if (listSyncNuserRecord != null && listSyncNuserRecord.size() > 0) {
                parameter.SyncNuserRecord = listSyncNuserRecord.get(0).equals("1");
            } else {
                parameter.SyncNuserRecord = false;
            }

        }
        return parameter;
    }
}
