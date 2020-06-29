package core.test.dict;

import com.bsoft.nis.common.service.dataset.DataSetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import java.io.IOException;

/**
 * Created by Administrator on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
// 加载基本的数据源、事务管理等公用配置
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
public class AgesTest {

        @Autowired
        DataSetService service;

        /**
         * 获取科室字典数据 (缓存)
         */
        @Test
        public void getAges() {
                
        }

        @BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
