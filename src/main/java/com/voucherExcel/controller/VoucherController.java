package com.voucherExcel.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.voucherExcel.model.Voucher;
import com.voucherExcel.services.VoucherService;

@RestController
@RequestMapping("/api")
public class VoucherController {
	
	@Autowired
	VoucherService voucherService;
	
	private static final Log logger = LogFactory.getLog(VoucherController.class);
	
	
	@RequestMapping(value = "/csv/voucher", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
	    String message = "";

	    if (CSVHelper.hasCSVFormat(file)) {
	      try {
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
	
	@RequestMapping(value = "/csv/voucher/todos", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<List<Voucher>> getAllTutorials() {
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
	
	//alta voucher
			@RequestMapping(value = "/excel/importar", headers = "content-type=multipart/*", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		//	@PostMapping("/upload")
//	@RequestMapping(value = "/excel", headers = "content-type=multipart/*", method = RequestMethod.POST)
			public ResponseEntity<ResponseMessage> addVoucher(@RequestParam("file") MultipartFile file) {
					logger.info("ALTA Controller");
					String message = "";
					
					 if (CSVHelper.hasExcelFormat(file)) {
						 logger.info("ALTA if controller");
					      try {
					    	  logger.info("ALTA infreso try conetroller");
					    	  voucherService.addVoucherExcel(file);
					    	  logger.info("ALTA termino de gusrdar controller");
					    	  
					    	  message = "Uploaded the file successfully: " + file.getOriginalFilename();
					    	  return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
					      } catch (Exception e) {
					    	  logger.info("ALTA catch controller");
					    	  message = "Could not upload the file: " + file.getOriginalFilename() + "!";
					          return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
					      }
					    }
					 logger.info("ALTA fuera if controller");
					    message = "Please upload an excel file!";
					    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
			}
	
	//listado de todos los voucher
	@RequestMapping(value = "/excel/voucher/todos", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Voucher>> getVouchers(){
		logger.info("LISTA DE VOUCHEEERRRR");
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
	
	@RequestMapping(value = "/voucher/HOLA", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getVoucherSs(){
		logger.info("LISTA DE VOUCHEEERRRR");
		String message = "HOLIIIISSS";
		return message;
	}
	
	
	
	
	
	
	//opcion 2 importar
	@RequestMapping(value = "/import-excel", method = RequestMethod.POST)
    public ResponseEntity<List<Voucher>> importExcelFile(@RequestParam("file") MultipartFile files) throws IOException {
		logger.info("Entro al controller... HOLAAAAAA");
		HttpStatus status = HttpStatus.OK;
        List<Voucher> vouchers = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
            if (index > 0) {
                Voucher voucher = new Voucher();

                XSSFRow row = worksheet.getRow(index);
//                Integer id = (int) row.getCell(0).getNumericCellValue();

//                voucher.set_id(id.toString());
                voucher.setTipoDoc(row.getCell(0).getColumnIndex());
                voucher.setDni(row.getCell(1).getStringCellValue());
                voucher.setNombreApellido(row.getCell(2).getStringCellValue());
                voucher.setValor(row.getCell(3).getColumnIndex());
                voucher.setFechaDesde(row.getCell(4).getDateCellValue());
                voucher.setFechaHasta(row.getCell(5).getDateCellValue());
                voucher.setEmpresa(row.getCell(6).getStringCellValue());
                voucher.setCodigoVoucher(row.getCell(7).getStringCellValue());
                voucher.setCodigoBarras(row.getCell(8).getStringCellValue());
                voucher.setPuntoVenta(row.getCell(9).getColumnIndex());
                
                
                //https://por-porkaew15.medium.com/how-to-import-excel-by-spring-boot-2624367c8468
                
                vouchers.add(voucher);
            }
        }

        return new ResponseEntity<>(vouchers, status);
    }
	
}
