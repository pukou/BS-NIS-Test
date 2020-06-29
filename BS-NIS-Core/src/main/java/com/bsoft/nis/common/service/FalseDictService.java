package com.bsoft.nis.common.service;

import com.bsoft.nis.common.servicesup.mapper.FalseServiceMapper;
import com.bsoft.nis.domain.core.FalseDict;
import com.bsoft.nis.domain.core.SqlStr;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 假字典服务 ==
 * 模拟字典数据，只包含key -value类数据集合
 * 如：KSDM ,KSMC ; 最终将字典全部转成 DMSB,DMMC
 * Created by Administrator on 2016/10/18.
 */
@Service
public class FalseDictService extends RouteDataSourceService{
    private Log logger = LogFactory.getLog(FalseDictService.class);

    @Autowired
    FalseServiceMapper mapper;

    public BizResponse<FalseDict> getDictCollectionOneKey(DataSource dataSource, String sql) {
        BizResponse<FalseDict> response = new BizResponse<>();
        keepOrRoutingDateSource(dataSource);
        try{
            SqlStr  sqlO = new SqlStr();
            sqlO.setSql(sql);
            List<FalseDict> dicts = mapper.getDictCollectionOneKey(sqlO);
            response.datalist = dicts;
            response.isSuccess = true;
            response.message = "获取成功!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = e.getMessage();
        }
        return response;
    }
}
