package dao.Repositories.JDBC;

import commons.Constants;
import dao.Repositories.Interfaces.PaymentRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.PaymentsSQLMapper;
import dao.model.Payment;
import domain.exceptions.DataBaseError;
import jakarta.inject.Inject;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCPaymentRepository implements PaymentRepository {

    private final DBConnection db;
    private final PaymentsSQLMapper paymentsSQLMapper;

    @Inject
    public JDBCPaymentRepository(DBConnection db, PaymentsSQLMapper paymentsSQLMapper) {
        this.db = db;
        this.paymentsSQLMapper = paymentsSQLMapper;

    }


    @Override
    public List<Payment> getAllById(int id) {
        List<Payment> payments = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(SQLQuery.SELECT_PAYMENTS_BYPATIENTID)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    payments.add(paymentsSQLMapper.mapPayments(rs));
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(JDBCPaymentRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return payments;
    }

    @Override
    public void restore(Payment payment) {

            try (Connection c = db.getConnection();
                 PreparedStatement ps = c.prepareStatement(SQLQuery.RESTORE_PAYMENT)) {
                ps.setInt(1, payment.getId());
                ps.setInt(2, payment.getPatient_id());
                ps.setDouble(3, payment.getAmount());
                ps.setDate(4, Date.valueOf(payment.getPayment_date()));
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new DataBaseError(Constants.DATABASE_ERROR);
                }
            } catch (SQLException e) {
                Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DataBaseError(Constants.DATABASE_ERROR);
            }
        }

}
