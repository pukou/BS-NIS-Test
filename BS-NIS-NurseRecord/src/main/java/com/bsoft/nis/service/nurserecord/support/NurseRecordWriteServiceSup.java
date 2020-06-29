package com.bsoft.nis.service.nurserecord.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.DataSourceContextHolder;
import com.bsoft.nis.core.datasource.MultiRoutingDataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.nurserecord.LastDataBean;
import com.bsoft.nis.domain.nurserecord.RefrenceValue;
import com.bsoft.nis.domain.nurserecord.db.*;
import com.bsoft.nis.mapper.nurserecord.NurseRecordInterface;
import com.bsoft.nis.mapper.nurserecord.NurseRecordWriteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/10/20.
 */
@Service
public class NurseRecordWriteServiceSup extends RouteDataSourceService {

    @Autowired
    NurseRecordWriteMapper mapper;
    @Autowired
    List<NurseRecordInterface> mappers;

    @Autowired
    MultiRoutingDataSource dataSource;

    /**
     * 获取当前记录的活动列
     * @param jgid
     * @param jlbh
     * @param zyh
     * @param nowStr
     * @return
     */
    public List<Controll> getActivieCtrlListByJlbh(String jgid, String jlbh, String zyh, String nowStr)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getActivieCtrlListByJlbh(jgid, jlbh, zyh, nowStr,dbtype);
    }

    /**
     * 获取当前结构的活动列
     * @param jgid
     * @param jgbh
     * @param zyh
     * @param nowStr
     * @return
     */
    public List<Controll> getActivieCtrlListByJgbh(String jgid, String jgbh, String zyh, String nowStr)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getActivieCtrlListByJgbh(jgid, jgbh, zyh, nowStr,dbtype);
    }

    /**
     * 当前记录的控件列表
     * @param jgid
     * @param jlbh
     * @return
     */
    public List<Controll> getCtrlListByJlbh(String jgid, String jlbh)
            throws SQLException,DataAccessException{
        return mapper.getCtrlListByJlbh(jgid, jlbh);
    }

    /**
     * 当前结构的控件列表
     * @param jgid
     * @param jgbh
     * @return
     */
    public List<Controll> getCtrlListByJgbh(String jgid, String jgbh)
            throws SQLException,DataAccessException{
        return mapper.getCtrlListByJgbh(jgid, jgbh);
    }
  /*
        升级编号【56010022】============================================= start
        护理记录:可以查看项目最近3次的记录，可以选择其中一次的数据到当前的护理记录单上。
        ================= Classichu 2017/10/18 10:41
        */
  public List<LastDataBean> getlastXMData(String jgid, String xmbh, String zyh,String hsgh)
          throws SQLException,DataAccessException{
      return mapper.getlastXMData(jgid, xmbh,hsgh,zyh);
  }
  /* =============================================================== end */
    /**
     * 获取风险评估引用数据
     * @param zyh
     * @param pageIndex
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<RefrenceValue> getDangerRefrence(String zyh, int pageIndex, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getDangerRefrence(zyh, pageIndex, jgid);
    }

    /**
     * 获取生命体征引用数据
     * @param zyh
     * @param pageIndex
     * @param jgid
     * @return
     */
    public List<RefrenceValue> getLifeSignRefrece(String zyh, int pageIndex, String jgid,String xmh)
            throws SQLException,DataAccessException{
        return mapper.getLifeSignRefrece(zyh, pageIndex, jgid, xmh);
    }

    /**
     * 获取风险评估记录总数
     * @param zyh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<Map> getDangerRecordSum(String zyh)
            throws SQLException,DataAccessException{
        return mapper.getDangerRecordSum(zyh);
    }

    /**
     * 获取生命体征记录总数
     * @param zyh
     * @param yskz
     * @param jgid
     * @return
     */
    public List<Map> getLifeSignRecordSum(String zyh, String yskz, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getLifeSignRecordSum(zyh, yskz, jgid);
    }

    /**
     * 根据住院号获取目录
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<NRDbTree> getNRTree(String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getNRTree(zyh, jgid);
    }

    /**
     * 根据模板类型获取记录目录
     * @param zyh
     * @param jgid
     * @param mblx
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<NRDbTree> getNRTreeByMblx(String zyh, String jgid, String mblx)
            throws SQLException,DataAccessException{
        return mapper.getNRTreeByMblx(zyh, jgid, mblx);
    }

    /**
     * 根据住院号和结构编号获取记录目录
     * @param zyh
     * @param jgbh
     * @return
     */
    public List<Map> getNRTreeByZYHAndJGBH(String zyh, String jgbh)
            throws SQLException,DataAccessException{
        return mapper.getNRTreeByZYHAndJGBH(zyh, jgbh);
    }

    /**
     * 新增护理记录
     * @param saveData
     * @param nrItems
     * @param contents
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveNurseRecord(NRData saveData, List<NRItem> nrItems, List<NRContent> contents)
            throws SQLException,DataAccessException{
        // 新增ENR_JL01
        String dbtype = getCurrentDataSourceDBtype();
        saveData.dbtype = dbtype;
        mapper.addNurseRecord(saveData);

        // 新增ENR_JL02
        for (NRItem item:nrItems){
            mapper.addNurseRecordDetail(item);
        }

        // 新增ENR_JLML
        for (NRContent content:contents){
            mapper.addNurseRecordContent(content);
        }
    }

    /**
     * 根据jlbh获取ENR_JL01数据
     * @param jlbh
     * @param jgid
     * @return
     */
    public List<NRData> getNRData(String jlbh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getNRData(jlbh,jgid);
    }

    /**
     * 根据住院号、机构编号获取记录目录列表
     * @param zyh
     * @param jgbh
     * @param jgid
     * @return
     */
    public List<NRContent> getNRContents(String zyh, String jgbh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getNRContents(zyh, jgbh, jgid);
    }

    /**
     * 更新护理记录数据
     * @param updatePrimaryRecords
     * @param insertContents
     * @param insertItems
     * @param updateItems
     * @param JLBH
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateNurseRecord(List<NRData> updatePrimaryRecords, List<NRContent> insertContents, List<NRItem> insertItems, List<NRItem> updateItems, String JLBH)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        // 更新ENR_JL01
        for (NRData nrData:updatePrimaryRecords){
            nrData.dbtype = dbtype;
            mapper.updatePrimaryRecord(nrData);
        }
        // 更新主记录[记录行数]
        mapper.updatePrimaryRecordLines(JLBH);

        // 新增ENR_JLML
        for (NRContent content:insertContents){
            mapper.addNurseRecordContent(content);
        }
        // 新增ENR_JL02
        for (NRItem item:insertItems){
            mapper.addNurseRecordDetail(item);
        }
        // 更新ENR_JL02
        for (NRItem item:updateItems){
            mapper.updateNurseRecordDetail(item);
        }
    }

    /**
     * 删除护理记录数据
     * @param jlbh
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Integer deleteNurseRecord(String jlbh, String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.deleteNurseRecord(jlbh,zyh,jgid);
    }

    /**
     * 获取用户签名信息
     * @param yhid
     * @return
     */
    public List<EMR_WH_QMXX> getSignValids(String yhid)
            throws SQLException,DataAccessException{
        return mapper.getSignValids(yhid);
    }

    /**
     * 签名护理记录
     * @param jlxh
     * @param nowStr
     * @param jlbh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Integer signNameNurseRecord(String jlxh, String nowStr, String jlbh, String jgid)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.signNameNurseRecord(jlxh, nowStr, jlbh, jgid,dbtype);
    }

    /**
     * 获取病人过敏药物，用于引用
     * @param zyh
     * @param startime
     * @param endStr
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<DrugMedicalAdviceRefContent> getGrugMedicalAdvices(String zyh, String startime, String endStr, String jgid)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getGrugMedicalAdvices(zyh, startime, endStr, jgid,dbtype);
    }

    /**
     * 福建协和客户化：通过药品序号获取药品简称
     * @param ypxh
     * @return
     * @throws DataAccessException
     */
    public String getYPJCForYPXH(String ypxh)
            throws DataAccessException{
        return mapper.getYPJCForYPXH(ypxh);
    }

    /**
     * 获取手术引用
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<OperationRefContent> getOperationRefs(String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.getOperationRefs(zyh, jgid);
    }

    /**
     * 获取生命体征引用
     * @param zyh
     * @param startime
     * @param endStr
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SignRefContent> getLifeSignRefs(String zyh, String startime, String endStr, String jgid)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.getLifeSignRefs(zyh, startime, endStr, jgid,dbtype);
    }

    /**
     * TODO:测试用，删除
     * @param ksrq
     * @param jsrq
     * @return
     */
    public List<Map> getLzjh(String ksrq, String jsrq) {

        // get current datasource and database type
        String dbtype = DataSourceContextHolder.getDataBaseTypeName();
        dbtype = getCurrentDataSourceDBtype();
        NurseRecordInterface mapper = mappers.get(2);
        String name = mapper.getClass().getName();
        Class _class = mapper.getClass();
        return mappers.get(2).getLzjh(ksrq, jsrq);
    }

    public String getMergeRule(String jgbh, String zyh, String yhid, String jlsj) {
        String dbtype = DataSourceContextHolder.getDataBaseTypeName();
        return mapper.getMergeRule(jgbh, zyh, yhid, jlsj, dbtype);
    }

	public List<NRItem> getNRItemsForSync(String jlbh) throws SQLException,DataAccessException {
		return mapper.getNRItemsForSync(jlbh);
	}

	public Integer updateNRItemsForSyncDel(List<Long> mxbhList) {
		return mapper.updateNRItemsForSyncDel(mxbhList);
	}

	public Integer updateNRDataForSyncDel(String jlbh) {
		return mapper.updateNRDataForSyncDel(jlbh);
	}

    public List<NRData> getNRDatas(String zyh, String jgbh, String nowStr) {
        String dbtype = this.getCurrentDataSourceDBtype();
        return mapper.getNRDatas(zyh, jgbh, nowStr, dbtype);
    }

    public List<Map> getExsitProjectsInRecord(List<String> xms, String jlbh)
            throws SQLException,DataAccessException{
        Map map = new HashMap<>();
        map.put("XMS",xms);
        map.put("JLBH",jlbh);
        return mapper.getExsitProjectsInRecord(map);
    }
}
