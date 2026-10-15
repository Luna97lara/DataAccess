package ui;

import commons.Constants;
import domain.model.CredentialDTO;
import domain.model.MedRecordDTO;
import domain.model.PatientDTO;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    static void main() {
        try {
            SeContainerInitializer initializer = SeContainerInitializer.newInstance();
            final SeContainer container = initializer.initialize();

            PatientUI patientUI = container.select(PatientUI.class).get();
            DoctorUI doctorUI = container.select(DoctorUI.class).get();
            MedRecordUI medRecordUI = container.select(MedRecordUI.class).get();
            CredentialUI credentialUI = container.select(CredentialUI.class).get();

            Scanner sc = new Scanner(System.in);
            IO.println(Constants.LOGIN);
            String username = sc.nextLine();
            String password = sc.nextLine();
            if (credentialUI.login(new CredentialDTO(username, password))) {
                IO.println(Constants.CONNECTED);
                Scanner scanner = new Scanner(System.in);
                int option;
                do {
                    IO.println(Constants.OPTIONS_MENU);
                    option = scanner.nextInt();
                    switch (option) {
                        case 1:
                            IO.println(Constants.LIST_OF_PATIENTS + patientUI.getPatients());
                            break;
                        case 2:
                            patientUI.addPatient(new PatientDTO(Constants.DANI, LocalDate.parse(Constants.DATEDANI), Constants.PHONEDANI, 2, Constants.USERDANI, Constants.PSWDANI));
                            break;
                        case 3:
                            patientUI.updatePatient(new PatientDTO(Constants.MIKE, LocalDate.parse(Constants.DATEMIKE), Constants.PHONEMIKE, 0, Constants.USERMIKE, Constants.PSWMIKE));
                            break;
                        case 4:
                            IO.println(Constants.INSERT_ID);
                            patientUI.deletePatients(scanner.nextInt());
                            break;
                        case 5:
                            IO.println(doctorUI.getAll());
                            break;
                        case 6:
                            IO.println(Constants.INSERT_ID);
                            int id = scanner.nextInt();
                            IO.println(medRecordUI.getMedRecords(id));
                            break;
                        case 7:
                            medRecordUI.addMedRecord(new MedRecordDTO(3, 1, "Feo", LocalDate.parse("2005-07-07")));
                            break;
                        case 8:
                            medRecordUI.updateMedRecord(new MedRecordDTO(1, "Feo actualizable", LocalDate.parse("2005-07-07"), 2, 1, null));
                            break;
                        case 9:
                            IO.println(Constants.INSERT_ID);
                            int idMed = scanner.nextInt();
                            medRecordUI.deleteMedRecord(idMed);
                            break;
                        case 10:
                            IO.println(Constants.CLOSINGCONECTION);
                            IO.println(Constants.EXITMENU);
                        default:
                            IO.println(Constants.ERROR);
                    }
                } while (option != 10);
            } else {
                IO.println(Constants.LOGINERROR);
            }
        } catch (Error e) {
            IO.print(Constants.CRITICAL_ERROR + e.getMessage());
            System.exit(1);
        }
    }
}
