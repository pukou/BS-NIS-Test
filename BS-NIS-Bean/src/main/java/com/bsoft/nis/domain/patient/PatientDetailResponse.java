package com.bsoft.nis.domain.patient;

import com.bsoft.nis.domain.patient.db.AllergicDrug;
import com.bsoft.nis.domain.patient.db.ExpenseTotal;
import com.bsoft.nis.domain.patient.db.SickPersonDetailVo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Describtion: 病人详情接口返回对象
 * Created: dragon
 * Date： 2016/10/19.
 */
public class PatientDetailResponse implements Serializable{

    /**
     * 病人信息
     */
    private SickPersonDetailVo patient;

    /**
     * 费用信息
     */
    private ExpenseTotal expenseTotal;

    /**
     * 诊断信息
     */
    private String diagnose;

    private List<AllergicDrug> allergicDrugs;
    /**
     * 异常信息
     */
    private List<State> states;

    @JsonProperty(value = "patient")
    public SickPersonDetailVo getPatient() {
        return patient;
    }

    public void setPatient(SickPersonDetailVo patient) {
        this.patient = patient;
    }

    @JsonProperty(value = "diagnose")
    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    @JsonProperty(value = "states")
    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    @JsonProperty(value = "expenseTotal")
    public ExpenseTotal getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(ExpenseTotal expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    @JsonProperty(value = "allergicDrugs")
    public List<AllergicDrug> getAllergicDrugs() {
        return allergicDrugs;
    }

    public void setAllergicDrugs(List<AllergicDrug> allergicDrugs) {
        this.allergicDrugs = allergicDrugs;
    }
}
