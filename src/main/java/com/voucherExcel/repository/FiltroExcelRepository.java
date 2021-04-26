package com.voucherExcel.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;


public interface FiltroExcelRepository extends CrudRepository<Excel, String>{

	Page<Excel> findAll(Pageable pageable);
	
	Page<Excel> findByEstado(String estado, Pageable paging);
	@Query(value = "{'fecha' : {$gte : ?0, $lte: ?1}}")
	Page<Excel> findByFecha(LocalDate desde, LocalDate hasta, Pageable pageable);
	@Query(value = "{'estado' : ?0, 'fecha' : {$gte : ?1, $lte: ?2}}")
	Page<Excel> findByEstadoAndFecha(String estado, LocalDate desde, LocalDate hasta, Pageable pageable);

}
