package com.bsoft.nis.service.advicecheck;

import com.bsoft.nis.adviceexecute.ModelManager.PrefixInfoManager;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.advicecheck.*;
import com.bsoft.nis.domain.adviceexecute.PrefixInfo;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.advicecheck.support.AdviceCheckServiceSup;
import com.bsoft.nis.service.adviceexecute.AdviceExecuteMainService;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.birthday.BirthdayUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by king on 2016/11/28.
 */
@Service
public class AdviceCheckMainService extends RouteDataSourceService {

    @Autowired
    AdviceCheckServiceSup serviceSup;

    @Autowired
    SystemParamService systemParamService;

    @Autowired
    DictCachedHandler dictCachedHandler;

    @Autowired
    PrefixInfoManager prefixInfoManager;//条码前缀管理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    private Log logger = LogFactory.getLog(AdviceExecuteMainService.class);


    public BizResponse<AdviceCheckList> getDosingCheckList(String brbq, String syrq, String gslx, String type, String status, String jgid) {
        BizResponse<AdviceCheckList> response = new BizResponse<>();
        try {
            String jyhdbz = null;
            String byhdbz = null;
            if (type.equals("2")) {
                jyhdbz = status;
                boolean qybyhd = false;
                BizResponse<String> tempResponse = systemParamService.getUserParams("1", null, "IENR_YZZX_QYBYHD", jgid, DataSource.MOB);
                if (tempResponse.isSuccess) {
                    if (tempResponse.datalist.get(0).equals("1")) {
                        qybyhd = true;
                    }
                }
                if (qybyhd) {
                    byhdbz = "1";
                }
            } else {
                byhdbz = status;
            }
            List<AdviceForm> checkList = serviceSup.getDosingCheckList(brbq, syrq, jyhdbz, byhdbz, gslx);

            //将工号转化为姓名
            for (AdviceForm check : checkList) {
                if (check.BYHDGH != null) {
                    check.BYHDR = dictCachedHandler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, check.BYHDGH);
                }
                if (check.JYHDGH != null) {
                    check.JYHDR = dictCachedHandler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, check.JYHDGH);
                }
            }

            List<AdviceForm> patientList = getPatientsByBQ(brbq, jgid);

            for (int i = 0; i < checkList.size(); i++) {
                for (AdviceForm patient : patientList) {
                    if (checkList.get(i).ZYH.equals(patient.ZYH)) {
                        checkList.get(i).BRXM = patient.BRXM;
                        checkList.get(i).ZYHM = patient.ZYHM;
                        checkList.get(i).BRNL = patient.BRNL;
                        checkList.get(i).BRXB = patient.BRXB;
                        break;
                    }
                }
            }
            AdviceCheckParams params = getPara(jgid);
            if (params.IsSimpleMode == null || params.IsDispensingCheck == null) {
                throw new SQLException();
            }
            AdviceCheckList list = new AdviceCheckList();
            list.adviceForm = checkList;
            list.adviceCheckParams = params;

            response.data = list;

