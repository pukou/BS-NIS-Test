package com.bsoft.nis.service.advicequery;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.adviceexecute.PhraseModel;
import com.bsoft.nis.domain.adviceqyery.*;
import com.bsoft.nis.domain.adviceqyery.db.AdviceBqyzVo;
import com.bsoft.nis.domain.adviceqyery.db.TransfusionInfoVoTemp;
import com.bsoft.nis.domain.patient.db.SickPersonDetailVo;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.mapper.patient.PatientMapper;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.advicequery.support.AdviceQueryServiceSup;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 医嘱执行(查询)主服务
 * User: 苏泽雄
 * Date: 16/12/16
 * Time: 17:13:29
 */
@Service
public class AdviceQueryMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(AdviceQueryMainService.class);

    @Autowired
    AdviceQueryServiceSup service;
    @Autowired
    DictCachedHandler handler; // 缓存处理器
    @Autowired
    PatientMapper patientMapper; // 病人mapper
    @Autowired
    DateTimeService timeService; // 日期时间服务
    @Autowired
    PatientMainService patientService; // 病人服务

    /**
     * 获取医嘱列表
     *
     * @param zyh
     * @param lsyz
     * @param wxbz
     * @param kssj
     * @param jssj
     * @param jgid
     * @return
     */
    public BizResponse<AdviceVo> getAdviceList(String zyh, String lsyz, String wxbz,
                                               String kssj, String jssj, String jgid) {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<AdviceVo> response = new BizResponse<>();

        try {
            // 获取当前数据库时间
            String now = timeService.now(DataSource.HRP);

            // 根据住院号，获取病人类型
            String dbtype = getCurrentDataSourceDBtype();
            SickPersonDetailVo patient = patientMapper.getPatientDetail(zyh, jgid,dbtype);
            String jllx = patient.JLLX; // 1 成人  2 婴儿

            Date date = DateConvert.toDateTime(jssj, "yyyy-MM-dd");
            LocalDateTime localDateTime = DateConvert.toLocalDateTime(date);
            localDateTime = localDateTime.plusDays(1);
            jssj = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // 获取病区医嘱
            List<AdviceBqyzVo> adviceBqyzList = service
                    .getAdviceBqyzList(zyh, kssj, jssj, now, jgid, jllx, lsyz, wxbz);

            // 转化为医嘱列表
            List<AdviceVo> adviceList = buildAdviceList(adviceBqyzList, jgid);
            response.isSuccess = true;
            response.datalist = adviceList;
            response.message = "获取医嘱计划列表成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取医嘱计划列表失败]数据库查询错误";
        } catch (ParseException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取医嘱计划列表失败]时间格式解析错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取医嘱计划列表失败]服务内部错误";
        }
        return response;
    }

    /**
     * 取医嘱明细(执行记录)
     *
     * @param jlxh
     * @param jgid
     * @return
     */
    public BizResponse<AdviceDetail> getAdviceDetail(String jlxh, String jgid) {
        BizResponse<AdviceDetail> response = new BizResponse<>();

        try {
            // 获取一组医嘱（Android端未用到该数据，暂注释掉）
            // 当前用于查询是否有主记录
            keepOrRoutingDateSource(DataSource.HRP);
            List<AdviceBqyzVo> adviceBqyzList = service.getAdviceOne(jlxh, jgid);
            // List<AdviceVo> adviceList = buildAdviceList(adviceBqyzList, jgid);
            if (adviceBqyzList == null || adviceBqyzList.isEmpty()) {
                response.isSuccess = false;
                response.message = "";
                return response;
            }

            // 获取执行记录
            keepOrRoutingDateSource(DataSource.MOB);
            List<AdviceDetail> detailList = service.getAdviceRecord(jlxh, jgid);
            for (AdviceDetail detail : detailList) {
                String zxr = detail.JSGH;
                if (zxr != null && !"".equals(zxr)) {
                    detail.JSGH = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, zxr);
                }
                String ksr = detail.KSGH;
                if (ksr != null && !"".equals(ksr)) {
                    detail.KSGH = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, ksr);
                }
            }
            response.isSuccess = true;
            response.datalist = detailList;
            response.message = "获取医嘱明细成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取医嘱明细失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取医嘱明细失败]服务内部错误";
        }
        return response;
    }

    /**
     * 病区医嘱转化为医嘱列表
     *
     * @param adviceBqyzList
     * @param jgid
     * @return
     */
    private List<AdviceVo> buildAdviceList(List<AdviceBqyzVo> adviceBqyzList, String jgid)
            throws ParseException {
        String now = timeService.now(DataSource.HRP);
        List<AdviceVo> adviceList = new ArrayList<>();
        for (AdviceBqyzVo adviceBqyz : adviceBqyzList) {
            AdviceVo advice = new AdviceVo();
            advice.JLXH = adviceBqyz.JLXH;
            advice.YFSB = adviceBqyz.YFSB;
            advice.YZMC = adviceBqyz.YZMC;
            advice.YPXH = adviceBqyz.YPXH;
            advice.KZSJ = adviceBqyz.KZSJ;
            advice.TZSJ = adviceBqyz.TZSJ;
            advice.YPDJ = adviceBqyz.YPDJ;
            advice.BZXX = adviceBqyz.BZXX;
            advice.YZZH = adviceBqyz.YZZH;
            advice.LSYZ = adviceBqyz.LSYZ;
            advice.XMLX = adviceBqyz.XMLX;
            advice.YPLX = adviceBqyz.YPLX;

            String xmlx = adviceBqyz.XMLX;
            String ypxh = adviceBqyz.YPXH;
            /*advice.SLDW = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", ypxh,
                            "SLDW");*/
            advice.SLDW = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", ypxh,
                            "BFDW");//病房单位
            advice.JLDW = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", ypxh,
                            "JLDW");
            advice.SYPCMC = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_SYPC, jgid, adviceBqyz.SYPC);
            advice.YPYFMC = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_YPYF, jgid, adviceBqyz.YPYF);
            advice.KZYSMC = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, adviceBqyz.KZYS);
            advice.TZYSMC = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, adviceBqyz.TZYS);
            advice.YZDL = medicalAdviceTypeToDL(xmlx);
          /*  if (adviceBqyz.YCJL.contains(".")) {
                advice.YCJLMS = adviceBqyz.YCJL.substring(0, adviceBqyz.YCJL.indexOf("."));
            }
            if (adviceBqyz.YCSL.contains(".")) {
                advice.YCSLMS = adviceBqyz.YCSL.substring(0, adviceBqyz.YCSL.indexOf("."));
            }*/
            advice.YCJLMS = adviceBqyz.YCJL + "(" +advice.JLDW+ ")";
            advice.YCSLMS = adviceBqyz.YCSL + "(" +advice.SLDW+ ")";
            //根据开始时间，结束时间，判定是否“新开医嘱”、“新停医嘱”,决定文本颜色
            //true：新开(蓝色)，false：新停(红色)，空：两者均不是
            String kzsj = adviceBqyz.KZSJ;
            String tzsj = adviceBqyz.TZSJ;
            if (kzsj != null && !"".equals(kzsj) && DateUtil.between(kzsj, now) < 1) {
                advice.TEXTCOLOR = "true";
            }
            if (tzsj != null && !"".equals(tzsj) && DateUtil.between(tzsj, now) < 1) {
                advice.TEXTCOLOR = "false";
            }
            // 无效标志
            String qrsj = adviceBqyz.QRSJ;
            Boolean wxbz = (adviceBqyz.LSYZ == 1 && qrsj != null && !""
                    .equals(qrsj)) || (adviceBqyz.LSYZ == 0 && (tzsj != null && !""
                    .equals(tzsj) && DateUtil.between(tzsj, now) > 0) || "1"
                    .equals(adviceBqyz.LSBZ)) || "1".equals(adviceBqyz.ZFBZ);
            advice.WXBZ = String.valueOf(wxbz);

            adviceList.add(advice);
        }
        return adviceList;
    }

    /**
     * 获取医嘱大类
     *
     * @param xmlx
     * @return
     */
    // TODO 未完成
    private String medicalAdviceTypeToDL(String xmlx) {
        return "-1";
        /*
        foreach (AdviceType mt in Global.AdviceTypeList)
            {
                if (mt.DMLX != null && mt.DMLX.Contains(xmlx))
                {
                    return mt.DMSB;
                }
            }
            return "-1";
		 */
    }

    /**
     * 获取病人输液单以及明细信息
     *
     * @param zyh
     * @param kssj
     * @param syzt
     * @param jgid
     * @return
     */
    public BizResponse<TransfusionData> getTransfusionListPatient(String zyh, String kssj,
                                                                  String syzt, String jgid) {
        BizResponse<TransfusionData> response = new BizResponse<>();
        TransfusionData data = new TransfusionData();
        List<TransfusionVo> syd;
        List<TransfusionInfoVo> symx = new ArrayList<>();
        try {
            // 结束日期
            String jssj = DateUtil.dateoffDays(kssj, "1");
            // 获取输液单
            keepOrRoutingDateSource(DataSource.MOB);
            syd = service.getTransfusionListByZyh(zyh, kssj, jssj, syzt, jgid);
            for (TransfusionVo transfusion : syd) {
                String ksgh = transfusion.KSGH;
                if (ksgh != null && !"".equals(ksgh)) {
                    transfusion.KSGH = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, ksgh);
                }
                String jsgh = transfusion.JSGH;
                if (jsgh != null && !"".equals(jsgh)) {
                    transfusion.JSGH = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, jsgh);
                }
            }
            data.SYD = syd;

            // 获取输液单明细
            // 获取输液单号
            keepOrRoutingDateSource(DataSource.MOB);
            List<String> sydhList = service.getSydhByZyh(zyh, kssj, jssj, syzt, jgid);

            if (sydhList != null && !sydhList.isEmpty()) {
                // 获取明细
                List<TransfusionInfoVoTemp> infoTempList = service
                        .getTransfusionInfoList(sydhList);
                keepOrRoutingDateSource(DataSource.HRP);
                for (TransfusionInfoVoTemp infoTemp : infoTempList) {
                    TransfusionInfoVo info = new TransfusionInfoVo();
                    info.YZXH = infoTemp.YZXH;
                    info.SYDH = infoTemp.SYDH;
                    // 获取医嘱名称  TODO 查询次数过多，待优化
                    String yzxh = infoTemp.YZXH;
                    info.YZMC = service.getYzmcByYzxh(yzxh, jgid);

                    String ycjl = infoTemp.YCJL;
                    String ycsl = infoTemp.YCSL;
                    if (ycjl != null && !"".equals(ycjl) && Float.parseFloat(ycjl) != 0f) {
                        info.JLXX = ycjl + infoTemp.JLDW;
                    }
                    if (ycsl != null && !"".equals(ycsl) && Float.parseFloat(ycsl) != 0f) {
                        info.SLXX = ycsl + infoTemp.SLDW;
                    }
                    symx.add(info);
                }
            }
            data.SYMX = symx;

            response.isSuccess = true;
            response.data = data;
            response.message = "获取输液单成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输液单失败]数据库查询错误";
        } catch (ParseException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输液单失败]时间格式解析错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输液单失败]服务内部错误";
        }
        return response;
    }

    /**
     * 查询单条输液的巡视记录
     *
     * @param sydh
     * @param jgid
     * @return
     */
    public BizResponse<TransfusionPatrolRecord> GetTransfusion(String sydh, String jgid) {
        BizResponse<TransfusionPatrolRecord> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            // 获取输液巡视记录
            List<TransfusionPatrolRecord> patrolList = service
                    .getTransfusionPatrolList(sydh, jgid);
            if (patrolList != null && !patrolList.isEmpty()) {
                // 获取输液反应类型
                List<PhraseModel> reactionList = service.getTransfusionReactionList(jgid);
                for (TransfusionPatrolRecord patrol : patrolList) {
                    patrol.XSXM = handler
                            .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid,
                                    patrol.XSGH);
                    for (PhraseModel reaction : reactionList) {
                        if (reaction.DYXH.equals(patrol.SYFY)) {
                            patrol.FYMC = reaction.DYMS;
                        }
                    }
                }
            }
            response.isSuccess = true;
            response.datalist = patrolList;
            response.message = "输液巡视记录查询成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[输液巡视记录查询失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[输液巡视记录查询失败]服务内部错误";
        }
        return response;
    }

    /**
     * 获取输液反应类型
     *
     * @return
     */
    public BizResponse<PhraseModel> GetTransfusionReaction(String jgid) {
        BizResponse<PhraseModel> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            List<PhraseModel> reactionList = service.getTransfusionReactionList(jgid);
            response.isSuccess = true;
            response.datalist = reactionList;
            response.message = "获取输液反应类型成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输液反应类型失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取输液反应类型失败]服务内部错误";
        }
        return response;
    }

    /**
     * 获取病人列表  获取全部病人，然后根据type进行过滤
     *
     * @param bqid
     * @param type      医嘱相关包括：2 医嘱  3 口服单  4 注射单  5 输液单
     * @param starttime
     * @param endtime
     * @param hsgh
     * @param jgid
     * @return
     */
    public BizResponse<SickPersonVo> GetPatientList(String bqid, Integer type,
                                                    Integer starttime, Integer endtime, String hsgh, String jgid) {
        // 获取病区全部病人(我的病人)
        BizResponse<SickPersonVo> response = patientService.getDeptPatients(bqid, hsgh, jgid);
        List<SickPersonVo> patients = response.datalist;
        List<SickPersonVo> patientList = new ArrayList<>();
        try {
            if (starttime == -1 || endtime == -1) {
                return response;
            }
            LocalDateTime today = LocalDate.now().atStartOfDay();
            String kssj = today.plus(starttime, ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String jssj = today.plus(endtime, ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            switch (type) {
                case 2: // 医嘱
                    break;
                case 3: // 口服单
                    for (SickPersonVo patient : patients) {
                        List<String> kfdhList = service
                                .getKfdhListByZyh(kssj, jssj, patient.ZYH, jgid);
                        if (kfdhList == null || kfdhList.isEmpty()) {
                            continue;
                        }
                        patientList.add(patient);
                    }
                    response.isSuccess = true;
                    response.datalist = patientList;
                    break;
                case 4: // 注射单
                    for (SickPersonVo patient : patients) {
                        List<String> zsdhList = service
                                .getZsdhListByZyh(kssj, jssj, patient.ZYH, jgid);
                        if (zsdhList == null || zsdhList.isEmpty()) {
                            continue;
                        }
                        patientList.add(patient);
                    }
                    response.isSuccess = true;
                    response.datalist = patientList;
                    break;
                case 5: // 输液单
                    for (SickPersonVo patient : patients) {
                        List<String> sydhList = service
                                .getSydhByZyh(patient.ZYH, kssj, jssj, null, jgid);
                        if (sydhList == null || sydhList.isEmpty()) {
                            continue;
                        }
                        patientList.add(patient);
                    }
                    response.isSuccess = true;
                    response.datalist = patientList;
                    break;
                default:
                    response.isSuccess = false;
                    response.datalist = null;
                    response.message = "服务不支持该类型";
                    break;
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取病人列表失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取病人列表失败]服务内部错误";
        }
        return response;
    }
}
