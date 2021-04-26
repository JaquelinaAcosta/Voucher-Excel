package com.voucherExcel.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;

public interface VoucherService {
	
	//Voucher updateVoucher(Voucher voucher) throws Exception;
	Voucher deleteVoucher(String vpucherId);
	void addVoucher(MultipartFile voucher);
	List<Voucher> addVoucherExcel(MultipartFile voucher);
	List<Voucher> getVouchers();
	Voucher getCodigoVoucher(String cv);
	Optional<Voucher> getVoucher(String id);

	
	//Estados de voucher
	//cambio de estado ELIMINAR
	Voucher estadoEliminarVoucher(Voucher voucher) throws Exception;
	
	//Extender vigencia voucher VENCIDO
	Voucher estadoExtenderVigencia(Voucher voucher) throws Exception;
	
	//Cambio de estado NO DISPONIBLE
	Voucher estadoNoDisponible(Voucher voucher) throws Exception;
	
	//Hacer DUPLICADO de un voucher No-Disponible
	Voucher duplicadoVoucher(Voucher voucher) throws Exception;
	
	//Asociar voucher duplicado
	Voucher voucherDupliAsociado(Voucher voucher, Voucher duplicado);

}
