package com.demo.shifty.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.shifty.domain.Tracker;
import com.demo.shifty.util.ShiftyUtil;

@Controller
public class HomeController {

	private static final Logger _log = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String landingPage(Model model) {
		_log.info("on landing Page");
		return "index";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		return "home";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response, Model model) throws IOException {
		_log.info("In Upload Page");
		
		XSSFWorkbook wb = null;
		if (file.getContentType().equals("application/pdf")) {
			byte[] byteArr = {};
			try {
				byteArr = file.getBytes();
				InputStream is = new ByteArrayInputStream(byteArr);
				Tracker tracker = ShiftyUtil.getTrackerFromPDF(is);
				is.close();
				wb = ShiftyUtil.getExcelFromTracker(tracker);

				response.addHeader("Content-Disposition",
						"attachment; filename=" + tracker.getEmployeeName() + ".xlsx");
				response.setContentType("application/xlsx");
				wb.write(response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				response.sendRedirect("/");
			}finally{
				wb.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		}else{
			response.sendRedirect("/");
		}
	}
}
