package core.test.dict;

import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
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
public class CachedDictTest {

        @Autowired
        DictCachedHandler handler;


        /**
         * 怎么样获取value值
         * 获取科室字典数据 (缓存)
         */
        @Test
        public void getDictValue() {
                String value ;
                // handler.getValueByKeyFromCached(CachedDictEnum.MOB_BRXZ, "1", "7001");
              /*  System.out.println(value);
                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_BRXZ, "1", "7002");
                System.out.println(value);
                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_BRXZ, "1", "7003");
                System.out.println(value);
                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_BRXZ, "1", "7004");
                System.out.println(value);
                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_BRXZ, "1", "7005");
                System.out.println(value);
                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_BRXZ, "1", "7006");
                System.out.println(value);
                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_KSDM, "1", "1017");
                System.out.println(value);
                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_KSDM, "1", "1018");
                System.out.println(value);
                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_KSDM, "1", "1019");
                System.out.println(value);*/

                value = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, "1", "1019");
                System.out.println(value);
                return ;
        }

        @BeforeTransaction
        public void beforeTrans() {
            //DataSourceContextHolder.setDataSource(DataSource.PORTAL);
        }

}
