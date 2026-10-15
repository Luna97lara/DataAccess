package domain.mappers;

import dao.model.Credential;
import dao.model.Patient;
import domain.model.PatientDTO;

public class PatientDTOMapper {
    public Patient dtoToEntity(PatientDTO patientDTO){
        return new Patient(patientDTO.getId(), patientDTO.getName(), patientDTO.getBirthDate(), patientDTO.getPhone(), new Credential(patientDTO.getUserName(), patientDTO.getPassword()));
    }


    public PatientDTO entityDTO(Patient patient1){
        return new PatientDTO(patient1.getName(), patient1.getBirthDate(), patient1.getPhone(), 0, null, null);
    }
}
