package dao.Repositories.JDBC;

import commons.Constants;
import dao.Repositories.Interfaces.PatientRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.PatientSQLMapper;
import dao.model.Patient;
import domain.exceptions.DataBaseError;
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
        try (Connection con = dbConnection.getConnection();
             PreparedStatement ps=con.prepareStatement(SQLQuery.INSERT_PATIENT)){
            ps.setString(1, patient.getName());
            ps.setDate(2, Date.valueOf(patient.getBirthDate()));
            ps.setString(3, patient.getPhone());
            ps.executeUpdate();
        } catch (SQLException ex){
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return id;
    }

    @Override
    public void update(Patient patient) {
        try(Connection con = dbConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(SQLQuery.UPDATE_PATIENT)) {
           ps.setString(1, patient.getName());
           ps.setDate(2, Date.valueOf(patient.getBirthDate()));
           ps.setString(3, patient.getPhone());
           ps.executeUpdate();
        } catch (SQLException ex){
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }

    }

    @Override
    public void delete(int idDelete) {
        try(Connection con = dbConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(SQLQuery.DELETE_PATIENT)) {
            ps.setInt(1, idDelete);
            ps.executeUpdate();
        }catch (SQLException ex){
            Logger.getLogger(JDBCPatientRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }



}
