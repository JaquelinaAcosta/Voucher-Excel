package com.voucherExcel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.voucherExcel.model.Excel;

@Repository
public interface ExcelRepository extends MongoRepository<Excel,String>{

	Optional<Excel> findById(String id);
	
	List<Excel> findByEstado(String estado);
}
