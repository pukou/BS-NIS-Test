package com.bsoft.nis.domain.nurserecord.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:特殊符号引用
 * Created: dragon
 * Date： 2016/11/28.
 */
public class RefContentClassification implements Serializable{
    private static final long serialVersionUID = 5722584295542227136L;
    public String LBBH;
    public String LBMC;
    public List<RefContent> Items = new ArrayList<>();
}
