package com.voucherExcel.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.voucherExcel.ResponseMessage;
import com.voucherExcel.helpers.CSVHelper;
import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.services.ExcelService;
import com.voucherExcel.services.VoucherService;

@CrossOrigin("*")
@RestController
@RequestMapping("/apiExcel")
public class ExcelController {

	@Autowired
	VoucherService voucherService;
	@Autowired
	ExcelService excelService;
	
	private static final Log logger = LogFactory.getLog(ExcelController.class);
	
	
	//Carga archivo Excel y alta voucher desde Excel
	@RequestMapping(value = "/excel/importar",
			headers = "content-type=multipart/*", 
			method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public ResponseEntity<ResponseMessage> addVoucher(@RequestParam("file") MultipartFile file) {
			String message = "";
			
			 if (CSVHelper.hasExcelFormat(file)) {
			      try {
			    	  voucherService.addVoucherExcel(file);    	  
			    	  message = "Uploaded the file successfully: " + file.getOriginalFilename();
			    	  return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			      } catch (Exception e) {
			    	  message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			          return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			      }
			    }
			    message = "Please upload an excel file!";
			    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
	
	
	
	//modificacion de estado Excel "DISPONIBLE" y habilitacion de voucher para que sea utilizable
		@RequestMapping(value = "/excel/disponible", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
		consumes=MediaType.APPLICATION_JSON_VALUE)
		public Excel updateExcel(@RequestBody @Valid Excel excel) throws Exception {
			return excelService.updateExcel(excel);	
		}
		
		//modificacion de estado Excel "CANCELADO" y eliminacion del Voucher asocialdo (Eliminacion fisica para que no cree conflictos y ocupe memoria en BD)
		@RequestMapping(value = "/excel/cancelar", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
		consumes=MediaType.APPLICATION_JSON_VALUE)
		public Excel cancelarExcel(@RequestBody @Valid Excel excel) throws Exception {
			return excelService.cancelarExcel(excel);	
		}
		
		//listado de archivos Excel
		@RequestMapping(value = "/excel/todos", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
		public List<Excel> getExcel()
		{
			return excelService.getExcels();
		}
		
		//listado de archivos Excel por estados
		@RequestMapping(value = "/excel/listaEstados/{estado}", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
		public List<Excel> getExcelEstados(@PathVariable String estado)
		{
			return excelService.getExcelsEstado(estado);
		}
		
		
		
}
