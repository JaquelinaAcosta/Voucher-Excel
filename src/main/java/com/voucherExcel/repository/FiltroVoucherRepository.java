package com.voucherExcel.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.voucherExcel.model.Voucher;

public interface FiltroVoucherRepository extends CrudRepository<Voucher, String>{

	Page<Voucher> findAll(Pageable pageable);
	
	Page<Voucher> findByEstado(String estado, Pageable pageable);
	Page<Voucher> findByDni(String dni, Pageable pageable);
	Page<Voucher> findByCodigoVoucher(String codigoVoucher, Pageable pageable);	
	@Query(value = "{'fechaHasta' : {$gte : ?0, $lte: ?1}}")
	Page<Voucher> findByFechaHasta(LocalDate desde, LocalDate hasta, Pageable pageable);
	@Query(value = "{'estado' : ?0, 'fechaHasta' : {$gte : ?1, $lte: ?2}}")
	Page<Voucher> findByEstadoAndFechaHasta(String estado, LocalDate desde, LocalDate hasta, Pageable pageable);
	@Query(value = "{'dni' : ?0, 'fechaHasta' : {$gte : ?1, $lte: ?2}}")
	Page<Voucher> findByDniAndFechaHasta(String dni, LocalDate desde, LocalDate hasta, Pageable pageable);
	@Query(value = "{'dni' : ?0, 'estado' : ?1, 'fechaHasta' : {$gte : ?2, $lte: ?3}}")
	Page<Voucher> findByDniAndEstadoAndFechaHasta(String dni, String estado, LocalDate desde, LocalDate hasta, Pageable pageable);
	
	//@Query(value = "{ 'estado' : ?0, 'dni' : ?1}")
	Page<Voucher> findByEstadoAndDni(String estado, String dni, Pageable paging);

	
	

	
	
	
	
}
