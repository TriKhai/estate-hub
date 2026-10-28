package com.javaweb.api;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.javaweb.Beans.TransactionCreateRequest;
import com.javaweb.Beans.TransactionCreateResponse;
import com.javaweb.Beans.TransactionDTO;
import com.javaweb.service.TransactionService;
@RestController
public class TransactionAPI {
    private final TransactionService service;
    public TransactionAPI(TransactionService service) { this.service = service; }
    @GetMapping("/api/customers/{customerId}/transactions")
    public ResponseEntity<List<TransactionDTO>> findByCustomerId(
            @PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok(service.findByCustomerId(customerId));
    }
    @PostMapping("/api/customers/{customerId}/transactions")
    public ResponseEntity<TransactionCreateResponse> create(
            @PathVariable("customerId") Long customerId,
            @RequestBody TransactionCreateRequest request) {
        TransactionCreateResponse response = new TransactionCreateResponse();
        response.setId(service.create(customerId, request));
        return ResponseEntity.status(201).body(response);
    }
}
