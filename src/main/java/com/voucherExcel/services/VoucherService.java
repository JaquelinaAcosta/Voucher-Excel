package com.voucherExcel.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.voucher.model.Empresa;
import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.model.res.VoucherEstados;
import com.voucherExcel.model.res.VoucherProceso;

public interface VoucherService {
	
	//Voucher updateVoucher(Voucher voucher) throws Exception;
	Voucher deleteVoucher(String vpucherId);
	VoucherProceso addVoucherExcel(MultipartFile voucher, String empresa, String usuarioResponsable);
	List<Voucher> getVouchers();
	Voucher getCodigoVoucher(String cv);
	Optional<Voucher> getVoucher(String id);

	
	//Estados de voucher
	//cambio de estado ELIMINAR
	Voucher estadoEliminarVoucher(VoucherEstados voucher) throws Exception;
	
	//Extender vigencia voucher VENCIDO
	Voucher estadoExtenderVigencia(VoucherEstados voucher) throws Exception;
	
	//Cambio de estado NO DISPONIBLE
	Voucher estadoNoDisponible(VoucherEstados voucher) throws Exception;
	
	//Hacer DUPLICADO de un voucher No-Disponible
	Voucher duplicadoVoucher(VoucherEstados voucher) throws Exception;
	
	//Asociar voucher duplicado
	Voucher voucherDupliAsociado(VoucherEstados voucher, Voucher duplicado);

}
