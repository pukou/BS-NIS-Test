package com.bsoft.nis.service.bloodtransfusion;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.bloodtransfusion.BloodRecieveSaveData;
import com.bsoft.nis.domain.bloodtransfusion.BloodReciveInfo;
import com.bsoft.nis.domain.bloodtransfusion.BloodTransfusionInfo;
import com.bsoft.nis.domain.bloodtransfusion.BloodTransfusionTourInfo;
import com.bsoft.nis.domain.patient.db.SickPersonDetailVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.service.bloodtransfusion.support.BloodTransfusionServiceSup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 输血模块主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Service
public class BloodTransfusionMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(BloodTransfusionMainService.class);

    @Autowired
    BloodTransfusionServiceSup service; // 输血模块服务

    @Autowired
    PatientServiceSup patientServiceSup; // 病人服务

    @Autowired
    SystemParamService systemParamService;//用户参数服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    /**
     * 获取输血计划列表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param zyh   住院号
     * @param jgid  机构id
     * @return
     */
    public BizResponse<BloodTransfusionInfo> getBloodTransfusionPlanList(String start, String end, String zyh, String jgid) {
        BizResponse<BloodTransfusionInfo> response = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            SickPersonDetailVo sickPersonDetailVo = patientServiceSup.getPatientDetail(zyh, jgid);
            String zyhm = sickPersonDetailVo.ZYHM;
            List<BloodTransfusionInfo> bloodTransfusionInfoList = service.getBloodTransfusionPlanList(start, end, zyhm, jgid);
            for (BloodTransfusionInfo bloodTransfusionInfo : bloodTransfusionInfoList) {
                bloodTransfusionInfo.SXR1 = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, bloodTransfusionInfo.SXR1);
                bloodTransfusionInfo.SXR2 = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, bloodTransfusionInfo.SXR2);
                bloodTransfusionInfo.JSR = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, bloodTransfusionInfo.JSR);
            }
            response.datalist = bloodTransfusionInfoList;
            response.isSuccess = true;
            response.message = "获取输血计划列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输血计划列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输血计划列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 输血医嘱执行
     *
     * @param xdh           血袋号
     * @param xdxh          血袋序号
     * @param zyh           住院号
     * @param hdgh          核对工号
     * @param zxgh          执行工号
     * @param operationType 操作类型
     *                      0 开始；3结束
     * @param jgid          机构id
     * @return
     */
    public BizResponse<BloodTransfusionInfo> excueteBloodTransfusion(String xdh, String xdxh, String zyh, String hdgh, String zxgh,
                                                                     String operationType, String jgid) {
        BizResponse<BloodTransfusionInfo> response = new BizResponse<>();
        try {
            response = CheckStatus(xdh, xdxh, operationType, jgid);
            if (!response.isSuccess) {
                return response;
            }
            if (operationType.equals("0")) {//开始输血
                response = Start(xdh, xdxh, hdgh, zxgh, jgid);
            } else if (operationType.equals("3")) {//结束输血
                response = End(xdh, xdxh, zxgh, jgid);
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输血计划列表数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输血计划列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取输血签收列表
     *
     * @param start  血袋号
     * @param end    血袋序号
     * @param bqid   住院号
     * @param status 核对工号
     *               0:未签收，1：已签收
     * @param jgid   机构id
     * @return
     */
    public BizResponse<BloodReciveInfo> getBloodRecieveList(String start, String end, String bqid, String status, String jgid) {
        BizResponse<BloodReciveInfo> response = new BizResponse<>();
        try {
            Date date = DateConvert.toDateTime(end, "yyyy-MM-dd");
            LocalDateTime localDateTime = DateConvert.toLocalDateTime(date);
            localDateTime = localDateTime.plusDays(1);
            end = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<BloodReciveInfo> list = service.getBloodRecieveList(start, end, bqid, status, jgid);
            for (BloodReciveInfo info : list) {
                info.SXXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, info.SXGH);
                info.QSXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, info.QSGH);
                info.CJXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, info.CXGH);
            }
            response.datalist = list;
            response.isSuccess = true;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输血签收列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输血签收列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 血液交接-取消签收
     *
     * @param xmid
     * @return
     */
    public BizResponse<String> devliyBloodRecieve(String xmid, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(xmid)) {
                response.message = "传入参数为空!";
                response.isSuccess = false;
                return response;
            }
            boolean isSucess = service.cancleBloodRecieve(xmid) > 0;
            if (isSucess) {
                response.isSuccess = true;
                response.message = "取消签收成功!";
            } else {
                response.isSuccess = false;
                response.message = "取消签收失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[血液交接-取消签收失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[血液交接-取消签收失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 血液交接-签收
     *
     * @param bloodRecieveSaveData
     * @return
     */
    public BizResponse<String> saveBloodRecieve(BloodRecieveSaveData bloodRecieveSaveData) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (bloodRecieveSaveData == null) {
                response.message = "没有要保存的数据!";
                response.isSuccess = false;
                return response;
            }
            String now = timeService.now(DataSource.LIS);
            String bytm = "";
            for (String item : bloodRecieveSaveData.SampleId) {
                bytm += item + ",";
            }
            bytm = bytm.substring(0, bytm.length() - 1);
            boolean isSucess = service.bloodRecieve(bytm, bloodRecieveSaveData.HGGH, bloodRecieveSaveData.HSGH, now) > 0;
            if (isSucess) {
                response.isSuccess = true;
                response.message = "签收成功!";
            } else {
                response.isSuccess = false;
                response.message = "签收失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[血液交接-签收失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[血液交接-签收失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取输血巡视记录
     *
     * @param sxdh 输血单号
     * @param jgid
     */
    public BizResponse<BloodTransfusionTourInfo> getBloodTransfusionTourInfoList(String sxdh, String jgid) {
        BizResponse<BloodTransfusionTourInfo> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(sxdh)) {
                response.message = "传入参数为空!";
                response.isSuccess = false;
                return response;
            }
            List<BloodTransfusionTourInfo> list = service.getBloodTransfusionTourInfoList(sxdh, jgid);
            for (BloodTransfusionTourInfo info : list) {
                info.FYMC = info.BLFY.equals("1") ? "有不良反应" : "无不良反应";
                info.XSXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, info.XSGH);
            }
            response.datalist = list;
            response.isSuccess = true;
            response.message = "获取输血巡视记录数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输血巡视记录数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输血巡视记录数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存输血巡视记录
     *
     * @param bloodTransfusionTourInfo
     */
    public BizResponse<String> saveBloodTransfusionTourInfo(BloodTransfusionTourInfo bloodTransfusionTourInfo) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (bloodTransfusionTourInfo == null) {
                response.message = "传入参数为空!";
                response.isSuccess = false;
                return response;
            }
            boolean isSucess;
            if (bloodTransfusionTourInfo.OperType.equals("0")) {
                bloodTransfusionTourInfo.XSRQ = timeService.now(DataSource.LIS);
                List<BloodTransfusionTourInfo> list = service.getBloodTransfusionTourInfoList(
                        bloodTransfusionTourInfo.SXDH, bloodTransfusionTourInfo.JGID);
                bloodTransfusionTourInfo.XSCS = String.valueOf(list.size() + 1);
                isSucess = service.addBloodTransfusionTourInfo(bloodTransfusionTourInfo) > 0;
            } else if (bloodTransfusionTourInfo.OperType.equals("1")) {
                isSucess = service.editBloodTransfusionTourInfo(bloodTransfusionTourInfo) > 0;
            } else if (bloodTransfusionTourInfo.OperType.equals("2")) {
                isSucess = service.deleteBloodTransfusionTourInfo(bloodTransfusionTourInfo) > 0;
            } else {
                response.message = "传入参数为空!";
                response.isSuccess = false;
                return response;
            }
            String msg = "";
            if (bloodTransfusionTourInfo.OperType.equals("0")) {
                msg = "新增";
            } else if (bloodTransfusionTourInfo.OperType.equals("1")) {
                msg = "修改";
            } else if (bloodTransfusionTourInfo.OperType.equals("2")) {
                msg = "删除";
            }
            if (isSucess) {
                response.isSuccess = true;
                response.message = msg + "输血巡视记录数据成功!";
            } else {
                response.isSuccess = false;
                response.message = msg + "输血巡视记录数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[编辑输血巡视记录数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[编辑输血巡视记录数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 血袋上交
     *
     * @param sxdh 输血单号
     * @param yhid 用户id
     * @param jgid
     */
    public BizResponse<String> saveBloodBagRecieve(String sxdh, String yhid, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(sxdh) || StringUtils.isBlank(yhid) || StringUtils.isBlank(jgid)) {
                response.message = "传入参数为空!";
                response.isSuccess = false;
                return response;
            }
            String now = timeService.now(DataSource.LIS);
            boolean isSucess = service.saveBloodBagRecieve(sxdh, yhid, now, jgid) > 0;
            if (isSucess) {
                response.isSuccess = true;
                response.message = "血袋上交成功!";
            } else {
                response.isSuccess = true;
                response.message = "血袋上交失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[血袋上交失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[血袋上交失败]服务内部错误!";
        }
        return response;
    }

    private BizResponse<BloodTransfusionInfo> Start(String xdh, String xdxh, String hdgh, String zxgh, String jgid)
            throws SQLException, DataAccessException {
        BizResponse<BloodTransfusionInfo> response = new BizResponse<>();
        if (hdgh.equals(zxgh)) {
            response.isSuccess = false;
            response.message = "核对人和执行人不能为同一个!";
            return response;
        }
        String now = timeService.now(DataSource.LIS);
        int count = service.startBloodTransfusion(xdh, xdxh, zxgh, hdgh, now, jgid);
        if (count > 0) {
            List<BloodTransfusionInfo> list = new ArrayList<>();
            BloodTransfusionInfo bloodTransfusionInfo = new BloodTransfusionInfo();
            bloodTransfusionInfo.KSSJ = now;
            bloodTransfusionInfo.SXR1 = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, zxgh);
            bloodTransfusionInfo.SXR2 = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, hdgh);
            list.add(bloodTransfusionInfo);
            response.datalist = list;
            response.isSuccess = true;
        } else {
            response.isSuccess = false;
            response.message = "[输血开始失败!]数据库执行错误!";
        }
        return response;
    }

    private BizResponse<BloodTransfusionInfo> End(String xdh, String xdxh, String jsr, String jgid)
            throws SQLException, DataAccessException {
        BizResponse<BloodTransfusionInfo> response = new BizResponse<>();
        String now = timeService.now(DataSource.LIS);
        int count = service.endBloodTransfusion(xdh, xdxh, jsr, now, jgid);
        if (count > 0) {
            List<BloodTransfusionInfo> list = new ArrayList<>();
            BloodTransfusionInfo bloodTransfusionInfo = new BloodTransfusionInfo();
            bloodTransfusionInfo.JSSJ = now;
            bloodTransfusionInfo.JSR = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, jsr);
            list.add(bloodTransfusionInfo);
            response.datalist = list;
            response.isSuccess = true;
        } else {
            response.isSuccess = false;
            response.message = "[输血结束失败!]数据库执行错误!";
        }
        return response;
    }

    private BizResponse<BloodTransfusionInfo> CheckStatus(String xdh, String xdxh, String operationType, String jgid)
            throws SQLException, DataAccessException {
        BizResponse<BloodTransfusionInfo> response = new BizResponse<>();
        List<BloodTransfusionInfo> bloodTransfusionInfoList = service.getBloodTransfusionInfoList(xdh, xdxh, jgid);
        for (BloodTransfusionInfo info : bloodTransfusionInfoList) {
            if (info == null) {
                continue;
            }
            if (operationType.equals("0")) {
                if (!StringUtils.isBlank(info.JSR)) {
                    response.isSuccess = false;
                    response.message = "[输血已结束不能再开始]参数传入有误!";
                    return response;
                }
                if (!StringUtils.isBlank(info.SXR1)) {
                    response.isSuccess = false;
                    response.message = "[输血已开始不能再开始]参数传入有误!";
                    return response;
                }
            } else if (operationType.equals("3")) {
                if (!StringUtils.isBlank(info.JSR)) {
                    response.isSuccess = false;
                    response.message = "[输血已结束不能再结束]参数传入有误!";
                    return response;
                }
                if (StringUtils.isBlank(info.SXR1)) {
                    response.isSuccess = false;
                    response.message = "[输血未开始不能结束]参数传入有误!";
                    return response;
                }

            }
        }
        response.isSuccess = true;
        return response;
    }
}
