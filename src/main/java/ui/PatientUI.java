package ui;

import commons.Constants;
import dao.model.Patient;
import domain.service.PatientService;
import domain.model.CredentialDTO;
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
        List<Patient> patients=patientService.getPatients();
        //Map it to List <PatientDTO>
        return patientsDTO;
    }
    public int addPatient(PatientDTO patient){return patientService.addPatient(patient);};
    public void updatePatient(PatientDTO patient){patientService.updatePatient(patient);}
    public void deletePatient(int id){patientService.deletePatient(id);}
    public boolean login(CredentialDTO credential){return patientService.login(credential);}

    public void menu(){
        int option = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            IO.println(Constants.OPTIONS_MENU);
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    IO.println(getPatients());
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    patientService.deletePatients(insertId());

                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    IO.println("See you later!");
                default:
                    IO.println("Invalid option. Please, try again");
            }
        } while (option != 10);
    }



    public int insertId(){
        Scanner sc=new Scanner(System.in);
        IO.println("Insert the id");
        return sc.nextInt();
    }

}
