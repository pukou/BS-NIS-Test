package test.uti.date;

import com.bsoft.nis.util.date.DateCompare;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/11/1.
 */
//@ContextConfiguration(locations = { "classpath:springContext-bean.xml" })
//@RunWith(SpringJUnit4ClassRunner.class)
public class DateConvertTest {
    @Test
    public void dateConvert(){
        try {
            //System.out.println(DateConvert.toString("2016/11/12 23:12:000:000","mm:ss"));
          /*  System.out.println(DateCompare.compare("2016-11-01 00:00:00", "2016-10-10 00:00:00"));
            System.out.println(String.format("%tR", "2016-11-12 23:12:000:000"));
                    System.out.println(DateCompare.isGreaterThan("2016-11-01 00:00:02", "2016-11-01 00:00:01"));
*/

            System.out.println(DateUtil.between("2015-12-01","2016-12-31"));


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testreturn(){
        String ls = run();
        System.out.println(ls);
    }

    public String run(){
        String ret = "你";
        try{
            ret = "2012-12-01";
            return run2( ret) ;
        }catch (Exception e){
            try{
                return ret = "他";
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        ret ="我2";
        return ret;
    }

    public String run2(String ret){
        return "我3";
    }

    @Test
    public void testListDel(){
        List<Map> list = new ArrayList<>();
        for (int i = 1; i < 21;i++){
            Map map1 = new HashMap();
            map1.put("id",i);
            map1.put("name","龙"+i);
            list.add(map1);
        }

        for (int i = 0 ; i < list.size();i++){
            Map map2 = list.get(i);
            if (map2.get("id").equals(1)){
                list.remove(map2);
                i--;
            }
        }
        return ;
    }

    @Test
    public void collectTest(){
        List<Map> list = new ArrayList<>();

        for (int i = 1; i < 21;i++){
            Map map1 = new HashMap();
            map1.put("id",i);
            map1.put("name","龙"+i);
            list.add(map1);
        }
         String id = "1";
        List<Map> list1 = (List)CollectionUtils.select(list, new MedicalPredicate(id));

        return ;
    }

    class MedicalPredicate implements Predicate{
        String id;
        public MedicalPredicate(String id){
            this.id = id;
        }
        @Override
        public boolean evaluate(Object o) {
            Map map = (Map) o;
            return String.valueOf(map.get("id")).equals(id);
        }
    }

}


