import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bsoft.nis.domain.nurseplan.Problem;
import com.bsoft.nis.domain.nurseplan.ProblemSaveData;
import org.junit.Test;

/**
 * Describtion:
 * User: dragon (xinghl@bsoft.com.cn)
 * Date： 2017/6/14.
 * Time:  17:09.
 */
public class ProblemTest {

    @Test
    public void createJson(){
        ProblemSaveData record = new ProblemSaveData();
        record.Problem = new Problem();
        String json = JSON.toJSONString(record, SerializerFeature.WriteMapNullValue);
        System.out.println(json);
    }
}
