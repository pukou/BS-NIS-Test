package com.bsoft.nis.rpc.patient;

import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.rpc.server.api.PatientServerApi;
import ctd.util.annotation.RpcService;

import java.util.ArrayList;
import java.util.List;

/**
 * Describtion:
 * Created: dragon
 * Date： 2017/1/6.
 */
public class PatientRpcServiceIml implements PatientServerApi{
    @Override
    public Patient getSinglePatient() {
        return null;
    }

    //@RpcService
    @Override
    public List<Patient> getPatientsByZyh(String zyh) {
        List<Patient> list = new ArrayList<>();
        Patient patient = new Patient();
        patient.ZYH ="1";
        patient.BRXM = "邢海龙";
        list.add(patient);
        return list;
    }
}
