package com.voucherExcel.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.voucherExcel.model.Voucher;
import com.voucherExcel.repository.VoucherRepository;

@SpringBootApplication
@EnableScheduling
@Service
public class ProcesarVoucherVencidoScheduled {
	
	@Inject
	private VoucherRepository voucherRepository;	

	private SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yyyy");
	private static final Log logger = LogFactory.getLog(ProcesarVoucherVencidoScheduled.class);
	
	//Voucher vencido
	@Async
	@Scheduled(cron = "0 0 1 * * ?") 
	public void estadoVencidoVoucher() throws Exception {
		List<Voucher> vouchersE = vouchersEmitidos();
		
		Date hoy = new Date();
		String hoy1 = f2.format(hoy);
		for(Voucher v: vouchersE) {
			String fechaH = f2.format(v.getFechaHasta());
			if(f2.parse(fechaH).before(f2.parse(hoy1))) {
				v.setEstado("V");
				v.setEstadosPasados(v.getEstadosPasados()+'\n'+"VENCIDO el dia "+ f2.format(new Date())+" por SISTEMA");
				voucherRepository.save(v);
			}
		}		
	}
	
	public List<Voucher> vouchersEmitidos(){
		//obtener los vouchers que estan en estado EMITIDO y Habilitados
		List<Voucher> vouchersEmitidos = voucherRepository.findByEstadoAndHabilitado("E", true);
		return vouchersEmitidos;
	}

}
