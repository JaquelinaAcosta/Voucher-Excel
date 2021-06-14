package com.voucherExcel.services.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.voucherExcel.helpers.CSVHelper;
import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.model.res.VoucherProceso;
import com.voucherExcel.repository.ExcelRepository;
import com.voucherExcel.repository.VoucherRepository;
import com.voucherExcel.services.VoucherService;

@Service
public class VoucherServiceImpl implements VoucherService {

	
	@Inject
	private VoucherRepository voucherRepository;
	
	@Inject
	private ExcelRepository excelRepository;
	
	private Boolean bandera;
	private SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yyyy");
	
	private static final Log logger = LogFactory.getLog(VoucherServiceImpl.class);

	  public void addVoucher(MultipartFile file) {
		bandera = false;
	    try {
	      List<Voucher> vouchers = CSVHelper.csvToVouchers(file.getInputStream());

	      voucherRepository.saveAll(vouchers);
	    } catch (IOException e) {
	      throw new RuntimeException("fail to store csv data: " + e.getMessage());
	    }
	  }

	 
	@Override
	public VoucherProceso addVoucherExcel(MultipartFile file) {
		bandera = false;
		int registros = 0;
		try {
			VoucherProceso voucherProceso = CSVHelper.excelToVouchers(file.getInputStream());
			List<Voucher> vouchers = voucherProceso.getVouchers();
			//Controla si el codigo de voucher esta repetido en Base de Datos
		      for(Voucher v : vouchers) {
		    	  Voucher vAux = voucherRepository.findByCodigoVoucher(v.getCodigoVoucher());
		    	  registros++;
		    	  if(vAux!=null) {
		    		  bandera = true;
		    		  voucherProceso.setError(voucherProceso.getError() +'\n'+"El codigo de voucher: " + vAux.getCodigoVoucher() + " esta repetido dentro del sistema");
		    	  }
		      }
		    //Creacion excel
		      Excel excel = new Excel();
		      excel.setEstado("IMPORTANDO");
			  excel.setFecha(new Date());
			  excel.setNombreExcel("Voucher " + (new Date()).toString());
			  excel.setCantidadRegistros(registros);
			  logger.info(excel);
			  Excel excelAdd = excelRepository.save(excel);
			  
			  //comprobar error
		      if (bandera) {
		    	  System.out.print("El voucher se encuentra en la BD, revisar Archivo a cargar");
		    	  vouchers.clear();
		    	  excelRepository.delete(excelAdd);
		      }  	  
		      
		      if (!voucherProceso.getError().isEmpty()) {
		    	  excelRepository.delete(excelAdd);
		      }
		     
			  for(Voucher v : vouchers) {
		    	  v.setExcel(excelAdd);
		    	  v.setEstadosPasados("EMITIDO el día "+ f2.format(excelAdd.getFecha())+" por el usuario: "+"agregar usuario");
		    	  v.setHabilitado(false);
		      }
			  
		      voucherRepository.saveAll(vouchers);
		      List<Voucher> addVouchers = vouchers;
		      voucherProceso.setVouchers(addVouchers);
		      return voucherProceso;
		} catch (IOException e) {
		      throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}
	
	/* public void save(MultipartFile file) {
    try {
      List<Tutorial> tutorials = ExcelHelper.excelToTutorials(file.getInputStream());
      repository.saveAll(tutorials);
    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
  }*/

//	@Override
//	public Voucher updateVoucher(Voucher voucher) throws Exception {
//		Voucher updateVoucher = voucherRepository.save(voucher);
//		return updateVoucher;
//	}

	@Override
	public Voucher deleteVoucher(String vpucherId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Voucher> getVouchers() {
		List<Voucher> vouchers = voucherRepository.findAll();
		return vouchers;
	}


	@Override
	public Voucher getCodigoVoucher(String cv) {
		Voucher voucher = voucherRepository.findByCodigoVoucher(cv);
		return voucher;
	}


	@Override
	public Optional<Voucher> getVoucher(String id) {
		Optional<Voucher> voucher = voucherRepository.findById(id);
		return voucher;
	}


	//Elimiar voucher
	@Override
	public Voucher estadoEliminarVoucher(Voucher voucher) throws Exception {
		Optional<Voucher> v = voucherRepository.findById(voucher.get_id());
		if(v.get().getEstado().equals("E")) {
			voucher.setEstadosPasados(v.get().getEstadosPasados()+'\n'+"ELIMINADO el día "+ f2.format(new Date())+" por el usuario: "+"agregar usuario");
			voucher.setEstado("EL");
			//probar si es necesaria esta linea
			voucher.setObservacion(voucher.getObservacion());
			Voucher voucherAdd = voucherRepository.save(voucher);
			return voucherAdd;
		}else {
			return v.get();
		}
		
	}


	//Extender vigencia de voucher vencido
	@Override
	public Voucher estadoExtenderVigencia(Voucher voucher) throws Exception {
		Optional<Voucher> v = voucherRepository.findById(voucher.get_id());
		if(v.get().getEstado().equals("V")) {
			voucher.setEstadosPasados(v.get().getEstadosPasados()+'\n'+"EMITIDO con extención de VIGENCIA, el día "+ f2.format(new Date())+" por el usuario: "+"agregar usuario");
			voucher.setEstado("E");
			Voucher voucherAdd = voucherRepository.save(voucher);
			return voucherAdd;
		}else {
			return v.get();
		}
	}


	@Override
	public Voucher estadoNoDisponible(Voucher voucher) throws Exception {
		Optional<Voucher> v = voucherRepository.findById(voucher.get_id());
		if(v.get().getEstado().equals("U")) {
				voucher.setEstadosPasados(v.get().getEstadosPasados()+'\n'+"NO DISPONIBLE el día "+ f2.format(new Date())+" por el usuario: "+"agregar usuario");
				voucher.setEstado("ND");
				Voucher voucherAdd = voucherRepository.save(voucher);
				return voucherAdd;
		}else {
			System.out.print("El voucher no está en estado UTILIZADO");
			return v.get();
		}
	}


	@Override
	public Voucher duplicadoVoucher(Voucher voucher) throws Exception {
		Optional<Voucher> v = voucherRepository.findById(voucher.get_id());
		if (v.get().getIdCopia() == null && v.get().getEstado().equals("ND")) {;
			Voucher voucherD = new Voucher();
			voucherD.setCodigoVoucher(v.get().getCodigoVoucher()+"-D");
			voucherD.setDni(v.get().getDni());
			voucherD.setEmpresa(v.get().getEmpresa());
			voucherD.setFechaDesde(v.get().getFechaDesde());
			voucherD.setFechaHasta(v.get().getFechaHasta());
			voucherD.setHabilitado(true);
			voucherD.setNombreApellido(v.get().getNombreApellido());
			voucherD.setEstado("E");
			voucherD.setTipoDoc(v.get().getTipoDoc());
			voucherD.setValor(v.get().getValor());
			voucherD.setPuntoVenta(v.get().getPuntoVenta());
			voucherD.setCodigoBarras(v.get().getCodigoBarras());
			voucherD.setEstadosPasados("DUPLICADO del voucher "+v.get().getCodigoVoucher()+", el día "+f2.format(new Date())+" por el usuario: "+"agregar usuario");
			//TODO: hacer set de empresa de emision
			
			return voucherRepository.save(voucherD);
			
		}
		return null;
	}


	@Override
	public Voucher voucherDupliAsociado(Voucher voucher, Voucher duplicado) {
		Optional<Voucher> v = voucherRepository.findById(duplicado.get_id());
		voucher.setIdCopia(v.get().get_id());
		voucher.setEstadosPasados(v.get().getEstadosPasados()+'\n'+"DUPLICADO el día "+f2.format(new Date())+" por el usuario: "+"agregar usuario");
		return voucherRepository.save(voucher);
		
	}
	

}
