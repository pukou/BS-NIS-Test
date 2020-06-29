package core.test.log;
import com.bsoft.nis.common.service.log.LogOperSubType;
import com.bsoft.nis.common.service.log.LogOperType;
import com.bsoft.nis.common.service.log.LogService;
import com.bsoft.nis.domain.core.log.db.OperLog;
import com.bsoft.nis.pojo.exchange.BizResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
// 加载基本的数据源、事务管理等公用配置
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
public class logservice {

        @Autowired
        LogService service;


        @Test
        public void getLogByPrimary() {
                BizResponse<List<OperLog>> response = service.getLogByPrimary("0");
                return ;
        }

        @Test
        public void addLog() {
                OperLog log = new OperLog();
                log.ZYH = 00000L;
                log.CZNR = "测试啦";
                log.CZLB = 1;
                log.CZLX = 1;
                log.GLBH = "1";
                log.CZGH = "0000";
                log.BQDM = 1l;
                log.CZZT = "1";
                log.SBXX = "";
                log.CZSJ = "2016-11-22";
                log.BZXX = "测试啦";
                log.ZFBZ = 0;
               // BizResponse<String> response = service.addLog(log);
                System.out.print(LogOperType.SpecimenCollect.get());
                return ;
        }

        @Test
        public void deleteLog() {
                OperLog log = new OperLog();
                log.ZYH = 00000L;
                log.CZNR = "测试啦";
                log.CZLB = 1;
                log.CZLX = 1;
                log.GLBH = "1";
                log.CZGH = "0000";
                log.BQDM = 1l;
                log.CZZT = "1";
                log.SBXX = "";
                log.CZSJ = "2016-11-22";
                log.BZXX = "测试啦";
                log.ZFBZ = 0;
                // BizResponse<String> response = service.addLog(log);
                System.out.print(LogOperType.SpecimenCollect.get());
                ArrayList<LogOperSubType> list = new ArrayList<>();
                list.add(LogOperSubType.EXECUTE);
                service.deleteLog("0", "1", "1", LogOperType.SpecimenCollect, list);
                return ;
        }


        @Test
        public void getLog() {
                OperLog log = new OperLog();
                log.ZYH = 00000L;
                log.CZNR = "测试啦";
                log.CZLB = 1;
                log.CZLX = 1;
                log.GLBH = "1";
                log.CZGH = "0000";
                log.BQDM = 1l;
                log.CZZT = "1";
                log.SBXX = "";
                log.CZSJ = "2016-11-22";
                log.BZXX = "测试啦";
                log.ZFBZ = 0;
                // BizResponse<String> response = service.addLog(log);
                System.out.print(LogOperType.SpecimenCollect.get());
                ArrayList<LogOperSubType> list = new ArrayList<>();
                list.add(LogOperSubType.EXECUTE);
                BizResponse<List<OperLog>> response = service.getSpecimenCollectLogs("0", "1");
                return ;
        }


        @BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
