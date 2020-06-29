package com.bsoft.nis.service.pivas;

import com.bsoft.nis.core.datasource.DataSource;
import com.bsoft.nis.core.datasource.RouteDataSourceService;
import com.bsoft.nis.domain.pivas.PivasTransDetail;
import com.bsoft.nis.domain.pivas.PivasTransTitle;
import com.bsoft.nis.domain.pivas.PivasTransform;
import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.pivas.support.PivasServiceSup;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 静配中心主服务
 * Created by dragon on 2016/10/10.
 */
@Service
public class PivasMainService extends RouteDataSourceService {

    @Autowired
    PivasServiceSup service;


    public BizResponse<String> sendData(String xml) {
        keepOrRoutingDateSource(DataSource.MOB);
        BizResponse<String> bizResponse = new BizResponse();
        PivasTransTitle pivasTitle = new PivasTransTitle();
        List<PivasTransform> list_form = new ArrayList<>();
        try {
            if (parseXML(xml, pivasTitle, list_form)) {
                bizResponse = service.operation(pivasTitle, list_form);
            }
        } catch (Exception e) {
            e.printStackTrace();
            bizResponse.isSuccess = false;
            bizResponse.message = e.getMessage();
        }
        return bizResponse;
    }

    //dm4j 解析xml成对象
    public Boolean parseXML(String xml, PivasTransTitle pivasTitle, List<PivasTransform> list_form)
            throws Exception {

        Document document = null;
        document = DocumentHelper.parseText(xml);

        //获取根节点
        Element element = document.getRootElement();

        //获取标题
        Element title = element.element("transtitle");
        pivasTitle.transareaid = title.element("transareaid").getTextTrim();
        pivasTitle.transtime = title.element("transtime").getTextTrim();

        //获取输液主项
        List<Element> formList = element.element("transforms").elements("transform");
        //循环输液单
        for (Element root : formList) {
            PivasTransform pivastrsform = new PivasTransform();
            pivastrsform.advicegrpno = root.element("advicegrpno").getTextTrim();
            pivastrsform.barcodeno = root.element("barcodeno").getTextTrim();
            pivastrsform.makeflag = root.element("makeflag").getTextTrim();
            pivastrsform.maketime = root.element("maketime").getTextTrim();
            pivastrsform.makeuserno = root.element("makeuserno").getTextTrim();
            pivastrsform.medusage = root.element("medusage").getTextTrim();
            pivastrsform.medfreq = root.element("medfreq").getTextTrim();
            pivastrsform.patientareaid = root.element("patientareaid").getTextTrim();
            pivastrsform.patientbedid = root.element("patientbedid").getTextTrim();
            pivastrsform.patientid = root.element("patientid").getTextTrim();
            pivastrsform.patientno = root.element("patientno").getTextTrim();
            pivastrsform.plandate = root.element("plandate").getTextTrim();
            pivastrsform.plantime = root.element("plantime").getTextTrim();
            pivastrsform.printnum = root.element("printnum").getTextTrim();
            pivastrsform.printtime = root.element("printtime").getTextTrim();
            pivastrsform.printuserno = root.element("printuserno").getTextTrim();
            pivastrsform.sourcefrom = root.element("sourcefrom").getTextTrim();
            pivastrsform.sourcefromid = root.element("sourcefromid").getTextTrim();
            pivastrsform.timedesc = root.element("timedesc").getTextTrim();
            pivastrsform.timeno = root.element("timeno").getTextTrim();
            pivastrsform.jgid = root.element("jgid").getTextTrim();

            //获取输液明细
            List<Element> detailsList = root.element("transdetails").elements("transdetail");
            for (Element node : detailsList) {
                PivasTransDetail pivasTransDetail = new PivasTransDetail();
                pivasTransDetail.adviceid = node.element("adviceid").getTextTrim();
                pivasTransDetail.advicemainflag = node.element("advicemainflag").getTextTrim();
                pivasTransDetail.doseage = node.element("doseage").getTextTrim();
                pivasTransDetail.doseunit = node.element("doseunit").getTextTrim();
                pivasTransDetail.medid = node.element("medid").getTextTrim();
                pivasTransDetail.quantity = node.element("quantity").getTextTrim();
                pivasTransDetail.quantityunit = node.element("quantityunit").getTextTrim();
                pivastrsform.transdetails.add(pivasTransDetail);
            }
            list_form.add(pivastrsform);
        }
        return true;
    }
}

