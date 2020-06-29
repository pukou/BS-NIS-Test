package com.bsoft.nis.service.nurserecord;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.common.service.dataset.DataSetService;
import com.bsoft.nis.common.servicesup.support.DicServiceSup;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.core.Dictionary;
import com.bsoft.nis.domain.core.cached.Map2;
import com.bsoft.nis.domain.nurserecord.*;
import com.bsoft.nis.domain.nurserecord.db.*;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.domain.synchron.OutArgument;
import com.bsoft.nis.mapper.patient.PatientMapper;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.nurserecord.support.NurseRecordConfigServiceSup;
import com.bsoft.nis.service.nurserecord.support.NurseRecordWriteServiceSup;
import com.bsoft.nis.service.patient.PatientMainService;
import com.bsoft.nis.util.date.DateCompare;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import com.bsoft.nis.util.date.birthday.BirthdayUtil;
import com.bsoft.nis.util.date.pojo.OffCycle;
import com.bsoft.nis.util.string.StringTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Describtion:护理记录书写
 * Created: dragon
 * Date： 2016/10/20.
 */
@Service
public class NurseRecordWriteService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(NurseRecordWriteService.class);

    @Autowired
    DateTimeService timeService; // 数据日期函数

    @Autowired
    NurseRecordWriteServiceSup service; // 护理记录书写

    @Autowired
    DataSetService dataSetService;      // 数据组服务

    @Autowired
    PatientMainService patientService;   // 病人服务

    @Autowired
    PatientMapper patientMapper; // 病人子服务

    @Autowired
    DictCachedHandler cachedHandler;     // 缓存服务

    @Autowired
    DicServiceSup dictService; // 字典服务

    @Autowired
    NurseRecordConfigServiceSup configServiceSup; // 护理记录配置服务

    @Autowired
    IdentityService identservice; // 主键服务

    @Autowired
    SystemParamService systemParamService; // 用户参数服务


    /**
     * 根据结构编号获取护理记录控件列表
     *
     * @param jgbh 结构编号
     * @param zyh  住院号
     * @param jgid 机构ID
     * @return
     */
    public BizResponse<List<StuctrueResponse>> getCtrlListByJgbh(String jgbh, String zyh, String jgid) {
        BizResponse<List<StuctrueResponse>> response = new BizResponse<>();
        BizResponse<Patient> patientBizResponse = new BizResponse<>();
        List<StuctrueResponse> list = new ArrayList<>();
        List<Controll> controlls, activeControlls;
        List<StructurePlugIn> dynamiclist, staticlist, btlist;
        Patient patient = null;
        String nowStr;
        if (StringUtils.isBlank(jgid) || StringUtils.isBlank(zyh) || StringUtils.isBlank(jgbh))
            return response;

        try {
            keepOrRoutingDateSource(DataSource.HRP);
            nowStr = timeService.now(DataSource.PORTAL);
            patientBizResponse = patientService.getPatientByZyh(zyh);
            patient = patientBizResponse.data;
            keepOrRoutingDateSource(DataSource.ENR);

            // 动态列控件
            activeControlls = service.getActivieCtrlListByJgbh(jgid, jgbh, zyh, nowStr);

            // 基本控件
            controlls = service.getCtrlListByJgbh(jgid, jgbh);

            if (controlls.size() > 0) {
                dynamiclist = new ArrayList<>();
                staticlist = new ArrayList<>();
                btlist = new ArrayList<>();

                for (Controll controll : controlls) {
                    StructurePlugIn plugin = new StructurePlugIn();
                    plugin.KJH = Integer.valueOf(controll.getXMBH());
                    plugin.YSBH = controll.getYSBH();
                    plugin.YSLX = controll.getYSLX();
                    plugin.XSMC = controll.getXSMC();
                    plugin.SJLX = Integer.valueOf(controll.getSJLX());
                    plugin.SJGS = controll.getSJGS();
                    plugin.HHJG = controll.getHHJG();
                    plugin.YSZSX = controll.getYXZSX();
                    plugin.YSZXX = controll.getYXZXX();
                    plugin.ZCZSX = controll.getZCZSX();
                    plugin.ZCZXX = controll.getZCZXX();
                    plugin.SFBT = controll.getSFBT();
                    plugin.KJLX = 2;
                    //add 2018-03-05 13:56:13
                    plugin.KSLH = controll.getKSLH();
                    String yskz = controll.getYSKZ();

                    switch (plugin.YSLX) {
                        case "1":  // 文本型
                            break;
                        case "2":  // 列表型
                            // 两种格式：【0（或1）多选分隔符#AAA/BBB/CCC】,第一位表示是否允许多选,分隔符后面是项目名称
                            // 【0#AAA/BBB/CCC$DD/EE/FF】,第一位表示是否允许多选,#分隔符后面是项目名称,$分隔符后面是项目说明
                            String[] datagroup = yskz.split("#");
                            String[] datagroup2 = null;
                            if (datagroup.length >= 2) {
                                plugin.SFDX = datagroup[0].substring(0, 1);
                                plugin.DXFG = plugin.SFDX.equals("1") ? datagroup[0].substring(1, datagroup[0].length() - 1) : "";
                                datagroup2 = datagroup[1].split("\\$");
                                if (datagroup2.length >= 2) {
                                    datagroup = datagroup2[0].split("/");
                                    datagroup2 = datagroup2[1].split("/");
                                } else {
                                    datagroup = datagroup[1].split("/");
                                    datagroup2 = datagroup;
                                }
                            }

                            if (datagroup.length > 0) {
                                plugin.DropdownItem = new ArrayList<>();
                                for (int i = 0; i < datagroup.length; i++) {
                                    String value = datagroup[i];
                                    String xznr;
                                    if (i < datagroup2.length) {
                                        xznr = datagroup2[i];
                                    } else {
                                        xznr = value;
                                    }
                                    DropItem dropItem = new DropItem();
                                    dropItem.VALUE = value;
                                    dropItem.XZNR = xznr;
                                    plugin.DropdownItem.add(dropItem);
                                }
                                plugin.KJLX = 4;
                            } else {
                                plugin.SFDX = "";
                            }
                            break;
                        case "3":  // 引用型
                            if (yskz.startsWith("1")) {      // 格式：1数据组号#行号#列号#列名
                                yskz = yskz.substring(1);
                                String[] kzarr = yskz.split("#");
                                if (kzarr.length == 3) {
                                    String sjzxh = kzarr[0];
                                    String row = kzarr[1];
                                    String col = kzarr[2];
                                    String value = dataSetService.getDataSetValue(sjzxh, zyh, row, col);
                                    plugin.KJNR = value;

                                    // 当前年龄
                                    if (patient != null) {
                                        String csny = patient.CSNY;
                                        String ryrq = patient.RYRQ;
                                        if (col.equals("DQNL"))
                                            plugin.KJNR = BirthdayUtil.getAgesPairCommonStrOnlySui(csny, nowStr);

                                        // 入院年龄
                                        if (col.equals("RYNL"))
                                            plugin.KJNR = BirthdayUtil.getAgesPairCommonStrOnlySui(csny, ryrq);
                                    }
                                }
                            } else if (yskz.startsWith("2")) { // SQL语句型

                            } else {

                            }
                            break;
                        case "4":  // 特殊型
                            if ("服务器时间".equals(yskz)) {
                                plugin.KJNR = nowStr;

                            } else if ("产程开始时间".equals(yskz)) {
                                plugin.KJNR = "";

                            } else if (yskz.startsWith("453")) {  // 风险
                                String[] ys = yskz.split("#");
                                if (ys.length < 2)
                                    break;
                                plugin.YSLX = ys[0];
                                plugin.YSKZ = ys[1];
                                Association ass = getAssociation(zyh, "453", ys[1], 0, jgid);
                                plugin.PageIndex = 0;
                                plugin.PageSize = ass.getPageSize();
                                plugin.RefrenceValue = ass.getValues();
                            }
                            break;
                        case "5":  // 字典型
                            if (!StringUtils.isBlank(yskz)) {
                                String[] zd = yskz.split("#");
                                if (zd.length > 0) {
                                    List<Map2> dicts = cachedHandler.getCachedDatasContainKeyValue(zd[0], jgid);
                                    for (Map2 map2 : dicts) {
                                        plugin.DropdownItem.add(new DropItem(map2.key, map2.value));
                                    }
                                    plugin.KJLX = 4;
                                }
                            }
                            break;
                        case "6":  // 体征型
                            plugin.YSKZ = yskz;
                            Association sign = getAssociation(zyh, "6", yskz, 0, jgid);
                            plugin.PageIndex = 0;
                            plugin.PageSize = sign.getPageSize();
                            plugin.RefrenceValue = sign.getValues();
                            break;
                        default:
                            break;
                    }

                    // 活动标识
                    if (controll.getHDBZ().equals("1")) {
                        plugin.SFXS = "0";
                        for (Controll controll1 : activeControlls) {
                            if (String.valueOf(plugin.KJH).equals(controll.getXMBH())) {
                                plugin.SFXS = "1";
                            }
                        }
                        dynamiclist.add(plugin);
                    }
                    //else if ("1".equals(controll.getSFBT())) {
                    else if (!"0".equals(controll.getYMCLFS())) {
                        // change by louis 2018-03-05 15:29:21
                        plugin.SFXS = "1";
                        btlist.add(plugin);
                        //plugin.SFXS = Integer.valueOf(controll.getKSLH()) > 50 ? "0" : "1";
                    } else {
                        plugin.SFXS = "1";
                        staticlist.add(plugin);
                    }
                }


                // 返回控件组
                // 必填项目控件
                StuctrueResponse btgroup = new StuctrueResponse();
                btgroup.LBH = "0";
                btgroup.LBMC = "必填列";
                btgroup.ZLX = "必填列";
                btgroup.NRControllist = btlist;
                list.add(btgroup);
                // 静态项目控件
                StuctrueResponse jtgroup = new StuctrueResponse();
                jtgroup.LBH = "1";
                jtgroup.LBMC = "静态列";
                jtgroup.ZLX = "静态列";
                jtgroup.NRControllist = staticlist;
                list.add(jtgroup);
                // 动态项目控件
                StuctrueResponse dyngroup = new StuctrueResponse();
                dyngroup.LBH = "2";
                dyngroup.LBMC = "动态列";
                dyngroup.ZLX = "动态列";
                dyngroup.NRControllist = dynamiclist;
                list.add(dyngroup);
            }

            // 确定是否是否换页(转科换页)
            if (controlls.size() > 0) {
                if (confirmChangePage(zyh, jgbh, jgid)) {
                    for (StuctrueResponse struct : list) {
                        struct.HYBZ = "1";
                    }
                } else {
                    for (StuctrueResponse struct : list) {
                        struct.HYBZ = "0";
                    }
                }
            }


            response.isSuccess = true;
            response.data = list;
            response.message = "获取记录记录控件成功!";

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录控件列表]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录控件列表]服务内部错误!";
        }
        return response;
    }

    /**
     * 根据记录编号获取护理记录控件列表
     *
     * @param zyh
     * @param jlbh
     * @param jgid
     * @param sysType
     * @return
     */
    public BizResponse<List<StuctrueResponse>> getCtrlListByJlbh(String zyh, String jlbh, String jgid, int sysType) {
        BizResponse<List<StuctrueResponse>> response = new BizResponse<>();
        BizResponse<Patient> patientBizResponse = new BizResponse<>();
        List<StuctrueResponse> list = new ArrayList<>();
        List<Controll> controlls, activeControlls;
        List<StructurePlugIn> dynamiclist, staticlist, btlist;
        Patient patient = null;

        String nowStr;
        if (StringUtils.isBlank(jgid) || StringUtils.isBlank(zyh) || StringUtils.isBlank(jlbh))
            return response;

        try {
            keepOrRoutingDateSource(DataSource.HRP);
            nowStr = timeService.now(DataSource.PORTAL);
            patientBizResponse = patientService.getPatientByZyh(zyh);
            patient = patientBizResponse.data;
            keepOrRoutingDateSource(DataSource.ENR);

            // 获取主记录
            List<NRData> nrDatas = service.getNRData(jlbh, jgid);
            if (nrDatas.size() <= 0) {
                response.isSuccess = false;
                response.data = list;
                response.message = "获取JL01数据失败!";
                return response;
            }

            // 动态列控件
            activeControlls = service.getActivieCtrlListByJlbh(jgid, jlbh, zyh, nowStr);

            // 基本控件
            controlls = service.getCtrlListByJlbh(jgid, jlbh);

            if (controlls.size() > 0) {
                dynamiclist = new ArrayList<>();
                staticlist = new ArrayList<>();
                btlist = new ArrayList<>();

                for (Controll controll : controlls) {
                    StructurePlugIn plugin = new StructurePlugIn();
                    plugin.KJH = Integer.valueOf(controll.getXMBH());
                    plugin.YSBH = controll.getYSBH();
                    plugin.YSLX = controll.getYSLX();
                    plugin.XSMC = controll.getXSMC();
                    plugin.SJLX = Integer.valueOf(controll.getSJLX());
                    plugin.SJGS = controll.getSJGS();
                    plugin.HHJG = controll.getHHJG();
                    plugin.YSZSX = controll.getYXZSX();
                    plugin.YSZXX = controll.getYXZXX();
                    plugin.ZCZSX = controll.getZCZSX();
                    plugin.ZCZXX = controll.getZCZXX();
                    plugin.SFBT = controll.getSFBT();
                    plugin.KJLX = 2;
                    plugin.KJNR = controll.getXMQZ();
                    //add 2018-03-05 13:56:13
                    plugin.KSLH = controll.getKSLH();
                    String yskz = controll.getYSKZ();

                    switch (plugin.YSLX) {
                        case "1":  // 文本型
                            break;
                        case "2":  // 列表型
                            // 两种格式：【0（或1）多选分隔符#AAA/BBB/CCC】,第一位表示是否允许多选,分隔符后面是项目名称
                            // 【0#AAA/BBB/CCC$DD/EE/FF】,第一位表示是否允许多选,#分隔符后面是项目名称,$分隔符后面是项目说明
                            String[] datagroup = yskz.split("#");
                            String[] datagroup2 = null;
                            if (datagroup.length >= 2) {
                                plugin.SFDX = datagroup[0].substring(0, 1);
                                plugin.DXFG = plugin.SFDX.equals("1") ? datagroup[0].substring(1, datagroup[0].length() - 1) : "";
                                datagroup2 = datagroup[1].split("\\$");
                                if (datagroup2.length >= 2) {
                                    datagroup = datagroup2[0].split("/");
                                    datagroup2 = datagroup2[1].split("/");
                                } else {
                                    datagroup = datagroup[1].split("/");
                                    datagroup2 = datagroup;
                                }
                            }

                            if (datagroup.length > 0) {
                                plugin.DropdownItem = new ArrayList<>();
                                for (int i = 0; i < datagroup.length; i++) {
                                    String value = datagroup[i];
                                    String xznr;
                                    if (i < datagroup2.length) {
                                        xznr = datagroup2[i];
                                    } else {
                                        xznr = value;
                                    }
                                    DropItem dropItem = new DropItem();
                                    dropItem.VALUE = value;
                                    dropItem.XZNR = xznr;
                                    plugin.DropdownItem.add(dropItem);
                                }
                                plugin.KJLX = 4;
                            } else {
                                plugin.SFDX = "";
                            }
                            break;
                        case "3":  // 引用型
                            if (yskz.startsWith("1")) {      // 格式：1数据组号#行号#列号#列名
                                yskz = yskz.substring(1);
                                String[] kzarr = yskz.split("#");
                                if (kzarr.length == 3) {
                                    String sjzxh = kzarr[0];
                                    String row = kzarr[1];
                                    String col = kzarr[2];
                                    String value = dataSetService.getDataSetValue(sjzxh, zyh, row, col);
                                    plugin.KJNR = value;

                                    // 当前年龄
                                    if (patient != null) {
                                        String csny = patient.CSNY;
                                        String ryrq = patient.RYRQ;
                                        if (col.equals("DQNL"))
                                            plugin.KJNR = BirthdayUtil.getAgesPairCommonStrOnlySui(csny, nowStr);

                                        // 入院年龄
                                        if (col.equals("RYNL"))
                                            plugin.KJNR = BirthdayUtil.getAgesPairCommonStrOnlySui(csny, ryrq);
                                    }
                                }
                            } else if (yskz.startsWith("2")) { // SQL语句型

                            } else {

                            }
                            break;
                        case "4":  // 特殊型
                            if ("服务器时间".equals(yskz)) {
                                plugin.KJNR = nowStr;

                            } else if ("产程开始时间".equals(yskz)) {
                                plugin.KJNR = "";

                            } else if (yskz.startsWith("453")) {  // 风险
                                String[] ys = yskz.split("#");
                                if (ys.length < 2)
                                    break;
                                plugin.YSLX = ys[0];
                                plugin.YSKZ = ys[1];
                                Association ass = getAssociation(zyh, "453", ys[1], 0, jgid);
                                plugin.PageIndex = 0;
                                plugin.PageSize = ass.getPageSize();
                                plugin.RefrenceValue = ass.getValues();
                            }
                            break;
                        case "5":  // 字典型
                            if (!StringUtils.isBlank(yskz)) {
                                String[] zd = yskz.split("#");
                                if (zd.length > 0) {
                                    List<Map2> dicts = cachedHandler.getCachedDatasContainKeyValue(zd[0], jgid);
                                    for (Map2 map2 : dicts) {
                                        plugin.DropdownItem.add(new DropItem(map2.key, map2.value));
                                    }
                                }
                                plugin.KJLX = 4;
                            }
                            break;
                        case "6":  // 体征型
                            plugin.YSKZ = yskz;
                            Association sign = getAssociation(zyh, "6", yskz, 0, jgid);
                            plugin.PageIndex = 0;
                            plugin.PageSize = sign.getPageSize();
                            plugin.RefrenceValue = sign.getValues();
                            break;
                        default:
                            break;
                    }

                    // 活动标识
                    if (controll.getHDBZ().equals("1")) {
                        plugin.SFXS = "0";
                        for (Controll controll1 : activeControlls) {
                            if (String.valueOf(plugin.KJH).equals(controll.getXMBH())) {
                                plugin.SFXS = "1";
                            }
                        }
                        dynamiclist.add(plugin);
                    }
                    //else if ("1".equals(controll.getSFBT())) {
                    else if(!"0".equals(controll.getYMCLFS())){
                        // change by louis 2018-03-05 15:29:21
                        plugin.SFXS = "1";
                        btlist.add(plugin);
                        //标准版开始列号大于50当做页眉，故不显示
                        //plugin.SFXS = Integer.valueOf(controll.getKSLH()) > 50 ? "0" : "1";
                    } else {
                        plugin.SFXS = "1";
                        staticlist.add(plugin);
                    }
                }

                // 返回控件组
                // 必填项目控件
                StuctrueResponse btgroup = new StuctrueResponse();
                btgroup.LBH = "0";
                btgroup.LBMC = "必填列";
                btgroup.ZLX = "必填列";
                btgroup.NRControllist = btlist;
                list.add(btgroup);
                // 静态项目控件
                StuctrueResponse jtgroup = new StuctrueResponse();
                jtgroup.LBH = "1";
                jtgroup.LBMC = "静态列";
                jtgroup.ZLX = "静态列";
                jtgroup.NRControllist = staticlist;
                list.add(jtgroup);
                // 动态项目控件
                StuctrueResponse dyngroup = new StuctrueResponse();
                dyngroup.LBH = "2";
                dyngroup.LBMC = "动态列";
                dyngroup.ZLX = "动态列";
                dyngroup.NRControllist = dynamiclist;
                list.add(dyngroup);
            }

            // 确定是否是否换页(转科换页)
            if (controlls.size() > 0) {
                for (StuctrueResponse struct : list) {
                    struct.HYBZ = nrDatas.get(0).HYBZ;
                }
            }

            response.isSuccess = true;
            response.data = list;
            response.message = "获取记录记录控件成功!";

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录控件列表]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录控件列表]服务内部错误!";
        }

        return response;
    }

    /**
     * 获取特殊类型组合数据
     *
     * @param zyh
     * @param yslx
     * @param yskz
     * @param pageIndex
     * @param jgid
     * @return
     */
    public Association getAssociation(String zyh, String yslx, String yskz, int pageIndex, String jgid) {
        Association association = new Association();
        String sum = "0";

        if (yslx.equals("6")) {
            // 获取生命体征记录
            association.setValues(getLifeSignRefrece(zyh, pageIndex, jgid, yskz));
            association.setPageIndex(pageIndex);

            // 计算记录页数
            List<Map> counts1 = getLifeSignRecordSum(zyh, yskz, jgid);
            if (counts1.size() > 0) {
                sum = String.valueOf(((Map) counts1.get(0)).get("TOTAL"));
            }
            Double size1 = Double.parseDouble(sum) / 5;

            association.setPageSize((int) Math.ceil(size1));

        } else if (yslx.equals("453")) {
            // 获取风险评估记录
            association.setValues(getDangerRefrence(zyh, pageIndex, jgid));
            association.setPageIndex(pageIndex);
            // 计算记录页数
            List<Map> counts = getDangerRecordSum(zyh);
            if (counts.size() > 0) {
                sum = String.valueOf(((Map) counts.get(0)).get("TOTAL"));
            }
            Double size = Double.parseDouble(sum) / 5;
            association.setPageSize((int) Math.ceil(size));
        }
        return association;
    }

    /**
     * 获取生命体征引用数据
     *
     * @param zyh
     * @param pageIndex
     * @param jgid
     * @return
     */
    public List<RefrenceValue> getLifeSignRefrece(String zyh, int pageIndex, String jgid, String xmh) {
        keepOrRoutingDateSource(DataSource.ENR);
        List<RefrenceValue> list = new ArrayList<>();
        try {
            list = service.getLifeSignRefrece(zyh, pageIndex, jgid, xmh);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    /*
      升级编号【56010022】============================================= start
      护理记录:可以查看项目最近3次的记录，可以选择其中一次的数据到当前的护理记录单上。
      ================= Classichu 2017/10/18 10:41
      */
    public BizResponse<LastDataBean> getlastXMData(String zyh, String xmbh,String hsgh, String jgid) {
        keepOrRoutingDateSource(DataSource.ENR);
        List<LastDataBean> list = new ArrayList<>();
        BizResponse<LastDataBean> bizResponse = new BizResponse<>();
        try {
            list = service.getlastXMData(jgid, xmbh, zyh,hsgh);
            bizResponse.isSuccess = true;
            bizResponse.datalist = list;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return bizResponse;
    }
    /* =============================================================== end */

    /**
     * 获取风险评估引用数据
     *
     * @param zyh
     * @param pageIndex
     * @param jgid
     * @return
     */
    public List<RefrenceValue> getDangerRefrence(String zyh, int pageIndex, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        List<RefrenceValue> list = new ArrayList<>();
        try {
            list = service.getDangerRefrence(zyh, pageIndex, jgid);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    /**
     * 获取风险记录总数
     *
     * @param zyh
     * @return
     */
    public List<Map> getDangerRecordSum(String zyh) {
        keepOrRoutingDateSource(DataSource.MOB);
        List<Map> list = new ArrayList<>();

        try {
            list = service.getDangerRecordSum(zyh);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return list;
    }

    /**
     * 获取生命体征记录总数
     *
     * @param zyh
     * @param yskz
     * @param jgid
     * @return
     */
    private List<Map> getLifeSignRecordSum(String zyh, String yskz, String jgid) {
        keepOrRoutingDateSource(DataSource.ENR);
        List<Map> list = new ArrayList<>();

        try {
            list = service.getLifeSignRecordSum(zyh, yskz, jgid);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return list;
    }

    /**
     * 获取护理记录目录
     *
     * @param zyh
     * @param mblx
     * @param jgid
     * @return
     */
    public BizResponse<List<NRTree>> getNRTreeByMblx(String zyh, String mblx, String jgid) {
        keepOrRoutingDateSource(DataSource.ENR);
        BizResponse<List<NRTree>> response = new BizResponse<>();

        List<NRDbTree> trees = new ArrayList<>();
        List<NRTree> ret_trees = new ArrayList<>();

        try {
            if (StringUtils.isEmpty(mblx)) {
                trees = service.getNRTree(zyh, jgid);
            } else {
                trees = service.getNRTreeByMblx(zyh, jgid, mblx);
            }

            if (trees.size() > 0) {
                for (int i = 0; i < trees.size(); i++) {
                    NRDbTree dbtree = trees.get(i);
                    NRTree tree = new NRTree();
                    tree.XSNR = dbtree.JGMC;
                    tree.LBBH = dbtree.BLLB;
                    tree.SHZT = false;
                    tree.WCZT = false;
                    tree.JGBH = dbtree.JGBH;
                    tree.JLSJ = dbtree.JLSJ;
                    //ADD
                    tree.SXHS = dbtree.SXHS;
                    Boolean isfirst = true;
                    while (i < trees.size()) {
                        dbtree = trees.get(i);
                        NRTree treechild = new NRTree();
                        if (!isfirst) {
                            if (DateCompare.isEqual(dbtree.JLSJ, trees.get(i - 1).JLSJ)) {
                                treechild.XSNR = dbtree.JLMC;
                                treechild.DOFR = false;
                            } else {
                                treechild.XSNR = DateConvert.getDateMonth(DateConvert.toDateTime(dbtree.JLSJ)) + "-" + DateConvert.getDateDay(DateConvert.toDateTime(dbtree.JLSJ)) + " " + dbtree.JLMC;
                                treechild.DOFR = true;
                            }
                        } else {
                            treechild.XSNR = DateConvert.getDateMonth(DateConvert.toDateTime(dbtree.JLSJ)) + "-" + DateConvert.getDateDay(DateConvert.toDateTime(dbtree.JLSJ)) + " " + dbtree.JLMC;
                            treechild.DOFR = true;
                            isfirst = false;
                        }

                        treechild.JLSJ = DateConvert.toString(dbtree.JLSJ);
                        treechild.LBBH = dbtree.BLLB;
                        treechild.WCZT = !(dbtree.WCQM.equals("0"));
                        treechild.SHZT = !(dbtree.SYBZ.equals("0"));
                        treechild.JLBH = dbtree.JLBH;
                        treechild.JGBH = dbtree.JGBH;
                        //ADD
                        treechild.SXHS = dbtree.SXHS;

                        tree.ZML.add(treechild);
                        i++;

                        if (i < trees.size()) {
                            if (!trees.get(i).JGBH.equals(trees.get(i - 1).JGBH)) {
                                i--;
                                break;
                            }
                        }
                    }
                    ret_trees.add(tree);
                }
            }
            response.data = ret_trees;
            response.isSuccess = true;
            response.message = "获取记录目录成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录目录]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录目录]服务内部错误!";
        }

        return response;
    }

    /**
     * 根据住院号和结构编号获取记录目录
     *
     * @param zyh
     * @param jgbh
     * @return
     */
    public BizResponse<List<Map>> getNRTreeByZYHAndJGBH(String zyh, String jgbh) {
        keepOrRoutingDateSource(DataSource.ENR);
        BizResponse<List<Map>> response = new BizResponse<>();

        try {
            response.data = service.getNRTreeByZYHAndJGBH(zyh, jgbh);
            response.isSuccess = true;
            response.message = "获取护理记录目录成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录目录]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录目录]服务内部错误!";
        }
        return response;
    }

    /**
     * 保存护理记录
     *
     * @param record
     * @return
     */
    public BizResponse<String> saveNurseRecord(NRRecordRequest record) {
        BizResponse<String> response = new BizResponse<>();
        BizResponse<Long> identResponse = new BizResponse<>();

        if (record == null) {
            response.isSuccess = false;
            response.message = "记录内容为null";
            return response;
        }
        if (StringUtils.isEmpty(record.JGID) || StringUtils.isEmpty(record.YHID)) {
            response.isSuccess = false;
            response.message = "机构ID或用户ID不可为空";
            return response;
        }

        try {
            // 获取护理记录模板
            keepOrRoutingDateSource(DataSource.ENR);
            List<Template> templates = configServiceSup.getNurseRecordTemplateByJgbh(record.JGBH);
            // 获取病人信息
            keepOrRoutingDateSource(DataSource.HRP);
            Patient patient = patientMapper.getPatientByZyh(record.ZYH);
            // 获取ENR_JL01主键
            identResponse = identservice.getIdentityMax("ENR_JL01", 1, DataSource.ENR);

            if (templates.size() <= 0 || patient == null) {
                response.isSuccess = false;
                response.message = "保存时未找到该患者!";
                return response;
            }
            if (!identResponse.isSuccess) {
                response.isSuccess = false;
                response.message = "主键获取错误";
                return response;
            }

            Long jlbh = identResponse.datalist.get(0);

            // 组装ENR_JL01数据
            Template template = templates.get(0);
            NRData saveData = new NRData();
            saveData.JLBH = jlbh;
            saveData.ZYH = patient.ZYH;
            saveData.JGBH = template.JGBH;
            saveData.BLLB = template.BLLB;
            saveData.BLLX = "2";
            saveData.MBLB = template.MBLB;
            saveData.MBBH = "0";
            saveData.SXBQ = patient.BRBQ;
            saveData.SXHS = record.YHID;
            saveData.JLSJ = saveData.SXSJ = saveData.XTSJ = record.JLSJ;

            saveData.JLMC = String.format("%tR", DateConvert.toDateTime(record.JLSJ));
            saveData.DLHHBZ = "0";
            saveData.ZJLX = "0";
            saveData.JLHS = "-1";
            saveData.JGID = record.JGID;
            saveData.HYBZ = record.HHBZ;

            // 组装ENR_JLML数据
            if (StringUtils.isEmpty(record.ZYH) || StringUtils.isEmpty(template.BLLB)) {
                response.isSuccess = false;
                response.message = "住院号或病历类别为空!";
                return response;
            }
            keepOrRoutingDateSource(DataSource.ENR);
            List<Map> jlmls = service.getNRTreeByZYHAndJGBH(patient.ZYH, template.JGBH);

            // 该结构在记录目录中不存在的情况插入一条数据
            List<NRContent> contents = new ArrayList<>();
            String treeJlxh = String.valueOf(identservice.getIdentityMax("ENR_JLML", DataSource.ENR));
            if (StringUtils.isEmpty(treeJlxh)) {
                response.isSuccess = false;
                response.message = "记录目录主键获取错误!";
                return response;
            }
            if (jlmls.size() <= 0) {
                NRContent content = new NRContent();
                content.JLXH = Long.valueOf(treeJlxh);
                content.ZYH = Long.valueOf(patient.ZYH);
                content.BLLX = 2;
                content.BLLB = Long.valueOf(template.BLLB);
                content.JGBH = Long.valueOf(template.JGBH);
                content.JGID = (record.JGID);
                content.JLYS = 0;
                content.PLCX = 0;
                contents.add(content);
            }

            // 组装ENR_JL02数据
            List<NRItem> nrItems = new ArrayList<>();
            keepOrRoutingDateSource(DataSource.ENR);
            List<NRItem> items = configServiceSup.getNurseReocrdItemByJgbh(template.JGBH, record.JGID);
            List<NRRecordItemRequest> itemsrequest = record.ItemList;
            for (NRRecordItemRequest requestItem : itemsrequest) {
                Boolean isExsit = false;
                NRItem item1 = null;
                for (NRItem nrItem : items) {
                    if (nrItem.XMBH.equals(String.valueOf(requestItem.XMBH))) {
                        isExsit = true;
                        item1 = nrItem;
                        break;
                    }
                }
                if (isExsit) {
                    NRItem item = new NRItem();
                    item.MXBH = identservice.getIdentityMax("ENR_JL02", DataSource.ENR);
                    item.JLBH = jlbh;
                    item.XMBH = item1.XMBH;
                    item.XMMC = item1.XMMC;
                    item.XSMC = item1.XSMC;
                    item.XMQZ = requestItem.VALUE;
                    item.KSLH = item1.KSLH;
                    item.JSLH = item1.JSLH;
                    item.HDBZ = item1.HDBZ;
                    //item.YMCLFS = item1.KSLH > 50 ? "1" : item1.YMCLFS;
                    item.YMCLFS = item1.YMCLFS;
                    item.HHJG = item1.HHJG;
                    item.XGBZ = "0";
                    item.JGID = record.JGID;
                    // 来源表单
                    item.LYBD = requestItem.LYBD;
                    item.LYBH = requestItem.LYBH;
                    item.LYMX = requestItem.LYMX;
                    item.LYMXLX = requestItem.LYMXLX;
                    nrItems.add(item);
                }
            }

            // 保存数据
            keepOrRoutingDateSource(DataSource.ENR);
            service.saveNurseRecord(saveData, nrItems, contents);
            response.data = String.valueOf(jlbh);
            response.isSuccess = true;
            response.message = "保存记录成功!";

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录保存]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录保存]服务内部错误!";
        }
        return response;
    }

    /**
     * 更新护理记录
     *
     * @param record
     * @return
     */
    public BizResponse<String> updateNurseRecord(NRRecordRequest record) {
        BizResponse<String> response = new BizResponse<>();
        List<NRData> updatePrimaryRecords = new ArrayList<>(); // 更新主记录(ENR_JL01)列表
        List<NRItem> insertItems = new ArrayList<>(); // 明细(ENR_JL02)-添加列表
        List<NRItem> updateItems = new ArrayList<>(); // 明细(ENR_JL02)-更新列表
        List<NRContent> insertContents = new ArrayList<>(); // 记录目录(ENR_JLML)-添加列表

        if (record == null) {
            response.isSuccess = false;
            response.message = "记录内容为null";
            return response;
        }
        if (StringUtils.isEmpty(record.JGID)) {
            response.isSuccess = false;
            response.message = "用户ID不可为空";
            return response;
        }

        try {
            keepOrRoutingDateSource(DataSource.ENR);
            List<NRData> exsitNRDatas = service.getNRData(record.JLBH, record.JGID);
            if (exsitNRDatas.size() <= 0) {
                response.isSuccess = false;
                response.message = "当前更新的记录不存在!";
                return response;
            }
            NRData nrdata = exsitNRDatas.get(0);
            if (StringUtils.isEmpty(String.valueOf(nrdata.JLBH)) || nrdata.JLBH == 0L) {
                response.isSuccess = false;
                response.message = "当前更新的记录不存在!";
                return response;
            }

            // 1.准备明细数据
            List<NRItem> itemTemplets = configServiceSup.getNurseReocrdItemByJgbh(nrdata.JGBH, record.JGID);
            List<NRRecordItemRequest> items = record.ItemList;
            List<NRItem> dbItems = nrdata.XMLB;
            for (NRRecordItemRequest requestitem : items) {
                Boolean isExsit = false;
                NRItem item1 = null;

                for (NRItem item2 : dbItems) {
                    if (item2.XMBH.equals(String.valueOf(requestitem.XMBH))) {
                        isExsit = true;
                        item1 = item2;
                        break;
                    }
                }
                // 1.1明细更新列表
                if (isExsit) {
                    item1.XMQZ = requestitem.VALUE;
                    //item1.YMCLFS = item1.KSLH > 50 ? "1" : item1.YMCLFS;
                    /*//协和
                    //item1.YMCLFS = item1.KSLH > 50 ? "1" : item1.YMCLFS;*/
                    item1.JGID = record.JGID;
                    updateItems.add(item1);
                } else { // 1.2明细添加列表
                    // 获取护理记录结构
                    Boolean isExsitInTemp = false;
                    NRItem exsitItemInTemp = null;
                    for (NRItem itemTemp : itemTemplets) {
                        if (itemTemp.XMBH.equals(String.valueOf(requestitem.XMBH))) {
                            isExsitInTemp = true;
                            exsitItemInTemp = itemTemp;
                        }
                    }
                    if (isExsitInTemp) {
                        NRItem _item = new NRItem();
                        _item.MXBH = identservice.getIdentityMax("ENR_JL02", DataSource.ENR);
                        _item.JLBH = nrdata.JLBH;
                        _item.XMBH = exsitItemInTemp.XMBH;
                        _item.XMMC = exsitItemInTemp.XMMC;
                        _item.XSMC = exsitItemInTemp.XSMC;
                        _item.XMQZ = requestitem.VALUE;
                        _item.KSLH = exsitItemInTemp.KSLH;
                        _item.JSLH = exsitItemInTemp.JSLH;
                        _item.HDBZ = exsitItemInTemp.HDBZ;
                        //_item.YMCLFS = exsitItemInTemp.KSLH > 50 ? "1" : exsitItemInTemp.YMCLFS;
                        _item.YMCLFS = exsitItemInTemp.YMCLFS;
                        _item.HHJG = exsitItemInTemp.HHJG;
                        _item.XGBZ = "0";
                        _item.JGID = record.JGID;
                        // 来源表单
                        _item.LYBD = requestitem.LYBD;
                        _item.LYBH = requestitem.LYBH;
                        _item.LYMX = requestitem.LYMX;
                        _item.LYMXLX = requestitem.LYMXLX;
                        insertItems.add(_item);
                    }
                }
            }

            // 2.更新记录目录(项目列表为空时跟新目录，不知道为啥 =_= )
            if (nrdata.XMLB.size() <= 0) {
                keepOrRoutingDateSource(DataSource.ENR);
                List<NRContent> contents = service.getNRContents(record.ZYH, nrdata.JGBH, record.JGID);
                if (contents.size() <= 0) {
                    NRContent content = new NRContent();
                    content.JLXH = identservice.getIdentityMax("ENR_JLML", DataSource.ENR);
                    content.ZYH = Long.valueOf(record.ZYH);
                    content.BLLX = Integer.valueOf(nrdata.BLLX);
                    content.BLLB = Long.valueOf(nrdata.BLLB);
                    content.JGBH = Long.valueOf(nrdata.JGBH);
                    content.JGID = record.JGID;
                    content.JLYS = 0;
                    content.PLCX = 0;
                    insertContents.add(content);
                }
            }

            // 3.更新主记录(ENR_JL01)
            if (!StringUtils.isEmpty(record.HHBZ) || !DateCompare.isEqual(nrdata.JLSJ, record.JLSJ)) {
                NRData primaryData = new NRData();
                primaryData.JLBH = nrdata.JLBH;
                primaryData.JLSJ = nrdata.JLSJ;
                primaryData.JLMC = String.format("%tR", DateConvert.toDateTime(record.JLSJ));
                primaryData.HYBZ = record.HHBZ;
                primaryData.ZYH = record.ZYH;
                primaryData.JGBH = record.JGID;
                primaryData.JLHS = "-1";
                updatePrimaryRecords.add(primaryData);
            }

            keepOrRoutingDateSource(DataSource.ENR);
            service.updateNurseRecord(updatePrimaryRecords, insertContents, insertItems, updateItems, record.JLBH);
            response.data = String.valueOf(nrdata.JLBH);
            response.isSuccess = true;
            response.message = "保存成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录更新]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录更新]服务内部错误!";
        }
        return response;
    }

    /**
     * 删除护理记录数据
     *
     * @param zyh      住院号
     * @param jlbh     记录编号
     * @param yhid     用户ID
     * @param yhxm     用户姓名
     * @param ip       ip
     * @param hostname 主机名
     * @param jgid     机构ID
     * @return
     */
    public BizResponse<String> deleteNurseRecord(String zyh, String jlbh, String yhid, String yhxm, String ip, String hostname, String jgid) {
        keepOrRoutingDateSource(DataSource.ENR);
        BizResponse<String> response = new BizResponse<>();
        try {
            // 1.删除护理记录
            service.deleteNurseRecord(jlbh, zyh, jgid);
            response.isSuccess = true;
            response.message = "护理记录删除成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录删除]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录删除]服务内部错误!";
        }
        return response;
    }

    /**
     * 护理记录签名
     *
     * @param data
     * @return
     */
    public BizResponse<String> signNameNurseRecord(SignatureDataRequest data) {
        BizResponse<String> response = new BizResponse<>();
        List<NRData> nrDatas = new ArrayList<>();
        try {
            // 1.判断护理记录状态
            keepOrRoutingDateSource(DataSource.ENR);
            nrDatas = service.getNRData(data.JLBH, null);
            if (nrDatas.size() <= 0) {
                response.isSuccess = false;
                response.message = "签名的记录不存在!";
                return response;
            }
            NRData nrData = nrDatas.get(0);
            String jlzt = nrData.JLZT;
            if (StringUtils.isEmpty(jlzt))
                jlzt = "0";
            if (!jlzt.equals("0")) {
                response.isSuccess = false;
                response.message = "当前护理记录[" + nrData.JLMC + "]处于非书写状态，不能进行完成操作!";
                return response;
            }

            // 3.判断签名类型(0 文字签名  1 图片签名)
            Boolean fileSign = false;
            //List<String> list = systemParamService.getUserParams("1", "ENR", "FileSign", data.JGID, DataSource.MOB).datalist;
            //协和客户化：护理记录模块用户参数在GY_YHCS里
            List<String> list = systemParamService.getUserParams("1", "ENR", "FileSign", data.JGID, DataSource.HRP).datalist;
            if (list != null && !list.isEmpty()) {
                fileSign = "1".equals(list.get(0));
            }

            // 4.签名
            String nowStr = timeService.now(DataSource.PORTAL);
            keepOrRoutingDateSource(DataSource.ENR);
            int ret;
            if (fileSign) {
                // 2.签名权限
                keepOrRoutingDateSource(DataSource.HRP);
                List<EMR_WH_QMXX> qmxxes = service.getSignValids(data.YHID);
                if (qmxxes.size() <= 0) {
                    response.isSuccess = false;
                    response.message = "用户没有签名数据不能签名!";
                    return response;
                }
                EMR_WH_QMXX qmxx = qmxxes.get(0);
                ret = service.signNameNurseRecord(qmxx.JLXH, nowStr, data.JLBH, data.JGID);
            } else {
                ret = service.signNameNurseRecord("-1", nowStr, data.JLBH, data.JGID);
            }
            if (ret < 1) {
                response.isSuccess = false;
                response.message = "当前护理记录可能已经签名或者不存在!";
                return response;
            }

            response.isSuccess = true;
            response.message = "签名成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录签名]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录签名]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取病人过敏药物，用于引用
     *
     * @param zyh
     * @param startime
     * @param endtime
     * @param jgid
     * @return
     */
    public BizResponse<List<DrugMedicalAdviceRefContent>> getGrugMedicalAdvices(String zyh, String startime, String endtime, String jgid) {
        BizResponse<List<DrugMedicalAdviceRefContent>> response = new BizResponse<>();
        List<DrugMedicalAdviceRefContent> list = new ArrayList<>();

        try {
            String endStr = DateUtil.dateoff(OffCycle.DAY, endtime, "1");
            String nowStr = timeService.now(DataSource.PORTAL);
            keepOrRoutingDateSource(DataSource.HRP);
            list = service.getGrugMedicalAdvices(zyh, startime, endStr, jgid);
            //福建协和客户化：护理记录引用医嘱
            if (list != null && list.size() > 0) {
                for (DrugMedicalAdviceRefContent item : list) {
                    String ypjc = service.getYPJCForYPXH(item.YPXH);
                    String yzmc = StringUtils.isBlank(ypjc) ? FormatAdvice(item.YZMC) : ypjc;
                    item.YZMC = yzmc;
                    item.YYNR = yzmc;
                    item.YCJL = StringTool.parseDecimalStr2Fixed(item.YCJL);
                    item.JLDW = cachedHandler.getValueByKeyFromCached(CachedDictEnum.MOB_TYPK, jgid, "YPXH", item.YPXH, "JLDW");
                }
            }

            response.isSuccess = true;
            response.data = list;
            response.message = "获取过敏药物引用成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[过敏药物引用]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[过敏药物引用]服务内部错误!";
        }
        return response;
    }

    //截取括号外面的值
    public String subAdviceName(String yzmc) {
        int j = yzmc.indexOf("(");
        int k = yzmc.indexOf(")");
        if (j == -1) {//不存在左括号
            if (k == -1) {//不存在右括号
                //不做任何操作
            } else {//存在右括号
                if (k == 0) {//右边括号在第一个字符位置
                    //想办法向后截取
                    yzmc = yzmc.substring(k + 1, yzmc.length());
                } else {//右边括号在中间位置
                    //想办法向前截取
                    yzmc = yzmc.substring(0, k);
                }
            }
        } else {//存在左括号
            if (j == 0) {//左边括号在第一个字符字符位置
                //想办法向后截取
                if (k == -1) {//不存在右括号
                    yzmc = yzmc.substring(j + 1, yzmc.length());
                } else {//存在右括号
                    yzmc = yzmc.substring(k + 1, yzmc.length());
                }
            } else {//左边括号在中间位置
                //想办法向前截取
                yzmc = yzmc.substring(0, j);
            }
        }
        //递归处理
        if (yzmc.contains("(") || yzmc.contains(")")) {
            yzmc = subAdviceName(yzmc);
        }
        return yzmc;
    }

    // 截断药品名称(只保留药品名称)
    private String FormatAdvice(String yzmc) {
        int i = yzmc.indexOf("/");
        if (i != 0) {
            yzmc = yzmc.substring(0, i);
        }
        yzmc = yzmc.replace("（", "(").replace("）", ")");
        //
        boolean isstart = NumberCharHelper.startWithCircle(yzmc);
        if (!isstart) {
            return subAdviceName(yzmc);
        }
        String numberChar = yzmc.substring(0, 1);
        String tempYzmc = yzmc.substring(1, yzmc.length());
        String tempYzmcBack = subAdviceName(tempYzmc);
        return numberChar + tempYzmcBack;
    }

    /**
     * 获取手术引用
     *
     * @param zyh
     * @param jgid
     * @return
     */
    public BizResponse<List<RefContent>> getOperationRefs(String zyh, String jgid) {
        BizResponse<List<RefContent>> response = new BizResponse<>();
        List<RefContent> list = new ArrayList<>();
        List<OperationRefContent> operList = new ArrayList<>();

        try {
            keepOrRoutingDateSource(DataSource.HRP);
            operList = service.getOperationRefs(zyh, jgid);
            for (OperationRefContent content : operList) {
                RefContent content1 = new RefContent();
                content1.JLSJ = content.SSRQ;
                content1.YYBH = content.SSBH;
                content1.YYNR = "于" + content.SSRQ + "在" + content.MZFS + "下进行" + content.SSMC;
                list.add(content1);
            }
            response.data = list;
            response.isSuccess = true;
            response.message = "获取手术引用成功!";

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[手术引用]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[手术引用]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取生命体征引用
     *
     * @param zyh
     * @param startime
     * @param endtime
     * @param jgid
     * @return
     */
    public BizResponse<List<SignRefContent>> getLifeSignRefs(String zyh, String startime, String endtime, String jgid) {
        BizResponse<List<SignRefContent>> response = new BizResponse<>();
        List<SignRefContent> list = new ArrayList<>();

        try {
            String endStr = DateUtil.dateoff(OffCycle.DAY, endtime, "1");
            String nowStr = timeService.now(DataSource.PORTAL);
            keepOrRoutingDateSource(DataSource.ENR);
            list = service.getLifeSignRefs(zyh, startime, endStr, jgid);
            response.data = list;
            response.isSuccess = true;
            response.message = "获取生命体征引用成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[生命体征引用]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[生命体征引用]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取特殊符号引用
     *
     * @param dmlb
     * @return
     */
    public BizResponse<List<RefContentClassification>> getOtherRefs(String dmlb) {
        BizResponse<List<RefContentClassification>> response = new BizResponse<>();
        List<RefContentClassification> list = new ArrayList<>();
        List<Dictionary> dictionaries = new ArrayList<>();

        try {
            // 获取代码字典(HIS)
            keepOrRoutingDateSource(DataSource.HRP);
            List<String> dmlbs = new ArrayList<>();
            dmlbs.add("4");
            dmlbs.add("5");
            dmlbs.add("7");
            dmlbs.add("10");
            dmlbs.add("9");
            dictionaries = dictService.getHisDictsByMulDmlb(dmlbs);

            RefContentClassification ref = new RefContentClassification();
            for (Dictionary dictionary : dictionaries) {
                if (dictionary.DMSB.equals("0")) {
                    if (!StringUtils.isEmpty(ref.LBBH))
                        list.add(ref);
                    ref = new RefContentClassification();
                    ref.LBBH = dictionary.DMLB;
                    ref.LBMC = dictionary.DMMC;
                } else {
                    if (dictionary.DMLB.equals(ref.LBBH)) {
                        RefContent content = new RefContent();
                        content.YYBH = dictionary.DMSB;
                        content.YYNR = dictionary.DMMC;
                        content.BZXX = dictionary.BZBM;
                        ref.Items.add(content);
                    }
                }
            }

            response.isSuccess = true;
            response.message = "获取特殊符号引用成功!";
            response.data = list;

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[特殊符号引用]SQL语句错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[特殊符号引用]服务内部错误!";
        }
        return response;
    }

    /**
     * TODO:测试用，删除
     *
     * @return
     */
    public List<Map> getLzjh(String ksrq, String jsrq) {
        List<Map> list = new ArrayList<>();
        keepOrRoutingDateSource(DataSource.LIS);
        list = service.getLzjh(ksrq, jsrq);
        return list;
    }

    public String getMergeRule(NRRecordRequest saveData) {
        keepOrRoutingDateSource(DataSource.ENR);
        String jlbh = service.getMergeRule(saveData.JGBH, saveData.ZYH, saveData.YHID, saveData.JLSJ);
        return jlbh;
    }

    public NRRecordRequest getHeaderAndFooter(NRRecordRequest saveData) {
        BizResponse<Patient> patientBizResponse = new BizResponse<>();
        List<StuctrueResponse> list = new ArrayList<>();
        List<Controll> controlls, activeControlls;
        List<StructurePlugIn> staticlist, btlist, ymlist;
        Patient patient = null;

        String nowStr;

        try {
            keepOrRoutingDateSource(DataSource.HRP);
            nowStr = timeService.now(DataSource.PORTAL);
            patientBizResponse = patientService.getPatientByZyh(saveData.ZYH);
            patient = patientBizResponse.data;
            keepOrRoutingDateSource(DataSource.ENR);
            // 基本控件
            controlls = service.getCtrlListByJgbh(saveData.JGID, saveData.JGBH);

            if (controlls.size() > 0) {
                staticlist = new ArrayList<>(); // 静态列
                btlist = new ArrayList<>(); // 比填列
                ymlist = new ArrayList<>(); // 页眉(KSLH>50)

                for (Controll controll : controlls) {
                    StructurePlugIn plugin = new StructurePlugIn();
                    plugin.KJH = Integer.valueOf(controll.getXMBH());
                    plugin.YSBH = controll.getYSBH();
                    plugin.YSLX = controll.getYSLX();
                    plugin.XSMC = controll.getXSMC();
                    plugin.SJGS = controll.getSJGS();
                    plugin.HHJG = controll.getHHJG();
                    plugin.YSZSX = controll.getYXZSX();
                    plugin.YSZXX = controll.getYXZXX();
                    plugin.ZCZSX = controll.getZCZSX();
                    plugin.ZCZXX = controll.getZCZXX();
                    plugin.SFBT = controll.getSFBT();
                    plugin.KJLX = 2;

                    String yskz = controll.getYSKZ();

                    switch (plugin.YSLX) {
                        case "1":  // 文本型
                            break;
                        case "2":  // 列表型
                            // 两种格式：【0（或1）多选分隔符#AAA/BBB/CCC】,第一位表示是否允许多选,分隔符后面是项目名称
                            // 【0#AAA/BBB/CCC$DD/EE/FF】,第一位表示是否允许多选,#分隔符后面是项目名称,$分隔符后面是项目说明
                            String[] datagroup = yskz.split("#");
                            String[] datagroup2 = null;
                            if (datagroup.length >= 2) {
                                plugin.SFDX = datagroup[0].substring(0, 1);
                                plugin.DXFG = plugin.SFDX.equals("1") ? datagroup[0].substring(1, datagroup[0].length() - 1) : "";
                                datagroup2 = datagroup[1].split("\\$");
                                if (datagroup2.length >= 2) {
                                    datagroup = datagroup2[0].split("/");
                                    datagroup2 = datagroup2[1].split("/");
                                } else {
                                    datagroup = datagroup[1].split("/");
                                    datagroup2 = datagroup;
                                }
                            }

                            if (datagroup.length > 0) {
                                plugin.DropdownItem = new ArrayList<>();
                                for (int i = 0; i < datagroup.length; i++) {
                                    String value = datagroup[i];
                                    String xznr;
                                    if (i < datagroup2.length) {
                                        xznr = datagroup2[i];
                                    } else {
                                        xznr = value;
                                    }
                                    DropItem dropItem = new DropItem();
                                    dropItem.VALUE = value;
                                    dropItem.XZNR = xznr;
                                    plugin.DropdownItem.add(dropItem);
                                }
                                plugin.KJLX = 4;
                            } else {
                                plugin.SFDX = "";
                            }
                            break;
                        case "3":  // 引用型
                            if (yskz.startsWith("1")) {      // 格式：1数据组号#行号#列号#列名
                                yskz = yskz.substring(1);
                                String[] kzarr = yskz.split("#");
                                if (kzarr.length == 3) {
                                    String sjzxh = kzarr[0];
                                    String row = kzarr[1];
                                    String col = kzarr[2];
                                    String value = dataSetService.getDataSetValue(sjzxh, saveData.ZYH, row, col);
                                    plugin.KJNR = value;

                                    // 当前年龄
                                    if (patient != null) {
                                        String csny = patient.CSNY;
                                        String ryrq = patient.RYRQ;
                                        if (col.equals("DQNL"))
                                            plugin.KJNR = BirthdayUtil.getAgesPairCommonStrOnlySui(csny, nowStr);

                                        // 入院年龄
                                        if (col.equals("RYNL"))
                                            plugin.KJNR = BirthdayUtil.getAgesPairCommonStrOnlySui(csny, ryrq);
                                    }
                                }
                            } else if (yskz.startsWith("2")) { // SQL语句型

                            } else {

                            }
                            break;
                        case "4":  // 特殊型
                            if ("服务器时间".equals(yskz)) {
                                plugin.KJNR = nowStr;

                            } else if ("产程开始时间".equals(yskz)) {
                                plugin.KJNR = "";

                            } else if (yskz.startsWith("453")) {  // 风险
                                String[] ys = yskz.split("#");
                                if (ys.length < 2)
                                    break;
                                plugin.YSLX = ys[0];
                                plugin.YSKZ = ys[1];
                                Association ass = getAssociation(saveData.ZYH, "453", ys[1], 0, saveData.JGID);
                                plugin.PageIndex = 0;
                                plugin.PageSize = ass.getPageSize();
                                plugin.RefrenceValue = ass.getValues();
                            }
                            break;
                        case "5":  // 字典型
                            if (!StringUtils.isBlank(yskz)) {
                                String[] zd = yskz.split("#");
                                if (zd.length > 0) {
                                    List<Map2> dicts = cachedHandler.getCachedDatasContainKeyValue(zd[0], saveData.JGID);
                                    for (Map2 map2 : dicts) {
                                        plugin.DropdownItem.add(new DropItem(map2.key, map2.value));
                                    }
                                    plugin.KJLX = 4;
                                }
                            }
                            break;
                        case "6":  // 体征型
                            plugin.YSKZ = yskz;
                            Association sign = getAssociation(saveData.ZYH, "6", yskz, 0, saveData.JGID);
                            plugin.PageIndex = 0;
                            plugin.PageSize = sign.getPageSize();
                            plugin.RefrenceValue = sign.getValues();
                            break;
                        default:
                            break;
                    }
                    // 页眉加入到ymlist
                    //if (Integer.parseInt(controll.getKSLH()) > 50) {
                    if(!"0".equals(controll.getYMCLFS())){
                        ymlist.add(plugin);
                    }
                }

                // 返回控件组
                // 必填项目控件
                StuctrueResponse btgroup = new StuctrueResponse();
                btgroup.LBH = "0";
                btgroup.LBMC = "必填列";
                btgroup.ZLX = "必填列";
                btgroup.NRControllist = btlist;
                list.add(btgroup);
                // 静态项目控件
                StuctrueResponse jtgroup = new StuctrueResponse();
                jtgroup.LBH = "1";
                jtgroup.LBMC = "静态列";
                jtgroup.ZLX = "静态列";
                jtgroup.NRControllist = staticlist;
                list.add(jtgroup);

                for (StructurePlugIn plugIn : ymlist) {
                    NRRecordItemRequest itemRequest = new NRRecordItemRequest();
                    itemRequest.XMBH = plugIn.KJH;
                    itemRequest.VALUE = plugIn.KJNR;
                    saveData.ItemList.add(itemRequest);
                }
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return saveData;
    }

    public BizResponse<String> updateNurseRecordForSyncDel(OutArgument outArgument) {
        BizResponse<String> response = new BizResponse<>();
        keepOrRoutingDateSource(DataSource.ENR);
        try {
            // 获取JL02中的明细
            List<NRItem> itemList = service.getNRItemsForSync(outArgument.mbjlxh);
            List<Long> mxbhList = new ArrayList<>();
            if (itemList.size() > 1) { // 更新ENR_JL02中的XMQZ= ''
                for (NRItem item : itemList) {
                    if (outArgument.lybdlx.equals(item.LYBD) && outArgument.lyjlxh.equals(item.LYBH)) {
                        mxbhList.add(item.MXBH);
                    }
                }
                if (mxbhList.size() == itemList.size()) { // 两个list的size相同
                    service.updateNRDataForSyncDel(outArgument.mbjlxh);
                } else if (!mxbhList.isEmpty()) {
                    service.updateNRItemsForSyncDel(mxbhList);
                }
            } else { // 作废整条记录,更新ENR_JL01中的JLZT= 9
                service.updateNRDataForSyncDel(outArgument.mbjlxh);
            }
            response.isSuccess = true;
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录删除]数据库操作错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录删除]服务内部错误!";
        }
        return response;
    }

    /**
     * 确定是否需要换页
     *
     * @param zyh
     * @param jgbh
     * @return
     */
    public Boolean confirmChangePage(String zyh, String jgbh, String jgid) {

        Boolean isChangePage = false;
        List<String> list = systemParamService.getUserParams("1", "ENR", "ENR_ZKHYSX", jgid, DataSource.MOB).datalist;
        // 未配置参数，默认不换页
        if (list.size() <= 0) {
            return false;
        }

        String changeDeptChangePage = list.get(0);
        if (StringUtils.isEmpty(changeDeptChangePage)) {
            return false;
        }

        if (changeDeptChangePage.equals("0")) {
            return false;
        }

        // 转科判断
        if (changeDeptChangePage.equals("1")) {
            String nowStr = timeService.now(DataSource.ENR);
            keepOrRoutingDateSource(DataSource.ENR);
            List<NRData> lists = service.getNRDatas(zyh, jgbh, nowStr);
            // 一次未写
            if (lists.size() <= 0) {
                return false;
            }

            String sxbq = lists.get(0).SXBQ;
            if (StringUtils.isEmpty(sxbq)) {
                return false;
            }

            // 获取当前病人的科室
            keepOrRoutingDateSource(DataSource.HRP);
            Patient patient = patientMapper.getPatientByZyh(zyh);
            isChangePage = !patient.BRBQ.equals(sxbq);
        }

        return isChangePage;
    }

    public List<Map> getExsitProjectsInRecord(List<String> xms, String jlbh) {
        List<Map> list = new ArrayList<>();
        keepOrRoutingDateSource(DataSource.ENR);
        try {
            list = service.getExsitProjectsInRecord(xms, jlbh);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }
}
