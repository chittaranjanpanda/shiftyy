package com.demo.shifty.domain;

import java.util.List;

public class Tracker {
	private String month;
	private String dateOfFilling;
	private String employeeCode;
	private String employeeName;
	private List<String> daysWorked;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDateOfFilling() {
		return dateOfFilling;
	}

	public void setDateOfFilling(String dateOfFilling) {
		this.dateOfFilling = dateOfFilling;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<String> getDaysWorked() {
		return daysWorked;
	}

	public void setDaysWorked(List<String> daysWorked) {
		this.daysWorked = daysWorked;
	}

}
