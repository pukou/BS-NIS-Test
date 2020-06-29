package com.bsoft.nis.service.skintest;


import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.skintest.SickerPersonSkinTest;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.service.skintest.support.SkinTestBaseServiceSup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Classichu on 2017/11/16.
 * 中医护理
 */
@Service
public class SkinTestBaseService extends RouteDataSourceService {

    @Autowired
    SkinTestBaseServiceSup serviceSup;

    @Autowired
    IdentityService identityService;

    @Autowired
    PatientMainService patientService;

    @Autowired
    DateTimeService timeService; // 日期时间服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    public BizResponse<SickerPersonSkinTest> getSkinTest(String zyh, String type, String brbq, String jgid) {
        BizResponse<SickerPersonSkinTest> response = new BizResponse<>();
        try {
            List<SickerPersonSkinTest> skinTestList = null;
            if ("1".equals(type)) {
                skinTestList = serviceSup.getAllSkinTest(zyh, brbq, jgid);
            } else if ("2".equals(type)) {
                skinTestList = serviceSup.getNeedSkinTest(zyh, brbq, jgid);
            }

            for (SickerPersonSkinTest sickerPersonSkinTest : skinTestList) {
                if (sickerPersonSkinTest.KSGH != null) {
                    sickerPersonSkinTest.KSR = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, sickerPersonSkinTest.KSGH);
                }
                if (sickerPersonSkinTest.JSGH != null) {
                    sickerPersonSkinTest.JSR = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, sickerPersonSkinTest.JSGH);
                }
            }

            response.datalist = skinTestList;
            response.isSuccess = true;
            response.message = "病人皮试记录列表获取成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【病人皮试记录列表】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【病人皮试记录列表】服务内部错误";
        }
        return response;
    }

    public BizResponse<String> saveSkinTest(SickerPersonSkinTest skinTest) {

        BizResponse<String> response = new BizResponse<>();
        try {
            Integer edit = serviceSup.updateSkinTest(skinTest);
            //修改成功 && 皮试完成后
            if (edit > 0 && "1".equals(skinTest.PSZT)) {
                //调用存储过程 SP_MOB_HIS_UPDATE_PSJG
                Map<String, Object> zxparms = new HashMap<>();
                zxparms.put("VN_YZXH", skinTest.YZXH);
                zxparms.put("VN_PSJG", skinTest.PSJG);
                zxparms.put("VN_YSYZBH", skinTest.YSYZBH);
                //
                zxparms.put("VN_RET", 0);
                zxparms.put("VV_RETMSG", "执行失败");
                Map<String, String> map = serviceSup.update_ZYBQYZ_PSJG(zxparms);
                if ("0".equals(map.get("code"))) {
                    response.isSuccess = false;
                    response.message = "数据已保存:" + map.get("message");
                    return response;
                }
                //
                response.data = "保存成功";
                response.isSuccess = true;
                response.message = "保存成功!";
            } else {
                response.data = "保存失败";
                response.isSuccess = false;
                response.message = "保存失败!";
            }
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【病人皮试记录保存】数据库查询错误";
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【病人皮试记录保存】服务内部错误";
        }
        return response;
    }

}
