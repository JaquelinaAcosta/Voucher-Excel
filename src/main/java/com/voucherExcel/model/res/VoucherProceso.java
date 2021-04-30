package com.voucherExcel.model.res;

import java.util.List;

import com.voucherExcel.model.Voucher;

public class VoucherProceso{
	
	private List<Voucher> vouchers;
	private String error;
	
	public VoucherProceso() {
		super();
	}
	public VoucherProceso(List<Voucher> vouchers, String error) {
		super();
		this.vouchers = vouchers;
		this.error = error;
	}
	public List<Voucher> getVouchers() {
		return vouchers;
	}
	public void setVouchers(List<Voucher> vouchers) {
		this.vouchers = vouchers;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	


	
}
