package test.uti.list;

import com.bsoft.nis.util.date.birthday.BirthdayUtil;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/11/1.
 */
@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class getAgeTest {
    @Test
    public void getAges(){
        String result = BirthdayUtil.getAgesYear("","2015-01-01");
        System.out.println(result);

    }
}
