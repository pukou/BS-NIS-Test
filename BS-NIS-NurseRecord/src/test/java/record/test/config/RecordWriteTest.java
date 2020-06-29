package record.test.config;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.domain.nurserecord.NRTree;
import com.bsoft.nis.domain.nurserecord.StuctrueResponse;
import com.bsoft.nis.domain.nurserecord.db.NRData;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.nurserecord.NurseRecordWriteService;
import com.bsoft.nis.service.nurserecord.support.NurseRecordWriteServiceSup;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2016/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
// 加载基本的数据源、事务管理等公用配置
/*@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional*/
public class RecordWriteTest {

        @Autowired
        NurseRecordWriteService service;

        @Autowired
        DateTimeService timeservice;

        @Autowired
        NurseRecordWriteServiceSup supservice;

        /**
         * 护理记录列表
         */
        @Test
        public void getNurseRecordStructureList() {
               BizResponse<List<StuctrueResponse>> response = service.getCtrlListByJgbh("350", "605953", "1");
                System.out.println("===");
                double size1;
                size1 = 1.001d;
                //Integer.parseInt(Math.ceil(size1));
                //Integer.parseInt("1");
                //int i =(int)size1;
                //Math.ceil(Double0);
                // 将double转成int
                int i ;

                 BizResponse<List<StuctrueResponse>> response1 = service.getCtrlListByJlbh("605953","","1",1);
                return ;
        }

        @Test
        public void getNRTreeByMblx() {
            BizResponse<List<NRTree>> response = service.getNRTreeByMblx("605743", null, "1");
            System.out.println("===");
            double size1;
            size1 = 1.001d;
            //Integer.parseInt(Math.ceil(size1));
            //Integer.parseInt("1");
            //int i =(int)size1;
            //Math.ceil(Double0);
            // 将double转成int
            int i ;


            return ;
        }

        @Test
        public void conllectFilter() {
            List<String> list = new ArrayList<>();
            list.add("1");
            list.add("3");
            list.add("4");
            list.add("5");
            list.add("6");


            CollectionUtils.filter(list, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    Integer str = Integer.valueOf(String.valueOf(o));
                    return str > 3;
                }
            });

            List<String> to = new ArrayList<>(Arrays.asList(new String[list.size()]));
            Collections.copy(to,list);

            list.add("100");
            System.out.println(list.size());

            int i ;


            return ;
        }

    @Test
    public void getNRdata() {

        try {

            List<NRData> list = supservice.getNRData("12118","1");
            boolean i = 1 ==1 ;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return ;
    }


    @Test
    public void getSwitchDataSource() {
        List<Map> list = service.getLzjh("2016-03-01","2016-12-12");
        boolean i = 1 ==1 ;
        return ;
    }





    /*@BeforeTransaction
        public void beforeTrans() {
            DataSourceContextHolder.setDataSource(DataSource.ENR);
        }*/

}
