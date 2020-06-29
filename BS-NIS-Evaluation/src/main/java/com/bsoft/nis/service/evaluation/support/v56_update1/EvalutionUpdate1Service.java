package com.bsoft.nis.service.evaluation.support.v56_update1;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.evaluation.evalnew.EvalutionRecord;
import com.bsoft.nis.domain.evaluation.evalnew.EvalutionRecordDetail;
import com.bsoft.nis.mapper.evaluation.v56_update1.EvalutionRecordDetailMapper;
import com.bsoft.nis.mapper.evaluation.v56_update1.EvalutionRecordMapper;
import com.bsoft.nis.mapper.evaluation.v56_update1.EvalutionStyleMapper;
import com.bsoft.nis.mapper.evaluation.v56_update1.EvalutionStyleProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;

/**
 * description:重新设计护理评估单：
 * 1.支持无限级项目
 * 2.支持自伸缩
 * create by: dragon xinghl@bsoft.com.cn
 * create time:2017/11/24 9:36
 * since:5.6 update1
 */
@Service
public class EvalutionUpdate1Service extends RouteDataSourceService{
    @Autowired
    EvalutionStyleMapper styleMapper;
    @Autowired
    EvalutionStyleProjectMapper styleProjectMapper;
    @Autowired
    EvalutionRecordMapper recordMapper;
    @Autowired
    EvalutionRecordDetailMapper recordDetailMapper;

    /**
     * 新增评估记录
     * @param record
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(rollbackFor = Exception.class)
    public void addEvalutionRecord(EvalutionRecord record) throws SQLException,DataAccessException{
        recordMapper.insertSelective(record);
        for (EvalutionRecordDetail detail:record.getDetails()){
            recordDetailMapper.insertSelective(detail);
        }
    }

    /**
     * 更新评估记录和记录明细
     * @param record
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEvalutionRecord(EvalutionRecord record) throws SQLException,DataAccessException{
        for (EvalutionRecordDetail detail:record.getDetails()){
            if (detail.getStatus().equals("add")){
                recordDetailMapper.insertSelective(detail);
            }else if (detail.getStatus().equals("update")){
                recordDetailMapper.updateByPrimaryKeySelective(detail);
            }else if (detail.getStatus().equals("delete")){
                recordDetailMapper.deleteByPrimaryKey(detail.getMXXH());
            }
        }
    }

    /**
     * 更新签名护士
     * @param jlxh
     * @param usercode
     * @param username
     * @param now
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateNurseSign(String jlxh, String usercode, String username, Date now) throws SQLException,DataAccessException{
        recordMapper.updateNurseSign(jlxh,usercode,username,now);
    }

    /**
     * 更新签名护士长
     * @param jlxh
     * @param usercode
     * @param username
     * @param now
     * @throws SQLException
     * @throws DataAccessException
     */
    public void updatePNurseSign(String jlxh, String usercode, String username, Date now) throws SQLException,DataAccessException{
        recordMapper.updatePNurseSign(jlxh,usercode,username,now);
    }

    public void clearSignInfo(String jlxh, Integer who) throws SQLException,DataAccessException{
        recordMapper.clearSignInfo(jlxh,who);
    }
}
