package dao.Repositories.JDBC;

import dao.Repositories.Interfaces.MedRecordRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.MedRecordSQLMapper;
import dao.model.MedRecord;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCMedRecordRepository implements MedRecordRepository {
    private final DBConnection dbconnection;
    private final MedRecordSQLMapper medRecordSQLMapper;


    @Inject
    public JDBCMedRecordRepository(DBConnection dbconnection, MedRecordSQLMapper medRecordSQLMapper) {
        this.dbconnection = dbconnection;
        this.medRecordSQLMapper = medRecordSQLMapper;
    }

    @Override
    public List<MedRecord> getAllById(int patientId){
        List<MedRecord> medRecords = new ArrayList<>();
        try{
            Connection connection = dbconnection.getConnection();
            PreparedStatement ps=connection.prepareStatement("");
            ps.setInt(1, patientId);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                medRecords.add(medRecordSQLMapper.mapMedRecord(rs));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return medRecords;
    }

    @Override
    public int addMedRecord (MedRecord medRecord){
       int medRecordId = 0;
        try(Connection connection = dbconnection.getConnection();
            PreparedStatement ps=connection.prepareStatement(SQLQuery.INSERT_MEDRECORD)){
            ps.setInt(1, medRecord.getPatient_id());
            ps.setInt(2, medRecord.getDoctor_id());
            ps.setString(3, medRecord.getDiagnosis());
            ps.setDate(4, Date.valueOf(medRecord.getAdmission_date()));
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                medRecordId = rs.getInt(1);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return medRecordId;
    }

    @Override
    public void updateMedRecord(MedRecord medRecord){

        });
    }
    @Override
    public void deleteMedRecord(int idDelete){

    }
}
