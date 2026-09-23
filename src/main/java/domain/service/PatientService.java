package domain.service;

import dao.Repositories.JDBCPatientRepository;
import dao.model.Patient;
import domain.mappers.PatientDTOMapper;
import domain.model.PatientDTO;
import jakarta.inject.Inject;

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
        PatientDTO patientDTO = patientMapper.entityDTO(patients);
        return patients;
    }

    public void deletePatients(int id){
        jdbcPatientRepository.delete(id);
    }


    public boolean login(String patient, String password) {
        return jdbcPatientRepository.login(patient, password);
    }

    public void updatePatient(PatientDTO patient) {
        jdbcPatientRepository.update(patients);
    }

    public int addPatient(PatientDTO patientDTO) {
        Patient patient=patientMapper.dtoToEntity(patientDTO);
        return jdbcPatientRepository.add(patient);
    }
}
