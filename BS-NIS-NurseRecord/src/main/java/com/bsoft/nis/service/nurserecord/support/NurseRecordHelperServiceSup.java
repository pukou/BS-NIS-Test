package com.bsoft.nis.service.nurserecord.support;

import com.bsoft.nis.domain.nurserecord.db.HelpLeaf;
import com.bsoft.nis.domain.nurserecord.db.HelpTree;
import com.bsoft.nis.mapper.nurserecord.NurseRecordHelperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Describtion:护理记录助手子服务
 * Created: dragon
 * Date： 2016/11/23.
 */
@Service
public class NurseRecordHelperServiceSup {
    @Autowired
    NurseRecordHelperMapper mapper;

    /**
     * 获取助手目录(EMR_MLLB)
     *
     * 协和客户化
     * 旧版本EMR_MLLB在his库
     * 新版本ENR_MLLB在enr库
     *
     * @return
     */
    public List<HelpTree> getHelpContent() throws SQLException, DataAccessException {
        return mapper.getHelpContent();
    }

    /**
     * 获取助手目录子目录
     *
     * @param ysbh
     * @param xmbh
     * @param jgbh
     * @param mlbh
     * @param ygdm
     * @param ksdm
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HelpLeaf> getHelperItem(String ysbh, String xmbh, String jgbh, String mlbh, String ygdm, String ksdm, String jgid)
            throws SQLException, DataAccessException {
        ksdm = "%," + ksdm + ",%";
        return mapper.getHelperItem(ysbh, xmbh, jgbh, mlbh, ygdm, ksdm, jgid);
    }
}
