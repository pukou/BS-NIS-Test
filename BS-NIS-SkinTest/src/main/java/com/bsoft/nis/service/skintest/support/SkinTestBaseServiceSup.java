package com.bsoft.nis.service.skintest.support;


import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.skintest.SickerPersonSkinTest;
import com.bsoft.nis.mapper.skintest.SkinTestBaseServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Classichu on 2017/11/16.
 * 中医护理
 */
@Service
public class SkinTestBaseServiceSup extends RouteDataSourceService {

    @Autowired
    SkinTestBaseServiceMapper mapper;


    public List<SickerPersonSkinTest> getAllSkinTest(String zyh, String brbq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getAllSkinTest(zyh, brbq, jgid);
    }

    public List<SickerPersonSkinTest> getNeedSkinTest(String zyh, String brbq, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getNeedSkinTest(zyh, brbq, jgid);
    }

    public Integer updateSkinTest(SickerPersonSkinTest skinTest)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = getCurrentDataSourceDBtype();
        skinTest.dbtype = dbtype;
        return mapper.updateSkinTest(skinTest);
    }

    public Map<String, String> update_ZYBQYZ_PSJG(Map<String, Object> zxparms)
            throws SQLException, DataAccessException {
        Map<String, String> map = new HashMap<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            List<?> results = mapper.update_ZYBQYZ_PSJG(zxparms);
            String code = zxparms.get("VN_RET").toString();
            if (code != null && code.equals("1")) {
                map.put("code", "1");
                map.put("messge", "执行成功");
                return map;
            }
            String message = zxparms.get("VV_RETMSG") == null ? "保存失败" : zxparms.get("VV_RETMSG").toString();
            map.put("code", "0");
            map.put("message", message);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