            response.isSuccess = true;
            response.message = "获取加药摆药核对列表成功!";
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【获取加药摆药核对列表】数据库查询错误";
            logger.error(e.getMessage());
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【获取加药摆药核对列表】服务内部错误";
            logger.error(e.getMessage());
        }
        return response;

    }

    public BizResponse<CheckDetail> scanExecuteDoskingCheck(String tmbh, String prefix, String userId, boolean isCheck, String type, String jgid) {
        BizResponse<CheckDetail> response = new BizResponse<>();
        try {
            String gslx = getPreFix(prefix, jgid);
            String tm = prefix + tmbh;
            CheckForm cf = create(tm, gslx);
            //====================================================================================
            // 扫描无效条码时处理(如福建协和及龙岩一院：输液瓶签打印之后变更条码号码，核对扫描到的
            //                    是之前已打印的条码号码) mod by mengdw at 2019.01.17
            //------------------------------------------------------------------------------------
            if(cf == null){
                response.isSuccess = false;
                response.message = "当前条码(" + tm + ")无效！";
                return response;
            }
            //=====================================================================================
            CheckDetail checkDetail;
            if (isCheck) {
                checkDetail = updateCheckForm(userId, gslx, type, jgid, cf);
            } else {
                if (systemParamService.getUserParams("1", null, "IENR_D_C_Mode", jgid, DataSource.MOB).equals("1")) {
                    checkDetail = getCheckDetail(cf.SYDH, gslx, jgid);
                } else {
                    checkDetail = updateCheckForm(userId, gslx, type, jgid, cf);
                }
            }
            if (checkDetail == null) {
                response.isSuccess = false;
                response.message = "【扫描加药摆药核对】数据库查询错误";
            } else {
                if (isCheck && checkDetail.IsFalse.equals("IsTrue")) {
                    response.isSuccess = false;
                    response.message = checkDetail.Msg;
                } else {
                    response.data = checkDetail;
                    response.isSuccess = true;
                    response.message = "扫描加药摆药核对成功!";
                }
            }
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【扫描加药摆药核对】数据库查询错误";
            logger.error(e.getMessage());
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【扫描加药摆药核对】服务内部错误";
            logger.error(e.getMessage());
        }

        return response;
    }

    public BizResponse<CheckDetail> handExecuteDoskingCheck(String sydh, String gslx, String userId, boolean isCheck, String type, String jgid) {
        BizResponse<CheckDetail> response = new BizResponse<>();
        try {
            CheckForm cf = createBySydh(sydh, gslx);
            CheckDetail checkDetail = null;
            if (isCheck) {
                checkDetail = updateCheckForm(userId, gslx, type, jgid, cf);
            } else {
                checkDetail = getCheckDetail(sydh, gslx, jgid);
            }
            if (checkDetail == null) {
                response.isSuccess = false;
                response.message = "【手动加药摆药核对】数据库查询错误";
            } else {
                if (isCheck && checkDetail.IsFalse.equals("IsTrue")) {
                    response.isSuccess = false;
                    response.message = checkDetail.Msg;

                } else {
                    response.data = checkDetail;
                    response.isSuccess = true;
                    response.message = "手动加药摆药核对成功!";
                }
            }
        } catch (SQLException | DataAccessException e) {
            response.isSuccess = false;
            response.message = "【手动加药摆药核对】数据库查询错误";
            logger.error(e.getMessage());
        } catch (Exception e) {
            response.isSuccess = false;
            response.message = "【手动加药摆药核对】服务内部错误";
            logger.error(e.getMessage());
        }

        return response;
    }

    /**
     * 获取病人列表
     *
     * @param brbq 病人病区
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<AdviceForm> getPatientsByBQ(String brbq, String jgid)
            throws SQLException, DataAccessException, ParseException {
        List<AdviceForm> adviceForms = serviceSup.getPatientsByBQ(brbq, jgid);

        String nowTime = DateConvert.getStandardString(timeService.now(DataSource.HRP));
        //生日到年龄的换算
        for (int i = 0; i < adviceForms.size(); i++) {
            if (adviceForms.get(i).CSNY != null) {
                adviceForms.get(i).BRNL = BirthdayUtil.getAgesPairCommonStrSimple(adviceForms.get(i).CSNY, nowTime);
            }
        }
        return adviceForms;
    }


    /**
     * 是否启用摆药核对
     *
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public AdviceCheckParams getPara(String jgid)
            throws SQLException, DataAccessException {
        AdviceCheckParams adviceCheckParams = new AdviceCheckParams();
        BizResponse<String> check = systemParamService.getUserParams("1", null, "IENR_YZZX_QYBYHD", jgid, DataSource.MOB);
        BizResponse<String> mode = systemParamService.getUserParams("1", null, "IENR_D_C_Mode", jgid, DataSource.MOB);
        if (check.isSuccess && mode.isSuccess) {
            if (check.datalist.size() > 0 && check.datalist.get(0).equals("1")) {
                adviceCheckParams.IsDispensingCheck = "1";
            } else {
                adviceCheckParams.IsDispensingCheck = "0";
            }
            if (mode.datalist.size() > 0 && mode.datalist.get(0).equals("1")) {
                adviceCheckParams.IsSimpleMode = "1";
            } else {
                adviceCheckParams.IsSimpleMode = "0";
            }
        }
        return adviceCheckParams;
    }


    /**
     * 根据药品用法获得具体用法名称
     *
     * @param jlxh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<AdviceFormDetail> getAdviceName(List<String> jlxh, String jgid)
            throws SQLException, DataAccessException {

        List<AdviceFormDetail> adviceFormDetailList = serviceSup.getAdviceName(jlxh);
        for (AdviceFormDetail detail : adviceFormDetailList) {
            if (detail.YPYF != null) {
                detail.YFMC = dictCachedHandler.getValueByKeyFromCached(CachedDictEnum.MOB_YPYF, jgid, detail.YPYF);
            }
        }

        return adviceFormDetailList;
    }


    /**
     * 获得核对信息
     *
     * @param tmbh
     * @param gslx
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public CheckForm create(String tmbh, String gslx)
            throws SQLException, DataAccessException {
        List<CheckForm> list = serviceSup.create(tmbh, gslx);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }


    public CheckForm createBySydh(String sydh, String gslx)
            throws SQLException, DataAccessException {
        List<CheckForm> list = serviceSup.createBySydh(sydh, gslx);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获得药品核对详情
     *
     * @param sydh 输液单号
     * @param gslx 归属类型
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public CheckDetail getCheckDetail(String sydh, String gslx, String jgid)
            throws SQLException, DataAccessException {

        CheckDetail checkDetail = new CheckDetail();
        List<AdviceFormDetail> adviceFormDetailList = serviceSup.getCheckDetail(sydh, gslx);
        List<String> yzxh = new ArrayList<>();
        for (AdviceFormDetail a : adviceFormDetailList) {
            if (a.YZXH != null) {
                yzxh.add(a.YZXH);
            }
        }
        if (yzxh.size() == 0) {
            yzxh.add("-1");
        }
        List<AdviceFormDetail> adviceNameList = getAdviceName(yzxh, jgid);
        for (AdviceFormDetail detail : adviceFormDetailList) {
            for (AdviceFormDetail name : adviceNameList) {
                if (detail.YZXH.equals(name.YZXH)) {
                    detail.YZMC = name.YZMC;
                    detail.YPYF = name.YPYF;
                    detail.YFMC = name.YFMC;
                    detail.SYPC = name.SYPC;
                }
            }
        }
        checkDetail.adviceFormDetails = adviceFormDetailList;
        return checkDetail;
    }


    /**
     * 药品核对
     *
     * @param userId
     * @param gslx
     * @param type
     * @param jgid
     * @param cf
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    private CheckDetail updateCheckForm(String userId, String gslx, String type, String jgid, CheckForm cf)
            throws SQLException, DataAccessException, ParseException {

        CheckDetail checkDetail = new CheckDetail();
        int count = 0;
        String nowTime = DateConvert.getStandardString(timeService.now(DataSource.HRP));
        if (type.equals("1")) {
            if (cf.BYHDBZ.equals("1")) {
                checkDetail.Msg = "摆药核对已核对！";
                return checkDetail;
            } else {
                if (gslx.equals("4")) {//4 输液类型
                    count = serviceSup.updateCheckForm1(userId, nowTime, cf.SYDH, jgid);
                } else {//5 注射类型
                    count = serviceSup.updateCheckForm2(userId, nowTime, cf.SYDH, jgid);
                }
            }
            checkDetail.Msg = "摆药核对成功！";
        } else {
            BizResponse<String> check = systemParamService.getUserParams("1", null, "IENR_YZZX_QYBYHD", jgid, DataSource.MOB);
            //加药核对前先摆药核对
            if (check.datalist.get(0).equals("1")) {
                if (cf.BYHDBZ.equals("0")) {
                    checkDetail.Msg = "请先摆药核对！";
                    return checkDetail;
                }
            }
            if (cf.JYHDBZ.equals("1")) {
                checkDetail.Msg = "加药核对已核对！";
                return checkDetail;
            }
            /*if (cf.BYHDGH.equals(userId)) {
                checkDetail.Msg = "加药核和摆药核对不能是同一个人！";
                return checkDetail;
            }*/
            if (gslx.equals("4")) {
                count = serviceSup.updateCheckForm3(userId, nowTime, cf.SYDH, jgid);
            } else {
                count = serviceSup.updateCheckForm4(userId, nowTime, cf.SYDH, jgid);
            }
            checkDetail.Msg = "加药核对成功！";
        }
        if (count == 0) {
            checkDetail.Msg = "核对失败！";
            return checkDetail;
        } else {
            checkDetail.IsFalse = "IsFalse";
        }

        return checkDetail;
    }

    public String getPreFix(String preFix, String jgid)
            throws SQLException, DataAccessException {
        Map<String, List<PrefixInfo>> map = prefixInfoManager.getPrefixInfoMap();
        for (Map.Entry<String, List<PrefixInfo>> entry : map.entrySet()) {
            List<PrefixInfo> list = entry.getValue();
            for (PrefixInfo item : list) {
                if (Objects.equals(item.Prefix, preFix) && Objects.equals(item.Agency, jgid)) {
                    return entry.getKey();
                }
            }
        }
        return null;

    }
}
