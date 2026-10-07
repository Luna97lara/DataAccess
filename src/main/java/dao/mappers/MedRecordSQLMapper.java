package dao.mappers;

import dao.model.MedRecord;
import domain.model.MedRecordDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MedRecordSQLMapper {
    public MedRecord mapMedRecord(ResultSet rs) throws SQLException {
        MedRecord medRecord = new MedRecord();
        medRecord.setId(rs.getInt("record_id"));
        medRecord.setPatient_id(rs.getInt("patient_id"));
        medRecord.setDoctor_id(rs.getInt("doctor_id"));
        medRecord.setDiagnosis(rs.getString("diagnosis"));
        medRecord.setAdmission_date(rs.getDate("admission_date").toLocalDate());
        return medRecord;
    }
}
