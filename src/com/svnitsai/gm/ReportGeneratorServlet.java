package com.svnitsai.gm;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

@WebServlet("/servlet/report")
public class ReportGeneratorServlet extends HttpServlet {
	public static enum ReportType {
		// Add new enum to getReportEnum and in reports.jsp
		PayableMorning("Daily Payables", "SP_M_Final.jasper",
				"DailyPayables.pdf"), SupplierEvening("Supplier Payments",
				"SP_Evening_Final.jasper", "DailySupplierPayments.pdf"), CollectionsMorning(
				"Credit Sales Report", "Daily_Collection_Morning.jasper",
				"CreditSales.pdf"), CollectionsEvening("Collections Report",
				"Daily_Collection_Evening.jasper", "DailyCollections.pdf"), LedgerReport(
				"Ledger Report", "Payment_Collection_Main.jasper",
				"LedgerReport.pdf");

		String displayName;
		String jasperName;
		String pdfName;

		ReportType(String d, String j, String p) {
			this.displayName = d;
			this.jasperName = j;
			this.pdfName = p;
		}

		public String getDisplayName() {
			return this.displayName;
		}

		public String getJasperFileName() {
			return this.jasperName;
		}

		public String getPdfFileName() {
			return this.pdfName;
		}

		public static ReportType getReportEnum(String type) {

			if (PayableMorning.name().equals(type)) {
				return PayableMorning;
			} else if (SupplierEvening.name().equals(type)) {
				return SupplierEvening;
			} else if (CollectionsMorning.name().equals(type)) {
				return CollectionsMorning;
			} else if (CollectionsEvening.name().equals(type)) {
				return CollectionsEvening;
			} else if (LedgerReport.name().equals(type)) {
				return LedgerReport;
			} else {
				System.out.println("Unknown Report: " + type);
				return null;
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reportType = request.getParameter("type");
		String selectedDate = request.getParameter("date");

		ReportType reportTypeEnum = ReportType.getReportEnum(reportType);
		if (reportTypeEnum != null) {
			String displayDate = "";
			String formattedDate = "";
			if ("today".equals(selectedDate)) {
				// get today's date
				Date currentDate = Calendar.getInstance().getTime();
				formattedDate = Util.getFormattedDateForDB(currentDate);
				displayDate = Util.getFormattedDate(currentDate);
			} else {
				formattedDate = Util.getFormattedDateForDB(selectedDate);
				displayDate = selectedDate;
			}

			System.out.println("****** REport Generator Servlet *********");
			System.out.println("Formatted Date: " + formattedDate);

			Map parameters = new HashMap();
			if (formattedDate.length() > 0) {
				String startDate = formattedDate;
				
				parameters.put("dateFrom", startDate);
				//parameters.put("dateTo", endDate);
				parameters.put("selectedDate", displayDate);
			}

			String basePath = request.getRealPath("/") + "\\";
			String jasperName = basePath + "jrxml\\"
					+ reportTypeEnum.getJasperFileName();
			String pdfName = basePath + reportTypeEnum.getPdfFileName();
			System.out.println("JAsper: " + jasperName);
			generateReport(jasperName, pdfName, parameters);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/"
					+ reportTypeEnum.getPdfFileName());
			dispatcher.forward(request, response);
		} else {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/web/reports.jsp?status=failed");
			dispatcher.forward(request, response);
		}

	}

	public static void generateReport(String jasperName, String pdfName,
			Map parameters) {
		Connection conn = null;

		try {

			conn = DBHandler.getConnection();

			String printFileName = JasperFillManager.fillReportToFile(
					jasperName, parameters, conn);
			if (printFileName != null) {
				/**
				 * 1- export to PDF
				 */
				JasperExportManager.exportReportToPdfFile(printFileName,
						pdfName);
			}
		} catch (JRException e) {
			e.printStackTrace();
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
	 * ReportGeneratorServlet.generateReport("", "", new HashMap()); }
	 */
}
