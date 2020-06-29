package record.test.help;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.domain.nurserecord.NRRecordItemRequest;
import com.bsoft.nis.domain.nurserecord.NRRecordRequest;
import com.bsoft.nis.domain.nurserecord.NRTree;
import com.bsoft.nis.domain.nurserecord.StuctrueResponse;
import com.bsoft.nis.domain.nurserecord.db.HelpTree;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.nurserecord.NurseRecordHelperService;
import com.bsoft.nis.service.nurserecord.NurseRecordWriteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })*/
// 加载基本的数据源、事务管理等公用配置
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
public class RecordHelperTest {

       /* @Autowired
        NurseRecordHelperService service;

        @Autowired
        DateTimeService timeservice;
*/
        /**
         * 护理记录列表
         */
        @Test
        public void getNurseRcordHelperContent() {
                //BizResponse<List<HelpTree>> response = service.getHelpContent("360","10296","10044","100051","1032","1");
                return ;
        }

        @Test
        public void getNRTreeByMblx() {

            int i ;


            return ;
        }


    @Test
    public void createJson(){
        System.out.println("==");
        NRRecordRequest record = new NRRecordRequest();
        record.ItemList = new ArrayList<>();
        record.ItemList.add(new NRRecordItemRequest());

        String json = JSON.toJSONString(record, SerializerFeature.WriteMapNullValue);
        System.out.println(json);
    }





    /*@BeforeTransaction*/
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
