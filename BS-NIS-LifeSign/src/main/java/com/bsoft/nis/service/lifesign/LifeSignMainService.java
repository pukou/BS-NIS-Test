package com.bsoft.nis.service.lifesign;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.clinicalevent.ClinicalEventInfo;
import com.bsoft.nis.domain.clinicalevent.ClinicalEventSaveData;
import com.bsoft.nis.domain.clinicalevent.ClinicalEventType;
import com.bsoft.nis.domain.lifesign.*;
import com.bsoft.nis.domain.patient.db.SickPersonDetailVo;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.Project;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.lifesign.support.LifeSignServiceSup;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import com.bsoft.nis.util.date.DateCompare;
import com.bsoft.nis.util.date.DateUtil;
import ctd.net.rpc.Client;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: 生命体征主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-10-27
 * Time: 14:32
 * Version:
 */
@Service
public class LifeSignMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(LifeSignMainService.class);

    @Autowired
    LifeSignServiceSup service; // 生命体征服务

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
     * 获取生命体征控件json
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    public BizResponse<LifeSignTypeItem> getLifeSignTypeItemListOld(String zyh, String bqid, String jgid) {
        BizResponse<LifeSignTypeItem> response = new BizResponse<>();
        try {
            SickPersonDetailVo sickPersonDetailVo = patientServiceSup.getPatientDetail(zyh, jgid);
            List<LifeSignQualityInfo> lifeSignQualityInfoList = service.getQualityInfoList();
            List<LifeSignTypeItem> lifeSignTypeItemList = service.getLifeSignTypeItemList(bqid, jgid);
            for (int i = 0; i < lifeSignTypeItemList.size(); i++) {
                LifeSignTypeItem lifeSignTypeItem = lifeSignTypeItemList.get(i);
                List<LifeSignInputItem> lifeSignInputItemList = service.getLifeSignInputItemList(lifeSignTypeItem.LBH);
                lifeSignTypeItem.LifeSignInputItemList = lifeSignInputItemList;
                for (int j = 0; j < lifeSignInputItemList.size(); j++) {
                    LifeSignInputItem lifeSignInputItem = lifeSignInputItemList.get(j);
                    List<LifeSignControlItem> lifeSignControlItemList = service.getLifeSignControlItemList(lifeSignInputItem.SRXH);
                    lifeSignInputItem.LifeSignControlItemList = lifeSignControlItemList;
                    for (int k = 0; k < lifeSignControlItemList.size(); k++) {
                        LifeSignControlItem lifeSignControlItem = lifeSignControlItemList.get(k);
                        List<LifeSignOptionItem> LifeSignOptionItemList = null;
                        if (lifeSignControlItem.TSBZ != null && !lifeSignControlItem.TSBZ.equals("0")) {
                            lifeSignControlItem.KJLX = service.getLifeSignKjlxByTsbs(lifeSignControlItem.TSBZ);
                        }
                        GetExctionInfo(zyh, lifeSignControlItem, jgid, lifeSignQualityInfoList, sickPersonDetailVo);
                        if (Objects.equals(lifeSignControlItem.KJLX, "3")) {
                            LifeSignOptionItemList = new ArrayList<>();
                            List<LifeSignInputItem> lifeSignInputItemListTemp = service.getLifeSignInputItemList(lifeSignControlItem.XSLB);
                            for (int x = 0; x < lifeSignInputItemListTemp.size(); x++) {
                                LifeSignOptionItem lifeSignOptionItem = new LifeSignOptionItem();
                                lifeSignOptionItem.XZH = lifeSignInputItemListTemp.get(x).SRXH;
                                lifeSignOptionItem.XZNR = lifeSignInputItemListTemp.get(x).SRXM;
                                LifeSignOptionItemList.add(lifeSignOptionItem);
                            }
                        }
                        if (Objects.equals(lifeSignControlItem.KJLX, "4")) {
                            /*
                            升级编号【56010051】============================================= start
                            PB端维护单纯勾选数字输入时候，PDA端不可输入
                            ================= Classichu 2017/11/20 17:40
                            */
                            /*if (lifeSignControlItem.QTSR.equals("0")) {
                                lifeSignControlItem.SZSR = "0";
                            }*/
                            /* =============================================================== end */
                            if (lifeSignControlItem.SZSR.equals("0")) {
                                lifeSignControlItem.ZCSX = "";
                                lifeSignControlItem.ZCXX = "";
                                lifeSignControlItem.FFSX = "";
                                lifeSignControlItem.FFXX = "";
                            }
                            if (lifeSignControlItem.TSBZ != null && !lifeSignControlItem.TSBZ.equals("0")) {
                                LifeSignOptionItemList = service.getLifeSignSpecialOptionItemList(lifeSignControlItem.KJH);
                            } else {
                                LifeSignOptionItemList = service.getLifeSignOptionItemList(lifeSignControlItem.KJH);
                            }
                        }
                        lifeSignControlItem.LifeSignOptionItemList = LifeSignOptionItemList;

                    }
                }
            }
            response.datalist = lifeSignTypeItemList;
            response.isSuccess = true;
            response.message = "获取生命体征控件成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征控件]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征控件]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取生命体征控件json
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    public BizResponse<LifeSignTypeItem> getLifeSignTypeItemList(String zyh, String bqid, String jgid) {
        BizResponse<LifeSignTypeItem> response = new BizResponse<>();
        try {
            SickPersonDetailVo sickPersonDetailVo = patientServiceSup.getPatientDetail(zyh, jgid);
            List<LifeSignQualityInfo> lifeSignQualityInfoList = service.getQualityInfoList();
            //
            List<LifeSignTypeItem> lifeSignTypeItemList = service.getLifeSignTypeItemList(bqid, jgid);
            //
            List<LifeSignOptionItem> allLifeSignOptionItemList = service.getAllLifeSignOptionItemList();
            List<LifeSignControlItem> allLifeSignControlItemList = service.getAllLifeSignControlItemList();
            List<LifeSignInputItem> allLifeSignInputItemList = service.getAllLifeSignInputItemList();
            //
            for (int i = 0; i < lifeSignTypeItemList.size(); i++) {
                LifeSignTypeItem lifeSignTypeItem = lifeSignTypeItemList.get(i);
//                List<LifeSignInputItem> lifeSignInputItemList = service.getLifeSignInputItemList(lifeSignTypeItem.LBH);
                List<LifeSignInputItem> lifeSignInputItemList = new ArrayList<>();
                for (LifeSignInputItem allLifeSignInputItem : allLifeSignInputItemList) {
                    if (allLifeSignInputItem.LBH.equals(lifeSignTypeItem.LBH)) {
                        lifeSignInputItemList.add(allLifeSignInputItem);
                    }
                }
                lifeSignTypeItem.LifeSignInputItemList = lifeSignInputItemList;
                for (int j = 0; j < lifeSignInputItemList.size(); j++) {
                    LifeSignInputItem lifeSignInputItem = lifeSignInputItemList.get(j);
                    List<LifeSignControlItem> lifeSignControlItemList = new ArrayList<>();
                    // List<LifeSignControlItem> lifeSignControlItemList = service.getLifeSignControlItemList(lifeSignInputItem.SRXH);
                    for (LifeSignControlItem allLifeSignControlItem : allLifeSignControlItemList) {
//      FIXME                   allLifeSignControlItem.XMDW=handler.getValueByKeyFromCached(CachedDictEnum.MOB_EMRDMZD, jgid, allLifeSignControlItem.XMDW);
                        if (allLifeSignControlItem.SRXH.equals(lifeSignInputItem.SRXH)) {
                            lifeSignControlItemList.add(allLifeSignControlItem);
                        }
                    }
                    lifeSignInputItem.LifeSignControlItemList = lifeSignControlItemList;
                    for (int k = 0; k < lifeSignControlItemList.size(); k++) {
                        LifeSignControlItem lifeSignControlItem = lifeSignControlItemList.get(k);
                        List<LifeSignOptionItem> LifeSignOptionItemList = null;
                        if (lifeSignControlItem.TSBZ != null && !lifeSignControlItem.TSBZ.equals("0")) {
                            lifeSignControlItem.KJLX = service.getLifeSignKjlxByTsbs(lifeSignControlItem.TSBZ);
                        }
                        GetExctionInfo(zyh, lifeSignControlItem, jgid, lifeSignQualityInfoList, sickPersonDetailVo);
                        if (Objects.equals(lifeSignControlItem.KJLX, "3")) {
                            LifeSignOptionItemList = new ArrayList<>();
                            // List<LifeSignInputItem> lifeSignInputItemListTemp = service.getLifeSignInputItemList(lifeSignControlItem.XSLB);
                            List<LifeSignInputItem> lifeSignInputItemListTemp = new ArrayList<>();
                            for (LifeSignInputItem allLifeSignInputItem : allLifeSignInputItemList) {
                                if (allLifeSignInputItem.LBH.equals(lifeSignControlItem.XSLB)) {
                                    lifeSignInputItemListTemp.add(allLifeSignInputItem);
                                }
                            }
                            for (int x = 0; x < lifeSignInputItemListTemp.size(); x++) {
                                LifeSignOptionItem lifeSignOptionItem = new LifeSignOptionItem();
                                lifeSignOptionItem.XZH = lifeSignInputItemListTemp.get(x).SRXH;
                                lifeSignOptionItem.XZNR = lifeSignInputItemListTemp.get(x).SRXM;
                                LifeSignOptionItemList.add(lifeSignOptionItem);
                            }
                        }
                        if (Objects.equals(lifeSignControlItem.KJLX, "4")) {
                            LifeSignOptionItemList = new ArrayList<>();
                             /*
                            升级编号【56010051】============================================= start
                            PB端维护单纯勾选数字输入时候，PDA端不可输入
                            ================= Classichu 2017/11/20 17:40
                            */
                            /*if (lifeSignControlItem.QTSR.equals("0")) {
                                lifeSignControlItem.SZSR = "0";
                            }*/
                            /* =============================================================== end */
                            if (lifeSignControlItem.SZSR.equals("0")) {
                                lifeSignControlItem.ZCSX = "";
                                lifeSignControlItem.ZCXX = "";
                                lifeSignControlItem.FFSX = "";
                                lifeSignControlItem.FFXX = "";
                            }
                            if (lifeSignControlItem.TSBZ != null && !lifeSignControlItem.TSBZ.equals("0")) {
                                LifeSignOptionItemList = service.getLifeSignSpecialOptionItemList(lifeSignControlItem.KJH);
                            } else {
//                                LifeSignOptionItemList = service.getLifeSignOptionItemList(lifeSignControlItem.KJH);
                                for (LifeSignOptionItem allLifeSignOptionItem : allLifeSignOptionItemList) {
                                    if (allLifeSignOptionItem.KJH.equals(lifeSignControlItem.KJH)) {
                                        LifeSignOptionItemList.add(allLifeSignOptionItem);
                                    }
                                }

                            }
                        }
                        lifeSignControlItem.LifeSignOptionItemList = LifeSignOptionItemList;

                    }
                }
            }
            response.datalist = lifeSignTypeItemList;
            response.isSuccess = true;
            response.message = "获取生命体征控件成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征控件]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征控件]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取生命体征(活动)控件json
     *
     * @param srxh 输入序号
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<LifeSignInputItem> getLifeSignItem(String srxh, String zyh, String jgid) {
        BizResponse<LifeSignInputItem> response = new BizResponse<>();
        try {
            SickPersonDetailVo sickPersonDetailVo = patientServiceSup.getPatientDetail(zyh, jgid);
            List<LifeSignQualityInfo> lifeSignQualityInfoList = service.getQualityInfoList();
            LifeSignInputItem lifeSignInputItem = service.getLifeSignInputItem(srxh);
            List<LifeSignControlItem> lifeSignControlItemList = service.getLifeSignControlItemList(lifeSignInputItem.SRXH);
            lifeSignInputItem.LifeSignControlItemList = lifeSignControlItemList;
            for (int k = 0; k < lifeSignControlItemList.size(); k++) {
                LifeSignControlItem lifeSignControlItem = lifeSignControlItemList.get(k);
                List<LifeSignOptionItem> LifeSignOptionItemList = null;
                if (lifeSignControlItem.TSBZ != null && !lifeSignControlItem.TSBZ.equals("0")) {
                    lifeSignControlItem.KJLX = service.getLifeSignKjlxByTsbs(lifeSignControlItem.TSBZ);
                }
                GetExctionInfo(zyh, lifeSignControlItem, jgid, lifeSignQualityInfoList, sickPersonDetailVo);
                if (Objects.equals(lifeSignControlItem.KJLX, "3")) {
                    LifeSignOptionItemList = new ArrayList<>();
                    List<LifeSignInputItem> lifeSignInputItemListTemp = service.getLifeSignInputItemList(lifeSignControlItem.XSLB);
                    for (int x = 0; x < lifeSignInputItemListTemp.size(); x++) {
                        LifeSignOptionItem lifeSignOptionItem = new LifeSignOptionItem();
                        lifeSignOptionItem.XZH = lifeSignInputItemListTemp.get(x).SRXH;
                        lifeSignOptionItem.XZNR = lifeSignInputItemListTemp.get(x).SRXM;
                        LifeSignOptionItemList.add(lifeSignOptionItem);
                    }
                }
                if (Objects.equals(lifeSignControlItem.KJLX, "4")) {
                     /*
                            升级编号【56010051】============================================= start
                            PB端维护单纯勾选数字输入时候，PDA端不可输入
                            ================= Classichu 2017/11/20 17:40
                            */
                            /*if (lifeSignControlItem.QTSR.equals("0")) {
                                lifeSignControlItem.SZSR = "0";
                            }*/
                            /* =============================================================== end */
                    if (lifeSignControlItem.SZSR.equals("0")) {
                        lifeSignControlItem.ZCSX = "";
                        lifeSignControlItem.ZCXX = "";
                        lifeSignControlItem.FFSX = "";
                        lifeSignControlItem.FFXX = "";
                    }
                    if (lifeSignControlItem.TSBZ != null && !lifeSignControlItem.TSBZ.equals("0")) {
                        LifeSignOptionItemList = service.getLifeSignSpecialOptionItemList(lifeSignControlItem.KJH);
                    } else {
                        LifeSignOptionItemList = service.getLifeSignOptionItemList(lifeSignControlItem.KJH);
                    }
                }
                lifeSignControlItem.LifeSignOptionItemList = LifeSignOptionItemList;

            }
            response.data = lifeSignInputItem;
            response.isSuccess = true;
            response.message = "获取生命体征活动控件成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征活动控件]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征活动控件]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存生命体征数据
     *
     * @param lifeSignSaveData 要保存的数据对象
     * @return
     */
    public BizResponse<LifeSignSync> saveLifeSignData(LifeSignSaveData lifeSignSaveData) {
        BizResponse<LifeSignSync> response = new BizResponse<>();
        LifeSignSync lifeSignSync = new LifeSignSync();
        try {
            if (null == lifeSignSaveData.lifeSignSaveDataItemList || lifeSignSaveData.lifeSignSaveDataItemList.size() == 0) {
                response.isSuccess = false;
                response.message = "没有要保存的数据";
                return response;
            }
            keepOrRoutingDateSource(DataSource.HRP);
            SickPersonDetailVo sickPersonDetailVo = patientServiceSup.getPatientDetail(lifeSignSaveData.ZYH, lifeSignSaveData.JGID);
            String jhbz = lifeSignSaveData.IsTemp.equals("0") ? "1" : "0";
            if (StringUtils.isBlank(lifeSignSaveData.CJZH) || lifeSignSaveData.CJZH.equals("0")) {
                lifeSignSaveData.CJZH = String.valueOf(identityService.getIdentityMax("BQ_SMTZ_GROUP", DataSource.ENR));
            } else {
                service.deleteSmtzByCjzh(lifeSignSaveData.CJZH);
                // 同步
//	            deleSyncData("5", lifeSignSaveData.CJZH, "0", "0", lifeSignSaveData.JGID);
            }
            String cjh = String.valueOf(identityService.getIdentityMax("BQ_SMTZ", lifeSignSaveData.lifeSignSaveDataItemList.size(), DataSource.ENR).datalist.get(0));
            String cjsj = !StringUtils.isBlank(lifeSignSaveData.TempTime)
                    ? (timeService.now(DataSource.ENR).substring(0,10).concat(" ")).concat(lifeSignSaveData.TempTime.concat(":00"))
                    : timeService.now(DataSource.ENR);
            String jlsj = timeService.now(DataSource.ENR);
            String hljl_cjsj = timeService.now(DataSource.ENR);//DateTime.MinValue;//护理记录用的采集时间
            List<LifeSignRealSaveDataItem> lifeSignRealSaveDataItemList = new ArrayList<>();
            for (LifeSignSaveDataItem item : lifeSignSaveData.lifeSignSaveDataItemList) {
                String xmh = item.TZXM;
                String fcgl = null;//复测关联
                String fcnr = null;//复测内容
                String fcbz = "0";//复测标志
                String xmxb = null;//项目下标
                //TODO 前端没有解析此方法的返回结果
                //LifeSignCheck(lifeSignSaveData.ZYH, xmh, item.Data, item.YCBZ, lifeSignSaveData.JGID);
                for (LifeSignSaveDataTerm term : item.lifeSignSaveDataTermList) {
                    if (term.Name.equals("Twfc")) {//设置体温复测节点
                        if (xmh.equals("1")) {
                            fcbz = "1";
                            fcgl = term.ID;
                            fcnr = term.Data;
                        }
                    } else if (term.Name.equals("Ttfc")) {
                        if (xmh.equals("502")) {
                            fcbz = "1";
                            fcgl = term.ID;
                        }
                    } else {//特殊控件
                        String tsid = term.ID;//特殊项目ID
                        String tsnr = term.Data;//特殊项目内容
                        if (!StringUtils.isBlank(tsid) && !StringUtils.isBlank(tsnr)) {
                            String lbbz = service.getLifeSignLbbzByTsbs(tsid);
                            if (lbbz.equals("1")) {//更新采集时间
                                if (!tsnr.equals("-1")) {
//                                    Date date = DateConvert.toDateTime(cjsj, "yyyy-MM-dd");
//                                    LocalDateTime datetime = LocalDateTime.of(DateConvert.toLocalDate(DateConvert.toLocalDateTime(date)), LocalTime.parse("00:00:00"));
//                                    datetime = datetime.plusMinutes(Long.valueOf(tsnr));
//                                    cjsj = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                                if (dt_tsCjsj > hljl_cjsj)
//                                        hljl_cjsj = dt_tsCjsj;
                                }
                            } else if (lbbz.equals("2")) {//更新项目下标
                                xmxb = tsnr;
                            }
                        }
                    }
                }
                String twdxs = service.getLifeSignTwdxsByXmh(xmh);
                twdxs = StringUtils.isBlank(twdxs) ? "0" : twdxs;
                LifeSignRealSaveDataItem lifeSignRealSaveDataItem = new LifeSignRealSaveDataItem();
                lifeSignRealSaveDataItem.CJH = cjh;
                lifeSignRealSaveDataItem.ZYH = lifeSignSaveData.ZYH;
                lifeSignRealSaveDataItem.JHBZ = jhbz;
                lifeSignRealSaveDataItem.CJSJ = cjsj;
                lifeSignRealSaveDataItem.XMH = xmh;
                lifeSignRealSaveDataItem.TZNR = item.Data;
                lifeSignRealSaveDataItem.CJZH = lifeSignSaveData.CJZH;
                lifeSignRealSaveDataItem.BRKS = sickPersonDetailVo.BRKS;
                lifeSignRealSaveDataItem.BRBQ = sickPersonDetailVo.BRBQ;
                lifeSignRealSaveDataItem.BRCH = sickPersonDetailVo.BRCH;
                lifeSignRealSaveDataItem.JLSJ = jlsj;
                lifeSignRealSaveDataItem.JLGH = lifeSignSaveData.URID;
                lifeSignRealSaveDataItem.YCBZ = item.YCBZ;
                lifeSignRealSaveDataItem.FCBZ = fcbz;
                lifeSignRealSaveDataItem.FCGL = fcgl;
                lifeSignRealSaveDataItem.BZXX = fcnr;
                lifeSignRealSaveDataItem.XMXB = xmxb;
                lifeSignRealSaveDataItem.TWDXS = twdxs;
                lifeSignRealSaveDataItem.ZFBZ = "0";
                lifeSignRealSaveDataItem.JGID = lifeSignSaveData.JGID;
                lifeSignRealSaveDataItemList.add(lifeSignRealSaveDataItem);
                cjh = String.valueOf(Integer.parseInt(cjh) + 1);
            }

            boolean isSucess = service.addLifeSignDataBatch(lifeSignRealSaveDataItemList) > 0;
            if (isSucess) {
                if (lifeSignSaveData.customIsSync) {
                    //协和客户化需求
                    Response<SelectResult> syncResponse = buildSyncData(lifeSignSaveData);
                    if (syncResponse.ReType == 2) {
                        lifeSignSync.IsSync = true;
                        lifeSignSync.SyncData = syncResponse.Data;
                    }
                }
                //保存成功后  保存lifeSignRealSaveDataItemList
                lifeSignSync.mLifeSignRealSaveDataItemList = lifeSignRealSaveDataItemList;
                //
                response.isSuccess = true;
                response.message = "保存生命体征数据成功!";
                response.data = lifeSignSync;
            } else {
                response.isSuccess = false;
                response.message = "保存生命体征数据失败!";
                response.data = lifeSignSync;
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存生命体征数据]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存生命体征数据]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取生命体征历史数据
     *
     * @param start 开始日期
     * @param end   结束日期
     * @param zyh   住院号
     * @param jgid  机构id
     * @return
     */
    public BizResponse<LifeSignHistoryData> getLifeSignHistoryData(String start, String end, String zyh, String jgid) {
        BizResponse<LifeSignHistoryData> response = new BizResponse<>();
        try {
            //后一天的凌晨
              end = DateUtil.dateoffDays(end, "1");
        } catch (ParseException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征历史数据失败]服务内部错误!";
        }
        try {

            LifeSignHistoryData lifeSignHistoryData = new LifeSignHistoryData();
            lifeSignHistoryData.lifeSignHistoryDataType = service.getLifeSignHistoryDataType();
            LifeSignHistoryDataType lifeSignHistoryDataType = new LifeSignHistoryDataType();
            lifeSignHistoryDataType.XMH = 0;
            lifeSignHistoryDataType.XMMC = "全部";
            lifeSignHistoryData.lifeSignHistoryDataType.add(0, lifeSignHistoryDataType);
            lifeSignHistoryData.lifeSignHistoryDataItem = service.getLifeSignHistoryDataItem(zyh, start, end);

            response.data = lifeSignHistoryData;
            response.isSuccess = true;
            response.message = "获取生命体征历史数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征历史数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征历史数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 删除(作废)生命体征数据
     *
     * @param cjh  采集号
     * @param jgid
     * @return
     */
    public BizResponse<String> deleteLifeSignHistoryData(String cjh, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(cjh)) {
                response.isSuccess = false;
                response.message = "请先选择要删除的数据行";
                return response;
            }
            int num = service.deleteLifeSignHistoryData(cjh);

            // 同步
//	        String cjzh = service.getCjzhByCjh(cjh, jgid);
//	        deleSyncData("5", cjzh, "0", "0", jgid);

            response.data = String.valueOf(num);
            response.isSuccess = true;
            response.message = "删除生命体征数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除生命体征数据]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除生命体征数据]服务内部错误!";
        }
        return response;
    }
    public BizResponse<String> updateLifeSignHistoryData(String cjh,String value, String jgid) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(cjh)) {
                response.isSuccess = false;
                response.message = "请先选择要修改的数据行";
                return response;
            }
            int num = service.updateLifeSignHistoryData(cjh,value);

            // 同步
//	        String cjzh = service.getCjzhByCjh(cjh, jgid);
//	        deleSyncData("5", cjzh, "0", "0", jgid);

            response.data = String.valueOf(num);
            response.isSuccess = true;
            response.message = "修改生命体征数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[修改生命体征数据]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[修改生命体征数据]服务内部错误!";
        }
        return response;
    }
    /**
     * 获取生命体征复测历史数据
     *
     * @param tzxm 体征项目
     * @param zyh  住院号
     * @param jgid 机构id
     * @return
     */
    public BizResponse<LifeSignDoubleCheckHistoryData> getLifeSignDoubleCheckHistoryData(String tzxm, String zyh, String jgid) {
        BizResponse<LifeSignDoubleCheckHistoryData> response = new BizResponse<>();
        try {
            Date jssj = service.getLifeSignDoubleCheckMaxCjsj(tzxm, zyh);
            String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jssj);
            String start = DateUtil.dateoffDays(end, "-1");
            LifeSignDoubleCheckHistoryData lifeSignDoubleCheckHistoryData = new LifeSignDoubleCheckHistoryData();
            lifeSignDoubleCheckHistoryData.lifeSignDoubleCheckHistoryDataItemList = service.getLifeSignDoubleCheckHistoryDataItem(tzxm, zyh, start, end);
            if (tzxm.equals("1")) {
                lifeSignDoubleCheckHistoryData.lifeSignDoubleCheckCoolingMeasureList = service.getLifeSignDoubleCheckCoolingMeasure();
            }

            response.data = lifeSignDoubleCheckHistoryData;
            response.isSuccess = true;
            response.message = "获取生命体征复测历史数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征复测历史数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征复测历史数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取生命体征采集时刻表
     *
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     */
    public BizResponse<LifeSignTimeEntity> getTimePointList(String bqid, String jgid) {
        BizResponse<LifeSignTimeEntity> response = new BizResponse<>();
        try {
            List<LifeSignTimeEntity> lifeSignTimeEntityList = new ArrayList<>();
            List<String> list = systemParamService.getUserParams("1", "ENR", "ENR_TWD_QCSJD", jgid, DataSource.MOB).datalist;
            String userPar = (list != null && list.size() > 0) ? list.get(0) : "1";
            int index = Integer.parseInt(userPar);
            index = index >= 1 ? index - 1 : index;
            for (String[] arr : timePointsArray) {
                LifeSignTimeEntity lifeSignTimeEntity = new LifeSignTimeEntity();
                lifeSignTimeEntity.NAME = arr[index];
                lifeSignTimeEntity.VALUE = GetTimeInvite(arr[index]);
                lifeSignTimeEntityList.add(lifeSignTimeEntity);
            }
            response.datalist = lifeSignTimeEntityList;
            response.isSuccess = true;
            response.message = "获取生命体征采集时刻表数据成功!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取生命体征采集时刻表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取临床事件类型列表
     *
     * @param zyh  住院号
     * @param yhid 用户id
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     */
    public BizResponse<ClinicalEventType> getClinicalEventTypeList(String zyh, String yhid, String bqid, String jgid) {
        BizResponse<ClinicalEventType> response = new BizResponse<>();
        try {
            response.datalist = realGetClinicalEventTypeList(zyh, yhid, bqid, jgid);
            response.isSuccess = true;
            response.message = "获取临床事件类型列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取临床事件类型列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取临床事件类型列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存临床事件
     *
     * @param clinicalEventSaveData 要保存的数据
     * @return
     */
    public BizResponse<ClinicalEventType> saveClinicalEventData(ClinicalEventSaveData clinicalEventSaveData) {
        BizResponse<ClinicalEventType> response = new BizResponse<>();
        try {
            List<ClinicalEventInfo> clinicalEventTypeInfoList = clinicalEventSaveData.ClinicalEventType.ClinicalEventInfoList;
            for (ClinicalEventInfo info : clinicalEventTypeInfoList) {
                if (info.MODIFIED) {
                    if (info.SJXH.equals("-1")) {
                        //add
                        info.SJXH = "add:" + String.valueOf(identityService.getIdentityMax("EMR_LCSJBC", DataSource.ENR));
                    }
                }
            }
            keepOrRoutingDateSource(DataSource.ENR);
            response = realSaveClinicalEventData(clinicalEventSaveData);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存临床事件类型列表数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存临床事件类型列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 删除临床事件
     *
     * @param sjxh 事件序号
     * @param zyh  住院号
     * @param bqid 病区id
     * @param jgid 机构id
     * @return
     */
    public BizResponse<ClinicalEventType> deleteClinicalEventData(String sjxh, String zyh, String yhid, String bqid, String jgid) {
        BizResponse<ClinicalEventType> response = new BizResponse<>();
        try {
            boolean isSucess = service.deleteClinicalEventInfo(sjxh) > 0;
            if (!isSucess) {
                response.isSuccess = isSucess;
                response.message = "删除临床事件类型列表数据失败!";
            } else {
                response.isSuccess = isSucess;
                response.message = "删除临床事件类型列表数据成功!";
                response.datalist = realGetClinicalEventTypeList(zyh, yhid, bqid, jgid);
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除临床事件类型列表数据失败]数据库执行错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存临床事件类型列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 注意：调用此方法之前必须显式指定启用哪个事务
     */
    @Transactional(rollbackFor = Exception.class)
    private BizResponse<ClinicalEventType> realSaveClinicalEventData(ClinicalEventSaveData clinicalEventSaveData)
            throws SQLException, DataAccessException, ParseException {
        BizResponse<ClinicalEventType> response = new BizResponse<>();
        List<ClinicalEventInfo> clinicalEventTypeInfoList = clinicalEventSaveData.ClinicalEventType.ClinicalEventInfoList;
        boolean isSucess = false;
        for (ClinicalEventInfo info : clinicalEventTypeInfoList) {
            if (info.MODIFIED) {
                if (info.SJXH.contains("add:")) {
                    //add
                    info.SJXH = info.SJXH.replace("add:", "");
                    isSucess = service.addClinicalEventInfo(info) > 0;
                    if (!isSucess) {
                        response.isSuccess = isSucess;
                        response.message = "[获取临床事件类型列表数据失败]数据库执行错误!";
                        return response;
                    }
                } else {
                    //update
                    isSucess = service.editClinicalEventInfo(info) > 0;
                    if (!isSucess) {
                        response.isSuccess = isSucess;
                        response.message = "[获取临床事件类型列表数据失败]数据库执行错误!";
                        return response;
                    }
                }
            }
        }
        response.datalist = realGetClinicalEventTypeList(clinicalEventSaveData.ZYH, clinicalEventSaveData.YHID, clinicalEventSaveData.BQID, clinicalEventSaveData.JGID);
        response.isSuccess = true;
        response.message = "保存临床事件类型列表数据成功!";
        return response;
    }

    private List<ClinicalEventType> realGetClinicalEventTypeList(String zyh, String yhid, String bqid, String jgid)
            throws SQLException, DataAccessException, ParseException {
        String[] arr = new String[]{"自定义", "分娩", "死亡", "手术"};
        List<ClinicalEventType> clinicalEventTypeList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            ClinicalEventType clinicalEventType = new ClinicalEventType();
            clinicalEventType.TypeValue = String.valueOf(i);
            clinicalEventType.TypeName = arr[i];
            clinicalEventType.ClinicalEventInfoList = service.getClinicalEventInfoList(zyh, clinicalEventType.TypeValue);
            for (ClinicalEventInfo info : clinicalEventType.ClinicalEventInfoList) {
                info.JLR = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, info.JLGH);
                info.MODIFIED = false;
            }
            clinicalEventType.Count = String.valueOf(clinicalEventType.ClinicalEventInfoList.size());
            if (clinicalEventType.Count.equals("0")) {
                if (i == 1) {//分娩
                    List<String> fmsjList = service.getClinicalEventChildbirthInfo(zyh);
                    String fmsj = "";
                    if(fmsjList !=null && fmsjList.size()>0){
                        fmsj = fmsjList.get(0);
                    }
                    getClinicalEventType(i, fmsj, clinicalEventType, zyh, yhid, jgid);

                } else if (i == 2) {//死亡
                    String swsj = service.getClinicalEventDieInfo(zyh);
                    getClinicalEventType(i, swsj, clinicalEventType, zyh, yhid, jgid);
                }
            }
            clinicalEventTypeList.add(clinicalEventType);
        }
        return clinicalEventTypeList;
    }

    private void getClinicalEventType(int i, String time, ClinicalEventType clinicalEventType, String zyh, String yhid, String jgid)
            throws SQLException, DataAccessException, ParseException {
        if (!StringUtils.isBlank(time) && DateCompare.compare(time, "1900-01-01 00:00:00") == 1) {
//        if (!StringUtils.isBlank(time) && DateCompare.compare(time, "1900-01-01") == 1) {
            ClinicalEventInfo info = new ClinicalEventInfo();
            info.SJXH = String.valueOf(identityService.getIdentityMax("EMR_LCSJBC", DataSource.ENR));
            info.SJGS = "3";
            info.JZXH = zyh;
            info.JZHM = zyh;
            info.SJFL = String.valueOf(i);
            info.FSSJ = time;
            info.SJMS = null;
            info.JGID = jgid;
            info.JLSJ = timeService.now(DataSource.ENR);
            info.JLGH = yhid;
            info.JLR = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, info.JLGH);
            info.XTBZ = "1";
            info.MODIFIED = false;
            service.addClinicalEventInfo(info);
            clinicalEventType.ClinicalEventInfoList.add(info);
            clinicalEventType.Count = "1";
        }
    }

    private String[][] timePointsArray = new String[][]{
            new String[]{"02:00", "04:00", "03:00"},
            new String[]{"06:00", "08:00", "07:00"},
            new String[]{"10:00", "12:00", "11:00"},
            new String[]{"14:00", "16:00", "15:00"},
            new String[]{"18:00", "20:00", "19:00"},
            new String[]{"22:00", "00:00", "23:00"}};

    private int GetTimeInvite(String timePoint) {
        String[] arr = timePoint.split(":");
        if (arr.length == 2) {
            return Integer.parseInt(arr[0]) * 60 + Integer.parseInt(arr[1]);
        }
        return 0;
    }

    /**
     * 体征特殊校验处理
     *
     * @param zyh  住院号
     * @param xmh  项目号
     * @param tznr 体征内容
     * @param ycbz 异常标志
     * @param jgid 机构id
     * @return
     */
    private BizResponse<LifeSignDoubleCheckHistoryDataItem> LifeSignCheck(String zyh, String xmh, String tznr, String ycbz, String jgid)
            throws SQLException, DataAccessException, ParseException {
        BizResponse<LifeSignDoubleCheckHistoryDataItem> response = new BizResponse<>();
        response.isSuccess = false;
        response.message = "[获取体征特殊校验处理数据失败]服务内部错误!";
        //大便次数判断(提醒)
        if (xmh.equals("31") && StringUtils.isBlank(tznr)) {
            int days = 3;//连续几天
            String end = timeService.now(DataSource.ENR);
            String start = end;
            start = DateUtil.dateoffDays(timeService.now(DataSource.MOB), String.valueOf(-(days - 1)));
            List<LifeSignDoubleCheckHistoryDataItem> itemList = service.getLifeSignSpecialDataItem(zyh, start, end);
            boolean lxsr = true;
            for (int i = 1; i < days; i++) {
                String tempDate = DateUtil.dateoffDays(end, String.valueOf(-i));
                boolean tempState = false;
                for (LifeSignDoubleCheckHistoryDataItem item : itemList) {
                    if (DateUtil.dateoffDays(item.CJSJ, "0").equals(tempDate)) {
                        if (item.TZNR.equals("0")) {
                            tempState = true;
                        } else {
                            tempState = false;
                            break;
                        }
                    }
                    if (!tempState) {
                        lxsr = false;
                        break;
                    }
                }
            }
            if (lxsr) {//存在连续3日
//                    DataTable mes;
//                    if (!ds.Tables.Contains("MES")) {
//                        mes = new DataTable();
//                        mes.TableName = "MES";
//                        mes.Columns.Add("MESSTR", typeof(string));
//                        ds.Tables.Add(mes);
//                    } else
//                        mes = ds.Tables["MES"];
//                    mes.Rows.Add("该病人已连续3日大便次数为0");
            }

        }

        return response;
    }

    private void GetExctionInfo(String zyh, LifeSignControlItem lifeSignControlItem, String jgid,
                                List<LifeSignQualityInfo> lifeSignQualityInfoList, SickPersonDetailVo sickPersonDetailVo)
            throws SQLException, DataAccessException {
        String tzxm = lifeSignControlItem.TZXM;
        if (!StringUtils.isBlank(tzxm)) {


            for (int i = 0; i < lifeSignQualityInfoList.size(); i++) {
                LifeSignQualityInfo lifeSignQualityInfo = lifeSignQualityInfoList.get(i);
                if (lifeSignQualityInfo.Compare(tzxm, sickPersonDetailVo.JLLX, sickPersonDetailVo.BRNL)) {
                    lifeSignControlItem.ZCSX = lifeSignQualityInfo.ZCSX;
                    lifeSignControlItem.ZCXX = lifeSignQualityInfo.ZCXX;
                    lifeSignControlItem.FFSX = lifeSignQualityInfo.FFSX;
                    lifeSignControlItem.FFXX = lifeSignQualityInfo.FFXX;
                    break;
                }
            }
        }
    }

    /**
     * 组装同步入参，并进行同步
     *
     * @param data
     * @return
     */
    private Response<SelectResult> buildSyncData(LifeSignSaveData data) {
        InArgument inArgument = new InArgument();
        String jlsj = timeService.now(DataSource.HRP);
        inArgument.zyh = data.ZYH;
        inArgument.bqdm = data.BQID;
        inArgument.hsgh = data.URID;
        inArgument.bdlx = "5";
        inArgument.lybd = "0";
        inArgument.flag = "0";
        inArgument.jlxh = data.CJZH;
        inArgument.jlsj = jlsj;
        inArgument.jgid = data.JGID;
        inArgument.lymx = "0";
        inArgument.lymxlx = "0";

        for (LifeSignSaveDataItem item : data.lifeSignSaveDataItemList) {
            Project project = new Project("0", data.CJZH);
            Project newProject = new Project(item.TZXM, item.Data);
            project.saveProjects.add(newProject);
            inArgument.projects.add(project);
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
     * @param bdlx
     * @param jlxh
     * @param lymx
     * @param lymxlx
     * @param jgid
     */
    private void deleSyncData(String bdlx, String jlxh, String lymx, String lymxlx, String jgid) {
        InArgument inArgument = new InArgument();
        inArgument.bdlx = bdlx;
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

    public BizResponse<SickPersonVo> getPatientList(String bqid, String start, String end, String type, String jgid) {
        BizResponse<SickPersonVo> bizResponse = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            List<SickPersonVo> list = patientServiceSup.getPatientsForDept(bqid, jgid);
            keepOrRoutingDateSource(DataSource.ENR);
            if (type.equals("0")) {//表示选择全部病人
                bizResponse.datalist = list;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                String now = timeService.now(DataSource.ENR);
                cal.setTime(sdf.parse(now));
                cal.add(Calendar.MINUTE, Integer.parseInt(start));
                Iterator ite = list.iterator();
                while (ite.hasNext()) {
                    SickPersonVo sickPersonVo = (SickPersonVo) ite.next();
                    String tzxm = null;
                    List<String> xmList = service.getZKXM(sickPersonVo.ZYH, cal.getTime(), cal.getTime());
                    if (xmList == null || xmList.size() <= 0) {
                        ite.remove();
                    } else {
                        for (String s : xmList) {
                            tzxm += TextUtils.isEmpty(s) ? s : ("," + s);
                        }
                    }
                    sickPersonVo.TZXM = tzxm;
                }
                bizResponse.datalist = list;
            }
            bizResponse.isSuccess = true;
        } catch (SQLException | ParseException | DataAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;

    }

    public BizResponse<List<LifeSignHistoryInfo>> getLifeSignHistoryInfo(String zyh, String xmh){
        BizResponse<List<LifeSignHistoryInfo>> bizResponse = new BizResponse<>();

        try{
            int nums = 0;
            List<LifeSignHistoryInfo> list = service.getLifeSignHistoryInfo(zyh, xmh);
            bizResponse.data = new ArrayList<>();

            for(LifeSignHistoryInfo hInfo : list){
                bizResponse.data.add(hInfo);
                nums ++;

                if(nums > 5) break;
            }

            bizResponse.isSuccess = true;
        }catch (Exception ex){
            logger.error(ex.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取体征历史数据失败!" + ex.getMessage();
        }

        return bizResponse;
    }
}
