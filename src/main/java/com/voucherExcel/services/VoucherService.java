package com.voucherExcel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.voucherExcel.model.Voucher;

public interface VoucherService {
	
	//Voucher updateVoucher(Voucher voucher) throws Exception;
	Voucher deleteVoucher(String vpucherId);
	void addVoucher(MultipartFile voucher);
	List<Voucher> addVoucherExcel(MultipartFile voucher);
	List<Voucher> getVouchers();
	Voucher getCodigoVoucher(String cv);
	Optional<Voucher> getVoucher(String id);

}
