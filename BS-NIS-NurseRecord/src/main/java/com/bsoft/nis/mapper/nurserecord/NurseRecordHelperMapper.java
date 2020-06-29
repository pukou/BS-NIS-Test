package com.bsoft.nis.mapper.nurserecord;

import com.bsoft.nis.domain.nurserecord.db.HelpLeaf;
import com.bsoft.nis.domain.nurserecord.db.HelpTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Describtion:
 * Created: dragon
 * Date： 2016/11/23.
 */
public interface NurseRecordHelperMapper {

    List<HelpTree> getHelpContent();

    List<HelpLeaf> getHelperItem(@Param(value = "YSBH") String ysbh,
                                 @Param(value = "XMBH") String xmbh,
                                 @Param(value = "JGBH") String jgbh,
                                 @Param(value = "MLBH") String mlbh,
                                 @Param(value = "YGDM") String ygdm,
                                 @Param(value = "KSDM") String ksdm,
                                 @Param(value = "JGID") String jgid);
}
