package com.bsoft.nis.service.nurserecord;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.nurserecord.db.HelpLeaf;
import com.bsoft.nis.domain.nurserecord.db.HelpTree;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.nurserecord.support.NurseRecordHelperServiceSup;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Describtion:护理记录助手服务
 * Created: dragon
 * Date： 2016/11/23.
 */
@Service
public class NurseRecordHelperService extends RouteDataSourceService {
    private Log logger = LogFactory.getLog(NurseRecordHelperService.class);

    @Autowired
    NurseRecordHelperServiceSup service;

    /**
     * 获取护理记录助手目录
     *
     * @param ysbh
     * @param xmbh
     * @param jgbh
     * @param ygdm
     * @param bqid
     * @param jgid
     * @return
     */
    public BizResponse<List<HelpTree>> getHelpContent(String ysbh, String xmbh, String jgbh, String ygdm, String bqid, String jgid) {
        BizResponse<List<HelpTree>> response = new BizResponse<>();
        if (StringUtils.isEmpty(jgid)) {
            response.isSuccess = false;
            response.message = "参数[机构ID]不可为空";
            return response;
        }
        ysbh = StringUtils.isEmpty(ysbh) ? "0" : ysbh;
        ygdm = StringUtils.isEmpty(ygdm) ? "0" : ygdm;
        bqid = StringUtils.isEmpty(bqid) ? "0" : bqid;
        xmbh = StringUtils.isEmpty(xmbh) ? "0" : xmbh;

        List<HelpTree> list = new ArrayList<>();

        try {
            keepOrRoutingDateSource(DataSource.ENR);
            //协和
//            keepOrRoutingDateSource(DataSource.HRP);
            list = service.getHelpContent();
            for (HelpTree helpTree : list) {
                BizResponse<List<HelpLeaf>> bizResponse = getHelperApi(ysbh, xmbh, jgbh, helpTree.MLBH, ygdm, bqid, jgid);
                if (bizResponse.isSuccess) {
                    helpTree.helpLeafList = bizResponse.data;
                }
            }
            List<HelpTree> listtemp = new ArrayList<>(Arrays.asList(new HelpTree[list.size()]));
            Collections.copy(listtemp, list);

            // 过滤出MLBM长度为2的目录
            CollectionUtils.filter(list, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    HelpTree helpTree = (HelpTree) o;
                    return helpTree.MLBM.length() == 2;
                }
            });

