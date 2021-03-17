package com.voucherExcel.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.voucherExcel.controller.VoucherController;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.services.VoucherService;


public class CSVHelper {
	
	private static Boolean errorExcel = false;
	private static Boolean errorExcelCV = false;
	
	@Autowired
	static VoucherService voucherService;
	
	@Autowired
	static VoucherController controlador;
	
	
	private static final Log logger = LogFactory.getLog(CSVHelper.class);
	
	 public static String TYPECSV = "text/csv";
	  static String[] HEADERsCSV = { "tipoDoc", "dni", "nombreApellido", "valor", "fechaDesde", "fechaHasta", "empresa", "estado", "codigoVoucher", "codigoBarras", "puntoVenta" };

	  public static boolean hasCSVFormat(MultipartFile file) {

	    if (!TYPECSV.equals(file.getContentType())) {
	      return false;
	    }

	    return true;
	  }

	  public static List<Voucher> csvToVouchers(InputStream is){
	    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        CSVParser csvParser = new CSVParser(fileReader,
	            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
	    	logger.info("Entro al TRY CSVHELPER");
	    	
	    	logger.info("IS");
	    	logger.info(is);

	      List<Voucher> vouchers = new ArrayList<Voucher>();

	      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
	      logger.info("CSV RECORDS");
	      logger.info(csvRecords);
	     
	      DateFormat fechaDesde = new SimpleDateFormat("dd-MM-yyyy");
	      DateFormat fechaHasta = new SimpleDateFormat("dd-MM-yyyy");

	      for (CSVRecord csvRecord : csvRecords) {
	    	  logger.info("CSV FOR");
	    	  logger.info(csvRecords);
	    	  logger.info(csvRecord);
	    	  logger.info("DNIII");

	    	  Voucher voucher = new Voucher(
//	              Integer.parseInt(csvRecord.get("tipoDoc")),
//	              csvRecord.get("dni"),
//	              csvRecord.get("nombreApellido"),
//	              Integer.parseInt(csvRecord.get("valor")),
//	              fechaDesde.parse(csvRecord.get("fechaDesde")),
//	              fechaHasta.parse(csvRecord.get("fechaHasta")),
//	              csvRecord.get("empresa"),
//	              csvRecord.get("estado"),
//	              csvRecord.get("codigoVoucher"),
//	              csvRecord.get("codigoBarras"),
//	              Integer.parseInt(csvRecord.get("puntoVenta"))
	            );

	        vouchers.add(voucher);
	        logger.info("voucher");
	        logger.info(voucher);
	        
	      }
	      
	      

	      return vouchers;
	   
	  }catch (IOException e) {
	      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
	    } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return null;}
	
	
	
	
	
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  
 // public static String TYPE ="application/vnd.ms-excel";
  static String[] HEADERs = { "tipoDoc", "dni", "nombreApellido", "valor", "fechaDesde", "fechaHasta", "empresa", "estado", "codigoVoucher", "codigoBarras", "puntoVenta" };
  static String SHEET = "Vouchers";
  
  public static boolean hasExcelFormat(MultipartFile file){
	  if (!TYPE.equals(file.getContentType())) {
		  return false;
	  }
	  return true;
  }

  public static List<Voucher> excelToVouchers(InputStream is) {
	  try {
		  Workbook workbook = new XSSFWorkbook(is);

		  Sheet sheet = workbook.getSheetAt(0);
		  //      Sheet sheet = workbook.createSheet();

		  Iterator<Row> rows = sheet.iterator();

		  List<Voucher> vouchers = new ArrayList<Voucher>();
		  List<String> codigoVoucher = new ArrayList<String>();

		  int rowNumber = 0;

		  while (rows.hasNext()) {

			  Row currentRow = rows.next();

			  // skip header
			  if (rowNumber == 0) {
				  rowNumber++;
				  continue;
			  }

			  Iterator<Cell> cellsInRow = currentRow.iterator();

			  Voucher voucher = new Voucher();

			  int cellIdx = 0;
			  while (cellsInRow.hasNext()) {
				  Cell currentCell = cellsInRow.next();

				  switch (cellIdx) {

				  case 0:
					  voucher.setTipoDoc((int) currentCell.getNumericCellValue());
					  break;

				  case 1:
					  voucher.setDni(String.valueOf((int) currentCell.getNumericCellValue()));
					  break;

				  case 2:
					  voucher.setNombreApellido(currentCell.getStringCellValue());
					  break;

				  case 3:
					  voucher.setValor((int) currentCell.getNumericCellValue());
					  break;

				  case 4:    
					  SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy");
					  Date fecha = currentCell.getDateCellValue();
					  String fechaD = f1.format(fecha);
					  try {
						  voucher.setFechaDesde(f1.parse(fechaD));
					  } catch (ParseException ex) {
						  ex.printStackTrace();}
					  break;

				  case 5:
					  SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yyyy");
					 // String fecha2 = currentCell.toString();
					  Date fecha2 = currentCell.getDateCellValue();
					  String fechaH = f2.format(fecha2);
					  Date hoy = new Date();
					  String hoy1 = f2.format(hoy);
					  try {
						  if(f2.parse(hoy1).before(f2.parse(fechaH))) {
							  voucher.setFechaHasta(f2.parse(fechaH)); 
							  logger.info("Guarda fecha");
						  } else {
							  errorExcel = true;
							  logger.info("error excel fecha");
						  }
					//	  voucher.setFechaHasta(f2.parse(fecha2));         	  
					  } catch (ParseException ex) {
						  ex.printStackTrace();}
					  break;

				  case 6:
					  voucher.setEmpresa(currentCell.getStringCellValue());
					  break;

				  case 7:
					  if (currentCell.getStringCellValue().contentEquals("E")) {
						  voucher.setEstado(currentCell.getStringCellValue());
					  }else {
						  errorExcel = true;
						  logger.info("error excel");
					  }         		
					  break;

				  case 8:
					  String codigo = String.valueOf((int) currentCell.getNumericCellValue());
					  					  
					  // controla si el codigo de voucher no esta repetido dentro del archivo excel
					  for(Voucher v : vouchers) {
						  if(v.getCodigoVoucher().equals(codigo)) {
							  errorExcelCV = true;
							  logger.info("CV repetido");
						  }
					  }

					  if (!errorExcelCV) {
						  voucher.setCodigoVoucher(codigo);
						  logger.info("Inserta CV");
					  }else {
						  errorExcel=true;
					  }

					  break;
					  
				  case 9:
					  
					  voucher.setCodigoBarras(String.valueOf((int) currentCell.getNumericCellValue()));
					  break;

				  case 10:
					  voucher.setPuntoVenta((int)currentCell.getNumericCellValue());
					  break;

				  default:
					  break;
				  }

				  cellIdx++;
				  logger.info(cellIdx);
			  }

			  vouchers.add(voucher);

		  }
		  if(errorExcel) {
			  vouchers.clear();
			  System.out.print("Error en la carga de archivo, Revise su archivo");
		  }
		  workbook.close();

		  return vouchers;
	  } catch (IOException e) {
		  throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
	  }
  } 

  
}
  
