package com.bsoft.nis.core.cached;

import com.bsoft.nis.common.service.DictCachedService;
import com.bsoft.nis.domain.core.cached.Map2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Describtion: 字典缓存处理器
 * Created: dragon
 * Date： 2016/10/19.
 */
@Component
public class DictCachedHandler {

    @Autowired
    DictCachedService cachedService; // 缓存服务

    public List<Map> cachedDatas(CachedDictEnum dictname,String jgid,String whereStr){
        List list = cachedService.getDictDatasByDictName(dictname.getDictName(), jgid,whereStr);
        return list;
    }

    /**
     * 缓存数据
     * @param dictname  字典名称
     * @param jgid      机构ID
     * @return
     */
    public List<Map> cachedDatas(CachedDictEnum dictname,String jgid){
        List list = cachedService.getDictDatasByDictName(dictname.getDictName(), jgid);
        return list;
    }

    public List<Map> cachedDatas(String dictname,String jgid){
        List list = cachedService.getDictDatasByDictName(dictname, jgid);
        return list;
    }

    public List<Map> cachedDatas(String dictname,String jgid,String whereStr){
        List list = cachedService.getDictDatasByDictName(dictname, jgid,whereStr);
        return list;
    }

    /**
     * 获取字典列表数据
     * 约定：字典第一列为key,第二列为value
     * @param dict
     * @param jgid
     * @return
     */
    public List<Map2> getCachedDatasContainKeyValue(String dict,String jgid){
        List<Map2> list = new ArrayList<>();
        String keyname ,valuename,dictname;

        switch (dict){
            case "MOB_BRXZ":
                keyname = "BRXZ";
                valuename = "XZMC";
                dictname = "MOB_BRXZ";
                jgid = null;
                break;
            case "MOB_CDDZ":
                keyname = "YPCD";
                valuename = "CDMC";
                dictname = "MOB_CDDZ";
                jgid = null;
                break;
            case "MOB_DMZD":
                keyname = "DMSB";
                valuename = "DMMC";
                dictname = "MOB_DMZD";
                jgid = null;
                break;
            case "MOB_EMRDMZD":
                keyname = "DMSB";
                valuename = "DMMC";
                dictname = "MOB_EMRDMZD";
                jgid = null;
                break;
            case "MOB_FYBM":
                keyname = "FYXH";
                valuename = "FYMC";
                dictname = "MOB_FYBM";
                jgid = null;
                break;
            case "MOB_KSDM":
                keyname = "KSDM";
                valuename = "KSMC";
                dictname = "MOB_KSDM";
                break;
            case "MOB_SSDM":
                keyname = "SSNM";
                valuename = "SSMC";
                dictname = "MOB_SSDM";
                jgid = null;
                break;
            case "MOB_SYPC":
                keyname = "PCBM";
                valuename = "PCMC";
                dictname = "MOB_SYPC";
                jgid = null;
                break;
            case "MOB_TYPK":
                keyname = "YPXH";
                valuename = "YPMC";
                dictname = "MOB_TYPK";
                jgid = null;
                break;
            case "MOB_XTPZ":
                keyname = "PZBH";
                valuename = "DMMC";
                dictname = "MOB_XTPZ";
                jgid = null;
                break;
            case "MOB_YGDM":
                keyname = "YGDM";
                valuename = "YGXM";
                dictname = "MOB_YGDM";
                break;
            case "MOB_YLSF":
                keyname = "FYXH";
                valuename = "FYMC";
                dictname = "MOB_YLSF";
                jgid = null;
                break;
            case "MOB_YPSX":
                keyname = "YPSX";
                valuename = "SXMC";
                dictname = "MOB_YPSX";
                jgid = null;
                break;
            case "MOB_YPYF":
                keyname = "YPYF";
                valuename = "XMMC";
                dictname = "MOB_YPYF";
                jgid = null;
                break;
            case "MOB_YWLB":
                keyname = "YWLB";
                valuename = "LBMC";
                dictname = "MOB_YWLB";
                jgid = null;
                break;
            case "GY_JBBM":
                keyname = "JBXH";
                valuename = "JBMC";
                dictname = "GY_JBBM";
                jgid = null;
                break;
            default:
                keyname = "";
                valuename = "";
                dictname = "";
                break;
        }

        if (StringUtils.isBlank(dictname))
            return list;

        // 获取字典数据
        List<Map> dicts = cachedDatas(dictname, jgid);

        // 字典数据转换
        for(Map map:dicts){
            if(!map.containsKey(keyname) || !map.containsKey(valuename)){
                break;
            }
            if (StringUtils.isBlank(String.valueOf(map.get(keyname))) || StringUtils.isBlank(String.valueOf(map.get(valuename)))){
                break;
            }

            Map2 map2 = new Map2(String.valueOf(map.get(keyname)),String.valueOf(map.get(valuename)));
            list.add(map2);
        }
        return list;
    }

