package com.bsoft.nis.advicesplit;

import com.bsoft.nis.advicesplit.args.DeptArgs;
import com.bsoft.nis.advicesplit.args.ExcuteResults;
import com.bsoft.nis.advicesplit.args.PatientArgs;
import com.bsoft.nis.advicesplit.commit.ResultCommitControll;
import com.bsoft.nis.advicesplit.exception.CommitException;
import com.bsoft.nis.advicesplit.exception.NoPatientException;
import com.bsoft.nis.advicesplit.exception.SplitException;
import com.bsoft.nis.advicesplit.exception.WrapperException;
import com.bsoft.nis.advicesplit.prepare.PrepareControll;
import com.bsoft.nis.advicesplit.split.AdviceSplitControll;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.pojo.exchange.BizResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Describtion:医嘱拆分执行器
 * Created: dragon
 * Date： 2016/12/1.
 */
@Component
@Scope(value = "prototype")
public class AdviceSplitExcutor extends AbstractPatientManage{
    private Log logger = LogFactory.getLog(AdviceSplitExcutor.class);

    @Autowired
    PrepareControll prepareControll;   // 准备控制器

    @Autowired
    AdviceSplitControll splitControll; // 拆分控制器

    @Autowired
    ResultCommitControll commitControll; // 提交控制器

    // 医嘱拆分需要的参数
    public DeptArgs papareExcutorParams = new DeptArgs();
    // 参数ID
    public String paramid ;
    // 机构ID
    public String jgid ;
    // 开始日期
    public String ksrq;
    // 结束日期
    public String jsrq;
    // 是否单病人执行
    public Boolean isSinglePatient;

    /**
     * 单病人医嘱拆分
     * @param zyh
     * @param jgid
     * @return
     */
    public BizResponse<String> excuteSplitForOnePatient(String zyh,String jgid,String ksrq,String jsrq){
        BizResponse<String> response = new BizResponse<>();
        this.paramid = zyh;
        this.jgid = jgid;
        this.ksrq = ksrq;
        this.jsrq = jsrq;
        isSinglePatient = true;
        LocalDateTime start = LocalDateTime.now();

        // 拆分医嘱数据准备阶段
        try {
            praparingAdviceSplitDataFromPatient(zyh,jgid,ksrq,jsrq);
        } catch (WrapperException e) {
            logger.error(e.getMessage(),e);
            response.isSuccess = false;
            response.message = "医嘱拆分数据准备阶段出错!";
            return response;
        }catch (NoPatientException e){
            logger.error(e.getMessage(),e);
            response.isSuccess = true;
            response.message ="医嘱拆分数据准备阶段->无拆分病人!";
            return response;
        }

        // 医嘱拆分阶段
        // 1.校验是否存在拆分病人
        if (papareExcutorParams.getPatientWrappers().size()<=0) {
            response.isSuccess = true;
            response.message ="无拆分病人!";
            return response;
        }
        // 2.开始按病人拆分
        // 部分病人拆分失败，不影响其他的病人的拆分
        List<PatientArgs> patientArgses = papareExcutorParams.getPatientWrappers();
        for (PatientArgs patientArgs:patientArgses){
            try {
                // 判断医嘱是否存于正在其他线程拆分，若正在拆分的病人，不再拆分
                if (this.exsitInPool(patientArgs)) continue;
                this.addOnePatientIntoPool(patientArgs);
                this.excuteSplit(patientArgs);
                this.commit(patientArgs);
            }catch (SplitException e){
                logger.error(e.getMessage(),e);
                continue;
            }catch (CommitException e){
                logger.error(e.getMessage(),e);
                continue;
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                continue;
            }finally {
                this.releaseOnePatientFromPool(patientArgs);
            }
        }

        LocalDateTime end = LocalDateTime.now();
        System.out.println(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS")));
        System.out.println(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS")));
        response.isSuccess = true;
        response.message = "执行成功!";
        return response;
    }

    /**
     * 单科室医嘱拆分
     * @param ksdm
     * @param jgid
     * @return
     */
    public BizResponse<String> excuteSplitForOneDept(String ksdm,String jgid,String ksrq,String jsrq){
        BizResponse<String> response = new BizResponse<>();
        this.paramid = ksdm;
        this.jgid = jgid;
        this.ksrq = ksrq;
        this.jsrq = jsrq;
        isSinglePatient = false;
        LocalDateTime start = LocalDateTime.now();

        // 数据准备阶段
        try {
            praparingAdviceSplitDataFromDept(ksdm, jgid, ksrq, jsrq);
        }catch (WrapperException e){
            logger.error(e.getMessage(),e);
            response.isSuccess = false;
            response.message = "医嘱拆分数据准备阶段出错!";
            return response;
        }catch (NoPatientException e){
            logger.error(e.getMessage(),e);
            response.isSuccess = true;
            response.message ="医嘱拆分数据准备阶段->无拆分病人!";
            return response;
        }

        // 医嘱拆分阶段(按单病人拆分)
        // 2.开始按病人拆分
        // 部分病人拆分失败，不影响其他的病人的拆分
        List<PatientArgs> patientArgses = papareExcutorParams.getPatientWrappers();
        for (PatientArgs patientArgs:patientArgses){
            try {
                // 判断医嘱是否存于正在其他线程拆分，若正在拆分的病人，不再拆分
                if (this.exsitInPool(patientArgs)) continue;
                this.addOnePatientIntoPool(patientArgs);
                this.excuteSplit(patientArgs);
                this.commit(patientArgs);
            }catch (SplitException e){
                logger.error(e.getMessage(),e);
                continue;
            }catch (CommitException e){
                logger.error(e.getMessage(),e);
                continue;
            }finally {
                this.releaseOnePatientFromPool(patientArgs);
            }
        }

        LocalDateTime end = LocalDateTime.now();
        System.out.println(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS")));
        System.out.println(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS")));

        response.isSuccess = true;
        response.message = "执行成功!";
        return response;
    }

    @Override
    public void praparingAdviceSplitDataFromPatient(String zyh,String jgid,String ksrq,String jsrq) throws WrapperException,NoPatientException{
        prepareControll.init(this);
        try{
            prepareControll.prepareWrapper(zyh,true,jgid,ksrq,jsrq);
        }catch (WrapperException ex){
            throw ex;
        }catch (NoPatientException ex){
            throw ex;
        }
    }

    @Override
    public void praparingAdviceSplitDataFromDept(String bqdm,String jgid,String ksrq,String jsrq) throws WrapperException,NoPatientException{
        prepareControll.init(this);
        try{
            prepareControll.prepareWrapper(bqdm,false,jgid,ksrq,jsrq);
        }catch (WrapperException ex){
            throw ex;
        }catch (NoPatientException ex){
            throw ex;
        }

    }

    @Override
    public void excuteSplit() throws SplitException{
        BizResponse<Boolean> response ;
        splitControll.init(this);
        List<PatientArgs> patientArgs = papareExcutorParams.getPatientWrappers();
        //response = splitControll.split();
    }
    @Override
    public void excuteSplit(PatientArgs patientArg) throws SplitException{
        splitControll.init(this);
        try{
            splitControll.split(patientArg);
        }catch (SplitException e){
            throw e;
        }
    }

    @Override
    public void commit(PatientArgs patientArg)throws CommitException{
        commitControll.init(this);
        try{
            commitControll.commit(patientArg);
        }catch (CommitException e){
            throw e;
        }
    }


    @Override
    public void commit() throws CommitException{
        return ;
    }
}
