package com.bsoft.nis.service.healthguid;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.healthguid.*;
import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.Project;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.healthguid.support.HealthGuidServiceSup;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import com.bsoft.nis.util.date.DateUtil;
import ctd.net.rpc.Client;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 健康教育主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Service
public class HealthGuidMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(HealthGuidMainService.class);

    @Autowired
    HealthGuidServiceSup service; // 健康教育服务

    @Autowired
    PatientServiceSup patientServiceSup; // 病人服务

    @Autowired
    IdentityService identityService;//种子表服务

    @Autowired
    SystemParamService systemParamService;//用户参数服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    /**
     * 获取病区健康宣教列表及其记录数量
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HealthGuid> getHealthGuidList(String zyh, String bqid, String jgid) {
        BizResponse<HealthGuid> response = new BizResponse<>();
        try {
            List<HealthGuid> healthGuids = service.getHealthGuidList(bqid, jgid);
            for (HealthGuid healthGuid : healthGuids) {
                if (healthGuid.GLLX.equals("1")) {//表单
                    healthGuid.HealthGuidItems = service.getHealthGuidItemListForBd(healthGuid.GLLX, healthGuid.XH, zyh, bqid, jgid);
                } else {
                    healthGuid.HealthGuidItems = service.getHealthGuidItemListForFl(healthGuid.GLLX, healthGuid.XH, zyh, bqid, jgid);
                }
                healthGuid.SL = (healthGuid.HealthGuidItems != null && healthGuid.HealthGuidItems.size() > 0)
                        ? String.valueOf(healthGuid.HealthGuidItems.size())
                        : "0";
            }
            response.datalist = healthGuids;
            response.isSuccess = true;
            response.message = "获取健康教育类型列表成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育类型列表失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育类型列表失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取健康宣教具体的某一条宣教单
     *
     * @param lxbh     样式序号/归类序号
     * @param xh       IENR_JKXJJL主键
     * @param type     1：表单 2：分类
     * @param operType 操作类型 1：添加 2：修改 9：其他模块引用健康宣教模块
     * @param jgid     机构id
     * @return
     */
    public BizResponse<HealthGuidData> getHealthGuidData(String lxbh, String xh, String type, String operType, String jgid) {
        BizResponse<HealthGuidData> response = new BizResponse<>();
        try {
            String realXh;
            String xjdlpj = systemParamService.getUserParams("1", "IENR", "IENR_XJDLPJ", jgid, DataSource.MOB).datalist.get(0);
            if (StringUtils.isBlank(xh)) {
                realXh = operType.equals("9") ? service.getMaxJlxh(lxbh, type) : "0";
            } else {
                realXh = xh;
            }
            HealthGuidData healthGuidData = getRealHealthGuidData(lxbh, realXh, type, xjdlpj, jgid);
            response.data = healthGuidData;
            response.isSuccess = true;
            response.message = "获取健康教育数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育数据失败]数据库查询错误!";
        } catch (IOException io) {
            logger.error(io.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育数据失败]服务内部错误!";
        } catch (ClassNotFoundException nf) {
            logger.error(nf.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育数据失败]服务内部错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取具体的某一条宣教单
     * <p>
     * 归类类型新增专用：通过归类类型先找到对应的样式序号然后进行新增
     *
     * @param lxbh 归类序号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HealthGuidData> getHealthGuidDataSpecial(String lxbh, String jgid) {
        BizResponse<HealthGuidData> response = new BizResponse<>();
        try {
            String ysxh = service.getYsxhByGlxh(lxbh, jgid);
            String xjdlpj = systemParamService.getUserParams("1", "IENR", "IENR_XJDLPJ", jgid, DataSource.MOB).datalist.get(0);
            HealthGuidData healthGuidData = getRealHealthGuidData(ysxh, "0", "2", xjdlpj, jgid);
            response.data = healthGuidData;
            response.isSuccess = true;
            response.message = "获取健康教育数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育数据失败]数据库查询错误!";
        } catch (IOException io) {
            logger.error(io.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育数据失败]服务内部错误!";
        } catch (ClassNotFoundException nf) {
            logger.error(nf.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育数据失败]服务内部错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取健康教育数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存健康宣教数据
     *
     * @param healthGuidSaveData
     * @return
     */
    public BizResponse<HealthGuidData> saveHealthGuidData(HealthGuidSaveData healthGuidSaveData) {
        BizResponse<HealthGuidData> response = new BizResponse<>();
        try {
            if (healthGuidSaveData != null) {
                String xjdlpj = systemParamService.getUserParams("1", "IENR", "IENR_XJDLPJ", healthGuidSaveData.JGID, DataSource.MOB).datalist.get(0);
                HealthGuidData par = healthGuidSaveData.HealthGuidData;
                boolean isSucess =
                        realSaveHealthGuidData(par, xjdlpj, healthGuidSaveData.ZYH, healthGuidSaveData.BQID, healthGuidSaveData.JGID);
                if (isSucess) {
                    String czbz = par.OperType.equals("1") ? "0" : "1";
                    Response<SelectResult> syncResponse = buildSyncData(healthGuidSaveData.BQID, healthGuidSaveData.ZYH, par, czbz, healthGuidSaveData.JGID);
                    HealthGuidData realReturnHealthGuidData = getRealHealthGuidData(par.GLXH, par.XH, par.GLLX, par.OperType, healthGuidSaveData.JGID);
                    if (syncResponse.ReType == 2) {
                        realReturnHealthGuidData.IsSync = true;
                        realReturnHealthGuidData.SyncData = syncResponse.Data;
                    }
                    response.data = realReturnHealthGuidData;
                    response.isSuccess = true;
                    response.message = "保存健康教育数据成功!";
                } else {
                    response.isSuccess = false;
                    response.message = "[保存健康教育数据失败]服务内部错误!";
                }
            } else {
                response.isSuccess = false;
                response.message = "[保存健康教育数据失败]传入参数为空";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存健康教育数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存健康教育数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 删除健康宣教数据
     *
     * @param jlxh 记录序号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<String> deleteHealthGuidData(String jlxh, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.MOB);
            response = realDeleteHealthGuidData(jlxh, jgid);
            if (response.isSuccess) {
                deleSyncData(jlxh, "0", "0", jgid);
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除健康教育数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除健康教育数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取备注信息
     *
     * @param xmxh 项目序号
     * @return
     */
    public BizResponse<String> getHealthGuidRemark(String xmxh) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (!StringUtils.isBlank(xmxh)) {
                response.data = service.getHealthGuidRemark(xmxh);
                response.isSuccess = true;
                response.message = "获取备注信息成功!";
            } else {
                response.isSuccess = false;
                response.message = "[获取备注信息失败]传入参数为空";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取备注信息失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取备注信息失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 签名
     *
     * @param jlxh 记录序号
     * @param qmgh 签名工号
     * @param glxh 关联序号
     * @param gllx 关联类型
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HealthGuidData> signatureHealthGuid(String jlxh, String qmgh, String glxh, String gllx, String jgid) {
        BizResponse<HealthGuidData> response = new BizResponse<>();
        try {
            String now = timeService.now(DataSource.MOB);
            boolean isSucess = service.Signature(jlxh, qmgh, now) > 0;
            if (isSucess) {
                String xjdlpj = systemParamService.getUserParams("1", "IENR", "IENR_XJDLPJ", jgid, DataSource.MOB).datalist.get(0);
                HealthGuidData healthGuidData = getRealHealthGuidData(glxh, jlxh, gllx, xjdlpj, jgid);
                response.data = healthGuidData;
                response.isSuccess = true;
                response.message = "签名成功!";
            } else {
                response.isSuccess = false;
                response.message = "签名失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[签名失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[签名失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 取消签名
     *
     * @param jlxh 记录序号
     * @param glxh 关联序号
     * @param gllx 关联类型
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HealthGuidData> cancleSignatureHealthGuid(String jlxh, String glxh, String gllx, String jgid) {
        BizResponse<HealthGuidData> response = new BizResponse<>();
        try {
            boolean isSucess = service.CancleSignature(jlxh) > 0;
            if (isSucess) {
                String xjdlpj = systemParamService.getUserParams("1", "IENR", "IENR_XJDLPJ", jgid, DataSource.MOB).datalist.get(0);
                HealthGuidData healthGuidData = getRealHealthGuidData(glxh, jlxh, gllx, xjdlpj, jgid);
                response.data = healthGuidData;
                response.isSuccess = true;
                response.message = "取消签名成功!";
            } else {
                response.isSuccess = false;
                response.message = "取消签名失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[取消签名失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[取消签名失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取具体的某一条宣教单/宣教归类 独立评价项目
     *
     * @param lxbh 样式序号/归类序号
     * @param xh   IENR_JKXJJL主键
     * @param type 1：表单 2：分类
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HealthGuidEvaluateData> getRealHealthGuidEvaluateData(String lxbh, String xh, String type, String jgid) {
        BizResponse<HealthGuidEvaluateData> response = new BizResponse<>();
        try {
            String xjdlpj = systemParamService.getUserParams("1", "IENR", "IENR_XJDLPJ", jgid, DataSource.MOB).datalist.get(0);
            HealthGuidEvaluateData healthGuidEvaluateData = new HealthGuidEvaluateData();
            healthGuidEvaluateData.XH = xh;
            healthGuidEvaluateData.GLXH = lxbh;
            healthGuidEvaluateData.GLLX = type;
            healthGuidEvaluateData.HealthGuidDefaultOpers = getHealthGuidDefaultOperListForXjdlpj(jgid);
            if (type.equals("1")) {//表单
                healthGuidEvaluateData.HealthGuidTypes = service.getHealthGuidTypeListByYsxh(lxbh);
            } else if (type.equals("2")) {//归类
                healthGuidEvaluateData.HealthGuidTypes = service.getHealthGuidTypeListByGlxh(lxbh);
            } else {
                healthGuidEvaluateData.HealthGuidTypes = new ArrayList<>();
            }
            for (HealthGuidType healthGuidType : healthGuidEvaluateData.HealthGuidTypes) {
                healthGuidType.HealthGuidDetails = service.getHealthGuidDetailListByGlxh(healthGuidType.LXBH);
                for (HealthGuidDetail healthGuidDetail : healthGuidType.HealthGuidDetails) {
                    healthGuidDetail.HealthGuidOpers = deepCopyList(healthGuidEvaluateData.HealthGuidDefaultOpers);
                }
            }
            List<HealthGuidDetail> healthGuidDetails = service.getHealthGuidDetailListByJlxh(xh);
            for (HealthGuidDetail detailItem : healthGuidDetails) {
                HealthGuidType healthGuidType = null;
                for (HealthGuidType typeItem : healthGuidEvaluateData.HealthGuidTypes) {
                    if (typeItem.LXBH.equals(detailItem.GLXH)) {
                        healthGuidType = typeItem;
                        break;
                    }
                }
                if (healthGuidType != null) {
                    healthGuidType.ISCHECK = "1";
                    if (detailItem.ZDYBZ.equals("0")) {
                        HealthGuidDetail healthGuidDetail = null;
                        for (HealthGuidDetail detail : healthGuidType.HealthGuidDetails) {
                            if (detail.XH.equals(detailItem.XH)) {
                                healthGuidDetail = detail;
                            }
                        }
                        if (healthGuidDetail != null) {
                            healthGuidDetail.ISCHECK = "1";
                            healthGuidDetail.JLXM = detailItem.JLXM;
                            healthGuidDetail.JLXH = detailItem.JLXH;
                            healthGuidDetail.GLXH = detailItem.GLXH;
                            healthGuidDetail.ZDYBZ = detailItem.ZDYBZ;
                            healthGuidDetail.XJSJ = detailItem.XJSJ;
                            healthGuidDetail.XJGH = detailItem.XJGH;
                            healthGuidDetail.XJDX = detailItem.XJDX;
                            healthGuidDetail.XJFS = detailItem.XJFS;
                            healthGuidDetail.XJPJ = detailItem.XJPJ;
                            healthGuidDetail.PJSJ = detailItem.PJSJ;
                            healthGuidDetail.PJGH = detailItem.PJGH;
                            healthGuidDetail.ISOPER = "0";
                            healthGuidDetail.HealthGuidOpers =
                                    getHealthGuidOperList(healthGuidDetail.HealthGuidOpers, detailItem.XJDX, detailItem.XJFS, detailItem.XJPJ, xjdlpj, jgid);
                        }
                    } else {
                        HealthGuidDetail healthGuidDetail = new HealthGuidDetail();
                        healthGuidDetail.ISCHECK = "1";
                        healthGuidDetail.JLXM = detailItem.JLXM;
                        healthGuidDetail.JLXH = detailItem.JLXH;
                        healthGuidDetail.GLXH = detailItem.GLXH;
                        healthGuidDetail.XH = detailItem.XH;
                        healthGuidDetail.MS = detailItem.MS;
                        healthGuidDetail.ZDYBZ = detailItem.ZDYBZ;
                        healthGuidDetail.XJSJ = detailItem.XJSJ;
                        healthGuidDetail.XJGH = detailItem.XJGH;
                        healthGuidDetail.XJDX = detailItem.XJDX;
                        healthGuidDetail.XJFS = detailItem.XJFS;
                        healthGuidDetail.XJPJ = detailItem.XJPJ;
                        healthGuidDetail.PJSJ = detailItem.PJSJ;
                        healthGuidDetail.PJGH = detailItem.PJGH;
                        healthGuidDetail.ISOPER = "0";
                        healthGuidDetail.DLBZ = "1";
                        healthGuidDetail.XMZH = "0";
                        healthGuidDetail.HealthGuidOpers =
                                getHealthGuidOperList(null, detailItem.XJDX, detailItem.XJFS, detailItem.XJPJ, xjdlpj, jgid);
                        healthGuidType.HealthGuidDetails.add(healthGuidDetail);
                    }
                }
            }
            for (int i = 0; i < healthGuidEvaluateData.HealthGuidTypes.size(); i++) {
                HealthGuidType healthGuidType = healthGuidEvaluateData.HealthGuidTypes.get(i);
                if (healthGuidType.ISCHECK.equals("0")) {
                    healthGuidEvaluateData.HealthGuidTypes.remove(healthGuidType);
                    i--;
                    continue;
                }
                for (int j = 0; j < healthGuidType.HealthGuidDetails.size(); j++) {
                    HealthGuidDetail healthGuidDetail = healthGuidType.HealthGuidDetails.get(j);
                    if (healthGuidDetail.ISCHECK.equals("0")) {
                        healthGuidType.HealthGuidDetails.remove(healthGuidDetail);
                        j--;
                    } else {
                        if (StringUtils.isBlank(healthGuidDetail.PJGH) || StringUtils.isBlank(healthGuidDetail.PJSJ)) {
                            healthGuidDetail.ISCHECK = "0";
                        }
                    }
                }
                if (healthGuidType.HealthGuidDetails.size() == 0) {
                    healthGuidEvaluateData.HealthGuidTypes.remove(healthGuidType);
                    i--;
                }
            }
            response.data = healthGuidEvaluateData;
            response.isSuccess = true;
            response.message = "获取独立评价数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取独立评价数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取独立评价数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存数据 评价/取消评价 独立评价用
     *
     * @param healthGuidEvaluateData 数据对象
     * @return
     */
    public BizResponse<String> saveHealthGuidEvaluateData(HealthGuidEvaluateData healthGuidEvaluateData) {
        BizResponse<String> response = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.MOB);
            response = realSaveHealthGuidEvaluateData(healthGuidEvaluateData);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[评价失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[评价失败]服务内部错误!";
        }
        return response;
    }


    private boolean realSaveHealthGuidData(HealthGuidData healthGuidData, String xjdlpj, String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        boolean isSucess;
        if (healthGuidData.OperType.equals("1")) {
            isSucess = addHealthGuidData(healthGuidData, xjdlpj, zyh, bqid, jgid);
        } else if (healthGuidData.OperType.equals("2")) {
            isSucess = editHealthGuidData(healthGuidData, xjdlpj, zyh, bqid, jgid);
        } else {
            isSucess = false;
        }
        return isSucess;
    }

    private boolean addHealthGuidData(HealthGuidData healthGuidData, String xjdlpj, String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        healthGuidData.XH = String.valueOf(identityService.getIdentityMax("IENR_JKXJJL",DataSource.MOB));
        //保存到IENR_JKXJJL的GLXH
        String realGlxh = "";
        if (healthGuidData.GLLX.equals("1")) {
            realGlxh = healthGuidData.GLXH;
        } else if (healthGuidData.GLLX.equals("2")) {
            for (HealthGuidType healthGuidType : healthGuidData.HealthGuidTypes) {
                if (healthGuidType.ISCHECK.equals("1")) {
                    realGlxh = healthGuidType.LXBH;
                    break;
                }
            }
        }
        //此行代码可以防止保存之后获取数据有问题
        healthGuidData.GLXH = realGlxh;
        for (HealthGuidType healthGuidType : healthGuidData.HealthGuidTypes) {
            if (healthGuidType.ISCHECK.equals("1")) {
                for (HealthGuidDetail healthGuidDetail : healthGuidType.HealthGuidDetails) {
                    if (healthGuidDetail.ISCHECK.equals("1")) {
                        healthGuidDetail.JLXM = String.valueOf(identityService.getIdentityMax("IENR_XJJLXM",DataSource.MOB));
                    }
                }
            }
        }
        keepOrRoutingDateSource(DataSource.MOB);
        return realAddHealthGuidData(healthGuidData, xjdlpj, zyh, bqid, jgid);
    }

    /**
     * 注意：调用此方法之前必须显式指定启用哪个事务
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean realAddHealthGuidData(HealthGuidData healthGuidData, String xjdlpj, String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        String now = DateUtil.getApplicationDateTime();
        String qmsj = StringUtils.isBlank(healthGuidData.QMGH) ? "" : now;
        boolean isSucess = service.addHealthGuidJkxjjl(healthGuidData.XH, zyh, bqid, healthGuidData.GLXH, healthGuidData.GLLX,
                healthGuidData.JLSJ, healthGuidData.JLGH, now, healthGuidData.JLGH, qmsj, healthGuidData.QMGH, jgid, "") > 0;
        if (isSucess) {
            for (HealthGuidType healthGuidType : healthGuidData.HealthGuidTypes) {
                if (healthGuidType.ISCHECK.equals("1")) {
                    for (HealthGuidDetail healthGuidDetail : healthGuidType.HealthGuidDetails) {
                        if (healthGuidDetail.ISCHECK.equals("1")) {
                            isSucess = addHealthGuidDetail(healthGuidDetail, xjdlpj, healthGuidData.XH, healthGuidType.LXBH);
                            if (!isSucess) {
                                return isSucess;
                            }
                        }
                    }
                }
            }
        }

        return isSucess;
    }

    private boolean addHealthGuidDetail(HealthGuidDetail healthGuidDetail, String xjdlpj, String jlxh, String glxh)
            throws SQLException, DataAccessException {
        getHealthGuidDetailAttribute(healthGuidDetail, xjdlpj);
        boolean isSucess = service.addHealthGuidXjjlxm(healthGuidDetail.JLXM, jlxh, glxh, healthGuidDetail.XH, healthGuidDetail.ZDYBZ,
                healthGuidDetail.ZDYBZ.equals("1") ? healthGuidDetail.MS : "", "", healthGuidDetail.XJSJ, healthGuidDetail.XJGH,
                healthGuidDetail.XJDX, healthGuidDetail.XJFS, healthGuidDetail.XJPJ) > 0;
        return isSucess;
    }

    private boolean editHealthGuidData(HealthGuidData healthGuidData, String xjdlpj, String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        for (HealthGuidType healthGuidType : healthGuidData.HealthGuidTypes) {
            if (healthGuidType.ISCHECK.equals("1")) {
                for (HealthGuidDetail healthGuidDetail : healthGuidType.HealthGuidDetails) {
                    if (healthGuidDetail.ISCHECK.equals("1") && StringUtils.isBlank(healthGuidDetail.JLXM)) {
                        healthGuidDetail.JLXM = "add:" + String.valueOf(identityService.getIdentityMax("IENR_XJJLXM",DataSource.MOB));
                    }
                }
            }
        }
        keepOrRoutingDateSource(DataSource.MOB);
        return realEditHealthGuidData(healthGuidData, xjdlpj, zyh, bqid, jgid);
    }

    /**
     * 注意：调用此方法之前必须显式指定启用哪个事务
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean realEditHealthGuidData(HealthGuidData healthGuidData, String xjdlpj, String zyh, String bqid, String jgid)
            throws SQLException, DataAccessException {
        boolean isSucess = false;
        for (HealthGuidType healthGuidType : healthGuidData.HealthGuidTypes) {
            if (healthGuidType.ISCHECK.equals("0")) {
                isSucess = service.deleteHealthGuidXjjlxmByGlxh(healthGuidData.XH, healthGuidType.LXBH) >= 0;
                if (!isSucess) {
                    return isSucess;
                }
            } else {
                for (HealthGuidDetail healthGuidDetail : healthGuidType.HealthGuidDetails) {
                    if (healthGuidDetail.ISCHECK.equals("0")) {
                        isSucess = service.deleteHealthGuidXjjlxmByJlxm(healthGuidDetail.JLXM) >= 0;
                        if (!isSucess) {
                            return isSucess;
                        }
                    } else if (healthGuidDetail.ISCHECK.equals("1")) {
                        if (!StringUtils.isBlank(healthGuidDetail.JLXM) && !healthGuidDetail.JLXM.contains("add:")) {
                            //修改当前宣教记录项目
                            isSucess = editHealthGuidDetail(healthGuidDetail, xjdlpj);
                            if (!isSucess) {
                                return isSucess;
                            }

                        } else {
                            healthGuidDetail.JLXM = healthGuidDetail.JLXM.replace("add:", "");
                            //添加宣教记录项目
                            isSucess = addHealthGuidDetail(healthGuidDetail, xjdlpj, healthGuidData.XH, healthGuidType.LXBH);
                            if (!isSucess) {
                                return isSucess;
                            }
                        }
                    }
                }
            }
        }


        return isSucess;
    }

    private boolean editHealthGuidDetail(HealthGuidDetail healthGuidDetail, String xjdlpj)
            throws SQLException, DataAccessException {
        getHealthGuidDetailAttribute(healthGuidDetail, xjdlpj);
        boolean isSucess = service.editHealthGuidXjjlxm(healthGuidDetail.JLXM, healthGuidDetail.XJSJ, healthGuidDetail.XJGH,
                healthGuidDetail.XJDX, healthGuidDetail.XJFS, healthGuidDetail.XJPJ) > 0;
        return isSucess;
    }

    private void getHealthGuidDetailAttribute(HealthGuidDetail healthGuidDetail, String xjdlpj) {
        healthGuidDetail.XJDX = "";
        healthGuidDetail.XJFS = "";
        healthGuidDetail.XJPJ = "";
        for (HealthGuidOper healthGuidOper : healthGuidDetail.HealthGuidOpers) {
            for (HealthGuidOperItem healthGuidOperItem : healthGuidOper.HealthGuidOperItems) {
                if (healthGuidOper.XH.equals("1")) {
                    if (healthGuidOperItem.ISCHECK.equals("1")) {
                        healthGuidDetail.XJDX += healthGuidOperItem.XH + "|";
                    }
                } else if (healthGuidOper.XH.equals("2")) {
                    if (healthGuidOperItem.ISCHECK.equals("1")) {
                        healthGuidDetail.XJFS += healthGuidOperItem.XH + "|";
                    }
                } else if (healthGuidOper.XH.equals("3") && xjdlpj.equals("0")) {
                    if (healthGuidOperItem.ISCHECK.equals("1")) {
                        healthGuidDetail.XJPJ += healthGuidOperItem.XH + "|";
                    }
                }
            }
        }
        if (!StringUtils.isBlank(healthGuidDetail.XJDX)) {
            healthGuidDetail.XJDX = healthGuidDetail.XJDX.substring(0, healthGuidDetail.XJDX.length() - 1);
        }
        if (!StringUtils.isBlank(healthGuidDetail.XJFS)) {
            healthGuidDetail.XJFS = healthGuidDetail.XJFS.substring(0, healthGuidDetail.XJFS.length() - 1);
        }
        if (!StringUtils.isBlank(healthGuidDetail.XJPJ)) {
            healthGuidDetail.XJPJ = healthGuidDetail.XJPJ.substring(0, healthGuidDetail.XJPJ.length() - 1);
        }

    }

    private HealthGuidData getRealHealthGuidData(String lxbh, String xh, String type, String xjdlpj, String jgid)
            throws SQLException, DataAccessException, IOException, ClassNotFoundException {
        HealthGuidData healthGuidData = new HealthGuidData();
        healthGuidData.XH = xh;
        healthGuidData.GLXH = lxbh;
        healthGuidData.OperType = xh.equals("0") ? "1" : "2";
        healthGuidData.GLLX = type;
        healthGuidData.HealthGuidDefaultOpers = getHealthGuidDefaultOperList(xjdlpj, jgid);
        healthGuidData.XJDLPJ = xjdlpj;
        if (healthGuidData.OperType.equals("1")) {//新增
            //新增时候传进来都是样式序号
            healthGuidData.HealthGuidTypes = service.getHealthGuidTypeListByYsxh(lxbh);
        } else {//修改
            if (type.equals("1")) {//表单
                healthGuidData.HealthGuidTypes = service.getHealthGuidTypeListByYsxh(lxbh);
            } else if (type.equals("2")) {//归类
                healthGuidData.HealthGuidTypes = service.getHealthGuidTypeListByGlxh(lxbh);
            } else {
                healthGuidData.HealthGuidTypes = new ArrayList<>();
            }
        }
        for (HealthGuidType healthGuidType : healthGuidData.HealthGuidTypes) {
            healthGuidType.HealthGuidDetails = service.getHealthGuidDetailListByGlxh(healthGuidType.LXBH);
            for (HealthGuidDetail healthGuidDetail : healthGuidType.HealthGuidDetails) {
                healthGuidDetail.HealthGuidOpers = deepCopyList(healthGuidData.HealthGuidDefaultOpers);
            }
        }
        if (healthGuidData.OperType.equals("1")) {//新增
            healthGuidData.HealthGuidTypes.get(0).ISCHECK = "1";
            healthGuidData.GLXH = healthGuidData.HealthGuidTypes.get(0).LXBH;
        } else if (healthGuidData.OperType.equals("2")) {//修改
            HealthGuidData obj = service.getHealthGuidDataByJlxh(xh);
            healthGuidData.JLSJ = obj.JLSJ;
            healthGuidData.JLGH = obj.JLGH;
            healthGuidData.QMGH = obj.QMGH;
            if (!StringUtils.isBlank(healthGuidData.QMGH)) {
                for (HealthGuidType healthGuidType : healthGuidData.HealthGuidTypes) {
                    for (HealthGuidDetail healthGuidDetail : healthGuidType.HealthGuidDetails) {
                        healthGuidDetail.ISOPER = "1";
                    }
                }
            }
            List<HealthGuidDetail> healthGuidDetails = service.getHealthGuidDetailListByJlxh(xh);
            for (HealthGuidDetail detailItem : healthGuidDetails) {
                String isOper = "0";
                if (!StringUtils.isBlank(healthGuidData.QMGH) || (xjdlpj.equals("1") && !StringUtils.isBlank(detailItem.PJGH))) {
                    //已经签名或者独立评价模式下已经评价过的项目不允许修改
                    isOper = "1";
                }
                HealthGuidType healthGuidType = null;
                for (HealthGuidType typeItem : healthGuidData.HealthGuidTypes) {
                    if (typeItem.LXBH.equals(detailItem.GLXH)) {
                        healthGuidType = typeItem;
                        break;
                    }
                }
                if (healthGuidType != null) {
                    healthGuidType.ISCHECK = "1";
                    if (detailItem.ZDYBZ.equals("0")) {
                        HealthGuidDetail healthGuidDetail = null;
                        for (HealthGuidDetail detail : healthGuidType.HealthGuidDetails) {
                            if (detail.XH.equals(detailItem.XH)) {
                                healthGuidDetail = detail;
	                            break;
                            }
                        }
                        if (healthGuidDetail != null) {
                            healthGuidDetail.ISCHECK = "1";
                            healthGuidDetail.JLXM = detailItem.JLXM;
                            healthGuidDetail.JLXH = detailItem.JLXH;
                            healthGuidDetail.GLXH = detailItem.GLXH;
                            healthGuidDetail.ZDYBZ = detailItem.ZDYBZ;
                            healthGuidDetail.XJSJ = detailItem.XJSJ;
                            healthGuidDetail.XJGH = detailItem.XJGH;
                            healthGuidDetail.XJDX = detailItem.XJDX;
                            healthGuidDetail.XJFS = detailItem.XJFS;
                            healthGuidDetail.XJPJ = detailItem.XJPJ;
                            healthGuidDetail.PJSJ = detailItem.PJSJ;
                            healthGuidDetail.PJGH = detailItem.PJGH;

                            healthGuidDetail.ISOPER = isOper;
                            healthGuidDetail.HealthGuidOpers =
                                    getHealthGuidOperList(healthGuidDetail.HealthGuidOpers, detailItem.XJDX, detailItem.XJFS, detailItem.XJPJ, xjdlpj, jgid);
                        }
                    } else {
                        HealthGuidDetail healthGuidDetail = new HealthGuidDetail();
                        healthGuidDetail.ISCHECK = "1";
                        healthGuidDetail.JLXM = detailItem.JLXM;
                        healthGuidDetail.JLXH = detailItem.JLXH;
                        healthGuidDetail.GLXH = detailItem.GLXH;
                        healthGuidDetail.XH = detailItem.XH;
                        healthGuidDetail.MS = detailItem.MS;
                        healthGuidDetail.ZDYBZ = detailItem.ZDYBZ;
                        healthGuidDetail.XJSJ = detailItem.XJSJ;
                        healthGuidDetail.XJGH = detailItem.XJGH;
                        healthGuidDetail.XJDX = detailItem.XJDX;
                        healthGuidDetail.XJFS = detailItem.XJFS;
                        healthGuidDetail.XJPJ = detailItem.XJPJ;
                        healthGuidDetail.PJSJ = detailItem.PJSJ;
                        healthGuidDetail.PJGH = detailItem.PJGH;
                        healthGuidDetail.ISOPER = isOper;
                        healthGuidDetail.DLBZ = "1";
                        healthGuidDetail.XMZH = "0";
                        healthGuidDetail.HealthGuidOpers =
                                getHealthGuidOperList(null, detailItem.XJDX, detailItem.XJFS, detailItem.XJPJ, xjdlpj, jgid);
                        healthGuidType.HealthGuidDetails.add(healthGuidDetail);
                    }
                }
            }

        }
        return healthGuidData;
    }

    private List<HealthGuidOper> getHealthGuidDefaultOperList(String xjdlpj, String jgid)
            throws SQLException, DataAccessException {
        List<HealthGuidOper> healthGuidOpers = new ArrayList<>();

        HealthGuidOper xjdx = new HealthGuidOper();
        xjdx.XH = "1";
        xjdx.MS = "宣教对象";
        xjdx.HealthGuidOperItems = service.getHealthGuidXjdx();
        healthGuidOpers.add(xjdx);

        HealthGuidOper xjfs = new HealthGuidOper();
        xjfs.XH = "2";
        xjfs.MS = "宣教方式";
        xjfs.HealthGuidOperItems = service.getHealthGuidXjfs();
        healthGuidOpers.add(xjfs);

        if (xjdlpj.equals("0")) {
            HealthGuidOper xgpj = new HealthGuidOper();
            xgpj.XH = "3";
            xgpj.MS = "效果评价";
            xgpj.HealthGuidOperItems = service.getHealthGuidXgpj();
            healthGuidOpers.add(xgpj);
        }

        return healthGuidOpers;
    }

    private List<HealthGuidOper> getHealthGuidDefaultOperListForXjdlpj(String jgid)
            throws SQLException, DataAccessException {
        List<HealthGuidOper> healthGuidOpers = new ArrayList<>();
        HealthGuidOper xgpj = new HealthGuidOper();
        xgpj.XH = "3";
        xgpj.MS = "效果评价";
        xgpj.HealthGuidOperItems = service.getHealthGuidXgpj();
        healthGuidOpers.add(xgpj);

        return healthGuidOpers;
    }

    private List<HealthGuidOper> getHealthGuidOperList(List<HealthGuidOper> list, String xjdx, String xjfs, String xjpj, String xjdlpj, String jgid)
            throws SQLException, DataAccessException, IOException, ClassNotFoundException {
        String[] xjdxArray = xjdx == null ? new String[0] : xjdx.split("\\|");
        String[] xjfsArray = xjfs == null ? new String[0] : xjfs.split("\\|");
        String[] xjpjArray = xjpj == null ? new String[0] : xjpj.split("\\|");
        if (list == null || list.size() == 0) {
            list = deepCopyList(getHealthGuidDefaultOperList(xjdlpj, jgid));
        }
        for (HealthGuidOper healthGuidOper : list) {
            if (healthGuidOper.XH.equals("1")) {//宣教对象
                for (String item : xjdxArray) {
                    for (HealthGuidOperItem operItem : healthGuidOper.HealthGuidOperItems) {
                        if (operItem.XH.equals(item)) {
                            operItem.ISCHECK = "1";
                        }
                    }
                }
            } else if (healthGuidOper.XH.equals("2")) {//宣教方式
                for (String item : xjfsArray) {
                    for (HealthGuidOperItem operItem : healthGuidOper.HealthGuidOperItems) {
                        if (operItem.XH.equals(item)) {
                            operItem.ISCHECK = "1";
                        }
                    }
                }

            } else if (healthGuidOper.XH.equals("3")) {//评价结果
                for (String item : xjpjArray) {
                    for (HealthGuidOperItem operItem : healthGuidOper.HealthGuidOperItems) {
                        if (operItem.XH.equals(item)) {
                            operItem.ISCHECK = "1";
                        }
                    }
                }

            }

        }
        return list;
    }

    /**
     * 保存数据 评价/取消评价 独立评价用
     * <p>
     * 注意：调用此方法之前必须显式指定启用哪个事务
     *
     * @param healthGuidEvaluateData 数据对象
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<String> realSaveHealthGuidEvaluateData(HealthGuidEvaluateData healthGuidEvaluateData)
            throws SQLException, DataAccessException {
        BizResponse<String> response = new BizResponse<>();
        boolean isSucess = false;
        for (HealthGuidType healthGuidType : healthGuidEvaluateData.HealthGuidTypes) {
            if (healthGuidType.ISCHECK.equals("1")) {
                for (HealthGuidDetail healthGuidDetail : healthGuidType.HealthGuidDetails) {
                    if (healthGuidDetail.ISCHECK.equals("1")) {
                        if (!StringUtils.isBlank(healthGuidDetail.JLXM)) {
                            List<HealthGuidOperItem> xjpjList = healthGuidDetail.HealthGuidOpers.get(0).HealthGuidOperItems;
                            healthGuidDetail.XJPJ = "";
                            for (HealthGuidOperItem item : xjpjList) {
                                if (item.ISCHECK.equals("1")) {
                                    healthGuidDetail.XJPJ += item.XH + "|";
                                }
                            }
                            if (StringUtils.isBlank(healthGuidDetail.XJPJ)) {
                                response.message = "[评价失败]效果评价不能为空!";
                                response.isSuccess = false;
                                return response;
                            }
                            healthGuidDetail.XJPJ = healthGuidDetail.XJPJ.substring(0, healthGuidDetail.XJPJ.length() - 1);
                            isSucess = service.Evaluate(healthGuidDetail.JLXM, healthGuidDetail.XJPJ, healthGuidDetail.PJSJ, healthGuidDetail.PJGH) > 0;
                            if (!isSucess) {
                                response.message = "[评价失败]数据库执行错误!";
                                response.isSuccess = false;
                                return response;
                            }
                        }
                    } else {
                        isSucess = service.CancleEvaluateByJlxm(healthGuidDetail.JLXM) > 0;
                        if (!isSucess) {
                            response.message = "[评价失败]数据库执行错误!";
                            response.isSuccess = false;
                            return response;
                        }
                    }
                }
            } else {
                isSucess = service.CancleEvaluateByJlxhAndGlxh(healthGuidEvaluateData.XH, healthGuidType.LXBH) > 0;
                if (!isSucess) {
                    response.message = "[评价失败]数据库执行错误!";
                    response.isSuccess = false;
                    return response;
                }
            }
        }
        if (isSucess) {
            response.data = "评价成功!";
            response.isSuccess = true;
        }
        return response;
    }

    /**
     * 删除健康宣教数据
     * <p>
     * 注意：调用此方法之前必须显式指定启用哪个事务
     *
     * @param jlxh 记录序号
     * @param jgid 机构id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<String> realDeleteHealthGuidData(String jlxh, String jgid)
            throws SQLException, DataAccessException {
        BizResponse<String> response = new BizResponse<>();
        if (!StringUtils.isBlank(jlxh)) {
            boolean isSucess = service.deleteHealthGuidXjjlxmByJlxh(jlxh) > 0;
            if (isSucess) {
                isSucess = service.deleteHealthGuidJkxjjlByJlxh(jlxh) > 0;
                if (isSucess) {
                    response.isSuccess = true;
                    response.message = "删除健康教育数据成功!";
                } else {
                    response.isSuccess = false;
                    response.message = "[删除健康教育数据失败]服务内部错误!";
                }
            } else {
                response.isSuccess = false;
                response.message = "[删除健康教育数据失败]服务内部错误!";
            }
        } else {
            response.isSuccess = false;
            response.message = "[删除健康教育数据失败]传入参数为空";
        }
        return response;
    }

    /**
     * 将templatesList中的数据深度copy到targetList中
     *
     * @param templatesList 模版数组
     */
    private ArrayList<HealthGuidOper> deepCopyList(List<HealthGuidOper> templatesList)
            throws IOException, ClassNotFoundException {
        ArrayList<HealthGuidOper> targetList = new ArrayList<HealthGuidOper>();
        if (templatesList == null) {
            return targetList;
        }
        for (int i = 0; i < templatesList.size(); i++) {
            targetList.add(templatesList.get(i).DeepClone());
        }
        return targetList;
    }

    /**
     * 同步操作
     *
     * @param bqid
     * @param zyh
     * @param data
     * @param czbz
     * @param jgid
     * @return
     */
    private Response<SelectResult> buildSyncData(String bqid, String zyh, HealthGuidData data, String czbz,
                                                 String jgid) {
        InArgument inArgument = new InArgument();
        inArgument.zyh = zyh;
        inArgument.bqdm = bqid;
        inArgument.hsgh = data.JLGH;
        inArgument.bdlx = "3";
        inArgument.lybd = "0";
        inArgument.flag = czbz;
        inArgument.jlxh = data.XH;
        inArgument.jlsj = data.JLSJ;
        inArgument.jgid = jgid;
        inArgument.lymx = "0";
        inArgument.lymxlx = "0";

        for (HealthGuidType type : data.HealthGuidTypes) {
            if ("1".equals(type.ISCHECK)) {
                Project project = new Project("2", data.XH);
                for (HealthGuidDetail detail : type.HealthGuidDetails) {
                    if ("1".equals(detail.ISCHECK)) {
                        project.value = detail.GLXH;
                        Project newProject = new Project(detail.XH, detail.MS);
                        project.saveProjects.add(newProject);
                    }
                }
                if (!project.saveProjects.isEmpty()) {
                    inArgument.projects.add(project);
                }
            }
        }
        Response<SelectResult> response = new Response<>();
        try {
            response = Client
                    .rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchron", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
        return response;
    }

    /**
     * 删除操作的同步
     *
     * @param jlxh
     * @param lymx
     * @param lymxlx
     * @param jgid
     */
    private void deleSyncData(String jlxh, String lymx, String lymxlx, String jgid) {
        InArgument inArgument = new InArgument();
        inArgument.bdlx = "3";
        inArgument.jlxh = jlxh;
        inArgument.lymx = lymx;
        inArgument.lymxlx = lymxlx;
        inArgument.jgid = jgid;
        inArgument.flag = "2";

        Response<String> response = new Response<>();
        try {
            response = Client.rpcInvoke("nis-synchron.synchronRpcServerProvider", "DeleSyncData", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
    }
}
