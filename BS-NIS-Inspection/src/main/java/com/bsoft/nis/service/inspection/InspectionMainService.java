package com.bsoft.nis.service.inspection;


import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.inspection.*;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.inspection.support.InspectionService;
import com.bsoft.nis.service.patient.support.PatientServiceSup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 检验检查查询主服务
 * Created by Administrator on 2016/10/10.
 */
@Service
public class InspectionMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(InspectionMainService.class);

    @Autowired
    InspectionService service; // 检验检查查询服务

    @Autowired
    DictCachedHandler handler; // 缓存处理器

    @Autowired
    PatientServiceSup patientService;


    public BizResponse<InspectionVo> GetInspectionList(String zyh, String jgid) {
        BizResponse<InspectionVo> bizResponse = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            //更具住院号获取住院号码
            String zyhm = patientService.getPatientDetail(zyh, jgid).ZYHM;
            keepOrRoutingDateSource(DataSource.LIS);
            bizResponse.datalist = service.GetInspectionList(zyhm, jgid);
            bizResponse.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取检验列表错误!" + e.getMessage();
        }
        return bizResponse;
    }


    public BizResponse<InspectionDetailVo> GetInspectionDetail(String inspectionNumber, String jgid) {
        keepOrRoutingDateSource(DataSource.LIS);
        BizResponse<InspectionDetailVo> bizResponse = new BizResponse<>();
        try {
            bizResponse.datalist = service.GetInspectionDetail(inspectionNumber, jgid);
            bizResponse.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取明细错误!" + e.getMessage();
        }
        return bizResponse;
    }
    /*
            升级编号【56010025】============================================= start
            检验检查：检验List项目数据趋势图，项目分类查看
            ================= Classichu 2017/10/18 9:34
            */
    public BizResponse<InspectionXMBean> GetInspectionXMBeanList(String xmid, String zyh, String jgid) {
        keepOrRoutingDateSource(DataSource.LIS);
        BizResponse<InspectionXMBean> bizResponse = new BizResponse<>();
        try {
            bizResponse.datalist = service.GetInspectionXMBeanList(xmid,zyh, jgid);
            bizResponse.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取项目错误!" + e.getMessage();
        }
        return bizResponse;
    }
       /* =============================================================== end */


    public BizResponse<ExamineVo> GetExamineResultList(String zyh, String jgid) {
        BizResponse<ExamineVo> bizUis = new BizResponse<>();
        BizResponse<ExamineVo> bizRis = new BizResponse<>();
        BizResponse<ExamineVo> bizResponse = new BizResponse<>();
        try {
            keepOrRoutingDateSource(DataSource.HRP);
            //更具住院号获取住院号码
//            String zyhm = patientService.getPatientDetail(zyh, jgid).ZYHM;
            keepOrRoutingDateSource(DataSource.RIS);
            bizRis.datalist = service.GetRisList(zyh, jgid);
            // 同一个库包括三项 超声/内镜/
            /*keepOrRoutingDateSource(DataSource.RIS);
            bizUis.datalist = service.GetUisList(zyhm, jgid);*/
            bizResponse.datalist = bizRis.datalist;
            /*if (bizUis.datalist.size() > 0) {
                for (ExamineVo examineVo : bizUis.datalist) {
                    bizResponse.datalist.add(examineVo);
                }
            }*/

            /*if (bizResponse.datalist.size() > 0) {
                for (int i = 0; i < bizResponse.datalist.size(); i++) {
                    String ygbh = bizResponse.datalist.get(i).BGYS;
                    //String ygxm = handler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, jgid, ygbh);
                    //获取员工姓名
                    bizResponse.datalist.get(i).BGYSXM = ygbh;
                }
            }*/
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取检查列表错误!" + e.getMessage();
        }

        bizResponse.isSuccess = true;
        return bizResponse;
    }


    public BizResponse<ExamineDetailVo> GetExamineResultDetail(String examineNumber, String type, String jgid) {
        BizResponse<ExamineDetailVo> bizResponse = new BizResponse<>();
        try {
            switch (type) {
                case "0":
                    keepOrRoutingDateSource(DataSource.RIS);
                    bizResponse.datalist = service.GetRisDetail(examineNumber, jgid);
                    break;
                case "1":
                    keepOrRoutingDateSource(DataSource.RIS);
                    bizResponse.datalist = service.GetRisDetail(examineNumber, jgid);
                    break;
                default:
                    keepOrRoutingDateSource(DataSource.RIS);
                    bizResponse.datalist = service.GetRisDetail(examineNumber, jgid);
                    break;
            }
            bizResponse.isSuccess = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            bizResponse.isSuccess = false;
            bizResponse.message = "获取明细错误!" + e.getMessage();
        }

        bizResponse.isSuccess = true;
        return bizResponse;
    }


}
