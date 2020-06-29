package com.bsoft.nis.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description:重新设计护理评估单：
 * 支持yyyy-MM-dd Hh:mm:ss 日期格式的反序列号
 * create by: dragon xinghl@bsoft.com.cn
 * create time:2017/11/24 14:08
 * since:5.6 update1
 */
public class CustomJsonDateDeserializer extends JsonDeserializer<Date>{
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = jsonParser.getText();
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
