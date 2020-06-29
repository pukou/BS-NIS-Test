package test.uti.date;

import com.bsoft.nis.util.date.DateBetweenEnum;
import com.bsoft.nis.util.date.DateUtil;
import com.bsoft.nis.util.date.birthday.BirthdayUtil;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Describtion:java8 新日期api测试
 * Created: dragon
 * Date： 2016/12/15.
 */
public class Java8DateTimeTest {

    @Test
    public void testLocalDate(){
        LocalDate date = LocalDate.now();
        System.out.println("1当前时间和默认格式:" +date);
        LocalDate date1 = LocalDate.parse("2016-12-12");
        System.out.println("2字符串转LocalDate:" +date1);

        // 日期格式字符 转换成日期
        LocalDate date2 = LocalDate.parse("2016-12-18");
        System.out.println("3日期时间字符串转LocalDate:" + date2);

        // 时间转换
        LocalTime time = LocalTime.parse("23:59");
        System.out.println("8:时间转换" + time);

        String timeStr = time.format(DateTimeFormatter.ofPattern("HH"));
        System.out.println("8:时间转换" + timeStr);

        // 字符串日期型 转成LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse("2016-12-12 12:12:22.00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"));
        System.out.println("4日期时间字符串转LocalDateDatetime:" + dateTime);
        // LocalDateTime 转换字符串
        String dateTime1 = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("4日期时间字符串转LocalDateDatetime:" + dateTime1);

        // datetime 加减
        LocalDateTime add = dateTime.plusDays(1);
        System.out.println("6日期加一天:" + add);
        LocalDateTime munus = dateTime.minusDays(1);
        System.out.println("7日期减一天:" + munus);

        // 将已知的日期LocalDate 和已知的日期LocalTime转换LocalDateTime
        // 第一种方式：
        LocalDate date3 = date2;
        LocalTime time1 = time;
        LocalDateTime dateTime2 = LocalDateTime.of(date3.getYear(),date3.getMonth(),date3.getDayOfMonth(),time1.getHour(),time1.getMinute(),time1.getSecond());
        System.out.println("8:日期和时间组合成日期时间" + dateTime2);

        // 第二种方式：
        LocalDateTime dateTime3 = LocalDateTime.of(date3, time1);
        System.out.println("8:日期和时间组合成日期时间" + dateTime3);
        // LocalDateTime由日期 + 时间组合

        // 日期时间类型比较
        Boolean isGreater = add.isAfter(munus);  // after 相当于大于号作用
        System.out.println("8:日期比较" + isGreater);
        Boolean isLetter = add.isBefore(munus);  // before 相当于小于号作用
        System.out.println("8:日期比较" + isLetter);
        Boolean isEqual = add.isEqual(munus);  // equal 相当于等于号作用
        System.out.println("8:日期比较" + isEqual);

        // 最小日期
        LocalDateTime minDateTime = LocalDateTime.MIN;
        System.out.println("8:最小日期" + minDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Boolean isEqual1 = minDateTime.isEqual(LocalDateTime.MIN);  // equal 相当于等于号作用
        System.out.println("8:日期比较" + isEqual1);

        // 最大日期
        LocalDateTime maxDateTime = LocalDateTime.MAX;
        System.out.println("8:最大日期" + maxDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Boolean isEqual2 = add.isBefore(LocalDateTime.MAX);  // equal 相当于等于号作用
        System.out.println("8:日期比较" + isEqual2);

        // LocalDateTime 获取LocalDate 和LocalTime

        //System.out.println("8:日期比较" + dateTime.getDayOfYear());
        // 可用于比较俩个日期差
        LocalDate date8 = date2.plusDays(1);
        Long days = date2.toEpochDay();
        Long dayss = date8.toEpochDay();
        System.out.println("8:与时间纪元相差天数：" + days);
        System.out.println("8:与时间纪元相差天数：" + dayss);

        // 星期
        System.out.println("9：星期:" + date2.getDayOfWeek().getValue());

        LocalDateTime now = LocalDateTime.of(LocalDate.parse("2017-04-24"),LocalTime.parse("00:00:00"));
        now = now.plusMinutes(12);
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        System.out.println(LocalTime.parse("00:00:00").plusMinutes(360).format(DateTimeFormatter.ofPattern("HH:mm:ss")));


    }

    @Test
    public void testException(){
        User user = new User();
        try {

            getMsg(user);


        } catch (Exception e) {
            System.out.println(user.name);
            e.printStackTrace();
        }
    }

    public void getMsg(User user) throws Exception{
        user.id = "1";
        user.name= "邢海龙";
        throw new Exception(user.name);
    }

    class User{
        public String id;
        public String name;
    }

    @Test
    public void java8DateTest(){
        String endStr = "2017-12-23 15:50:01";
        String birthStr = "2017-08-22 13:50:01";
        LocalDateTime end = LocalDateTime.parse(endStr,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime birth = LocalDateTime.parse(birthStr,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        /*System.out.println(end.toEpochSecond(ZoneOffset.MIN));
        System.out.println(birth.toEpochSecond(ZoneOffset.MIN));
        System.out.println(end.toEpochSecond(ZoneOffset.MIN) - birth.toEpochSecond(ZoneOffset.MIN));

        System.out.println(28*365);
        System.out.println("小时差："+DateUtil.betweenOfDay(birth,end)*12);
        System.out.println("小时差："+DateUtil.between(birth,end, DateBetweenEnum.DAY));
        System.out.println(end.getDayOfMonth());*/

        // 计算年龄测试
        try {
            System.out.println(BirthdayUtil.getAgesCommon(birthStr,endStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
