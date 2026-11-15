package dao.Repositories.Interfaces;



import dao.model.Payment;

import java.util.List;

public interface PaymentRepository {

    List<Payment> getAllById(int id);

    void restore(Payment payment);

}
