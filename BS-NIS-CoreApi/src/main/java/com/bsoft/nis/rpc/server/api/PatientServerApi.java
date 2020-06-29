package com.bsoft.nis.rpc.server.api;

import com.bsoft.nis.domain.patient.Patient;
import ctd.util.annotation.RpcService;

import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public interface PatientServerApi {

    Patient getSinglePatient();

    @RpcService
    List<Patient> getPatientsByZyh(String zyh);
}
