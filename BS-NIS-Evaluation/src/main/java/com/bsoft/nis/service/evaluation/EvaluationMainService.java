package com.bsoft.nis.service.evaluation;


import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.common.service.portal.DataBaseLinkService;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.dangerevaluate.DERecord;
import com.bsoft.nis.domain.evaluation.*;
import com.bsoft.nis.domain.evaluation.db.*;
import com.bsoft.nis.domain.evaluation.dynamic.*;
import com.bsoft.nis.domain.evaluation.pgnr.EvalBaseVo;
import com.bsoft.nis.domain.evaluation.pgnr.EvalFljlVo;
import com.bsoft.nis.domain.evaluation.pgnr.EvalJlxmVo;
import com.bsoft.nis.domain.evaluation.pgnr.EvalXmxxVo;
import com.bsoft.nis.domain.healthguid.HealthGuidData;
import com.bsoft.nis.domain.lifesign.LifeSignHistoryDataItem;
import com.bsoft.nis.domain.patient.db.SickPersonDetailVo;
import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.Project;
import com.bsoft.nis.domain.synchron.SelectResult;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.evaluation.support.EvaluationService;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
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
import java.util.stream.Collectors;


/**
 * 护理评估主服务
 * Created by Administrator on 2016/10/10.
 */
@Service
public class EvaluationMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(EvaluationMainService.class);

    @Autowired
    EvaluationService service;

    @Autowired
    SystemParamService systemParamService;//获取系统参数服务

    @Autowired
    PatientServiceSup patientService;//病人信息服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    @Autowired
    DataBaseLinkService dataService;//数据库连接服务

    @Autowired
    IdentityService identity;//获取种子服务

    @Autowired
    DateTimeService timeService;//获取数据库时间服务

    public Integer operationTypeAdd = 1;//新增

    public Integer operationTypeModification = operationTypeAdd + 1;//修改

    public Integer operationTypeSignature = operationTypeModification + 1;//签名

    public BizResponse<String> getSJHQFS(String yslx) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> bizResponse = new BizResponse<>();
        try {
            bizResponse.data = service.getSJHQFS(yslx);
            bizResponse.isSuccess = true;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

    public BizResponse<EvaluateRecordItem> GetEvaluationList(String start, String end, String zyh, String jgid) {

        List<EvaluateRecordItem> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BizResponse<EvaluateRecordItem> bizResponse = new BizResponse<>();
        Calendar cal = Calendar.getInstance();
        try {
            if (zyh == null || zyh.equals("")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "查询失败：未选择病人";
                return bizResponse;
            }
            if (sdf.parse(start).getTime() > sdf.parse(end).getTime()) {
                bizResponse.isSuccess = false;
                bizResponse.message = "查询失败：起始日期大于结束日期";
                return bizResponse;
            }
            //结束时间加1
            cal.setTime(sdf.parse(end));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            end = sdf.format(cal.getTime());
//            //获取emr同步系统参数
//            BizResponse<String> biz = systemParamService.getUserParams("1", "IENR", "IENR_E_C_EMR", jgid, DataSource.MOB);
//            if (!biz.isSuccess) {
//                bizResponse.isSuccess = false;
//                bizResponse.message = "获取用户参数错误" + biz.message;
//                return bizResponse;
//            }
//            if (biz.datalist.get(0).equals("1")) {
//                keepOrRoutingDateSource(DataSource.HRP);
//                list = service.getRcordData(zyh, start, end, jgid, "EMR");
//            }
            keepOrRoutingDateSource(DataSource.MOB);
            bizResponse.datalist = service.getRcordData(zyh, start, end, jgid, "MOB");

            bizResponse.isSuccess = true;
            return bizResponse;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }

        return bizResponse;

    }

    public BizResponse<EvaluateRecordItem> getAllEvaluationList(String zyh, String jgid) {

        List<EvaluateRecordItem> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BizResponse<EvaluateRecordItem> bizResponse = new BizResponse<>();
        Calendar cal = Calendar.getInstance();
        try {
            if (zyh == null || zyh.equals("")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "查询失败：未选择病人";
                return bizResponse;
            }
//            //获取emr同步系统参数
//            BizResponse<String> biz = systemParamService.getUserParams("1", "IENR", "IENR_E_C_EMR", jgid, DataSource.MOB);
//            if (!biz.isSuccess) {
//                bizResponse.isSuccess = false;
//                bizResponse.message = "获取用户参数错误" + biz.message;
//                return bizResponse;
//            }
//            if (biz.datalist.get(0).equals("1")) {
//                keepOrRoutingDateSource(DataSource.HRP);
//                list = service.getRcordData(zyh, start, end, jgid, "EMR");
//            }
            keepOrRoutingDateSource(DataSource.MOB);
            bizResponse.datalist = service.getAllRecordData(zyh, jgid, "MOB");

            bizResponse.isSuccess = true;
            return bizResponse;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }

        return bizResponse;

    }

    private boolean hasIn(List<EvaluateRecordItem> raw, String tempYSXH) {
        for (EvaluateRecordItem evaluateRecordItem : raw) {
            if (tempYSXH.equals(evaluateRecordItem.YSXH)) {
                return true;
            }
        }
        return false;
    }

    public BizResponse<EvaluateRecordItem> GetNewEvaluationList(String bqdm, String jgid, String zyh) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<EvaluateRecordItem> bizResponse = new BizResponse<>();
        try {
            bizResponse.datalist = service.GetNewEvaluationList(bqdm, jgid);
            if (bizResponse.datalist != null) {
                //协和需求 该病人曾经写过的记录
                BizResponse<EvaluateRecordItem> temp = getAllEvaluationList(zyh, jgid);
                for (EvaluateRecordItem evaluateRecordItem : temp.datalist) {
                    //查找未该病区未显示的记录对应的样式
                    if (!hasIn(bizResponse.datalist, evaluateRecordItem.YSXH)) {
                        evaluateRecordItem.isZKNotCheckBQ = true;
//                       evaluateRecordItem.TXSJ = null;
                        bizResponse.datalist.add(evaluateRecordItem);
                    }
                }
            }
            bizResponse.isSuccess = true;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

    public BizResponse<EvaluateRecordItem> GetNewEvaluationListForYslx(String yslx, String bqdm, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<EvaluateRecordItem> bizResponse = new BizResponse<>();
        try {

            bizResponse.datalist = service.GetNewEvaluationListForYslx(yslx, bqdm, jgid);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        bizResponse.isSuccess = true;
        return bizResponse;
    }

    public BizResponse<EvaluateFormItem> GetExistEvaluation(String jlxh, String txsj, String jgid, String dzlx) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<EvaluateFormItem> bizResponse = new BizResponse<>();
        try {
            EvaluateBDJLVo jlVo = service.getBDJL(jlxh);
            List<EvaluateBDJLXMVo> xmList = service.getBDJLXM(jlxh, null);
            List<EvaluateBDJLXXVo> xxList = service.getBDJLXX(jlxh, null, null);
            if (jlVo != null) {
                String zyh = jlVo.getZYH();
                String bqdm = jlVo.getBRBQ();
                if (bqdm == null || bqdm.equals("")) {//没有病区则获取病区
                    keepOrRoutingDateSource(DataSource.HRP);
                    SickPersonDetailVo patient = patientService.getPatientDetail(zyh, jgid);
                    bqdm = patient.BRBQ;
                }
                Form form = CreateEvaluation(zyh, jlxh, jlVo.getYSXH(), bqdm, txsj, jgid, dzlx);

                form.ID = String.valueOf(jlVo.getJLXH());
                form.TXGH = jlVo.getTXGH();
                form.TXSJ = jlVo.getTXSJ();
                form.Score = jlVo.getJGDJ();
                form.QMGH = jlVo.getQMGH();
                form.SYZT = jlVo.getSYZT();
                EvaluateFormItem evaluateFormItem = new EvaluateFormItem();
                evaluateFormItem.form = SetItemNode(form, xmList, xxList);
                evaluateFormItem.BTX = evaluateFormItem.form.checkForm;
                bizResponse.data = evaluateFormItem;
                bizResponse.isSuccess = true;
                return bizResponse;
            }
            bizResponse.isSuccess = false;
            bizResponse.message = "没有记录";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

    /**
     * 填写表单记录
     * 先循环项目 寻找控件填入
     *
     * @param form
     * @param xmList
     * @param xxList
     * @return
     */
    private Form SetItemNode(Form form, List<EvaluateBDJLXMVo> xmList, List<EvaluateBDJLXXVo> xxList) {
        for (EvaluateBDJLXMVo xmjl : xmList) {
            for (Classification fl : form.clazzs) {//循环分类
                for (ItemNode xm : fl.itemNodes) {//循环项目
                    if (xm.inputs != null && xm.inputs.size() > 0) {
                        for (Input input : xm.inputs) {
                            if (xxList != null && xxList.size() > 0) {
                                for (EvaluateBDJLXXVo xxVo : xxList) {
                                    if (xxVo.getJLXM().equals(String.valueOf(xmjl.getJLXM())) &&
                                            (xxVo.getYSXX().equals("0") ? String.valueOf(xm.ID).equals(xmjl.getYSXM()) : String.valueOf(input.ID).equals(xxVo.getYSXX()))) {
                                        input = (Input) setItemValue(input, String.valueOf(xmjl.getJLXM()), xxVo.getXXNR(), null);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (xm.numbers != null && xm.numbers.size() > 0) {
                        for (Numeric numeric : xm.numbers) {
                            if (xxList != null && xxList.size() > 0) {
                                for (EvaluateBDJLXXVo xxVo : xxList) {
                                    if (xxVo.getJLXM().equals(String.valueOf(xmjl.getJLXM())) &&
                                            (xxVo.getYSXX().equals("0") ? String.valueOf(xm.ID).equals(xmjl.getYSXM()) : String.valueOf(numeric.ID).equals(xxVo.getYSXX()))) {
                                        numeric = (Numeric) setItemValue(numeric, String.valueOf(xmjl.getJLXM()), xxVo.getXXNR(), null);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (xm.spinnerDataInfos != null && xm.spinnerDataInfos.size() > 0) {
                        if (xxList != null && xxList.size() > 0) {
                            for (SpinnerDataInfo spinnerDataInfo : xm.spinnerDataInfos) {
                                for (EvaluateBDJLXXVo xxVo : xxList) {
                                    if (xxVo.getJLXM().equals(String.valueOf(xmjl.getJLXM())) &&
                                            (xxVo.getYSXX().equals("0") ? String.valueOf(xm.ID).equals(xmjl.getYSXM()) : String.valueOf(spinnerDataInfo.ID).equals(xxVo.getYSXX()))) {
                                        spinnerDataInfo = (SpinnerDataInfo) setItemValue(spinnerDataInfo, String.valueOf(xmjl.getJLXM()), xxVo.getXXNR(), null);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    //福建协和客户化：特殊控件
                    if (xm.specSpinnerDataInfos != null && xm.specSpinnerDataInfos.size() > 0) {
                        if (xxList != null && xxList.size() > 0) {
                            for (SpecSpinnerDataInfo specSpinnerDataInfo : xm.specSpinnerDataInfos) {
                                for (EvaluateBDJLXXVo xxVo : xxList) {
                                    if (xxVo.getJLXM().equals(String.valueOf(xmjl.getJLXM())) &&
                                            (xxVo.getYSXX().equals("0") ? String.valueOf(xm.ID).equals(xmjl.getYSXM()) : String.valueOf(specSpinnerDataInfo.ID).equals(xxVo.getYSXX()))) {
                                        specSpinnerDataInfo = (SpecSpinnerDataInfo) setItemValue(specSpinnerDataInfo, String.valueOf(xmjl.getJLXM()), xxVo.getXXNR(), null);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (xm.datetimes != null && xm.datetimes.size() > 0) {
                        if (xxList != null && xxList.size() > 0) {
                            for (DateTime dateTime : xm.datetimes) {
                                for (EvaluateBDJLXXVo xxVo : xxList) {
                                    if (xxVo.getJLXM().equals(String.valueOf(xmjl.getJLXM())) &&
                                            (xxVo.getYSXX().equals("0") ? String.valueOf(xm.ID).equals(xmjl.getYSXM()) : String.valueOf(dateTime.ID).equals(xxVo.getYSXX()))) {
                                        dateTime = (DateTime) setItemValue(dateTime, String.valueOf(xmjl.getJLXM()), xxVo.getXXNR(), null);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (xm.cbs != null && xm.cbs.size() > 0) {
                        if (xxList != null && xxList.size() > 0) {
                            for (CheckBox checkBox : xm.cbs) {
                                for (EvaluateBDJLXXVo xxVo : xxList) {
                                    if (xxVo.getJLXM().equals(String.valueOf(xmjl.getJLXM())) && String.valueOf(checkBox.ID).equals(xxVo.getYSXX())) {
                                        checkBox = (CheckBox) setItemValue(checkBox, String.valueOf(xmjl.getJLXM()), xxVo.getXXNR(), xxList);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (xm.rbs != null && xm.rbs.size() > 0) {
                        if (xxList != null && xxList.size() > 0) {
                            for (RadioBox radioBox : xm.rbs) {
                                for (EvaluateBDJLXXVo xxVo : xxList) {
                                    if (xxVo.getJLXM().equals(String.valueOf(xmjl.getJLXM())) && String.valueOf(radioBox.ID).equals(xxVo.getYSXX())) {
                                        radioBox = (RadioBox) setItemValue(radioBox, String.valueOf(xmjl.getJLXM()), xxVo.getXXNR(), xxList);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return form;
    }

    /**
     * 填入值，如项目为Radiobox 或者CheckBox时 循环控件寻找对应选项填入
     */
    private Object setItemValue(Object control, String xmid, String value, List<EvaluateBDJLXXVo> xxList) {
        String name = control.getClass().getSimpleName();
        switch (name) {
            case "Numeric":
                Numeric numeric = (Numeric) control;
                numeric.Value = value;
                return numeric;
            case "Input":
                Input input = (Input) control;
                input.Value = value;
                return input;
            case "DateTime":
                DateTime dateTime = (DateTime) control;
                dateTime.Value = value;
                return dateTime;
            case "SpinnerDataInfo":
                SpinnerDataInfo spinnerDataInfo = (SpinnerDataInfo) control;
                spinnerDataInfo.Value = value;
                return spinnerDataInfo;
            case "SpecSpinnerDataInfo":
                SpecSpinnerDataInfo specSpinnerDataInfo = (SpecSpinnerDataInfo) control;
                specSpinnerDataInfo.Value = value;
                return specSpinnerDataInfo;
            case "RadioBox"://单选和多选可能有下级
                RadioBox radioBox = (RadioBox) control;
                radioBox.IsSelected = 1;
                if (radioBox.inputs != null && radioBox.inputs.size() > 0) {
                    for (Input inputControl : radioBox.inputs) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(inputControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    inputControl = (Input) setItemValue(inputControl, xxjl.getJLXM(), xxjl.getXXNR(), null);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (radioBox.datetimes != null && radioBox.datetimes.size() > 0) {
                    for (DateTime datControl : radioBox.datetimes) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(datControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    datControl = (DateTime) setItemValue(datControl, xxjl.getJLXM(), xxjl.getXXNR(), null);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (radioBox.numbers != null && radioBox.numbers.size() > 0) {
                    for (Numeric numControl : radioBox.numbers) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(numControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    numControl = (Numeric) setItemValue(numControl, xxjl.getJLXM(), xxjl.getXXNR(), null);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (radioBox.rbs != null && radioBox.rbs.size() > 0) {
                    for (RadioBox rbControl : radioBox.rbs) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(rbControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    rbControl = (RadioBox) setItemValue(rbControl, xxjl.getJLXM(), xxjl.getXXNR(), xxList);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (radioBox.cbs != null && radioBox.cbs.size() > 0) {
                    for (CheckBox cbControl : radioBox.cbs) {
                        if (xxList != null && (xxList.size() > 0)) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(cbControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    cbControl = (CheckBox) setItemValue(cbControl, xxjl.getJLXM(), xxjl.getXXNR(), xxList);
                                    break;
                                }
                            }
                        }
                    }
                }
                return radioBox;
            case "CheckBox":
                CheckBox checkBox = (CheckBox) control;
                checkBox.IsSelected = 1;
                if (checkBox.inputs != null && checkBox.inputs.size() > 0) {
                    for (Input inputControl : checkBox.inputs) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(inputControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    inputControl = (Input) setItemValue(inputControl, xxjl.getJLXM(), xxjl.getXXNR(), null);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (checkBox.datetimes != null && checkBox.datetimes.size() > 0) {
                    for (DateTime datControl : checkBox.datetimes) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(datControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    datControl = (DateTime) setItemValue(datControl, xxjl.getJLXM(), xxjl.getXXNR(), null);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (checkBox.numbers != null && checkBox.numbers.size() > 0) {
                    for (Numeric numControl : checkBox.numbers) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(numControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    numControl = (Numeric) setItemValue(numControl, xxjl.getJLXM(), xxjl.getXXNR(), null);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (checkBox.rbs != null && checkBox.rbs.size() > 0) {
                    for (RadioBox rbControl : checkBox.rbs) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(rbControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    rbControl = (RadioBox) setItemValue(rbControl, xxjl.getJLXM(), xxjl.getXXNR(), xxList);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (checkBox.cbs != null && checkBox.cbs.size() > 0) {
                    for (CheckBox cbControl : checkBox.cbs) {
                        if (xxList != null && xxList.size() > 0) {
                            for (EvaluateBDJLXXVo xxjl : xxList) {
                                if (xxjl.getJLXM().equals(xmid) && String.valueOf(cbControl.ID).equals(xxjl.getYSXX())) {
                                    xxList.remove(xxjl);//删除此行以减少后续循环
                                    cbControl = (CheckBox) setItemValue(cbControl, xxjl.getJLXM(), xxjl.getXXNR(), xxList);
                                    break;
                                }
                            }
                        }
                    }
                }
                return checkBox;
            default:
                break;
        }
        return control;
    }


    /**
     * 创建整张评估表单
     */
    private Form CreateEvaluation(String zyh, String jlxh, String ysxh, String bqdm, String txsj, String jgid, String dzlx) {
        keepOrRoutingDateSource(DataSource.MOB);
        Form form = null;
        try {
            form = service.getBDYS(ysxh, jgid);
            EvaluateCheckForm checkForm = new EvaluateCheckForm();
            if (form != null) {
                //获取分类项目列表
                form.clazzs = CreateClassification(checkForm, jlxh, ysxh, bqdm, jgid, dzlx);
            }
            form.checkForm = checkForm;
            form = GetRelationData(form, zyh, txsj, jgid);

            //获取系统参数 判断表单是否能操作
            form.save.Value = systemParamService.getUserParams("1", "IENR", "IENR_PGDBTXMYXBC", jgid, DataSource.MOB).datalist.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return form;
    }

    //关联表单获取数据处理
    private Form GetRelationData(Form form, String zyh, String txsj, String jgid) {
        keepOrRoutingDateSource(DataSource.ENR);
        try {
            List<LifeSignHistoryDataItem> tzList = service.getSMTZ(zyh, jgid);
            keepOrRoutingDateSource(DataSource.MOB);
            List<DERecord> fxList = service.getFXPG(zyh, jgid);
            List<HealthGuidData> xjList = service.getJKXJ(zyh, jgid);
            for (Classification clazz : form.clazzs) {
                for (ItemNode item : clazz.itemNodes) {
                    //对所有控件进行循环操作有控件有关联表单
                    if (item.inputs != null && item.inputs.size() > 0) {
                        item.inputs = SetItemNodeDate(item.inputs, tzList, fxList, xjList, form.YSLX, txsj, item.Dzlx, item.Dzxm, item.Dzbd, item.Dzbdlx);
                    }
                    if (item.datetimes != null && item.datetimes.size() > 0) {
                        item.datetimes = SetItemNodeDate(item.datetimes, tzList, fxList, xjList, form.YSLX, txsj, item.Dzlx, item.Dzxm, item.Dzbd, item.Dzbdlx);
                    }
                    if (item.spinnerDataInfos != null && item.spinnerDataInfos.size() > 0) {
                        item.spinnerDataInfos = SetItemNodeDate(item.spinnerDataInfos, tzList, fxList, xjList, form.YSLX, txsj, item.Dzlx, item.Dzxm, item.Dzbd, item.Dzbdlx);
                    }
                    if (item.numbers != null && item.numbers.size() > 0) {
                        item.numbers = SetItemNodeDate(item.numbers, tzList, fxList, xjList, form.YSLX, txsj, item.Dzlx, item.Dzxm, item.Dzbd, item.Dzbdlx);
                    }
                    if (item.cbs != null && item.cbs.size() > 0) {
                        item.cbs = SetItemNodeDate(item.cbs, tzList, fxList, xjList, form.YSLX, txsj, item.Dzlx, item.Dzxm, item.Dzbd, item.Dzbdlx);
                    }
                    if (item.rbs != null && item.rbs.size() > 0) {
                        item.rbs = SetItemNodeDate(item.rbs, tzList, fxList, xjList, form.YSLX, txsj, item.Dzlx, item.Dzxm, item.Dzbd, item.Dzbdlx);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return form;
    }

    public List SetItemNodeDate(List list, List<LifeSignHistoryDataItem> tzList, List<DERecord> fxList, List<HealthGuidData> xjList,
                                String yslx, String txsj, String sj_dzlx, String sj_dzxm, String dzbd, String dzbdlx) {
        String name = list.get(0).getClass().getSimpleName();
        String dzlx = sj_dzlx;
        String dzxm = sj_dzxm;
        switch (name) {
            case "Numeric":
                for (Numeric numeric : (List<Numeric>) list) {
                    //add by louis 适应2种情况
                    //如 体温 dzlx 打在父控件上
                    //如 既往史 dzlx 打在自身控件上

                    dzlx = (numeric.Dzlx != null ? numeric.Dzlx : "");
                    dzxm = (numeric.Dzxm != null ? numeric.Dzxm : "");

                    if ("5".equals(dzlx) && !StringUtils.isBlank(dzxm)) {
                        LifeSignHistoryDataItem tzData = GetTZDataRow(yslx, txsj, tzList, dzxm);
                        if (tzData != null) {
                            numeric.Value = tzData.TZNR;
                            //add by louis
                            numeric.dzbdjl_my = String.valueOf(tzData.CJH);
                        }

                    } else if ("2".equals(dzlx)) { //风险
                        if(dzbd == null) dzbd = numeric.Dzbd;
                        if(dzbdlx == null) dzbdlx = numeric.Dzbdlx;

                        DERecord fxData = GetFXDataRow(yslx, txsj, fxList, dzbd, dzbdlx);
                        if (fxData != null) {
                            numeric.Value = fxData.PGZF;
                            //add by louis
                            numeric.dzbdjl_my = fxData.PGXH;
                        }
                    } else if ("3".equals(dzlx)) { //宣教
                        if (!(dzbd == null || dzbd.equals("") || dzbdlx == null || dzbdlx.equals("") || dzxm == null || dzxm.equals(""))) {
                            HealthGuidData xjData = GetXJDataRow(yslx, txsj, xjList, dzbd, dzbdlx, dzxm);
                            if (xjData != null) {
                                numeric.Value = xjData.XMNR;
                                //add by louis
                               /* todo  2017-6-6 11:20:28
                                numeric.dzbdjl_my=String.valueOf(xjData.XH);
                                */
                            }
                        }
                    }
                }
                break;
            case "Input":
                for (Input input : (List<Input>) list) {
                    //add by louis 适应2种情况
                    //如 体温 dzlx 打在父控件上
                    //如 既往史 dzlx 打在自身控件上

                    dzlx = (input.Dzlx != null ? input.Dzlx : "");
                    dzxm = (input.Dzxm != null ? input.Dzxm : "");

                    if ("5".equals(dzlx) && !StringUtils.isBlank(dzxm)) {
                        LifeSignHistoryDataItem tzData = GetTZDataRow(yslx, txsj, tzList, dzxm);
                        if (tzData != null) {
                            input.Value = tzData.TZNR;
                            //add by louis
                            input.dzbdjl_my = String.valueOf(tzData.CJH);
                        }

                    } else if ("2".equals(dzlx)) { //风险
                        if(dzbd == null) dzbd = input.Dzbd;
                        if(dzbdlx == null) dzbdlx = input.Dzbdlx;

                        DERecord fxData = GetFXDataRow(yslx, txsj, fxList, dzbd, dzbdlx);
                        if (fxData != null) {
                            input.Value = fxData.PGZF;
                            //add by louis
                            input.dzbdjl_my = fxData.PGXH;
                        }
                    } else if ("3".equals(dzlx)) { //宣教
                        if (!(dzbd == null || dzbd.equals("") || dzbdlx == null || dzbdlx.equals("") || dzxm == null || dzxm.equals(""))) {
                            HealthGuidData xjData = GetXJDataRow(yslx, txsj, xjList, dzbd, dzbdlx, dzxm);
                            if (xjData != null) {
                                input.Value = xjData.XMNR;
                                //add by louis
                               /* todo  2017-6-6 11:20:28
                                numeric.dzbdjl_my=String.valueOf(xjData.XH);
                                */
                            }
                        }
                    }
                }
                break;
            case "DateTime":
                for (DateTime dateTime : (List<DateTime>) list) {
                    if (dateTime.Dzlx == null) {
                        break;
                    }
                    if (dateTime.Dzlx.equals("5") && !(dateTime.Dzxm == null || dateTime.Dzxm.equals(""))) {
                        LifeSignHistoryDataItem tzData = GetTZDataRow(yslx, txsj, tzList, dateTime.Dzxm);
                        if (tzData != null) {
                            dateTime.Value = tzData.TZNR;
                        }

                    } else if (dateTime.Dzlx.equals("2")) { //风险
                        DERecord fxData = GetFXDataRow(yslx, txsj, fxList, dateTime.Dzbd, dateTime.Dzbdlx);
                        if (fxData != null) {
                            dateTime.Value = fxData.PGZF;
                        }
                    } else if (dateTime.Dzlx.equals("3")) { //宣教
                        if (!(dateTime.Dzbd == null || dateTime.Dzbd.equals("") || dateTime.Dzbdlx == null || dateTime.Dzbdlx.equals("") || dateTime.Dzxm == null || dateTime.Dzxm.equals(""))) {
                            HealthGuidData xjData = GetXJDataRow(yslx, txsj, xjList, dateTime.Dzbd, dateTime.Dzbdlx, dateTime.Dzxm);
                            if (xjData != null) {
                                dateTime.Value = xjData.XMNR;
                            }
                        }
                    }
                }
                break;
            case "SpinnerDataInfo":
                for (SpinnerDataInfo spinnerDataInfo : (List<SpinnerDataInfo>) list) {
                    if (spinnerDataInfo.Dzlx == null) {
                        break;
                    }
                    if (spinnerDataInfo.Dzlx.equals("5") && !(spinnerDataInfo.Dzxm == null || spinnerDataInfo.Dzxm.equals(""))) {
                        LifeSignHistoryDataItem tzData = GetTZDataRow(yslx, txsj, tzList, spinnerDataInfo.Dzxm);
                        if (tzData != null) {
                            spinnerDataInfo.Value = tzData.TZNR;
                        }

                    } else if (spinnerDataInfo.Dzlx.equals("2")) { //风险
                        DERecord fxData = GetFXDataRow(yslx, txsj, fxList, spinnerDataInfo.Dzbd, spinnerDataInfo.Dzbdlx);
                        if (fxData != null) {
                            spinnerDataInfo.Value = fxData.PGZF;
                        }
                    } else if (spinnerDataInfo.Dzlx.equals("3")) { //宣教
                        if (!(spinnerDataInfo.Dzbd == null || spinnerDataInfo.Dzbd.equals("") || spinnerDataInfo.Dzbdlx == null || spinnerDataInfo.Dzbdlx.equals("") || spinnerDataInfo.Dzxm == null || spinnerDataInfo.Dzxm.equals(""))) {
                            HealthGuidData xjData = GetXJDataRow(yslx, txsj, xjList, spinnerDataInfo.Dzbd, spinnerDataInfo.Dzbdlx, spinnerDataInfo.Dzxm);
                            if (xjData != null) {
                                spinnerDataInfo.Value = xjData.XMNR;
                            }
                        }
                    }
                }
                break;
            case "RadioBox":
                for (RadioBox radioBox : (List<RadioBox>) list) {
                   /* if (radioBox.Dzlx == null) {
                        break;
                    }*/
//                    if (radioBox.Dzlx.equals("5") && !(radioBox.Dzxm == null || radioBox.Dzxm.equals(""))) {
                    if ("5".equals(radioBox.Dzlx) && !StringUtils.isBlank(radioBox.Dzxm)) {
                        LifeSignHistoryDataItem tzData = GetTZDataRow(yslx, txsj, tzList, radioBox.Dzxm);
                        if (tzData != null) {
                            radioBox.IsSelected = 1;
                        }

                    } else if ("2".equals(radioBox.Dzlx)) { //风险
                        DERecord fxData = GetFXDataRow(yslx, txsj, fxList, radioBox.Dzbd, radioBox.Dzbdlx);
                        if (fxData != null) {
                            radioBox.IsSelected = 1;
                        }
                    } else if ("3".equals(radioBox.Dzlx)) { //宣教
                        if (!(radioBox.Dzbd == null || radioBox.Dzbd.equals("") || radioBox.Dzbdlx == null || radioBox.Dzbdlx.equals("") || radioBox.Dzxm == null || radioBox.Dzxm.equals(""))) {
                            HealthGuidData xjData = GetXJDataRow(yslx, txsj, xjList, radioBox.Dzbd, radioBox.Dzbdlx, radioBox.Dzxm);
                            if (xjData != null) {
                                radioBox.IsSelected = 1;
                            }
                        }
                    } else { //Radiobox 和CheckBox 可能有下级控件
                        boolean hasChild = false;
                        if (radioBox.inputs != null && radioBox.inputs.size() > 0) {
                            radioBox.inputs = SetItemNodeDate(radioBox.inputs, tzList, fxList, xjList, yslx, txsj, radioBox.Dzlx, radioBox.Dzxm, radioBox.Dzbd, radioBox.Dzbdlx);
                            for (Input input : radioBox.inputs) {
                                if (!StringUtils.isBlank(input.Value)){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (radioBox.rbs != null && radioBox.rbs.size() > 0) {
                            radioBox.rbs = SetItemNodeDate(radioBox.rbs, tzList, fxList, xjList, yslx, txsj, radioBox.Dzlx, radioBox.Dzxm, radioBox.Dzbd, radioBox.Dzbdlx);
                            for (RadioBox radioBoxChild : radioBox.rbs) {
                                if (radioBoxChild.IsSelected==1){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (radioBox.cbs != null && radioBox.cbs.size() > 0) {
                            radioBox.cbs = SetItemNodeDate(radioBox.cbs, tzList, fxList, xjList, yslx, txsj, radioBox.Dzlx, radioBox.Dzxm, radioBox.Dzbd, radioBox.Dzbdlx);
                            for (CheckBox checkBox : radioBox.cbs) {
                                if (checkBox.IsSelected==1){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (radioBox.numbers != null && radioBox.numbers.size() > 0) {
                            radioBox.numbers = SetItemNodeDate(radioBox.numbers, tzList, fxList, xjList, yslx, txsj, radioBox.Dzlx, radioBox.Dzxm, radioBox.Dzbd, radioBox.Dzbdlx);
                            for (Numeric numeric : radioBox.numbers) {
                                if (!StringUtils.isBlank(numeric.Value)){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (radioBox.datetimes != null && radioBox.datetimes.size() > 0) {
                            radioBox.datetimes = SetItemNodeDate(radioBox.datetimes, tzList, fxList, xjList, yslx, txsj, radioBox.Dzlx, radioBox.Dzxm, radioBox.Dzbd, radioBox.Dzbdlx);
                            for (DateTime dateTime : radioBox.datetimes) {
                                if (!StringUtils.isBlank(dateTime.Value)){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (hasChild) {
                            radioBox.IsSelected = 1;
                        }
                    }
                }
                break;
            case "CheckBox":
                for (CheckBox checkBox : (List<CheckBox>) list) {
                    if (checkBox.Dzlx == null) {
                        break;
                    }
//                    if (checkBox.Dzlx.equals("5") && !(checkBox.Dzxm == null || checkBox.Dzxm.equals(""))) {
                    if ("5".equals(checkBox.Dzlx) && !StringUtils.isBlank(checkBox.Dzxm)) {
                        LifeSignHistoryDataItem tzData = GetTZDataRow(yslx, txsj, tzList, checkBox.Dzxm);
                        if (tzData != null) {
                            checkBox.IsSelected = 1;
                        }

                    } else if ("2".equals(checkBox.Dzlx)) { //风险
                        DERecord fxData = GetFXDataRow(yslx, txsj, fxList, checkBox.Dzbd, checkBox.Dzbdlx);
                        if (fxData != null) {
                            checkBox.IsSelected = 1;
                        }
                    } else if ("3".equals(checkBox.Dzlx)) { //宣教
                        if (!(checkBox.Dzbd == null || checkBox.Dzbd.equals("") || checkBox.Dzbdlx == null || checkBox.Dzbdlx.equals("") || checkBox.Dzxm == null || checkBox.Dzxm.equals(""))) {
                            HealthGuidData xjData = GetXJDataRow(yslx, txsj, xjList, checkBox.Dzbd, checkBox.Dzbdlx, checkBox.Dzxm);
                            if (xjData != null) {
                                checkBox.IsSelected = 1;
                            }
                        }
                    } else { //Radiobox 和CheckBox 可能有下级控件
                        boolean hasChild = false;
                        if (checkBox.inputs != null && checkBox.inputs.size() > 0) {
                            checkBox.inputs = SetItemNodeDate(checkBox.inputs, tzList, fxList, xjList, yslx, txsj, checkBox.Dzlx, checkBox.Dzxm, checkBox.Dzbd, checkBox.Dzbdlx);
                            for (Input input : checkBox.inputs) {
                                if (!StringUtils.isBlank(input.Value)){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (checkBox.rbs != null && checkBox.rbs.size() > 0) {
                            checkBox.rbs = SetItemNodeDate(checkBox.rbs, tzList, fxList, xjList, yslx, txsj, checkBox.Dzlx, checkBox.Dzxm, checkBox.Dzbd, checkBox.Dzbdlx);
                            for (RadioBox radioBox : checkBox.rbs) {
                                if (radioBox.IsSelected==1){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (checkBox.cbs != null && checkBox.cbs.size() > 0) {
                            checkBox.cbs = SetItemNodeDate(checkBox.cbs, tzList, fxList, xjList, yslx, txsj, checkBox.Dzlx, checkBox.Dzxm, checkBox.Dzbd, checkBox.Dzbdlx);
                            for (CheckBox checkBoxChild : checkBox.cbs) {
                                if (checkBoxChild.IsSelected==1){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (checkBox.numbers != null && checkBox.numbers.size() > 0) {
                            checkBox.numbers = SetItemNodeDate(checkBox.numbers, tzList, fxList, xjList, yslx, txsj, checkBox.Dzlx, checkBox.Dzxm, checkBox.Dzbd, checkBox.Dzbdlx);
                            for (Numeric numeric : checkBox.numbers) {
                                if (!StringUtils.isBlank(numeric.Value)){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (checkBox.datetimes != null && checkBox.datetimes.size() > 0) {
                            checkBox.datetimes = SetItemNodeDate(checkBox.datetimes, tzList, fxList, xjList, yslx, txsj, checkBox.Dzlx, checkBox.Dzxm, checkBox.Dzbd, checkBox.Dzbdlx);
                            for (DateTime dateTime : checkBox.datetimes) {
                                if (!StringUtils.isBlank(dateTime.Value)){
                                    hasChild = true;
                                    break;
                                }
                            }
                        }
                        if (hasChild) {
                            checkBox.IsSelected = 1;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return list;
    }

    private DERecord GetFXDataRow(String yslx, String txsj, List<DERecord> fxList, String dzbd, String dzbdlx) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(txsj != null && txsj.length() == 16 ) txsj += ":00";
            Date _txsj = sdf.parse(txsj);

            String hqfs = StringUtils.isEmpty(service.getSJHQFS(yslx)) ? "2" : service.getSJHQFS(yslx);//hqfs=2   默认值
            if (hqfs.equals("2")) { //评估时间最近的有效记录
                //====start 2017-4-24 17:16:24
                long minDiffTime = _txsj.getTime();
                int index = -1;
                for (int i = 0; i < fxList.size(); i++) {
                    DERecord deRecord = fxList.get(i);
                    //if (String.valueOf(deRecord.PGLX).equals(dzbd) && (_txsj.getTime() > sdf.parse(deRecord.PGSJ).getTime())) {
                    if (String.valueOf(deRecord.PGLX).equals(dzbdlx) && (_txsj.getTime() > sdf.parse(deRecord.PGSJ).getTime())) {
                        long diff = _txsj.getTime() - sdf.parse(deRecord.PGSJ).getTime();
                        //if (diff <= minDiffTime) {
                        if (diff < minDiffTime) {
                            minDiffTime = diff;
                            index = i;
                        }
                    }
                }
                if (index > -1) {
                    return fxList.get(index);
                }
                //====start 2017-4-24 17:16:24
            }
            /*if (yslx.equals("1")) {
                for (DERecord deRecord : fxList) {
                    if (deRecord.PGDH.equals(dzbd) && deRecord.PGLX.equals(dzbdlx)) {
                        return deRecord;
                    }
                }
            }*/
            else if (hqfs.equals("1")) {//第一次操作的有效记录
               /* DERecord deRecord = new DERecord();
                Iterator ite = fxList.iterator();
                while (ite.hasNext()) {
                    deRecord = (DERecord) ite.next();
                *//*    if (!(deRecord.PGDH.equals(dzbd) || deRecord.PGLX.equals(dzbdlx))) {
                        ite.remove();
                    }*//*
                //change   2017年4月27日10:34:49
                    if (!deRecord.PGLX.equals(dzbd)) {
                        ite.remove();
                    }
                }
                deRecord = null;
                if (fxList.size() > 0) {
                    //时间差
                    Long ts = sdf.parse(fxList.get(0).PGSJ).getTime() - _txsj.getTime();
                    deRecord = fxList.get(0);
                    for (DERecord hel : fxList) {
                        Long temps = sdf.parse(hel.PGSJ).getTime() - _txsj.getTime();
                        if (ts < temps) {
                            ts = temps;
                            deRecord = hel;
                        }
                    }
                }
                return deRecord;*/
                long maxDiffTime = 0;
                int index = -1;
                for (int i = 0; i < fxList.size(); i++) {
                    DERecord deRecord = fxList.get(i);
                    //if (String.valueOf(deRecord.PGLX).equals(dzbd) && (_txsj.getTime() > sdf.parse(deRecord.PGSJ).getTime())) {
                    if (String.valueOf(deRecord.PGLX).equals(dzbdlx) && (_txsj.getTime() > sdf.parse(deRecord.PGSJ).getTime())) {
                        long diff = _txsj.getTime() - sdf.parse(deRecord.PGSJ).getTime();
                        //if (diff >= maxDiffTime) {
                        if (diff > maxDiffTime) {
                            maxDiffTime = diff;
                            index = i;
                        }
                    }
                }
                if (index > -1) {
                    return fxList.get(index);
                }
            } else if (hqfs.equals("3")) {//当前创建的记录
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HealthGuidData GetXJDataRow(String yslx, String txsj, List<HealthGuidData> xjList, String dzbd, String dzbdlx, String dzxm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date _txsj = sdf.parse(txsj);
            if (yslx.equals("1")) {
                for (HealthGuidData healthdata : xjList) {
                    if (healthdata.XH.equals(dzxm) && healthdata.GLLX.equals(dzbdlx)
                            && healthdata.GLXH.equals(dzbd) && (_txsj.getTime() > sdf.parse(healthdata.JLSJ).getTime())) {
                        return healthdata;
                    }
                }
            } else if (yslx.equals("11") || yslx.equals("12") || yslx.equals("12")
                    || yslx.equals("13") || yslx.equals("14") || yslx.equals("14")) {
                HealthGuidData healthdata;
                Iterator ite = xjList.iterator();
                while (ite.hasNext()) {
                    healthdata = (HealthGuidData) ite.next();
                    if (!healthdata.XH.equals(dzxm) || !healthdata.GLLX.equals(dzbdlx)
                            || healthdata.GLXH.equals(dzbd) || (_txsj.getTime() > sdf.parse(healthdata.JLSJ).getTime())) {
                        ite.remove();
                    }
                }
                healthdata = null;
                if (xjList.size() > 0) {
                    //时间差
                    Long ts = sdf.parse(xjList.get(0).JLSJ).getTime() - _txsj.getTime();
                    healthdata = xjList.get(0);
                    for (HealthGuidData hel : xjList) {
                        Long temps = sdf.parse(hel.JLSJ).getTime() - _txsj.getTime();
                        if (ts < temps) {
                            ts = temps;
                            healthdata = hel;
                        }
                    }
                }
                return healthdata;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private LifeSignHistoryDataItem GetTZDataRow(String yslx, String txsj, List<LifeSignHistoryDataItem> tzList, String dzxm) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            //如果txsj很早 那么可能会出现如下情况：最近的有效记录 不是当前填写的
            Date _txsj = sdf.parse(txsj);
            String hqfs = StringUtils.isEmpty(service.getSJHQFS(yslx)) ? "2" : service.getSJHQFS(yslx);//hqfs=2   默认值
            if (hqfs.equals("2")) { //评估时间最近的有效记录
                //====start 2017-4-24 17:16:24
                long minDiffTime = _txsj.getTime();
                int index = -1;
                for (int i = 0; i < tzList.size(); i++) {
                    LifeSignHistoryDataItem lifeData = tzList.get(i);
                    if (String.valueOf(lifeData.XMH).equals(dzxm) && (_txsj.getTime() > sdf.parse(lifeData.CJSJ).getTime())) {
                        long diff = _txsj.getTime() - sdf.parse(lifeData.CJSJ).getTime();
                        if (diff <= minDiffTime) {
                            minDiffTime = diff;
                            index = i;
                        }
                    }
                }
                if (index > -1) {
                    return tzList.get(index);
                }
                //return null;//TODO 2017年4月25日16:53:18  test
             /*   for (LifeSignHistoryDataItem lifeData : tzList) {
                    if (String.valueOf(lifeData.XMH).equals(dzxm) && (_txsj.getTime() > sdf.parse(lifeData.CJSJ).getTime())) {
                        return lifeData;
                    }
                }*/
                //====end
            } else if (hqfs.equals("1")) { //第一次操作的有效记录
              /*  LifeSignHistoryDataItem lifeData;
                Iterator ite = tzList.iterator();
                while (ite.hasNext()) {
                    lifeData = (LifeSignHistoryDataItem) ite.next();
                    if (!(String.valueOf(lifeData.XMH).equals(dzxm) || (_txsj.getTime() > sdf.parse(lifeData.CJSJ).getTime()))) {
                        ite.remove();
                    }
                }
                lifeData = null;
                if (tzList.size() > 0) {
                    //时间差
                    Long ts = sdf.parse(tzList.get(0).CJSJ).getTime() - _txsj.getTime();
                    lifeData = tzList.get(0);
                    for (LifeSignHistoryDataItem hel : tzList) {
                        Long temps = sdf.parse(hel.CJSJ).getTime() - _txsj.getTime();
                        if (ts < temps) {
                            ts = temps;
                            lifeData = hel;
                        }
                    }
                }
                  return lifeData;
                */
                long maxDiffTime = 0;
                int index = -1;
                for (int i = 0; i < tzList.size(); i++) {
                    LifeSignHistoryDataItem lifeData = tzList.get(i);
                    if (String.valueOf(lifeData.XMH).equals(dzxm) && (_txsj.getTime() > sdf.parse(lifeData.CJSJ).getTime())) {
                        long diff = _txsj.getTime() - sdf.parse(lifeData.CJSJ).getTime();
                        if (diff >= maxDiffTime) {
                            maxDiffTime = diff;
                            index = i;
                        }
                    }
                }
                if (index > -1) {
                    return tzList.get(index);
                }


            } else if (hqfs.equals("3")) {// 当前创建的记录
                return null;
               /* for (LifeSignHistoryDataItem lifeData : tzList
                        ) {
                    if (String.valueOf(lifeData.XMH).equals(dzxm) && (_txsj.getTime() > sdf.parse(lifeData.CJSJ).getTime())) {
                        if (lifeData.CJH == cjh) {
                            return lifeData;
                        }
                    }
                }*/
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建项目分类
     */
    private List<Classification> CreateClassification(EvaluateCheckForm checkForm, String jlxh, String ysxh, String bqdm, String jgid, String dzlx) {
        List<Classification> list_fl = new ArrayList<>();
        List<CheckCls> clsList = new ArrayList<>();
        try {
            list_fl = service.getYSFL(ysxh, jlxh);
            List<EvaluateBDYSXMVo> list_flxm = service.getFLXM(ysxh, dzlx);
            List<EvaluateBDYSXXVo> list_flxx = service.getFLXX(ysxh, dzlx);

            //填写必填标志
            list_flxm = GetNewDataTable(list_flxm, bqdm, jgid);
            for (Classification flVo : list_fl) {
                CheckCls cls = new CheckCls();
                cls.FLID = flVo.ID;
                cls.ITEMS = createCheckItems(String.valueOf(flVo.ID), list_flxm);
                clsList.add(cls);
                flVo.itemNodes = CreateNodeItem(list_flxm, list_flxx, String.valueOf(flVo.ID));
            }
            checkForm.CLS = clsList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list_fl;
    }


    //必填项目列表增加
    private List<CheckItem> createCheckItems(String ysfl, List<EvaluateBDYSXMVo> list_flxm) {
        List<CheckItem> list = new ArrayList<>();
        for (EvaluateBDYSXMVo evaluateBDYSXMVo : list_flxm) {
            if (evaluateBDYSXMVo.getYSFL().equals(ysfl) && evaluateBDYSXMVo.getBTBZ().equals("1")) {
                CheckItem checkItem = new CheckItem();
                checkItem.XMID = evaluateBDYSXMVo.getYSXM().intValue();
                checkItem.XMMC = evaluateBDYSXMVo.getXMMC();
                checkItem.TXBZ = "0";
                list.add(checkItem);
            }
        }
        return list;
    }

    /**
     * 创建项目
     */
    private List<ItemNode> CreateNodeItem(List<EvaluateBDYSXMVo> list_flxm, List<EvaluateBDYSXXVo> list_flxx, String flxh) {
        String groupid = "0";
        List<ItemNode> list = new ArrayList<>();
        for (EvaluateBDYSXMVo xmVo : list_flxm) {
            if (xmVo.getYSFL().equals(flxh)) {
                ItemNode itemNode = new ItemNode();
                if (xmVo.getDZLX() != null) {
                    itemNode.Dzlx = xmVo.getDZLX();
                } else {
                    itemNode.Dzlx = "-1";
                }
                itemNode.Dzxm = xmVo.getDZXM();
                itemNode.Dzbd = xmVo.getDZBD();
                itemNode.Dzbdlx = xmVo.getDZBDLX();
                itemNode.Btbz = xmVo.getBTBZ();
                itemNode.ID = xmVo.getYSXM().intValue();
                itemNode.NText = xmVo.getXMMC();

                String ysxm = String.valueOf(xmVo.getYSXM());
                //输入类型  1,手工输入 2,单项选择 3,多项选择,4.下拉选择
                //福建协和客户化：5 特殊控件
                String srlx = xmVo.getSRLX();
                String sjlx = xmVo.getSJLX();//数据类型  1 数字，2 字符，3日期
                if (srlx.equals("1")) {
                    String qzwb = xmVo.getQZWB();//前置文本
                    String hzwb = xmVo.getHZWB();//后置文本
                    Boolean isscore;
                    Float xmdj = Float.parseFloat("0");
                    if (xmVo.getXMDJ() == null || xmVo.getXMDJ().equals("")) {
                        isscore = false;
                    } else {
                        try {
                            xmdj = Float.parseFloat(xmVo.getXMDJ());
                            isscore = true;
                        } catch (NumberFormatException e) {
                            isscore = false;
                        }
                    }
                    Integer id = Integer.parseInt(ysxm);
                    String frontId = ysxm + "01";
                    String postpositionId = ysxm + "02";
                    switch (sjlx) {//输入框更具数据类型确定前端输入控件类型
                        case "1":
                            List<Numeric> numericList = new ArrayList<>();
                            String groupid_new = ysxm;//add 作为客户端view的id为0的时候  使用ysxm 所以传入
                            Numeric numeric = CreateNumberBox(itemNode, 0, frontId, postpositionId, xmVo.getSJSX(), xmVo.getSJXX(), isscore, String.valueOf(xmdj), groupid_new);
                            numericList.add(numeric);
                            itemNode.numbers = numericList;
                            itemNode.childViewModelLists.add(new ChildViewModel(ChildViewModel.Numeric, numeric));
                            break;
                        case "2":
                            List<Input> inputList = new ArrayList<>();
                            String groupid_new2 = ysxm;//add 作为客户端view的id为0的时候  使用ysxm 所以传入
                            Input input = createInput(itemNode, 0, frontId, postpositionId, isscore, String.valueOf(xmdj), groupid_new2);
                            inputList.add(input);
                            itemNode.inputs = inputList;
                            itemNode.childViewModelLists.add(new ChildViewModel(ChildViewModel.Input, input));
                            break;
                        case "3":
                            List<DateTime> dateTimeList = new ArrayList<>();
                            DateTime dateTime = createDateTimeBox(0, frontId, postpositionId, isscore, String.valueOf(xmdj), groupid);
                            //福建协和客户化：时间格式
                            dateTime.SJGS = xmVo.getSJGS();
                            dateTimeList.add(dateTime);
                            itemNode.datetimes = dateTimeList;
                            itemNode.childViewModelLists.add(new ChildViewModel(ChildViewModel.DateTime, dateTime));
                            break;
                        default:
                            break;
                    }
                    List<Label> labelList = new ArrayList<>();
                    if (qzwb != null && !qzwb.equals("")) { //不为空添加前置文本的显示控件
                        id = Integer.parseInt(ysxm + "01");
                        Label label = CreateLeable(id, qzwb, groupid);
                        labelList.add(label);
                        itemNode.childViewModelLists.add(new ChildViewModel(ChildViewModel.Label, label));
                    }
                    if (hzwb != null && !hzwb.equals("")) {//不为空添加后置文本的显示控件
                        id = Integer.parseInt(ysxm + "02");
                        Label label = CreateLeable(id, hzwb, groupid);
                        labelList.add(label);
                        itemNode.childViewModelLists.add(new ChildViewModel(ChildViewModel.Label, label));
                    }
                    if (labelList.size() > 0) {
                        itemNode.labels = labelList;
                    }
                } else if (srlx.equals("4")) {
                    List<SpinnerDataInfo> spinnerDataInfoList = new ArrayList<>();
                    SpinnerDataInfo spinnerDataInfo = CreateSpinner(xmVo.getXLLB());
                    spinnerDataInfoList.add(spinnerDataInfo);
                    itemNode.spinnerDataInfos = spinnerDataInfoList;
                    itemNode.childViewModelLists.add(new ChildViewModel(ChildViewModel.Spinner, spinnerDataInfo));
                } else if (srlx.equals("5")) {//福建协和客户化
                    List<SpecSpinnerDataInfo> specSpinnerDataInfoList = new ArrayList<>();
                    List<EvaluateBDYSXXVo> list_xxVo = new ArrayList<>();
                    for (EvaluateBDYSXXVo xxVo : list_flxx) {
                        if (xxVo.getYSXM().equals(String.valueOf(xmVo.getYSXM()))) {
                            list_xxVo.add(xxVo);
                        }
                    }
                    SpecSpinnerDataInfo specSpinnerDataInfo = CreateSpecSpinner(list_xxVo);
                    specSpinnerDataInfoList.add(specSpinnerDataInfo);
                    itemNode.specSpinnerDataInfos = specSpinnerDataInfoList;
                    itemNode.childViewModelLists.add(new ChildViewModel(ChildViewModel.SpecSpinner, specSpinnerDataInfo));
                } else {
                    itemNode.addControlList(CreateControlList(list_flxx, String.valueOf(xmVo.getYSXM()), "0", null));
                }
                list.add(itemNode);
            }
        }
        return list;
    }

    /**
     * 创建选项 根据fjxx创建选项字典
     *
     * @return
     */
    private List CreateControlList(List<EvaluateBDYSXXVo> list_flxx, String xmxh, String fjxx, String gpid) {
        List list = new ArrayList<>();
        Integer igroupid = 0;
        try {
            Iterator ite = list_flxx.iterator();
            while (ite.hasNext()) {
                EvaluateBDYSXXVo xxdata = (EvaluateBDYSXXVo) ite.next();
                if (xxdata.getYSXM().equals(xmxh) && xxdata.getFJXX().equals(fjxx)) {
                    String groupid = (gpid == null || gpid.equals("")) ? String.valueOf(++igroupid) : gpid;
                    String czlx = xxdata.getCZLX();
                    String qzwb = xxdata.getQZWB();//前置文本
                    String hzwb = xxdata.getHZWB();//后置文本
                    if (qzwb != null && !qzwb.equals("")) { //不为空添加前置文本的显示控件
                        Integer id = Integer.parseInt(xxdata.getYSXX() + "01");
                        list.add(CreateLeable(id, qzwb, groupid));
                    }
                    if (hzwb != null && !hzwb.equals("")) {//不为空添加后置文本的显示控件
                        Integer id = Integer.parseInt(xxdata.getYSXX() + "02");
                        list.add(CreateLeable(id, hzwb, groupid));
                    }
                    switch (czlx) {
                        case "1":
                            String sjlx = xxdata.getSJLX();
                            if (sjlx.equals("1")) {
                                Numeric numeric = GetNumericBox(xxdata, groupid);
                                list.add(numeric);
                            } else if (sjlx.equals("2")) {
                                Input input = GetInputBox(xxdata, groupid);
                                list.add(input);
                            } else if (sjlx.equals("3")) {
                                DateTime dateTime = GetDateTimeBox(xxdata, groupid);
                                list.add(dateTime);
                            }
                            break;
                        case "2":
                            RadioBox radioBox = GetRadioBox(list_flxx, xxdata, groupid);
                            list.add(radioBox);
                            break;
                        case "3":
                            CheckBox checkBox = GetCheckBox(list_flxx, xxdata, groupid);
                            list.add(checkBox);
                            break;
                        case "4":
                            SpinnerDataInfo spinnerDataInfo = GetSpinnerBox(xxdata, groupid);
                            list.add(spinnerDataInfo);
                            break;
                        default:
                    }

                }
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }

    /**
     * 填写BTBZ
     */
    private List<EvaluateBDYSXMVo> GetNewDataTable(List<EvaluateBDYSXMVo> list_flxm, String bqdm, String jgid) {
        try {
            for (EvaluateBDYSXMVo flxmVo : list_flxm) {
                Integer count = null;
                count = service.getBTBZ(flxmVo.getYSXH(), flxmVo.getYSXM(), bqdm, jgid);
                String btbz = count > 0 ? "0" : "1";
                flxmVo.setBTBZ(btbz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list_flxm;
    }

    /**
     * 创建显示控件
     */
    private Label CreateLeable(Integer id, String text, String groupid) {
        Label label = new Label();
        label.ID = id;
        label.Text = text;
        label.GroupId = groupid;
        return label;
    }

    /**
     * 创建数值输入框
     */
    private Numeric CreateNumberBox(ItemNode node, Integer id, String frontId, String postpositionId, String upLimit, String downLimit, Boolean isscore, String xmdj, String groupid) {
        Numeric number = new Numeric();
        number.ID = id;
        number.FrontId = frontId;
        number.PostpositionId = postpositionId;
        number.UpLimit = upLimit;
        number.DownLimit = downLimit;
        number.IsScored = isscore ? "1" : "0";
        number.Score = xmdj;
        number.GroupId = groupid;

        number.Dzlx = node.Dzlx;
        number.Dzbd = node.Dzbd;
        number.Dzxm = node.Dzxm;
        number.Dzbdlx = node.Dzbdlx;

        return number;
    }

    /**
     * 创建输入控件
     *
     * @return
     */
    private Input createInput(ItemNode node, Integer id, String frontId, String postpositionId, Boolean isscore, String xmdj, String groupid) {
        Input input = new Input();
        input.ID = id;
        input.FrontId = frontId;
        input.PostpositionId = postpositionId;
        input.IsScored = isscore ? "1" : "0";
        input.Score = xmdj;
        input.GroupId = groupid;

        input.Dzlx = node.Dzlx;
        input.Dzbd = node.Dzbd;
        input.Dzxm = node.Dzxm;
        input.Dzbdlx = node.Dzbdlx;

        return input;
    }

    /**
     * 创建时间控件
     *
     * @return
     */
    public DateTime createDateTimeBox(Integer id, String frontId, String postpositionId, Boolean isscore, String xmdj, String groupid) {
        DateTime dat = new DateTime();
        dat.ID = id;
        dat.FrontId = frontId;
        dat.PostpositionId = postpositionId;
        dat.IsScored = isscore ? "1" : "0";
        dat.Score = xmdj;
        dat.GroupId = groupid;
        return dat;
    }


    /**
     * 创建下拉控件
     *
     * @return
     */
    private SpinnerDataInfo CreateSpinner(String xllb) {
        SpinnerDataInfo spinnerDataInfo = new SpinnerDataInfo();
        if (xllb != null && !xllb.equals("")) {
            if (xllb.startsWith("@")) {
                if (xllb.substring(xllb.length() - 1).equals(";")) {
                    xllb = xllb.substring(0, xllb.length() - 1);
                }
                String[] strs = xllb.substring(1).split(";");
                for (int i = 0; i < strs.length; i++) {
                    String[] str = strs[i].split(",");
                    DropData data = new DropData();
                    data.XMID = str[1];
                    data.XMMC = str[0];
                    spinnerDataInfo.datas.add(data);
                }
            } else if (xllb.startsWith("$")) {
                String[] strs = xllb.substring(1).split(":");
                String sorceName = strs[0].substring(1);
                String sql = strs[1];
                keepOrRoutingDateSource(dataService.getDataSourceBySourceName(sorceName));
                List<SqlDropData> list = null;
                try {
                    list = service.getDropListBySql(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (SqlDropData sqlData : list) {
                    DropData data = new DropData();
                    data.XMMC = sqlData.XMMC;
                    data.XMID = sqlData.XMDM;
                    spinnerDataInfo.datas.add(data);
                }
            }
        }
        return spinnerDataInfo;
    }

    /**
     * 创建下拉控件
     * 福建协和客户化
     *
     * @return
     */
    private SpecSpinnerDataInfo CreateSpecSpinner(List<EvaluateBDYSXXVo> list) {
        SpecSpinnerDataInfo specSpinnerDataInfo = new SpecSpinnerDataInfo();
        if (list != null && list.size() > 0) {
            try {
                specSpinnerDataInfo.ID = Integer.valueOf(list.get(0).getYSXM());
            } catch (Exception e) {
                specSpinnerDataInfo.ID = -1;
            }
            for (EvaluateBDYSXXVo item : list) {
                DropData data = new DropData();
                data.XMID = String.valueOf(item.getYSXX());
                data.XMMC = item.getXXNR();
                data.QZWB = item.getQZWB();
                specSpinnerDataInfo.datas.add(data);
            }
        }
        return specSpinnerDataInfo;
    }


    public RadioBox GetRadioBox(List<EvaluateBDYSXXVo> list_flxx, EvaluateBDYSXXVo xxVo, String groupid) {
        RadioBox radioBox = new RadioBox();
        radioBox.ID = Integer.parseInt(String.valueOf(xxVo.getYSXX()));
        radioBox.Text = xxVo.getXXNR();
        radioBox.Value = xxVo.getXXNR();
        radioBox.Score = xxVo.getXXDJ();
        radioBox.GroupId = groupid;
        radioBox.Jfgz = xxVo.getJFGZ();
        radioBox.Xxdj = xxVo.getXXDJ();
        radioBox.Dzbd = xxVo.getDZBD();
        radioBox.Dzlx = xxVo.getDZLX();
        radioBox.Dzxm = xxVo.getDZXM();
        radioBox.Dzbdlx = xxVo.getDZBDLX();
        radioBox.FrontId = String.valueOf(xxVo.getYSXX()) + "01";
        radioBox.PostpositionId = String.valueOf(xxVo.getYSXX()) + "02";
        String qzwb = xxVo.getQZWB();
        String hzwb = xxVo.getHZWB();
        List list = new ArrayList();
        list = CreateControlList(list_flxx, xxVo.getYSXM(), String.valueOf(xxVo.getYSXX()), groupid);
        if (qzwb != null && !qzwb.equals("")) { //不为空添加前置文本的显示控件
            list.add(CreateLeable((Integer.parseInt(xxVo.getYSXX() + "01")), qzwb, groupid));
        }
        if (hzwb != null && !hzwb.equals("")) {//不为空添加前置文本的显示控件
            list.add(CreateLeable(Integer.parseInt(xxVo.getYSXX() + "02"), hzwb, groupid));
        }
        radioBox.addControlList(list);
        return radioBox;
    }


    public CheckBox GetCheckBox(List<EvaluateBDYSXXVo> list_flxx, EvaluateBDYSXXVo xxVo, String groupid) {
        CheckBox checkBox = new CheckBox();
        checkBox.ID = Integer.parseInt(String.valueOf(xxVo.getYSXX()));
        checkBox.Text = xxVo.getXXNR();
        checkBox.Value = xxVo.getXXNR();
        checkBox.Score = xxVo.getXXDJ();
        checkBox.GroupId = groupid;
        checkBox.Jfgz = xxVo.getJFGZ();
        checkBox.Xxdj = xxVo.getXXDJ();
        checkBox.Dzbd = xxVo.getDZBD();
        checkBox.Dzlx = xxVo.getDZLX();
        checkBox.Dzxm = xxVo.getDZXM();
        checkBox.Dzbdlx = xxVo.getDZBDLX();
        checkBox.FrontId = String.valueOf(xxVo.getYSXX()) + "01";
        checkBox.PostpositionId = String.valueOf(xxVo.getYSXX()) + "02";
        String qzwb = xxVo.getQZWB();
        String hzwb = xxVo.getHZWB();

        List list = new ArrayList();
        list = CreateControlList(list_flxx, xxVo.getYSXM(), String.valueOf(xxVo.getYSXX()), groupid);
        if (qzwb != null && !qzwb.equals("")) { //不为空添加前置文本的显示控件
            list.add(CreateLeable((Integer.parseInt(xxVo.getYSXX() + "01")), qzwb, groupid));
        }
        if (hzwb != null && !hzwb.equals("")) {//不为空添加后置文本的显示控件
            list.add(CreateLeable(Integer.parseInt(xxVo.getYSXX() + "02"), hzwb, groupid));
        }
        checkBox.addControlList(list);
        return checkBox;
    }

    public Numeric GetNumericBox(EvaluateBDYSXXVo xxVo, String groupid) {
        Numeric numeric = new Numeric();
        numeric.ID = Integer.parseInt(String.valueOf(xxVo.getYSXX()));
        numeric.UpLimit = xxVo.getSJSX();
        numeric.DownLimit = xxVo.getSJXX();
        numeric.Score = xxVo.getXXDJ();
        numeric.GroupId = groupid;
        numeric.Jfgz = xxVo.getJFGZ();
        numeric.Xxdj = xxVo.getXXDJ();
        numeric.Dzbd = xxVo.getDZBD();
        numeric.Dzlx = xxVo.getDZLX();
        numeric.Dzxm = xxVo.getDZXM();
        numeric.Dzbdlx = xxVo.getDZBDLX();
        numeric.FrontId = String.valueOf(xxVo.getYSXX()) + "01";
        numeric.PostpositionId = String.valueOf(xxVo.getYSXX()) + "02";
        return numeric;
    }

    public SpinnerDataInfo GetSpinnerBox(EvaluateBDYSXXVo xxVo, String groupid) {
        SpinnerDataInfo spinnerDataInfo = new SpinnerDataInfo();
        spinnerDataInfo.ID = Integer.parseInt(String.valueOf(xxVo.getYSXX()));
        spinnerDataInfo.Score = xxVo.getXXDJ();
        spinnerDataInfo.GroupId = groupid;
        spinnerDataInfo.Jfgz = xxVo.getJFGZ();
        spinnerDataInfo.Xxdj = xxVo.getXXDJ();
        spinnerDataInfo.Dzbd = xxVo.getDZBD();
        spinnerDataInfo.Dzlx = xxVo.getDZLX();
        spinnerDataInfo.Dzxm = xxVo.getDZXM();
        spinnerDataInfo.Dzbdlx = xxVo.getDZBDLX();
        spinnerDataInfo.FrontId = String.valueOf(xxVo.getYSXX()) + "01";
        spinnerDataInfo.PostpositionId = String.valueOf(xxVo.getYSXX()) + "02";
        return spinnerDataInfo;
    }

    public Input GetInputBox(EvaluateBDYSXXVo xxVo, String groupid) {
        Input input = new Input();
        input.ID = Integer.parseInt(String.valueOf(xxVo.getYSXX()));
        input.Score = xxVo.getXXDJ();
        input.GroupId = groupid;
        input.Jfgz = xxVo.getJFGZ();
        input.Xxdj = xxVo.getXXDJ();
        input.Dzbd = xxVo.getDZBD();
        input.Dzlx = xxVo.getDZLX();
        input.Dzxm = xxVo.getDZXM();
        input.Dzbdlx = xxVo.getDZBDLX();
        input.FrontId = String.valueOf(xxVo.getYSXX()) + "01";
        input.PostpositionId = String.valueOf(xxVo.getYSXX()) + "02";
        return input;
    }

    public DateTime GetDateTimeBox(EvaluateBDYSXXVo xxVo, String groupid) {
        DateTime dateTime = new DateTime();
        dateTime.ID = Integer.parseInt(String.valueOf(xxVo.getYSXX()));
        dateTime.Score = xxVo.getXXDJ();
        dateTime.GroupId = groupid;
        dateTime.Jfgz = xxVo.getJFGZ();
        dateTime.Xxdj = xxVo.getXXDJ();
        dateTime.Dzbd = xxVo.getDZBD();
        dateTime.Dzlx = xxVo.getDZLX();
        dateTime.Dzxm = xxVo.getDZXM();
        dateTime.Dzbdlx = xxVo.getDZBDLX();
        dateTime.FrontId = String.valueOf(xxVo.getYSXX()) + "01";
        dateTime.PostpositionId = String.valueOf(xxVo.getYSXX()) + "02";
        return dateTime;
    }

    /**
     * 获取新的评估单
     *
     * @param bqdm
     * @param zyh
     * @param ysxh
     * @param txsj
     * @param jgid
     * @return
     */

    public BizResponse<EvaluateFormItem> GetNewEvaluation(boolean isZKNotCheckBQ, boolean isNewPage, String bqdm, String zyh, String ysxh, String txsj, String jgid) {
        BizResponse<EvaluateFormItem> bizResponse = new BizResponse<>();
        EvaluateFormItem evaluateFormItem = new EvaluateFormItem();
        if (ysxh == null || ysxh.equals("")) {
            bizResponse.isSuccess = false;
            bizResponse.message = "单号为空";
            return bizResponse;
        }
        if (!isNewPage) {//不是强制新增页面
            //!
            //原本需求是默认打开的是新增页眉  但是 协和 需求 改成有记录显示最近一条记录
            BizResponse<EvaluateRecordItem> bizTemp = getAllEvaluationList(zyh, jgid);
            if (bizTemp.isSuccess && bizTemp.datalist != null && !bizTemp.datalist.isEmpty()) {
                bizResponse.isSuccess = true;
                List<EvaluateRecordItem> evaluateRecordItemList = new ArrayList<>();
                for (EvaluateRecordItem evaluateRecordItem : bizTemp.datalist) {
                    if (ysxh.equals(evaluateRecordItem.YSXH)) {
                        evaluateRecordItemList.add(evaluateRecordItem);
                    }
                }
                if (evaluateRecordItemList != null && !evaluateRecordItemList.isEmpty()) {
                    //SQL ： ORDER BY TXSJ  最近一条记录
                    String jlxh = evaluateRecordItemList.get(evaluateRecordItemList.size() - 1).JLXH;
                    if (jlxh != null && !"".equals(jlxh)) {
                        BizResponse<EvaluateFormItem> lastFormItemBizResponse =
                                GetExistEvaluation(jlxh, txsj, jgid, null);
                        if (lastFormItemBizResponse.isSuccess) {
                            bizResponse.data = lastFormItemBizResponse.data;
                            bizResponse.isSuccess = true;
                            bizResponse.message = "获取最近一条记录成功";
                            return bizResponse;
                        }
                    }
                }
            }
            //
        }
        BizResponse<String> biz = CheckEvalSingleType(isZKNotCheckBQ, bqdm, zyh, ysxh, operationTypeAdd, jgid);
        if (!biz.isSuccess) {
            //
            bizResponse.isSuccess = false;
            bizResponse.message = biz.message;
            return bizResponse;
        }
        evaluateFormItem.form = CreateEvaluation(zyh, "0", ysxh, bqdm, txsj, jgid, null);
        evaluateFormItem.BTX = evaluateFormItem.form.checkForm;
        bizResponse.data = evaluateFormItem;
        bizResponse.isSuccess = true;
        return bizResponse;
    }

    /**
     * 判断能否操作表单
     */
    private BizResponse<String> CheckEvalSingleType(boolean isZKNotCheckBQ, String bqdm, String zyh, String ysxh, Integer operationType, String jgid) {
        BizResponse<String> bizResponse = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            List<SingleCharacter> dzList = service.getSingle(bqdm, ysxh, jgid);
            Integer count = service.getBDJLCount(ysxh, zyh, jgid);
            if (dzList.size() > 0) {
                SingleCharacter singleVo = dzList.get(0);
                if (singleVo.ZFBZ.equals("1")) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "表单已作废";
                    return bizResponse;
                }
                //单一文本而且记录多于一条,并且是新增的操作
                if (singleVo.DYWD.equals("1") && count > 0 && operationType.equals(operationTypeAdd)) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "表单只能有一份";
                    return bizResponse;
                }
            } else {
                if (isZKNotCheckBQ) {
                    bizResponse.isSuccess = true;
                    return bizResponse;
                }
                bizResponse.isSuccess = false;
                bizResponse.message = "表单不是本病区";
                return bizResponse;
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        bizResponse.isSuccess = true;
        return bizResponse;
    }

    /**
     * 保存评估单
     */
    public BizResponse<EvaluateFormItem> SaveEvaluation(EvaluateSaveRespose parms) {
        BizResponse<EvaluateFormItem> bizResponse = new BizResponse<>();
        SaveForm form = parms.saveForm;//获取表首项信息
        if (!form.LYBS.equals("0")) {
            bizResponse.isSuccess = false;
            bizResponse.message = "不支持保存至EMR";
            return bizResponse;
        }
        //判断该表单样式能否操作
        BizResponse<String> biz = CheckEvalSingleType(parms.isZKNotCheckBQ, parms.bqdm, form.ZYH, form.YSXH, operationTypeModification, parms.jgid);
        if (!biz.isSuccess) {
            bizResponse.isSuccess = false;
            bizResponse.message = biz.message;
            return bizResponse;
        }
        if (form.ID != null && !form.ID.equals("")) {
            //判断该记录是否作废
            biz = CheckFormStatus(form.ID);
            if (!biz.isSuccess) {
                bizResponse.isSuccess = false;
                bizResponse.message = biz.message;
                return bizResponse;
            }
            for (SaveForm classification : parms.lists) {//循环分类
                if (classification.FLLX.equals("0")) {
                    continue;
                }
                //判断签名
                BizResponse<EvaluateBDJLVo> qmBiz = GetEvaluationRecordInfo(form.ID, classification.ID, "0");
                if (!qmBiz.isSuccess) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = qmBiz.message;
                    return bizResponse;
                }
                String tempUseId = null;
                String syzt = qmBiz.data.getSYZT();
                if (syzt != null && !syzt.equals("0")) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "评估单已审阅不能修改";
                    return bizResponse;
                }
                if (classification.DLBZ == null || classification.DLBZ.equals("0")) {
                    tempUseId = qmBiz.data.getQMGH();
                    if (tempUseId != null && !tempUseId.equals("") && !tempUseId.equals(form.TXGH)) {
                        bizResponse.isSuccess = false;
                        bizResponse.message = "只有本人才能修改";
                        return bizResponse;
                    }
                } else {
                    qmBiz = GetEvaluationRecordInfo(form.ID, classification.ID, classification.DLBZ);
                    if (!qmBiz.isSuccess) {
                        bizResponse.isSuccess = false;
                        bizResponse.message = qmBiz.message;
                        return bizResponse;
                    }
                    if (qmBiz.data != null) {
                        tempUseId = qmBiz.data.HSQM1;
                        String secondUserId = qmBiz.data.HSQM2;
                        if (tempUseId != null && !tempUseId.equals("")) {
                            if (classification.QMBZ.equals("1")) {
                                if (secondUserId != null && !secondUserId.equals("")) {
                                    if (!secondUserId.equals(classification.TXGH)) {
                                        bizResponse.isSuccess = false;
                                        bizResponse.message = "只有本人才能修改";
                                        return bizResponse;
                                    }
                                } else {
                                    bizResponse.isSuccess = false;
                                    bizResponse.message = "只有本人才能修改";
                                    return bizResponse;
                                }
                            } else {
                                bizResponse.isSuccess = false;
                                bizResponse.message = "只有本人才能修改";
                                return bizResponse;
                            }
                        }
                    }
                }
                //校验数据
                biz = CheckSaveData(classification.entities, classification.ID);
                if (!biz.isSuccess) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = biz.message;
                    return bizResponse;
                }
            }
        }
        //保存数据
        keepOrRoutingDateSource(DataSource.MOB);
        String jlxh = SaveEvaluationData(parms);
        if (jlxh == null) {
            bizResponse.isSuccess = false;
            bizResponse.message = "保存出错";
            return bizResponse;
        }

        UpdateThread updateThread = new UpdateThread();//同步更新评估内容数据
        updateThread.setJlxhAndYsxh(jlxh, form.YSXH);
        updateThread.start();

        BizResponse<EvaluateFormItem> bizForm = GetExistEvaluation(jlxh, form.TXSJ, parms.jgid, null);
        bizResponse.data = bizForm.data;

        //同步数据
        Response<SelectResult> _response = BuilderSyncData(form.ZYH, parms.bqdm, form.TXGH, form.YSXH, form.TXSJ, jlxh, "0", parms.jgid);
        if (_response.ReType == 2) {
            bizResponse.data.IsSync = 1;
            bizResponse.data.list.add(_response.Data);
        }
        bizResponse.isSuccess = true;
        return bizResponse;

    }


    //Todo 暂未完成
    //同步数据源
    private Response<SelectResult> BuilderSyncData(String zyh, String bqdm, String txgh, String bdxh, String txsj, String jlxh, String czbz, String jgid) {

        InArgument inArgument = new InArgument();
        inArgument.zyh = zyh;
        inArgument.bqdm = bqdm;
        inArgument.hsgh = txgh;
        inArgument.bdlx = "1";
        inArgument.lybd = bdxh;
        inArgument.flag = czbz;
        inArgument.jlxh = jlxh;
        inArgument.jgid = jgid;
        inArgument.lymx = "0";
        inArgument.lymxlx = "0";
        if (txsj == null || "".equals(txsj)) {
            txsj = timeService.now(DataSource.PORTAL);
        }
        inArgument.jlsj = txsj;
        Project project = new Project("0", jlxh);
        Project _project = new Project("0", "");
        project.saveProjects.add(_project);
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

    //    @Transactional(rollbackFor = Exception.class)
    //保存表单记录操作
    private String SaveEvaluationData(EvaluateSaveRespose parms) {
        String jlxh;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SaveForm form = parms.saveForm;
        List<SaveForm> list = parms.lists;
        EvaluateBDJLVo bdjlVo = new EvaluateBDJLVo();
        EvaluateBDJLFLVo jlflVo = new EvaluateBDJLFLVo();
        try {
            if (form.ID == null || form.ID.equals("")) {//新增操作
                bdjlVo.setJLXH(identity.getIdentityMax("IENR_BDJL", 1, DataSource.MOB).datalist.get(0));
                bdjlVo.setZYH(form.ZYH);
                bdjlVo.setBRBQ(parms.bqdm);
                bdjlVo.setYSXH(form.YSXH);
                bdjlVo.setYSLX(form.YSLX);
                bdjlVo.setTXSJ(form.TXSJ);
                bdjlVo.setTXGH(form.TXGH);
                bdjlVo.setJGDJ(form.Score);
                bdjlVo.setJLSJ(timeService.now(DataSource.MOB));
                bdjlVo.setJGID(parms.jgid);
                service.addBDJL(bdjlVo);
                if (list.size() > 0) {
                    for (SaveForm saveForm : list) {
                        jlflVo.setJLFL(identity.getIdentityMax("IENR_BDJLFL", 1, DataSource.MOB).datalist.get(0));
                        jlflVo.setJLXH(String.valueOf(bdjlVo.getJLXH()));
                        jlflVo.setYSFL(saveForm.ID);
                        jlflVo.setFLDJ(saveForm.Score);
                        jlflVo.FLLX = saveForm.FLLX;
                        keepOrRoutingDateSource(DataSource.MOB);
                        service.addBDJLFL(jlflVo);
                    }
                }
            } else {
                String nowDateTime = timeService.getNowDateTimeStr(DataSource.MOB);
                //修改操作
                bdjlVo.setJLXH(Long.parseLong(form.ID));
                bdjlVo.setJGDJ(form.Score);
                bdjlVo.setJLXH(Long.valueOf(form.ID));
                bdjlVo.setTXSJ(nowDateTime);
                bdjlVo.type = "1";
                service.updateBDJL(bdjlVo);
                if (list.size() > 0) {
                    for (SaveForm saveForm : list) {
                        List<EvaluateBDJLFLVo> _list_jlfl = service.getBDJLFL(form.ID, saveForm.ID);
                        if (_list_jlfl == null || _list_jlfl.size() <= 0) {
                            long jlfl = identity.getIdentityMax("IENR_BDJLFL", 1, DataSource.MOB).datalist.get(0);
                            jlflVo.setJLFL(jlfl);
                            jlflVo.setJLXH(String.valueOf(bdjlVo.getJLXH()));
                            jlflVo.setYSFL(saveForm.ID);
                            jlflVo.setFLDJ(saveForm.Score);
                            jlflVo.FLLX = saveForm.FLLX;
                            keepOrRoutingDateSource(DataSource.MOB);
                            service.addBDJLFL(jlflVo);
                        }
                    }
                }
            }
            //对表单项目和表单项目选项表的操作
            for (SaveForm saveForm : list) {
//                service.updateBDJLFLFLLX(saveForm.ID, saveForm.FLLX);
                service.updateBDJLFLFLLXNew(form.ID, saveForm.ID, saveForm.FLLX);
                for (VEntity vEntity : saveForm.entities) {
                    if (saveForm.XSFLLX.equals("0") || !(saveForm.FLLX.equals("0") || saveForm.XSFLLX.equals("0"))) {
                        GetOptionSave(vEntity, String.valueOf(bdjlVo.getJLXH()));
                    }
                }
            }
            jlxh = String.valueOf(bdjlVo.getJLXH());

            //福建协和客户化：保存之后自动签名
            String now = timeService.now(DataSource.MOB);
            for (SaveForm saveForm : list) {
                jlflVo.setJLXH(jlxh);
                jlflVo.setYSFL(saveForm.ID);
                if (saveForm.entities != null && saveForm.entities.length > 0) {
                    //执行签名操作
                    jlflVo.setHSQM1(saveForm.HSQM1);
                    jlflVo.setQMSJ1(now);
                }
                jlflVo.type = "2";
                service.updateBDJLFL(jlflVo);
            }

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            return null;
        } catch (Exception e) {
            return null;
        }
        return jlxh;
    }

    private void GetOptionSave(VEntity vEntity, String jlxh) {
        String type = vEntity.CtrlType;
        EvaluateBDJLXMVo jlxmVo = new EvaluateBDJLXMVo();
        jlxmVo.setYSXM(String.valueOf(vEntity.XMID));
        jlxmVo.setJLXH(jlxh);
        // add by louis
        jlxmVo.setDZLX(vEntity.dzlx);
        jlxmVo.setDZBDJL(vEntity.dzbdjl);

        try {
            keepOrRoutingDateSource(DataSource.MOB);
            List<EvaluateBDJLXMVo> jlxmList = service.getBDJLXM(jlxh, String.valueOf(vEntity.XMID));
            //福建协和客户化：添加对特殊控件的处理 增加 || type.equals("SpinnerDataInfo")判断条件
            if (type.equals("Input") || type.equals("Spinnner") || type.equals("DateTime")
                    || type.equals("Numeric") || type.equals("SpecSpinnerDataInfo") || type.equals("SpinnerDataInfo")) {
                jlxmVo.setXMDJ(vEntity.Value);
                if (jlxmList.size() > 0) {//记录中已有改项目则更新
                    jlxmVo.setJLXM(jlxmList.get(0).getJLXM());

                    service.updateBDJLXM(jlxmVo);
                } else {//否则新增
                    jlxmVo.setJLXM(identity.getIdentityMax("IENR_BDJLXM", 1, DataSource.MOB).datalist.get(0));

                    keepOrRoutingDateSource(DataSource.MOB);
                    service.addBDJLXM(jlxmVo);
                }
                if (!String.valueOf(vEntity.XXID).equals("")) {
                      /*升级编号【56010048】============================================= start
                        PDA端输入部分值（修改时选项变成新增），PC端无法显示
                                ================= Classichu 2017/10/17 8:55
                                */
                    //List<EvaluateBDJLXXVo> jlxxList = service.getBDJLXX(jlxh, String.valueOf(vEntity.XXID), String.valueOf(vEntity.XMID));
                    List<EvaluateBDJLXXVo> jlxxList = service.getBDJLXX(jlxh, String.valueOf(vEntity.XXID_Raw), String.valueOf(vEntity.XMID));
                    /* =============================================================== end */
                    if (jlxxList.size() > 0) {
                        EvaluateBDJLXXVo xxVo = new EvaluateBDJLXXVo();
                        /*升级编号【56010048】============================================= start
                        PDA端输入部分值（修改时选项变成新增），PC端无法显示
                                ================= Classichu 2017/10/17 8:55
                                */
                        //### xxVo.setJLXX((long) vEntity.XXID);//2017-10-16 22:53:27
                        xxVo.setJLXX(jlxxList.get(0).getJLXX());
                        xxVo.setXXNR(vEntity.Value);
                        /* =============================================================== end */

                        //fixme 2017-6-5 18:22:45 是不是也需要set剩下的字段
                        service.updateBDJLXX(xxVo);
                    } else {
                        EvaluateBDJLXXVo xxVo = new EvaluateBDJLXXVo();
                        xxVo.setJLXX(identity.getIdentityMax("IENR_BDJLXX", 1, DataSource.MOB).datalist.get(0));
                        xxVo.setXXNR(vEntity.Value);
                        xxVo.setJLXM(String.valueOf(jlxmVo.getJLXM()));
                        xxVo.setJLXH(jlxh);
                           /*升级编号【56010048】============================================= start
                        PDA端输入部分值（修改时选项变成新增），PC端无法显示
                                ================= Classichu 2017/10/17 8:55
                                */
                        //xxVo.setYSXX(String.valueOf(vEntity.XXID));//2017-10-16 21:50:32
                        xxVo.setYSXX(String.valueOf(vEntity.XXID_Raw));
                        /* =============================================================== end */
                        //fixme 2017-6-5 18:22:45 是不是也需要set剩下的字段
                        keepOrRoutingDateSource(DataSource.MOB);
                        service.addBDJLXX(xxVo);
                    }
                }
            } else {//对checkbox。radiobox的操作
                jlxmVo.setXMDJ(vEntity.Score);
                   /*升级编号【56010048】============================================= start
                        PDA端输入部分值（修改时选项变成新增），PC端无法显示
                                ================= Classichu 2017/10/17 8:55
                                */
                List<EvaluateBDJLXXVo> jlxxList = service.getBDJLXX(jlxh, String.valueOf(vEntity.XXID), String.valueOf(vEntity.XMID));
                //2017-10-18 09:26:11   List<EvaluateBDJLXXVo> jlxxList = service.getBDJLXX(jlxh, String.valueOf(vEntity.XXID_Raw), String.valueOf(vEntity.XMID));
                /* =============================================================== end */
                if (jlxxList.size() <= 0) {
                    if (jlxmList.size() > 0) {
                        Integer xmdj = 0;
                        try {
                            xmdj = Integer.parseInt(jlxmList.get(0).getXMDJ());
                        } catch (NumberFormatException e) {
                        }
                        try {
                            xmdj = xmdj + Integer.parseInt(vEntity.Score);
                        } catch (NumberFormatException e) {
                        }
                        jlxmVo.setJLXM(jlxmList.get(0).getJLXM());
                        jlxmVo.setXMDJ(String.valueOf(xmdj));

                        service.updateBDJLXM(jlxmVo);
                    } else {
                        jlxmVo.setJLXM(identity.getIdentityMax("IENR_BDJLXM", 1, DataSource.MOB).datalist.get(0));


                        keepOrRoutingDateSource(DataSource.MOB);
                        service.addBDJLXM(jlxmVo);

                    }
                    EvaluateBDJLXXVo xxVo = new EvaluateBDJLXXVo();
                    xxVo.setJLXX(identity.getIdentityMax("IENR_BDJLXX", 1, DataSource.MOB).datalist.get(0));
                    xxVo.setXXNR(vEntity.Value);
                    xxVo.setJLXM(String.valueOf(jlxmVo.getJLXM()));
                    xxVo.setJLXH(jlxh);
                      /*升级编号【56010048】============================================= start
                        PDA端输入部分值（修改时选项变成新增），PC端无法显示
                                ================= Classichu 2017/10/17 8:55
                                */
                    xxVo.setYSXX(String.valueOf(vEntity.XXID));//2017-10-18 09:26:11
                    //xxVo.setYSXX(String.valueOf(vEntity.XXID_Raw));
                    /* =============================================================== end */
                    //fixme 2017-6-5 18:22:45 是不是也需要set剩下的字段
                    keepOrRoutingDateSource(DataSource.MOB);
                    service.addBDJLXX(xxVo);
                } else {
                    if (jlxmList.size() > 0) {
                        Integer xmdj = 0;
                        try {
                            xmdj = Integer.parseInt(jlxmList.get(0).getXMDJ());
                        } catch (NumberFormatException ignored) {
                        }
                        try {
                            xmdj = xmdj - Integer.parseInt(vEntity.Score);
                        } catch (NumberFormatException ignored) {
                        }
                        jlxmVo.setJLXM(jlxmList.get(0).getJLXM());
                        jlxmVo.setXMDJ(String.valueOf(xmdj));

                        service.updateBDJLXM(jlxmVo);
                    }
                    //service.deleteBDJLXX(jlxxList.get(0).getYSXX());//会把所有YSXX都删除掉(造成数据丢失) 龙岩发现2018.12.26
                    service.deleteBDJLXX(jlxxList.get(0).getJLXX().toString());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    //校验保存数据
    private BizResponse<String> CheckSaveData(VEntity[] entities, String ysxh) {
        BizResponse<String> bizResponse = new BizResponse<>();
        if (ysxh == null || ysxh.equals("")) {
            bizResponse.isSuccess = false;
            bizResponse.message = "表单样式序号为空";
            return bizResponse;
        }
        if (entities != null && entities.length > 0) {
            for (VEntity vEntity : entities) {
                if (String.valueOf(vEntity.XMID).equals("")) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "数据不正确,请刷新后尝试";
                    return bizResponse;
                }
            }
        }
        bizResponse.isSuccess = true;
        return bizResponse;
    }

    //获取签名和审阅的信息
    private BizResponse<EvaluateBDJLVo> GetEvaluationRecordInfo(String jlxh, String ysfl, String dlbz) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse bizResponse = new BizResponse();
        EvaluateBDJLVo bdjlVo = new EvaluateBDJLVo();
        try {
            if (dlbz == null || dlbz.equals("0")) {
                bdjlVo = service.getBDJL(jlxh);
                bizResponse.data = bdjlVo;
            } else {
                List<EvaluateBDJLFLVo> jlflList = service.getBDJLFL(jlxh, ysfl);
                if (jlflList != null && jlflList.size() > 0) {
                    bdjlVo.HSQM1 = jlflList.get(0).getHSQM1();
                    bdjlVo.HSQM2 = jlflList.get(0).getHSQM2();
                    bizResponse.data = bdjlVo;
                } else {
                    bizResponse.data = null;
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
            return bizResponse;
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
            return bizResponse;
        }
        bizResponse.isSuccess = true;
        return bizResponse;
    }

    //判断表单是否作废
    private BizResponse<String> CheckFormStatus(String jlxh) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse bizResponse = new BizResponse();
        try {
            EvaluateBDJLVo bdjlVo = service.getBDJL(jlxh);
            if (bdjlVo == null) {
                bizResponse.isSuccess = false;
                bizResponse.message = "评估单记录不存在";
                return bizResponse;
            }
            if (bdjlVo.getSYZT().equals("1")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "评估单已作废";
                return bizResponse;
            }

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
            return bizResponse;
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
            return bizResponse;
        }
        bizResponse.isSuccess = true;
        return bizResponse;
    }


    public BizResponse<EvaluateResponse> CancelEvaluation(String jlxh, String jgid) {
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            EvaluateBDJLVo bdjlVo = new EvaluateBDJLVo();
            bdjlVo.setJLXH(Long.parseLong(jlxh));
            bdjlVo.setJGDJ(jgid);
            bdjlVo.setZFBZ("1");
            bdjlVo.type = "4";
            if (service.updateBDJL(bdjlVo)) {
                bizResponse.message = "作废成功";
            } else {
                bizResponse.message = "作废失败";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
            return bizResponse;
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
            return bizResponse;
        }
        bizResponse.isSuccess = true;
        return bizResponse;
    }

    public BizResponse<EvaluateResponse> EvaluationSignature(String jlxh, String ysxh, String ysfl, String lybs, String hsqm1, String hsqm2, String dlbz, String qmbz) {
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        try {
            if (lybs.equals("1")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "该版本不支持EMR签名";
                return bizResponse;
            }
            String now = timeService.now(DataSource.MOB);
            //判断签名
            BizResponse<EvaluateBDJLVo> qmBiz = GetEvaluationRecordInfo(jlxh, ysfl, dlbz);
            if (!qmBiz.isSuccess) {
                bizResponse.isSuccess = false;
                bizResponse.message = qmBiz.message;
                return bizResponse;
            }
            if (qmBiz.data == null) {
                bizResponse.isSuccess = false;
                bizResponse.message = "该分类没有保存的数据先保存数据";
                return bizResponse;
            }
            if (dlbz.equals("0")) {
                String userId = qmBiz.data.getQMGH();
                if (userId != null && !userId.equals("")) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "已签名不需要再签名";
                    return bizResponse;
                }
                EvaluateBDJLVo bdjlVo = new EvaluateBDJLVo();
                bdjlVo.setJLXH(Long.parseLong(jlxh));
                bdjlVo.setQMGH(hsqm1);
                bdjlVo.setQMSJ(now);
                bdjlVo.type = "2";
                keepOrRoutingDateSource(DataSource.MOB);
                service.updateBDJL(bdjlVo);
            } else {
                String userId = qmBiz.data.HSQM1;
                if (userId != null && !userId.equals("")) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "已签名不需要再签名";
                    return bizResponse;
                }
                EvaluateBDJLFLVo jlflVo = new EvaluateBDJLFLVo();
                jlflVo.setJLXH(jlxh);
                jlflVo.setYSFL(ysfl);
                jlflVo.setHSQM1(hsqm1);
                jlflVo.setQMSJ1(now);
                if (qmbz.equals("0")) {//单签
                    jlflVo.type = "1";
                    service.updateBDJLFL(jlflVo);
                } else {//双签
                    jlflVo.setQMSJ2(hsqm2);
                    jlflVo.setQMSJ2(now);
                    jlflVo.type = "2";
                    service.updateBDJLFL(jlflVo);
                }
            }
            bizResponse.message = "签名成功";
            bizResponse.isSuccess = true;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

    public BizResponse<EvaluateResponse> CancelEvaluationSignature(String jlxh, String ysxh, String ysfl, String lybs, String hsqm1, String hsqm2, String dlbz, String qmbz) {
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        try {
            if (lybs.equals("1")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "该版本不支持EMR签名";
                return bizResponse;
            }
            //判断签名
            BizResponse<EvaluateBDJLVo> qmBiz = GetEvaluationRecordInfo(jlxh, ysfl, dlbz);
            if (!qmBiz.isSuccess) {
                bizResponse.isSuccess = false;
                bizResponse.message = qmBiz.message;
                return bizResponse;
            }
            if (dlbz.equals("0")) {
                String userId = qmBiz.data.getQMGH();
                if (userId == null || userId.equals("")) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "未签名不需要取消签名";
                    return bizResponse;
                }
                EvaluateBDJLVo bdjlVo = new EvaluateBDJLVo();
                bdjlVo.setJLXH(Long.parseLong(jlxh));
                bdjlVo.type = "2";
                keepOrRoutingDateSource(DataSource.MOB);
                service.updateBDJL(bdjlVo);
            } else {
                String userId = qmBiz.data.HSQM1;
                if (userId == null || userId.equals("")) {
                    bizResponse.isSuccess = false;
                    bizResponse.message = "未签名不需要取消签名";
                    return bizResponse;
                }
                EvaluateBDJLFLVo jlflVo = new EvaluateBDJLFLVo();
                jlflVo.setJLXH(jlxh);
                jlflVo.setYSFL(ysfl);
                jlflVo.type = "2";
                service.updateBDJLFL(jlflVo);
            }
            bizResponse.message = "取消签名成功";
            bizResponse.isSuccess = true;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

    public BizResponse<EvaluateResponse> EvaluationReview(String jlxh, String sygh, String jgid) {
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            //判断签名
            BizResponse<EvaluateBDJLVo> qmBiz = GetEvaluationRecordInfo(jlxh, "", "0");
            if (!qmBiz.isSuccess) {
                bizResponse.isSuccess = false;
                bizResponse.message = qmBiz.message;
                return bizResponse;
            }
            String userId = qmBiz.data.getSYGH();
            if (userId != null && !userId.equals("")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "已审阅不需要再审阅";
                return bizResponse;
            }
            if (!CheckSingnature(jlxh)) {
                bizResponse.isSuccess = false;
                bizResponse.message = "还有未签名不能审阅";
                return bizResponse;
            }
            EvaluateBDJLVo bdjlVo = new EvaluateBDJLVo();
            bdjlVo.setSYGH(sygh);
            bdjlVo.setSYSJ(timeService.now(DataSource.MOB));
            bdjlVo.setJLXH(Long.valueOf(jlxh));
            bdjlVo.setSYZT("1");
            bdjlVo.type = "3";
            service.updateBDJL(bdjlVo);
            bizResponse.message = "审阅成功";
            bizResponse.isSuccess = true;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

    //判断整个表单签名状态
    private boolean CheckSingnature(String jlxh) {
        try {
            List<EvaluateBDJLVo> list = service.getBDQMXX(jlxh);
            for (EvaluateBDJLVo vo : list) {
                if (vo.DLBZ.equals("1") ? (vo.HSQM1 == null || vo.HSQM1.equals("")) : (vo.getQMGH() == null || vo.getQMGH().equals(""))) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public BizResponse<EvaluateResponse> CancelEvaluationReview(String jlxh, String sygh) {
        BizResponse<EvaluateResponse> bizResponse = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.MOB);
        try {
            //判断签名
            BizResponse<EvaluateBDJLVo> qmBiz = GetEvaluationRecordInfo(jlxh, "", "0");
            if (!qmBiz.isSuccess) {
                bizResponse.isSuccess = false;
                bizResponse.message = qmBiz.message;
                return bizResponse;
            }
            String userId = qmBiz.data.getSYGH();
            if (userId == null || userId.equals("")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "未审阅不需要取消审阅";
                return bizResponse;
            }
            if (!sygh.equals(userId)) {
                bizResponse.isSuccess = false;
                bizResponse.message = "取消审阅不是同一个人";
                return bizResponse;
            }
            EvaluateBDJLVo bdjlVo = new EvaluateBDJLVo();
            bdjlVo.setSYGH(null);
            bdjlVo.setSYSJ(null);
            bdjlVo.setJLXH(Long.valueOf(jlxh));
            bdjlVo.setSYZT("0");
            bdjlVo.type = "3";
            service.updateBDJL(bdjlVo);
            bizResponse.message = "取消审阅成功";
            bizResponse.isSuccess = true;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;

    }

    //更新评估内容
    public void updatePGNR(String jlxh, String ysxh) {
        try {
            List<EvaluateBDJLFLVo> flList = service.getBDJLFL(jlxh, null);
            if (flList != null && flList.size() > 0) {
                List<EvaluateBDYSXMVo> ysxmList = service.getFLXM(ysxh, null);
                List<EvaluateBDYSXXVo> ysxxList = service.getFLXX(ysxh, null);
                List<EvaluateBDJLXMVo> jlxmList = service.getBDJLXM(jlxh, null);
                List<EvaluateBDJLXXVo> jlxxList = service.getBDJLXX(jlxh, null, null);
                String pgnr = "";//总评估内容
                //循环记录分类
                for (EvaluateBDJLFLVo bdjlflVo : flList) {
                    //每个分类的评估内容
                    String flpgnr = "";
                    if (bdjlflVo.FLLX.equals("0")) {
                        service.updateBDJLFLPGNR("无", jlxh, String.valueOf(bdjlflVo.getJLFL()));
                        continue;
                    }
                    //循环没个样式项目
                    for (EvaluateBDYSXMVo bdysxmVo : ysxmList) {
                        //如果是同一个分类
                        if (bdysxmVo.getYSFL().equals(bdjlflVo.getYSFL())) {
                            //去记录项目中寻找是否有相应的项目
                            for (EvaluateBDJLXMVo bdjlxmVo : jlxmList) {
                                if (bdjlxmVo.getYSXM().equals(String.valueOf(bdysxmVo.getYSXM()))) {
                                    jlxmList.remove(bdjlxmVo);
                                    flpgnr += bdysxmVo.getXMMC() + ":";
                                    //去项目选项表查找值
                                    flpgnr += getPGNR(ysxxList, jlxxList, String.valueOf(bdysxmVo.getYSXM()), String.valueOf(bdjlxmVo.getJLXM()), "0");
                                    break;
                                }
                            }
                        }
                    }
                    pgnr += flpgnr;
                    service.updateBDJLFLPGNR(flpgnr, jlxh, String.valueOf(bdjlflVo.getJLFL()));
                }
                service.updatteBDJLPGNR(pgnr, jlxh);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private String getPGNR(List<EvaluateBDYSXXVo> ysxxList, List<EvaluateBDJLXXVo> jlxxList, String ysxm, String jlxm, String fjxx) {
        //循环样式选项
        String pgnr = "";
        for (EvaluateBDYSXXVo bdysxxVo : ysxxList) {
            //循环对应项目下的选项或 对应选项下的字典控件fjxx为0为选项控件
            if (bdysxxVo.getYSXM().equals(ysxm) && bdysxxVo.getFJXX().equals(fjxx)) {
                //去jlxx表查找是否有该控件的记录
                for (EvaluateBDJLXXVo bdjlxxVo : jlxxList) {
                    if (bdjlxxVo.getJLXM().equals(jlxm) && bdjlxxVo.getYSXX().equals(String.valueOf(bdysxxVo.getYSXX()))) {
                        jlxxList.remove(bdjlxxVo);
                        if (bdysxxVo.getCZLX().equals("2") || bdysxxVo.getCZLX().equals("3")) {//单选或多选
                            pgnr = bdysxxVo.getXXNR();
                            String xjnr = getPGNR(ysxxList, jlxxList, ysxm, jlxm, String.valueOf(bdysxxVo.getYSXX()));
                            if (!xjnr.equals("")) {
                                pgnr = pgnr + ":" + xjnr;
                            }
                        } else if (bdysxxVo.getCZLX().equals("2")) {//输入
                            pgnr = bdysxxVo.getXXNR() + ":" + bdjlxxVo.getXXNR() + bdysxxVo.getHZWB();
                        }
                        //下拉不操作
                        break;
                    }
                }
            }
        }
        return pgnr;
    }

    //设置排序类
    public static Comparator<EvalBaseVo> evalBaseVoComparator =
            new Comparator<EvalBaseVo>() {

                @Override
                public int compare(EvalBaseVo vo1, EvalBaseVo vo2) {
                    return (vo1.PLSX - vo2.PLSX);
                }
            };

    //更新评估单评估内容
    public void updatePgdPgnr(long jlxh, long ysxh) {
        try {
            List<EvalFljlVo> dtFLJL = service.getEvalFljl(jlxh);
            if (dtFLJL == null || dtFLJL.size() < 1) return;

            List<EvalXmxxVo> dtYSXM = service.getEvalYsxm(ysxh);
            List<EvalXmxxVo> dtYSXX = service.getEvalYsxx(ysxh);
            List<EvalJlxmVo> dtJLXM = service.getEvalJlxm(jlxh);
            List<EvalJlxmVo> dtJLXX = service.getEvalJlxx(jlxh);

            String FLNR = "", PGNR = "", flmc = "", xmmc = "", GLCOL = "";
            long xx_srlx = 0, xx2_srlx = 0;
            long xm_srlx = 0;
            boolean xm_flag = false, xx_flag = false;
            //long ysfl = 0, ysxm = 0, jlxm = 0, ysxx2 = 0, ysxx = 0,

            for (EvalFljlVo fljlVo : dtFLJL) {
                //分类循环
                FLNR = "";
                flmc = fljlVo.FLMC;
                final long ysfl = fljlVo.YSFL;

                //使用lambda表达式过滤出当前分类的所有项目,需要JDK1.8+ 版本
                List<EvalXmxxVo> drYSXM = dtYSXM.stream().filter((EvalXmxxVo ysxmNode) -> ysxmNode.YSFL == ysfl).collect(Collectors.toList());
                if (drYSXM == null || drYSXM.size() < 1) continue;
                Collections.sort(drYSXM, evalBaseVoComparator);

                for (EvalXmxxVo ysxmVo : drYSXM) {
                    final long ysxm = ysxmVo.YSXM;
                    xmmc = ysxmVo.XMMC;
                    xm_srlx = ysxmVo.CZLX;

                    xm_flag = false;

                    List<EvalJlxmVo> drJLXM = dtJLXM.stream().filter((EvalJlxmVo jlxmNode) -> jlxmNode.YSXM == ysxm).collect(Collectors.toList());
                    if (drJLXM == null || drJLXM.size() < 1) continue;

                    final long jlxm = drJLXM.get(0).JLXM;

                    if (xm_srlx == 1) {
                        // 输入
                        List<EvalJlxmVo> drJLXX = dtJLXX.stream().filter((EvalJlxmVo jlxxNode) -> jlxxNode.JLXM == jlxm).collect(Collectors.toList());
                        if (drJLXX == null || drJLXX.size() < 1) continue;

                        FLNR += xmmc + ":" + (StringUtils.isEmpty(drJLXX.get(0).XMNR) ? "" : drJLXX.get(0).XMNR)
                                + (StringUtils.isEmpty(ysxmVo.HZWB) ? "" : ysxmVo.HZWB);

                    } else if (xm_srlx == 4) {
                        // 下拉 暂不处理
                        continue;
                    } else if (xm_srlx == 2 || xm_srlx == 3) {
                        //单选及多选
                        List<EvalXmxxVo> drYSXX = dtYSXX.stream().filter((EvalXmxxVo ysxxNode) -> ysxxNode.YSXM == ysxm && ysxxNode.FJXX == 0).collect(Collectors.toList());
                        if (drYSXX == null || drYSXX.size() < 1) {
                            FLNR += xmmc + ";";
                            continue;
                        }

                        Collections.sort(drYSXX, evalBaseVoComparator);
                        //选项级
                        for (EvalXmxxVo ysxxVo : drYSXX) {
                            final long ysxx = ysxxVo.YSXX;
                            GLCOL = ysxxVo.GLCOL;
                            xx_flag = false;

                            List<EvalJlxmVo> drJLXX = null;
                            if (!StringUtils.isEmpty(GLCOL)) {
                                drJLXX = dtJLXX.stream().filter((EvalJlxmVo jlxxNode) -> jlxxNode.JLXM == jlxm && jlxxNode.YSXX == ysxx).collect(Collectors.toList());
                                if (drJLXX == null || drJLXX.size() < 1) continue;
                            }

                            if (!xm_flag) {
                                FLNR += xmmc + ":";
                                xm_flag = true;
                            }

                            xx_srlx = ysxxVo.CZLX;
                            if (xx_srlx == 1) {
                                //输入
                                if (StringUtils.isEmpty(GLCOL)) continue;
                                FLNR += ysxxVo.XMMC + ":"
                                        + (StringUtils.isEmpty(drJLXX.get(0).XMNR) ? "" : drJLXX.get(0).XMNR)
                                        + (StringUtils.isEmpty(ysxxVo.HZWB) ? "" : ysxxVo.HZWB);

                            } else if (xx_srlx == 4) {
                                //下拉暂不处理
                                continue;
                            } else if (xx_srlx == 2 || xx_srlx == 3) {
                                List<EvalXmxxVo> drYSXX2 = dtYSXX.stream().filter((EvalXmxxVo ysxxNode) -> ysxxNode.YSXM == ysxm && ysxxNode.FJXX == ysxx).collect(Collectors.toList());
                                if (drYSXX2 != null && drYSXX2.size() > 0)
                                    Collections.sort(drYSXX2, evalBaseVoComparator);
                                if (drYSXX2 == null || drYSXX2.size() < 1) {
                                    if (!xx_flag) {
                                        FLNR += ysxxVo.XMMC + ";";
                                        xx_flag = true;
                                    }
                                    continue;
                                }

                                for (EvalXmxxVo ysxx2Vo : drYSXX2) {
                                    final long ysxx2 = ysxx2Vo.YSXX;
                                    List<EvalJlxmVo> drJLXX2 = dtJLXX.stream().filter((EvalJlxmVo jlxxNode) -> jlxxNode.JLXM == jlxm && jlxxNode.YSXX == ysxx2).collect(Collectors.toList());
                                    if (drJLXX2 == null || drJLXX2.size() < 1) continue;

                                    if (!xx_flag) {
                                        FLNR += ysxxVo.XMMC + ":";
                                        xx_flag = true;
                                    }

                                    xx2_srlx = ysxx2Vo.CZLX;
                                    if (xx2_srlx == 1) {
                                        //录入
                                        FLNR += ysxx2Vo.XMMC + ":"
                                                + (StringUtils.isEmpty(drJLXX2.get(0).XMNR) ? "" : drJLXX2.get(0).XMNR)
                                                + (StringUtils.isEmpty(ysxx2Vo.HZWB) ? "" : ysxx2Vo.HZWB) + ";";
                                    } else if (xx2_srlx == 4) {
                                        //下拉暂不处理
                                        continue;
                                    } else if (xx2_srlx == 2 || xx2_srlx == 3) {
                                        FLNR += ysxx2Vo.XMMC + ";";
                                    }


                                }// For drYSXX2

                            }

                        }// For drYSXX
                    }
                } // For drYSXM
                FLNR = "【" + flmc + "】" + FLNR;
                PGNR += FLNR;
                service.updateBDJLFLPGNR(FLNR, String.valueOf(jlxh), String.valueOf(ysfl));
            }// For dtFLJL

            service.updatteBDJLPGNR(PGNR, String.valueOf(jlxh));

            return;

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取对照项目的数据
     */
    public BizResponse<EvaluateFormItem> CreateRelativeEvaluation(String zyh, String ysxh, String dzlx, String bqdm, String txsj, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<EvaluateFormItem> bizResponse = new BizResponse<>();
        try {
            Form form = CreateEvaluation(zyh, null, ysxh, bqdm, txsj, jgid, dzlx);
            EvaluateFormItem evaluateFormItem = new EvaluateFormItem();
            evaluateFormItem.form = form;
            bizResponse.data = evaluateFormItem;
            bizResponse.isSuccess = true;
            return bizResponse;
        } catch (DataAccessException e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        } catch (Exception e) {
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;

    }


    class UpdateThread extends Thread {

        private String ysxh;

        private String jlxh;

        public void setJlxhAndYsxh(String _jlxh, String _ysxh) {
            this.jlxh = _jlxh;
            this.ysxh = _ysxh;
        }

        @Override
        public void run() {
            //updatePGNR(jlxh, ysxh);
            updatePgdPgnr(Long.parseLong(jlxh), Long.parseLong(ysxh));
        }
    }
}


