package com.bsoft.nis.pojo.auth;

import java.io.Serializable;

/**
 * 用户对象
 * Created by Administrator on 2016/10/13.
 */
public class User implements Serializable{
    private static final long serialVersionUID = 5031950482704250076L;

    public String userId;

    public String userCode;

    public String userName;

    public String orgCode;

    public String orgName;

}
