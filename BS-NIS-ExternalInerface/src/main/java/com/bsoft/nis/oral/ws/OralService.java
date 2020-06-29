package com.bsoft.nis.oral.ws;

import com.bsoft.nis.pojo.exchange.BizResponse;
import com.bsoft.nis.service.oralmedication.OralMedicationMainService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;

/**
 * 包药机WebService接口(给创业平台调用)
 * Created by mdw on 2019/1/3.
 */
/*@WebService(serviceName="OralMedicationService", portName="IOralMedicationService", targetNamespace="http://tempuri.org/" )*/
@WebService(serviceName="OralService", portName="OralServiceSoap")
public class OralService {
    private Log logger = LogFactory.getLog(OralService.class);
    public static ApplicationContext context;
    //接收数据方法
    public String SendData(String xml){
        //logger.info("原始口服XML: " + xml);
        logger.error("原始口服XML: " + xml);    //因为日志级别已经调到了ERROR,所以此处用error记录传过来的xml,正式运行以后关闭该功能
        boolean bSuccess = false;
        String   errText = "";
        try{
            //转换成旧包药机接口格式数据
            String dataXml = ConvertData(xml);

            OralMedicationMainService service = (OralMedicationMainService) context.getBean("oralMedicationMainService");
            BizResponse<String> bizResponse = service.sendData(dataXml);

            bSuccess = bizResponse.isSuccess;
            //if(!bSuccess) errText = bizResponse.message;
            errText = bizResponse.message;

        }catch (Exception ex){
            bSuccess = false;
            errText = ex.getMessage();
        }

        //包装返回值
        String retXml = "<BSXml><MsgHeader><Sender>HIS</Sender><Status>" + (bSuccess?"true":"false")
                      + "</Status><ErrCode>" + (bSuccess?"":errText) + "</ErrCode>"
                      + "<Detail>"+ errText +"</Detail></MsgHeader></BSXml>";
                      //+ "<Detail>"+ (bSuccess?"操作成功":errText) +"</Detail></MsgHeader></BSXml>";

        return retXml;
    }

    //血糖接口
    public String SendBloodGlucoseData(String xml){
        logger.error("原始血糖XML: " + xml);
        boolean bSuccess = false;
        String   errText = "";

        try{
            //获取OralMedicationMainService对象的引用, 并调用血糖保存方法
            OralMedicationMainService service = (OralMedicationMainService) context.getBean("oralMedicationMainService");
            BizResponse<String> bizResponse = service.saveBloodGlucoseData(xml);

            bSuccess = bizResponse.isSuccess;
            if(!bSuccess) errText = bizResponse.message;

        }catch (Exception ex){
            bSuccess = false;
            errText = ex.getMessage();
        }

        //包装返回值
        String retXml = "<BSXml><MsgHeader><Sender>HIP</Sender><Status>" + (bSuccess?"true":"false")
                + "</Status><ErrCode>" + (bSuccess?"":errText) + "</ErrCode>"
                + "<Detail>"+ (bSuccess?"操作成功":errText) +"</Detail></MsgHeader></BSXml>";

        return retXml;

    }

    //方法上加@WebMentod(exclude=true)后，此方法不被发布；
    @WebMethod(exclude=true)
    public String ConvertData(String xml)throws Exception{
        //XML解析器
        SAXReader reader = new SAXReader();
        Document oldDoc = reader.read(new CharArrayReader(xml.toCharArray()));

        //转换计划用药时间
        Node node = oldDoc.selectSingleNode("/BSXml/Prescription/PlanDateTime");
        node.setText(FormatDateString(node.getText()));

        //转换包药时间
        node = oldDoc.selectSingleNode("/BSXml/Prescription/PackDateTime");
        node.setText(FormatDateString(node.getText()));

        Node oldNode = oldDoc.selectSingleNode("/BSXml/Prescription");
        Node newNode = (Node) oldNode.clone();

        Document newDoc = DocumentHelper.createDocument();
        newDoc.setXMLEncoding("utf-8");

        //设置根结点
        newNode.setName("autotablet");
        newDoc.add(newNode);


        CharArrayWriter writer = new CharArrayWriter();
        newDoc.write(writer);

        //转换相应的标签
        String	dataStr = writer.toString()
                .replaceAll("MachineNo", "markerid")
                .replaceAll("VisitId", "patientid")
                .replaceAll("WardCode", "areaid")
                .replaceAll("BedNo", "bedid")
                .replaceAll("PlanDateTime", "plantime")
                .replaceAll("PlanPoint", "planpoint")
                .replaceAll("AutoId", "autoid")
                .replaceAll("PackNum", "packnum")
                .replaceAll("PackDateTime", "packtime")
                .replaceAll("Packages", "packages")
                /**.replaceAll("Package", "package")**//**不能直接替换,否则Package开头的都将替换**/
                .replaceAll("PackageId", "packageid")
                .replaceAll("PackageCode", "packagecode")
                .replaceAll("PackageIndex", "packageindex")
                .replaceAll("Package", "package")
                .replaceAll("Tablets", "tablets")
                .replaceAll("Tablet", "tablet")
                .replaceAll("AdviceId", "adviceid")
                .replaceAll("Frequency", "frequency")
                .replaceAll("TimesId", "Timesid")
                .replaceAll("TimesName", "timesname")
                .replaceAll("MachineNo", "markerid")
                .replaceAll("MedId", "medid")
                .replaceAll("PruducetAreaId", "pruductareaid")
                .replaceAll("DoseAge", "doseage")
                .replaceAll("DoseUnit", "doseunit")
                .replaceAll("MachineNo", "markerid")
                /**.replaceAll("Quantity", "quantity") **/ /**要放到QuantityUnit之后**/
                .replaceAll("QuantityUnit", "quantityunit")
                .replaceAll("Quantity", "quantity")
                .replaceAll("Usage", "usage");

        return dataStr;
    }

    //把20190103T164500格式数据转化为2019-01-03 16:45:00格式
    @WebMethod(exclude=true)
    public String FormatDateString(String dateStr){
        StringBuffer sb = new StringBuffer("")
                .append(dateStr.substring(0,4)).append("-")
                .append(dateStr.substring(4,6)).append("-")
                .append(dateStr.substring(6,8)).append(" ")
                .append(dateStr.substring(9,11)).append(":")
                .append(dateStr.substring(11,13)).append(":")
                .append(dateStr.substring(13,15));

        return sb.toString();
    }

    //包药机接口WebService服务启动方法
    public static void startOralService(ApplicationContext ctx, int port){
        context = ctx;

        String urlStr = "http://0.0.0.0:"+ String.valueOf(port) + "/NIS/OralService";
        System.out.println("正在发布(" + urlStr + ") ...... ");
        Endpoint.publish(urlStr, new OralService());
    }
}
