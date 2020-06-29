package com.bsoft.nis.core.dictionary;

import com.bsoft.nis.common.service.FalseDictService;
import com.bsoft.nis.domain.core.FalseDict;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.core.datasource.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字典集合Handler类  // TODO : 待完成整个缓存字典数据
 * 字典数据做缓存处理 ；目前默认缓存1个小时
 * Created by Administrator on 2016/10/18.
 */
@Component
public class DictCollectionHandler {

    @Autowired
    FalseDictService service; // 模拟字典服务

    /**
     * 根据SQL语句获取模拟字典数据(key-value)
     * @param dataSource  // 数据源
     * @param SQL         // 查询语句
     * @param qid         // 待匹配ID
     * @return str
     */
    public String getDictCollectionOneKey(DataSource dataSource,String SQL,String qid){
        BizResponse<FalseDict> response ;
        String value = "";
        response = service.getDictCollectionOneKey(dataSource,SQL);

        if (!response.isSuccess){
            return value;
        }

        List<FalseDict> dicts = response.datalist;

        for (FalseDict dict : dicts){
            if (qid.equals(dict.DMSB)){
                value = dict.DMMC;
                break;
            }
        }
        return value;
    }

    /**
     * 科室信息
     * @param qid  // 待处理值
     * @param jgid // 机构信息
     * @return str
     */
    public String getDictCollectionOneKeyForDept(String qid,String jgid){
        DataSource ds = DataSource.HRP;
        String sql = "SELECT KSDM DMSB,KSMC DMMC FROM V_MOB_HIS_KSDM WHERE JGID = " + jgid; // 获取科室的SQL语句
        String value = "";

        value = getDictCollectionOneKey(ds,sql,qid);
        return value;
    }

    /**
     * 用户信息
     * @param qid  // 待处理值
     * @param jgid // 机构信息
     * @return str
     */
    public String getDictCollectionOneKeyForUser(String qid,String jgid){
        DataSource ds = DataSource.HRP;
        String sql = "SELECT YGDM DMSB,YGXM DMMC FROM V_MOB_HIS_YGDM WHERE JGID = " + jgid; // 获取科室的SQL语句
        String value = "";

        value = getDictCollectionOneKey(ds,sql,qid);
        return value;
    }

    /**
     * 获取病人性质
     * @param qid   // 待处理值
     * @param jgid  // 机构信息
     * @return
     */
    public String getDictCollectionOneKeyForPatientProperty(String qid,String jgid){
        DataSource ds = DataSource.HRP;
        String sql = "SELECT YGDM DMSB,YGXM DMMC FROM V_MOB_HIS_YGDM WHERE JGID = " + jgid; // 获取科室的SQL语句
        String value = "";

        value = getDictCollectionOneKey(ds,sql,qid);
        return value;
    }

}
