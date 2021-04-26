package com.voucherExcel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.voucherExcel.model.Excel;
import com.voucherExcel.model.Voucher;

@Repository
public interface VoucherRepository extends MongoRepository<Voucher,String>{
	Optional<Voucher> findById(String id);

	Voucher findByCodigoVoucher(String codigoVoucher);
	
	List<Voucher> findByExcel(Excel excel);
	Voucher findByEstado(String estado);

	@Query(value = "{'estado' : ?0 , 'habilitado' : ?1}")
	List<Voucher> findByEstadoAndHabilitado(String estado, Boolean habilitado);
	
	Voucher findByCodigoVoucherAndDni(String codigoVoucher, String dni);	
	
}
