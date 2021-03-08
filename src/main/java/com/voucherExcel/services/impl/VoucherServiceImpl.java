package com.voucherExcel.services.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.voucherExcel.helpers.CSVHelper;
import com.voucherExcel.model.Voucher;
import com.voucherExcel.repository.VoucherRepository;
import com.voucherExcel.services.VoucherService;

@Service
public class VoucherServiceImpl implements VoucherService {

	
	@Inject
	private VoucherRepository voucherRepository;
	
	private static final Log logger = LogFactory.getLog(VoucherServiceImpl.class);

	  public void addVoucher(MultipartFile file) {
	    try {
	      List<Voucher> vouchers = CSVHelper.csvToVouchers(file.getInputStream());
	      voucherRepository.saveAll(vouchers);
	    } catch (IOException e) {
	      throw new RuntimeException("fail to store csv data: " + e.getMessage());
	    }
	  }

	 
	@Override
	public void addVoucherExcel(MultipartFile file) {
		logger.info("ALTA");
		try {
			logger.info("ALTA TRY");
			List<Voucher> vouchers = CSVHelper.excelToVouchers(file.getInputStream());
			voucherRepository.saveAll(vouchers);
			logger.info("ALTA GUARDO");
		} catch (IOException e) {
		      throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
		logger.info("FUERA ALTA");
	}
	
	/* public void save(MultipartFile file) {
    try {
      List<Tutorial> tutorials = ExcelHelper.excelToTutorials(file.getInputStream());
      repository.saveAll(tutorials);
    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
  }*/

	@Override
	public Voucher updateVoucher(Voucher voucher) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

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
	
	
	
	

}
