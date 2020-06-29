package com.bsoft.nis.service.inspection;


import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.common.service.log.LogOperSubType;
import com.bsoft.nis.common.service.log.LogOperType;
import com.bsoft.nis.common.service.log.LogService;
import com.bsoft.nis.common.servicesup.support.doublecheck.DoubleCheckServiceSup;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.core.doublecheck.DoubleCheckType;
import com.bsoft.nis.domain.core.log.db.OperLog;
import com.bsoft.nis.domain.inspection.CYInfoBean;
import com.bsoft.nis.domain.inspection.SpecimenVo;
import com.bsoft.nis.domain.inspection.db.SpecimenJYTM;
import com.bsoft.nis.domain.patient.db.SickPersonDetailVo;
import com.bsoft.nis.domain.patient.db.SickPersonVo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.inspection.support.SpecimenService;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import com.bsoft.nis.util.date.DateCompare;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 标本采集主服务
 * Created by Administrator on 2016/10/10.
 */
@Service
public class SpecimenMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(SpecimenMainService.class);

    @Autowired
    SpecimenService service; // 标本采集服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    @Autowired
    IdentityService identity;//获取种子服务

    @Autowired
    SystemParamService systemParamService;//获取系统参数服务
    @Autowired
    DateTimeService timeService;

    @Autowired
    PatientServiceSup patientService;

    @Autowired
    LogService logService;//日志操作服务

    @Autowired
    DoubleCheckServiceSup doubleCheckService;//双人核对记录表操作服务


    OperLog operLog = new OperLog();

    public BizResponse<SpecimenVo> GetSpecimenList(String zyh, String jgid) {
        BizResponse<SpecimenVo> bizResponse = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            //更具住院号获取住院号码
            String zyhm = patientService.getPatientDetail(zyh, jgid).ZYHM;
            String ryrq = patientService.getPatientDetail(zyh, jgid).RYRQ;
            keepOrRoutingDateSource(DataSource.LIS);
            bizResponse.datalist = service.GetCaptureData(zyhm, jgid, ryrq);
            if (bizResponse.datalist != null && bizResponse.datalist.size() > 0) {
                //获取发放记录
                BizResponse<List<OperLog>> list = logService.getSpecimenCollectLogs(zyh, jgid);
                if (list.isSuccess) {
                    if (list.data.size() > 0) {
                        for (SpecimenVo specimenVo : bizResponse.datalist) {
                            for (OperLog ope : list.data) {
                                if (ope.GLBH.equals(specimenVo.TMBH)) {
                                    specimenVo.FFZT = 1;
                                    break;
                                }
                                specimenVo.FFZT = 0;
                            }
                        }
                    } else {
                        for (SpecimenVo specimenVo : bizResponse.datalist) {
                            specimenVo.FFZT = 0;
                        }
                    }
                } else {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "获取发放记录出错";
                    return bizResponse;
                }
            }
            bizResponse.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取采集列表错误!" + e.getMessage();
        }
        return bizResponse;
    }


    public BizResponse<SpecimenVo> GetHistorySpecimenList(String zyh, String start, String end, String jgid) {
        BizResponse<SpecimenVo> bizResponse = new BizResponse<>();
        List<SpecimenVo> list = new ArrayList<>();
        try {
            //方便 sql 语句处理
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(end));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            end = sdf.format(cal.getTime());
            keepOrRoutingDateSource(DataSource.HRP);
            //更具住院号获取住院号码
            String zyhm = patientService.getPatientDetail(zyh, jgid).ZYHM;
            keepOrRoutingDateSource(DataSource.LIS);
            list = service.GetHistoryCaptureData(zyhm, start, end, jgid);
            for (SpecimenVo aList : list) {
                String sgys = aList.SGYS;
                if (sgys != null && !sgys.equals("")) {
                    //sgys = sgys.substring(sgys.indexOf("("), sgys.indexOf(")"));
                    //aList.SGYS = sgys;

                    int sIndex = 0, eIndex = 0;
                    sIndex = sgys.indexOf("(");
                    eIndex = sgys.indexOf(")");

                    sgys = (sIndex > -1 && eIndex > -1) ? sgys.substring(sIndex, eIndex): "";
                    aList.SGYS = sgys;
                }

                if (sgys != null && !sgys.equals("")) {
                    int sIndex = 0, eIndex = 0;
                    sIndex = sgys.indexOf("(");
                    eIndex = sgys.indexOf(")");
                    if(sIndex > -1 && eIndex > -1) {
                        sgys = sgys.substring(sIndex, eIndex);
                    }else{
                        sgys = "";
                    }
                    aList.SGYS = sgys;
                }
            }
           /* if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    //协和 存的是员工编号
                    String ygbh = list.get(i).CYR;
                    list.get(i).CYR = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, ygbh);
                }
            }*/
            bizResponse.datalist = list;
            bizResponse.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取采集列表错误!" + e.getMessage();
        }
        return bizResponse;
    }


    public BizResponse<String> ExecuteSpecimen(String zyh, String urid, String tmbh, String isScan, String sbmc, String jgid) {
        BizResponse<String> bizResponse = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            //更具住院号获取病人信息
            SickPersonDetailVo sickPersonDetailVo = patientService.getPatientDetail(zyh, jgid);
            String zyhm = sickPersonDetailVo.ZYHM;
            String bqdm = sickPersonDetailVo.BRBQ;

            keepOrRoutingDateSource(DataSource.LIS);
            if (isScan.equals("1")) {//转换条码
                Map<String, Object> parms = new HashMap<>();
                parms.put("VV_INDOCNO", tmbh);
                parms.put("VV_OUTDOCNO", "");
                parms.put("VN_RET", 0);
                tmbh = service.GetDocNoBySp(parms);
                if (tmbh == null) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "转换条码出错！";
                    return bizResponse;
                }
            }
            //双签判断
            List<String> list = systemParamService.getUserParams("1", "IENR", "IENR_I_DC", jgid, DataSource.MOB).datalist;
            if (list != null && list.size() > 0 && list.get(0).equals("1")) {//获取双签系统参数
                if (DoubleCheck(tmbh, jgid)) {
                    BizResponse<DoubleCheckType> biz = SingleCheck(tmbh, urid, jgid, zyh, null);
                    if (!biz.isSuccess) {
                        bizResponse.isSuccess = false;
                        bizResponse.message = biz.message;
                        return bizResponse;
                    }
                }
            }
            String nowDateTime = timeService.getNowDateTimeStr(DataSource.MOB);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowDate = simpleDateFormat.parse(nowDateTime);
            //add 2018-6-28 11:06:12
            keepOrRoutingDateSource(DataSource.LIS);
            //获取条码信息
            SpecimenJYTM specimenJYTM = service.getJYTM(tmbh);
            if (specimenJYTM.TMBH != null && !specimenJYTM.TMBH.equals("")) {
                if (!specimenJYTM.ZYHM.equals(zyhm)) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "该项目不属于该病人!";
                    return bizResponse;
//                } else if (!"0".equals(specimenJYTM.CYBZ)) {
                } else if (specimenJYTM.CYBZ != null && !"0".equals(specimenJYTM.CYBZ)) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "该项目已被执行";
                    return bizResponse;
                } else {
                    Map<String, Object> zxparms = new HashMap<>();
                    zxparms.put("VV_DOCNO", tmbh);
                    zxparms.put("VV_CYR", urid);
//                    zxparms.put("VD_CYRQ", new Date());
                    zxparms.put("VD_CYRQ", nowDate);
                    zxparms.put("VN_JGID", Integer.parseInt(jgid));
                    zxparms.put("VN_TYPE", 1);
                    zxparms.put("VN_RET", 0);
                    zxparms.put("VV_SBMC", sbmc);
                    zxparms.put("VV_RETMSG", "执行失败");
                    Map<String, String> map = service.CaptureExecute(zxparms);
                    if ("0".equals(map.get("code"))) {
                        bizResponse.isSuccess = false;
                        bizResponse.message = map.get("message");
                        return bizResponse;
                    } else {
                        //获取是否更新emr_yzb系统参数
                        Boolean isUPYZB = false;
                        List<String> _list = systemParamService.getUserParams("1", "IENR", "IENR_I_UPYZB", jgid, DataSource.MOB).datalist;
                        if (_list != null && _list.size() > 0) {
                            isUPYZB = _list.get(0).equals("1");
                        }
                        if (isUPYZB) {
                            //更新医嘱本
                            if (!UpdateYZB(tmbh, zyh, urid, nowDate, jgid)) {
                                bizResponse.isSuccess = false;
                                bizResponse.message = "更新医嘱本出错";
                                return bizResponse;
                            }
                        }
                        //enr_czjl插入操作记录
                        operLog.BQDM = Long.parseLong(bqdm);
                        operLog.ZYH = Long.parseLong(zyh);
                        operLog.CZLB = 1;
                        operLog.CZLX = 6;
                        operLog.GLBH = tmbh;
                        operLog.CZGH = urid;
                        operLog.CZZT = "0";
                        operLog.CZSJ = nowDateTime;
                        operLog.JGID = jgid;
                        logService.addLog(operLog);
                        bizResponse.isSuccess = true;
                        return bizResponse;
                    }
                }
            } else {
                bizResponse.isSuccess = false;
                bizResponse.message = "没找到该条码对应检验数据";
                return bizResponse;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "执行错误!" + e.getMessage();
        }
        return bizResponse;
    }

    /**
     * 判断单签记录存在,确定在第二次扫描是否执行  IENR_SRHDJL
     *
     * @param tmbh
     * @param urid
     * @param jgid
     * @param zyh
     * @param bqid
     * @return
     */
    private BizResponse<DoubleCheckType> SingleCheck(String tmbh, String urid, String jgid, String zyh, String bqid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DoubleCheckType> bizResponse = new BizResponse<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (tmbh == null || urid == null) {
                bizResponse.isSuccess = false;
                bizResponse.message = "条码为空或者护士工号为空";
                return bizResponse;
            }
            String nowDateTime = timeService.getNowDateTimeStr(DataSource.MOB);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowDate = simpleDateFormat.parse(nowDateTime);
            //
            DoubleCheckType doubleCheckType = new DoubleCheckType();
            doubleCheckType.setJLXH(identity.getIdentityMax("IENR_SRHDJL", 1, DataSource.MOB).datalist.get(0));
            doubleCheckType.setHDBS1(tmbh);
            doubleCheckType.setZYH(zyh);
            doubleCheckType.setHDSJ(nowDateTime);
            doubleCheckType.setBRBQ(bqid);
            doubleCheckType.setHDGH(urid);
            doubleCheckType.setJGID(jgid);
            doubleCheckType.setZFBZ("0");
            bizResponse.datalist = doubleCheckService.getDoubleCheck(tmbh, jgid);
            if (bizResponse.datalist.size() > 0) {
                String hdsj = bizResponse.datalist.get(0).getHDSJ();
                String hdgh = bizResponse.datalist.get(0).getHDGH();
                Long l = (nowDate.getTime() - sdf.parse(hdsj).getTime()) / 1000;
                //获取双签间隔系统参数
                String s = "0";
                List<String> list = systemParamService.getUserParams("1", "IENR", "IENR_I_DC_TOUT", jgid, DataSource.MOB).datalist;
                if (list != null && list.size() > 0) {
                    s = list.get(0);
                }
                if (l < Long.getLong(s)) {
                    if (hdgh.equals(urid)) {
                        doubleCheckType.setHDZT("0");
                        //双人核对记录插入操作
                        doubleCheckService.addDoubleCheck(doubleCheckType);
                        bizResponse.isSuccess = false;
                        bizResponse.message = "双人核对不能为同一名护士";
                        return bizResponse;
                    }
                    doubleCheckType.setHDZT("1");
                    //双人核对记录插入操作
                    doubleCheckService.addDoubleCheck(doubleCheckType);
                    bizResponse.isSuccess = true;
                } else {
                    doubleCheckType.setHDZT("0");
                    //双人核对记录插入操作
                    doubleCheckService.addDoubleCheck(doubleCheckType);
                    bizResponse.isSuccess = false;
                    bizResponse.message = "请下一位护士核对执行";
                }
            } else {
                doubleCheckType.setHDZT("0");
                //双人核对记录插入操作
                doubleCheckService.addDoubleCheck(doubleCheckType);
                bizResponse.isSuccess = false;
                bizResponse.message = "请下一位护士核对执行";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "插入核对记录失败!" + e.getMessage();
        }
        return bizResponse;
    }

    /**
     * 双签判断
     *
     * @param tmbh
     * @param jgid
     * @return
     */
    private boolean DoubleCheck(String tmbh, String jgid) {
        keepOrRoutingDateSource(DataSource.LIS);
        try {
            Map<String, Object> mapSRHD = service.DoubleCheck(tmbh, jgid);
            if (mapSRHD.isEmpty()) {
                return false;
            }
            if (mapSRHD.get("SRHDBZ").toString().equals("1")) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }


    public BizResponse<CYInfoBean> GetCYInfoByTMBH(String tmbh, String brid, String jgid) {
        keepOrRoutingDateSource(DataSource.LIS);
        BizResponse<CYInfoBean> response = new BizResponse<>();
        List<CYInfoBean> cyInfoBeanList = null;
        try {
            cyInfoBeanList = service.GetCYInfoByTMBH(tmbh, brid);
            for (CYInfoBean cyInfoBean : cyInfoBeanList) {
                String ygbh = cyInfoBean.CYR;
                cyInfoBean.CYR = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, ygbh);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
        }
        response.isSuccess = true;
        response.datalist = cyInfoBeanList;
        return response;
    }

    public BizResponse<String> CancelSpecimen(String zyh, String urid, String tmbh, String sbmc, String jgid) {


        BizResponse<String> bizResponse = new BizResponse<>();
        try {
            String nowDateTime = timeService.getNowDateTimeStr(DataSource.MOB);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowDate = simpleDateFormat.parse(nowDateTime);
    //
            keepOrRoutingDateSource(DataSource.LIS);
            Map<String, Object> zxparms = new HashMap<>();
            zxparms.put("VV_DOCNO", tmbh);
            zxparms.put("VV_CYR", urid);
            zxparms.put("VD_CYRQ", nowDate);
            zxparms.put("VN_JGID", Integer.parseInt(jgid));
            zxparms.put("VN_TYPE", 2);
            zxparms.put("VN_RET", 0);
            zxparms.put("VV_RETMSG", "执行失败");
            zxparms.put("VV_SBMC", sbmc);
            Map<String, String> map = service.CaptureExecute(zxparms);
            if (map.get("code").equals("0")) {
                bizResponse.isSuccess = false;
                bizResponse.message = map.get("message");
                return bizResponse;
            } else {
                //获取是否更新emr_yzb系统参数
                Boolean isUPYZB = false;
                List<String> _list = systemParamService.getUserParams("1", "IENR", "IENR_I_UPYZB", jgid, DataSource.MOB).datalist;
                if (_list != null && _list.size() > 0) {
                    isUPYZB = _list.get(0).equals("1");
                }
                if (isUPYZB) {
                    //更新医嘱本
                    //if (!UpdateYZB(tmbh, zyh, urid, nowDate, jgid)) {
                    if (!UpdateYZB(tmbh, zyh, null, null, jgid)) {
                        bizResponse.isSuccess = false;
                        bizResponse.message = "更新医嘱本出错";
                        return bizResponse;
                    }
                }

                List<LogOperSubType> list = new ArrayList<>();
                list.add(LogOperSubType.PROVIDE);
                list.add(LogOperSubType.RECYCLE);
                //作废操作记录
                logService.deleteLog(zyh, jgid, tmbh, LogOperType.SpecimenCollect, list);
                bizResponse.isSuccess = true;
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "执行错误!" + e.getMessage();
        }
        return bizResponse;
    }

    public BizResponse<String> Delivery(String zyh, String urid, String tmbh, String isScan, String jgid) {
        keepOrRoutingDateSource(DataSource.LIS);
        BizResponse<String> bizResponse = new BizResponse<>();
        try {
            //获取检验条码信息
            SpecimenJYTM specimenJYTM = service.getJYTM(tmbh);
            if (specimenJYTM.TMBH != null && !specimenJYTM.TMBH.equals("")) {
                if (specimenJYTM.CYBZ.equals("1")) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "该项目已被执行";
                    return bizResponse;
                }
            }

            if (isScan.equals("1")) {//转换条码
                Map<String, Object> parms = new HashMap<>();
                parms.put("VV_INDOCNO", tmbh);
                parms.put("VV_OUTDOCNO", "");
                parms.put("VN_RET", 0);
                tmbh = service.GetDocNoBySp(parms);
                if (tmbh == null) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "转换条码出错！";
                    return bizResponse;
                }
            }
            String nowDateTime = timeService.getNowDateTimeStr(DataSource.MOB);
            //插入操作记录表
            keepOrRoutingDateSource(DataSource.HRP);
            operLog.BQDM = Long.parseLong(patientService.getPatientDetail(zyh, jgid).BRBQ);
            operLog.ZYH = Long.parseLong(zyh);
            operLog.CZLB = 1;
            operLog.CZLX = 5;
            operLog.GLBH = tmbh;
            operLog.CZGH = urid;
            operLog.CZZT = "0";
            operLog.CZSJ = nowDateTime;
            operLog.JGID = jgid;
            logService.addLog(operLog);
            bizResponse.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "发放错误!" + e.getMessage();
        }
        return bizResponse;
    }

    public List<SpecimenVo> specimenTypeFilter(List<SpecimenVo> tList, int mTypeFilterPos) {
        if (mTypeFilterPos <= 0) {
            return tList;
        }
        if (mTypeFilterPos == 1) {
            //血液
            List<SpecimenVo> tempList = new ArrayList<>();
            for (SpecimenVo specimenVo : tList) {
                if ("1".equals(specimenVo.BBFL)) {
                    tempList.add(specimenVo);
                }
            }
            return tempList;
        }
        if (mTypeFilterPos == 2) {
            //24小时尿液
            List<SpecimenVo> tempList = new ArrayList<>();
            for (SpecimenVo specimenVo : tList) {
                if ("24".equals(specimenVo.JYLX)) {
                    tempList.add(specimenVo);
                }
            }
            return tempList;
        }
        if (mTypeFilterPos == 3) {
            //其他
            List<SpecimenVo> tempList = new ArrayList<>();
            for (SpecimenVo specimenVo : tList) {
                if (!"1".equals(specimenVo.BBFL) && !"24".equals(specimenVo.JYLX)) {
                    tempList.add(specimenVo);
                }
            }
            return tempList;
        }
        return tList;
    }

    public BizResponse<SickPersonVo> GetPatientList(String bqdm, String jgid, String mCheckBoxFiter, String mTypeFilterPos) {
        BizResponse<SickPersonVo> bizResponse = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.HRP);
        try {
            //根据 brbq，jgid 获取病人信息
            List<SickPersonVo> list = patientService.getPatientsForDept(bqdm, jgid);
            Iterator iter = list.iterator();
            //
            List<String> zyhmList = new ArrayList<>();
            for (SickPersonVo sickPersonVo : list) {
                if (!TextUtils.isBlank(sickPersonVo.ZYHM)) {
                    zyhmList.add(sickPersonVo.ZYHM);
                }
            }
            //
            keepOrRoutingDateSource(DataSource.LIS);
            //
            //mTypeFilterPos// 0 全部 1 血液 2 24小时尿液  3 其他
            //
            List<SpecimenVo> specimenVoList_DCJ_ALL = service.GetCaptureDataList(zyhmList, jgid, null);
            List<SpecimenVo> specimenVoList_YCJ_ALL = new ArrayList<>();
            //【1】过滤 待采集/全部  mCheckBoxFiter//0 待采集  1 全部
            if ("1".equals(mCheckBoxFiter)) {
                specimenVoList_YCJ_ALL = service.GetHistoryCaptureDataList(zyhmList, null, null, jgid);
            }
            while (iter.hasNext()) {
                SickPersonVo sickPersonVo = (SickPersonVo) iter.next();
//                List<SpecimenVo> listCap = new ArrayList<>();
                List<SpecimenVo> listBack = new ArrayList<>();
                String zyhm = sickPersonVo.ZYHM;
                String ryrq = sickPersonVo.RYRQ;
//                listCap = service.GetCaptureData(zyhm, jgid, ryrq);

//                List<SpecimenVo> specimenVoList_DCJ = service.GetCaptureData(zyhm, jgid, ryrq);
//                List<SpecimenVo> specimenVoList_YCJ = GetHistorySpecimenList(zyhm, jgid, ryrq);
                List<SpecimenVo> specimenVoList_DCJ = new ArrayList<>();
                if (specimenVoList_DCJ_ALL != null) {
                    for (SpecimenVo specimenVo : specimenVoList_DCJ_ALL) {
                        if (specimenVo.ZYHM.equals(zyhm)) {
                            //补充 RYRQ 条件  (KDSJ > RYRQ)
                            boolean cha = false;
                            try {
                                cha = DateCompare.isGreaterThan(specimenVo.KDSJ, sickPersonVo.RYRQ);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (cha) {
                                specimenVoList_DCJ.add(specimenVo);
                            }
                        }
                    }
                }
                List<SpecimenVo> specimenVoList_YCJ = new ArrayList<>();
                //
                if (specimenVoList_YCJ_ALL != null) {
                    for (SpecimenVo specimenVo : specimenVoList_YCJ_ALL) {
                        if (specimenVo.ZYHM.equals(zyhm)) {
                            specimenVoList_YCJ.add(specimenVo);
                        }
                    }
                }
                //【2】过滤类型
                int typeFilterPos = Integer.valueOf(mTypeFilterPos);
                specimenVoList_DCJ = specimenTypeFilter(specimenVoList_DCJ, typeFilterPos);
                specimenVoList_YCJ = specimenTypeFilter(specimenVoList_YCJ, typeFilterPos);
                //
                int dcj = specimenVoList_DCJ.size();
                int ycj = specimenVoList_YCJ.size();
//                listCap= fiterSpecimenVoList(listCapAll,zyhm,ryrq);
//                if (listCap.size() == 0) {
                sickPersonVo.dcjCount = dcj;

                if (ycj == 0 && dcj == 0) {
                    iter.remove();
                } else {
                    for (SpecimenVo specimenVo : specimenVoList_DCJ) {
                        if (specimenVo.FFZT == 1) {
                            listBack.add(specimenVo);
                        }
                    }
                    if (listBack.size() == 0) {
                        list.get(list.indexOf(sickPersonVo)).FFZT = 0;
                    } else if (listBack.size() == specimenVoList_DCJ.size()) {
                        list.get(list.indexOf(sickPersonVo)).FFZT = 1;
                    } else {
                        list.get(list.indexOf(sickPersonVo)).FFZT = 2;
                    }
                }
            }
            bizResponse.isSuccess = true;
            bizResponse.datalist = list;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "发放错误!" + e.getMessage();
        }
        return bizResponse;
    }

    //更新医嘱本
    public Boolean UpdateYZB(String tmbh, String zyh, String urid, Date hszxsj, String jgid) {
        keepOrRoutingDateSource(DataSource.LIS);
        try {
            /*Map<String, String> mapSQBH = service.GetSQBH(tmbh, jgid);
            if (mapSQBH.isEmpty()) {
                return false;
            }
            String sqdh = mapSQBH.get("SQDH");
            keepOrRoutingDateSource(DataSource.HRP);
            service.UpdateYZB(hszxsj, urid, sqdh, zyh, jgid);*/

            //以下代码根据TMBH、JGID获取YZBXH列表，并更新医嘱执行工号及时间
            List<Long> yzbxhList = service.GetYZXH(tmbh, jgid);
            if(yzbxhList.size() < 1) return false;

            keepOrRoutingDateSource(DataSource.HRP);
            service.UpdateYZB2(hszxsj, urid, yzbxhList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}

