package ui;

import domain.service.PatientService;
import domain.model.PatientDTO;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Scanner;

public class PatientUI {
    private final PatientService patientService;

    @Inject
    public PatientUI(PatientService patientService){
        this.patientService=patientService;
    }
    public List<PatientDTO> getPatients(){
        return patientService.getPatients();
    }
    public int addPatient(PatientDTO patient){return patientService.addPatient(patient);};
    public void updatePatient(PatientDTO patient){patientService.updatePatient(patient);}
    public void deletePatients(int idDelete){patientService.deletePatients(idDelete);}


}
