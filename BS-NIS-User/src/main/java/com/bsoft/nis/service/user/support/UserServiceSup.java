package com.bsoft.nis.service.user.support;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.core.barcode.Barcode;
import com.bsoft.nis.domain.user.PDAInfo;
import com.bsoft.nis.domain.user.db.Agency;
import com.bsoft.nis.domain.user.db.LoginUser;
import com.bsoft.nis.mapper.user.UserServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
@Service
public class UserServiceSup extends RouteDataSourceService{
    @Autowired
    UserServiceMapper mapper;

    /**
     * 获取机构列表接口(如传入空值，获取所有机构列表，如传入员工代码,则获取该工号关联的机构列表)
     *
     * @param urid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public List<Agency> getAgency(String urid)
            throws SQLException, DataAccessException {
        return mapper.getAgency(urid);
    }

    /**
     * 获取PDAINFO信息
     * @param manuer
     * @param model
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public PDAInfo getPDAInfo(String manuer, String model)throws SQLException, DataAccessException {
        return mapper.getPDAInfo(manuer, model);
    }

    /**
     * 新增一条PDAINFO信息
     * @param manuer
     * @param model
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public int addPDAInfo(String manuer, String model)throws SQLException, DataAccessException {
        return mapper.addPDAInfo(manuer, model);
    }

    /**
     * 根据员工标号获取员工
     *
     * @param urid
     * @param jgid
     * @return
     */
    @Transactional(readOnly = true)
    public LoginUser getUserByYGBH(String urid, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getUserByYGBH(urid, jgid);
    }

    /**
     * 根据用户代码(内码)获取用户
     *
     * @param ygdm
     * @param jgid
     * @return
     */
    @Transactional(readOnly = true)
    public LoginUser getUserByYGDM(String ygdm, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getUserByYGDM(ygdm, jgid);
    }

    /**
     * 根据员工编号、密码获取员工
     *
     * @param urid
     * @param jgid
     * @param md5Str
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Transactional(readOnly = true)
    public LoginUser getUserByYGBHAndPwd(String urid, String jgid, String md5Str)
            throws SQLException, DataAccessException {
        return mapper.getUserByYGBHAndPwd(urid, jgid, md5Str);
    }

    /**
     * 根据胸卡获取员工代码(内码)
     *
     * @param guid
     * @param jgid
     * @return
     */
    @Transactional(readOnly = true)
    public LoginUser getUserByCard(String guid, String jgid)
            throws SQLException, DataAccessException {
        return mapper.getUserByCard(guid, jgid);
    }

    /**
     * 获取条码设定信息
     *
     * @return
     */
    public List<Barcode> GetTmzd()
            throws SQLException, DataAccessException {
        keepOrRoutingDateSource(DataSource.MOB);
        return mapper.GetTmzd();
    }


}
