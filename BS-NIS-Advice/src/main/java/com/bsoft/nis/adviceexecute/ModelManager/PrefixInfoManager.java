package com.bsoft.nis.adviceexecute.ModelManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.bsoft.nis.domain.adviceexecute.PrefixInfo;
import com.bsoft.nis.domain.core.barcode.Barcode;
import com.bsoft.nis.service.user.support.UserServiceSup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-21
 * Time: 14:23
 * Version:
 */
@Component
public class PrefixInfoManager {

    private Log logger = LogFactory.getLog(PrefixInfoManager.class);

    @Autowired
    UserServiceSup service;

    private Map prefixInfoMap;//参数对象Map

    //参数对象Map
    public Map<String, List<PrefixInfo>> getPrefixInfoMap() {
        if (prefixInfoMap == null) {
            InitPrefixInfo();
        }

        return prefixInfoMap;
    }

    public void InitPrefixInfo() {
        prefixInfoMap = new HashMap();
        List<Barcode> tmsdList = null;
        try {
            tmsdList = service.GetTmzd();
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (tmsdList != null) {
            /**
             * 归属类型 1 护理治疗 2 标本采样 3 口服用药 4 静脉输液 5 注射用药 6 自理用药 7 体征采集 9 其它医嘱
             */
            String[] gslxArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
            for (String gslx : gslxArray) {
                List<PrefixInfo> list = new ArrayList<>();
                for (Barcode item : tmsdList) {
                    if (gslx.equals(item.FLBS)) {
                           /*升级编号【56010043】============================================= start
                   条码扫描支持长度
            ================= Classichu 2018/03/07 9:34
            */
                        PrefixInfo prefixInfo = new PrefixInfo(item.TMQZ, item.JGID, item.TMFL, item.GZNR);
                        /* =============================================================== end */
                        list.add(prefixInfo);
                    }
                }
                prefixInfoMap.put(gslx, list);
            }
        }
    }
}
