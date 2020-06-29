package com.bsoft.nis.adviceexecute.Preprocessing;

import com.bsoft.nis.domain.adviceexecute.ExecuteArg;
import com.bsoft.nis.domain.adviceexecute.PlanArgInfo;
import com.bsoft.nis.domain.adviceexecute.PreprocessingScannResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: 通用预处理
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-23
 * Time: 16:53
 * Version:
 */
@Component
public class CommonPreprocessing {


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
        return null;
    }

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
        return null;
    }

}
