package com.javaweb.repository;

import java.util.List;

import com.javaweb.repository.entity.TransactionEntity;

public interface TransactionRepository {
	List<TransactionEntity> findByCustomerId(Long customerId);
	Long insert(TransactionEntity entity);
}
