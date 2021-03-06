package com.voucherExcel.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.voucher.model.Empresa;

import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.Date;

@Document
public class Excel {
	
	@Id
	private String _id;
	private String nombreExcel;
	private Date fecha;
	private String estado;
	private int cantidadRegistros;

	private String empresaEmision;
	private String responsable; 

	
	public Excel() {
	
	}
	
	public Excel(String _id, String nombreExcel, Date fecha, String estado, int cantidadRegistros, String EmpresaEmision) {
	this._id = _id;
	this.nombreExcel = nombreExcel;
	this.fecha = fecha;
	this.estado = estado;
	this.cantidadRegistros = cantidadRegistros;
	this.empresaEmision = empresaEmision;
	}
	
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	public String getNombreExcel() {
		return nombreExcel;
	}
	public void setNombreExcel(String nombreExcel) {
		this.nombreExcel = nombreExcel;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getCantidadRegistros() {
		return cantidadRegistros;
	}
	public void setCantidadRegistros(int cantidadRegistros) {
		this.cantidadRegistros = cantidadRegistros;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	public String getEmpresaEmision() {
		return empresaEmision;
	}
	public void setEmpresaEmision(String empresaEmision) {
		this.empresaEmision = empresaEmision;
	}
	
	

}
