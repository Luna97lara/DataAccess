package ui;

import domain.model.MedRecordDTO;
import domain.service.MedRecordService;
import jakarta.inject.Inject;

import java.util.List;

public class MedRecordUI {
    private final MedRecordService medRecordService;

    @Inject
    public MedRecordUI(MedRecordService medRecordService){
        this.medRecordService = medRecordService;
    }

    public List<MedRecordDTO> getMedRecords(int patientId){
        return medRecordService.getAllByPatientId(patientId);
    }

    public int addMedRecord(MedRecordDTO medRecordDTO){
        return medRecordService.addMedRecord(medRecordDTO);
    }

    public void updateMedRecord(MedRecordDTO medRecordDTO){
        medRecordService.updateMedRecord(medRecordDTO);
    }

    public void deleteMedRecord(int idDelete){
        medRecordService.deleteMedRecord(idDelete);
    }




}
