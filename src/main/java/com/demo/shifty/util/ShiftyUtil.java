package com.demo.shifty.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.demo.shifty.domain.Tracker;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

@Component
public class ShiftyUtil {
	
	public static XSSFWorkbook getExcelFromTracker(Tracker tracker) throws IOException {
		
		
		XSSFWorkbook wb = new XSSFWorkbook(new ClassPathResource("template.xlsx").getInputStream());
		XSSFSheet sheet = wb.getSheet("Shift Allowance Tracker");

		sheet.getRow(1).getCell(3).setCellValue(tracker.getMonth());
		sheet.getRow(1).getCell(5).setCellValue(tracker.getDateOfFilling());

		int workDateRow = 5;
		sheet.getRow(workDateRow).getCell(4).setCellValue(tracker.getDaysWorked().size());
		for (String workedDate : tracker.getDaysWorked()) {
			sheet.getRow(workDateRow).getCell(0).setCellValue(workDateRow - 4);
			sheet.getRow(workDateRow).getCell(1).setCellValue(tracker.getEmployeeCode());
			sheet.getRow(workDateRow).getCell(2).setCellValue(tracker.getEmployeeName());
			sheet.getRow(workDateRow).getCell(3).setCellValue(workedDate);
			workDateRow++;
		}
		return wb;
	}

	public static Tracker getTrackerFromPDF(InputStream is) throws IOException {
		PdfReader reader = new PdfReader(is);
		int noOfPages = reader.getNumberOfPages();
		Tracker tracker = new Tracker();
		List<String> daysWorked = new ArrayList<String>();

		String xslDatePattern = "dd/MM/yyyy";
		DateFormat xslDateFmt = new SimpleDateFormat(xslDatePattern);

		String pdfDatePattern = "dd-MMM-yyyy";
		DateFormat pdfDateFmt = new SimpleDateFormat(pdfDatePattern);

		Calendar cal = Calendar.getInstance();

		if (!reader.isTampered()) {
			for (int i = 1; i <= noOfPages; i++) {
				String page = PdfTextExtractor.getTextFromPage(reader, i);
				String[] content = page.split("\n");

				String topContent = content[0];

				tracker.setDateOfFilling(xslDateFmt.format(cal.getTime()));
				tracker.setEmployeeCode(topContent.split(":")[1].split(" ")[1].trim());
				tracker.setEmployeeName(topContent.split(":")[2].trim());

				for (int j = 2; j <= (content.length - 2); j++) {
					if (content[j].contains("Present")) {
						String[] eachDay = content[j].split(" ");
						try {
							daysWorked.add(xslDateFmt.format(pdfDateFmt.parse(eachDay[0])));
						} catch (ParseException e) {
						}
					}
				}

				tracker.setMonth(
						new DateFormatSymbols().getMonths()[(Integer.valueOf(daysWorked.get(0).split("/")[1])) - 1]);
				tracker.setDaysWorked(daysWorked);
			}
		}
		return tracker;
	}
}
