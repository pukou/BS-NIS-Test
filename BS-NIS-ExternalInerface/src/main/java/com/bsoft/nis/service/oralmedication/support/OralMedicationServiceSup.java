package com.bsoft.nis.service.oralmedication.support;

import com.bsoft.nis.common.service.IdentityService;
import com.bsoft.nis.core.datasource.DataBaseTypeHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.oralmedication.OMSPackage;
import com.bsoft.nis.domain.oralmedication.OMSTablet;
import com.bsoft.nis.domain.oralmedication.OMSTitle;
import com.bsoft.nis.mapper.oralmedication.OralMedicationMapper;
import com.bsoft.nis.pojo.exchange.BizResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 包药机服务
 * Created by Administrator on 2016/10/18.
 */
@Service
public class OralMedicationServiceSup extends RouteDataSourceService {

    @Autowired
    IdentityService identity;//获取种子服务

    @Autowired
    OralMedicationMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public BizResponse<String> operation(OMSTitle omsTitle) throws Exception {

        BizResponse<String> bizResponse = new BizResponse<>();
        //ENR_KFD Insert
        //数据校验
        if (omsTitle.autoid == null || omsTitle.autoid.equals("")) {
            bizResponse.isSuccess = false;
            bizResponse.message = "口服单号为空";
            return bizResponse;
        }

        if (omsTitle.patientid == null || omsTitle.patientid.equals("")) {
            bizResponse.isSuccess = false;
            bizResponse.message = "住院号为空";
            return bizResponse;
        }

        if (omsTitle.areaid == null || omsTitle.areaid.equals("")) {
            bizResponse.isSuccess = false;
            bizResponse.message = "病人病区为空";
            return bizResponse;
        }

        if (omsTitle.bedid == null || omsTitle.bedid.equals("")) {
            bizResponse.isSuccess = false;
            bizResponse.message = "病人床号为空";
            return bizResponse;
        }
        if (omsTitle.packages.size() <= 0) {
            bizResponse.isSuccess = false;
            bizResponse.message = "数据中至少要包含一条 Package";
            return bizResponse;
        }

        omsTitle.KFDH = identity.getIdentityMax("IENR_KFD", 1, DataSource.MOB).datalist.get(0);

        addOMSTitle(omsTitle);

        String tmpStr = "";
        // ENR_KFBZ Insert
        for (OMSPackage omsPackage : omsTitle.packages) {
            if (omsPackage.tablets.size() <= 0) {
                throw new Exception("Package数据中至少要包含一条 Tablet");
            }

            int omsCnt = getOMSCntByTmbh(omsPackage.packagecode);
            if( omsCnt > 0){
                //throw new Exception("条码编号(" + omsPackage.packagecode + ")已存在！");
                tmpStr += omsPackage.packagecode + ",";
                continue;
            }

            omsPackage.KFMX = identity.getIdentityMax("IENR_KFBZ", 1,DataSource.MOB).datalist.get(0);
            omsPackage.KFDH = omsTitle.KFDH.toString();
            addOMSPackage(omsPackage);

            // ENR_BZMX Insert
            for (OMSTablet omsTablet : omsPackage.tablets) {
                omsTablet.BZMX = identity.getIdentityMax("IENR_BZMX", 1,DataSource.MOB).datalist.get(0);
                omsTablet.KFDH = omsTitle.KFDH.toString();
                omsTablet.KFMX = omsPackage.KFMX.toString();
                addOMSTablet(omsTablet);
            }
        }
        bizResponse.isSuccess = true;
        if(!"".equals(tmpStr)) {
            bizResponse.message = "重复条码:" + tmpStr.substring(0, tmpStr.length() - 1);
        }
        return bizResponse;

    }

    private Boolean addOMSTablet(OMSTablet omsTablet)
            throws SQLException, DataAccessException {
        return mapper.addOMSTablet(omsTablet);
    }

    private Boolean addOMSPackage(OMSPackage omsPackage)
            throws SQLException, DataAccessException {
        return mapper.addOMSPackage(omsPackage);
    }

    private Boolean addOMSTitle(OMSTitle omsTitle)
            throws SQLException, DataAccessException {
        omsTitle.dbtype = this.getCurrentDataSourceDBtype().toLowerCase();
        return mapper.addOMSTitle(omsTitle);
    }

    private int getOMSCntByTmbh(String tmbh) throws SQLException, DataAccessException{
        return mapper.getOMSCntByTmbh(tmbh);
    }

    public int addXTJCJL(String zyh, String brbq, String brch, String cjsj, String cjgh, String clsd, String clz, String xml)
            throws SQLException, DataAccessException{

        long jlxh = identity.getIdentityMax("IENR_XTCLJL", 1, DataSource.MOB).datalist.get(0);

        keepOrRoutingDateSource(DataSource.MOB);
        String dbtype = this.getCurrentDataSourceDBtype();
        return mapper.addXTJCJL(jlxh, zyh, brbq, brch, cjsj, cjgh, clsd, clz, xml, dbtype);
    }
}
