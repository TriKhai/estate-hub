package com.javaweb;
import java.lang.reflect.Proxy;
import java.util.*;
import com.javaweb.Beans.*;
import com.javaweb.repository.*;
import com.javaweb.repository.entity.*;
import com.javaweb.service.impl.TransactionServiceImpl;
import com.javaweb.customException.*;

/** Run main with -ea; uses repository doubles, no database writes. */
public class TransactionServiceCheck {
    static int checks;
    static TransactionEntity saved;
    static boolean customerExists = true, typeExists = true;
    static void expect(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); } catch (Throwable e) {
            if (!type.isInstance(e)) throw new AssertionError(e);
            checks++; return;
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
    static TransactionCreateRequest request(Long type, String note) {
        TransactionCreateRequest r = new TransactionCreateRequest();
        r.setTypeId(type); r.setNote(note); return r;
    }
    public static void main(String[] args) {
        CustomerRepository customers = (CustomerRepository) Proxy.newProxyInstance(
            CustomerRepository.class.getClassLoader(), new Class<?>[]{CustomerRepository.class},
            (proxy, method, values) -> {
                if (method.getName().equals("findById")) return customerExists ? new CustomerEntity() : null;
                throw new AssertionError("Unexpected customer method");
            });
        TransactionRepository transactions = new TransactionRepository() {
            public Long insert(TransactionEntity e) { saved = e; return 42L; }
            public List<TransactionEntity> findByCustomerId(Long id) {
                return saved == null ? Collections.emptyList() : Arrays.asList(saved);
            }
        };
        TransactionTypeRepository types = new TransactionTypeRepository() {
            public boolean existsById(Long id) { return typeExists; }
            public List<TransactionTypeEntity> findAll() { return Collections.emptyList(); }
        };
        TransactionServiceImpl service = new TransactionServiceImpl(customers, transactions, types);
        assert service.findByCustomerId(1L).isEmpty(); checks++;
        expect(InvalidCustomerException.class, () -> service.findByCustomerId(0L));
        expect(InvalidCustomerException.class, () -> service.create(1L, null));
        expect(InvalidCustomerException.class, () -> service.create(1L, request(null, "note")));
        expect(InvalidCustomerException.class, () -> service.create(1L, request(-1L, "note")));
        expect(InvalidCustomerException.class, () -> service.create(1L, request(1L, "  ")));
        expect(InvalidCustomerException.class, () -> service.create(1L, request(1L, null)));
        expect(InvalidCustomerException.class, () -> service.create(1L, request(1L, String.join("", Collections.nCopies(256,"a")))));
        customerExists = false;
        expect(CustomerNotFoundException.class, () -> service.findByCustomerId(1L));
        expect(CustomerNotFoundException.class, () -> service.create(1L, request(1L,"note")));
        customerExists = true; typeExists = false;
        expect(InvalidCustomerException.class, () -> service.create(1L, request(1L,"note")));
        assert saved == null; checks++;
        typeExists = true;
        assert service.create(5L, request(2L,"  Called O'Connor  ")) == 42L;
        assert saved.getCustomerId() == 5L && saved.getTypeId() == 2L;
        assert saved.getNote().equals("Called O'Connor"); checks++;
        saved.setTypeName("Call");
        assert service.findByCustomerId(5L).get(0).getTypeName().equals("Call"); checks++;
        System.out.println("PASS: " + checks + " service checks");
    }
}
