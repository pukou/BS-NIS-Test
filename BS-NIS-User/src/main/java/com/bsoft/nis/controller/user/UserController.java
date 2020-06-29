package com.bsoft.nis.controller.user;

import com.bsoft.nis.domain.core.barcode.Barcode;
import com.bsoft.nis.domain.office.AreaVo;
import com.bsoft.nis.domain.user.LoginResponse;
import com.bsoft.nis.domain.user.PDAInfo;
import com.bsoft.nis.domain.user.db.LoginUser;
import com.bsoft.nis.domain.user.session.LoginUserEntity;
import com.bsoft.nis.domain.user.session.ManageUnitEntity;
import com.bsoft.nis.domain.user.session.UserRoleEntity;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.service.OfficeService;
import com.bsoft.nis.service.UserMainService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户请求分发器
 * Created by Administrator on 2016/10/12.
 */
@Controller
public class UserController {

    @Autowired
    UserMainService service;          // 用户服务

    @Autowired
    OfficeService officeService;

    /**
     * 测试热部署
     *
     * @return
     */
    @RequestMapping(value = "user/login/reload")
    public
    @ResponseBody
    Response<String> login() {
        Response<String> result = new Response<>();
        result.ReType = 0;
        result.Msg = "登录成功";
        return result;
    }


    /**
     * 采用自定义对象方式返回值
     *
     * @return
     */
    @RequestMapping(value = "user/login")
    public
    @ResponseBody
    Response<String> login(HttpServletRequest request) {
        Response<String> result = new Response<>();
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }

        Map user = new HashMap();
        user.put("userid", "4517");
        user.put("username", "邢海龙");
        session.setAttribute("user", user);