    public List<Map2> getCachedDatasContainKeyValue(CachedDictEnum dictname,String jgid){
        List<Map2> list = new ArrayList<>();
        // 获取字典数据
        List<Map> dicts = cachedDatas(dictname, jgid);

        // 字典数据转换
        for(Map map:dicts){
            if(!map.containsKey(dictname.getCompareCol()) || !map.containsKey(dictname.getRetCol())){
                break;
            }
            if (StringUtils.isBlank(String.valueOf(map.get(dictname.getCompareCol()))) || StringUtils.isBlank(String.valueOf(map.get(dictname.getRetCol())))){
                break;
            }

            Map2 map2 = new Map2(String.valueOf(map.get(dictname.getCompareCol())),String.valueOf(map.get(dictname.getRetCol())));
            list.add(map2);
        }
        return list;
    }

    /**
     * 获取某条字典数据的value值 ，如：根据科室代码获取科室名称
     * @param dictname       字典名称
     * @param jgid           机构ID
     * @param comparecol     比较字段名
     * @param comparevalue   比较字段值
     * @param retcol         返回字段名
     * @return
     */
    public String getValueByKeyFromCached(CachedDictEnum dictname,String jgid,String comparecol,String comparevalue,String retcol){
        String value = "";
        List<Map> dicts = cachedDatas(dictname.getDictName(), dictname.getJgid() == null?null:jgid);

        for (int i = 0 ; i<dicts.size();i++){
            Map map = dicts.get(i);
            if(!map.containsKey(comparecol) || !map.containsKey(retcol)){
                continue;
            }
            String id = String.valueOf(map.get(comparecol));
            if (id.equals(comparevalue)){
                value = String.valueOf(map.get(retcol));
                break;
            }
        }
        return value;
    }

    public String getValueByKeyFromCached(CachedDictEnum dictname,String jgid,String comparecol,String comparevalue,String retcol,String whereStr){
        String value = "";
        List<Map> dicts = cachedDatas(dictname.getDictName(), dictname.getJgid() == null?null:jgid,whereStr);

        for (int i = 0 ; i<dicts.size();i++){
            Map map = dicts.get(i);
            if(!map.containsKey(comparecol) || !map.containsKey(retcol)){
                continue;
            }
            String id = String.valueOf(map.get(comparecol));
            if (id.equals(comparevalue)){
                value = String.valueOf(map.get(retcol));
                break;
            }
        }
        return value;
    }

    /**
     * 获取某条字典数据的value值 ，如：根据科室代码获取科室名称
     * 默认常用字典key-value用法
     * @param dictname      字典名称
     * @param jgid          机构ID
     * @param comparevalue  比较值
     * @return
     */
    public String getValueByKeyFromCached(CachedDictEnum dictname,String jgid,String comparevalue){
        return getValueByKeyFromCached(dictname,jgid,dictname.getCompareCol(),comparevalue,dictname.getRetCol());
    }

    /**
     * 获取某条字典数据的value值 ，如：根据科室代码获取科室名称
     * 默认常用字典key-value用法
     * @param dictname      字典名称
     * @param jgid          机构ID
     * @param comparevalue  比较值
     * @param whereStr      where条件 ，“ dmlb = 245” 不需要加and 和where，只需要些条件语句即可
     * @return
     */
    public String getValueByKeyFromCached(CachedDictEnum dictname,String jgid,String comparevalue,String whereStr){
        return getValueByKeyFromCached(dictname,jgid,dictname.getCompareCol(),comparevalue,dictname.getRetCol(),whereStr);
    }
}
