package com.bsoft.nis.service.user.support;

import com.bsoft.nis.domain.office.AreaVo;
import com.bsoft.nis.mapper.user.OfficeServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * 科室服务
 * Created by Administrator on 2016/10/17.
 */
@Service
public class OfficeServiceSup {

    @Autowired
    OfficeServiceMapper mapper;

    /**
     * 获取护士病区权限
     *
     * @param ygdm
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public List<AreaVo> getOfficesByYGDM(String ygdm, String jgid) throws SQLException, DataAccessException {
        return mapper.getOfficesByYGDM(ygdm, jgid);
    }
    /**
     * 获取条码设定信息
     *
     * @param jgid
     * @return
     */
    public List<AreaVo> getAreaVoForSurgery(String jgid)
            throws SQLException, DataAccessException {
        return mapper.getAreaVoForSurgery(jgid);
    }
}
