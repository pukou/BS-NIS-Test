package com.bsoft.nis.service.nurserecord.support;

import com.bsoft.nis.domain.nurserecord.db.NRItem;
import com.bsoft.nis.domain.nurserecord.db.Structure;
import com.bsoft.nis.domain.nurserecord.db.Template;
import com.bsoft.nis.mapper.nurserecord.NurseRecordConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 *护理记录配置服务
 * Describtion:
 * Created: dragon
 * Date： 2016/10/20.
 */
@Service
public class NurseRecordConfigServiceSup {
    @Autowired
    NurseRecordConfigMapper mapper;

    /**
     * 获取护理记录类型列表
     * @param bqid
     * @param jgid
     * @param sysType
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Structure> getNurseRecordStructureList(String bqid, String jgid, int sysType)
            throws SQLException,DataAccessException{
        return mapper.getNurseRecordStructureList(bqid,jgid,sysType);
    }

    /**
     * 获取获取护理记录病例类别
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Structure> getStructrueListForNurse()
            throws SQLException,DataAccessException{
        return mapper.getStructrueListForNurse();
    }

    /**
     * 获取护理记录模板(ENR_JG01)
     * @param lbbh
     * @param jgid
     * @param sysType
     * @param bqid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Template> getNurseRecordTemplateList(String bqid,String lbbh, String jgid, int sysType)
            throws SQLException,DataAccessException{
        return mapper.getNurseRecordTemplateList(lbbh, jgid, sysType, bqid);
    }

    /**
     * 根据结构编号获取记录模板(ENR_JG01)
     * @param jgbh
     * @return
     */
    public List<Template> getNurseRecordTemplateByJgbh(String jgbh)
            throws SQLException,DataAccessException{
        return mapper.getNurseRecordTemplateByJgbh(jgbh);
    }

    /**
     * 根据结构编号获取项目数据(ENR_JG02)
     * @param jgbh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<NRItem> getNurseReocrdItemByJgbh(String jgbh,String jgid)
            throws SQLException,DataAccessException{
        return mapper.getNurseReocrdItemByJgbh(jgbh,jgid);
    }
}
