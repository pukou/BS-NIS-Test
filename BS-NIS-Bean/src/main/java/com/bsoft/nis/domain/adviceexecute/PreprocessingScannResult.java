package com.bsoft.nis.domain.adviceexecute;

import java.util.List;

/**
 * Description: 条码扫描结果
 * User: 田孝鸣(tianxm@bsoft.com.cn)
 * Date: 2016-12-19
 * Time: 14:10
 * Version:
 */
public class PreprocessingScannResult {
    public boolean Success;
    public String Message;
    public List<ExecuteArg> ExcutArg;

    public PreprocessingScannResult(List<ExecuteArg> excutArg) {
        this.ExcutArg = excutArg;
        this.Success = true;
    }

    public PreprocessingScannResult(String message) {
        this.Message = message;
        this.Success = false;
    }
}
