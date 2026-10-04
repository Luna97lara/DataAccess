package dao.Repositories.JDBC;

import commons.Constants;
import dao.Repositories.Interfaces.DoctorRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.DoctorSQLMapper;
import dao.model.Doctor;
import domain.exceptions.DataBaseError;
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
        try(Connection con = dbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SQLQuery.INSERT_DOCTOR)){
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getPhone());
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return id;
    }

    public void update(Doctor doctor){
        try (Connection con = dbConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(SQLQuery.UPDATE_DOCTOR)) {
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getPhone());
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }

    public void delete(int id){
        try (Connection con = dbConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(SQLQuery.DELETE_DOCTOR)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }catch (SQLException ex){
            Logger.getLogger(JDBCDoctorRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }

}