        result.ReType = 0;
        result.Msg = "登录成功";
        result.Data = session.getId();
        System.out.println(session.getId());
        return result;
    }

    /**
     * 密码登录请求
     *
     * @param urid
     * @param pwd
     * @param jgid
     * @param sysType
     * @return
     */
    @RequestMapping(value = {"mobile/user/login"})
    public
    @ResponseBody
    Response<LoginResponse> login(HttpServletRequest request,
                                  @RequestParam(value = "urid", required = false) String urid,
                                  @RequestParam(value = "pwd", required = false) String pwd,
                                  @RequestParam(value = "jgid", required = false) String jgid,
                                  @RequestParam(value = "sysType", required = false) Integer sysType) {
        Response<LoginResponse> response = new Response<>();
        BizResponse<LoginResponse> bizResponse = new BizResponse<>();

        bizResponse = service.login(urid, pwd, jgid);
        if (!bizResponse.isSuccess) {
            response.ReType = -1;
        } else {
            response.ReType = 0;
            // 记录当前用户的信息
            LoginUser user = bizResponse.data.LonginUser;
            setUserMsgIntoSession(request, user.YHID, user.YHXM, jgid);
            // 返回sessionid
            HttpSession session = request.getSession();
            bizResponse.data.SessionId = session.getId();
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;

        return response;
    }

    /**
     * 扫描胸卡登录请求
     *
     * @param guid
     * @param jgid
     * @return
     */
    @RequestMapping(value = {"mobile/user/login/scan"})
    public
    @ResponseBody
    Response<LoginResponse> scanLogin(@RequestParam(value = "guid") String guid,
                                      @RequestParam(value = "jgid") String jgid,
                                      HttpServletRequest request) {
        Response<LoginResponse> response = new Response<>();
        BizResponse<LoginResponse> bizResponse = new BizResponse<>();

        bizResponse = service.scanLogin(guid, jgid);
        if (!bizResponse.isSuccess) {
            response.ReType = -1;
        } else {
            response.ReType = 0;
            // 记录当前用户信息
            LoginUser user = bizResponse.data.LonginUser;
            setUserMsgIntoSession(request, user.YHID, user.YHXM, jgid);
            // 返回sessionid
            HttpSession session = request.getSession();
            bizResponse.data.SessionId = session.getId();
        }
        response.Data = bizResponse.data;
        response.Msg = bizResponse.message;

        return response;
    }

    /**
     * 通过胸卡获取登录用户信息
     *
     * @param guid
     * @param jgid
     * @return
     */
    @RequestMapping(value = {"mobile/user/get/login/user/by/xk"})
    public
    @ResponseBody
    Response<LoginUser> getLoginUserByXk(@RequestParam(value = "guid") String guid,
                                         @RequestParam(value = "jgid") String jgid,
                                         HttpServletRequest request) {
        Response<LoginUser> response = new Response<>();
        BizResponse<LoginResponse> bizResponse = new BizResponse<>();

        bizResponse = service.scanLogin(guid, jgid);
        if (!bizResponse.isSuccess) {
            response.ReType = -1;
        } else {
            response.ReType = 0;
        }
        response.Data = bizResponse.data.LonginUser;
        response.Msg = bizResponse.message;

        return response;
    }


    /**
     * 获取PDAInfo信息
     * @param manuer    生产厂商
     * @param model     设备型号
     * @return
     */
    @RequestMapping(value = "mobile/user/get/pdainfo")
    public
    @ResponseBody
    Response<PDAInfo> getPDAInfo(@RequestParam(value = "manuer") String manuer, @RequestParam(value = "model") String model) {
        Response<PDAInfo> response = new Response<>();
        BizResponse<PDAInfo> bizResponse = new BizResponse<>();
        bizResponse = service.getPDAInfo(manuer, model);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;
    }

    /**
     * 获取机构列表
     *
     * @param userid
     * @return
     */
    @RequestMapping(value = "mobile/user/get/agencys")
    public
    @ResponseBody
    Response<List> getAgencies(@RequestParam(value = "urid", required = false) String userid) {
        Response<List> response = new Response<>();
        BizResponse<List> bizResponse = new BizResponse<>();
        bizResponse = service.getAgency(userid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;
    }

    /**
     * 密码登录请求
     *
     * @param urid
     * @param jgid
     * @return
     */
    @RequestMapping(value = {"mobile/user/logout"})
    public
    @ResponseBody
    Response<String> logout(HttpServletRequest request,
                            @RequestParam(value = "urid", required = false) String urid,
                            @RequestParam(value = "jgid", required = false) String jgid) {
        Response<String> response = new Response<>();
        try {
            invalidSession(request);
            response.ReType = 0;
            response.Msg = "注销该用户成功!";

        } catch (Exception e) {
            response.ReType = 0;
            response.Msg = "注销该用户失败!";
        }

        return response;
    }

    /**
     * 获取条码信息-包含校验功能
     *
     * @param barcode
     * @param jgid
     * @return
     */
    @RequestMapping(value = {"mobile/user/getBarcodeInfo"})
    public
    @ResponseBody
    Response<Barcode> getBarcodeInfo(@RequestParam(value = "barcode", required = false) String barcode,
                                     @RequestParam(value = "jgid", required = false) String jgid) {
        Response<Barcode> response = new Response<>();
        BizResponse<Barcode> bizResponse = new BizResponse<>();

        bizResponse = service.getBarcodeInfo(barcode, jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;
    }

    /**
     * 返回条码设定 - 此方法供查询RFID条码前缀用
     *
     * @param jgid
     * @return
     */
    @RequestMapping(value = {"auth/mobile/user/getBarcodeSetting"})
    public
    @ResponseBody
    Response<String> getBarcodeSetting(@RequestParam(value = "jgid", required = false) String jgid) {
        Response<String> response = new Response<>();
        BizResponse<String> bizResponse = new BizResponse<>();

        bizResponse = service.getBarcodeSetting(jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.data;

        return response;
    }


    /**
     * 获取手术科室列表列表
     *
     * @param jgid
     * @return
     */
    @RequestMapping(value = "auth/mobile/user/getAreaVoForSurgery")
    public
    @ResponseBody
    Response<List<AreaVo>> getAreaVoForSurgery(@RequestParam(value = "jgid", required = false) String jgid) {
        Response<List<AreaVo>> response = new Response<>();
        BizResponse<AreaVo> bizResponse = new BizResponse<>();
        bizResponse = officeService.getAreaVoForSurgery(jgid);

        if (bizResponse.isSuccess) {
            response.ReType = 0;
        } else {
            response.ReType = -1;
        }
        response.Msg = bizResponse.message;
        response.Data = bizResponse.datalist;

        return response;
    }


    /**
     * 记录当前用户信息到会话中
     */
    public void setUserMsgIntoSession(HttpServletRequest request, String userId, String userName, String oraganCode) {
        // 判断是否存在session
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }

        // 判断session中是否存在user对象
        Map user = (HashMap) session.getAttribute("user");

        // 创建user对象
        LoginUserEntity userEntity = new LoginUserEntity();
        UserRoleEntity role = new UserRoleEntity();
        ManageUnitEntity manageUnit = new ManageUnitEntity();

        userEntity.setDisplayName("");
        userEntity.setOrganizCode("");
        userEntity.setManageUnitId("");
        userEntity.setOrganName("");
        userEntity.setRoleId("");
        userEntity.setRoleName("");
        userEntity.setUserName(userName);
        userEntity.setUserId(userId);
        role.setId("");
        role.setName("");
        manageUnit.setId("");
        manageUnit.setName("");
        manageUnit.setRef(oraganCode);

        manageUnit.setHisOrganizationCode(oraganCode);

        userEntity.setRole(role);
        userEntity.setManageUnit(manageUnit);
        // 将对象转换成MAP 对象转成json -> json转成map
        String jsonString;
        ObjectMapper mapper = new ObjectMapper();

        try {
            jsonString = mapper.writeValueAsString(userEntity);
            user = mapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        session.setAttribute("user", user);
    }

    private void invalidSession(HttpServletRequest request) {
        // 判断是否存在session
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        // 判断session中是否存在user对象
        Map user = (HashMap) session.getAttribute("user");
        if (user != null) {
            session.removeAttribute("user");
        }

    }
}
