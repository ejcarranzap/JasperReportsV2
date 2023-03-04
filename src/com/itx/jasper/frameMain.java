package com.itx.jasper;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import tools.Conexion;

import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class frameMain extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frameMain frame = new frameMain();
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
					frame.setUndecorated(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static Map<String, Object> run(List<String> args){
	    Map<String, Object> result = new HashMap<String, Object>();
	    
	    if(args.size() > 0){
	      result.put("status", String.valueOf(0));
	      result.put("value", "join result: Jasper");
	    }else{
	      result.put("status", "-1");
	      result.put("error_msg", "empty list!");
	    }
	    
	    return result;
	 }

	/**
	 * Create the frame.
	 */
	public frameMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 554, 359);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.SOUTH);
		
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generateReport();
			}
		});
		toolBar.add(btnGenerate);
	}
	
	private void generateReport(){
		try{
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					"D:\\SOFTWARE\\NODEJS\\rpts620\\MyReports\\Report1.jasper", null,
					Conexion.conectar());
			JRPdfExporter exp = new JRPdfExporter();
			exp.setExporterInput(new SimpleExporterInput(jasperPrint));
			exp.setExporterOutput(new SimpleOutputStreamExporterOutput("Report1.pdf"));
			SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
			exp.setConfiguration(conf);
			exp.exportReport();
			
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("Report1.xlsx"));
			SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
			configuration.setOnePagePerSheet(true);
		    configuration.setDetectCellType(true);
		    exporter.setConfiguration(configuration);
		    exporter.exportReport();
	 
			JasperPrint jasperPrintWindow = JasperFillManager.fillReport(
					"D:\\SOFTWARE\\NODEJS\\rpts620\\MyReports\\Report1.jasper", null,
					Conexion.conectar());
			JasperViewer jasperViewer = new JasperViewer(jasperPrintWindow);
			jasperViewer.setVisible(true);
		}catch(Exception ex){
			JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage());
		}
	}	

}
