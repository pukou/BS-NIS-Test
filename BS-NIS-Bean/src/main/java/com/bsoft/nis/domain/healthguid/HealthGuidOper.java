package com.bsoft.nis.domain.healthguid;

import java.io.*;
import java.util.List;

/**
 * Description:
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-11-08
 * Time: 09:48
 * Version:
 */
public class HealthGuidOper implements Serializable {

    //序号
    public String XH;

    //描述
    public String MS;

    //可操作项目
    public List<HealthGuidOperItem> HealthGuidOperItems;

    public HealthGuidOper DeepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(HealthGuidOper.this);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        HealthGuidOper dest = (HealthGuidOper) in.readObject();
        return dest;
    }
}
