package com.javaweb.service;
import java.util.List;
import com.javaweb.Beans.*;
public interface TransactionService {
    List<TransactionDTO> findByCustomerId(Long customerId);
    Long create(Long customerId, TransactionCreateRequest request);
}
