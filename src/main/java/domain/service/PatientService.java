package domain.service;

import dao.Repositories.JDBC.JDBCPatientRepository;
import dao.model.Patient;
import domain.mappers.PatientDTOMapper;
import domain.model.PatientDTO;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private final JDBCPatientRepository jdbcPatientRepository;
    private final PatientDTOMapper patientMapper;
    @Inject
    public PatientService(JDBCPatientRepository jdbcPatientRepository, PatientDTOMapper patientMapper){
        this.jdbcPatientRepository = jdbcPatientRepository;
        this.patientMapper=patientMapper;
    }

    public List<PatientDTO> getPatients(){
        List<Patient> patients = jdbcPatientRepository.getAll();
        List<PatientDTO> patientDTOS = new ArrayList<>();
        for (Patient patient : patients){
            PatientDTO patientDTO = patientMapper.entityDTO(patient);
        }
        return patientDTOS;
    }

    public int addPatient(PatientDTO patientDTO){
        return jdbcPatientRepository.add(patientMapper.dtoToEntity(patientDTO));
    }

    public void deletePatients(int idDelete){
        jdbcPatientRepository.delete(idDelete);
    }


    public void updatePatient(PatientDTO patient) {
        jdbcPatientRepository.update(patientMapper.dtoToEntity(patient));
    }

}
