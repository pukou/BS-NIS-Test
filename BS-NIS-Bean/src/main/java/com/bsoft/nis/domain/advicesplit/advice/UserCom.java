package com.bsoft.nis.domain.advicesplit.advice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:TODO 删除
 * Created: dragon
 * Date： 2016/12/28.
 */
public class UserCom implements Serializable{

    public String comid ;

    public String comname ;

    public List<User> users = new ArrayList<>();
}
