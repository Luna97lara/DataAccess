package dao.Repositories.JDBC;

import commons.Constants;
import dao.Repositories.Interfaces.PatientRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.PatientSQLMapper;
import dao.model.Patient;
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

public class JDBCPatientRepository implements PatientRepository {
    private List<Patient> patients=new ArrayList();
    private final DBConnection dbConnection;
    private final PatientSQLMapper patientSQLMapper;


    @Inject
    public JDBCPatientRepository(DBConnection dbConnection, PatientSQLMapper patientSQLMapper) {
        this.dbConnection = dbConnection;
        this.patientSQLMapper = patientSQLMapper;
    }

    public List<Patient> getAll() {
        try (Connection con = dbConnection.getConnection();
             Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(SQLQuery.SELECT_FROM_PATIENTS);
            patients=patientSQLMapper.mapPatients(rs);
        }catch (SQLException ex){
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return patients;
    }

    @Override
    public int add(Patient patient) {
        int id=0;
        try (Connection con = dbConnection.getConnection()){
            try (PreparedStatement ps=con.prepareStatement(SQLQuery.INSERT_PATIENT, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement ps2=con.prepareStatement(SQLQuery.INSERT_CREDENTIALS)) {
                    con.setAutoCommit(false);
                    ps.setString(1, patient.getName());
                    ps.setDate(2, Date.valueOf(patient.getBirthDate()));
                    ps.setString(3, patient.getPhone());
                    int affectedRows = ps.executeUpdate();

                    if (affectedRows>0){
                        ResultSet rs=ps.getGeneratedKeys();
                        if(rs.next()){
                            patient.setId(rs.getInt(1));
                            ps2.setString(1, patient.getCredentials().getUsername());
                            ps2.setString(2, patient.getCredentials().getPassword());
                            ps2.setInt(3, patient.getId());
                            ps2.executeUpdate();
                            con.commit();
                        }
                    }
            } catch (SQLIntegrityConstraintViolationException ex) {
                Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
                con.rollback();
                throw new DuplicatedUsernameError(Constants.DUPLICATED_USERNAME);
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(e);
            } finally {
                con.setAutoCommit(true);
            }

            } catch (SQLException ex){
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return id=patient.getId();
    }


    @Override
    public void update(Patient patient) {
        try(Connection con = dbConnection.getConnection()){
            try {
                PreparedStatement ps = con.prepareStatement(SQLQuery.UPDATE_PATIENT, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement ps2 = con.prepareStatement(SQLQuery.UPDATE_CREDENTIALS);
                {
                    con.setAutoCommit(false);
                    ps.setString(1, patient.getName());
                    ps.setDate(2, Date.valueOf(patient.getBirthDate()));
                    ps.setString(3, patient.getPhone());
                    ps.setInt(4, patient.getId());
                    ps.executeUpdate();
                    int affectedRows =ps.executeUpdate();
                    if (affectedRows>0){
                        ResultSet rs=ps.getGeneratedKeys();
                        if(rs.next()){
                            patient.setId(rs.getInt(1));
                            ps2.setString(1, patient.getCredentials().getUsername());
                            ps2.setString(2, patient.getCredentials().getPassword());
                            ps2.setInt(3, patient.getId());
                            ps2.executeUpdate();
                            con.commit();
                        }
                    }
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
                con.rollback();
                throw new IdMissMatchingError(Constants.IDMISSMATCHING);
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex){
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }

    }

    @Override
    public void delete(int idDelete, boolean confirm) {
        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);
            if (confirm) {
                try (PreparedStatement ps = con.prepareStatement(SQLQuery.DELETE_MEDICATION);
                     PreparedStatement ps2 = con.prepareStatement(SQLQuery.DELETE_MEDRECORD);) {
                    ps.setInt(1, idDelete);
                    ps2.setInt(1, idDelete);
                    ps.executeUpdate();
                    ps2.executeUpdate();
                } catch (SQLException ex) {
                    con.rollback();
                    throw new DataBaseError(Constants.DATABASE_ERROR);
                }
            }
            try (PreparedStatement psPatient = con.prepareStatement(SQLQuery.DELETE_PATIENT, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psPayment = con.prepareStatement(SQLQuery.DELETE_PATIENTPAYMENTS);
                 PreparedStatement psAppointment = con.prepareStatement(SQLQuery.DELETE_APPOINTMENT);
                 PreparedStatement psCredential = con.prepareStatement(SQLQuery.DELETE_CREDENTIALS);
            ) {
                psPatient.setInt(1, idDelete);
                psPayment.setInt(1, idDelete);
                psAppointment.setInt(1, idDelete);
                psCredential.setInt(1, idDelete);
                int rowsAffected = psPatient.executeUpdate();
                psPayment.executeUpdate();
                psAppointment.executeUpdate();
                psCredential.executeUpdate();
                con.commit();
                if (rowsAffected == 0) {
                        throw new IdMissMatchingError(Constants.IDMISSMATCHING);
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                con.rollback();
                Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
                throw new ForeignKeyERROR(Constants.FK_ERROR);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new ForeignKeyERROR(Constants.FK_ERROR);
        } catch (SQLException | DataBaseError e) {
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DataBaseError(Constants.DATABASE_ERROR);

        }
    }

    @Override
    public Patient findById(int id) {
        Patient patient = new Patient();
        try (Connection c = dbConnection.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(SQLQuery.SELECT_patient_QUERY)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    patient = patientSQLMapper.mapPatient(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBCCredentialRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }

        if (patient == null) {
            throw new DataBaseError(Constants.MISSING_USER);
        }
        return patient;
    }

    @Override
    public void restore(Patient patient) {
        try (Connection c = dbConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SQLQuery.INSERT_PATIENT_WITH_EXISTING_ID, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, patient.getId());
            ps.setString(2, patient.getName());
            ps.setDate(3, Date.valueOf(patient.getBirthDate()));
            ps.setString(4, patient.getPhone());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DataBaseError(Constants.DATABASE_ERROR);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getMessage().contains("unique_name_dob")) {
                Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new DataBaseError(Constants.ERROR);
            }
        } catch (SQLException e) {

            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }

    }

}
