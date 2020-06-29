package core.test.dict;

import com.bsoft.nis.core.dictionary.DictCollectionHandler;
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
public class DictTest {

        @Autowired
        DictCollectionHandler handler;


        /**
         * 获取科室字典数据 (缓存)
         * @throws ClassNotFoundException
         * @throws IOException
         */
        @Test
        public void getDictValue() {
                for(int i = 0 ; i < 5 ; i++){
                        System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                        String value = handler.getDictCollectionOneKeyForDept("1126","1");
                        System.out.println(value);
                        System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));

                        System.out.println(handler.getDictCollectionOneKeyForUser("1000092", "1"));
                }
                return ;
        }

        @BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
