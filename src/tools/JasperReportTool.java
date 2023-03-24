package tools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class JasperReportTool {
	
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public static String generate(String rptPath, String rptSavePath, String rptName, String jsonParams, String exportType) throws JRException, IOException, URISyntaxException{
		String CurrentPath = System.getProperty("user.dir") + "\\JCReportTool_V3B.json";
		/*System.out.println("CurrentPath:" + CurrentPath);
		System.out.println("OS: " + System.getProperty("os.name"));*/
		if(System.getProperty("os.name").toLowerCase().contains("linux") || System.getProperty("os.name").toLowerCase().contains("mac")){
			CurrentPath = CurrentPath.replace("\\", "/");
		}
		DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
		JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory",
		    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Map parameters = new HashMap();		
		JSONObject obj = new JSONObject(jsonParams);
		generateParams(parameters,obj);
		
		File file =  new File(rptSavePath+rptName+timestamp.toInstant().toString().replace(":", "").replace(".", "").replace("-", "")+"."+exportType);
		
		//parameters.put("INVOICES_WHERE", " (FACT.U_SERIE = 'FESCV25A' AND FACT.U_NUMERO = 1039342 AND FACT.U_TIENDA = 21) OR (FACT.U_SERIE = 'FESCV25A' AND FACT.U_NUMERO = 1039344 AND FACT.U_TIENDA = 21)");
		Connection conn;
		if(Files.notExists(Paths.get(CurrentPath)) == false){
			try {
				JSONObject objPrms = new JSONObject(new String(Files.readAllBytes(Paths.get(CurrentPath))));				
				
				Conexion.cnString = AES256.decrypt(objPrms.getString("cnString"));
				Conexion.user = AES256.decrypt(objPrms.getString("user"));
				Conexion.pass = AES256.decrypt(objPrms.getString("pass"));
				Conexion.clsName = AES256.decrypt(objPrms.getString("clsName"));
				
				/*System.out.println("admin: " + AES256.encrypt("admin"));
				System.out.println(Conexion.cnString);*/
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		conn = Conexion.conectar();		
			
		JasperReport report = JasperCompileManager.compileReport(rptPath+rptName+".jrxml");
		JasperFillManager jasperFillManager = JasperFillManager.getInstance(context);
		//JasperPrint print = jasperFillManager.fillReport(rptPath+rptName+".jasper", parameters, conn);
		JasperPrint print = jasperFillManager.fillReport(report, parameters, conn);
		
		
		if(exportType.equals("pdf")){
			JasperExportManager.exportReportToPdfFile(print, file.toPath().toString());
		}else if(exportType.equals("doc")) {
			JRDocxExporter exporter = new JRDocxExporter();    
		    exporter.setExporterInput(new SimpleExporterInput(print)); 
		    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));    
		    exporter.exportReport();
		} else {
			/*JasperExportManager.exportReportToXmlStream(print, new FileOutputStream(file));*/
			
			
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			/*configuration.setOnePagePerSheet(true);*/
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(false);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		}					
		
		//System.out.println(file.toPath());
		String res = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())); 		
		Files.delete(file.toPath());
		return res;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map generateParams(Map parameters, JSONObject obj){
		Iterator<String> keys = obj.keys();
		
		while(keys.hasNext()){
			String key = keys.next();
			if(obj.get(key) instanceof JSONObject){
				Map parametersChild = generateParams(parameters, obj.getJSONObject(key));
				parameters.put(key, parametersChild);
			} else {
				parameters.put(key, obj.get(key).toString());
			}
		}
		
		return parameters;
	}
}
