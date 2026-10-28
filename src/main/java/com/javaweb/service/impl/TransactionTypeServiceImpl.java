package com.javaweb.service.impl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.javaweb.Beans.TransactionTypeDTO;
import com.javaweb.repository.TransactionTypeRepository;
import com.javaweb.repository.entity.TransactionTypeEntity;
import com.javaweb.service.TransactionTypeService;
@Service
public class TransactionTypeServiceImpl implements TransactionTypeService {
    private final TransactionTypeRepository repository;
    public TransactionTypeServiceImpl(TransactionTypeRepository repository) {
        this.repository = repository;
    }
    @Override
    public List<TransactionTypeDTO> findAll() {
        List<TransactionTypeDTO> result = new ArrayList<>();
        for (TransactionTypeEntity entity : repository.findAll()) {
            TransactionTypeDTO dto = new TransactionTypeDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setCode(entity.getCode());
            result.add(dto);
        }
        return result;
    }
}
