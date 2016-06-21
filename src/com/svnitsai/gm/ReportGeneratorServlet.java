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
		CollectionsMorningAll("Credit Sales Morning Report", "New_Daily_morning_collection_all.jasper", "CreditSalesMorningAll.pdf"),
		CollectionsMorningAM("Credit Sales (A-M) Morning Report", "New_Daily_morning_collection_am.jasper", "CreditSalesMorning_A_M.pdf"),
		CollectionsMorningNZ("Credit Sales (N-Z) Morning Report", "New_Daily_morning_collection_nz.jasper", "CreditSalesMorning_N_Z.pdf"),
		CollectionsEveningAll("Credit Sales Evening Report", "New_Daily_evening_collection_all.jasper", "CreditSalesEveningAll.pdf"), 
		CollectionsEveningAM("Credit Sales (A-M) Evening Report", "New_Daily_evening_collection_am.jasper", "CreditSalesEvening_A_M.pdf"), 
		CollectionsEveningNZ("Credit Sales (N-Z) Evening Report", "New_Daily_evening_collection_nz.jasper", "CreditSalesEvening_N_Z.pdf");

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

			if (CollectionsMorningAll.name().equals(type)) {
				return CollectionsMorningAll;
			} else if (CollectionsMorningAM.name().equals(type)) {
				return CollectionsMorningAM;
			} else if (CollectionsMorningNZ.name().equals(type)) {
				return CollectionsMorningNZ;
			} else if (CollectionsEveningAll.name().equals(type)) {
				return CollectionsEveningAll;
			} else if (CollectionsEveningAM.name().equals(type)) {
				return CollectionsEveningAM;
			}	else if (CollectionsEveningNZ.name().equals(type)) {
					return CollectionsEveningNZ;
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

			System.out.println(parameters);
			String basePath = request.getRealPath("/");
			String jasperName = basePath + "jrxml\\"
					+ reportTypeEnum.getJasperFileName();
			String pdfName = basePath + reportTypeEnum.getPdfFileName();
			System.out.println("JAsper: " + jasperName);
			generateReport(jasperName, pdfName, parameters);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/"
					+ reportTypeEnum.getPdfFileName());
			//dispatcher.sen(request, response);
			response.sendRedirect("/gm/" + reportTypeEnum.getPdfFileName());
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
