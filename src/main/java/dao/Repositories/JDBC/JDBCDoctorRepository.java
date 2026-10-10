package dao.Repositories.JDBC;

import commons.Constants;
import dao.Repositories.Interfaces.DoctorRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.DoctorSQLMapper;
import dao.model.Doctor;
import domain.exceptions.DataBaseError;
import domain.exceptions.DuplicatedUsernameError;
import domain.exceptions.ForeignKeyERROR;
import domain.exceptions.IdMissMatchingError;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCDoctorRepository implements DoctorRepository {
    private List<Doctor> doctors=new ArrayList();
    private final DBConnection dbConnection;
    private final DoctorSQLMapper doctorSQLMapper;

    @Inject
    public JDBCDoctorRepository(DBConnection dbConnection, DoctorSQLMapper doctorSQLMapper) {
        this.dbConnection = dbConnection;
        this.doctorSQLMapper = doctorSQLMapper;
    }

    @Override
    public List<Doctor> getAll() {
        List<Doctor> doctors=new ArrayList();
        try(Connection con = dbConnection.getConnection();
            Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(SQLQuery.SELECT_FROM_DOCTORS);
            doctors=doctorSQLMapper.mapDoctor(rs);

        }catch (SQLException ex){
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return doctors;
    }

    public int add(Doctor doctor){
        int id=0;
        try(Connection con = dbConnection.getConnection()) {
            try {
                PreparedStatement ps = con.prepareStatement(SQLQuery.INSERT_DOCTOR, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement ps2 = con.prepareStatement(SQLQuery.INSERT_CREDENTIALS);
                {
                    con.setAutoCommit(false);
                    ps.setString(1, doctor.getName());
                    ps.setString(2, doctor.getSpecialization());
                    ps.setString(3, doctor.getPhone());
                    ps.executeUpdate();
                    int affectedRows = ps2.executeUpdate();
                    if (affectedRows > 1) {
                        ResultSet rs = ps2.getGeneratedKeys();
                        if (rs.next()) {
                            doctor.setId(rs.getInt(1));
                            ps2.setString(1, doctor.getCredentials().getUsername());
                            ps2.setString(2, doctor.getCredentials().getPassword());
                            ps2.setInt(3, doctor.getId());
                            ps2.executeUpdate();
                            con.commit();
                        }
                    }
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
                con.rollback();
                throw new DuplicatedUsernameError(Constants.DUPLICATED_USERNAME);
            } catch (SQLException ex) {
                con.rollback();
                throw new RuntimeException(ex);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return id;
    }

    public void update(Doctor doctor){
        try (Connection con = dbConnection.getConnection()) {
            try {
                PreparedStatement ps = con.prepareStatement(SQLQuery.UPDATE_DOCTOR);
                PreparedStatement ps2 = con.prepareStatement(SQLQuery.UPDATE_CREDENTIALS);
                {
                    con.setAutoCommit(false);
                    ps.setString(1, doctor.getName());
                    ps.setString(2, doctor.getSpecialization());
                    ps.setString(3, doctor.getPhone());
                    ps.executeUpdate();
                    int affectedRows = ps2.executeUpdate();
                    if (affectedRows > 0) {
                        ResultSet rs = ps2.getGeneratedKeys();
                        if (rs.next()) {
                            doctor.setId(rs.getInt(1));
                            ps2.setString(1, doctor.getCredentials().getUsername());
                            ps2.setString(2, doctor.getCredentials().getPassword());
                            ps2.setInt(3, doctor.getId());
                            ps2.executeUpdate();
                            con.commit();
                        }
                    }
                }
            }catch (SQLIntegrityConstraintViolationException ex) {
                Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
                con.rollback();
                throw new IdMissMatchingError(Constants.IDMISSMATCHING);
            } catch (SQLException ex) {
                con.rollback();
                throw new RuntimeException(ex);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }

    public void delete(int id, boolean confirm){
        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);
            if (confirm) {
                try (PreparedStatement ps = con.prepareStatement(SQLQuery.DELETE_MEDICATION);
                     PreparedStatement ps2 = con.prepareStatement(SQLQuery.DELETE_MEDRECORD)) {
                    ps.setInt(1, id);
                    ps2.setInt(1, id);
                    ps.executeUpdate();
                    ps2.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException ex) {
                    con.rollback();
                    throw new ForeignKeyERROR(Constants.FK_ERROR);
                }
            }
            try (PreparedStatement psDoctor = con.prepareStatement(SQLQuery.DELETE_DOCTOR);
                 PreparedStatement psAppointment = con.prepareStatement(SQLQuery.DELETE_APPOINTMENT);
                 PreparedStatement psCredential = con.prepareStatement(SQLQuery.DELETE_CREDENTIALS)) {
                psDoctor.setInt(1, id);
                psAppointment.setInt(1, id);
                psCredential.setInt(1, id);
                int affectedRows = psDoctor.executeUpdate();
                psAppointment.setInt(1, id);
                psCredential.executeUpdate();
                con.commit();
                if (affectedRows == 0) {
                    throw new IdMissMatchingError(Constants.IDMISSMATCHING);
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                con.rollback();
                Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
                throw new ForeignKeyERROR(Constants.FK_ERROR);
            } finally {
                con.setAutoCommit(true);
            }
        }catch (SQLIntegrityConstraintViolationException e){
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new ForeignKeyERROR(Constants.FK_ERROR);
        } catch (SQLException ex){
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }

}
