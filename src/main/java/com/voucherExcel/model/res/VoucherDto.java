package com.voucherExcel.model.res;

import java.util.List;

public class VoucherDto {
	
	private List<String> codigoVoucher;
	private String dniCliente;
	private String facturaAsociada;
	
	public List<String> getCodigoVoucher() {
		return codigoVoucher;
	}
	public void setCodigoVoucher(List<String> codigoVoucher) {
		this.codigoVoucher = codigoVoucher;
	}
	public String getDniCliente() {
		return dniCliente;
	}
	public void setDniCliente(String dniCliente) {
		this.dniCliente = dniCliente;
	}
	public String getFacturaAsociada() {
		return facturaAsociada;
	}
	public void setFacturaAsociada(String facturaAsociada) {
		this.facturaAsociada = facturaAsociada;
	}

	
}
