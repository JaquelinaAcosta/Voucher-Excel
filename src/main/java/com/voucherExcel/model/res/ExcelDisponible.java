package com.voucherExcel.model.res;


import com.voucherExcel.model.Excel;

public class ExcelDisponible {
	private String responsable;
	private Excel excel;
	
		
	public ExcelDisponible(String responsable, Excel excel) {
		super();
		this.responsable = responsable;
		this.excel = excel;
	}
	public ExcelDisponible() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public Excel getExcel() {
		return excel;
	}
	public void setExcel(Excel excel) {
		this.excel = excel;
	}
	
	

}
