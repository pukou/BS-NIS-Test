package com.bsoft.nis.service.lifesign.support;

import com.bsoft.nis.domain.clinicalevent.ClinicalEventInfo;
import com.bsoft.nis.domain.lifesign.*;
import com.bsoft.nis.mapper.lifesign.LifeSignMapper;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Description: 生命体征
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Service
public class LifeSignServiceSup extends RouteDataSourceService {

    @Autowired
    LifeSignMapper mapper;

    String dbType;

    /**
     * 获取病区定制类型列表
     *
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignTypeItem> getLifeSignTypeItemList(String bqid, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignTypeItemList(bqid, jgid);
    }

    /**
     * 获取当前类型下的控件列表
     *
     * @param lbh 类别编号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignInputItem> getLifeSignInputItemList(String lbh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignInputItemList(lbh);
    }

    public List<LifeSignInputItem> getAllLifeSignInputItemList()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getAllLifeSignInputItemList();
    }

    /**
     * 根据输入序号获取输入控件
     *
     * @param srxh 输入序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public LifeSignInputItem getLifeSignInputItem(String srxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignInputItem(srxh);
    }

    /**
     * 获取当前类型下的控件列表
     *
     * @param srxh 输入控件
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignControlItem> getLifeSignControlItemList(String srxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignControlItemList(srxh);
    }

    public List<LifeSignOptionItem> getAllLifeSignOptionItemList()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getAllLifeSignOptionItemList();
    }

    public List<LifeSignControlItem> getAllLifeSignControlItemList()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getAllLifeSignControlItemList();
    }

    /**
     * 获取当前类型下的控件列表
     *
     * @param kjh 控件号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignOptionItem> getLifeSignOptionItemList(String kjh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignOptionItemList(kjh);
    }

    /**
     * 获取当前类型下的控件列表
     *
     * @param kjh 控件号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignOptionItem> getLifeSignSpecialOptionItemList(String kjh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignSpecialOptionItemList(kjh);
    }

    /**
     * 获取当前类型下的控件列表
     *
     * @param tsbs 特殊标识
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getLifeSignKjlxByTsbs(String tsbs)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignKjlxByTsbs(tsbs);
    }

    /**
     * 获取当前类型下的控件列表
     *
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignQualityInfo> getQualityInfoList()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getQualityInfoList();
    }

    /**
     * 按照采集组号删除生命体征数据
     *
     * @param cjzh 采集组号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Integer deleteSmtzByCjzh(String cjzh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.deleteSmtzByCjzh(cjzh);
    }

    /**
     * 通过特殊标识获取类别标志
     *
     * @param tsbs 特殊标识
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getLifeSignLbbzByTsbs(String tsbs)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignLbbzByTsbs(tsbs);
    }

    /**
     * 通过项目号获取体温单显示标志：1表示默认显示到体温单中 0否
     *
     * @param xmh 项目号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getLifeSignTwdxsByXmh(String xmh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignTwdxsByXmh(xmh);
    }

    /**
     * 批量保持生命体征数据
     *
     * @param lifeSignRealSaveDataItemList 对象列表
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addLifeSignDataBatch(List<LifeSignRealSaveDataItem> lifeSignRealSaveDataItemList)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addLifeSignDataBatch(lifeSignRealSaveDataItemList, dbType);
    }

    /**
     * 获取生命体征历史数据
     *
     * @param zyh   住院号
     * @param start 开始日期
     * @param end   结束日期
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignHistoryDataItem> getLifeSignHistoryDataItem(String zyh, String start, String end)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getLifeSignHistoryDataItem(zyh, start, end, dbType);
    }

    /**
     * 获取生命体征历史数据类型
     *
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignHistoryDataType> getLifeSignHistoryDataType()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignHistoryDataType();
    }

    /**
     * 删除(作废)生命体征数据
     *
     * @param cjh 采集号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteLifeSignHistoryData(String cjh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.deleteLifeSignHistoryData(cjh);
    }
    public int updateLifeSignHistoryData(String cjh,String value)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.updateLifeSignHistoryData(cjh,value);
    }
    /**
     * 获取最新采集时间
     *
     * @param tzxm 体征项目
     * @param zyh  住院号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public Date getLifeSignDoubleCheckMaxCjsj(String tzxm, String zyh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getLifeSignDoubleCheckMaxCjsj(tzxm, zyh);
    }

    /**
     * 获取生命体征复测历史数据
     *
     * @param tzxm  体征项目
     * @param zyh   住院号
     * @param start 开始时间
     * @param end   结束时间
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignDoubleCheckHistoryDataItem> getLifeSignDoubleCheckHistoryDataItem(String tzxm, String zyh, String start, String end)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getLifeSignDoubleCheckHistoryDataItem(tzxm, zyh, start, end, dbType);
    }

    /**
     * 获取生命体征复测降温措施
     *
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignDoubleCheckCoolingMeasure> getLifeSignDoubleCheckCoolingMeasure()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getLifeSignDoubleCheckCoolingMeasure();
    }

    /**
     * 获取体征特殊校验处理大便数据
     *
     * @param zyh   住院号
     * @param start 开始时间
     * @param end   结束时间
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<LifeSignDoubleCheckHistoryDataItem> getLifeSignSpecialDataItem(String zyh, String start, String end)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        dbType = getCurrentDataSourceDBtype();
        return mapper.getLifeSignSpecialDataItem(zyh, start, end, dbType);
    }

    /**
     * 获取体征特殊校验处理大便数据
     *
     * @param zyh  住院号
     * @param sjfl 事件分类
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<ClinicalEventInfo> getClinicalEventInfoList(String zyh, String sjfl)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getClinicalEventInfoList(zyh, sjfl);
    }

    /**
     * 新增临床事件信息
     *
     * @param clinicalEventInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addClinicalEventInfo(ClinicalEventInfo clinicalEventInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        dbType = getCurrentDataSourceDBtype();
        clinicalEventInfo.dbtype = dbType;
        return mapper.addClinicalEventInfo(clinicalEventInfo);
    }

    /**
     * 修改临床事件信息
     *
     * @param clinicalEventInfo
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editClinicalEventInfo(ClinicalEventInfo clinicalEventInfo)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        dbType = getCurrentDataSourceDBtype();
        clinicalEventInfo.dbtype = dbType;
        return mapper.editClinicalEventInfo(clinicalEventInfo);
    }

    /**
     * 删除临床事件信息
     *
     * @param sjxh
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteClinicalEventInfo(String sjxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.deleteClinicalEventInfo(sjxh);
    }

    /**
     * 获取死亡时间
     *
     * @param zyh 住院号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getClinicalEventDieInfo(String zyh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.HRP);
        return mapper.getClinicalEventDieInfo(zyh);
    }

    /**
     * 获取分娩时间
     *
     * @param zyh 住院号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<String> getClinicalEventChildbirthInfo(String zyh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.ENR);
        return mapper.getClinicalEventChildbirthInfo(zyh);
    }

	/**
	 * 根据cjh获取cjzh
	 * @param cjh
	 * @param jgid
	 * @return
	 */
	public String getCjzhByCjh(String cjh, String jgid) {
		keepOrRoutingDateSource(DataSource.ENR);
		return mapper.getCjzhByCjh(cjh, jgid);
	}

    public List<String> getZKXM(String zyh, Date start, Date end)
            throws SQLException, DataAccessException{
        return mapper.getZKXM(zyh,start,end);
    }

    public List<LifeSignHistoryInfo> getLifeSignHistoryInfo(String zyh, String xmh) throws SQLException, DataAccessException{
	    keepOrRoutingDateSource(DataSource.ENR);
	    String dbtype = this.getCurrentDataSourceDBtype();

	    return mapper.getLifeSignHistoryInfo(zyh, xmh, dbtype);
    }
}
