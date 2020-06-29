package test.uti.list;

import com.bsoft.nis.util.list.ListSelect;
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
public class ListUtilTest {



        /**
         * 病人详情
         */
        @Test
        public void listSelect() {
                List<TestUser> list = new ArrayList<>();
                TestUser user1 = new TestUser();
                user1.id = "1";
                user1.name = "邢海龙";
                TestUser user2 = new TestUser();
                user2.id = "1";
                user2.name = "邢海龙2";
                TestUser user3 = new TestUser();
                user3.id = "1";
                user3.name = "邢海龙3";

                TestUser user4 = new TestUser();
                user4.id = "2";
                user4.name = "文字1";
                TestUser user5 = new TestUser();
                user5.id = "2";
                user5.name = "文字2";
                list.add(user1);
                list.add(user2);
                list.add(user3);
                list.add(user4);
                list.add(user5);

                List<TestUser> list1 ;
                try {
                        list1 = ListSelect.select(user1,list,"id","1");
                        return ;
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                }
        }

        @BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}

class TestUser{
        public String name;
        public String id ;
}
