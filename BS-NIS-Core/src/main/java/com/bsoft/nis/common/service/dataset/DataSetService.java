package com.bsoft.nis.common.service.dataset;

import com.bsoft.nis.common.service.portal.DataBaseLinkService;
import com.bsoft.nis.common.servicesup.mapper.dataset.DataSetServiceMapper;
import com.bsoft.nis.domain.core.SqlStr;
import com.bsoft.nis.domain.core.dataset.DataSet;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

/**
 * Describtion:数据组定义服务
 * 1数据组的获取
 * 2数据组解析
 * 3数据返回
 * Created: dragon
 * Date： 2016/10/24.
 */
@Service
public class DataSetService extends RouteDataSourceService{

    private Log logger = LogFactory.getLog(DataSetService.class);

    @Autowired
    DataSetServiceMapper mapper;

    @Autowired
    DataBaseLinkService linkService; // 数据库连接服务

    /**
     * 获取数据组，并通过数据组获取某患者的数据，返回{行}{列}值
     * @param sjzxh
     * @param zyh
     * @param rowid
     * @param col
     * @return
     */
    public String getDataSetValue(String sjzxh,String zyh,String rowid,String col){
        String str = "";
        String sql ,datasource;
        List<Map> datas = new ArrayList<>();
        Integer rowindex ;
        Integer colindex ;

        rowindex = Integer.valueOf(rowid);

        try{
            keepOrRoutingDateSource(DataSource.ENR);
            List<DataSet> dataSets = mapper.getDataSetsBySJZXH(sjzxh);

            if (dataSets.size() <=0){
                logger.warn("【" + sjzxh + "】数据组未配置!");
                return str;
            }

            DataSet dataSet = dataSets.get(0);
            sql = dataSet.getSJZDY();             // sql语句
            datasource = dataSet.getGLSW();       // 数据源

            if (StringUtils.isBlank(sql.trim()) || StringUtils.isBlank(datasource.trim())){
                logger.warn("【" + dataSet.getSJZMC() + "】数据组SQL语句未定义或数据源未指定!");
                return str;
            }

            // 替换{ZYH}为真实住院号
            if (sql.contains("{ZYH}")){
                sql = sql.replace("{ZYH}",zyh);
            }
            if (sql.contains("{JZHM}")){
                sql = sql.replace("{JZHM}",zyh);
            }

            // 切换数据源   数据组 数据源转换通过Portal库
            //datasource =linkService.getDataBaseLinkName(datasource);
            if(datasource == null) datasource = "";
            if(datasource.matches("^[0-9]+$")) {
            datasource =linkService.getDataBaseLinkName(datasource);
            }
            switch (datasource){
                case "SQLHIS":
                    keepOrRoutingDateSource(DataSource.HRP);
                    break;
                case "SQLMOBHIS":
                    keepOrRoutingDateSource(DataSource.HRP);
                    break;
                case "SQLMOBENR":
                    keepOrRoutingDateSource(DataSource.ENR);
                    break;
                case "SQLMOBEMR":
                    keepOrRoutingDateSource(DataSource.EMR);
                    break;
                case "SQLMOBRIS":
                    keepOrRoutingDateSource(DataSource.RIS);
                    break;
                case "SQLMOBUIS":
                    keepOrRoutingDateSource(DataSource.RIS);
                    break;
                case "SQLMOBLIS":
                    keepOrRoutingDateSource(DataSource.LIS);
                    break;
                case "SQLEMR":
                    keepOrRoutingDateSource(DataSource.EMR);
                    break;
	            case "SQLCIS":
		            keepOrRoutingDateSource(DataSource.HRP);
		            break;
                default:
                    keepOrRoutingDateSource(DataSource.ENR);
            }

            // 根据配置的SQL语句，获取数据
            datas = mapper.getDataSetDatasBySql(new SqlStr(sql));

            // 根据规则返回数据
            Boolean isNum = com.bsoft.nis.util.string.StringUtils.isPositiveNumber(col);

            if (datas.size() >= rowindex){
                Map map = datas.get(rowindex - 1);
                if (!isNum){  // 列名
                    if (map.containsKey(col)){
                        str = String.valueOf(map.get(col));
                    }else{
                        str = "";
                    }
                }else{       // 列号
                    colindex = Integer.valueOf(col);
                    Set<String> keys = map.keySet();
                    String key = "";
                    if (keys.size() >= colindex){
                        Integer i = 1;
                        Iterator<String> it = keys.iterator();
                        while(it.hasNext()) {
                            if (i.equals(colindex)){
                                key = it.next();
                                break;
                            }else{
                                it.next();
                            }
                            i++;
                        }

                        if (!StringUtils.isBlank(key)){
                            if (map.containsKey(key)){
                                str = String.valueOf(map.get(key));
                            }else{
                                str = "";
                            }
                        }
                    }
                }
            }
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return str;
    }
}
