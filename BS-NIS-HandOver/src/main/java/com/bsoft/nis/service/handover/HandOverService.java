package com.bsoft.nis.service.handover;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.handover.*;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.handover.support.HandOverServiceSup;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-02-15
 * Time: 10:39
 * Version:
 */
@Service
public class HandOverService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(HandOverService.class);

    @Autowired
    HandOverServiceSup service; // 健康教育服务

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
     * 获取交接单模板列表及其记录数据
     *
     * @param zyh  住院号
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HandOverForm> getHandOverList(String zyh, String bqid, String jgid) {
        BizResponse<HandOverForm> response = new BizResponse<>();
        try {
            List<HandOverForm> handOverFormList = service.getHandOverFormList(jgid);
            for (HandOverForm handOverForm : handOverFormList) {
                handOverForm.HandOverRecordList = service.getHandOverRecordList(handOverForm.YSXH, "", zyh, bqid, jgid);
            }
            response.datalist = handOverFormList;
            response.isSuccess = true;
            response.message = "获取交接单模板列表及其记录数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取交接单模板列表及其记录数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取交接单模板列表及其记录数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取交接单记录数据 - 批量
     *
     * @param bqid 病区代码
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HandOverRecord> getHandOverRecordList(String bqid, String jgid) {
        BizResponse<HandOverRecord> response = new BizResponse<>();
        try {
            List<HandOverRecord> handOverRecordList = service.getHandOverRecordList("", "1", "", bqid, jgid);
            response.datalist = handOverRecordList;
            response.isSuccess = true;
            response.message = "获取交接单模板列表及其记录数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取交接单模板列表及其记录数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取交接单模板列表及其记录数据失败]服务内部错误!";
        }
        return response;
    }
    /**
     * 获取交接单记录数据 - 批量
     *
     * @param ssks 手术科室
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HandOverRecord> getHandOverRecordListBySSKS(String ssks, String jgid) {
        BizResponse<HandOverRecord> response = new BizResponse<>();
        try {
            List<HandOverRecord> handOverRecordList = service.getHandOverRecordListBySSKS("", "1", "",ssks, jgid);
            response.datalist = handOverRecordList;
            response.isSuccess = true;
            response.message = "获取交接单模板列表及其记录数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取交接单模板列表及其记录数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取交接单模板列表及其记录数据失败]服务内部错误!";
        }
        return response;
    }
    /**
     * 接收交接单记录数据
     *
     * @param handOverRecord
     * @return
     */
    public BizResponse<HandOverRecord> receiveHandOverRecord(HandOverRecord handOverRecord) {
        BizResponse<HandOverRecord> response = new BizResponse<>();
        try {
            if (handOverRecord == null) {
                response.isSuccess = false;
                response.message = "[保存交接单记录数据失败]传入参数错误!";
            }
            handOverRecord = getFinalHandOverRecord(handOverRecord, "2");
            keepOrRoutingDateSource(DataSource.MOB);
            //修改操作
            boolean isSucess = editHandOverRecord(handOverRecord, "2");
            if (isSucess) {
                response.isSuccess = true;
                response.data = handOverRecord;
                response.message = "保存交接单记录数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "保存交接单记录数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存交接单记录数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存交接单记录数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取交接单记录数据
     *
     * @param jlxh 记录序号
     * @param ysxh 样式序号
     * @param zyh  住院号
     * @param txsj 填写时间
     * @param jgid 机构id
     * @return
     */
    public BizResponse<HandOverRecord> getHandOverRecord(String jlxh, String ysxh, String zyh, String txsj, String jgid) {
        BizResponse<HandOverRecord> response = new BizResponse<>();
        try {
            HandOverRecord handOverRecord;
            if (StringUtils.isBlank(jlxh) || jlxh.equals("0")) {
                //新增操作
                handOverRecord = new HandOverRecord();
                handOverRecord.JLXH = "0";
                handOverRecord.YSXH = ysxh;
                handOverRecord.ZTBZ = "0";
                handOverRecord.HandOverFormTemplate = getHandOverFormTemplate(ysxh, zyh, txsj, jgid, "1");
                if (handOverRecord.HandOverFormTemplate != null) {
                    handOverRecord.YSLX = handOverRecord.HandOverFormTemplate.YSLX;
                }
                handOverRecord.HandOverFormBefore = filterHandOverFormTemplate(handOverRecord.HandOverFormTemplate, "1");

            } else {
                //修改操作
                handOverRecord = service.getHandOverRecord(jlxh);
                List<HandOverProject> projectList = service.getHandOverRecordProjectList(jlxh);
                List<HandOverOption> optionList = service.getHandOverRecordOptionList(jlxh);
                handOverRecord.HandOverFormTemplate = getHandOverFormTemplate(ysxh, zyh, txsj, jgid, "1");
                handOverRecord.HandOverFormBefore = filterHandOverFormTemplate(handOverRecord.HandOverFormTemplate, "1");
                handOverRecord.HandOverFormBefore = getHandOverForm(handOverRecord.HandOverFormBefore, projectList, optionList, "1");
                if (handOverRecord.ZTBZ.equals("1") || handOverRecord.ZTBZ.equals("2")) {
                    handOverRecord.HandOverFormAfert = filterHandOverFormTemplate(handOverRecord.HandOverFormTemplate, "2");
                    handOverRecord.HandOverFormAfert = getHandOverForm(handOverRecord.HandOverFormAfert, projectList, optionList, "2");
                    handOverRecord.HandOverFormAfert = getFinalHandOverFormForAfter(handOverRecord.HandOverFormBefore, handOverRecord.HandOverFormAfert);
                }
            }
            response.data = handOverRecord;
            response.isSuccess = true;
            response.message = "获取交接单记录数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取交接单记录数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取交接单记录数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存交接单记录数据
     *
     * @param handOverRecord
     * @return
     */
    public BizResponse<HandOverRecord> saveHandOverRecord(HandOverRecord handOverRecord) {
        BizResponse<HandOverRecord> response = new BizResponse<>();
        try {
            if (handOverRecord == null) {
                response.isSuccess = false;
                response.message = "[保存交接单记录数据失败]传入参数错误!";
            }
            boolean isSucess;
            handOverRecord = getFinalHandOverRecord(handOverRecord, "1");
            if (handOverRecord.JLXH.contains("add_")) {
                //新增操作
                handOverRecord.DQZD = service.getDqzd(handOverRecord.ZYH);
                keepOrRoutingDateSource(DataSource.MOB);
                isSucess = addHandOverRecord(handOverRecord);
            } else {
                //修改操作
                keepOrRoutingDateSource(DataSource.MOB);
                isSucess = editHandOverRecord(handOverRecord, "1");
            }
            if (isSucess) {
                response.isSuccess = true;
                response.data = handOverRecord;
                response.message = "保存交接单记录数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "保存交接单记录数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存交接单记录数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[保存交接单记录数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 作废交接单记录数据
     *
     * @param jlxh
     * @return
     */
    public BizResponse<String> delHandOverRecord(String jlxh) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(jlxh)) {
                response.isSuccess = false;
                response.message = "[删除交接单记录数据失败]传入参数错误!";
            }
            keepOrRoutingDateSource(DataSource.MOB);
            boolean isSucess = finalDelHandOverRecord(jlxh);
            if (isSucess) {
                response.isSuccess = true;
                response.data = "删除交接单记录数据成功!";
                response.message = "删除交接单记录数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "删除交接单记录数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除交接单记录数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除交接单记录数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 发起方发送交接单记录数据
     *
     * @param jlxh
     * @return
     */
    public BizResponse<String> sendHandOverRecord(String jlxh) {
        BizResponse<String> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(jlxh)) {
                response.isSuccess = false;
                response.message = "[发送交接单记录数据失败]传入参数错误!";
            }
            boolean isSucess = service.sendHandOverRecord(jlxh) > 0;
            if (isSucess) {
                response.isSuccess = true;
                response.data = "发送交接单记录数据成功!";
                response.message = "发送交接单记录数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "发送交接单记录数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[发送交接单记录数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[发送交接单记录数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取对照数据
     *
     * @param zyh
     * @param dzlx
     * @param txsj
     * @param jgid
     * @return
     */
    public BizResponse<RelativeItem> GetRelativeData(String zyh, String dzlx, String txsj, String jgid) {
        BizResponse<RelativeItem> response = new BizResponse<>();
        List<RelativeItem> relativeItemList = null;
        try {
            if (StringUtils.isBlank(zyh) || StringUtils.isBlank(dzlx)
                    || StringUtils.isBlank(txsj) || StringUtils.isBlank(jgid)) {
                response.isSuccess = false;
                response.message = "[获取对照数据失败]传入参数错误!";
            }
            if (dzlx.equals("2")) {
                relativeItemList = service.getRiskDataList(txsj, zyh, jgid);
            } else if (dzlx.equals("5")) {
                relativeItemList = service.getLifeSignDataList(txsj, zyh, jgid);
            }
            response.isSuccess = true;
            response.datalist = relativeItemList;
            response.message = "获取对照数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取对照数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取对照数据失败]服务内部错误!";
        }
        return response;
    }


    private HandOverRecord getFinalHandOverRecord(HandOverRecord handOverRecord, String jjqh) {
        handOverRecord.JLSJ = timeService.now(DataSource.MOB);
        if (StringUtils.isBlank(handOverRecord.JLXH) || handOverRecord.JLXH.equals("0")) {
            handOverRecord.JLXH = "add_" + String.valueOf(identityService.getIdentityMax("IENR_JJDJL",DataSource.MOB));
        }
        if (jjqh.equals("1")) {
            handOverRecord.JJNR = "[交接内容]" + ":";
        } else if (jjqh.equals("2")) {
            handOverRecord.JJNR += "[差异内容]" + ":";
        }
        HandOverForm handOverForm = jjqh.equals("1")
                ? handOverRecord.HandOverFormBefore
                : handOverRecord.HandOverFormAfert;
        for (HandOverClassify classify : handOverForm.HandOverClassifyList) {
            for (HandOverProject project : classify.HandOverProjectList) {
                if (project.ISMODIFY) {
                    if (project.ISSELECT) {
                        if (StringUtils.isBlank(project.JLXM) || project.JLXM.equals("0")) {
                            project.JLXM = "add_" + String.valueOf(identityService.getIdentityMax("IENR_JJDJLXM",DataSource.MOB));
                        }
                        project.JLXH = handOverRecord.JLXH.replace("add_", "");
                        project.YSXM = project.XMBS;
                        project.JJQH = jjqh;
                        project.XMHZ = "";
                        String qzwb = StringUtils.isBlank(project.QZWB) ? project.XMMC : project.QZWB;
                        qzwb = qzwb.contains(":") ? qzwb : qzwb + ":";
                        qzwb = qzwb.trim();
                        String hzwb = StringUtils.isBlank(project.HZWB) ? "" : project.HZWB;
                        hzwb = hzwb.trim();
                        for (HandOverOption option : project.HandOverOptionList) {
                            if (option.ISMODIFY) {
                                if (option.ISSELECT) {
                                    if (StringUtils.isBlank(option.JLXX) || option.JLXX.equals("0")) {
                                        option.JLXX = "add_" + String.valueOf(identityService.getIdentityMax("IENR_JJDJLXX",DataSource.MOB));
                                    }
                                    option.JLXM = project.JLXM.replace("add_", "");
                                    option.JLXH = handOverRecord.JLXH.replace("add_", "");
                                    option.YSXX = StringUtils.isBlank(option.XXBS) ? "0" : option.XXBS;
                                    option.DZLX = StringUtils.isBlank(option.DZLX) ? "0" : option.DZLX;
                                    option.DZBDJL = StringUtils.isBlank(option.DZBDJL) ? "0" : option.DZBDJL;
                                    project.XMHZ += option.XXNR + ",";
                                }
                            }
                        }
                        if (!StringUtils.isBlank(project.XMHZ)) {
                            project.XMHZ = project.XMHZ.substring(0, project.XMHZ.length() - 1);
                            project.XMHZ = qzwb + project.XMHZ + hzwb + ";";
                            if (!handOverRecord.JJNR.contains(project.XMHZ)) {
                                handOverRecord.JJNR += project.XMHZ;
                            }
                        }
                    }
                }
            }
        }

        return handOverRecord;
    }

    /**
     * 注意：调用此方法之前必须显式指定启用哪个事务
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean finalDelHandOverRecord(String jlxh)
            throws SQLException, DataAccessException {
//        boolean isSucess = service.delHandOverOptionByJlxh(jlxh) > 0;
//        if (!isSucess) {
//            return isSucess;
//        }
//        isSucess = service.delHandOverProjectByJlxh(jlxh) > 0;
//        if (!isSucess) {
//            return isSucess;
//        }
//        isSucess = service.delHandOverRecord(jlxh) > 0;

        return service.delHandOverRecord(jlxh) > 0;
    }

    /**
     * 注意：调用此方法之前必须显式指定启用哪个事务
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean addHandOverRecord(HandOverRecord handOverRecord)
            throws SQLException, DataAccessException {
        handOverRecord.JLXH = handOverRecord.JLXH.replace("add_", "");
        boolean isSucess = service.addHandOverRecord(handOverRecord) > 0;
        if (!isSucess) {
            return isSucess;
        }
        for (HandOverClassify classify : handOverRecord.HandOverFormBefore.HandOverClassifyList) {
            for (HandOverProject project : classify.HandOverProjectList) {
                if (project.ISMODIFY) {
                    if (project.ISSELECT) {
                        if (project.JLXM.contains("add_")) {//新增 项目
                            project.JLXM = project.JLXM.replace("add_", "");
                            isSucess = service.addHandOverProject(project) > 0;
                            if (!isSucess) {
                                return isSucess;
                            }
                            for (HandOverOption option : project.HandOverOptionList) {
                                if (option.ISMODIFY) {
                                    if (option.ISSELECT) {
                                        if (option.JLXX.contains("add_")) {//新增选项
                                            option.JLXX = option.JLXX.replace("add_", "");
                                            isSucess = service.addHandOverOption(option) > 0;
                                            if (!isSucess) {
                                                return isSucess;
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return isSucess;
    }

    /**
     * 注意：调用此方法之前必须显式指定启用哪个事务
     */
    @Transactional(rollbackFor = Exception.class)
    private boolean editHandOverRecord(HandOverRecord handOverRecord, String jjqh)
            throws SQLException, DataAccessException {
        boolean isSucess = jjqh.equals("1") ? service.editHandOverRecordForSender(handOverRecord) > 0
                : service.editHandOverRecordForReceiver(handOverRecord) > 0;
        if (!isSucess) {
            return isSucess;
        }
        HandOverForm handOverForm = jjqh.equals("1") ? handOverRecord.HandOverFormBefore
                : handOverRecord.HandOverFormAfert;
        for (HandOverClassify classify : handOverForm.HandOverClassifyList) {
            for (HandOverProject project : classify.HandOverProjectList) {
                if (project.ISMODIFY) {
                    if (project.ISSELECT) {
                        if (project.JLXM.contains("add_")) {//新增 项目
                            project.JLXM = project.JLXM.replace("add_", "");
                            isSucess = service.addHandOverProject(project) > 0;
                            if (!isSucess) {
                                return isSucess;
                            }
                            for (HandOverOption option : project.HandOverOptionList) {
                                if (option.ISMODIFY) {
                                    if (option.ISSELECT) {
                                        if (option.JLXX.contains("add_")) {//新增 选项
                                            option.JLXX = option.JLXX.replace("add_", "");
                                            isSucess = service.addHandOverOption(option) > 0;
                                            if (!isSucess) {
                                                return isSucess;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {//修改 项目
                            isSucess = service.editHandOverProject(project) > 0;
                            if (!isSucess) {
                                return isSucess;
                            }
                            for (HandOverOption option : project.HandOverOptionList) {
                                if (option.ISMODIFY) {
                                    if (option.ISSELECT) {
                                        if (option.JLXX.contains("add_")) {//新增 选项
                                            option.JLXX = option.JLXX.replace("add_", "");
                                            isSucess = service.addHandOverOption(option) > 0;
                                            if (!isSucess) {
                                                return isSucess;
                                            }
                                        } else {//修改 选项
                                            isSucess = service.editHandOverOption(option) > 0;
                                            if (!isSucess) {
                                                return isSucess;
                                            }
                                        }
                                    } else {//删除 选项
                                        if (!StringUtils.isBlank(option.JLXX)) {
                                            isSucess = service.delHandOverOption(option.JLXX) > 0;
                                            if (!isSucess && jjqh.equals("1")) {
                                                return isSucess;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {//删除 项目
                        if (!StringUtils.isBlank(project.JLXM)) {
                            isSucess = service.delHandOverOptionByJlxm(project.JLXM) > 0;
                            if (!isSucess) {
                                return isSucess;
                            }
                            isSucess = service.delHandOverProject(project.JLXM) > 0;
                            if (!isSucess && jjqh.equals("1")) {
                                return isSucess;
                            }
                        }
                    }
                }
            }
        }
        return isSucess;
    }

    /**
     * 获取交接单模板
     *
     * @param ysxh 样式序号
     * @return
     */
    private HandOverForm getHandOverFormTemplate(String ysxh, String zyh, String txsj, String jgid, String jjqh)
            throws SQLException, DataAccessException {
        HandOverForm handOverForm = service.getHandOverForm(ysxh);
        handOverForm.HandOverClassifyList = service.getHandOverClassifyList(ysxh);
        List<HandOverProject> handOverProjectList = service.getHandOverProjectList(ysxh);
        List<HandOverOption> handOverOptionList = service.getHandOverOptionList(ysxh);
        for (HandOverClassify classify : handOverForm.HandOverClassifyList) {
            List<HandOverProject> projectList = new ArrayList<>();
            for (HandOverProject project : handOverProjectList) {
                if (project.YSFL.equals(classify.YSFL)) {
                    List<HandOverOption> optionList = new ArrayList<>();
                    for (HandOverOption option : handOverOptionList) {
                        if (option.XMBS.equals(project.XMBS)) {
                            optionList.add(option);
                        }
                    }
                    //输入类型主动添加选项 方便前端处理
                    if (optionList.size() == 0 && project.CZLX.equals("1")) {
                        HandOverOption option = new HandOverOption();
                        option.XXBS = "0";
                        option.XMBS = project.XMBS;
                        option.YSXH = project.YSXH;
                        optionList.add(option);
                    }
                    project.HandOverOptionList = optionList;
                    projectList.add(project);
                }
            }
            classify.HandOverProjectList = projectList;
        }
        if (jjqh.equals("1")) {
            getHandOverFormForRelative(handOverForm, zyh, txsj, jgid);
        }
        return handOverForm;
    }

    /**
     * 获取交接前/后数据
     *
     * @param handOverFormTemplate 模板
     * @param jjqh                 1 交接前 2 交接后
     * @return
     */
    private HandOverForm filterHandOverFormTemplate(HandOverForm handOverFormTemplate, String jjqh)
            throws IOException, ClassNotFoundException {
        HandOverForm handOverForm = deepCopyObject(handOverFormTemplate);
        for (HandOverClassify classify : handOverForm.HandOverClassifyList) {
            for (int i = 0; i < classify.HandOverProjectList.size(); i++) {
                HandOverProject projectItem = classify.HandOverProjectList.get(i);
                if (jjqh.equals("1") && projectItem.SYFW.equals("2")) {
                    classify.HandOverProjectList.remove(projectItem);
                    i--;
                    continue;
                }
                if (jjqh.equals("2") && projectItem.SYFW.equals("1")) {
                    classify.HandOverProjectList.remove(projectItem);
                    i--;
                    continue;
                }
            }
        }
        return handOverForm;
    }

    private void getHandOverFormForRelative(HandOverForm handOverForm, String zyh, String txsj, String jgid)
            throws SQLException, DataAccessException {
        if (!StringUtils.isBlank(zyh) && !StringUtils.isBlank(txsj) && !StringUtils.isBlank(jgid)) {
            List<RelativeItem> riskList = service.getRiskDataList(txsj, zyh, jgid);
            List<RelativeItem> lifeSignList = service.getLifeSignDataList(txsj, zyh, jgid);
            for (HandOverClassify classify : handOverForm.HandOverClassifyList) {
                for (HandOverProject project : classify.HandOverProjectList) {
                    if (StringUtils.isBlank(project.DZLX)) {
                        continue;
                    }
                    if (project.CZLX.equals("1")) {
                        HandOverOption option = project.HandOverOptionList.get(0);
                        if (project.DZLX.equals("2")) {
                            for (RelativeItem item : riskList) {
                                if (project.DZBD.equals(String.valueOf(item.ID))) {
                                    option.DZLX = item.DZLX;
                                    option.DZBDJL = item.DZBDJL;
                                    option.XXNR = item.VALUE;
                                    option.ISMODIFY = true;
                                    option.ISSELECT = true;
                                    project.ISMODIFY = true;
                                    project.ISSELECT = true;
                                    break;
                                }
                            }
                        } else if (project.DZLX.equals("5")) {
                            for (RelativeItem item : lifeSignList) {
                                if (project.DZXM.equals(String.valueOf(item.ID))) {
                                    option.DZLX = item.DZLX;
                                    option.DZBDJL = item.DZBDJL;
                                    option.XXNR = item.VALUE;
                                    option.ISMODIFY = true;
                                    option.ISSELECT = true;
                                    project.ISMODIFY = true;
                                    project.ISSELECT = true;
                                    break;
                                }
                            }
                        }

                    } else {
                        for (HandOverOption option : project.HandOverOptionList) {
                            if (project.DZLX.equals("2")) {
                                for (RelativeItem item : riskList) {
                                    if (project.DZBD.equals(String.valueOf(item.ID))) {
                                        option.DZLX = item.DZLX;
                                        option.DZBDJL = item.DZBDJL;
                                        option.XXNR = item.VALUE;
                                        option.ISMODIFY = true;
                                        option.ISSELECT = true;
                                        project.ISMODIFY = true;
                                        project.ISSELECT = true;
                                        break;
                                    }
                                }
                            } else if (project.DZLX.equals("5")) {
                                for (RelativeItem item : lifeSignList) {
                                    if (project.DZXM.equals(String.valueOf(item.ID))) {
                                        option.DZLX = item.DZLX;
                                        option.DZBDJL = item.DZBDJL;
                                        option.XXNR = item.VALUE;
                                        option.ISMODIFY = true;
                                        option.ISSELECT = true;
                                        project.ISMODIFY = true;
                                        project.ISSELECT = true;
                                        break;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private HandOverForm getHandOverForm(HandOverForm handOverFormTemplate, List<HandOverProject> projectList, List<HandOverOption> optionList, String jjqh)
            throws SQLException, DataAccessException, IOException, ClassNotFoundException {
        HandOverForm handOverForm = deepCopyObject(handOverFormTemplate);
        for (HandOverProject project : projectList) {
            if (project.JJQH.equals(jjqh)) {
                List<HandOverOption> handOverOptionList = new ArrayList<>();
                for (HandOverOption option : optionList) {
                    if (option.JLXM.equals(project.JLXM)) {
                        handOverOptionList.add(option);
                    }
                }
                if (optionList.size() == 0) {
                    continue;
                }
                for (HandOverClassify classify : handOverForm.HandOverClassifyList) {
                    if (project.YSFL.equals(classify.YSFL)) {
                        for (HandOverProject projectItem : classify.HandOverProjectList) {
                            if (project.YSXM.equals(projectItem.XMBS)) {
                                projectItem.ISSELECT = true;
                                projectItem.JLXM = project.JLXM;
                                projectItem.JLXH = project.JLXH;
                                projectItem.YSXM = project.YSXM;
                                projectItem.JJQH = project.JJQH;
                                projectItem.XMHZ = project.XMHZ;
                                for (HandOverOption optionItem : projectItem.HandOverOptionList) {
                                    for (HandOverOption option : handOverOptionList) {
                                        if (!option.YSXX.equals("0")) {//非输入类型
                                            if (option.YSXX.equals(optionItem.XXBS)) {
                                                optionItem.ISSELECT = true;
                                                optionItem.JLXX = option.JLXX;
                                                optionItem.JLXM = option.JLXM;
                                                optionItem.JLXH = option.JLXH;
                                                optionItem.YSXX = option.YSXX;
                                                optionItem.XXNR = option.XXNR;
                                                optionItem.DZLX = option.DZLX;
                                                optionItem.DZBDJL = option.DZBDJL;
                                            }
                                        } else {//输入类型
                                            String ysxm = "";
                                            for (HandOverProject tempItem : projectList) {
                                                if (tempItem.JLXM.equals(option.JLXM)) {
                                                    ysxm = tempItem.YSXM;
                                                    break;
                                                }
                                            }
                                            if (!StringUtils.isBlank(ysxm) && ysxm.equals(optionItem.XMBS)) {
                                                optionItem.ISSELECT = true;
                                                optionItem.JLXX = option.JLXX;
                                                optionItem.JLXM = option.JLXM;
                                                optionItem.JLXH = option.JLXH;
                                                optionItem.YSXX = option.YSXX;
                                                optionItem.XXNR = option.XXNR;
                                                optionItem.DZLX = option.DZLX;
                                                optionItem.DZBDJL = option.DZBDJL;
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return handOverForm;

    }

    private HandOverForm getFinalHandOverFormForAfter(HandOverForm handOverFormBefore, HandOverForm handOverFormAfter) {
        for (HandOverClassify classifyBefore : handOverFormBefore.HandOverClassifyList) {
            for (HandOverProject projectBefore : classifyBefore.HandOverProjectList) {
                for (HandOverClassify classifyAfter : handOverFormAfter.HandOverClassifyList) {
                    for (HandOverProject projectAfter : classifyAfter.HandOverProjectList) {
                        if (projectBefore.XMBS.equals(projectAfter.XMBS)) {
                            String xmhzBefore = StringUtils.isBlank(projectBefore.XMHZ) ? "" : projectBefore.XMHZ;
                            String xmhzAfter = StringUtils.isBlank(projectAfter.XMHZ) ? "" : projectAfter.XMHZ;
                            projectAfter.ISDIFFERENT = !xmhzBefore.equals(xmhzAfter);
                        }
                    }
                }
            }
        }
        return handOverFormAfter;
    }

    public <T> List<T> deepCopyList(List<T> list)
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(list);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    public <T> T deepCopyObject(T obj)
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(obj);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        T dest = (T) in.readObject();
        return dest;
    }

}
