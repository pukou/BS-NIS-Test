package com.bsoft.mobile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bsoft.nis.domain.dangerevaluate.DERecord;
import com.bsoft.nis.domain.dangerevaluate.DERecordPostData;
import com.bsoft.nis.domain.dangerevaluate.MeasureRecord;
import com.bsoft.nis.domain.dangerevaluate.MeasureRecordPostData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    @org.junit.Test
    public void createJson(){
        MeasureRecordPostData record = new MeasureRecordPostData();
        record.MeasureRecord = new MeasureRecord();
        String json = JSON.toJSONString(record, SerializerFeature.WriteMapNullValue);
        System.out.println(json);
    }
}
