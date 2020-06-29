package com.bsoft.nis.service.patient;

import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.rpc.server.api.PatientServerApi;
import ctd.util.annotation.RpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Describtion:
 * Created: dragon
 * Date： 2017/1/9.
 */
public class PatientRpcServerProvide implements PatientServerApi{
    @Autowired
    PatientMainService service;
    @Override
    public Patient getSinglePatient() {
        return null;
    }

    @RpcService
    @Override
    public List<Patient> getPatientsByZyh(String zyh) {
        List<Patient> patients = service.getPatientList();
        return patients;
    }
}
