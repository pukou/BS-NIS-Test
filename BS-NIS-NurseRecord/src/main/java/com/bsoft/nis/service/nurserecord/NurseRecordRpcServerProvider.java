package com.bsoft.nis.service.nurserecord;

import com.bsoft.nis.common.service.SystemParamService;
import com.bsoft.nis.common.service.UserConfigService;
import com.bsoft.nis.domain.nurserecord.NRRecordItemRequest;
import com.bsoft.nis.domain.nurserecord.NRRecordRequest;
import com.bsoft.nis.domain.synchron.OutArgument;
import com.bsoft.nis.domain.synchron.Project;
import com.bsoft.nis.domain.synchron.SyncResult;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.pojo.exchange.Response;
import com.bsoft.nis.rpc.common.Synchron2MissionBusinessServerApi;
import ctd.util.annotation.RpcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Describtion:护理记录rpc服务提供者
 * Created: dragon
 * Date： 2017/1/11.
 */
public class NurseRecordRpcServerProvider implements Synchron2MissionBusinessServerApi {

    @Autowired
    NurseRecordWriteService recordWriteService;
    @Autowired
    SystemParamService systemParamService;//用户参数服务
    @Autowired
    UserConfigService userConfigService;//用户参数服务

    /**
     * 提供给同步服务调用
     * 统一的入参，写入目标服务(护理记录)
     *
     * @param outArgument
     * @return
     */
    @RpcService
    @Override
    public Response<SyncResult> synchron2MissionBusiness(OutArgument outArgument) {

        Response<SyncResult> response = new Response<>();
        BizResponse<String> saveResponse = new BizResponse<>();
        SyncResult syncResult = new SyncResult();
        NRRecordRequest saveData = new NRRecordRequest();
        saveData.ItemList = new ArrayList<>();
        saveData.JGBH = outArgument.bdys;
        saveData.JGID = outArgument.jgid;
        saveData.JLBH = null;
        saveData.JLSJ = outArgument.jlsj;
        saveData.SXSJ = outArgument.jlsj;
        saveData.ZYH = outArgument.zyh;
        saveData.HHBZ = "0";

        syncResult.LYJL = outArgument.lyjlxh;
        syncResult.LYLX = outArgument.lybdlx;
        syncResult.MBLX = outArgument.bdlx;
        syncResult.JGID = outArgument.jgid;
        syncResult.JGZT = outArgument.zyh;
        syncResult.Msg = outArgument.zyh;

        //福建协和客户化：医嘱执行同步到护理记录客户化
        Project specPorject = null;
        boolean syncDealYZMC = userConfigService.getUserConfig(outArgument.jgid).syncDealYZMC;
        for (Project project : outArgument.saveProjects) {
            //
            if (syncDealYZMC) {
                //福建协和客户化：医嘱执行同步到护理记录客户化
                if (outArgument.lybdlx.equals("9") && project.key.equals("46")) {
                    specPorject = project;
                    continue;
                }
            }
            NRRecordItemRequest recordItemRequest = new NRRecordItemRequest();
            recordItemRequest.XMBH = Integer.valueOf(project.key);
            recordItemRequest.VALUE = project.value;
            recordItemRequest.LYBD = outArgument.lybdlx;
            recordItemRequest.LYBH = outArgument.lyjlxh;
            recordItemRequest.LYMX = outArgument.lymx;
            recordItemRequest.LYMXLX = outArgument.lymxlx;

            syncResult.LYMX = outArgument.lymx;
            syncResult.LYMXLX = outArgument.lymxlx;
            saveData.ItemList.add(recordItemRequest);
        }

        //福建协和客户化：医嘱执行同步到护理记录客户化
        if (syncDealYZMC && specPorject != null) {
            //组合好的医嘱名称
            String zhhyzmc = specPorject.value;
            //执行拆分组合好的医嘱名称操作
            StringBuilder ytjsw = new StringBuilder();//液体及食物
            StringBuilder ytjswl = new StringBuilder();//液体及食物量
            StringBuilder ypyf = new StringBuilder();//途径（药品用法）
            //todo $符号是特殊字符
            String[] rowArray = zhhyzmc.replace("$$$", "$").split("\\$");
            for (String row : rowArray) {
                String str = row.replace("|||", "|");
                //todo 竖线是特殊字符
                String[] array = str.split("\\|");
                if (array.length != 4) {
                    //todo 需要程序进行异常处理
                }
                String yzmc = array[0];
                String jiliang = array[1];
                String danwei = array[2];
                String yongfa = array[3];
                if (!StringUtils.isBlank(yzmc)) {
                    //特殊处理
                    yzmc = FormatAdvice(yzmc);
                }
//                boolean isNeedAddHuanHang = false;
                int len = yzmc.length();//医嘱名称长度
                try {
                    len = new String(yzmc.getBytes("GB2312"), "iso-8859-1").length();
                } catch (UnsupportedEncodingException e) {

                }
                int lenTemp = 0;//要补足的空格位数
                if (len < 22) {
                    lenTemp = 22 - len;
//                    isNeedAddHuanHang = true;
                }
                for (int x = 0; x < lenTemp; x++) {
                    yzmc += " ";
                }
                if (danwei.equals("ml")) {
//                    ytjsw += yzmc + "\r\n" + "\r\n";
                    ytjsw.append(yzmc);
                    ytjsw.append("　");//没有 单位  全角空格  让 ytjsw 占满 2行 ！不考虑2行以上
//                    ytjswl += array[1] + "\r\n";
                    ytjsw.append("\r\n");
                    //
                    ytjswl.append(jiliang);
                    ytjswl.append("\r\n");
                } else {
//                    ytjsw += yzmc + "\r\n" + array[1] + array[2] + "\r\n";
//                    ytjswl += " " + "\r\n";
                    ytjsw.append(yzmc);
                    ytjsw.append(jiliang);
                    ytjsw.append(danwei);
                   /* if (isNeedAddHuanHang){
                        ytjsw.append("\r\n" );
                    }*/
                    ytjsw.append("\r\n");
                    //
                    ytjswl.append(" ");//不是 ml  空白
                    ytjswl.append("\r\n");
                }
//                ypyf += array[3] + "\r\n";
                ypyf.append(yongfa);
                ypyf.append("\r\n");
            }

            String ytjswStr = ytjsw.substring(0, ytjsw.length() - 2);
            String ytjswlStr = ytjswl.substring(0, ytjswl.length() - 2);
            String ypyfStr = ypyf.substring(0, ypyf.length() - 2);

            NRRecordItemRequest ytjswItem = new NRRecordItemRequest();
            ytjswItem.XMBH = Integer.valueOf("46");
            ytjswItem.VALUE = ytjswStr;
            ytjswItem.LYBD = outArgument.lybdlx;
            ytjswItem.LYBH = outArgument.lyjlxh;
            ytjswItem.LYMX = outArgument.lymx;
            ytjswItem.LYMXLX = outArgument.lymxlx;
            syncResult.LYMX = outArgument.lymx;
            syncResult.LYMXLX = outArgument.lymxlx;

            NRRecordItemRequest ytjswlItem = new NRRecordItemRequest();
            ytjswlItem.XMBH = Integer.valueOf("59");
            ytjswlItem.VALUE = ytjswlStr;
            ytjswlItem.LYBD = outArgument.lybdlx;
            ytjswlItem.LYBH = outArgument.lyjlxh;
            ytjswlItem.LYMX = outArgument.lymx;
            ytjswlItem.LYMXLX = outArgument.lymxlx;

            NRRecordItemRequest ypyfItem = new NRRecordItemRequest();
            ypyfItem.XMBH = Integer.valueOf("57");
            ypyfItem.VALUE = ypyfStr;
            ypyfItem.LYBD = outArgument.lybdlx;
            ypyfItem.LYBH = outArgument.lyjlxh;
            ypyfItem.LYMX = outArgument.lymx;
            ypyfItem.LYMXLX = outArgument.lymxlx;

            saveData.ItemList.add(ytjswItem);
            saveData.ItemList.add(ytjswlItem);
            saveData.ItemList.add(ypyfItem);
        }

        saveData.YHID = outArgument.hsgh;
        switch (outArgument.flag) {
            // 新增
            case "0":
                String jlbh = recordWriteService.getMergeRule(saveData);
                if (StringUtils.isEmpty(jlbh)) {
                    saveData = recordWriteService.getHeaderAndFooter(saveData);
                    saveResponse = recordWriteService.saveNurseRecord(saveData);
                    if (saveResponse.isSuccess) {
                        syncResult.MBJL = saveResponse.data;
                        syncResult.IsInsert = true;
                        response.ReType = 1;
                        response.Data = syncResult;
                    } else {
                        response.ReType = 0;
                        response.Msg = "同步写入护理记录失败" + saveResponse.message;
                    }
                } else {
                    Boolean isUpdate = true;
                    // 如果当前新增的记录的项目和可以合并的记录的项目有相同的就新增
                    List<NRRecordItemRequest> items = saveData.ItemList;
                    if (items.size() > 0) {
                        List<String> xms = new ArrayList<>();
                        for (NRRecordItemRequest item : items) {
                            xms.add(String.valueOf(item.XMBH));
                        }
                        List<Map> xmsin = recordWriteService.getExsitProjectsInRecord(xms, jlbh);
                        if (xmsin.size() > 0) {
                            isUpdate = false;
                        }
                    }

                    if (isUpdate) {
                        saveData.JLBH = jlbh;
                        saveResponse = recordWriteService.updateNurseRecord(saveData);
                        if (saveResponse.isSuccess) {
                            syncResult.MBJL = saveResponse.data;
                            response.ReType = 1;
                            response.Data = syncResult;
                        } else {
                            response.ReType = 0;
                            response.Msg = "同步更新护理记录失败" + saveResponse.message;
                        }
                    } else {
                        saveData = recordWriteService.getHeaderAndFooter(saveData);
                        saveResponse = recordWriteService.saveNurseRecord(saveData);
                        if (saveResponse.isSuccess) {
                            syncResult.MBJL = saveResponse.data;
                            syncResult.IsInsert = true;
                            response.ReType = 1;
                            response.Data = syncResult;
                        } else {
                            response.ReType = 0;
                            response.Msg = "同步写入护理记录失败" + saveResponse.message;
                        }
                    }

                }
                break;
            // 修改
            case "1":
                saveData.JLBH = outArgument.mbjlxh;
                saveResponse = recordWriteService.updateNurseRecord(saveData);
                if (saveResponse.isSuccess) {
                    syncResult.MBJL = saveResponse.data;
                    response.ReType = 1;
                    response.Data = syncResult;
                } else {
                    response.ReType = 0;
                    response.Msg = "更新护理记录失败" + saveResponse.message;
                }
                break;
            // 删除
            case "2":
                break;
        }
        response.ReType = 1;
        response.Msg = "同步更新护理记录成功";
        return response;
    }

