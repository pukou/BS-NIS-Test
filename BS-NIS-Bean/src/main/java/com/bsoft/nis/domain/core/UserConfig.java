package com.bsoft.nis.domain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserConfig {

    //
    @JsonIgnore
    public static final String DEFAULT_STRING_NEGATIVE = "-666";
    @JsonIgnore
    public static final String DEFAULT_STRING_EMPTY = "";
    @JsonIgnore
    public static final String DEFAULT_STRING_NULL = null;

    public boolean qiYong_JingTui;
    public String jingTui_YaoPinYongFa;
    public boolean qiYong_WeiZhuBenTuiZhu;
    public String weiZhuBenTuiZhu_YaoPinYongFa;
    public boolean qiYong_SY_CancelToIngOperation;
    public boolean qiYong_SY_CancelToStartOperation;
    public boolean qiYong_SY_LSBS_Show;
    //
    public boolean qiYong_Only_Sync_24;
    public boolean syncSaveJHH;

    public boolean navMenuShowByIcon;
    public boolean floatMenuShowByIcon;

    public boolean syncDealYZMC;
    public boolean qiYong_KF_CancelToStartOperation;
    public boolean qiYong_ZS_CancelToStartOperation;
}
