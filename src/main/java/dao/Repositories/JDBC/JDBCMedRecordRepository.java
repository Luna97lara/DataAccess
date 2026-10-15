package dao.Repositories.JDBC;

import com.mysql.cj.jdbc.exceptions.SQLError;
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
            PreparedStatement ps=connection.prepareStatement(SQLQuery.SELECT_MEDRECORD);
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
            try (PreparedStatement ps=connection.prepareStatement(SQLQuery.INSERT_MEDRECORD, Statement.RETURN_GENERATED_KEYS);
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
                        ps2.executeUpdate();
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
        return medRecordId=medRecord.getId();
    }

    @Override
    public void updateMedRecord(MedRecord medRecord){
        try(Connection con = dbconnection.getConnection()){
            try (PreparedStatement ps = con.prepareStatement(SQLQuery.INSERT_MEDRECORD);
                 PreparedStatement ps2= con.prepareStatement(SQLQuery.DELETE_MED_WHEN_RECORDID);
                 PreparedStatement ps3= con.prepareStatement(SQLQuery.INSERT_MEDICATION)) {
                con.setAutoCommit(false);
                ps.setInt(1, medRecord.getPatient_id());
                ps.setInt(2, medRecord.getDoctor_id());
                ps.setString(3, medRecord.getDiagnosis());
                ps.setDate(4, Date.valueOf(medRecord.getAdmission_date()));
                int affectedRows= ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (affectedRows>0){
                    ps2.setString(1, medRecord.getMedications().get(rs.getInt(2)));
                    for (int i = 0; i < medRecord.getMedications().size(); i++) {
                        if (rs.next()) {
                            int medRecordId = rs.getInt(1);
                            ps2.setInt(1, medRecordId);
                            ps2.setString(2, medRecord.getMedications().get(i));
                            con.commit();
                        }
                    }
                } else {
                    throw new IdMissMatchingError(Constants.IDMISSMATCHING);
                }
            } catch (SQLException ex){
                Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, ex);
                con.rollback();
                throw new DataBaseError(Constants.DATABASE_ERROR);
            }

        } catch (SQLException e) {
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }


    @Override
    public void deleteMedRecord(int idDelete, boolean confirm){
        try (Connection con = dbconnection.getConnection()) {
            con.setAutoCommit(false);
            if(confirm){
                try (PreparedStatement ps = con.prepareStatement(SQLQuery.DELETE_MEDICATION);
                     ){
                    ps.setInt(1, idDelete);
                    ps.executeUpdate();
                } catch (SQLException e){
                    con.rollback();
                    throw new DataBaseError(Constants.DATABASE_ERROR);
                }
            }
            try (PreparedStatement ps = con.prepareStatement(SQLQuery.DELETE_MEDRECORD, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idDelete);
                int rowsAffected=ps.executeUpdate();
                con.commit();
                if (rowsAffected == 0) {
                    throw new IdMissMatchingError(Constants.IDMISSMATCHING);
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                con.rollback();
                Logger.getLogger(JDBCMedRecordRepository.class.getName()).log(Level.SEVERE, null, ex);
                throw new ForeignKeyERROR(Constants.FK_ERROR);
            } finally{
                con.setAutoCommit(true);
            }
        }catch (SQLException e){
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }

    }
}
