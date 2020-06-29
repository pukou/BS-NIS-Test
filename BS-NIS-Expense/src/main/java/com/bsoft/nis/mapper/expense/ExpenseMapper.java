package com.bsoft.nis.mapper.expense;


import com.bsoft.nis.domain.expense.ExpenseDaysDetail;
import com.bsoft.nis.domain.expense.ExpenseVo;
import com.bsoft.nis.domain.patient.db.ExpenseTotal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public interface ExpenseMapper {

   ExpenseTotal GetTotleExpense(@Param(value = "ZYH")String zyh,@Param(value = "JGID") String jgid);

   List<ExpenseVo> GetDetailExpense(@Param(value = "ZYH")String zyh, @Param(value = "JGID")String jgid);

   List<ExpenseDaysDetail> GetItmeDetailOneDay(@Param(value = "ZYH")String zyh,@Param(value = "KSSJ") String start, @Param(value = "JSSJ")String end, @Param(value = "JGID")String jgid, @Param(value = "dbtype") String dbtype);

   ExpenseTotal GetJKJE(@Param(value = "ZYH")String zyh, @Param(value = "JGID")String jgid);
}
