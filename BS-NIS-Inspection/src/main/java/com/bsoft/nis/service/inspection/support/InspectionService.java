package com.bsoft.nis.service.inspection.support;

import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.inspection.*;
import com.bsoft.nis.mapper.inspection.InspectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 检验检查查询服务
 * Created by Administrator on 2016/10/10.
 */
@Service
public class InspectionService extends RouteDataSourceService {


    @Autowired
    InspectionMapper mapper;


    /**
     * 获取检验结果列表
     *
     * @param zyhm
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<InspectionVo> GetInspectionList(String zyhm, String jgid)
            throws SQLException, DataAccessException {
        return mapper.GetInspectionList(zyhm, jgid);
    }


    /**
     * 获取检验详细信息
     *
     * @param ybhm
     * @param jgid
     * @return
     */
    public List<InspectionDetailVo> GetInspectionDetail(String ybhm, String jgid)
            throws SQLException, DataAccessException {
        return mapper.GetInspectionDetail(ybhm, jgid);
    }
    /*
                升级编号【56010025】============================================= start
                检验检查：检验List项目数据趋势图，项目分类查看
                ================= Classichu 2017/10/18 9:34
                */
    public List<InspectionXMBean> GetInspectionXMBeanList(String xmid, String zyhm, String jgid)
            throws SQLException, DataAccessException {
        return mapper.GetInspectionXMBeanList(xmid,zyhm, jgid);
    }
   /* =============================================================== end */

    public List<ExamineVo> GetRisList(String zyhm, String jgid)
            throws SQLException, DataAccessException {
        return mapper.GetRisList(zyhm, jgid);
    }

    public List<ExamineDetailVo> GetRisDetail(String djbs, String jgid)
            throws SQLException, DataAccessException {
        return mapper.GetRisDetail(djbs, jgid);
    }
}
