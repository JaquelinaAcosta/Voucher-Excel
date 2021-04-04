package com.voucherExcel.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.voucherExcel.helpers.CSVHelper;
import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.services.VoucherService;

@RestController
@RequestMapping("/api")
public class VoucherController {
	
	@Autowired
	VoucherService voucherService;
	
	private static final Log logger = LogFactory.getLog(VoucherController.class);
	
	//Para carga de archivo csv, todavia no funciona bien
	@RequestMapping(value = "/csv/voucher",headers = "content-type=multipart/*", 
			method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		logger.info("ALTA Controller");
	    String message = "";

	    if (CSVHelper.hasCSVFormat(file)) {
	      try {
	    	  logger.info("TRY Controller");
	    	  voucherService.addVoucher(file);

	        message = "Uploaded the file successfully: " + file.getOriginalFilename();
	        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	      } catch (Exception e) {
	        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	      }
	    }

	    message = "Please upload a csv file!";
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	  }
	
	
	
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
	public Voucher estadoEliminarVoucher(@RequestBody @Valid Voucher voucher) throws Exception {
		return voucherService.estadoEliminarVoucher(voucher);	
	}
	
	//cambio de estado de Vencido a Emitido
	@RequestMapping(value = "/voucher/extenderVigencia", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
	consumes=MediaType.APPLICATION_JSON_VALUE)
	public Voucher estadoExtenderVigencia(@RequestBody @Valid Voucher voucher) throws Exception {
		return voucherService.estadoExtenderVigencia(voucher);	
	}
}
