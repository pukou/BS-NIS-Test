package com.bsoft.nis.common.service;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.core.UserConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserConfigService {

    private Log logger = LogFactory.getLog(UserConfigService.class);

    @Autowired
    SystemParamService systemParamService;


    public UserConfig getUserConfig(String jgid) {
        //取参数
        UserConfig userConfig = new UserConfig();

        //================================== advice ==============================
        //是否启用 静推
        List<String> qiYong_JingTuiList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_JTZX", jgid, DataSource.MOB).datalist;
        if (qiYong_JingTuiList != null && qiYong_JingTuiList.size() > 0) {
            userConfig.qiYong_JingTui = qiYong_JingTuiList.get(0).equals("1");
        } else {
            userConfig.qiYong_JingTui = false;
        }
        //静推 用法
        userConfig.jingTui_YaoPinYongFa = UserConfig.DEFAULT_STRING_NEGATIVE;
        if (userConfig.qiYong_JingTui) {
            List<String> jingTui_YaoPinYongFaList = systemParamService.getUserParams("1", "IENR", "IENR_JTYFDM", jgid, DataSource.MOB).datalist;
            if (jingTui_YaoPinYongFaList != null && jingTui_YaoPinYongFaList.size() > 0) {
                userConfig.jingTui_YaoPinYongFa = jingTui_YaoPinYongFaList.get(0);
            }
        }
        //是否启用 微注泵推注
        List<String> qiYong_WeiZhuBenTuiZhuList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_WZBTZX", jgid, DataSource.MOB).datalist;
        if (qiYong_WeiZhuBenTuiZhuList != null && qiYong_WeiZhuBenTuiZhuList.size() > 0) {
            userConfig.qiYong_WeiZhuBenTuiZhu = qiYong_WeiZhuBenTuiZhuList.get(0).equals("1");
        } else {
            userConfig.qiYong_WeiZhuBenTuiZhu = false;
        }
        //微注泵推注 用法
        userConfig.weiZhuBenTuiZhu_YaoPinYongFa = UserConfig.DEFAULT_STRING_NEGATIVE;
        if (userConfig.qiYong_WeiZhuBenTuiZhu) {
            List<String> weiZhuBenTuiZhu_YaoPinYongFaList = systemParamService.getUserParams("1", "IENR", "IENR_WZBTYFDM", jgid, DataSource.MOB).datalist;
            if (weiZhuBenTuiZhu_YaoPinYongFaList != null && weiZhuBenTuiZhu_YaoPinYongFaList.size() > 0) {
                userConfig.weiZhuBenTuiZhu_YaoPinYongFa = weiZhuBenTuiZhu_YaoPinYongFaList.get(0);
            }
        }

        List<String> qiYong_SY_CancelToIngOperationList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_QYQXDZXZ", jgid, DataSource.MOB).datalist;
        if (qiYong_SY_CancelToIngOperationList != null && qiYong_SY_CancelToIngOperationList.size() > 0) {
            userConfig.qiYong_SY_CancelToIngOperation = qiYong_SY_CancelToIngOperationList.get(0).equals("1");
        } else {
            userConfig.qiYong_SY_CancelToIngOperation = true;//默认启用
        }

        List<String> qiYong_SY_CancelToStartOperationList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_QYQXZX", jgid, DataSource.MOB).datalist;
        if (qiYong_SY_CancelToStartOperationList != null && qiYong_SY_CancelToStartOperationList.size() > 0) {
            userConfig.qiYong_SY_CancelToStartOperation = qiYong_SY_CancelToStartOperationList.get(0).equals("1");
        } else {
            userConfig.qiYong_SY_CancelToStartOperation = true;//默认启用
        }
        List<String> qiYong_KF_CancelToStartOperationList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_QYQXZX_FIXME", jgid, DataSource.MOB).datalist;
        if (qiYong_KF_CancelToStartOperationList != null && qiYong_KF_CancelToStartOperationList.size() > 0) {
            userConfig.qiYong_KF_CancelToStartOperation = qiYong_KF_CancelToStartOperationList.get(0).equals("1");
        } else {
            userConfig.qiYong_KF_CancelToStartOperation = true;//默认启用
        }

        List<String> qiYong_ZS_CancelToStartOperationList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_QYQXZX_FIXME", jgid, DataSource.MOB).datalist;
        if (qiYong_ZS_CancelToStartOperationList != null && qiYong_ZS_CancelToStartOperationList.size() > 0) {
            userConfig.qiYong_ZS_CancelToStartOperation = qiYong_ZS_CancelToStartOperationList.get(0).equals("1");
        } else {
            userConfig.qiYong_ZS_CancelToStartOperation = true;//默认启用
        }
        List<String> qiYong_SY_LSBS_ShowList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_YZZXLSBS", jgid, DataSource.MOB).datalist;
        if (qiYong_SY_LSBS_ShowList != null && qiYong_SY_LSBS_ShowList.size() > 0) {
            userConfig.qiYong_SY_LSBS_Show = qiYong_SY_LSBS_ShowList.get(0).equals("1");
        } else {
            userConfig.qiYong_SY_LSBS_Show = true;//默认启用
        }

        List<String> qiYong_Only_Sync_24List = systemParamService.getUserParams("1", "IENR", "IENR_PDA_qiYong_Only_Sync_24List", jgid, DataSource.MOB).datalist;
        if (qiYong_Only_Sync_24List != null && qiYong_Only_Sync_24List.size() > 0) {
            userConfig.qiYong_Only_Sync_24 = qiYong_Only_Sync_24List.get(0).equals("1");
        } else {
            userConfig.qiYong_Only_Sync_24 = false;//默认不启用
        }

        List<String> syncSaveJHHList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_syncSaveJHHList", jgid, DataSource.MOB).datalist;
        if (syncSaveJHHList != null && syncSaveJHHList.size() > 0) {
            userConfig.syncSaveJHH = syncSaveJHHList.get(0).equals("1");
        } else {
            userConfig.syncSaveJHH = false;//默认不启用
        }
        List<String> syncDealYZMCList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_syncDealYZMCList", jgid, DataSource.MOB).datalist;
        if (syncDealYZMCList != null && syncDealYZMCList.size() > 0) {
            userConfig.syncDealYZMC = syncDealYZMCList.get(0).equals("1");
        } else {
            userConfig.syncDealYZMC = false;//默认不启用
        }
        //================================== comm ==============================
        List<String> navMenuShowByIconList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_DHTBXS", jgid, DataSource.MOB).datalist;
        if (navMenuShowByIconList != null && !navMenuShowByIconList.isEmpty()) {
            userConfig.navMenuShowByIcon = "1".equals(navMenuShowByIconList.get(0));
        }
        userConfig.navMenuShowByIcon = true;
        //
        List<String> floatMenuShowByIconList = systemParamService.getUserParams("1", "IENR", "IENR_PDA_ANTBXS", jgid, DataSource.MOB).datalist;
        if (floatMenuShowByIconList != null && !floatMenuShowByIconList.isEmpty()) {
            userConfig.floatMenuShowByIcon = "1".equals(floatMenuShowByIconList.get(0));
        }
        userConfig.floatMenuShowByIcon = false;

        //
        return userConfig;
    }


}
