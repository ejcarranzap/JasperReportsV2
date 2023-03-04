package com.itx.jasper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import tools.JasperReportTool;

public class Main {		  
	
	public static void main(String... args) {
		try{			
						
			System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog","fatal");
			/*Instant start = Instant.now();
			Instant end = Instant.now();
			Duration res = Duration.between(start, end);*/
			//System.out.println("Generating report...");
			System.out.println(JasperReportTool.generate(args[0],args[1],args[2],args[3], args[4]));		
			//System.out.println("Report generation complete..." + res);
		}catch(Exception ex){
			System.out.println("Error JasperReportTool: " + ex.getMessage() );
			ex.printStackTrace();
		}		
		
	}

	public static Map<String, Object> run(List<String> args) {
		Map<String, Object> result = new HashMap<String, Object>();

		if (args.size() > 0) {
			result.put("status", String.valueOf(0));
			result.put("value", "join result: Jasper");
		} else {
			result.put("status", "-1");
			result.put("error_msg", "empty list!");
		}

		return result;
	}
}
