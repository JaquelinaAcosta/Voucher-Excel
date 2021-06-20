package com.voucherExcel.services.impl;

import java.util.List;
import java.util.Optional;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.voucher.model.Empresa;
import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.model.res.ExcelDisponible;
import com.voucherExcel.repository.ExcelRepository;
import com.voucherExcel.repository.VoucherRepository;
import com.voucherExcel.services.ExcelService;

@Service
public class ExcelServiceImpl implements ExcelService{
	
	@Inject
	private ExcelRepository excelRepository;
	@Inject
	private VoucherRepository voucherRepository;
	
	private static final Log logger = LogFactory.getLog(ExcelServiceImpl.class);
	private SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yyyy");
	
	
	@Override
	public Excel updateExcel(ExcelDisponible excel) throws Exception{
		Optional<Excel> excelBD = excelRepository.findById(excel.getExcel().get_id());
		if (!excelBD.get().getEstado().equals("CANCELADO")) {
			excelBD.get().setEstado("DISPONIBLE");
			Excel excelAdd = excelRepository.save(excelBD.get());
			if(excelBD.get().getEstado().equals("DISPONIBLE")) {
				List<Voucher> vouchers = voucherRepository.findByExcel(excel.getExcel());
				for(Voucher v: vouchers) {
					v.setHabilitado(true);
					v.setEstadosPasados("EMITIDO el día "+ f2.format(excelAdd.getFecha())+" por el usuario: "+ excel.getResponsable());
					voucherRepository.save(v);
				}		
			}
			return excelAdd;
		} 
		return excelBD.get();
	}

	@Override
	public Excel cancelarExcel(Excel excel) throws Exception{
		Optional<Excel> excelBD = excelRepository.findById(excel.get_id());
		if (!excelBD.get().getEstado().equals("DISPONIBLE")) {
			Excel excelAdd = excelRepository.save(excel);
			if(excel.getEstado().equals("CANCELADO")) {
				List<Voucher> vouchers = voucherRepository.findByExcel(excel);
				for(Voucher v: vouchers) {
					voucherRepository.delete(v);;
				}		
			}
			return excelAdd;
		} 
		return excelBD.get();
	}
	@Override
	public Excel delete(String vpucherId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Excel> getExcels() {
		List<Excel> archivos = excelRepository.findAll();
		return archivos;
	}

	@Override
	public List<Excel> getExcelsEstado(String estado) {
		List<Excel> archivos = excelRepository.findByEstado(estado);
		return archivos;
	}
	


}
