package com.bsoft.nis.domain.nurserecord;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Describtion:引用联合数据
 * Created: dragon
 * Date： 2016/11/1.
 */
public class Association implements Serializable{
    private static final long serialVersionUID = -4130766072582898754L;

    private List<RefrenceValue> values;

    private int pageIndex;

    private int pageSize;

    @JsonProperty(value = "values")
    public List<RefrenceValue> getValues() {
        return values;
    }

    public void setValues(List<RefrenceValue> values) {
        this.values = values;
    }

    @JsonProperty(value = "pageIndex")
    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @JsonProperty(value = "pageSize")
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
