package patient.test.detail;

import com.bsoft.nis.domain.patient.PatientDetailResponse;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.patient.PatientMainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * Created by Administrator on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
// 加载基本的数据源、事务管理等公用配置
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
public class PatientTest {

        @Autowired
        PatientMainService service;


        /**
         * 病人详情
         */
        @Test
        public void patientDetails() {
                BizResponse<PatientDetailResponse> response = service.getPatientDetail("605946","1");
                return ;
        }

        @BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
