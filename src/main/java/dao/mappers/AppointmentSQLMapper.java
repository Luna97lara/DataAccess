package dao.mappers;


import dao.model.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentSQLMapper {
    public Appointment mapAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("appointment_id"));
        appointment.setPatient_id(rs.getInt("patient_id"));
        appointment.setDoctor_id(rs.getInt("doctor_id"));
        appointment.setAppointment_date(rs.getDate("appointment_date").toLocalDate());
        return appointment;
    }
}
