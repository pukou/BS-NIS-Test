package service.test.login;

import com.bsoft.nis.domain.user.LoginResponse;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.UserMainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrator on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
// 加载基本的数据源、事务管理等公用配置
/*@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional*/
public class LoginUnitTest {

        @Autowired
        UserMainService service;


        /**
         * 获取机构
         */
        @Test
        public void login() {
                String urid = "5821";
                String pwd = "";
                BizResponse<LoginResponse> response ;
                response = service.login(urid,pwd,"1");
                return ;
        }




        //@BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.HRP);
        }

}
