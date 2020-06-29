package com.bsoft.nis.mapper.user;

import com.bsoft.nis.domain.core.barcode.Barcode;
import com.bsoft.nis.domain.user.PDAInfo;
import com.bsoft.nis.domain.user.db.Agency;
import com.bsoft.nis.domain.user.db.LoginUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */
public interface UserServiceMapper {

    LoginUser getUserByYGBH(@Param(value = "YGBH") String urid, @Param(value = "JGID") String jgid);

    LoginUser getUserByYGDM(@Param(value = "YGDM") String ygdm, @Param(value = "JGID") String jgid);

    LoginUser getUserByYGBHAndPwd(@Param(value = "YGBH") String urid, @Param(value = "JGID") String jgid, @Param(value = "YHMM") String md5Str);

    List<Agency> getAgency(@Param(value = "YGDM") String urid);

    LoginUser getUserByCard(@Param(value = "TMNR") String guid, @Param(value = "JGID") String jgid);

    List<Barcode> GetTmzd();

    PDAInfo getPDAInfo(@Param(value = "MANUER") String manuer, @Param(value = "MODEL") String model);

    int addPDAInfo(@Param(value = "MANUER") String manuer, @Param(value = "MODEL") String model);
}
