import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bsoft.nis.domain.healthguid.HealthGuidData;
import com.bsoft.nis.domain.healthguid.HealthGuidSaveData;
import org.junit.Test;

/**
 * Describtion:
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/6/14.
 * Time:  17:02.
 */
public class JsonCreateTest {

    @Test
    public void createJson(){
        HealthGuidSaveData record = new HealthGuidSaveData();
        record.HealthGuidData = new HealthGuidData();
        String json = JSON.toJSONString(record, SerializerFeature.WriteMapNullValue);
        System.out.println(json);
    }
}
