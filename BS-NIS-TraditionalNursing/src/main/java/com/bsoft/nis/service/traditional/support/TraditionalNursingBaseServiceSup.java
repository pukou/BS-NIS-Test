package com.bsoft.nis.service.traditional.support;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.traditional.JSJL;
import com.bsoft.nis.domain.traditional.Traditional_HLFA;
import com.bsoft.nis.domain.traditional.Traditional_SHJS;
import com.bsoft.nis.domain.traditional.Traditional_XGBZ;
import com.bsoft.nis.domain.traditional.Traditional_ZYZZ;
import com.bsoft.nis.domain.traditional.Traditional_ZZFJ;
import com.bsoft.nis.domain.traditional.Traditional_ZZFJJL;
import com.bsoft.nis.domain.traditional.Traditional_ZZJL;
import com.bsoft.nis.domain.traditional._HLJS;
import com.bsoft.nis.mapper.traditional.TraditionalNursingBaseServiceMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Classichu on 2017/11/16.
 * 中医护理
 */
@Service
public class TraditionalNursingBaseServiceSup extends RouteDataSourceService {

    @Autowired
    TraditionalNursingBaseServiceMapper mapper;


    @Transactional(readOnly = true)
    public List<Traditional_ZZJL> getZYZZJL(String zyh, String brbq, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getZY_ZZJL(zyh, brbq, jgid);
    }


    @Transactional(readOnly = true)
    public List<Traditional_ZZFJ> getZYZZFJ(String zzbh)
            throws SQLException, DataAccessException {
        return mapper.getZY_ZZFJ(zzbh);
    }

    @Transactional(readOnly = true)
    public List<String> getZY_RYZD(String zyh)
            throws SQLException, DataAccessException {
        return mapper.getZY_RYZD(zyh);
    }

    @Transactional(readOnly = true)
    public List<JSJL> getZYSHJSJL(String zyh, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getZY_JSJL(zyh, jgid);
    }

    @Transactional(readOnly = true)
    public List<Traditional_HLFA> getZY_HLFA(String jgid)
            throws SQLException, DataAccessException {
        return mapper.getZY_HLFA(jgid);
    }

    @Transactional(readOnly = true)
    public List<Traditional_ZYZZ> getZY_ZYZZ(String fabh)
            throws SQLException, DataAccessException {
        return mapper.getZY_ZYZZ(fabh);
    }

    @Transactional(readOnly = true)
    public List<Traditional_SHJS> getZY_SHJS(String zzbh)
            throws SQLException, DataAccessException {
        return mapper.getZY_SHJS(zzbh);
    }

    @Transactional(readOnly = true)
    public List<_HLJS> getZY_YZJHbyZZBH(String zyh, String jgid, String zzbh, String nowDateStr)
            throws SQLException, DataAccessException {
        return mapper.getZY_YZJHbyZZBH(zyh, jgid, zzbh,nowDateStr);
    }
    @Transactional(readOnly = true)
    public List<Traditional_XGBZ> getZY_XGBZ(String zzbh, String fscz)
            throws SQLException, DataAccessException {
        return mapper.getZY_XGBZ(zzbh,fscz);
    }
    @Transactional(readOnly = true)
    public Integer insertZY_ZZFJJL(Traditional_ZZFJJL traditional_zzfjjl)
            throws SQLException, DataAccessException {
        return mapper.insertZY_ZZFJJL(traditional_zzfjjl);
    }
    @Transactional(readOnly = true)
    public Integer insertZY_JSJL(JSJL jsjl)
            throws SQLException, DataAccessException {
        return mapper.insertZY_JSJL(jsjl);
    }

    @Transactional(readOnly = true)
    public Integer updateZY_ZZJL_PF(String sshjl, String sshfj, String sshpf,
                                    String sshsj, String zzbh, String fajl,String hlxg)
            throws SQLException, DataAccessException {
        return mapper.updateZY_ZZJL_PF(sshjl, sshfj, sshpf, sshsj, zzbh, fajl,hlxg);
    }

}
