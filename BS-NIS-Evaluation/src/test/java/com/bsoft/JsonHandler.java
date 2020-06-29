package com.bsoft;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bsoft.nis.domain.evaluation.EvaluateSaveRespose;
import com.bsoft.nis.domain.evaluation.SaveForm;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Describtion:
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/6/14.
 * Time:  16:50.
 */
public class JsonHandler {

    @Test
    public void createJson(){
        EvaluateSaveRespose record = new EvaluateSaveRespose();
        record.saveForm = new SaveForm();
        record.lists = new ArrayList<>();
        record.lists.add(new SaveForm());

        String json = JSON.toJSONString(record, SerializerFeature.WriteMapNullValue);
        System.out.println(json);
    }
}
