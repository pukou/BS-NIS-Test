import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bsoft.nis.domain.handover.HandOverForm;
import com.bsoft.nis.domain.handover.HandOverRecord;
import org.junit.Test;

/**
 * Describtion:
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/6/14.
 * Time:  17:06.
 */
public class HandOverTest {

    @Test
    public void createJson(){
        HandOverRecord record = new HandOverRecord();
        record.HandOverFormAfert = new HandOverForm();
        record.HandOverFormBefore = new HandOverForm();
        record.HandOverFormTemplate = new HandOverForm();
        String json = JSON.toJSONString(record, SerializerFeature.WriteMapNullValue);
        System.out.println(json);
    }
}
