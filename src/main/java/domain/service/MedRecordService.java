package domain.service;

import dao.Repositories.JDBC.JDBCMedRecordRepository;
import dao.model.MedRecord;
import domain.mappers.MedRecordDTOMapper;
import domain.model.MedRecordDTO;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;


public class MedRecordService {
    private final JDBCMedRecordRepository medRecordRepository;
    private final MedRecordDTOMapper medRecordDTOMapper;

    @Inject
    public MedRecordService(JDBCMedRecordRepository medRecordRepository, MedRecordDTOMapper medRecordDTOMapper) {
        this.medRecordRepository = medRecordRepository;
        this.medRecordDTOMapper = medRecordDTOMapper;
    }

    public List<MedRecordDTO> getAllByPatientId(int patientId) {
        List<MedRecordDTO> medRecordDTOS = new ArrayList<>();
        List<MedRecord> medRecords=medRecordRepository.getAllById(patientId);
        for (MedRecord medRecord : medRecords) {
            MedRecordDTO medRecordDTO= medRecordDTOMapper.entityToDto(medRecord);
            medRecordDTOS.add(medRecordDTO);
        }
        return medRecordDTOS;
    }

    public int addMedRecord(MedRecordDTO medRecordDTO) {
        return medRecordRepository.addMedRecord(medRecordDTOMapper.dtoToEntity(medRecordDTO));
    }

    public void updateMedRecord(MedRecordDTO medRecordDTO) {
        medRecordRepository.updateMedRecord(medRecordDTOMapper.dtoToEntity(medRecordDTO));
    }

    public void deleteMedRecord(int idDelete){
        medRecordRepository.deleteMedRecord(idDelete);
    }
}
