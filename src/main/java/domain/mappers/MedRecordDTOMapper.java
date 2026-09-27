package domain.mappers;

import dao.model.MedRecord;
import domain.model.MedRecordDTO;

import java.time.LocalDate;

public class MedRecordDTOMapper {
    public MedRecord dtoToEntity(MedRecordDTO medRecordDTO) {
        LocalDate date = null;
        if (medRecordDTO.getDate() != null) {
            date = LocalDate.parse(medRecordDTO.getDate());
        }
        return new MedRecord(medRecordDTO.getId(), medRecordDTO.getIdPatient(), medRecordDTO.getIdDoctor(), medRecordDTO.getDescription(), date);
    }

    public MedRecordDTO entityToDto(MedRecord medRecord) {
        String date = null;
        if (medRecord.getAdmission_date() != null) {
            date = medRecord.getAdmission_date().toString();
        }
        return new MedRecordDTO(medRecord.getId(), medRecord.getDiagnosis(), date, medRecord.getPatient_id(), medRecord.getDoctor_id(), null);
    }
}
