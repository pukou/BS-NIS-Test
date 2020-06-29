package com.bsoft.nis.domain.adviceexecute.ResponseBody;

import com.bsoft.nis.domain.synchron.InArgument;
import com.bsoft.nis.domain.synchron.SelectResult;

import java.util.List;

/**
 * Description: 医嘱执行返回结果
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-27
 * Time: 15:53
 * Version:
 */
public class ResponseBodyInfo {
    public String TableName;
    public String Message;
    public List<REModel> REModelList;
    public List<SJModel> SJModelList;
    public List<KFModel> KFModelList;
    public List<ZSModel> ZSModelList;
    public List<SYZTModel> SYZTModelList;
    public List<SYModel> SYModelList;
    public List<SQModel> SQModelList;
    public Boolean IsSync = false;
    public SelectResult SyncData;
    //add 2018-4-18 21:54:54
    public InArgument inArgument;

}
