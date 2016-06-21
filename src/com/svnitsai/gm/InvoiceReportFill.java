package com.svnitsai.gm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.svnitsai.gm.util.hibernate.HibernateUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

public class InvoiceReportFill {
	public static void createReport(String basePath) {
		String sourceFileName = basePath + "jrxml\\GMInvoice.jasper";
		Connection conn = null;

		try {
			String driver = HibernateUtil.getDbDriver();
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(	HibernateUtil.getDbURL(),
												HibernateUtil.getDbUsername(),
												HibernateUtil.getDbPassword());
			Map parameters = new HashMap();
			String printFileName = JasperFillManager.fillReportToFile(
					sourceFileName, parameters, conn);
			if (printFileName != null) {
				/**
				 * 1- export to PDF
				 */
				JasperExportManager.exportReportToPdfFile(printFileName,
						basePath + "GMInvoiceReport.pdf");
			}
		} catch (JRException e) {
			e.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	/*
	 * For testing only public static void main(String[] args) {
	 * InvoiceReportFill.createReport(""); }
	 */
}
