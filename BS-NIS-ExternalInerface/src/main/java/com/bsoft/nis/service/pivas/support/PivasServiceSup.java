package com.bsoft.nis.service.pivas.support;

import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.pivas.PivasTransDetail;
import com.bsoft.nis.domain.pivas.PivasTransTitle;
import com.bsoft.nis.domain.pivas.PivasTransform;
import com.bsoft.nis.mapper.pivas.PivasMapper;
import com.bsoft.nis.pojo.exchange.BizResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * 静配服务
 * Created by Administrator on 2016/10/18.
 */
@Service
public class PivasServiceSup extends RouteDataSourceService {

    @Autowired
    IdentityService identity;//获取种子服务

    @Autowired
    PivasMapper mapper;

    public Integer getPIVAS(String tmbh) {
        return mapper.getPIVAS(tmbh);
    }

    @Transactional(rollbackFor = Exception.class)
    public BizResponse<String> operation(PivasTransTitle pivasTitle, List<PivasTransform> list_form)
            throws Exception {
        BizResponse<String> bizResponse = new BizResponse<>();
        for (PivasTransform pivasTransform : list_form) {
            if (pivasTransform.patientid == null || pivasTransform.patientid.equals("")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "patientid 为空";
                return bizResponse;
            }
            if (pivasTransform.patientareaid == null || pivasTransform.patientareaid.equals("")) {
                bizResponse.isSuccess = false;
                bizResponse.message = "patientareaid 为空";
                return bizResponse;
            }

            //验证条码是否存在
            Integer num = getPIVAS(pivasTransform.barcodeno);
            if (num > 0) {
                bizResponse.isSuccess = false;
                bizResponse.message = "条码(" + pivasTransform.barcodeno + ")已经存在!";
                return bizResponse;
            }

            pivasTransform.SYDH = identity.getIdentityMax("IENR_SYD", 1, DataSource.MOB).datalist.get(0);
            addSYD(pivasTransform);
            if (pivasTransform.transdetails.size() > 0) {
                for (PivasTransDetail pivasTransDetail : pivasTransform.transdetails) {
                    pivasTransDetail.SYMX = identity.getIdentityMax("IENR_SYMX", 1,DataSource.MOB).datalist.get(0);
                    pivasTransDetail.SYDH = pivasTransform.SYDH.toString();
                    addSYMX(pivasTransDetail);
                }
            }

        }
        bizResponse.isSuccess = true;
        return bizResponse;
    }

    private Boolean addSYMX(PivasTransDetail pivasTransDetail)
            throws SQLException, DataAccessException {
        return mapper.addSYMX(pivasTransDetail);
    }

    private Boolean addSYD(PivasTransform pivasTransform)
            throws SQLException, DataAccessException {
        return mapper.addSYD(pivasTransform);
    }
}
