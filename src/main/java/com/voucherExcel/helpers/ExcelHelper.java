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
import com.voucherExcel.model.res.VoucherProceso;
import com.voucherExcel.services.VoucherService;


public class ExcelHelper {
	
	private static Boolean errorExcel = false;
	private static Boolean errorExcelCV = false;
	
	@Autowired
	static VoucherService voucherService;
	
	@Autowired
	static VoucherController controlador;
	
	
	private static final Log logger = LogFactory.getLog(ExcelHelper.class);
	
	 public static String TYPECSV = "text/csv";
	  static String[] HEADERsCSV = { "tipoDoc", "dni", "nombreApellido", "valor", "fechaDesde", "fechaHasta", "empresa", "estado", "codigoVoucher", "codigoBarras", "puntoVenta" };

	  public static boolean hasCSVFormat(MultipartFile file) {

	    if (!TYPECSV.equals(file.getContentType())) {
	      return false;
	    }

	    return true;
	  }	
	
	
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

  public static VoucherProceso excelToVouchers(InputStream is) throws IOException {
	  try {
		  Workbook workbook = new XSSFWorkbook(is);

		  Sheet sheet = workbook.getSheetAt(0);
		  //      Sheet sheet = workbook.createSheet();

		  Iterator<Row> rows = sheet.iterator();
		  
		  VoucherProceso voucherProceso = new VoucherProceso();
		  List<Voucher> vouchers = new ArrayList<Voucher>();
		  List<String> codigoVoucher = new ArrayList<String>();
		  String errores = new String();
		  errores = "";
		  
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
						  } else {
							  errorExcel = true;
							  voucher.setFechaHasta(f2.parse(fechaH)); 
							  errores = errores+'\n'+"Revisar la fila: "+ currentCell.getRowIndex()+ ". La fecha de vencimiento: "+ fechaH+" es menor a la fecha del dia";
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
						  voucher.setEstado(currentCell.getStringCellValue());
						  errores = errores +'\n'+"Revisar la fila: "+ currentCell.getRowIndex()+ ". El estado de voucher tiene que ser E.";
					  }         		
					  break;

				  case 8:
					  String codigo = String.valueOf((int) currentCell.getNumericCellValue());
					  					  
					  // controla si el codigo de voucher no esta repetido dentro del archivo excel
					  for(Voucher v : vouchers) {
						  if(v.getCodigoVoucher().equals(codigo)) {
							  errorExcelCV = true;
						  }
					  }
					  if (!errorExcelCV) {
						  voucher.setCodigoVoucher(codigo);
					  }else {
						  errorExcel = true;
						  errorExcelCV = false;
						  voucher.setCodigoVoucher(codigo);
						  errores = errores +'\n'+"Revisar la fila: "+ currentCell.getRowIndex()+ ". El codigo de voucher: " + codigo + " esta repetido dentro del archivo";
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
			  }
			  vouchers.add(voucher);
		  }
		  
		  if(errorExcel) {			  
			  System.out.println(errores);
			  vouchers.clear();
		  }
		  
		  workbook.close();
		  
		  voucherProceso.setError(errores);
		  voucherProceso.setVouchers(vouchers);

		  System.out.println(voucherProceso.getError());
		  System.out.println(voucherProceso.getVouchers());

		  return voucherProceso;
	  } catch (IOException e) {
		  throw new RuntimeException("No se puede analizar el archivo de Excel: " + e.getMessage());
	  }
  }

  
}
  
