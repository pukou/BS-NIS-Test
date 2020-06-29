package com.bsoft.nis.common.servicesup.support;

import com.bsoft.nis.common.servicesup.mapper.SystemParamsMapper;
import com.bsoft.nis.domain.core.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
@Service
public class SystemParamServiceSup {
    @Autowired
    SystemParamsMapper mapper;

    public List<String> getParamsInHRP(String gsjb, String gsdx, String csmc, String jgid) throws SQLException {

        return mapper.getParamsInHRP(gsjb, gsdx, csmc, jgid);
    }

    public List<String> getParamsInMOB(String gsjb, String gsdx, String csmc, String jgid) throws SQLException {

        return mapper.getParamsInMOB(gsjb, gsdx, csmc, jgid);
    }


    public List<UserParams> getParamsListInHRP(String gsjb, String gsdx, String[] csmcs, String jgid)throws SQLException {
        return mapper.getParamsListInHRP(gsjb, gsdx, csmcs, jgid);
    }

    public List<UserParams> getParamsListInMOB(String gsjb, String gsdx, String[] csmcs, String jgid)throws SQLException {
        return  mapper.getParamsListInMOB(gsjb, gsdx, csmcs, jgid);
    }
}
