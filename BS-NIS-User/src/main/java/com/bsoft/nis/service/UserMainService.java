package com.bsoft.nis.service;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.UserConfigService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.core.barcode.Barcode;
import com.bsoft.nis.domain.office.AreaVo;
import com.bsoft.nis.domain.user.LoginResponse;
import com.bsoft.nis.domain.user.PDAInfo;
import com.bsoft.nis.domain.user.TimeVo;
import com.bsoft.nis.domain.user.db.Agency;
import com.bsoft.nis.domain.user.db.LoginUser;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.user.support.UserServiceSup;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import com.bsoft.nis.util.security.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户主服务
 * Created by Administrator on 2016/10/12.
 */
@Service
public class UserMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(UserMainService.class);

    @Autowired
    UserServiceSup service;

    @Autowired
    OfficeService officeService;

    @Autowired
    DateTimeService dateTimeService;

    @Autowired
    UserConfigService userConfigService;//获取系统参数服务

    /**
     * 扫描登录验证
     *
     * @param guid
     * @param jgid
     * @return
     */
    public BizResponse<LoginResponse> scanLogin(String guid, String jgid) {
        BizResponse<LoginResponse> response = new BizResponse<>();
        LoginResponse loginResponse = new LoginResponse();
        LoginUser user;

        try {
            // 1.验证当前用户 (mob库)
            keepOrRoutingDateSource(DataSource.MOB);
            user = service.getUserByCard(guid, jgid);
            if (user == null) {
                response.isSuccess = false;
                response.message = "未查到该胸卡信息!";
                return response;
            }

            // 2.根据当前用户ID获取当前门户用户（portal库）
            keepOrRoutingDateSource(DataSource.PORTAL);
            user = service.getUserByYGDM(user.YHID, jgid);
            if (user == null) {
                response.isSuccess = false;
                response.message = "用户不存在!";
                return response;
            }
            loginResponse.LonginUser = user;

            // 3.系统时间
            TimeVo timeVo = new TimeVo();
            timeVo.Time = DateUtil.getApplicationDateTime();  // TODO , HIS数据服务器时间
            loginResponse.TimeVo = timeVo;

            // 4.科室信息
            keepOrRoutingDateSource(DataSource.HRP);
            BizResponse<AreaVo> officeAreas = officeService.getOfficesByYGDM(user.YHID, jgid);
            if (!officeAreas.isSuccess) {
                response.isSuccess = false;
                response.message = officeAreas.message;
                return response;
            }
            loginResponse.Areas = officeAreas.datalist;

            //
            loginResponse.userConfig = userConfigService.getUserConfig(jgid);

            response.isSuccess = true;
            response.data = loginResponse;
            response.message = "登录成功!";

        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "服务内部错误";
        }
        return response;
    }


    /**
     * 密码登录验证
     *
     * @param urid
     * @param pwd
     * @param jgid
     * @return
     */
    public BizResponse<LoginResponse> login(String urid, String pwd, String jgid) {
        BizResponse<LoginResponse> response = new BizResponse<>();
        LoginResponse loginResponse = new LoginResponse();
        String type = "1";  // 1 4系列门户  2 2系列门户
        LoginUser user;

        // 获取加密方式 TODO , 加密方式

        // 获取用户
        try {
            keepOrRoutingDateSource(DataSource.PORTAL);
            user = service.getUserByYGBH(urid, jgid);
            if (user == null) {
                response.isSuccess = false;
                response.message = "用户不存在!";
                return response;
            }

            String yhid = user.YHID;

            // 获取解密方式 4系列采用 urid+pwd MD5加密
            String md5Str = decipheringPwd(yhid, pwd, type);

            // 用用户ID(内码)+加密后密码重新验证
            user = service.getUserByYGBHAndPwd(urid, jgid, md5Str);
            if (user == null) {
                response.isSuccess = false;
                response.message = "密码错误!";
                return response;
            }

            // 用户信息
            loginResponse.LonginUser = user;

            // 系统日期
            TimeVo timeVo = new TimeVo();
            timeVo.Time = dateTimeService.now(DataSource.PORTAL);
            loginResponse.TimeVo = timeVo;

            // 科室信息
            keepOrRoutingDateSource(DataSource.HRP);
            BizResponse<AreaVo> officeAreas = officeService.getOfficesByYGDM(user.YHID, jgid);
            // add by louis 查询手术科室  //KSDM 存放 SSKS
            BizResponse<AreaVo> surgeryOfficeAreas = officeService.getSurgeryOffices(jgid);
            if (!officeAreas.isSuccess) {
                response.isSuccess = false;
                response.message = officeAreas.message;
                return response;
            }
            /**
             *  add by louis
             *  append datalist
             */
            if (surgeryOfficeAreas != null) {
                officeAreas.datalist.addAll(surgeryOfficeAreas.datalist);
            }
            loginResponse.Areas = officeAreas.datalist;
            //
            //getUserConfig
            loginResponse.userConfig = userConfigService.getUserConfig(jgid);

            response.isSuccess = true;
            response.data = loginResponse;
            response.message = "登录成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "数据库查询错误";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "服务内部错误";
        }
        return response;
    }

    // 解密方式
    public String decipheringPwd(String urid, String pwd, String type) {
        String str = "";

        // 解密
        switch (type) {
            case "1":  // 用户ID + 用户密码  MD5加密
                str = MD5Util.getMD5(urid + pwd);
                str = StringUtils.upperCase(str);
                break;
            case "2":  //

                break;
        }
        return str;
    }

    /**
     * 获取机构列表接口(如传入空值，获取所有机构列表，如传入员工代码,则获取该工号关联的机构列表)
     *
     * @param urid
     * @return
     */
    public BizResponse<List> getAgency(String urid) {
        keepOrRoutingDateSource(DataSource.PORTAL);
        BizResponse<List> response = new BizResponse<>();
        List<Agency> list;
        try {
            list = service.getAgency(urid);
            response.data = list;
            response.isSuccess = true;
            response.message = "获取机构成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "服务内部错误!";
        }
        return response;
    }

    /**
     * 获取PDAINFO信息，如果无，则新增一条信息
     * @param manuer
     * @param model
     * @return
     */
    public BizResponse<PDAInfo> getPDAInfo(String manuer, String model) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<PDAInfo> response = new BizResponse<>();
        PDAInfo pdaInfo;
        try {
            pdaInfo = service.getPDAInfo(manuer, model);
            if(pdaInfo == null){
                service.addPDAInfo(manuer, model);
                response.data = null;
            }else {
                if(StringUtils.isEmpty(pdaInfo.ACTION) || StringUtils.isEmpty(pdaInfo.DATA)){
                    response.data = null;
                }else{
                    response.data = pdaInfo;
                }
            }

            response.isSuccess = true;
            response.message = "获取PDAInfo成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "服务内部错误!";
        }
        return response;
    }

    /**
     * 获取条码信息
     *
     * @param barcode
     * @param jgid
     * @return
     */
    public BizResponse<Barcode> getBarcodeInfo(String barcode, String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<Barcode> response = new BizResponse<>();
        try {
            if (StringUtils.isBlank(jgid) || jgid.equals("-1")) {
                String[] strArray = barcode.split("_");
                if (strArray.length == 2) {
                    jgid = strArray[1];
                } else {
                    response.isSuccess = false;
                    response.message = "该条码无法识别!";
                }
            }
            List<Barcode> tmsdList = service.GetTmzd();
            BarcodeFactorys barcodeFactorys = new BarcodeFactorys();
            List<BarcodeRule> barcodeRuleList = new ArrayList<>();
            for (int i = 0; i < tmsdList.size(); i++) {
                if (tmsdList.get(i).JGID.equals(jgid)) {
                    barcodeRuleList.add(barcodeFactorys.Create(tmsdList.get(i)));
                }
            }
            Barcode finalBarcode = null;
            for (BarcodeRule barcodeRule : barcodeRuleList) {
                if (barcodeRule.IsMatched(barcode)) {
                    finalBarcode = barcodeRule.Barcode;
                    break;
                }
            }
            if (finalBarcode == null) {
                response.isSuccess = false;
                response.message = "该条码无法识别!";
            } else {
                response.isSuccess = true;
                response.data = finalBarcode;
                response.message = "解析该条码成功!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "服务内部错误!";
        }
        return response;
    }

    /**
     * 返回条码设定 - 此方法供查询RFID条码前缀用
     *
     * @param jgid
     * @return
     */
    public BizResponse<String> getBarcodeSetting(String jgid) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> response = new BizResponse<>();
        try {
            List<Barcode> tmsdList = service.GetTmzd();
            String tmqz = "";
            for (int i = 0; i < tmsdList.size(); i++) {
                Barcode barcode = tmsdList.get(i);
                if (barcode.JGID.equals(jgid) && barcode.TMFL.equals("5")) {
                    tmqz = barcode.TMQZ;
                    break;
                }
            }
            if (StringUtils.isBlank(tmqz)) {
                response.isSuccess = false;
                response.message = "RFID设备前缀获取出错!";
            } else {
                response.isSuccess = true;
                response.data = tmqz;
                response.message = "RFID设备前缀获取成功!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "服务内部错误!";
        }
        return response;
    }

    public abstract class BarcodeRule {

        public Barcode Barcode;

        abstract boolean IsMatched(String barcode);
    }

    /// <summary>
    /// 以日期格式开头的条码，支持YYYYMMDD、YYYY、YYYYMM、YYMMDD、YYMM,
    /// </summary>
    public class DateFormRule extends BarcodeRule {
        private String forms = "";
        private int type = 0;   //0:yyyymmdd,1:yyyymm,2:yymmdd,3:yymm,4:yyyy.

        public DateFormRule(Barcode barcode) {
            this.Barcode = barcode;
            this.forms = barcode.GZNR;
            switch (forms.length()) {
                case 4://yymm ||yyyy
                    String firsttwo = forms.substring(0, 2);
                    String endtwo = forms.substring(2, 2);
                    if (firsttwo.equals(endtwo))
                        type = 4;
                    else
                        type = 3;
                    break;
                case 6://yyyymm ||yymmdd
                    String pre = forms.substring(0, 2);
                    String mid = forms.substring(2, 2);
                    String end = forms.substring(4, 2);
                    if (!pre.equals(mid))
                        type = 2;
                    else
                        type = 1;
                    break;
                case 8://yyyymmdd
                    type = 0;
                    break;
                default:
                    break;
            }
        }

        @Override
        public boolean IsMatched(String barcode) {
            try {
                String formStr = barcode.substring(0, this.forms.length());
                String dev = "-";
                Date date;
                //0:yyyymmdd,1:yyyymm,2:yymmdd,3:yymm,4:yyyy.
                switch (type) {
                    case 0:
                        formStr = formStr.substring(0, 4) + dev + formStr.substring(4, 2) + dev + formStr.substring(6, 2);
                        date = DateConvert.toDateTime(formStr, "yyyy-MM-dd");
                        break;
                    case 1:
                        formStr = formStr.substring(0, 4) + dev + formStr.substring(4, 2);
                        date = DateConvert.toDateTime(formStr, "yyyy-MM");
                        break;
                    case 2:
                        formStr = formStr.substring(0, 2) + dev + formStr.substring(2, 2) + dev + formStr.substring(4, 2);
                        date = DateConvert.toDateTime(formStr, "yy-MM-dd");
                        break;
                    case 3:
                        formStr = formStr.substring(0, 2) + dev + formStr.substring(2, 2);   //  yy-mm
                        date = DateConvert.toDateTime(formStr, "yy-MM");
                        break;
                    case 4:
                        break;
                    default:
                        break;
                }
                Barcode.TMNR = barcode;
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    /// <summary>
    /// 含校验位的识别规则
    /// </summary>
    public class VerificationRule extends BarcodeRule {
        public VerificationRule(Barcode Barcode) {
            this.Barcode = Barcode;
            this.verification = Barcode.GZNR;//TODO
        }

        private String verification;

        @Override
        public boolean IsMatched(String barcode) {
            if (barcode.length() > verification.length() && barcode.lastIndexOf(verification) == barcode.length() - verification.length()) {
                Barcode.TMNR = barcode;
                return true;
            }
            return false;
        }
    }

    /// <summary>
    /// 固定长度的规则
    /// </summary>
    public class LengthRule extends BarcodeRule {
        private int length = 0;

        public LengthRule(Barcode barcode) {
            this.Barcode = barcode;
            try {
                this.length = Integer.parseInt(barcode.GZNR);
            } catch (Exception ex) {
                this.length = 0;
            }
        }

        @Override
        public boolean IsMatched(String barcode) {
            if (this.length == barcode.length()) {
                Barcode.TMNR = barcode;
                return true;
            }
            return false;
        }
    }

    /// <summary>
    /// 前缀规则
    /// </summary>
    public class PrefixRule extends BarcodeRule {
        private String prefix;

        public PrefixRule(Barcode barcodeObj) {
            this.Barcode = barcodeObj;
            prefix = barcodeObj.TMQZ;
        }

        public boolean IsMatched(String barcode) {
            //如果符合扫描标识
            if ((!StringUtils.isBlank(prefix)) && barcode.indexOf(prefix) == 0) {
                Barcode.TMNR = barcode.replace(prefix, "");
                return true;
            }
            return false;
        }
    }

    public class BarcodeFactorys {
        public BarcodeRule Create(Barcode barcode) {
            BarcodeRule iRule;
            switch (barcode.TMGZ) {
                case "3":
                    iRule = new DateFormRule(barcode);
                    break;
                case "2":
                    iRule = new LengthRule(barcode);
                    break;
                case "1":
                    iRule = new PrefixRule(barcode);
                    break;
                case "4":
                    iRule = new VerificationRule(barcode);
                    break;
                default:
                    iRule = null;
                    break;

            }
            return iRule;
        }
    }

}
