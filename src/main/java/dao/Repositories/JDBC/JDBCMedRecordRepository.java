package dao.Repositories.JDBC;

import dao.Repositories.Interfaces.MedRecordRepository;
import dao.model.MedRecord;

import java.util.ArrayList;
import java.util.List;

public class JDBCMedRecordRepository implements MedRecordRepository {
    private final List<MedRecord> medRecords=new ArrayList();

    @Override
    public List<MedRecord> getAllById(int patientId){
        return medRecords;
    }

    @Override
    public int addMedRecord (MedRecord medRecord){
        medRecords.add(medRecord);
        return medRecord.getId();
    }

    @Override
    public void updateMedRecord(MedRecord medRecord){
        medRecords.stream().filter(element->element.getId()==medRecord.getId()).findFirst().ifPresent(record->{

        });
    }
    @Override
    public void deleteMedRecord(int idDelete){
        medRecords.removeIf(medRecord ->  medRecord.getId()==idDelete);
    }
}
