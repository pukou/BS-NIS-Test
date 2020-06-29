package com.bsoft.nis.adviceexecute.Preprocessing;

import com.bsoft.nis.adviceexecute.ModelManager.ParameterManager;
import com.bsoft.nis.adviceexecute.ModelManager.PrefixInfoManager;
import com.bsoft.nis.domain.adviceexecute.ExecuteArg;
import com.bsoft.nis.domain.adviceexecute.PlanArgInfo;
import com.bsoft.nis.domain.adviceexecute.PrefixInfo;
import com.bsoft.nis.domain.adviceexecute.PreprocessingScannResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 计划预处理
 * 主要完成不同类型计划的完整性处理
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-20
 * Time: 17:06
 * Version:
 */
@Component
public class FinalPreprocessing {

    @Autowired
    PrefixInfoManager prefixInfoManager;//条码前缀管理器

    @Autowired
    ParameterManager parameterManager;//用户参数管理器

    @Autowired
    NursePreprocessing nursePreprocessing;//护理治疗类型预处理

    @Autowired
    OralMedicationPreprocessing oralMedicationPreprocessing;//口服用药预处理

    @Autowired
    TransfusePreprocessing transfusePreprocessing;//输液用药预处理

    @Autowired
    InjectionPreprocessing injectionPreprocessing;//注射用药预处理

    /**
     * 条码前缀
     * 条码前缀(一类型对应多条码)
     */
    private List<PrefixInfo> prefixList;

    /**
     * 归属类型
     */
    private String gslx;

 /*升级编号【56010043】============================================= start
                   条码扫描支持长度
            ================= Classichu 2018/03/07 9:34
            */

    /// <summary>
    /// 条码前缀核对
    /// </summary>
    /// <param name="prefix"></param>
    /// <param name="jgid"></param>
    /// <returns></returns>
    public boolean PrefixCheck(String prefix, String barcode, String jgid) {
        boolean isSelect = false;
        if (this.prefixList != null) {
            if (parameterManager.getParameterMap().get(jgid).PrefixCaseSensitive) {
                for (PrefixInfo item : prefixList) {
                    /***
                     if (item.Agency.equals(jgid) && item.Prefix.equals(prefix)) {
                     isSelect = true;
                     break;
                     }***/

                    if(prefix == null || prefix.equals("")){
                        //按长度来实现
                        if(item.GZNR == null || item.GZNR.equals("") || item.GZNR.toLowerCase().equals("null")) item.GZNR = "0";
                        int len = Integer.parseInt(item.GZNR);

                        if (item.Agency.equals(jgid) && item.TMFL.equals("2") && barcode.length() == len) {
                            isSelect = true;
                            break;
                        }

                    }else{
                        //按前缀来实现
                        if (item.Agency.equals(jgid) && item.Prefix.equals(prefix)) {
                            isSelect = true;
                            break;
                        }
                    }
                }
            } else {
                for (PrefixInfo item : prefixList) {
                    if(prefix == null || prefix.equals("")){
                        //按长度来实现
                        if(item.GZNR == null || item.GZNR.equals("") || item.GZNR.toLowerCase().equals("null")) item.GZNR = "0";
                        int len = Integer.parseInt(item.GZNR);

                        if (item.Agency.equals(jgid) && item.TMFL.equals("2") && barcode.length() == len) {
                            isSelect = true;
                            break;
                        }

                    }else{
                        //按前缀来实现
                        if (item.Agency.equals(jgid) && item.Prefix.toLowerCase().equals(prefix.toLowerCase())) {
                            isSelect = true;
                            break;
                        }
                    }
                }
            }
        }
        return isSelect;
    }

    /* =============================================================== end */

    public List<PrefixInfo> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList() {
        this.prefixList = prefixInfoManager.getPrefixInfoMap().get(gslx);
    }


    /**
     * 计划提交预处理
     *
     * @param zyh             住院号
     * @param yhid            用户id
     * @param planArgInfoList
     * @param jgid            机构id
     * @return
     */
    public List<ExecuteArg> Preprocessing(String zyh, String yhid, List<PlanArgInfo> planArgInfoList, String jgid) {
        List<ExecuteArg> ealist = null;
        if (planArgInfoList == null || planArgInfoList.size() == 0) {
            return ealist;
        }
        ealist = new ArrayList<>();
        //护理治疗
        ealist.addAll(nursePreprocessing.Preprocessing("1", zyh, yhid, planArgInfoList, jgid));
        //口服
        ealist.addAll(oralMedicationPreprocessing.Preprocessing("3", zyh, yhid, planArgInfoList, jgid));
        //输液
        ealist.addAll(transfusePreprocessing.Preprocessing("4", zyh, yhid, planArgInfoList, jgid));
        //注射
        ealist.addAll(injectionPreprocessing.Preprocessing("5", zyh, yhid, planArgInfoList, jgid));

        return ealist;
    }
  /*升级编号【56010043】============================================= start
                   条码扫描支持长度
            ================= Classichu 2018/03/07 9:34
            */
    /**
     * 扫描预处理
     *
     * @param zyh     住院号
     * @param yhid    用户id
     * @param barcode 条码内容
     * @param prefix  条码前缀
     * @param jgid    机构id
     * @return
     */
    public PreprocessingScannResult ScanPreprocessing(String zyh, String yhid, String barcode, String prefix, String jgid) {
        PreprocessingScannResult preprocessingScannResult = null;
        String realGslx = getRealGslx(prefix, barcode, jgid);
        if (realGslx.equals("3")) {
            preprocessingScannResult = oralMedicationPreprocessing.ScanPreprocessing(zyh, yhid, barcode, prefix, jgid);
        } else if (realGslx.equals("4")) {
            preprocessingScannResult = transfusePreprocessing.ScanPreprocessing(zyh, yhid, barcode, prefix, jgid);
        } else if (realGslx.equals("5")) {
            preprocessingScannResult = injectionPreprocessing.ScanPreprocessing(zyh, yhid, barcode, prefix, jgid);
        }
        return preprocessingScannResult;
    }

    /**
     * 计划提交预处理-口服药特有
     *
     * @param zyh  住院号
     * @param yhid 用户id
     * @param kfdh 口服单号
     * @param jgid 机构id
     * @return
     */
    public ExecuteArg OralPreprocessing(String zyh, String yhid, String kfdh, String jgid) {
        return oralMedicationPreprocessing.Preprocessing(zyh, yhid, kfdh, jgid);
    }

    private String getRealGslx(String prefix, String barcode, String jgid) {
        String realGslx = "";
        //需要添加其他归属类型的条码处理只需要在下面数组里面添加对应数据
        String[] gslxArr = new String[]{"3", "4", "5"};
        for (String gslx : gslxArr) {
            this.gslx = gslx;
            setPrefixList();
            if (PrefixCheck(prefix, barcode, jgid)) {
                realGslx = gslx;
                break;
            }
        }
        return realGslx;

    }
    /* =============================================================== end */
    public void getRealExecuteArg(ExecuteArg ea) {
        if (ea.GSLX.equals("4")) {
            transfusePreprocessing.getRealExecuteArg(ea);
        } else if (ea.GSLX.equals("3")) {
            oralMedicationPreprocessing.getRealExecuteArg(ea);
        }
    }


}
