package com.bsoft.nis;


import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 存储过程调用
 * Created by Administrator on 2016/11/2.
 */
public class SpecimenCallStoreProcedure {
    @Autowired
    DataSource dataSource;

    public String GetDocNoBySp(String tmbh)
            throws SQLException {
        Connection dbConnection = dataSource.getConnection();
        CallableStatement callableStatement=null;
        String insertStoreProc = "{call SP_MOB_LIS_GETJYTM(?,?,?)}";
        callableStatement = dbConnection.prepareCall(insertStoreProc);
        callableStatement.setString(1,tmbh);
        callableStatement.registerOutParameter(2, Types.VARCHAR);
        callableStatement.registerOutParameter(3,Types.INTEGER);
        callableStatement.execute();
        Integer code = callableStatement.getInt(3);
        if(code == 1){
            return callableStatement.getString(2);
        }
        return null;

    }
}
