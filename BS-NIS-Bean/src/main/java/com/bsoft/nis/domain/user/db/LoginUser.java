package com.bsoft.nis.domain.user.db;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/12.
 */
public class LoginUser implements Serializable{

    private static final long serialVersionUID = 3256815214429180991L;
    /**
     * 员工id
     */
    public String YHID;
    /**
     * 员工姓名
     */
    public String YHXM;

    /**
     * 当前默认病区
     */
    public int MRBZ;

    /**
     * 机构ID
     */
    public String JGID;

    /**
     * 登陆账号
     */
    public String YHDM;

    /**
     * 用户胸卡
     */
    public String YHXK;

    @Override
    public String toString() {
        return "YHID is " + YHID + "YHXM is" + YHXM + " JGID is" + JGID;
    }
}
