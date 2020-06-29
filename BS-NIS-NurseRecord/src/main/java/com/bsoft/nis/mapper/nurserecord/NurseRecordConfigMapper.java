package com.bsoft.nis.mapper.nurserecord;

import com.bsoft.nis.domain.nurserecord.db.NRItem;
import com.bsoft.nis.domain.nurserecord.db.Structure;
import com.bsoft.nis.domain.nurserecord.db.Template;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Describtion:护理记录配置接口
 * Created: dragon
 * Date： 2016/10/20.
 */
public interface NurseRecordConfigMapper {

    List<Structure> getNurseRecordStructureList(@Param(value = "KSDM") String bqid,@Param(value = "JGID") String jgid,@Param(value = "SYSTYPE") int sysType);

    List<Structure> getStructrueListForNurse();

    List<Template> getNurseRecordTemplateList(@Param(value = "LBBH") String lbbh,@Param(value = "JGID") String jgid,@Param(value = "SYSTYPE") int sysType,@Param(value = "KSDM") String bqid);

    List<Template> getNurseRecordTemplateByJgbh(@Param(value = "JGBH") String jgbh);

    List<NRItem> getNurseReocrdItemByJgbh(@Param(value = "JGBH") String jgbh,@Param(value = "JGID") String jgid);
}
