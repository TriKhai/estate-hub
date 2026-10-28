package com.javaweb.repository;

import java.util.List;

import com.javaweb.repository.entity.TransactionTypeEntity;

public interface TransactionTypeRepository {
	List<TransactionTypeEntity> findAll();
	boolean existsById(Long id);
}
