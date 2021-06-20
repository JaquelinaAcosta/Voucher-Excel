package com.voucherExcel.services;

import java.util.List;

import com.voucher.model.Empresa;
import com.voucherExcel.model.Excel;
import com.voucherExcel.model.res.ExcelDisponible;

public interface ExcelService {

	Excel updateExcel(ExcelDisponible excel) throws Exception;
	Excel cancelarExcel(Excel excel) throws Exception;
	Excel delete(String excelId);
	List<Excel> getExcels(); 
	
	List<Excel> getExcelsEstado(String estado);
}
