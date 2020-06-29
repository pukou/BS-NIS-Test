package com.bsoft.nis.core.datasource;

/**
 * 数据库类型
 * Created by Administrator on 2016/10/11.
 */
public enum DataSource {
    /**
     * HIS数据库
     */
    HRP,

    /**
     * 门户数据库
     */
    PORTAL,

    /**
     * 护理记录数据库
     */
    ENR,

    /**
     * 电子病历数据库
     */
    EMR,

    /**
     * 检验数据库
     */
    LIS,

    /**
     * 检查数据库（RIS,UIS）
     */
    RIS,

    /**
     * 移动数据库
     */
    MOB,
    /**
     * 包药机
     */
    OMS,
    /**
     * 静配中心
     */
    PIVAS
}
