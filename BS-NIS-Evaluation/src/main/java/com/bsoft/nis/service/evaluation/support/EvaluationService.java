package com.bsoft.nis.service.evaluation.support;


import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dangerevaluate.DERecord;
import com.bsoft.nis.domain.evaluation.EvaluateRecordItem;
import com.bsoft.nis.domain.evaluation.SingleCharacter;
import com.bsoft.nis.domain.evaluation.db.*;
import com.bsoft.nis.domain.evaluation.dynamic.Classification;
import com.bsoft.nis.domain.evaluation.dynamic.Form;
import com.bsoft.nis.domain.evaluation.dynamic.SqlDropData;
import com.bsoft.nis.domain.evaluation.pgnr.EvalFljlVo;
import com.bsoft.nis.domain.evaluation.pgnr.EvalJlxmVo;
import com.bsoft.nis.domain.evaluation.pgnr.EvalXmxxVo;
import com.bsoft.nis.domain.healthguid.HealthGuidData;
import com.bsoft.nis.domain.lifesign.LifeSignHistoryDataItem;
import com.bsoft.nis.mapper.evaluation.EvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 费用服务
 * Created by Administrator on 2016/10/10.
 */
@Service
public class EvaluationService extends RouteDataSourceService {


    private List<DERecord> FXPG;
    @Autowired
    EvaluationMapper mapper;


    /**
     *获取V_MOB_ENR_PGJL 记录数据
     * @param zyh
     * @param start
     * @param end
     * @param jgid
     * @param source
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<EvaluateRecordItem> getRcordData(String zyh, String start, String end, String jgid, String source)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        if(source.equals("EMR")){
            return  mapper.getRcordFromEMR(zyh, start, end, jgid,dbtype);
        }
        return  mapper.getRcordFromMOB(zyh, start, end, jgid,dbtype);
    }
    public List<EvaluateRecordItem> getAllRecordData(String zyh, String jgid, String source)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        if(source.equals("EMR")){
            return  mapper.getRcordFromEMR(zyh, null, null, jgid,dbtype);
        }
        return  mapper.getRcordFromMOB(zyh, null, null, jgid,dbtype);
    }
    public List<EvaluateRecordItem> GetNewEvaluationList(String bqdm, String jgid)
            throws SQLException,DataAccessException{
        return  mapper.GetNewEvaluationList(bqdm, jgid);

    }

    public List<EvaluateRecordItem> GetNewEvaluationListForYslx(String yslx, String bqdm, String jgid)
            throws SQLException,DataAccessException{
        return mapper.GetNewEvaluationListForYslx(yslx, bqdm, jgid);
    }
    //===================更新PGNR============================
//获取分类记录
    public List<EvalFljlVo> getEvalFljl(long jlxh)
            throws SQLException,DataAccessException{
        return mapper.getEvalFljl(jlxh);
    }

    //获取样式项目
    public List<EvalXmxxVo> getEvalYsxm(long ysxh)
            throws SQLException,DataAccessException{
        return mapper.getEvalYsxm(ysxh);
    }

    //获取样式选项
    public List<EvalXmxxVo> getEvalYsxx(long ysxh)
            throws SQLException,DataAccessException{
        return mapper.getEvalYsxx(ysxh);
    }

    //获取项目记录
    public List<EvalJlxmVo> getEvalJlxm(long jlxh)
            throws SQLException,DataAccessException{
        return mapper.getEvalJlxm(jlxh);
    }

    //获取选项记录
    public List<EvalJlxmVo> getEvalJlxx(long jlxh)
            throws SQLException,DataAccessException{
        return mapper.getEvalJlxx(jlxh);
    }
//===================更新PGNR============================
    /**
     * // 获取  数据获取方式 add 2017年4月26日11:16:18
     * @param yslx
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getSJHQFS(String yslx)
            throws SQLException,DataAccessException{
        return mapper.getSJHQFS(yslx);
    }
    /**
     * 获取一条表单记录
     * @param jlxh
     * @return
     */
    public EvaluateBDJLVo getBDJL(String jlxh)
            throws SQLException,DataAccessException{
        return mapper.getBDJL(jlxh);
    }
    /**
     * 获取一条记录的记录项目
     * @param jlxh
     * @return
     */
    public List<EvaluateBDJLXMVo> getBDJLXM(String jlxh,String ysxm)
            throws SQLException,DataAccessException{
        return mapper.getBDJLXM(jlxh, ysxm);
    }

    /**
     * 获取一条记录的记录选项
     * @param jlxh
     * @return
     */
    public List<EvaluateBDJLXXVo> getBDJLXX(String jlxh,String ysxx,String ysxm)
            throws SQLException,DataAccessException{
        return mapper.getBDJLXX(jlxh, ysxx, ysxm);
    }

    /**
     * 获取单个表单样式
     * @param ysxh
     * @param jgid
     * @return
     */
    public Form getBDYS(String ysxh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getBDYS(ysxh, jgid);
    }

    public List<Classification> getYSFL(String ysxh, String jlxh)
            throws SQLException,DataAccessException{
        return mapper.getYSFL(ysxh, jlxh);
    }

