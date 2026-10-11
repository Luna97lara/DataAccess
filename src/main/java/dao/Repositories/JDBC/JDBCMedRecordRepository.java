package dao.Repositories.JDBC;

import commons.Constants;
import dao.Repositories.Interfaces.MedRecordRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.MedRecordSQLMapper;
import dao.model.MedRecord;
import domain.exceptions.DataBaseError;
import domain.exceptions.ForeignKeyERROR;
import domain.exceptions.IdMissMatchingError;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try(Connection connection = dbconnection.getConnection()){
            try (PreparedStatement ps=connection.prepareStatement(SQLQuery.INSERT_MEDRECORD);
                 PreparedStatement ps2 = connection.prepareStatement(SQLQuery.INSERT_MEDICATION)) {
                connection.setAutoCommit(false);
                ps.setInt(1, medRecord.getPatient_id());
                ps.setInt(2, medRecord.getDoctor_id());
                ps.setString(3, medRecord.getDiagnosis());
                ps.setDate(4, Date.valueOf(medRecord.getAdmission_date()));
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                for (int i = 0; i < medRecord.getMedications().size(); i++) {
                    if (rs.next()) {
                        medRecordId = rs.getInt(1);
                        ps2.setInt(1, medRecordId);
                        ps2.setString(2, medRecord.getMedications().get(i));
                        connection.commit();
                    }
                }
            } catch (SQLException ex){
               Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, ex);
               connection.rollback();
                throw new DataBaseError(Constants.DATABASE_ERROR);
            }
        }catch (SQLException e){
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
        return medRecordId;
    }

    @Override
    public void updateMedRecord(MedRecord medRecord){

    }
    @Override
    public void deleteMedRecord(int idDelete){

    }
}
