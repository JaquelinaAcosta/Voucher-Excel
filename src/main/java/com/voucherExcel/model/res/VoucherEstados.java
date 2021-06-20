package com.voucherExcel.model.res;

import com.voucherExcel.model.Voucher;

public class VoucherEstados {
	
	private String user;
	private Voucher voucher;
	
	
	public VoucherEstados() {
		super();
	}
	public VoucherEstados(String user, Voucher voucher) {
		super();
		this.user = user;
		this.voucher = voucher;
	}
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Voucher getVoucher() {
		return voucher;
	}
	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}

}
