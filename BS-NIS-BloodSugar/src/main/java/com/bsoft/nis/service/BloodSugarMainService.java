package com.bsoft.nis.service;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.domain.bloodsugar.BloodSugar;
import com.bsoft.nis.domain.bloodsugar.PersonBloodSugar;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.bloodsugar.BloodSugarServiceSup;
import com.bsoft.nis.util.date.DateConvert;
import com.bsoft.nis.util.date.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 血糖治疗（new）主服务
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2017-05-22
 * Time: 13:37
 * Version:
 */
@Service
public class BloodSugarMainService {

    private Log logger = LogFactory.getLog(BloodSugarMainService.class);

    @Autowired
    IdentityService identityService;//种子表服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    @Autowired
    DateTimeService timeService; // 日期时间服务

    @Autowired
    BloodSugarServiceSup service; //血糖治疗服务

    /**
     * 获取测量时点列表
     *
     * @return
     */
    public BizResponse<String> getClsdList() {
        BizResponse<String> response = new BizResponse<>();
        try {
            List<String> list = service.getClsdList();
            list.add(0, "");
            response.datalist = list;
            response.isSuccess = true;
            response.message = "获取测量时点列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取测量时点列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取测量时点列表数据失败]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取当前病人当前时间段的血糖列表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param zyh   住院号
     * @param jgid  机构id
     * @return
     */
    public BizResponse<BloodSugar> getBloodSugarList(String start, String end, String zyh, String jgid) {
        BizResponse<BloodSugar> response = new BizResponse<>();
        try {
            Date date = DateConvert.toDateTime(end, "yyyy-MM-dd");
            LocalDateTime localDateTime = DateConvert.toLocalDateTime(date);
            localDateTime = localDateTime.plusDays(1);//加一天
            end = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<BloodSugar> bloodSugarList = service.getBloodSugarList(start, end, zyh, jgid);
            for (BloodSugar bloodSugar : bloodSugarList) {
                bloodSugar.SXXM = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, bloodSugar.SXGH);
            }
            response.datalist = bloodSugarList;
            response.isSuccess = true;
            response.message = "获取血糖列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取血糖列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取血糖列表数据失败]服务内部错误!";
        }
        return response;
    }

    public BizResponse<PersonBloodSugar> getNeedGetBloodSugarList(String clsd, String brbq, String jgid) {
        BizResponse<PersonBloodSugar> response = new BizResponse<>();
        try {
            List<PersonBloodSugar> personBloodSugarList = service.getNeedGetBloodSugarList(brbq, jgid);
            List<PersonBloodSugar> personBloodSugarListBack = findNeedRecordClsdListLogic(clsd, jgid, personBloodSugarList);
            response.datalist = personBloodSugarListBack;
            response.isSuccess = true;
            response.message = "获取血糖列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取血糖列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取血糖列表数据失败]服务内部错误!";
        }
        return response;
    }

    public BizResponse<PersonBloodSugar> getNeedGetBloodSugarListArr(List<String> clsdList, String brbq, String jgid) {
        BizResponse<PersonBloodSugar> response = new BizResponse<>();
        try {
            List<PersonBloodSugar> personBloodSugarListBack = new ArrayList<>();
            List<PersonBloodSugar> personBloodSugarList = service.getNeedGetBloodSugarList(brbq, jgid);
            for (int i = 0; i < clsdList.size(); i++) {
                String clsd = clsdList.get(i);
                List<PersonBloodSugar> personBloodSugarListNew = deepCopyList(personBloodSugarList);
                List<PersonBloodSugar> personBloodSugarListTemp = findNeedRecordClsdListLogic(clsd, jgid, personBloodSugarListNew);
                for (PersonBloodSugar personBloodSugar : personBloodSugarListTemp) {
                    boolean has = nextClsdList_IsHasReconded(i, jgid, personBloodSugar.ZYH, clsdList);
                    personBloodSugar.TXBZ = has ? "1" : "0";
                }
                personBloodSugarListBack.addAll(personBloodSugarListTemp);
            }
            response.datalist = personBloodSugarListBack;
            response.isSuccess = true;
            response.message = "获取血糖列表数据成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取血糖列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[获取血糖列表数据失败]服务内部错误!";
        }
        return response;
    }

    private boolean nextClsdList_IsHasReconded(int nowClsdPos, String jgid, String zyh, List<String> clsdList) {
        //取该时点后面的所有时点
        List<String> nextClsdList = new ArrayList<>();
        while (nowClsdPos < clsdList.size() - 1) {
            nowClsdPos++;
            String nextClsd = clsdList.get(nowClsdPos);
            nextClsdList.add(nextClsd);
        }
        try {
            boolean has = findTodayHasRecordThisCLSDList(nextClsdList, zyh, jgid);
            return has;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public <T> List<T> deepCopyList(List<T> list)
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(list);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    public <T> T deepCopyObject(T obj)
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(obj);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        T dest = (T) in.readObject();
        return dest;
    }
    private List<PersonBloodSugar> findNeedRecordClsdListLogic(String clsd, String jgid, List<PersonBloodSugar> personBloodSugarList) throws Exception {
        List<PersonBloodSugar> personBloodSugarListBack = new ArrayList<>();
        for (PersonBloodSugar personBloodSugar : personBloodSugarList) {
            switch (clsd) {
                case "早餐前":
                    if (personBloodSugar.YZMC.contains(clsd) ||
                            personBloodSugar.YZMC.contains("血糖谱") ||
                            personBloodSugar.YZMC.contains("三餐前")) {
                        if (!findTodayHasRecordThisCLSD(clsd, personBloodSugar.ZYH, jgid)) {
                            personBloodSugar.SDMC = clsd;
                            personBloodSugarListBack.add(personBloodSugar);
                        }
                    }
                    break;
                case "早餐后":
                    if (personBloodSugar.YZMC.contains(clsd) ||
                            personBloodSugar.YZMC.contains("血糖谱") ||
                            personBloodSugar.YZMC.contains("三餐后")) {
                        if (!findTodayHasRecordThisCLSD(clsd, personBloodSugar.ZYH, jgid)) {
                            personBloodSugar.SDMC = clsd;
                            personBloodSugarListBack.add(personBloodSugar);
                        }
                    }
                    break;
                case "午餐前":
                    if (personBloodSugar.YZMC.contains(clsd) ||
                            personBloodSugar.YZMC.contains("血糖谱") ||
                            personBloodSugar.YZMC.contains("三餐前")) {
                        if (!findTodayHasRecordThisCLSD(clsd, personBloodSugar.ZYH, jgid)) {
                            personBloodSugar.SDMC = clsd;
                            personBloodSugarListBack.add(personBloodSugar);
                        }
                    }
                    break;
                case "午餐后":
                    if (personBloodSugar.YZMC.contains(clsd) ||
                            personBloodSugar.YZMC.contains("血糖谱") ||
                            personBloodSugar.YZMC.contains("三餐后")) {
                        if (!findTodayHasRecordThisCLSD(clsd, personBloodSugar.ZYH, jgid)) {
                            personBloodSugar.SDMC = clsd;
                            personBloodSugarListBack.add(personBloodSugar);
                        }
                    }
                    break;
                case "晚餐前":
                    if (personBloodSugar.YZMC.contains(clsd) ||
                            personBloodSugar.YZMC.contains("血糖谱") ||
                            personBloodSugar.YZMC.contains("三餐前")) {
                        if (!findTodayHasRecordThisCLSD(clsd, personBloodSugar.ZYH, jgid)) {
                            personBloodSugar.SDMC = clsd;
                            personBloodSugarListBack.add(personBloodSugar);
                        }
                    }
                    break;

                case "晚餐后":

                    if (personBloodSugar.YZMC.contains(clsd) ||
                            personBloodSugar.YZMC.contains("血糖谱") ||
                            personBloodSugar.YZMC.contains("三餐后")) {
                        if (!findTodayHasRecordThisCLSD(clsd, personBloodSugar.ZYH, jgid)) {
                            personBloodSugar.SDMC = clsd;
                            personBloodSugarListBack.add(personBloodSugar);
                        }
                    }
                    break;
                case "睡前":
                    if (personBloodSugar.YZMC.contains(clsd) ||
                            personBloodSugar.YZMC.contains("血糖谱")) {
                        if (!findTodayHasRecordThisCLSD(clsd, personBloodSugar.ZYH, jgid)) {
                            personBloodSugar.SDMC = clsd;
                            personBloodSugarListBack.add(personBloodSugar);
                        }
                    }
                    break;
                default:
                    //其他
                    if (!personBloodSugar.YZMC.contains("血糖谱") &&
                            !personBloodSugar.YZMC.contains("三餐前") &&
                            !personBloodSugar.YZMC.contains("三餐后")) {
//                                if (!findIsHasCLSD(clsd,personBloodSugar.ZYH,jgid)){
                        personBloodSugar.SDMC = clsd;
                        personBloodSugarListBack.add(personBloodSugar);
//                                }
                    }
            }
        }
        return personBloodSugarListBack;
    }

    /**
     * 判断  传入的多个时点  是否有过记录
     * @param clsdList
     * @param zyh
     * @param jgid
     * @return
     * @throws Exception
     */
    private boolean findTodayHasRecordThisCLSDList(List<String> clsdList, String zyh, String jgid) throws Exception {
        String today = timeService.getNowDateStr(DataSource.MOB);
        String tomorrow = DateUtil.dateoffDays(today, "1");
        List<BloodSugar> list = service.getBloodSugarList(today, tomorrow, zyh, jgid);
        boolean todayHas = false;
        if (list != null && !list.isEmpty()) {
            for (BloodSugar bloodSugar : list) {
                if (clsdList.contains(bloodSugar.CLSD)) {
                    todayHas = true;
                    break;
                }
            }
        }
        return todayHas;
    }
    private boolean findTodayHasRecordThisCLSD(String clsd, String zyh, String jgid) throws Exception {
        String today = timeService.getNowDateStr(DataSource.MOB);
        String tomorrow = DateUtil.dateoffDays(today, "1");
        List<BloodSugar> list = service.getBloodSugarList(today, tomorrow, zyh, jgid);
        boolean todayHas = false;
        if (list != null && !list.isEmpty()) {
            for (BloodSugar bloodSugar : list) {
                if (clsd.equals(bloodSugar.CLSD)) {
                    todayHas = true;
                    break;
                }
            }
        }
        return todayHas;
    }

    /**
     * 新增血糖治疗数据
     *
     * @param zyh  住院号
     * @param sxbq 书写病区
     * @param brch 病人床号
     * @param sxsj 书写时间
     * @param sxgh 书写工号
     * @param cjgh 创建工号
     * @param clsd 测量时点
     * @param clz  测量值
     * @param jgid 机构id
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public BizResponse<String> addBloodSugar(String zyh, String sxbq, String brch, String sxsj, String sxgh,
                                             String cjgh, String clsd, String clz, String jgid) {

        BizResponse<String> response = new BizResponse<>();
        try {
            String jlxh = String.valueOf(identityService.getIdentityMax("IENR_XTCLJL", DataSource.MOB));
            String cjsj = timeService.now(DataSource.HRP);
            //
            String today = DateConvert.getDateString(sxsj);
            String tomorrow;
            try {
                tomorrow = DateUtil.dateoffDays(today, "1");
            } catch (ParseException e) {
                return response;
            }
            /*List<BloodSugar> list = service.getBloodSugarList(today, tomorrow, zyh, jgid);
            boolean todayHas = false;
            if (list != null && !list.isEmpty()) {
                for (BloodSugar bloodSugar : list) {
                    if (clsd.equals(bloodSugar.CLSD)) {
                        todayHas = true;
                        break;
                    }
                }
            }
            if (todayHas) {
                response.isSuccess = false;
                response.message = "今日该测量时点的血糖值已存在!";
                return response;
            }*/
            int count = service.addBloodSugar(jlxh, zyh, sxbq, brch, sxsj, sxgh, cjsj, cjgh, clsd, clz, jgid);
            if (count == -1) {
                response.data = jlxh;
                response.isSuccess = true;
                response.message = "新增血糖治疗数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "新增血糖治疗数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[新增血糖列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[新增血糖列表数据失败]服务内部错误!";
        }

        return response;
    }

    /**
     * 修改血糖治疗数据
     *
     * @param jlxh 记录序号
     * @param clsd 测量时点
     * @param sxsj 书写时间
     * @param clz  测量值
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public BizResponse<String> editBloodSugar(String jlxh, String clsd, String sxsj, String clz) {

        BizResponse<String> response = new BizResponse<>();
        try {
            int count = service.editBloodSugar(jlxh, clsd, sxsj, clz);
            if (count > 0) {
                response.data = "修改血糖治疗数据成功!";
                response.isSuccess = true;
                response.message = "修改血糖治疗数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "修改血糖治疗数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[修改血糖列表数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[修改血糖列表数据失败]服务内部错误!";
        }

        return response;
    }

    /**
     * 删除血糖治疗数据
     *
     * @param jlxh 记录序号
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public BizResponse<String> deleteBloodSugar(String jlxh) {

        BizResponse<String> response = new BizResponse<>();
        try {
            int count = service.deleteBloodSugar(jlxh);
            if (count > 0) {
                response.data = "删除血糖治疗数据成功!";
                response.isSuccess = true;
                response.message = "删除血糖治疗数据成功!";
            } else {
                response.isSuccess = false;
                response.message = "删除血糖治疗数据失败!";
            }
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除血糖治疗数据失败]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[删除血糖治疗数据失败]服务内部错误!";
        }

        return response;
    }
}
