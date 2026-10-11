package ui;

import commons.Constants;
import domain.exceptions.AppError;
import domain.exceptions.DataBaseError;
import domain.exceptions.DuplicatedUsernameError;
import domain.exceptions.ForeignKeyERROR;
import domain.service.PatientService;
import domain.model.PatientDTO;
import jakarta.ejb.DuplicateKeyException;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientUI {
    private final PatientService patientService;
    private final Scanner scanner = new Scanner(System.in);

    @Inject
    public PatientUI(PatientService patientService){
        this.patientService=patientService;
    }
    public List<PatientDTO> getPatients() {
        List<PatientDTO> patients =  new ArrayList<>();
        try {
            patients = patientService.getPatients();
        } catch (DataBaseError ex) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception e){
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR +e.getMessage());
        }
        return patients;
    }
    public int addPatient(PatientDTO patient) {
        int id = 0;
        try {
            id = patientService.addPatient(patient);
        }catch (DuplicatedUsernameError ex){
            IO.println(Constants.DUPLICATED_USERNAME);
            String answer=scanner.nextLine().trim();
            patient.setUserName(answer);
            IO.println(patientService.addPatient(patient));
        } catch (DataBaseError ex) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e){
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR +e.getMessage());
        }
        return id;
    }
    public void updatePatient(PatientDTO patient) {
        try {
            patientService.updatePatient(patient);
        } catch (DataBaseError error) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, error);
        } catch (Exception e){
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR +e.getMessage());
        }
    }
    public void deletePatients(int idDelete){
        try {
            patientService.deletePatients(idDelete, false);
        } catch (ForeignKeyERROR f){
            IO.println(Constants.PATIENT_WITH_MEDRECORDS);
            boolean confirm = scanner.nextLine().charAt(0) == 'Y';
            patientService.deletePatients(idDelete, confirm);
        } catch (DataBaseError error) {
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, error);
        }  catch (Exception e){
            Logger.getLogger(PatientUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR +e.getMessage());
        }
    }


}
