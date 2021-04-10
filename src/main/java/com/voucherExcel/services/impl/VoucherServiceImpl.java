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
	public List<Voucher> addVoucherExcel(MultipartFile file) {
		bandera = false;
		int registros = 0;
		try {
			List<Voucher> vouchers = CSVHelper.excelToVouchers(file.getInputStream());
			//Controla si el codigo de voucher esta repetido en Base de Datos
		      for(Voucher v : vouchers) {
		    	  Voucher vAux = voucherRepository.findByCodigoVoucher(v.getCodigoVoucher());
		    	  registros++;
		    	  if(vAux!=null) {
		    		  bandera = true;
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
		      
		     
			  for(Voucher v : vouchers) {
		    	  v.setExcel(excelAdd);
		    	  v.setEstadosPasados("EMITIDO el dia "+ f2.format(excelAdd.getFecha())+" por el usuario: "+"agregar usuario");
		      }
			  
		      voucherRepository.saveAll(vouchers);
		      List<Voucher> addVouchers = vouchers;
		      return addVouchers;
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
			voucher.setEstadosPasados(v.get().getEstadosPasados()+'\n'+"ELIMINADO el dia "+ f2.format(new Date())+" por el usuario: "+"agregar usuario");
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
			voucher.setEstadosPasados(v.get().getEstadosPasados()+'\n'+"EMITIDO con extención de VIGENCIA, el dia "+ f2.format(new Date())+" por el usuario: "+"agregar usuario");
			voucher.setEstado("E");
			Voucher voucherAdd = voucherRepository.save(voucher);
			return voucherAdd;
		}else {
			return v.get();
		}
	}
	

}
