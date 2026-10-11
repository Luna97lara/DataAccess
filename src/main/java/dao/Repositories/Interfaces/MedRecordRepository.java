package dao.Repositories.Interfaces;

import dao.model.MedRecord;
import dao.model.Patient;

import java.util.List;

public interface MedRecordRepository {
    List<MedRecord> getAllById(int patientId);
    int addMedRecord(MedRecord medRecord);
    void updateMedRecord(MedRecord medRecord);
    void deleteMedRecord(int idDelete, boolean confirm);
}
