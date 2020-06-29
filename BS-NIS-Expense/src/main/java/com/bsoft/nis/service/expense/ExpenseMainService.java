package com.bsoft.nis.service.expense;


import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.domain.expense.ExpenseRespose;
import com.bsoft.nis.domain.patient.db.ExpenseTotal;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.expense.support.ExpenseService;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * 费用主服务
 * Created by Administrator on 2016/10/10.
 */
@Service
public class ExpenseMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(ExpenseMainService.class);

    @Autowired
    ExpenseService service; // 费用服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器


    public BizResponse<ExpenseRespose> GetCharge(String zyh, String start, String end, String jgid) {
        keepOrRoutingDateSource(DataSource.HRP);
        ExpenseRespose expenseRespose = new ExpenseRespose();
        BizResponse<ExpenseRespose> bizResponse = new BizResponse<>();
        ExpenseTotal total,total1 = new ExpenseTotal();
        try {
            total = service.GetTotleExpense(zyh, jgid);
            total1 = service.GetJKJE(zyh,jgid);
	        if (total == null) {
		        total = new ExpenseTotal();
	        }
	        if (total1 == null) {
		        total1 = new ExpenseTotal();
	        }
            if(total.ZJJE == null || total.ZJJE.equals("")){
                total.ZJJE = "0";
            }
            if(total.ZFJE == null || total.ZFJE.equals("")){
                total.ZFJE = "0";
            }
            total.JKJE = "0";
            total.FYYE = "0";
            if(total1.JKJE == null || total1.JKJE.equals("")){
                total1.JKJE = "0";
            }
            total.JKJE = total1.JKJE;
            DecimalFormat df = new DecimalFormat("0.00");
            Double i1 = Double.parseDouble(total.JKJE);
            Double i2 = Double.parseDouble(total.ZFJE);
            total.FYYE = df.format((i1 * 100 - i2 * 100)/100);
            total.ZJJE = df.format(Double.parseDouble(total.ZJJE));
            total.JKJE = df.format(Double.parseDouble(total.JKJE));
            total.ZFJE = df.format(Double.parseDouble(total.ZFJE));
            expenseRespose.Table1 = total;
            expenseRespose.Table2 = service.GetDetailExpense(zyh, jgid);
            expenseRespose.Table3 = service.GetItmeDetailOneDay(zyh, start, end, jgid);
            if(expenseRespose.Table2.size()>0){
                for(int i=0;i < expenseRespose.Table2.size();i++ ){
                    expenseRespose.Table2.get(i).ZFJE = df.format(Double.parseDouble(expenseRespose.Table2.get(i).ZFJE));
                    expenseRespose.Table2.get(i).ZJJE = df.format(Double.parseDouble(expenseRespose.Table2.get(i).ZJJE));
                }
            }
            if(expenseRespose.Table3.size()>0){
                for(int i=0;i < expenseRespose.Table3.size();i++ ){
                    expenseRespose.Table3.get(i).FYDJ = df.format(Double.parseDouble(expenseRespose.Table3.get(i).FYDJ));
                    expenseRespose.Table3.get(i).ZJJE = df.format(Double.parseDouble(expenseRespose.Table3.get(i).ZJJE));
                }
            }
            bizResponse.data = expenseRespose;
            bizResponse.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取费用错误!";
        }
        return bizResponse;
    }

    public BizResponse<ExpenseRespose> GetDetailOneDay(String zyh, String start, String end, String jgid) {
        keepOrRoutingDateSource(DataSource.HRP);
        BizResponse<ExpenseRespose> bizResponse = new BizResponse<>();
        ExpenseRespose expenseRespose = new ExpenseRespose();
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(end));
            cal.add(Calendar.DAY_OF_MONTH,1);
            end = sdf.format(cal.getTime());
            expenseRespose.Table3 = service.GetItmeDetailOneDay(zyh, start, end, jgid);
            if(expenseRespose.Table3.size()>0){
                for(int i=0;i < expenseRespose.Table3.size();i++ ){
                    expenseRespose.Table3.get(i).FYDJ = df.format(Double.parseDouble(expenseRespose.Table3.get(i).FYDJ));
                    expenseRespose.Table3.get(i).ZJJE = df.format(Double.parseDouble(expenseRespose.Table3.get(i).ZJJE));
                }
            }
            bizResponse.data = expenseRespose;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取费用错误!";
        }
        bizResponse.isSuccess = true;
        return bizResponse;
    }
}