            // 递归处理子目录
            for (HelpTree helpTree : list) {
                helpTree.Items = getSubItems(ysbh, xmbh, jgbh, ygdm, bqid, jgid, helpTree.MLBM, helpTree.MLBH, listtemp);
            }
            /*for (int i = 0 ; i<list.size();i++){
                HelpTree _helpTree = list.get(i);
                if (_helpTree.Items == null){
                    list.remove(_helpTree);
                    i--;
                }else{
                    if (_helpTree.Items.size() <=0){
                        list.remove(_helpTree);
                        i--;
                    }
                }
            }*/
            for (int i = 0; i < list.size(); i++) {
                HelpTree _helpTree = list.get(i);
                if (_helpTree.Items == null || _helpTree.Items.size() <= 0) {
                    if (_helpTree.helpLeafList == null || _helpTree.helpLeafList.size() <= 0) {
                        list.remove(_helpTree);
                        i--;
                    }
                }
            }
            response.isSuccess = true;
            response.data = list;
            response.message = "获取目录成功";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录助手目录]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录助手目录]服务内部错误!";
        }

        return response;
    }

    public List<HelpTree> getSubItems(String ysbh, String xmbh, String jgbh, String ygdm, String ksdm, String jgid, String mlbm, String flbh, List<HelpTree> list) {
        // 拷贝一份备用
        List<HelpTree> listTemp = new ArrayList<>(Arrays.asList(new HelpTree[list.size()]));
        Collections.copy(listTemp, list);

        // 过滤满足mlbm长度
       /* CollectionUtils.filter(list, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                HelpTree helpTree = (HelpTree)o;
                if (helpTree.MLBM.startsWith(mlbm) && helpTree.MLBM.length() == mlbm.length()+2){
                    return true;
                }
                return false;
            }
        });*/
        // 过滤满足mlbm
        /*helpTree.Items = getSubItems(ysbh,xmbh,jgbh,ygdm,ksdm,jgid,helpTree.MLBM,helpTree.MLBH,list);
        if (helpTree.Items.size() == 0){
            List<HelpTree> temp = getHelperItem(ysbh,xmbh,jgbh,helpTree.MLBH,ygdm,ksdm,jgid);
            if (temp.size() == 0){
                return false;
            }
        }
        helpTree.FLBH = flbh;*/
        List<HelpTree> listfilter = new ArrayList<>();
        for (HelpTree helpTree : list) {
            if (helpTree.MLBM.startsWith(mlbm) && helpTree.MLBM.length() == mlbm.length() + 2) {
                listfilter.add(helpTree);
            } else {
                continue;
            }
        }

        List<HelpTree> listfilter2 = new ArrayList<>();
        for (HelpTree helpTree : listfilter) {
            helpTree.Items = getSubItems(ysbh, xmbh, jgbh, ygdm, ksdm, jgid, helpTree.MLBM, helpTree.MLBH, listTemp);
            if (helpTree.Items.size() == 0) {
                List<HelpTree> temp = getHelperItem(ysbh, xmbh, jgbh, helpTree.MLBH, ygdm, ksdm, jgid);
                if (temp.size() == 0) {
                    continue;
                } else {
                    helpTree.FLBH = flbh;
                    listfilter2.add(helpTree);
                }
            }
        }

        return listfilter2;
    }

    public List<HelpTree> getHelperItem(String ysbh, String xmbh, String jgbh, String mlbh, String ygdm, String ksdm, String jgid) {
        List<HelpTree> helpTrees = new ArrayList<>();
        List<HelpLeaf> list = getHelper(ysbh, xmbh, jgbh, mlbh, ygdm, ksdm, jgid);
        for (HelpLeaf leaf : list) {
            HelpTree tree = new HelpTree();
            tree.FLBH = leaf.MLBH;
            tree.MLBH = leaf.ZSBH;
            tree.MLBM = leaf.MLBH;
            tree.MLMC = leaf.ZSMC;
            tree.ZSNR = leaf.ZSNR;
            helpTrees.add(tree);
        }
        return helpTrees;
    }

    public List<HelpLeaf> getHelper(String ysbh, String xmbh, String jgbh, String mlbh, String ygdm, String ksdm, String jgid) {
        List<HelpLeaf> list = new ArrayList<>();
        if (StringUtils.isEmpty(jgid) || StringUtils.isEmpty(jgbh) || StringUtils.isEmpty(mlbh) ||
                StringUtils.isEmpty(ygdm) || StringUtils.isEmpty(ksdm)) {
            return list;
        }

        ysbh = StringUtils.isEmpty(ysbh) ? "0" : ysbh;
        xmbh = StringUtils.isEmpty(xmbh) ? "0" : xmbh;

        try {
            keepOrRoutingDateSource(DataSource.ENR);
            list = service.getHelperItem(ysbh, xmbh, jgbh, mlbh, ygdm, ksdm, jgid);
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return list;
    }

    public BizResponse<List<HelpLeaf>> getHelperApi(String ysbh, String xmbh, String jgbh, String mlbh, String ygdm, String ksdm, String jgid) {
        BizResponse<List<HelpLeaf>> response = new BizResponse<>();
        List<HelpLeaf> list = new ArrayList<>();
        if (StringUtils.isEmpty(jgid) || StringUtils.isEmpty(jgbh) || StringUtils.isEmpty(mlbh) ||
                StringUtils.isEmpty(ygdm) || StringUtils.isEmpty(ksdm)) {
            response.data = list;
            response.isSuccess = false;
            response.message = "参数不可为空!";
            return response;
        }

        ysbh = StringUtils.isEmpty(ysbh) ? "0" : ysbh;
        xmbh = StringUtils.isEmpty(xmbh) ? "0" : xmbh;

        try {
            keepOrRoutingDateSource(DataSource.ENR);
            list = service.getHelperItem(ysbh, xmbh, jgbh, mlbh, ygdm, ksdm, jgid);
            response.data = list;
            response.isSuccess = true;
            response.message = "获取成功!";
        } catch (SQLException | DataAccessException e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录助手内容]数据库查询错误!";
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.isSuccess = false;
            response.message = "[护理记录助手内容]服务内部错误!";
        }
        return response;
    }
}
