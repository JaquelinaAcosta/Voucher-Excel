package com.voucherExcel.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.voucherExcel.model.Voucher;

@Repository
public interface VoucherRepository extends MongoRepository<Voucher,String>{
	Optional<Voucher> findById(String id);

}
