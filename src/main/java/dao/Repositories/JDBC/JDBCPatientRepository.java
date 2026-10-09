package dao.Repositories.JDBC;

import commons.Constants;
import dao.Repositories.Interfaces.PatientRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.PatientSQLMapper;
import dao.model.Patient;
import domain.exceptions.DataBaseError;
import domain.exceptions.DuplicatedUsernameError;
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
        List<Patient> patients=new ArrayList();
        try (Connection con = dbConnection.getConnection();
             Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(SQLQuery.SELECT_FROM_PATIENTS);
            patients=patientSQLMapper.mapPatient(rs);
        }catch (SQLException ex){
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(ex.getMessage());
        }
        return patients;
    }

    @Override
    public int add(Patient patient) {
        int id=0;
        try (Connection con = dbConnection.getConnection()){
            try {
                PreparedStatement ps=con.prepareStatement(SQLQuery.INSERT_PATIENT, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement ps2=con.prepareStatement(SQLQuery.INSERT_CREDENTIALS);{
                    con.setAutoCommit(false);
                    ps.setString(1, patient.getName());
                    ps.setDate(2, Date.valueOf(patient.getBirthDate()));
                    ps.setString(3, patient.getPhone());
                    ps.executeUpdate();
                    int affectedRows =ps2.executeUpdate();
                    if (affectedRows>0){
                        ResultSet rs=ps2.getGeneratedKeys();
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
        return id;
    }


//Transaction in update and delete

    @Override
    public void update(Patient patient) {
        try(Connection con = dbConnection.getConnection()){
            try {
                PreparedStatement ps = con.prepareStatement(SQLQuery.UPDATE_PATIENT);
                PreparedStatement ps2 = con.prepareStatement(SQLQuery.UPDATE_CREDENTIALS);
                {
                    con.setAutoCommit(false);
                    ps.setString(1, patient.getName());
                    ps.setDate(2, Date.valueOf(patient.getBirthDate()));
                    ps.setString(3, patient.getPhone());
                    ps.executeUpdate();
                    int affectedRows =ps2.executeUpdate();
                    if (affectedRows>0){
                        ResultSet rs=ps2.getGeneratedKeys();
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
    public void delete(int idDelete) {
        try(Connection con = dbConnection.getConnection()){
            try {
                PreparedStatement ps = con.prepareStatement("");
                PreparedStatement ps2 = con.prepareStatement("");
                PreparedStatement ps3 = con.prepareStatement("");
                PreparedStatement ps4 = con.prepareStatement("");{
                    con.setAutoCommit(false);
                    ps.setInt(1, idDelete);
                    ps.executeUpdate();
                    con.commit();
                }
            }catch (SQLException e){
                Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, e);
                try{
                    con.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException();
                }
            } finally {
                con.setAutoCommit(true);
        }

        }catch (SQLException ex){
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }



}
