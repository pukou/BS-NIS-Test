package com.bsoft.nis.service.dangerevaluate;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dangerevaluate.*;
import com.bsoft.nis.domain.dangerevaluate.db.*;
import com.bsoft.nis.domain.evaluation.nursingeval.KeyValue;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.Project;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.mapper.patient.PatientMapper;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.dangerevaluate.support.DangerEvaluateServiceSup;
import com.bsoft.nis.util.date.DateUtil;
import com.bsoft.nis.util.date.birthday.BirthdayUtil;
import ctd.net.rpc.Client;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: 风险评估主服务
 * User: 苏泽雄
 * Date: 16/12/1
 * Time: 9:55:42
 */
@Service
public class DangerEvaluateMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(DangerEvaluateMainService.class);

    @Autowired
    DangerEvaluateServiceSup service;
    @Autowired
    DateTimeService timeService; // 日期时间服务
    @Autowired
    DictCachedHandler handler; // 缓存处理器
    @Autowired
    PatientMapper patientMapper; // 病人mapper
    @Autowired
    IdentityService identityService; // 种子表服务


    /**
     * 获取风险及记录
     *
     * @param zyh
     * @param jgid
     * @param bqid
     * @return
     */
    public BizResponse<DEOverview> getDEList(String zyh, String jgid, String bqid) {
        BizResponse<DEOverview> response = new BizResponse<>();
        try {
            // 获取病人
            keepOrRoutingDateSource(DataSource.HRP);
//            String nowStr = timeService.now(DataSource.HRP);
            Patient patient = patientMapper.getPatientByZyh(zyh);
//            String csny = patient.CSNY;
            String brnl = BirthdayUtil.getAgesPairCommonStrOnlySui(patient.CSNY, patient.RYRQ);

            // 获取风险质控
            keepOrRoutingDateSource(DataSource.MOB);
            String now = timeService.now(DataSource.MOB);
            List<DEQualityControlVo> qcList = service.getDEQualityControl();

            // 获取所有风险评估分类
            List<DEOverview> oList = service.getDEList(jgid, brnl, bqid);
            if (oList != null && !oList.isEmpty()) {
                // 获取各风险评估的记录情况
                for (DEOverview overview : oList) {
                    keepOrRoutingDateSource(DataSource.MOB);
                    List<SimDERecord> recordList = service
                            .getSimDERecordList(overview.PGDH, zyh);

                    // 该风险评估没有记录
                    if (recordList == null || recordList.isEmpty()) {
                        continue;
                    }

                    SimDERecord _record = recordList.get(0);
                    overview.PGJL = recordList;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    SimpleDateFormat simpleDateFormatOut = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    overview.PGMS = "上次 " + simpleDateFormatOut.format(simpleDateFormat.parse(_record.PGSJ)) + " " + _record.PGZF + "分 ";
                    overview.TXRQ = _record.TXRQ;

                    // 距离上次填写已经几天了
                    String _pgsj = _record.PGSJ;
                    Long diff = DateUtil.between(_pgsj, now);
                    String bet = String.valueOf(diff);

                    // 汇总当前该类评估单的填写情况，计算今日是否需要填写
                    for (DEQualityControlVo control : qcList) {
                        if (control.PGDH.equals(overview.PGDH) && control.FZSX >= Integer
                                .parseInt(_record.PGZF) && control.FZXX <= Integer
                                .parseInt(_record.PGZF)) {

                            overview.PGMS += control.ZKMS + " ";

                            //
                            if ("0".equals(control.ZKZQ)&&"0".equals(control.ZQPC)){
                                overview.TXJH = "0";
                                continue;
                            }
                            overview.PGMS += control.ZKZQ + "天" + control.ZQPC + "次 ";


                            // 隔几天写一次
                            double days4Write = Double.parseDouble(control.ZKZQ) / Double
                                    .parseDouble(control.ZQPC);
                            double m = diff - days4Write; // 比较两个间隔天数


                            if (m == 0 || m >= 1) { // 超过周期频率或者等于频率为必填
                                overview.TXJH = "2";
                            } else if (m <= -1) {
                                overview.TXJH = "0";
                            } else {
                                overview.TXJH = "1";
                            }
                            //2018-4-13 16:30:49 add
                            StringBuilder stringBuilder = new StringBuilder();
                       /* 按照pgsj计算 提前写的也能计算    if (_pgsj != null && !"".equals(_pgsj)) {
                                if (days4Write > 0) {
                                    //
                                    Long cha = DateUtil.between(_pgsj, now);
                                    if (cha < 0) {
                                        stringBuilder.append("距下次");
                                        stringBuilder.append(Math.abs((int) days4Write - cha));
                                    } else {
                                        if (cha > days4Write) {
                                            stringBuilder.append("已延期");
                                            stringBuilder.append(cha - (int) days4Write);
                                        } else {
                                            stringBuilder.append("距下次");
                                            stringBuilder.append((int) days4Write - cha);
                                        }
                                    }
                                }
                                stringBuilder.append("天");
                            }*/
                            //提醒日期 和PB保持一致
                            Long cha = DateUtil.between(overview.TXRQ, now);
                            if (cha < 0) {
                                stringBuilder.append("距下次");
                                stringBuilder.append(Math.abs(cha));
                            } else {
                                stringBuilder.append("已延期");
                                stringBuilder.append((long) cha);
                            }
                            //
                            overview.PGMS += stringBuilder.toString();
                            overview.PGMS += "天";
//                            overview.PGMS += " 已" + bet + "天";
                            break;
                        }
                    }

                    for (SimDERecord record : recordList) {
                        record.PGHS = handler
                                .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid,
                                        record.PGGH);
                        for (DEQualityControlVo control : qcList) {
                            /*if (control.PGDH.equals(overview.PGDH) && control.FZSX >= Integer
                                    .parseInt(_record.PGZF) && control.FZXX <= Integer
                                    .parseInt(_record.PGZF)) {
                                record.ZKMS = control.ZKMS;
                                break;
                            }*/
                            if (control.PGDH.equals(overview.PGDH)
                                    && control.FZSX >= Integer.parseInt(record.PGZF)
                                    && control.FZXX <= Integer.parseInt(record.PGZF)){
                                record.ZKMS = control.ZKMS;
                                break;
                            }
                        }
                    }
                }
            }
            response.datalist = oList;
            response.isSuccess = true;
            response.message = "获取风险评估列表成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取风险评估列表失败]数据库查询错误";
        } catch (ParseException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取风险评估列表失败]时间格式解析错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取风险评估列表失败]服务内部错误";
        }
        return response;
    }

    /**
     * 获取风险评估的质控规则，风险因子及因子评分
     *
     * @param pgdh
     * @param pglx
     * @param jgid
     * @return
     */
    public BizResponse<DERecord> getNewDangerEvaluate(String pgdh, String pglx, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DERecord> response = new BizResponse<>();

        try {
            DERecord record = new DERecord();
            record.PGDH = pgdh;
            record.PGLX = pglx;
            record.PGSJ = timeService.now(DataSource.MOB);

            // 质控规则
            List<DEQualityControlVo> qcList = service.getSimQualityControl(pgdh, "ASC");
            List<QualityControl> simQCList = new ArrayList<>();
            for (DEQualityControlVo control : qcList) {
                QualityControl qc = castToSimQC(control);
                simQCList.add(qc);
            }
            record.ZKGZ = simQCList;
            // 风险因子及因子评分
            record.FXYZ = service.getDEFactorList(pgdh);
            for (DEFactor factor : record.FXYZ) {
                factor.YZPF = service.getDEFactorGoalList(factor.FXYZ);
            }

            response.isSuccess = true;
            response.data = record;
            response.message = "获取风险评估成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取风险评估失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取风险评估失败]服务内部错误";
        }
        return response;
    }

    /**
     * 获取第一条风险记录,不存在就添加一条
     *
     * @param zyh
     * @param pgdh 废弃字段
     * @param pglx
     * @param jgid
     * @return
     */
    public BizResponse<DERecord> addOrGetDangerEvaluate(String zyh, String pgdh, String pglx,
                                                        String jgid, String hqfs) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DERecord> response = new BizResponse<>();
        List<String> pgxhList = new ArrayList<>();

        try {
            pgxhList = service.getFirstDERecord(zyh, pgdh, pglx, jgid);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估失败]服务内部错误";
        }

        if (pgxhList != null && !pgxhList.isEmpty()) {
        /*	String pgxh = pgxhList.get(0);
            return getDERecord(pgxh, jgid);*/
            if (hqfs.equals("2")) {//评估时间最近的有效记录
                String pgxh = pgxhList.get(pgxhList.size() - 1);
                return getDERecord(pgxh, jgid);
            } else if (hqfs.equals("1")) {//第一次操作的有效记录
                String pgxh = pgxhList.get(0);
                return getDERecord(pgxh, jgid);
            } else if (hqfs.equals("3")) {//当前创建的记录
                //edit为空 所以点击后 不需要数据
                return getNewDangerEvaluate(pgdh, pglx, jgid);
            }
            //原有逻辑数据
            String pgxh = pgxhList.get(0);
            return getDERecord(pgxh, jgid);
        } else {
            return getNewDangerEvaluate(pgdh, pglx, jgid);
        }
    }

    /**
     * @param pglx
     * @param jgid
     * @return
     */
    public BizResponse<DEPGHBean> getFXPGList(String pglx, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DEPGHBean> response = new BizResponse<>();
        List<DEPGHBean> pgdhList;
        try {
            pgdhList = service.getFXPGDHList(pglx, jgid);
            response.isSuccess = true;
            response.datalist = pgdhList;
            response.message = "查询风险评估单号列表成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估单号列表失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估单号列表失败]服务内部错误";
        }
        return response;
    }

    public BizResponse<DEEvaluate> getCSPJList(String csdh) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DEEvaluate> response = new BizResponse<>();
        List<DEEvaluate> list;
        try {
            list = service.getCSPJList(csdh);
            response.isSuccess = true;
            response.datalist = list;
            response.message = "查询风险评估评价列表成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估评价列表失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估评价列表失败]服务内部错误";
        }
        return response;
    }

    public BizResponse<KeyValue<String, String>> getPreOneCSJL(String csdh, String pgxh, String zyh) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<KeyValue<String, String>> response = new BizResponse<>();
        KeyValue<String, String> jlxhAndPgxh = KeyValue.create("", "");
        try {
            SimMeasureRecord simMeasureRecord = service.getPreOneCSJL(csdh, pgxh, zyh);
            if (simMeasureRecord != null) {
                jlxhAndPgxh.key = simMeasureRecord.JLXH;
                jlxhAndPgxh.value = simMeasureRecord.PGXH;
            }
            response.isSuccess = true;
            response.data = jlxhAndPgxh;
            response.message = "查询上次风险评估措施记录成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询上次风险评估措施记录]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询上次风险评估措施记录]服务内部错误";
        }
        return response;
    }

    /**
     * 获取评估记录
     *
     * @param pgxh
     * @return
     */
    public BizResponse<DERecord> getDERecord(String pgxh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DERecord> response = new BizResponse<>();
        DERecord record;

        try {
            // 评估记录
            record = service.getDERecordByPgxh(pgxh);
            if (record == null) {
                response.isSuccess = false;
                response.message = "[未找到风险评估记录]数据库查询错误";
                return response;
            }

            // 质控规则
            List<DEQualityControlVo> qcList = service.getSimQualityControl(record.PGDH, "ASC");
            List<QualityControl> simQCList = new ArrayList<>();
            for (DEQualityControlVo control : qcList) {
                QualityControl qc = castToSimQC(control);
                simQCList.add(qc);
            }
            record.ZKGZ = simQCList;

            // 风险因子及因子评分
            List<DEFactor> factorList = service.getDEFactorList(record.PGDH);
            record.FXYZ = factorList;
            for (DEFactor factor : factorList) {
                List<FactorGoal> goalList = service.getDEFactorGoalWithMXXH(factor.FXYZ, pgxh);
                if (goalList != null && !goalList.isEmpty()) {
                    for (FactorGoal goal : goalList) {
                        if (goal.MXXH != null && !"".equals(goal.MXXH)) {
                            goal.SELECT = true;
                        }
                    }
                }
                factor.YZPF = goalList;
            }

            record.PGHS = handler
                    .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, record.PGGH);

            response.isSuccess = true;
            response.data = record;
            response.message = "获取风险评估记录成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估记录失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[查询风险评估记录失败]服务内部错误";
        }
        return response;
    }

    /**
     * 添加一条风险评估措施，如果已经存在则返回记录
     *
     * @param pgdh
     * @param pglx
     * @param pgxh
     * @param jlxh
     * @param jgid
     * @return
     */
    public BizResponse<DEMeasure> addDEMeasure(String pgdh, String pglx, String pgxh,
                                               String jlxh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DEMeasure> response = new BizResponse<>();
        DEMeasure deMeasure = new DEMeasure();
        MeasureRecord mRecord = new MeasureRecord(); // deMeasure.RECORD
        Integer pgzf = 0;

        try {
            mRecord.CSSJ = timeService.now(DataSource.MOB);
            DERecord deRecord = service.getDERecordByPgxh(pgxh);
            if (deRecord != null) {
                pgzf = Integer.parseInt(deRecord.PGZF);
            }

            // 评估界面进入措施界面返回最近一次的措施记录
            if ("0".equals(jlxh)) {
                deMeasure = getMeasureRecord(jlxh, pgdh, pgxh, pgzf, jgid);
                if (deMeasure.RECORD != null) {
                    response.isSuccess = true;
                    response.data = deMeasure;
                    response.message = "查询成功，返回最近一次的措施记录";
                    return response;
                }
            }
            // 措施列表进入返回指定记录
//            if (!"0".equals(jlxh)) {
            if (!"0".equals(jlxh)&&!"needAdd".equals(jlxh)) {//2018-6-21 10:23:02
                deMeasure = getMeasureRecord(jlxh, pgdh, pgxh, pgzf, jgid);
                response.isSuccess = true;
                response.data = deMeasure;
                response.message = "查询成功，返回指定措施记录";
                return response;
            }

            // 其他情况当作新建处理
            // 评估及措施
            DEMeasureFormVo measureForm = service.getDEMeasureCode(pgdh, jgid);
            if (measureForm != null) {
                String csdh = measureForm.CSDH;
                String fxfx = measureForm.FXFX;
                mRecord.BDXH = csdh;

                List<DEMeasureItemVo> itemList = service.getDEMeasureItems(pgdh, jgid);
                if (itemList != null && !itemList.isEmpty()) {
                    List<MeasureItem> mItemList = new ArrayList<>(); // mRecord.CSXM
                    MeasureItem mItem = new MeasureItem();
                    String zmc = itemList.get(0).ZMC == null ? "" : itemList.get(0).ZMC;//add 2018-5-6 13:04:11
                    mItem.ZMC = zmc;
                    mItemList.add(mItem);

                    for (DEMeasureItemVo item : itemList) {
                        MeasureItem mi = new MeasureItem();
                        item.ZMC = item.ZMC == null ? "" : item.ZMC;//add 2018-5-6 13:04:11
                        if (zmc != null && !zmc.equals(item.ZMC)) {
                            zmc = item.ZMC;
                            mItem = new MeasureItem();
                            mItem.ZMC = zmc;
                            mItemList.add(mItem);
                        }

                        mi.ZMC = zmc;
                        mi.CSXH = item.CSXH;
                        mi.XMNR = item.XMNR;
                        mi.XMZH = item.XMZH;
                        mi.ZDYBZ = "0";

                        if ("1".equals(fxfx)) {
                            if (pgzf <= Integer.parseInt(item.FZSX)) {
                                mi.BTBZ = true;
                            }
                        } else {
                            if (pgzf >= Integer.parseInt(item.FZXX)) {
                                mi.BTBZ = true;
                            }
                        }

                        mItemList.add(mi);
                    }
                    mRecord.CSXM = mItemList;
                }
            }

            // 获取风险措施评价
            List<DEEvaluate> evaluateList = service.getDEEvaluateList(pgdh, jgid);
            DEEvaluate evaluate = new DEEvaluate();
            evaluate.PJXH = "0";
            evaluateList.add(0, evaluate);

            // 整合DEMeasure
            deMeasure.RECORD = mRecord;
            deMeasure.EVALUATE = evaluateList;

            response.isSuccess = true;
            response.data = deMeasure;
            response.message = "添加风险评估措施成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[添加风险评估措施失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[添加风险评估措施失败]服务内部错误";
        }
        return response;
    }

    private DEMeasure getMeasureRecord(String jlxh, String pgdh, String pgxh, Integer pgzf,
                                       String jgid) throws SQLException, DataAccessException {
        DEMeasure measure = new DEMeasure();
        // 主记录
        List<MeasureRecord> mRecordList = service.getDEMeasureRecord(pgxh);
        if (mRecordList != null && !mRecordList.isEmpty()) {
            if (!"0".equals(jlxh)) {
                for (MeasureRecord mRecord : mRecordList) {
                    String _jlxh = mRecord.JLXH;
                    if (_jlxh != null && _jlxh.equals(jlxh)) {
                        measure.RECORD = mRecord;
                        break;
                    }
                }
            } else {
                measure.RECORD = mRecordList.get(0);
            }
            String hszqm = measure.RECORD.HSZQM;
            if (hszqm != null && !"".equals(hszqm)) {
                measure.RECORD.HSZXM = handler
                        .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, hszqm);
            }
            String csgh = measure.RECORD.CSGH;
            if (csgh != null && !"".equals(csgh)) {
                measure.RECORD.HSXM = handler
                        .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, csgh);
            }

            // 获取措施单号
            keepOrRoutingDateSource(DataSource.MOB);
            DEMeasureFormVo measureForm = service.getDEMeasureCode(pgdh, jgid);
            if (measureForm != null) {
                String csdh = measureForm.CSDH;
                String fxfx = measureForm.FXFX;

                // 措施项目
                List<DEMeasureItemVo> itemList = service
                        .getDEMeasureItemRecord(csdh, measure.RECORD.JLXH);
                if (itemList != null && !itemList.isEmpty()) {
                    List<MeasureItem> mItemList = new ArrayList<>(); // measure.RECORD.CSXM

                    MeasureItem mItem = new MeasureItem();
                    String zmc = itemList.get(0).ZMC;
                    mItem.ZMC = zmc;
                    String jlxm = itemList.get(0).JLXM;
                    mItem.SELECT = (jlxm != null && !"".equals(jlxm));
                    mItemList.add(mItem);

                    for (DEMeasureItemVo item : itemList) {
                        MeasureItem mi = new MeasureItem();
                        if (zmc != null && !zmc.equals(item.ZMC)) {
                            zmc = item.ZMC;
                            mItem = new MeasureItem();
                            mItem.ZMC = zmc;
                            mItem.SELECT = (item.JLXM != null && !"".equals(item.JLXM));
                            mItemList.add(mItem);
                        }

                        mi.ZMC = zmc;
                        mi.CSXH = item.CSXH;
                        mi.XMNR = item.XMNR;
                        mi.JLXM = item.JLXM;
                        mi.ZDYBZ = item.ZDYBZ;
                        mi.SELECT = (item.JLXM != null && !"".equals(item.JLXM));

                        if ("自定义措施".equals(mi.ZMC)) {
                            mi.BTBZ = true;
                        }
                        if ("1".equals(fxfx)) {
                            if (pgzf <= Integer.parseInt(item.FZSX)) {
                                mi.BTBZ = true;
                            }
                        } else {
                            if (pgzf >= Integer.parseInt(item.FZXX)) {
                                mi.BTBZ = true;
                            }
                        }

                        mItemList.add(mi);
                    }
                    measure.RECORD.CSXM = mItemList;
                }
            }

            // 评价
            List<DEEvaluate> evaluateList = service.getDEEvaluateList(pgdh, jgid);
            DEEvaluate evaluate = new DEEvaluate();
            evaluate.PJXH = "0";
            evaluateList.add(0, evaluate);
            measure.EVALUATE = evaluateList;
        }
        return measure;
    }

    /**
     * 保存风险评估记录
     *
     * @param data JSON格式的保存数据，包含DERecord,ZYH,BQID,JGID
     * @return
     */
    public BizResponse<DERecord> saveDangerEvaluate(DERecordPostData data) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DERecord> response = new BizResponse<>();

        // 拆分data，初始化参数
        DERecord record = data.DERecord;
        String zyh = data.ZYH;
        String bqid = data.BQID;
        String jgid = data.JGID;

        String czbz;
        String pgxh = record.PGXH; // 评估序号
        String fxcd = "0"; // 风险程度
        String zkxh = "0"; // 质控序号
        String zkms = ""; // 质控描述
        double zqpc; // 质控描述
        Integer pgzf = Integer.parseInt(record.PGZF); // 评估总分
        Double jgts = 0d; // 隔几天写一次(两次书写的间隔天数)

        // 保存时的操作数据
        List<DERecordVo> recordAddList = new ArrayList<>();
        List<DERecordVo> recordUpdateList = new ArrayList<>();
        List<DERecordDetailVo> detailAddList = new ArrayList<>();
        List<String> detailDeleteList = new ArrayList<>();
        Map<String, String> remindFinshMap = new HashMap<>();
        List<DEQCRemindVo> remindAddList = new ArrayList<>();

        try {
            // 当前服务器时间
            String now = timeService.now(DataSource.MOB);

            // 计算风险程度
            List<DEQualityControlVo> qcList = service.getSimQualityControl(record.PGDH, "ASC");
            if (qcList != null && !qcList.isEmpty()) {
                for (DEQualityControlVo control : qcList) {
                    if (pgzf <= control.FZSX && pgzf >= control.FZXX) {
                        fxcd = control.FXCD;
                        zkxh = control.ZKXH;
                        zkms = control.ZKMS;
                        //
                        zqpc = (control.ZQPC == null || "".equals(control.ZQPC)) ? 0d : Double.parseDouble(control.ZQPC);

                        if (zqpc == 0d) {
                            jgts = 0d;
                        } else {
                            jgts = Double.parseDouble(control.ZKZQ) / zqpc;
                            if (jgts == 0d) {
                                jgts = 1d;
                            }
                        }
                        break;
                    }
                }
            }

            // 组建保存对象
            if (pgxh == null || "".equals(pgxh)) { // 新建
                czbz = "0";
                // 评估记录
                DERecordVo recordVo = new DERecordVo();
                pgxh = String.valueOf(identityService.getIdentityMax("IENR_FXPGJL", DataSource.MOB)); // 获取主键
                recordVo.PGXH = pgxh;
                recordVo.JGID = jgid;
                recordVo.PGLX = record.PGLX;
                recordVo.ZYH = zyh;
                recordVo.CJSJ = now;
                recordVo.CJGH = record.PGGH;
                recordVo.BRBQ = bqid;
                recordVo.PGDH = record.PGDH;
                recordVo.PGSJ = record.PGSJ;
                recordVo.PGGH = record.PGGH;
                recordVo.PGZF = record.PGZF;
                recordVo.FXCD = fxcd;
                recordVo.CDMS = record.ZKMS;
                recordAddList.add(recordVo);

                // 因子评分记录(评估记录明细)
                if (record.FXYZ != null && !record.FXYZ.isEmpty()) {
                    for (DEFactor factor : record.FXYZ) {
                        if (factor.YZPF != null && !factor.YZPF.isEmpty()) {
                            for (FactorGoal goal : factor.YZPF) {
                                if (goal.SELECT) {
                                    DERecordDetailVo detail = new DERecordDetailVo();
                                    detail.MXXH = String.valueOf(
                                            identityService.getIdentityMax("IENR_FXPGMX", DataSource.MOB));
                                    detail.FZYZ = factor.FXYZ;
                                    detail.FZXH = String.valueOf(goal.FZXH);
                                    detail.PFFZ = goal.PFFZ;
                                    detail.PGXH = pgxh;
                                    detailAddList.add(detail);
                                }
                            }
                        }
                    }
                }
            } else { // 修改
                czbz = "1";
                // 评估记录
                DERecordVo recordVo = new DERecordVo();
                recordVo.PGSJ = record.PGSJ;
                recordVo.PGGH = record.PGGH;
                recordVo.PGZF = record.PGZF;
                recordVo.PGXH = record.PGXH;
                recordVo.FXCD = fxcd;
                recordVo.CDMS = record.ZKMS;
                recordUpdateList.add(recordVo);

                // 因子评分记录(评估记录明细)
                if (record.FXYZ != null && !record.FXYZ.isEmpty()) {
                    for (DEFactor factor : record.FXYZ) {
                        if (factor.YZPF != null && !factor.YZPF.isEmpty()) {
                            for (FactorGoal goal : factor.YZPF) {
                                if (StringUtils.isBlank(goal.MXXH) && goal.SELECT) { // 新增
                                    DERecordDetailVo detail = new DERecordDetailVo();
                                    detail.MXXH = String.valueOf(
                                            identityService.getIdentityMax("IENR_FXPGMX", DataSource.MOB));
                                    detail.FZYZ = factor.FXYZ;
                                    detail.FZXH = String.valueOf(goal.FZXH);
                                    detail.PFFZ = goal.PFFZ;
                                    detail.PGXH = pgxh;
                                    detailAddList.add(detail);
                                } else if (StringUtils.isNotBlank(goal.MXXH) && !goal.SELECT) { // 删除
                                    detailDeleteList.add(goal.MXXH);
                                }
                            }
                        }
                    }
                }
            }

            // 风险质控提醒
            // 将该类型的评估提醒都置为已提醒状态
            remindFinshMap.put("PGLX", record.PGLX);
            remindFinshMap.put("ZYH", zyh);

            // 插入提醒
            if (jgts != 0d) {
                // 必须要做评估的提醒记录
                DEQCRemindVo remindVo = new DEQCRemindVo();
                remindVo.TXXH = String.valueOf(identityService.getIdentityMax("IENR_FXZKTX", DataSource.MOB));
                remindVo.ZKXH = zkxh;
                remindVo.ZYH = zyh;
                remindVo.BRBQ = bqid;
                remindVo.PGXH = pgxh;
                remindVo.TXNR = record.BRCH + "床" + record.BRXM + "今日必须做" + record.BDMC + ",上次评估" + DateUtil.getApplicationDate() + "【" + zkms + "】";
                String jgts1 = String.valueOf(Math.ceil(jgts));
                remindVo.TXRQ = DateUtil.dateoffDays(now, jgts1.substring(0, jgts1.indexOf(".")));
                remindVo.CJSJ = now;
                remindVo.PGZF = record.PGZF;
                remindVo.JGID = jgid;
                remindVo.PGLX = record.PGLX;
                remindVo.FXCD = fxcd;
                remindAddList.add(remindVo);
                // 可能要做评估的提醒记录
                if (Math.floor(jgts) < jgts) {
                    DEQCRemindVo remind = new DEQCRemindVo();
                    remind.TXXH = String
                            .valueOf(identityService.getIdentityMax("IENR_FXZKTX", DataSource.MOB));
                    remind.ZKXH = zkxh;
                    remind.ZYH = zyh;
                    remind.BRBQ = bqid;
                    remind.PGXH = pgxh;
                    remind.TXNR = record.BRCH + "床" + record.BRXM + "今日可能要做" + record.BDMC + ",上次评估" + DateUtil.getApplicationDate() + "【" + zkms + "】";
                    String jgts2 = String.valueOf(Math.floor(jgts));
                    remind.TXRQ = DateUtil.dateoffDays(now, jgts2.substring(0, jgts2.indexOf(".")));
                    remind.CJSJ = now;
                    remind.PGZF = record.PGZF;
                    remind.JGID = jgid;
                    remind.PGLX = record.PGLX;
                    remind.FXCD = fxcd;
                    remindAddList.add(remind);
                }
            }else{
                //无风险
                DEQCRemindVo remindVo = new DEQCRemindVo();
                remindVo.TXXH = String.valueOf(identityService.getIdentityMax("IENR_FXZKTX", DataSource.MOB));
                remindVo.ZKXH = zkxh;
                remindVo.ZYH = zyh;
                remindVo.BRBQ = bqid;
                remindVo.PGXH = pgxh;
                remindVo.TXNR = "无";
                remindVo.TXRQ = null;//无时间
                remindVo.CJSJ = now;
                remindVo.PGZF = record.PGZF;
                remindVo.JGID = jgid;
                remindVo.PGLX = record.PGLX;
                remindVo.FXCD = fxcd;
                remindAddList.add(remindVo);
            }
            // 执行数据库操作
            keepOrRoutingDateSource(DataSource.MOB);
            service.saveDERecord(recordAddList, recordUpdateList, detailAddList,
                    detailDeleteList, remindFinshMap, remindAddList);

            // 获取返回值
            DERecord _record = getDERecord(pgxh, jgid).data;
            Response<SelectResult> syncResponse = buildDERecordSyncData(zyh, bqid, _record, czbz,
                    jgid);
            if (syncResponse.ReType == 2) {
                _record.IsSync = true;
                _record.SyncData = syncResponse.Data;
            }

            response.isSuccess = true;
            response.data = _record;
            response.message = "保存风险评估记录成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存风险评估记录失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存风险评估记录失败]服务内部错误";
        }
        return response;
    }

    /**
     * 获取评估措施列表
     *
     * @param pgdh
     * @param pgxh
     * @param zyh
     * @param jgid
     * @return
     */
    public BizResponse<MeasureOverview> getDEMeasureList(String pgdh, String pgxh, String zyh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<MeasureOverview> response = new BizResponse<>();

        try {
            // 风险措施表
            List<MeasureOverview> list = service.getDEMeasureList(pgdh, jgid);
            if (list != null && !list.isEmpty()) {
                // 措施记录
                for (MeasureOverview measure : list) {
                    keepOrRoutingDateSource(DataSource.MOB);
                    List<SimMeasureRecord> recordList = service.getSimMeasureRecord(measure.CSDH, pgxh, zyh, jgid);
                    measure.RECOORD = recordList;
                    if (recordList != null && !recordList.isEmpty()) {
                        for (SimMeasureRecord record : recordList) {
                            record.CSXM = handler
                                    .getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, record.CSGH);
                        }
                    }
                }
            }

            response.isSuccess = true;
            response.datalist = list;
            response.message = "获取评估措施列表成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取评估措施列表失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取评估措施列表失败]服务内部错误";
        }
        return response;
    }

    /**
     * 保存评估措施记录
     *
     * @param data
     * @return
     */
    public BizResponse<DEMeasure> saveDEMeasure(MeasureRecordPostData data) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DEMeasure> response = new BizResponse<>();

        // 拆分data，初始化参数
        MeasureRecord record = data.MeasureRecord;
        String pgdh = data.PGDH;
        String zyh = data.ZYH;
        String bqid = data.BQID;
        String jgid = data.JGID;

        String jlxh;
        String czbz;//操作标志

        List<MeasureRecordVo> recordAddList = new ArrayList<>();
        List<MeasureRecordVo> recordUpdateList = new ArrayList<>();
        List<MeasureItemVo> itemAddList = new ArrayList<>();
        List<String> itemDeleteList = new ArrayList<>();
        try {
            if (record == null) {
                response.isSuccess = false;
                response.data = null;
                response.message = "无评估措施记录数据保存";
                return response;
            }

            String now = timeService.now(DataSource.MOB); // 当前服务器时间
            if (record.JLXH == null || "".equals(record.JLXH)) { // 新增
                czbz = "0";
                // 主记录
                MeasureRecordVo recordVo = new MeasureRecordVo();
                jlxh = String.valueOf(identityService.getIdentityMax("IENR_FXCSJL", DataSource.MOB));
                recordVo.JLXH = jlxh;
                recordVo.ZYH = zyh;
                recordVo.BDXH = record.BDXH;
                recordVo.PGXH = record.PGXH;
                recordVo.CJSJ = now;
                recordVo.CSGH = record.CSGH;
                recordVo.CSSJ = record.CSSJ;
                recordVo.JGID = jgid;
                recordVo.BRBQ = bqid;
//                recordVo.ZGQK = record.ZGQK;
                recordVo.CSPJ = record.CSPJ;
                //ADD 2018-7-3 15:09:26
                recordVo.HSZQM = record.HSZQM;
                recordVo.HSZQMSJ = record.HSZQMSJ;
                recordAddList.add(recordVo);

                // 措施记录
                if (record.CSXM != null && !record.CSXM.isEmpty()) {
                    for (MeasureItem item : record.CSXM) {
                        if (item.SELECT && item.CSXH != null && !"".equals(item.CSXH)) { // 去除组
                            MeasureItemVo itemVo = new MeasureItemVo();
                            itemVo.JLXM = String.valueOf(identityService.getIdentityMax("IENR_FXCSXM", DataSource.MOB));
                            if ("1".equals(item.ZDYBZ)) {
                                itemVo.JLXH = jlxh;
                                itemVo.CSXH = item.CSXH;
                                itemVo.ZDYBZ = item.ZDYBZ;
                                itemVo.XMNR =
                                        (item.XMNR == null || "".equals(item.XMNR)) ? "null" :
                                                item.XMNR;
                            } else {
                                itemVo.JLXH = jlxh;
                                itemVo.CSXH = item.CSXH;
                                itemVo.ZDYBZ = item.ZDYBZ;
                                itemVo.XMNR = "''";
                            }
                            itemAddList.add(itemVo);
                        }
                    }
                }
            } else { // 更新
                czbz = "1";
                // 主记录
                MeasureRecordVo recordVo = new MeasureRecordVo();
                jlxh = record.JLXH;
                recordVo.JLXH = jlxh;
                recordVo.CSSJ = record.CSSJ;
                recordVo.CSGH = record.CSGH;
//                recordVo.ZGQK = record.ZGQK;
                recordVo.CSPJ = record.CSPJ;
                //ADD 2018-7-3 15:09:26
                recordVo.HSZQM = record.HSZQM;
                recordVo.HSZQMSJ = record.HSZQMSJ;
                recordUpdateList.add(recordVo);

                // 措施纪录
                if (record.CSXM != null && !record.CSXM.isEmpty()) {
                    for (MeasureItem item : record.CSXM) {
                        if ((item.JLXM == null || "".equals(item.JLXM)) && item.SELECT && (item.CSXH != null && !""
                                .equals(item.CSXH))) { // 插入
                            MeasureItemVo itemVo = new MeasureItemVo();
                            itemVo.JLXM = String.valueOf(identityService.getIdentityMax("IENR_FXCSXM", DataSource.MOB));
                            itemVo.JLXH = record.JLXH;
                            itemVo.CSXH = item.CSXH;
                            itemVo.ZDYBZ = item.ZDYBZ;
                            itemVo.XMNR = item.XMNR;
                            itemAddList.add(itemVo);
                        } else if (!item.SELECT && (item.JLXM != null && !"".equals(item.JLXM)) && (item.CSXH != null && !""
                                .equals(item.CSXH))) { // 删除
                            itemDeleteList.add(item.JLXM);
                        }
                    }
                }
            }
            // 执行数据库操作
            keepOrRoutingDateSource(DataSource.MOB);
            service.saveDEMeasure(recordAddList, recordUpdateList, itemAddList, itemDeleteList);

            // 获取返回值
            Integer pgzf = 0;
            DERecord deRecord = service.getDERecordByPgxh(record.PGXH);
            if (deRecord != null) {
                pgzf = Integer.parseInt(deRecord.PGZF);
            }
            DEMeasure measure = getMeasureRecord(jlxh, pgdh, record.PGXH, pgzf, jgid);

            Response<SelectResult> syncResponse = buildDEMeasureSyncData(zyh, measure, czbz, jgid);
            if (syncResponse.ReType == 2) {
                measure.IsSync = true;
                measure.SyncData = syncResponse.Data;
            }

            response.isSuccess = true;
            response.data = measure;
            response.message = "保存评估措施记录成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存评估措施记录失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存评估措施记录失败]服务内部错误";
        }
        return response;
    }

    /**
     * 删除评估记录
     *
     * @param pgxh
     * @param jgid
     * @return
     */
    public BizResponse<String> deleteDERecord(String pgxh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> response = new BizResponse<>();

        try {
            String zyh = null;
            String pglx;
            String maxpgxh = null;
            Boolean needCancle = false; // 是否需要撤销maxpgxh的已提醒状态
            Map<String, String> map = new HashMap<>();

            // 获取最大pgxh
            DEQCRemindVo remind = service.getZYHOfDEQCRemind(pgxh);
            if (remind != null) {
                zyh = remind.ZYH;
                pglx = remind.PGLX;

                maxpgxh = service.getMAXOfDEQCRemind(zyh, pglx);
            }
            // pgxh > maxpgxh时，需撤销maxpgxh的已提醒状态
            if (maxpgxh != null && !"".equals(maxpgxh) && Integer.parseInt(pgxh) > Integer.parseInt(maxpgxh)) {
                needCancle = true;
                map.put("maxpgxh", maxpgxh);
                map.put("zyh", zyh);
            }
            // 删除评估记录，作废提醒
            map.put("pgxh", pgxh);
            Integer num = service.deleteDERecord(map, needCancle);

            // 同步数据的操作
            if (num >= 0) {
                deleSyncData("2", pgxh, "0", "0", jgid);
            }

            response.isSuccess = num >= 0;
            response.data = num >= 0 ? "风险评估记录删除成功" : "风险评估记录删除失败";
            response.message = response.data;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[风险评估记录删除失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[风险评估记录删除失败]服务内部错误";
        }
        return response;
    }

    /**
     * 删除措施评价
     *
     * @param jlxh
     * @param jgid
     * @return
     */
    public BizResponse<String> deleteDEMeasure(String jlxh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> response = new BizResponse<>();

        try {
            Integer num = service.deleteDEMeasure(jlxh);

            // 同步数据的操作
            if (num >= 0) {
                deleSyncData("7", jlxh, "0", "0", jgid);
            }

            response.isSuccess = num >= 0;
            response.data = num >= 0 ? "删除措施评价成功" : "删除措施评价失败";
            response.message = response.data;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除措施评价失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除措施评价失败]服务内部错误";
        }
        return response;
    }

    /**
     * 护士长审阅
     *
     * @param pgxh
     * @param hszgh
     * @param jgid
     * @return
     */
    public BizResponse<DERecord> checkDERecord(String pgxh, String hszgh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DERecord> response = new BizResponse<>();

        try {
            String now = timeService.now(DataSource.MOB);
            service.checkDERecord(hszgh, now, pgxh);

            DERecord record = getDERecord(pgxh, jgid).data;

            response.isSuccess = true;
            response.data = record;
            response.message = "护士长审阅成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护士长审阅失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护士长审阅失败]服务内部错误";
        }
        return response;
    }

    /**
     * 评价风险措施
     *
     * @param jlxh
     * @param pjsj
     * @param pjjg
     * @param pjr
     * @param pgdh
     * @param pgxh
     * @param jgid
     * @return
     */
    public BizResponse<DEMeasure> evaluateMeasure(String jlxh, String pjsj, String pjjg, String pjr, String pgdh,
                                                  String pgxh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<DEMeasure> response = new BizResponse<>();

        try {
            service.evaluateMeasure(jlxh, pjr, pjsj, pjjg);

            Integer pgzf = 0;
            DERecord record = service.getDERecordByPgxh(pgxh);
            if (record != null) {
                pgzf = Integer.parseInt(record.PGZF);
            }

            DEMeasure measure = getMeasureRecord(jlxh, pgdh, pgxh, pgzf, jgid);

            response.isSuccess = true;
            response.data = measure;
            response.message = "风险措施评价成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[风险措施评价失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[风险措施评价失败]服务内部错误";
        }
        return response;
    }

    /**
     * 获取当前的疼痛综合评估记录，没有时则新增
     *
     * @param pgxh
     * @param jgid
     * @return
     */
    public BizResponse<PainEvaluate> addOrGetPE(String pgxh, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<PainEvaluate> response = new BizResponse<>();

        try {
            // 获取疼痛评估项目
            List<PainEvaluate> list = service.getPainEvaluate(jgid);
            if (list != null && !list.isEmpty()) {
                for (PainEvaluate pain : list) {
                    // 获取疼痛评估选项
                    if ("1".equals(pain.XMLX)) { // 手动输入(没有选项)
                        PEOption option = service.getPEOptionOfNoXXXH(pgxh, pain.XMXH);
                        List<PEOption> optionList = new ArrayList<>();
                        if (option == null) {
                            option = new PEOption();
                            option.XMXH = pain.XMXH;
                            option.XXMC = "";
                            option.XGBZ = "1";
                        } else {
                            if (option.JLXM != null && !"".equals(option.JLXM)) {
                                option.SELECT = true;
                            }
                        }
                        optionList.add(option);
                        pain.PGXX = optionList;
                    } else { // 选择
                        List<PEOption> optionList = service.getPEOption(pgxh, pain.XMXH);
                        if (optionList != null && !optionList.isEmpty()) {
                            for (PEOption option : optionList) {
                                if (option.JLXM != null && !"".equals(option.JLXM)) {
                                    option.SELECT = true;
                                }
                            }
                        }
                        pain.PGXX = optionList;
                    }
                }
            }

            response.isSuccess = true;
            response.datalist = list;
            response.message = "获取疼痛综合评估成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取疼痛综合评估失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取疼痛综合评估失败]服务内部错误";
        }
        return response;
    }

    /**
     * 保存疼痛综合评估记录
     *
     * @param data
     * @return
     */
    public BizResponse<PainEvaluate> savePainEvaluate(PERecordPostData data) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<PainEvaluate> response = new BizResponse<>();

        //拆分data，初始化参数
        List<PEOption> optionList = data.RECORDS;
        String jgid = data.JGID;
        String zyh = data.ZYH;
        String pgxh = data.PGXH;

        List<PERecordVo> recordAddList = new ArrayList<>();
        List<PERecordVo> recordUpdateList = new ArrayList<>();
        List<String> recordDeleteList = new ArrayList<>();
        try {
            for (PEOption option : optionList) {
                String jlxm = option.JLXM;
                if (jlxm == null || "".equals(jlxm)) { // 新建
                    if (!option.SELECT) {
                        continue;
                    }
                    PERecordVo record = new PERecordVo();
                    record.JLXM = String.valueOf(identityService.getIdentityMax("IENR_TTXMJL", DataSource.MOB));
                    record.JGID = jgid;
                    record.ZYH = zyh;
                    record.PGXH = pgxh;
                    record.XMXH = option.XMXH;
                    record.XXXH = option.XXXH;
                    record.XMQZ = option.XMQZ;
                    recordAddList.add(record);
                } else { // 更新或删除
                    if (option.SELECT) { // 更新
                        PERecordVo record = new PERecordVo();
                        record.JLXM = jlxm;
                        record.XXXH = option.XXXH;
                        record.XMQZ = option.XMQZ;
                        recordUpdateList.add(record);
                    } else {
                        recordDeleteList.add(jlxm);
                    }
                }
            }
            // 执行数据库操作
            keepOrRoutingDateSource(DataSource.MOB);
            service.savePainEvaluate(recordAddList, recordUpdateList, recordDeleteList);

            // 获取返回数据
            List<PainEvaluate> peList = addOrGetPE(pgxh, jgid).datalist;

            response.isSuccess = true;
            response.datalist = peList;
            response.message = "保存疼痛综合评估成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存疼痛综合评估失败]数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存疼痛综合评估失败]服务内部错误";
        }
        return response;
    }

    /**
     * 构造风险评估措施的同步数据
     *
     * @param zyh
     * @param measure
     * @param czbz
     * @param jgid
     * @return
     */
    private Response<SelectResult> buildDEMeasureSyncData(String zyh, DEMeasure measure, String czbz,
                                                          String jgid) {
        InArgument inArgument = new InArgument();
        inArgument.zyh = zyh;
        inArgument.bqdm = measure.RECORD.BRBQ;
        inArgument.hsgh = measure.RECORD.CSGH;
        inArgument.bdlx = "7";
        inArgument.lybd = measure.RECORD.BDXH;
        inArgument.flag = czbz;
        inArgument.jlxh = measure.RECORD.JLXH;
        inArgument.jgid = jgid;
        inArgument.lymx = "0";
        inArgument.lymxlx = "0";
        String jlsj = measure.RECORD.CSSJ;
        if (jlsj == null || "".equals(jlsj)) {
            jlsj = timeService.now(DataSource.PORTAL);
        }
        inArgument.jlsj = jlsj;

        Project project = new Project("1", measure.RECORD.JLXH);
        for (MeasureItem item : measure.RECORD.CSXM) {
            if (item.SELECT && !StringUtils.isBlank(item.CSXH)) {
                Project newProject = new Project(item.JLXM, item.XMNR);
                project.saveProjects.add(newProject);
            }
        }
        inArgument.projects.add(project);

        Response<SelectResult> response = new Response<>();
        try {
            response = Client.rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchron", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
        return response;
    }

    /**
     * 构造风险评估的同步数据
     *
     * @param zyh
     * @param brbq
     * @param record
     * @param czbz
     * @param jgid
     * @return
     */
    private Response<SelectResult> buildDERecordSyncData(String zyh, String brbq, DERecord record,
                                                         String czbz, String jgid) {
        InArgument inArgument = new InArgument();
        inArgument.zyh = zyh;
        inArgument.bqdm = brbq;
        inArgument.hsgh = record.PGGH;
        inArgument.bdlx = "2";
        inArgument.lybd = record.PGDH;
        inArgument.flag = czbz;
        inArgument.jlxh = record.PGXH;
        inArgument.jgid = jgid;
        inArgument.lymx = "0";
        inArgument.lymxlx = "0";
        String jlsj = record.PGSJ;
        if (jlsj == null || "".equals(jlsj)) {
            jlsj = timeService.now(DataSource.PORTAL);
        }
        inArgument.jlsj = jlsj;

        Project project = new Project("0", record.PGXH);
        Project newProject = new Project("0", record.PGZF);
        project.saveProjects.add(newProject);
        inArgument.projects.add(project);

        Response<SelectResult> response = new Response<>();
        try {
            response = Client.rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchron", inArgument);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
        return response;
    }

    /**
     * 将数据库获取的风险评估质控转化为向Android传输的简单格式
     *
     * @param control
     * @return
     */
    private QualityControl castToSimQC(DEQualityControlVo control) {
        QualityControl qc = new QualityControl();
        qc.ZKMS = control.ZKMS;
        qc.FZSX = control.FZSX;
        qc.FZXX = control.FZXX;
        qc.CSBZ = control.CSBZ;
        return qc;
    }

    public Response<String> synchronRepeat(SelectResult data) {
        Response<String> response = new Response<>();
        try {
            response = Client.rpcInvoke("nis-synchron.synchronRpcServerProvider", "synchronRepeat", data);
        } catch (Throwable throwable) {
            response.ReType = 0;
            response.Msg = "同步目标失败";
            logger.error(throwable.getMessage(), throwable);
        }
        return response;
    }

    public void deleSyncData(String bdlx, String jlxh, String lymx, String lymxlx, String jgid) {
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
}
