package com.bsoft.nis.util.list;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:List类型选择工具类
 * Created: dragon
 * Date： 2016/10/21.
 */
public class ListSelect {


    /**
     * List 类型列表过滤：根据某字段的值来过滤
     * @param type        类型
     * @param sources     源列表
     * @param colname     字段名
     * @param value       字段值
     * @param <T>         泛型
     * @return            List<T>
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static<T> List<T> select(T type,List<T> sources,String colname,String value) throws IllegalAccessException, NoSuchFieldException {
        List<T> list = new ArrayList<>();

        if (sources.size()<=0
                || StringUtils.isBlank(colname)
                || StringUtils.isBlank(value)){
            return list;
        }

        for (T t:sources){
            // 1.获取字节文件对象
            Class c = t.getClass();
            // 2.获取该类的成员变量
            Field f = c.getDeclaredField(colname);
            // 3.取消语言访问检查
            f.setAccessible(true);
            // 4.判断是否满足条件
            if(value.equals(String.valueOf(f.get(t)))){
                list.add(t);
            }
        }
        return list;
    }
}
