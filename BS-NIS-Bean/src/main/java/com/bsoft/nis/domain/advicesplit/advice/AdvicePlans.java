package com.bsoft.nis.domain.advicesplit.advice;

import com.bsoft.nis.domain.advicesplit.advice.db.AdvicePlan;

import java.io.Serializable;
import java.util.List;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/12/19.
 */
public class AdvicePlans implements Serializable{
    public String dbtype;
    public List<AdvicePlan> plans;
}