    //截取括号外面的值
    public String subAdviceName(String yzmc) {
        int j = yzmc.indexOf("(");
        int k = yzmc.indexOf(")");
        if (j == -1) {//不存在左括号
            if (k == -1) {//不存在右括号
                //不做任何操作
            } else {//存在右括号
                if (k == 0) {//右边括号在第一个字符位置
                    //想办法向后截取
                    yzmc = yzmc.substring(k + 1, yzmc.length());
                } else {//右边括号在中间位置
                    //想办法向前截取
                    yzmc = yzmc.substring(0, k);
                }
            }
        } else {//存在左括号
            if (j == 0) {//左边括号在第一个字符字符位置
                //想办法向后截取
                if (k == -1) {//不存在右括号
                    yzmc = yzmc.substring(j + 1, yzmc.length());
                } else {//存在右括号
                    yzmc = yzmc.substring(k + 1, yzmc.length());
                }
            } else {//左边括号在中间位置
                //想办法向前截取
                yzmc = yzmc.substring(0, j);
            }
        }
        //递归处理
        if (yzmc.contains("(") || yzmc.contains(")")) {
            yzmc = subAdviceName(yzmc);
        }
        return yzmc;
    }

    // 截断药品名称(只保留药品名称)
    private String FormatAdvice(String yzmc) {
        int i = yzmc.indexOf("/");
        if (i != 0) {
            yzmc = yzmc.substring(0, i);
        }
        yzmc = yzmc.replace("（", "(").replace("）", ")");
        //
        boolean isstart = NumberCharHelper.startWithCircle(yzmc);
        if (!isstart) {
            return subAdviceName(yzmc);
        }
        String numberChar = yzmc.substring(0, 1);
        String tempYzmc = yzmc.substring(1, yzmc.length());
        String tempYzmcBack = subAdviceName(tempYzmc);
        return numberChar + tempYzmcBack;
    }

    /**
     * 删除的同步操作
     *
     * @param outArgument
     * @return
     */
    @RpcService
    @Override
    public Response<SyncResult> synchron2MissionBusinessDel(OutArgument outArgument) {
        Response<SyncResult> response = new Response<>();
        BizResponse<String> delResponse;
        delResponse = recordWriteService.updateNurseRecordForSyncDel(outArgument);

        if (delResponse.isSuccess) {
            response.ReType = 1;
            response.Msg = "同步删除护理记录成功";
        } else {
            response.ReType = 0;
            response.Msg = "同步删除护理记录失败>>" + delResponse.message;
        }
        return response;
    }
}
