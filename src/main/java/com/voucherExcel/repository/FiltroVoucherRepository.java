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
	
	//para usuario ROOT
	Page<Voucher> findByCodigoVoucher(String codigoVoucher, Pageable pageable);
	Page<Voucher> findByDni(String dni, Pageable pageable);
	@Query(value = "{'dni' : ?0, 'estado' : ?1, 'fechaHasta' : {$gte : ?2, $lte: ?3}}")
	Page<Voucher> findByDniAndEstadoAndFechaHasta(String dni, String estado, LocalDate desde, LocalDate hasta, Pageable pageable);
	@Query(value = "{'estado' : ?0, 'fechaHasta' : {$gte : ?1, $lte: ?2}}")
	Page<Voucher> findByEstadoAndFechaHasta(String estado, LocalDate desde, LocalDate hasta, Pageable pageable);
	@Query(value = "{'dni' : ?0, 'fechaHasta' : {$gte : ?1, $lte: ?2}}")
	Page<Voucher> findByDniAndFechaHasta(String dni, LocalDate desde, LocalDate hasta, Pageable pageable);
	@Query(value = "{'fechaHasta' : {$gte : ?0, $lte: ?1}}")
	Page<Voucher> findByFechaHasta(LocalDate desde, LocalDate hasta, Pageable pageable);
	
	
	//Busquedas diferenciando listados por empresa
	
	@Query(value = "{'empresaEmision' : ?0}")
	Page<Voucher> findByEmpresaEmision(String empresaEmision, Pageable pageable);
	
	Page<Voucher> findByEstadoAndEmpresaEmision(String estado, String empresaEmision, Pageable pageable);
	Page<Voucher> findByDniAndEmpresaEmision(String dni, String empresaEmision, Pageable pageable);
	Page<Voucher> findByCodigoVoucherAndEmpresaEmision(String codigoVoucher, String empresaEmision, Pageable pageable);	
	@Query(value = "{'fechaHasta' : {$gte : ?0, $lte: ?1}, 'empresaEmision' : ?2}")
	Page<Voucher> findByFechaHastaAndEmpresaEmision(LocalDate desde, LocalDate hasta, String empresaEmision, Pageable pageable);
	@Query(value = "{'estado' : ?0, 'fechaHasta' : {$gte : ?1, $lte: ?2}, 'empresaEmision' : ?3}")
	Page<Voucher> findByEstadoAndFechaHastaAndEmpresaEmision(String estado, LocalDate desde, LocalDate hasta, String empresaEmision, Pageable pageable);
	@Query(value = "{'dni' : ?0, 'fechaHasta' : {$gte : ?1, $lte: ?2}, 'empresaEmision' : ?3}")
	Page<Voucher> findByDniAndFechaHastaAndEmpresaEmision(String dni, LocalDate desde, LocalDate hasta, String empresaEmision, Pageable pageable);
	@Query(value = "{'dni' : ?0, 'estado' : ?1, 'fechaHasta' : {$gte : ?2, $lte: ?3}, 'empresaEmision' : ?4}")
	Page<Voucher> findByDniAndEstadoAndFechaHastaAndEmpresaEmision(String dni, String estado, LocalDate desde, LocalDate hasta, String empresaEmision, Pageable pageable);
	
	//@Query(value = "{ 'estado' : ?0, 'dni' : ?1}")
	Page<Voucher> findByEstadoAndDniAndEmpresaEmision(String estado, String dni, String empresaEmision, Pageable paging);

	
	

	
	
	
	
}
