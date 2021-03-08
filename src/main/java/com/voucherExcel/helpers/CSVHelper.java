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
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.voucherExcel.model.Voucher;


public class CSVHelper {
	
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

	      List<Voucher> vouchers = new ArrayList<Voucher>();

	      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
	     
	      DateFormat fechaDesde = new SimpleDateFormat("dd-MM-yyyy");
	      DateFormat fechaHasta = new SimpleDateFormat("dd-MM-yyyy");

	      for (CSVRecord csvRecord : csvRecords) {
	    	  Voucher voucher = new Voucher(
	              Integer.parseInt(csvRecord.get("tipoDoc")),
	              csvRecord.get("dni"),
	              csvRecord.get("nombreApellido"),
	              Integer.parseInt(csvRecord.get("valor")),
	              fechaDesde.parse(csvRecord.get("fechaDesde")),
	              fechaHasta.parse(csvRecord.get("fechaHasta")),
	              csvRecord.get("empresa"),
	              csvRecord.get("estado"),
	              csvRecord.get("codigoVoucher"),
	              csvRecord.get("codigoBarras"),
	              Integer.parseInt(csvRecord.get("puntoVenta"))
	            );

	        vouchers.add(voucher);
	      }
	      
	      

	      return vouchers;
	   
	  }catch (IOException e) {
	      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
	    } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	      Sheet sheet = workbook.getSheet(SHEET);
	      Iterator<Row> rows = sheet.iterator();

	      List<Voucher> vouchers = new ArrayList<Voucher>();

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
	              voucher.setDni(currentCell.getStringCellValue());
	              break;
	           
	            case 2:
	              voucher.setNombreApellido(currentCell.getStringCellValue());
	            break;

	            case 3:
	              voucher.setValor((int) currentCell.getNumericCellValue());
	            break;
	            
	            case 4:
		          voucher.setFechaDesde(currentCell.getDateCellValue());
		        break;
		        
	            case 5:
		           voucher.setFechaHasta(currentCell.getDateCellValue());
		        break;
		        
	            case 6:
		           voucher.setEmpresa(currentCell.getStringCellValue());
		        break;
		        
	            case 7:
		           voucher.setEstado(currentCell.getStringCellValue());
		        break;
		        
	            case 8:
		           voucher.setCodigoVoucher(currentCell.getStringCellValue());
		        break;
		        
	            case 9:
		           voucher.setCodigoBarras(currentCell.getStringCellValue());
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

	        workbook.close();

	        return vouchers;
	      } catch (IOException e) {
	        throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
	      }
	    } 
  
}
  
