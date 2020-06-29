package com.bsoft.nis.service.oralmedication;

import com.bsoft.nis.common.service.DateTimeService;
import com.bsoft.nis.core.cached.CachedDictEnum;
import com.bsoft.nis.core.cached.DictCachedHandler;
import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.oralmedication.OMSPackage;
import com.bsoft.nis.domain.oralmedication.OMSTablet;
import com.bsoft.nis.domain.oralmedication.OMSTitle;
import com.bsoft.nis.domain.patient.Patient;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.oralmedication.support.OralMedicationServiceSup;
import com.bsoft.nis.service.patient.PatientMainService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.CharArrayReader;
import java.util.List;

/**
 * 包药机主服务
 * Created by dragon on 2016/10/10.
 */
@Service
public class OralMedicationMainService extends RouteDataSourceService {

    private Log logger = LogFactory.getLog(OralMedicationMainService.class);
    @Autowired
    OralMedicationServiceSup service;

    @Autowired
    DictCachedHandler cachedHandler;     // 缓存服务

    @Autowired
    PatientMainService patientService;

    @Autowired
    DateTimeService dateTimeService;

    public BizResponse<String> sendData(String xml) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> bizResponse = new BizResponse();
        OMSTitle omsTitle = new OMSTitle();
        try {
            if (parseXML(xml, omsTitle)) {
                bizResponse = service.operation(omsTitle);
            }
        } catch (Exception e) {
            e.printStackTrace();
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

    //保存血糖记录
    public BizResponse<String> saveBloodGlucoseData(String xml){
        BizResponse<String> bizResponse = new BizResponse<>();
        String nodeName = "";
        try{
            String []clsdName = new String[]{"凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前","随机","凌晨"};

            SAXReader reader = new SAXReader();
            Document doc = reader.read(new CharArrayReader(xml.toCharArray()));

            nodeName = "PatientId";
            String zyh = doc.selectSingleNode("/BSXml/Data/" + nodeName).getText().trim();

            nodeName = "WardCode";
            String brbq = doc.selectSingleNode("/BSXml/Data/" + nodeName).getText().trim();

            nodeName = "TestTimePoint";
            String clsd = doc.selectSingleNode("/BSXml/Data/" + nodeName).getText().trim();

            nodeName = "Value";
            String clz = doc.selectSingleNode("/BSXml/Data/" + nodeName).getText().trim();

            nodeName = "Operator";
            String cjgh = doc.selectSingleNode("/BSXml/Data/" + nodeName).getText().trim();

            nodeName = "OperDateTime";
            String cjsj = doc.selectSingleNode("/BSXml/Data/" + nodeName).getText().trim();

            //员工工号转换为员工代码
            String ygdm = cachedHandler.getValueByKeyFromCached(CachedDictEnum.MOB_YGDM, "1", "YGBH", cjgh, "YGDM");
            if(ygdm != null && !"".equals(ygdm)) cjgh = ygdm;

            //获取病人信息，并从病人信息中获取病人床号
            BizResponse<Patient> bizPatient = patientService.getPatientByZyh(zyh);
            if(!bizPatient.isSuccess) throw new Exception(bizPatient.message);
            String brch = bizPatient.data.BRCH;

            int iClsd = Integer.parseInt(clsd);
            if(iClsd == 8){
                //如果没有测量时间，则取当前时间来处理
                if(cjsj == null || "".equals(cjsj)){
                    cjsj = dateTimeService.now(DataSource.MOB);
                }

                String []tmpArr = cjsj.split(" ");
                if(tmpArr.length < 2) throw new Exception("测量时间格式错误,非(yyyy-MM-dd HH:mm:ss)格式!");

                clsd = tmpArr[1].length() > 5 ? tmpArr[1].substring(0, 5): tmpArr[1];
            }else{
                clsd = clsdName[iClsd];
            }

            if("-1".equals(clz)){
                clz = "不在";
            }else if("-2".equals(clz)){
                clz = "拒测";
            }

            int ret = service.addXTJCJL(zyh, brbq, brch, cjsj, cjgh, clsd, clz, xml);
            if( ret > 0){
                bizResponse.isSuccess = true;
            }else{
                bizResponse.isSuccess = false;
                bizResponse.message = "保存血糖记录失败!";
            }

        }catch (Exception ex){
            logger.error(ex.getMessage());
            bizResponse.isSuccess = false;
            if("".equals(nodeName)){
                bizResponse.message = ex.getMessage();
            }else{
                bizResponse.message = "标签<" + nodeName + ">不存在!";
            }

        }

        return bizResponse;
    }


    //dm4j 解析xml成对象
    private boolean parseXML(String xml, OMSTitle omsTitle)
            throws Exception {
        Document document = null;
        document = DocumentHelper.parseText(xml);

        //获取根节点
        Element element = document.getRootElement();

        String  nodeName = "";
        try {

            //获取标题
            nodeName = "patientid";
            omsTitle.patientid = element.element("patientid").getTextTrim();
            nodeName = "areaid";
            omsTitle.areaid = element.element("areaid").getTextTrim();
            nodeName = "bedid";
            omsTitle.bedid = element.element("bedid").getTextTrim();
            nodeName = "plantime";
            omsTitle.plantime = element.element("plantime").getTextTrim();
            nodeName = "planpoint";
            omsTitle.planpoint = element.element("planpoint").getTextTrim();
            nodeName = "autoid";
            omsTitle.autoid = element.element("autoid").getTextTrim();
            nodeName = "packnum";
            omsTitle.packnum = element.element("packnum").getTextTrim();
            nodeName = "packtime";
            omsTitle.packtime = element.element("packtime").getTextTrim();
            //omsTitle.bzxx = element.element("bzxx").getTextTrim();    //文档中没有BZXX，此处会异常


            //获取包药机主项
            nodeName = "packages|package";
            List<Element> formList = element.element("packages").elements("package");


            //循环主项
            for (Element root : formList) {
                OMSPackage omsPackage = new OMSPackage();
                nodeName = "packageid";
                omsPackage.packageid = root.element("packageid").getTextTrim();
                nodeName = "packagecode";
                omsPackage.packagecode = root.element("packagecode").getTextTrim();
                nodeName = "packageindex";
                omsPackage.packageindex = root.element("packageindex").getTextTrim();
                //获取包药明细
                nodeName = "tablets|tablet";
                List<Element> detailsList = root.element("tablets").elements("tablet");
                for (Element node : detailsList) {
                    OMSTablet omsTablet = new OMSTablet();
                    nodeName = "adviceid";
                    omsTablet.adviceid = node.element("adviceid").getTextTrim();
                    nodeName = "quantityunit";
                    omsTablet.quantityunit = node.element("quantityunit").getTextTrim();
                    nodeName = "quantity";
                    omsTablet.quantity = node.element("quantity").getTextTrim();
                    nodeName = "doseunit";
                    omsTablet.doseunit = node.element("doseunit").getTextTrim();
                    nodeName = "doseage";
                    omsTablet.doseage = node.element("doseage").getTextTrim();
                    nodeName = "medid";
                    omsTablet.medid = node.element("medid").getTextTrim();
                    nodeName = "pruductareaid";
                    omsTablet.pruductareaid = node.element("pruductareaid").getTextTrim();
                    omsPackage.tablets.add(omsTablet);
                }
                omsTitle.packages.add(omsPackage);
                nodeName = "";
            }
        }catch(Exception ex){
            if("".equals(nodeName)){
                throw ex;
            }else {
                throw new Exception("xml结点(" + nodeName + ")不存在!");
            }
        }
        return true;
    }
}