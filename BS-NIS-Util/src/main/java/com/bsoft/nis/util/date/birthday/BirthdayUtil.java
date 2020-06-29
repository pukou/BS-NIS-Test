package com.bsoft.nis.util.date.birthday;

import com.bsoft.nis.util.date.DateBetweenEnum;
import com.bsoft.nis.util.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * 依赖Java8
 * Created by dragon on 2016/10/18.
 */
public class BirthdayUtil {
    public static class AgesPair {
        public long sui;
        public long yue;
        public long tian;
        public long xiaoshi;
    }

    /**
     * 新模式:
     * 年龄大于等于3*12个月的，用岁表示；
     * 小于3*12个月而又大于等于1*12个月的，用岁月表示；
     * 小于12个月而又大于等于6个月的，用月表示；
     * 小于6个月而大于等于29天的，用月天表示；
     * 大于72小时小于29天的，用天表示；
     * 小于72小时的，用小时表示。
     *
     * @param birthday
     * @param nowstr
     * @return
     */
    @Deprecated //暂时不使用，不要把年龄后面的中文传到前端
    public static String getAgesCommon(String birthday, String endStr) throws IllegalArgumentException, ParseException {
        if (StringUtils.isEmpty(birthday) || StringUtils.isEmpty(endStr))
            throw new IllegalArgumentException("出生年月无效,无法计算年龄!");
        if (birthday.length() > 19) {
            birthday = birthday.substring(0, 19);
        }
        if (endStr.length() > 19) {
            endStr = endStr.substring(0, 19);
        }

        LocalDateTime birthdayDT = LocalDateTime.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDT = LocalDateTime.parse(endStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String age = "";
        age = getAgesCommon(birthdayDT, endDT);
        return age;
    }

    /**
     * 新模式:
     * 年龄大于等于3*12个月的，用岁表示；
     * 小于3*12个月而又大于等于1*12个月的，用岁月表示；
     * 小于12个月而又大于等于6个月的，用月表示；
     * 小于6个月而大于等于29天的，用月天表示；
     * 大于72小时小于29天的，用天表示；
     * 小于72小时的，用小时表示。
     *
     * @param birthday
     * @param end
     * @return
     */
    @Deprecated //暂时不使用，不要把年龄后面的中文传到前端
    public static String getAgesCommon(LocalDateTime birthday, LocalDateTime end) throws IllegalArgumentException, ParseException {
        final Integer numberOfYear = 3 * 12; //单位是月,大于等于该变量值则显示[岁],默认是3*12个月
        final Integer numberOfYearMonth = 1 * 12; //单位是月,大于等于该变量值则显示[岁月],默认是1*12个月(需要小于numberOfYear)
        final Integer numberOfMonth = 6; //单位是月,大于等于该变量值则显示[月],默认是6个月(需要小于numberOfYearMonth)
        final Integer numberOfMonthDay = 29; //单位是天,大于等于该变量值则显示[月天],默认是29天(需要小于numberOfMonth)
        final Integer numberOfTime = 72; //单位是小时,大于等于该变量值则显示[小时],默认是72小时(需要小于numberOfMonthDay)
        String age = ""; //显示年龄字符串

        Long diffMonth; // 月份差
        Long diffDay; // 天数差
        Long diffHour; // 小时差
        Long remnantDay; // 剩余天数，结束日期与开始日期之差，该值可能小于0
        Long remnantHour; // 剩余小时，结束日期与开始日期小时之差，，考虑现在的算法细化到小时，天数不能随便加1，结合现实情况，小时加1
        Long dayBeginMonth; // 起始月总天数

        // 1.开始日期和结束日期有效性校验
        if (birthday == null && birthday.toLocalDate().isEqual(java.time.LocalDate.parse("1900-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            throw new IllegalArgumentException("出生年月无效，无法计算年龄!");
        if (end == null && end.toLocalDate().isEqual(java.time.LocalDate.parse("1900-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            throw new IllegalArgumentException("截止日期无效，无法计算年龄!");
        if (birthday.isAfter(end))
            throw new IllegalArgumentException("出生年月大于截止日期,无法计算年龄!");

        //2.基础数据提取
        java.time.LocalDate endD = end.toLocalDate();
        java.time.LocalDate birthD = birthday.toLocalDate();
        java.time.LocalTime endT = end.toLocalTime();
        java.time.LocalTime birthT = birthday.toLocalTime();
        diffMonth = DateUtil.between(birthday, end, DateBetweenEnum.MONTH);
        diffDay = DateUtil.between(birthday, end, DateBetweenEnum.DAY);
        remnantDay = Long.valueOf(endD.getDayOfMonth() - birthD.getDayOfMonth());
        remnantHour = Long.valueOf(endT.getHour() - birthT.getHour() + 1);

        if (remnantHour < 0) {
            remnantDay = remnantDay - 1;
            diffDay = diffDay - 1;
            remnantHour = remnantHour + 24;
        }
        diffHour = diffDay * 24 + remnantHour; // 计算实际总小时数

        // 根据剩余天数，算出实际总月数
        if (remnantDay < 0) {
            // 计算开始月的总天数
            int month = birthD.getMonthValue();
            if (month == 2) {
                if ((birthD.getYear() % 4 == 0 && birthD.getYear() % 100 != 0) || (birthD.getYear() % 400 == 0)) {
                    dayBeginMonth = 29L;
                } else {
                    dayBeginMonth = 28L;
                }
                dayBeginMonth = 28L;
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                dayBeginMonth = 31L;
            } else {
                dayBeginMonth = 30L;
            }
            diffMonth = diffMonth - 1; // 计算实际总月数
            remnantDay = remnantDay + dayBeginMonth;
        }

        if (diffMonth >= numberOfYear) { // 大于numberOfYear(3*12)个月按岁显示
            age = (diffMonth / 12) + "岁";
        } else if (diffMonth >= numberOfYearMonth && diffMonth < numberOfYear) {//大于ll_NumberOfYearMonth(1*12)个月而小于ll_NumberOfYear(3*12)个月按岁月显示，0月时只显示岁
            if (diffMonth % 12 == 0)
                age = (diffMonth / 12) + "岁";
            else
                age = (diffMonth / 12) + "岁" + (diffMonth % 12) + "月";
        } else if (diffMonth >= numberOfMonth && diffMonth < numberOfYearMonth) {//大于ll_NumberOfMonth(6)个月而小于ll_NumberOfYearMonth(1*12)个月按月显示
            age = diffMonth + "月";
        } else if (diffMonth < numberOfMonth) {
            if (diffDay >= numberOfMonthDay) {//大于ll_NumberOfMonthDay(29)天小于ll_NumberOfMonth(6)月按月天现实，0天时只显示月
                if (remnantDay == 0) {
                    age = diffMonth + "月";
                } else {
                    if (diffMonth == 0) {
                        age = remnantDay + "天";
                    } else {
                        age = diffMonth + "月" + remnantDay + "天";
                    }
                }
            } else if (diffHour > numberOfTime) {//大于ll_NumberOfTime(72)小时小于ll_NumberOfMonthDay(29) 时按天显示
                age = diffDay + "天";
            } else {
                age = diffHour + "小时";
            }
        }
        return age;
    }


    /**
     *
     * 只算岁，多用于计算
     * 不足1岁 进位
     *
     * @param birthday
     * @param endStr
     * @return
     * @throws IllegalArgumentException
     * @throws ParseException
     */
    public static String getAgesPairCommonStrOnlySui(String birthday, String endStr) throws IllegalArgumentException, ParseException {
        long sui = getAgesPairCommon(birthday, endStr).sui;
        return String.valueOf(sui < 1 ? 1 : sui);
    }

    /**
     * 只会出现一个中文单位
     * 舍去尾数
     * 31岁
     * 3岁
     * 6月
     * 2月
     * 10天
     * 5时
     *
     * @param birthday
     * @param endStr
     * @return
     * @throws IllegalArgumentException
     * @throws ParseException
     */
    public static String getAgesPairCommonStrSimple(String birthday, String endStr) throws IllegalArgumentException, ParseException {
        AgesPair agesPair = getAgesPairCommon(birthday, endStr);
        if (agesPair.sui > 0) {
            return agesPair.sui + "岁";
        }
        if (agesPair.yue > 0) {
            return agesPair.yue + "月";
        }
        if (agesPair.tian > 0) {
            return agesPair.tian + "天";
        }
        if (agesPair.xiaoshi > 0) {
//            return agesPair.tian + "小时";
            return agesPair.tian + "时";
        }
        return "";
    }

    /**
     * 会出现多个中文单位
     * 31岁
     * 3岁5月
     * 6月
     * 2月10天
     * 8天
     * 5时
     *
     * @param birthday
     * @param endStr
     * @return
     * @throws IllegalArgumentException
     * @throws ParseException
     */
    public static String getAgesPairCommonStr(String birthday, String endStr) throws IllegalArgumentException, ParseException {
        AgesPair agesPair = getAgesPairCommon(birthday, endStr);
        StringBuffer stringBuffer = new StringBuffer();
        if (agesPair.sui > 0) {
            stringBuffer.append(agesPair.sui);
            stringBuffer.append("岁");
        }
        if (agesPair.yue > 0) {
            stringBuffer.append(agesPair.yue);
            stringBuffer.append("月");
        }
        if (agesPair.tian > 0) {
            stringBuffer.append(agesPair.tian);
            stringBuffer.append("天");
        }
        if (agesPair.xiaoshi > 0) {
            stringBuffer.append(agesPair.xiaoshi);
            stringBuffer.append("小时");
        }
        return stringBuffer.toString();
    }

    public static AgesPair getAgesPairCommon(String birthday, String endStr) throws IllegalArgumentException, ParseException {
        if (StringUtils.isEmpty(birthday) || StringUtils.isEmpty(endStr))
            throw new IllegalArgumentException("出生年月无效,无法计算年龄!");
        if (birthday.length() > 19) {
            birthday = birthday.substring(0, 19);
        }
        if (endStr.length() > 19) {
            endStr = endStr.substring(0, 19);
        }

        LocalDateTime birthdayDT = LocalDateTime.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDT = LocalDateTime.parse(endStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return getAgesPairCommon(birthdayDT, endDT);
    }

    public static AgesPair getAgesPairCommon(LocalDateTime birthday, LocalDateTime end) throws IllegalArgumentException, ParseException {
        final Integer numberOfYear = 3 * 12; //单位是月,大于等于该变量值则显示[岁],默认是3*12个月
        final Integer numberOfYearMonth = 1 * 12; //单位是月,大于等于该变量值则显示[岁月],默认是1*12个月(需要小于numberOfYear)
        final Integer numberOfMonth = 6; //单位是月,大于等于该变量值则显示[月],默认是6个月(需要小于numberOfYearMonth)
        final Integer numberOfMonthDay = 29; //单位是天,大于等于该变量值则显示[月天],默认是29天(需要小于numberOfMonth)
        final Integer numberOfTime = 72; //单位是小时,大于等于该变量值则显示[小时],默认是72小时(需要小于numberOfMonthDay)
        AgesPair agesPair = new AgesPair();

        Long diffMonth; // 月份差
        Long diffDay; // 天数差
        Long diffHour; // 小时差
        Long remnantDay; // 剩余天数，结束日期与开始日期之差，该值可能小于0
        Long remnantHour; // 剩余小时，结束日期与开始日期小时之差，，考虑现在的算法细化到小时，天数不能随便加1，结合现实情况，小时加1
        Long dayBeginMonth; // 起始月总天数

        // 1.开始日期和结束日期有效性校验
        if (birthday == null && birthday.toLocalDate().isEqual(java.time.LocalDate.parse("1900-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            throw new IllegalArgumentException("出生年月无效，无法计算年龄!");
        if (end == null && end.toLocalDate().isEqual(java.time.LocalDate.parse("1900-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            throw new IllegalArgumentException("截止日期无效，无法计算年龄!");
        if (birthday.isAfter(end))
            throw new IllegalArgumentException("出生年月大于截止日期,无法计算年龄!");

        //2.基础数据提取
        java.time.LocalDate endD = end.toLocalDate();
        java.time.LocalDate birthD = birthday.toLocalDate();
        java.time.LocalTime endT = end.toLocalTime();
        java.time.LocalTime birthT = birthday.toLocalTime();
        diffMonth = DateUtil.between(birthday, end, DateBetweenEnum.MONTH);
        diffDay = DateUtil.between(birthday, end, DateBetweenEnum.DAY);
        remnantDay = Long.valueOf(endD.getDayOfMonth() - birthD.getDayOfMonth());
        remnantHour = Long.valueOf(endT.getHour() - birthT.getHour() + 1);

        if (remnantHour < 0) {
            remnantDay = remnantDay - 1;
            diffDay = diffDay - 1;
            remnantHour = remnantHour + 24;
        }
        diffHour = diffDay * 24 + remnantHour; // 计算实际总小时数

        // 根据剩余天数，算出实际总月数
        if (remnantDay < 0) {
            // 计算开始月的总天数
            int month = birthD.getMonthValue();
            if (month == 2) {
                if ((birthD.getYear() % 4 == 0 && birthD.getYear() % 100 != 0) || (birthD.getYear() % 400 == 0)) {
                    dayBeginMonth = 29L;
                } else {
                    dayBeginMonth = 28L;
                }
                dayBeginMonth = 28L;
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                dayBeginMonth = 31L;
            } else {
                dayBeginMonth = 30L;
            }
            diffMonth = diffMonth - 1; // 计算实际总月数
            remnantDay = remnantDay + dayBeginMonth;
        }

        if (diffMonth >= numberOfYear) { // 大于numberOfYear(3*12)个月按岁显示
            agesPair.sui = diffMonth / 12;
        } else if (diffMonth >= numberOfYearMonth && diffMonth < numberOfYear) {//大于ll_NumberOfYearMonth(1*12)个月而小于ll_NumberOfYear(3*12)个月按岁月显示，0月时只显示岁
            if (diffMonth % 12 == 0) {
                agesPair.sui = diffMonth / 12;
            } else {
                agesPair.sui = diffMonth / 12;
                agesPair.yue = diffMonth % 12;
            }
        } else if (diffMonth >= numberOfMonth && diffMonth < numberOfYearMonth) {//大于ll_NumberOfMonth(6)个月而小于ll_NumberOfYearMonth(1*12)个月按月显示
            agesPair.yue = diffMonth;
        } else if (diffMonth < numberOfMonth) {
            if (diffDay >= numberOfMonthDay) {//大于ll_NumberOfMonthDay(29)天小于ll_NumberOfMonth(6)月按月天现实，0天时只显示月
                if (remnantDay == 0) {
                    agesPair.yue = diffMonth;
                } else {
                    if (diffMonth == 0) {
                        agesPair.tian = remnantDay;
                    } else {
                        agesPair.yue = diffMonth;
                        agesPair.tian = remnantDay;
                    }
                }
            } else if (diffHour > numberOfTime) {//大于ll_NumberOfTime(72)小时小于ll_NumberOfMonthDay(29) 时按天显示
                agesPair.tian = diffDay;
            } else {
                agesPair.xiaoshi = diffHour;
            }
        }
        return agesPair;
    }


    /**
     * 获取年龄字符串
     *
     * @param birthday 字符串生日
     * @param nowstr   字符串当前时间
     * @return
     */
    @Deprecated
    public static String getAgesYear(String birthday, String nowstr) {
        LocalDate now, birth;
        String ages = "";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(birthday)) {
            return "";
        } else {
            try {
                Date d0 = format.parse(birthday);//20180707
                birth = LocalDate.parse(format.format(d0));
            } catch (ParseException e) {
                birth = null;
                e.printStackTrace();
            }

        }

        if (StringUtils.isBlank(nowstr)) {
            now = new LocalDate();
        } else {
            try {
                Date d1 = format.parse(nowstr);
                now = LocalDate.parse(format.format(d1));
            } catch (ParseException e) {
                e.printStackTrace();
                now = null;
            }
        }

        ages = getAgesYear(birth, now);
        return ages;
    }

    /**
     * 获取年龄字符串
     *
     * @param birthdate 出生日期
     * @param now       当前日期
     * @return <p>成功返回年龄字符串；失败返回null;<br/>格式：'y岁m月d天'</p>
     */
    @Deprecated
    public static String getAges(LocalDate birthdate, LocalDate now) {

        if (birthdate == null) {
            return null;
        }
        if (now == null) {
            now = new LocalDate();
        }

        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();
        StringBuffer result = new StringBuffer();
        if (years != 0) {
            result.append(years).append("岁");
        }
        if (months != 0) {
            result.append(months).append("月");
        }
        if (days != 0) {
            result.append(days).append("天");
        }
        return result.toString();
    }


    /**
     * 获取年龄字符串 (岁)
     *
     * @param birthdate
     * @param now
     * @return
     */
    @Deprecated
    public static String getAgesYear(LocalDate birthdate, LocalDate now) {

        if (birthdate == null) {
            return null;
        }
        if (now == null) {
            now = new LocalDate();
        }

        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        int years = period.getYears();
//        int months = period.getMonths();
//        int days = period.getDays();
//        StringBuffer result = new StringBuffer();
        if (years < 1) {
            years = 1;
        }
        return String.valueOf(years);
    }

    /**
     * 获取年龄字符串 (岁,月)
     *
     * @param birthdate
     * @param now
     * @return
     */
    @Deprecated
    public static String getAgesYearMouth(LocalDate birthdate, LocalDate now) {

        if (birthdate == null) {
            return null;
        }
        if (now == null) {
            now = new LocalDate();
        }

        Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();
        StringBuffer result = new StringBuffer();
        if (years != 0) {
            result.append(years).append("岁");
        }
        if (months != 0) {
            result.append(months).append("月");
        }
        return result.toString();
    }

    /**
     * 获取年龄字符串
     *
     * @param birthdate 出生日期
     * @return <p>成功返回年龄字符串；失败返回null;<br/>格式：'y岁m月d天'</p>
     */
    @Deprecated
    public static String getAges(LocalDate birthdate) {
        return getAges(birthdate, new LocalDate());
    }

    /**
     * 获取年龄字符串
     *
     * @param year  出生年份，例如 1970
     * @param month 出生月份 0-12
     * @param day   出生日
     * @return <p>成功返回年龄字符串；失败返回null;<br/>格式：'y岁m月d天'</p>
     */
    @Deprecated
    public static String getAges(int year, int month, int day) {
        return getAges(new LocalDate(year, month, day), new LocalDate());
    }


    /**
     * 获取年龄字符串
     *
     * @param year  出生年份，例如 1970
     * @param month 出生月份 0-12
     * @param day   出生日
     * @return <p>成功返回年龄字符串；失败返回null;<br/>格式：'y岁m月d天'</p>
     */

    /**
     * 获取年龄字符串
     *
     * @param year   出生年份，例如 1970
     * @param month  出生月份 0-12
     * @param day    出生日
     * @param nyear  当前年份
     * @param nmonth 当前月份 0-12
     * @param nday   当前日
     * @return <p>成功返回年龄字符串；失败返回null;<br/>格式：'y岁m月d天'</p>
     */
    @Deprecated
    public static String getAges(int year, int month, int day, int nyear, int nmonth, int nday) {
        return getAges(new LocalDate(year, month, day), new LocalDate(nyear, nmonth, nday));
    }
}
