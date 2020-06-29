package com.bsoft.nis.service.expense.support;


import com.bsoft.nis.domain.expense.ExpenseDaysDetail;
import com.bsoft.nis.domain.expense.ExpenseVo;
import com.bsoft.nis.domain.patient.db.ExpenseTotal;
import com.bsoft.nis.mapper.expense.ExpenseMapper;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 费用服务
 * Created by Administrator on 2016/10/10.
 */
@Service
public class ExpenseService extends RouteDataSourceService{


    @Autowired
    ExpenseMapper mapper;

    /**
     * 获取总付金额
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public ExpenseTotal GetTotleExpense(String zyh, String jgid)
            throws SQLException,DataAccessException {
        return mapper.GetTotleExpense(zyh,jgid);
    }

    /**
     * 取明细费用
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<ExpenseVo> GetDetailExpense(String zyh, String jgid)
            throws SQLException,DataAccessException{
        return  mapper.GetDetailExpense(zyh, jgid);
    }

    /**
     * 按天获取项目明细
     * @param zyh
     * @param start
     * @param end
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public List<ExpenseDaysDetail> GetItmeDetailOneDay(String zyh, String start, String end, String jgid)
            throws SQLException,DataAccessException{
        String dbtype = getCurrentDataSourceDBtype();
        return mapper.GetItmeDetailOneDay(zyh, start, end, jgid,dbtype);
    }

    /**
     * 获取缴付金额
     * @param zyh
     * @param jgid
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    public ExpenseTotal GetJKJE(String zyh, String jgid)
            throws SQLException,DataAccessException{
        return mapper.GetJKJE(zyh,jgid);
    }
}
