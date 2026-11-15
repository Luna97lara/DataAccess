package dao.Repositories.JDBC;

import commons.Constants;
import dao.Repositories.Interfaces.AppointmentRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.AppointmentSQLMapper;
import dao.model.Appointment;
import domain.exceptions.DataBaseError;
import jakarta.inject.Inject;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCAppointmentRepository implements AppointmentRepository {

    private final DBConnection db;
    private final AppointmentSQLMapper  appointmentMapper;

    @Inject
    public JDBCAppointmentRepository(DBConnection db, AppointmentSQLMapper appointmentMapper) {
        this.db = db;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public List<Appointment> getAllById(int id) {

        List<Appointment> appointments = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(SQLQuery.SELECT_APPOINTMENTS_BYPATIENTID)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    appointments.add(appointmentMapper.mapAppointment(rs));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(JDBCAppointmentRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return appointments;
    }

    @Override
    public void restore(Appointment appointment) {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(SQLQuery.RESTORE_APPOINTMENT)) {
            ps.setInt(1, appointment.getId());
            ps.setInt(2, appointment.getPatient_id());
            ps.setInt(3, appointment.getDoctor_id());
            ps.setDate(4, Date.valueOf(appointment.getAppointment_date()));
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

