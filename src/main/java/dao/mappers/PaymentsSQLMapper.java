package dao.mappers;

import dao.model.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentsSQLMapper {

        public Payment mapPayments(ResultSet rs) throws SQLException {
            Payment payment = new Payment();
            payment.setId(rs.getInt("payment_id"));
            payment.setPatient_id(rs.getInt("patient_id"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setPayment_date(rs.getDate("payment_date").toLocalDate());
            return payment;
        }

}
