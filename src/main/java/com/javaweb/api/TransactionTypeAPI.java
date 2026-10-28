package com.javaweb.api;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.javaweb.Beans.TransactionTypeDTO;
import com.javaweb.service.TransactionTypeService;
@RestController
public class TransactionTypeAPI {
    private final TransactionTypeService service;
    public TransactionTypeAPI(TransactionTypeService service) { this.service = service; }
    @GetMapping("/api/transaction-types")
    public ResponseEntity<List<TransactionTypeDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
