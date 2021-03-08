package com.voucherExcel.model;

import java.util.Date;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

//import com.voucher.model.Empresa;

@Document
public class Voucher {

	@Id
	private String _id;
	private int tipoDoc;
	private String dni;
	private String nombreApellido;
	private int valor;
	private Date fechaDesde;
	private Date fechaHasta;
	private String empresa;
	private String estado;
	private String codigoVoucher;
	private String codigoBarras;
	private Integer puntoVenta;
//	@DBRef
//	private Empresa empresaEmision;
	private String estadosPasados;
	private String facturaAsociada;
	private String Observacion;
	private String idCopia;
	
	
	public Voucher() {
		super();
	}
	public Voucher(int tipoDoc, String dni, String nombreApellido, int valor, Date fechaDesde,
			Date fechaHasta, String empresa, String estado, String codigoVoucher, String codigoBarras,
			Integer puntoVenta) {

		this.tipoDoc = tipoDoc;
		this.dni = dni;
		this.nombreApellido = nombreApellido;
		this.valor = valor;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.empresa = empresa;
		this.estado = estado;
		this.codigoVoucher = codigoVoucher;
		this.codigoBarras = codigoBarras;
		this.puntoVenta = puntoVenta;
	}

	public Voucher(String _id, int tipoDoc, String dni, String nombreApellido, int valor, Date fechaDesde,
			Date fechaHasta, String empresa, String estado, String codigoVoucher, String codigoBarras,
			Integer puntoVenta, String estadosPasados, String facturaAsociada,
			String observacion, String idCopia) {

		this._id = _id;
		this.tipoDoc = tipoDoc;
		this.dni = dni;
		this.nombreApellido = nombreApellido;
		this.valor = valor;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.empresa = empresa;
		this.estado = estado;
		this.codigoVoucher = codigoVoucher;
		this.codigoBarras = codigoBarras;
		this.puntoVenta = puntoVenta;
//		this.empresaEmision = empresaEmision;
		this.estadosPasados = estadosPasados;
		this.facturaAsociada = facturaAsociada;
		Observacion = observacion;
		this.idCopia = idCopia;
	}
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public int getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(int tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNombreApellido() {
		return nombreApellido;
	}
	public void setNombreApellido(String nombreApellido) {
		this.nombreApellido = nombreApellido;
	}
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCodigoVoucher() {
		return codigoVoucher;
	}
	public void setCodigoVoucher(String codigoVoucher) {
		this.codigoVoucher = codigoVoucher;
	}
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	public Integer getPuntoVenta() {
		return puntoVenta;
	}
	public void setPuntoVenta(Integer puntoVenta) {
		this.puntoVenta = puntoVenta;
	}
//	public Empresa getEmpresaEmision() {
//		return empresaEmision;
//	}
//	public void setEmpresaEmision(Empresa empresaEmision) {
//		this.empresaEmision = empresaEmision;
//	}
	public String getEstadosPasados() {
		return estadosPasados;
	}
	public void setEstadosPasados(String estadosPasados) {
		this.estadosPasados = estadosPasados;
	}
	public String getFacturaAsociada() {
		return facturaAsociada;
	}
	public void setFacturaAsociada(String facturaAsociada) {
		this.facturaAsociada = facturaAsociada;
	}
	public String getObservacion() {
		return Observacion;
	}
	public void setObservacion(String observacion) {
		Observacion = observacion;
	}
	public String getIdCopia() {
		return idCopia;
	}
	public void setIdCopia(String idCopia) {
		this.idCopia = idCopia;
	}
	
	
	
}
