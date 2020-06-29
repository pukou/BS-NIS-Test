package record.test.config;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.domain.nurserecord.db.Structure;
import com.bsoft.nis.domain.nurserecord.db.Template;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.nurserecord.NurseRecordConfigService;
import com.bsoft.nis.core.datasource.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
// 加载基本的数据源、事务管理等公用配置
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
public class RecordConfigTest {

        @Autowired
        NurseRecordConfigService service;

        @Autowired
        DateTimeService timeservice;

        /**
         * 护理记录列表
         */
        @Test
        public void getNurseRecordStructureList() {
                BizResponse<Structure> res = service.getNurseRecordStructureList("1031","1",1);
                BizResponse<Template> templateBizResponse ;
                // 获取表单
                List<Structure> list = res.datalist;
                System.out.println(list);
                for (Structure structure:list){
                        templateBizResponse = service.getNurseRecordTemplateList("1031",structure.LBBH,"1",1);
                        List<Template> list1 = templateBizResponse.datalist;
                        System.out.println(list1);
                }
                return ;
        }

        @Test
        public void get() {
                BizResponse<Structure> res = service.getNurseRecordStructureList("1031","1",1);
                BizResponse<Template> templateBizResponse ;
                // 获取表单
                List<Structure> list = res.datalist;
                System.out.println(list);
                for (Structure structure:list){
                        templateBizResponse = service.getNurseRecordTemplateList("1031",structure.LBBH,"1",1);
                        List<Template> list1 = templateBizResponse.datalist;
                        System.out.println(list1);
                }
                return ;
        }

        /**
         * 测试获取数据库当前日期
         */
        @Test
        public void getDateTime(){
                String now = timeservice.now(DataSource.HRP);
                System.out.println(now);
        }

        @BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
