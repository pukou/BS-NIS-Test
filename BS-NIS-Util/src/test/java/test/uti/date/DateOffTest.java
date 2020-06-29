package test.uti.date;

import com.bsoft.nis.util.date.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/11/1.
 */
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class DateOffTest {
    @Test
    public void dateOff(){
        String result;
        try {
           /* String result = DateUtil.dateoffDays("2016-11-02","1");
            System.out.println(result);
            result = DateUtil.dateoffDays("2016-11-02","-1");
            System.out.println(result);*/
            result = DateUtil.dateoffDays("2016-11-02","-1","yyyy-MM-dd");
            System.out.println(result);
        }catch (ParseException e){
            e.printStackTrace();
        }

    }
}
