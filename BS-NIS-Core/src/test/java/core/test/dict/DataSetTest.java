package core.test.dict;

import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.common.service.dataset.DataSetService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.dictionary.DictCollectionHandler;
import com.bsoft.nis.pojo.exchange.BizResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
// 加载基本的数据源、事务管理等公用配置
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
public class DataSetTest {

        @Autowired
        DataSetService service;

        @Autowired
        IdentityService identityService;


        /**
         * 获取科室字典数据 (缓存)
         */
        @Test
        public void getDataSet() {
                for(int i = 0 ; i< 50; i++){
                        String value = service.getDataSetValue("2", "605725", "1", "BRBQ");
                        System.out.println(value);
                }


                BizResponse<Long> response = identityService.getIdentityMax("ENR_SYD",1, DataSource.MOB);
                System.out.println(response.datalist.get(0));
                return ;
        }

        @BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