    public List<EvaluateBDYSXMVo> getFLXM(String ysxh,String dzlx)
            throws SQLException,DataAccessException{
        return mapper.getFLXM(ysxh,dzlx);
    }

    public List<EvaluateBDYSXXVo> getFLXX(String ysxh,String dzlx)
            throws SQLException,DataAccessException{
        return mapper.getFLXX(ysxh,dzlx);
    }

    public List<LifeSignHistoryDataItem> getSMTZ(String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getSMTZ(zyh, jgid);
    }

    public List<DERecord> getFXPG(String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getFXPG(zyh, jgid);
    }

    public List<HealthGuidData> getJKXJ(String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getJKXJ(zyh, jgid);
    }

    public Integer getBTBZ(String ysxh, Long ysxm, String bqdm, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getBTBZ(ysxh, ysxm, bqdm, jgid);
    }

    public List<SqlDropData> getDropListBySql(String sql)
            throws SQLException,DataAccessException{
        return mapper.getDropListBySql(sql);
    }

    public Boolean addBDJL(EvaluateBDJLVo bdjlVo)
            throws SQLException,DataAccessException{
        bdjlVo.dbtype = getCurrentDataSourceDBtype();
        return mapper.addBDJL(bdjlVo);
    }

    public List<EvaluateBDYSFLVo> getBDYSFL(String ysxh)
            throws SQLException,DataAccessException{
        return mapper.getBDYSFL(ysxh);
    }

    public Boolean addBDJLFL(EvaluateBDJLFLVo jlflVo)
            throws SQLException,DataAccessException{
        return mapper.addBDJLFL(jlflVo);
    }


    public Integer getBDJLCount(String ysxh, String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getBDJLCount(ysxh, zyh, jgid);
    }


    //type 1:更新表单记录 2：保存或取消签名 3:保存审阅或取消审阅 4：作废
    public Boolean updateBDJL(EvaluateBDJLVo bdjlVo)
            throws SQLException,DataAccessException{
        bdjlVo.dbtype = getCurrentDataSourceDBtype();
        return mapper.updateBDJL(bdjlVo);
    }

    public Boolean updateBDJLXM(EvaluateBDJLXMVo jlxmVo)
            throws SQLException,DataAccessException{
        return mapper.updateBDJLXM(jlxmVo);
    }

    public Boolean updateBDJLXX(EvaluateBDJLXXVo xxVo)
            throws SQLException,DataAccessException{
        return mapper.updateBDJLXX(xxVo);
    }

    public Boolean addBDJLXM(EvaluateBDJLXMVo jlxmVo)
            throws SQLException,DataAccessException{
        return mapper.addBDJLXM(jlxmVo);
    }


    public Boolean addBDJLXX(EvaluateBDJLXXVo xxVo)
            throws SQLException,DataAccessException{
        return mapper.addBDJLXX(xxVo);
    }

    public Boolean deleteBDJLXX(String ysxx)
            throws SQLException,DataAccessException{
        return mapper.deleteBDJLXX(ysxx);
    }

    public List<EvaluateBDJLFLVo> getBDJLFL(String jlxh, String ysfl)
            throws SQLException,DataAccessException{
        return mapper.getBDJLFL(jlxh, ysfl);
    }

    //判断表单单一性
    public List<SingleCharacter> getSingle(String bqdm, String ysxh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getSingle(bqdm, ysxh, jgid);
    }

    //type 1:单签 2：双签或取消签名
    public Boolean updateBDJLFL(EvaluateBDJLFLVo jlflVo)
            throws SQLException,DataAccessException{
        jlflVo.dbtype = getCurrentDataSourceDBtype();
        return mapper.updateBDJLFL(jlflVo);
    }


    //获取表单签名信息
    public List<EvaluateBDJLVo> getBDQMXX(String jlxh)
            throws SQLException,DataAccessException{
        return mapper.getBDQMXX(jlxh);
    }

    public Boolean updateBDJLFLPGNR(String flpgnr, String jlxh ,String jlfl)
            throws SQLException,DataAccessException{
        return mapper.updateBDJLFLPGNR(flpgnr,jlxh,jlfl);
    }

    public Boolean updatteBDJLPGNR(String pgnr, String jlxh)
            throws SQLException,DataAccessException{
        return mapper.updatteBDJLPGNR(pgnr, jlxh);
    }

    @Deprecated
    public Boolean updateBDJLFLFLLX(String jlfl, String fllx)
            throws SQLException,DataAccessException{
        return mapper.updateBDJLFLFLLX(jlfl,fllx);
    }
    public Boolean updateBDJLFLFLLXNew(String jlxh,String ysfl, String fllx)
            throws SQLException,DataAccessException{
        return mapper.updateBDJLFLFLLXNew(jlxh,ysfl,fllx);
    }
    public List<LifeSignHistoryDataItem> getSMTZByGroupId(String dzbdjl)
            throws SQLException,DataAccessException{
        return mapper.getSMTZByGroupId(dzbdjl);
    }

    public List<DERecord> getFXPGByPrimaryKey(String dzbdjl)
            throws SQLException,DataAccessException{
        return mapper.getFXPGByPrimaryKey(dzbdjl);
    }
}
