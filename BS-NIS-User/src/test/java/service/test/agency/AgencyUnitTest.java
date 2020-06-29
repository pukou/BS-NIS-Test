package service.test.agency;

import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.UserMainService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.DataSourceContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
// 加载基本的数据源、事务管理等公用配置
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class AgencyUnitTest {

        @Autowired
        UserMainService service;


        /**
         * 获取机构
         */
        @Test
        public void getAgency() {
                String userid = null;
                BizResponse<List> response ;
                response = service.getAgency(userid);
                return ;
        }




        @BeforeTransaction
        public void beforeTrans() {
            DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
