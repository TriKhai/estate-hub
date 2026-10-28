package com.javaweb.service.impl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.javaweb.Beans.TransactionCreateRequest;
import com.javaweb.Beans.TransactionDTO;
import com.javaweb.customException.CustomerNotFoundException;
import com.javaweb.customException.InvalidCustomerException;
import com.javaweb.repository.CustomerRepository;
import com.javaweb.repository.TransactionRepository;
import com.javaweb.repository.TransactionTypeRepository;
import com.javaweb.repository.entity.TransactionEntity;
import com.javaweb.service.TransactionService;
@Service
public class TransactionServiceImpl implements TransactionService {
    private final CustomerRepository customers;
    private final TransactionRepository transactions;
    private final TransactionTypeRepository types;
    public TransactionServiceImpl(CustomerRepository customers,
            TransactionRepository transactions, TransactionTypeRepository types) {
        this.customers = customers;
        this.transactions = transactions;
        this.types = types;
    }
    private void requireCustomer(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new InvalidCustomerException("ID khách hàng không hợp lệ");
        }
        if (customers.findById(customerId) == null) {
            throw new CustomerNotFoundException("Không tìm thấy khách hàng có ID " + customerId);
        }
    }
    @Override
    public List<TransactionDTO> findByCustomerId(Long customerId) {
        requireCustomer(customerId);
        List<TransactionDTO> result = new ArrayList<>();
        for (TransactionEntity entity : transactions.findByCustomerId(customerId)) {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(entity.getId());
            dto.setCustomerId(entity.getCustomerId());
            dto.setTypeId(entity.getTypeId());
            dto.setTypeName(entity.getTypeName());
            dto.setNote(entity.getNote());
            dto.setCreatedDate(entity.getCreatedDate());
            result.add(dto);
        }
        return result;
    }
    @Override
    public Long create(Long customerId, TransactionCreateRequest request) {
        if (request == null) {
            throw new InvalidCustomerException("Thiếu nội dung chăm sóc khách hàng");
        }
        if (request.getTypeId() == null || request.getTypeId() <= 0) {
            throw new InvalidCustomerException("ID loại chăm sóc không hợp lệ");
        }
        String note = request.getNote();
        if (note == null || note.trim().isEmpty() || note.trim().length() > 255) {
            throw new InvalidCustomerException("Nội dung chăm sóc phải có từ 1 đến 255 ký tự");
        }
        requireCustomer(customerId);
        if (!types.existsById(request.getTypeId())) {
            throw new InvalidCustomerException("Loại chăm sóc không tồn tại");
        }
        TransactionEntity entity = new TransactionEntity();
        entity.setCustomerId(customerId);
        entity.setTypeId(request.getTypeId());
        entity.setNote(note.trim());
        return transactions.insert(entity);
    }
}
