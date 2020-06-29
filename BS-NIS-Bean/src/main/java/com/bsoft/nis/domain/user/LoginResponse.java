package com.bsoft.nis.domain.user;

import com.bsoft.nis.domain.core.UserConfig;
import com.bsoft.nis.domain.office.AreaVo;
import com.bsoft.nis.domain.user.db.LoginUser;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */
public class LoginResponse implements Serializable{

    public LoginUser LonginUser;
    public TimeVo TimeVo;
    public List<AreaVo> Areas;
    public String SessionId;
    public UserConfig userConfig;
}
