package com.bsoft.nis.service.inspection.support;

import com.bsoft.nis.domain.inspection.*;
import com.bsoft.nis.domain.inspection.db.SpecimenJYTM;
import com.bsoft.nis.mapper.inspection.InspectionMapper;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标本采集服务
 * Created by Administrator on 2016/10/10.
 */
@Service
public class SpecimenService extends RouteDataSourceService {

    @Autowired
    InspectionMapper mapper;

    /**
     * 获取采集数据
     *
     * @param zyhm
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SpecimenVo> GetCaptureData(String zyhm, String jgid,String ryrq)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.GetCaptureData(zyhm, jgid,ryrq,dbtype);
    }
    public List<SpecimenVo> GetCaptureDataList(List<String> zyhmList, String jgid,String ryrq)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.GetCaptureDataList(zyhmList, jgid,ryrq,dbtype);
    }

    /**
     * 获取压史标本采集数据
     *
     * @param zyhm
     * @param start
     * @param end
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<SpecimenVo> GetHistoryCaptureData(String zyhm, String start, String end, String jgid)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.GetHistoryCaptureData(zyhm, start, end, jgid,dbtype);

    }
    public List<SpecimenVo> GetHistoryCaptureDataList(List<String> zyhmList, String start, String end, String jgid)
            throws SQLException, DataAccessException {
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.GetHistoryCaptureDataList(zyhmList, start, end, jgid,dbtype);

    }
    /**
     * 转换条码
     *
     * @param map
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public String GetDocNoBySp(Map<String, Object> map)
            throws SQLException, DataAccessException {
        try {
            List<List<?>> results = mapper.GetDocNoBySp(map);
            String code = map.get("VN_RET").toString();
            if (code != null && code.equals("1")) {
                return map.get("VV_OUTDOCNO").toString();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public SpecimenJYTM getJYTM(String tmbh)
            throws SQLException, DataAccessException{
        return mapper.getJYTM(tmbh);
    }

    public Map<String, String> CaptureExecute(Map<String, Object> zxparms)
            throws SQLException, DataAccessException{
        Map<String,String> map = new HashMap<>();
        try {
            List<?> results = mapper.CaptureExecute(zxparms);
            String code = zxparms.get("VN_RET").toString();
            if (code != null && code.equals("1")) {
                map.put("code","1");
                map.put("messge","执行成功");
                return map;
            }
            String message = zxparms.get("VV_RETMSG").toString();
            map.put("code","0");
            map.put("message",message);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Map<String, String> GetSQBH(String tmbh,String jgid)
            throws SQLException, DataAccessException{
        return mapper.GetSQBH(tmbh,jgid);

    }


    public void UpdateYZB(Date hszxsj, String zxhsgh, String sqdh, String zyh, String jgid)
            throws SQLException, DataAccessException{
        mapper.UpdateYZB(hszxsj,zxhsgh,sqdh,zyh,jgid);

    }

    public Map<String, Object> DoubleCheck(String tmbh, String jgid)
            throws SQLException, DataAccessException{
        return mapper.DoubleCheck(tmbh,jgid);
    }
    public List<CYInfoBean> GetCYInfoByTMBH(String tmbh, String brid)
            throws SQLException, DataAccessException{
        return mapper.GetCYInfoByTMBH(tmbh,brid);
    }

    public List<Long> GetYZXH(String tmbh, String jgid)
            throws SQLException, DataAccessException{
        return mapper.GetYZXH(tmbh,jgid);

    }

    public void UpdateYZB2(Date hszxsj, String zxhsgh, List<Long> yzbxhList)
            throws SQLException, DataAccessException{
        mapper.UpdateYZB2(hszxsj,zxhsgh, yzbxhList);
    }

}
