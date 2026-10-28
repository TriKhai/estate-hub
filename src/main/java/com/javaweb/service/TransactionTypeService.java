package com.javaweb.service;
import java.util.List;
import com.javaweb.Beans.TransactionTypeDTO;
public interface TransactionTypeService {
    List<TransactionTypeDTO> findAll();
}
