package com.voucherExcel.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.voucherExcel.ResponseMessage;

import com.voucherExcel.helpers.ExcelHelper;
import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.model.res.VoucherEstados;
import com.voucherExcel.repository.FiltroVoucherRepository;
import com.voucherExcel.services.VoucherService;

@CrossOrigin("*")
@RestController
@RequestMapping("/apiVoucher")
public class VoucherController {
	
	@Autowired
	VoucherService voucherService;
	
	@Autowired
	FiltroVoucherRepository filtroVoucherRepository;

	private static final Log logger = LogFactory.getLog(VoucherController.class);
	
	//Para carga de archivo csv, todavia no funciona bien
//	@RequestMapping(value = "/csv/voucher",headers = "content-type=multipart/*", 
//			method = RequestMethod.POST, 
//			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
//		logger.info("ALTA Controller");
//	    String message = "";
//
//	    if (CSVHelper.hasCSVFormat(file)) {
//	      try {
//	    	  logger.info("TRY Controller");
//	    	  voucherService.addVoucher(file);
//
//	        message = "Uploaded the file successfully: " + file.getOriginalFilename();
//	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//	      } catch (Exception e) {
//	        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//	      }
//	    }
//
//	    message = "Please upload a csv file!";
//	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
//	  }
	
	
	
	//listado de todos los voucher
	@RequestMapping(value = "/voucher/todos", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Voucher>> getVouchers(){
		try {
		      List<Voucher> vouchers = voucherService.getVouchers();
		 if (vouchers.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		      }

		      return new ResponseEntity<>(vouchers, HttpStatus.OK);
		    } catch (Exception e) {
		      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	}
	
	// Obtencion de voucher a traves de codigo de Voucher
	@RequestMapping(value = "/excel/voucher/codigoV/{codigo}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Voucher getVouchersPorCodigo(@PathVariable String codigo){
		return voucherService.getCodigoVoucher(codigo);
	}
	
	// Obtencion de voucher a traves de codigo de Voucher
	@RequestMapping(value = "/voucher/codigo/{id}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Optional<Voucher> getVouchersPorId(@PathVariable String id){
		return voucherService.getVoucher(id);
	}
	
	//CAMBIOS DE ESTADOS VOUCHER
	//cambio de estado a eliminado
	@RequestMapping(value = "/voucher/eliminar", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
	consumes=MediaType.APPLICATION_JSON_VALUE)
	public Voucher estadoEliminarVoucher(@RequestBody @Valid VoucherEstados voucher) throws Exception {
		return voucherService.estadoEliminarVoucher(voucher);	
	}
	
	//cambio de estado de Vencido a Emitido
	@RequestMapping(value = "/voucher/extenderVigencia", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
	consumes=MediaType.APPLICATION_JSON_VALUE)
	public Voucher estadoExtenderVigencia(@RequestBody @Valid VoucherEstados voucher) throws Exception {
		return voucherService.estadoExtenderVigencia(voucher);	
	}
	
	
	//cambio de estado de Utilizado a NoDisponible (ver campo observacion obligatorio en el FRONT)
	@RequestMapping(value = "/voucher/no-disponible", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
	consumes=MediaType.APPLICATION_JSON_VALUE)
	public Voucher estadoNoDisponible(@RequestBody @Valid VoucherEstados voucher) throws Exception {
		return voucherService.estadoNoDisponible(voucher);	
	}
		
		
	//cambio de estado de Utilizado a Duplicado (ver campo observacion obligatorio en el FRONT y validar la empresa de emision)
	@RequestMapping(value = "/voucher/duplicado", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
	consumes=MediaType.APPLICATION_JSON_VALUE)
	public Voucher duplicadoVoucher(@RequestBody @Valid VoucherEstados voucher) throws Exception {
		Voucher duplicado = voucherService.duplicadoVoucher(voucher);	
		return voucherService.voucherDupliAsociado(voucher, duplicado);
	}
	
	//Filtro de busqueda 
	 @GetMapping("/voucher/filtro")
	  public ResponseEntity<Map<String, Object>> getAllVocuhers(
	        @RequestParam(required = false) String estado,
	        @RequestParam(required = false) String dni,
	        @RequestParam(required = false) String codigo,
	        @RequestParam(required = false) String fechaD,
	        @RequestParam(required = false) String fechaH,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "3") int size
	      ) {

	    try {
	      List<Voucher> vouchers = new ArrayList<Voucher>();
	      Pageable paging = PageRequest.of(page, size);
	      
	      Page<Voucher> pageVous;
	      
	            
	      if (estado == null && dni == null && codigo == null && fechaD== null && fechaH== null)
	      	  pageVous = filtroVoucherRepository.findAll(paging);
	      else if (codigo != null) {
	    	  //como el codigo de voucher es unico y no se repite, al aplicar el filtro con codigo solo ingresa en este if
	    	  pageVous = filtroVoucherRepository.findByCodigoVoucher(codigo, paging);
	      }
	      else if (fechaD != null && fechaH != null && estado != null && dni != null) { 
	    	  LocalDate desde = LocalDate.parse(fechaD);
			  LocalDate hasta = LocalDate.parse(fechaH);
	    	  pageVous = filtroVoucherRepository.findByDniAndEstadoAndFechaHasta(dni, estado, desde, hasta, paging);
	      }
	      else if (estado != null && dni != null && fechaD == null && fechaH == null) {
	    	  pageVous = filtroVoucherRepository.findByEstadoAndDni(estado, dni, paging);
	      }
	      else if (fechaD != null && fechaH != null && estado != null && dni == null) { 
	    	  LocalDate desde = LocalDate.parse(fechaD);
			  LocalDate hasta = LocalDate.parse(fechaH);
	    	  pageVous = filtroVoucherRepository.findByEstadoAndFechaHasta(estado, desde, hasta, paging);
	      }
	      else if (fechaD != null && fechaH != null && estado == null && dni != null) {
	    	  LocalDate desde = LocalDate.parse(fechaD);
			  LocalDate hasta = LocalDate.parse(fechaH);
	    	  pageVous = filtroVoucherRepository.findByDniAndFechaHasta(dni, desde, hasta, paging);
	      }
	      else if (fechaD != null && fechaH != null && estado == null && dni == null) { 
	    	  LocalDate desde = LocalDate.parse(fechaD);
			  LocalDate hasta = LocalDate.parse(fechaH);
	    	  pageVous = filtroVoucherRepository.findByFechaHasta(desde, hasta, paging);
	      }
	      else if (estado != null && dni == null && fechaD == null && fechaH == null) {
	    	  pageVous = filtroVoucherRepository.findByEstado(estado, paging);
	      }
	      else {
	    	  pageVous = filtroVoucherRepository.findByDni(dni, paging);
	      }
   
	      vouchers = pageVous.getContent();
	      
	      Map<String, Object> response = new HashMap<>();
	      response.put("estados", vouchers);
	      response.put("currentPage", pageVous.getNumber());
	      response.put("totalItems", pageVous.getTotalElements());
	      response.put("totalPages", pageVous.getTotalPages());

	      return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	    }
	 

}
