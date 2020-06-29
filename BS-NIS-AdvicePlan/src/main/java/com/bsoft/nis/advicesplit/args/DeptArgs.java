package com.bsoft.nis.advicesplit.args;

import com.bsoft.nis.domain.advicesplit.plantype.DeptPlanTypes;
import com.bsoft.nis.domain.advicesplit.ratetime.db.ExcuteRate;
import com.bsoft.nis.domain.patient.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:病区信息
 * 1.病区代码
 * 2.待拆分病人信息
 * 3.待拆分病人信息（包装类）
 * 4.病区定制的计划列表
 * 5.病区定制的计划列表所对应的频次列表
 * Created: dragon
 * Date： 2016/12/1.
 */
public class DeptArgs {

    /**
     * 科室代码
     */
    String BQDM;
    /**
     * 待拆分病人列表
     */
    List<Patient> patients = new ArrayList<>();
    /**
     * 待拆分病人列表(组装过后)
     */
    List<PatientArgs> patientArgs = new ArrayList<>();

    /**
     * 病区定制计划类型列表
     */
    List<DeptPlanTypes> plantypes = new ArrayList<>();
    /**
     * 病区定制执行频次列表
     */
    List<ExcuteRate> excuteRates = new ArrayList<>();

    public List<PatientArgs> getPatientWrappers(){
        return this.patientArgs;
    }

    public void addPatientWrapper(PatientArgs patientArgs){
        this.patientArgs.add(patientArgs);
    }

    public List<Patient> getPatients(){
        return this.patients;
    }

    public void addPatient(Patient patient){
        this.patients.add(patient);
    }

    public void addPatients(List<Patient> patients){
        this.patients = patients;
    }

    public void clearPatients(){
        patients = new ArrayList<>();
    }

    public void setBQDM(String ksdm){
        this.BQDM = ksdm;
    }

    public String getBQDM(){
        return this.BQDM;
    }

    public void setPlantypes(List<DeptPlanTypes> plantypes){
        this.plantypes = plantypes;
    }

    public List<DeptPlanTypes> getPlantypes(){
        return this.plantypes;
    }

    public List<ExcuteRate> getExcuteRates() {
        return excuteRates;
    }

    public void setExcuteRates(List<ExcuteRate> excuteRates) {
        this.excuteRates = excuteRates;
    }
}
