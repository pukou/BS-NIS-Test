package com.bsoft.nis.service.nurserecord;

import com.bsoft.nis.domain.nurserecord.db.Structure;
import com.bsoft.nis.domain.nurserecord.db.Template;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.nurserecord.support.NurseRecordConfigServiceSup;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.util.list.ListSelect;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 护理记录配置主服务
 * Describtion:
 * Created: dragon
 * Date： 2016/10/20.
 */
@Service
public class NurseRecordConfigService extends RouteDataSourceService{

    private Log logger = LogFactory.getLog(NurseRecordConfigService.class);

    @Autowired
    NurseRecordConfigServiceSup service;

    /**
     * 获取护理记录类型列表
     * @param bqid
     * @param jgid
     * @param sysType
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public BizResponse<Structure> getNurseRecordStructureList(String bqid, String jgid, int sysType) {
        BizResponse<Structure> response = new BizResponse<>();
        List<Structure> structures,structures1 ;  // 病历类别
        List<Template> templates ;    // JG01

        try{
            // EMR病历类别
            structures1 = new ArrayList<>();
            keepOrRoutingDateSource(DataSource.EMR);
            structures = service.getNurseRecordStructureList(bqid, jgid, sysType);
            // ENR护理病历结构
            keepOrRoutingDateSource(DataSource.ENR);
            for (Structure structure:structures){
                templates = service.getNurseRecordTemplateList(bqid,structure.LBBH,jgid,sysType);
                if (templates.size() > 0){
                    structures1.add(structure);
                }
            }

            response.datalist = structures1;
            response.isSuccess = true;
            response.message = "获取成功!";
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录类型]数据库查询错误!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录类型]服务内部错误!";
        }
        return response;
    }

    /**
     * 获取护理结构列表(enr_jg01)
     * @param bqid
     * @param lbbh
     * @param jgid
     * @param sysType
     * @return
     */
    public BizResponse<Template> getNurseRecordTemplateList(String bqid, String lbbh, String jgid, int sysType) {
        BizResponse<Template> response = new BizResponse<>();
        List<Structure> bllbs = new ArrayList<>();
        List<Template> jg01s = new ArrayList<>();
        Structure structure = new Structure();

        if (StringUtils.isBlank(bqid))
            bqid = null;
        if (StringUtils.isBlank(lbbh))
            lbbh = null;

        try{
            // 获取结构01数据
            keepOrRoutingDateSource(DataSource.ENR);
            jg01s = service.getNurseRecordTemplateList(bqid, lbbh, jgid, sysType);

            // 获取EMR病历类别数据
            if (jg01s.size()>0){
                keepOrRoutingDateSource(DataSource.EMR);
                bllbs = service.getStructrueListForNurse();

                for (Template template:jg01s){
                    if (!StringUtils.isBlank(template.BLLB)){
                        List<Structure> list = ListSelect.select(structure, bllbs, "LBBH", template.BLLB);
                        if (list.size() > 0){
                            template.BLLBMC = list.get(0).LBMC;
                        }
                    }

                    if (!StringUtils.isBlank(template.MBLB)){
                        List<Structure> list = ListSelect.select(structure,bllbs,"LBBH",template.MBLB);
                        if (list.size() > 0){
                            template.MBLBMC = list.get(0).LBMC;
                        }
                    }
                }
            }

            response.datalist = jg01s;
            response.isSuccess = true;
            response.message = "获取护理记录结构成功!";
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录结构]数据库查询错误!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录结构]服务内部错误!";
        }

        return response;
    }

    /**
     * 根据结构编号获取记录模板
     * @param jgbh
     * @return
     */
    public BizResponse<List<Template>> getNurseRecordTemplateByJgbh(String jgbh){
        keepOrRoutingDateSource(DataSource.ENR);
        BizResponse<List<Template>> response = new BizResponse<>();

        try{
            List<Template> list = service.getNurseRecordTemplateByJgbh(jgbh);
            response.data = list;
            response.isSuccess = true;
            response.message = "获取成功!";
        }catch (SQLException | DataAccessException e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[ENR_JG01]数据库查询错误!";
        }catch (Exception e){
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[ENR_JG01]服务内部错误!";
        }
        return response;
    }
}
