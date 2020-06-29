package com.bsoft.nis.service.healthguid.support;

import com.bsoft.nis.domain.healthguid.*;
import com.bsoft.nis.mapper.healthguid.HealthGuidMapper;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Description: 健康教育
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Service
public class HealthGuidServiceSup extends RouteDataSourceService {

    @Autowired
    HealthGuidMapper mapper;

    String dbType;

    /**
     * 获取病区定制样式列表
     *
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuid> getHealthGuidList(String bqid, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidList(bqid, jgid);
    }

    /**
     * 获取指定类型的宣教单
     *
     * @param gllx 关联类型
     * @param glxh 关联序号
     * @param zyh  住院号
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidItem> getHealthGuidItemListForBd(String gllx, String glxh, String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidItemListForBd(gllx, glxh, zyh, bqid, jgid);
    }

    /**
     * 获取指定类型的宣教单
     *
     * @param gllx 关联类型
     * @param ysxh 样式序号
     * @param zyh  住院号
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidItem> getHealthGuidItemListForFl(String gllx, String ysxh, String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidItemListForFl(gllx, ysxh, zyh, bqid, jgid);
    }

    /**
     * 获取记录序号最大值
     *
     * @param glxh 关联序号
     * @param gllx 关联类型
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getMaxJlxh(String glxh, String gllx)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getMaxJlxh(glxh, gllx);
    }

    /**
     * 获取健康宣教 宣教对象
     *
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidOperItem> getHealthGuidXjdx()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidXjdx();
    }

    /**
     * 获取健康宣教 宣教方式
     *
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidOperItem> getHealthGuidXjfs()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidXjfs();
    }

    /**
     * 获取健康宣教 效果评价
     *
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidOperItem> getHealthGuidXgpj()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidXgpj();
    }

    /**
     * 获取健康宣教分类列表
     *
     * @param ysxh 样式序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidType> getHealthGuidTypeListByYsxh(String ysxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidTypeListByYsxh(ysxh);
    }

    /**
     * 获取健康宣教分类列表
     *
     * @param glxh 归类序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidType> getHealthGuidTypeListByGlxh(String glxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidTypeListByGlxh(glxh);
    }

    /**
     * 获取健康宣教项目列表
     *
     * @param glxh 关联序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidDetail> getHealthGuidDetailListByGlxh(String glxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidDetailListByGlxh(glxh);
    }

    /**
     * 获取健康宣教 主记录
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public HealthGuidData getHealthGuidDataByJlxh(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidDataByJlxh(jlxh);
    }

    /**
     * 获取健康宣教项目列表
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<HealthGuidDetail> getHealthGuidDetailListByJlxh(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidDetailListByJlxh(jlxh);
    }

    /**
     * 新增健康教育主记录
     *
     * @param jlxh 记录序号
     * @param zyh  住院号
     * @param bqid 病区id
     * @param glxh 关联序号
     * @param gllx 关联类型
     * @param jlsj 记录时间
     * @param jlgh 记录工号
     * @param cjsj 采集时间
     * @param cjgh 采集工号
     * @param qmsj 签名时间
     * @param qmgh 签名工号
     * @param jgid 机构id
     * @param bzxx 备注信息
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addHealthGuidJkxjjl(String jlxh, String zyh, String bqid, String glxh, String gllx, String jlsj, String jlgh,
                                   String cjsj, String cjgh, String qmsj, String qmgh, String jgid, String bzxx)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addHealthGuidJkxjjl(jlxh, zyh, bqid, glxh, gllx, jlsj, jlgh, cjsj, cjgh, qmsj, qmgh, jgid, bzxx, dbType);
    }

    /**
     * 新增健康教育宣教项目
     *
     * @param jlxm  记录项目
     * @param jlxh  记录序号
     * @param glxh  关联序号
     * @param xmxh  项目序号
     * @param zdybz 自定义标志
     * @param xmnr  项目内容
     * @param bzxx  备注信息
     * @param xjsj  宣教时间
     * @param xjgh  宣教工号
     * @param xjdx  宣教对象
     * @param xjfs  宣教方式
     * @param xjpj  效果评价
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addHealthGuidXjjlxm(String jlxm, String jlxh, String glxh, String xmxh, String zdybz, String xmnr,
                                   String bzxx, String xjsj, String xjgh, String xjdx, String xjfs, String xjpj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.addHealthGuidXjjlxm(jlxm, jlxh, glxh, xmxh, zdybz, xmnr, bzxx, xjsj, xjgh, xjdx, xjfs, xjpj, dbType);
    }

    /**
     * 修改健康教育宣教项目
     *
     * @param jlxm 记录项目
     * @param xjsj 宣教时间
     * @param xjgh 宣教工号
     * @param xjdx 宣教对象
     * @param xjfs 宣教方式
     * @param xjpj 效果评价
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int editHealthGuidXjjlxm(String jlxm, String xjsj, String xjgh, String xjdx, String xjfs, String xjpj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.editHealthGuidXjjlxm(jlxm, xjsj, xjgh, xjdx, xjfs, xjpj, dbType);
    }

    /**
     * 删除健康宣教主记录
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteHealthGuidJkxjjlByJlxh(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteHealthGuidJkxjjlByJlxh(jlxh);
    }

    /**
     * 删除健康教育项目记录数据
     *
     * @param jlxm 记录项目
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteHealthGuidXjjlxmByJlxm(String jlxm)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteHealthGuidXjjlxmByJlxm(jlxm);
    }

    /**
     * 删除健康教育项目记录数据
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteHealthGuidXjjlxmByJlxh(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteHealthGuidXjjlxmByJlxh(jlxh);
    }

    /**
     * 删除健康教育项目记录数据
     *
     * @param jlxh 记录序号
     * @param glxh 关联序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int deleteHealthGuidXjjlxmByGlxh(String jlxh, String glxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.deleteHealthGuidXjjlxmByGlxh(jlxh, glxh);
    }

    /**
     * 通过关联序号获取样式序号
     *
     * @param glxh 关联序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getYsxhByGlxh(String glxh, String jgid)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getYsxhByGlxh(glxh);
    }

    /**
     * 通过关联序号获取样式序号
     *
     * @param xmxh 项目序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String getHealthGuidRemark(String xmxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.getHealthGuidRemark(xmxh);
    }

    /**
     * 签名
     *
     * @param jlxh 记录序号
     * @param qmgh 签名工号
     * @param qmsj 签名时间
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int Signature(String jlxh, String qmgh, String qmsj)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.Signature(jlxh, qmgh, qmsj, dbType);
    }

    /**
     * 取消签名
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int CancleSignature(String jlxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.CancleSignature(jlxh);
    }

    /**
     * 独立评价 评价
     *
     * @param jlxm 记录项目
     * @param xjpj 宣教评价
     * @param pjsj 评价时间
     * @param pjgh 评价工号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int Evaluate(String jlxm, String xjpj, String pjsj, String pjgh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        dbType = getCurrentDataSourceDBtype();
        return mapper.Evaluate(jlxm, xjpj, pjsj, pjgh, dbType);
    }

    /**
     * 独立评价 取消评价
     *
     * @param jlxm 记录项目
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int CancleEvaluateByJlxm(String jlxm)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.CancleEvaluateByJlxm(jlxm);
    }

    /**
     * 独立评价 取消评价
     *
     * @param jlxh 记录序号
     * @param glxh 归类序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int CancleEvaluateByJlxhAndGlxh(String jlxh, String glxh)
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.CancleEvaluateByJlxhAndGlxh(jlxh, glxh);
    }
}
