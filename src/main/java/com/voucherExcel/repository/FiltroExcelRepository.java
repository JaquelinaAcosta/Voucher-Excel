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
	
	Page<Excel> findByEmpresaEmision(String empresaEmision, Pageable pageable);
	Page<Excel> findByEstadoAndEmpresaEmision(String estado,String empresaEmision,  Pageable paging);
	@Query(value = "{'fecha' : {$gte : ?0, $lte: ?1}, 'empresaEmision' : ?2}")
	Page<Excel> findByFechaAndEmpresaEmision(LocalDate desde, LocalDate hasta,String empresaEmision,  Pageable pageable);
	@Query(value = "{'estado' : ?0, 'fecha' : {$gte : ?1, $lte: ?2}, 'empresaEmision' : ?3}")
	Page<Excel> findByEstadoAndFechaAndEmpresaEmision(String estado, LocalDate desde, LocalDate hasta, String empresaEmision, Pageable pageable);

	@Query(value = "{'estado' : ?0, 'fecha' : {$gte : ?1, $lte: ?2}}")
	Page<Excel> findByEstadoAndFecha(String estado, LocalDate desde, LocalDate hasta, Pageable pageable);
	@Query(value = "{'fecha' : {$gte : ?0, $lte: ?1}}")
	Page<Excel> findByFecha(LocalDate desde, LocalDate hasta,  Pageable pageable);
	Page<Excel> findByEstado(String estado, Pageable paging);

}
