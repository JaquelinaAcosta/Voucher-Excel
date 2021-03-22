package com.voucherExcel.services;

import java.util.List;

import com.voucherExcel.model.Excel;

public interface ExcelService {

	Excel updateExcel(Excel excel) throws Exception;
	Excel cancelarExcel(Excel excel) throws Exception;
	Excel delete(String excelId);
	List<Excel> getExcels(); 
	
	List<Excel> getExcelsEstado(String estado);
}
