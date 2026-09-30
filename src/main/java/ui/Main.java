package ui;

import commons.Constants;
import domain.model.MedRecordDTO;
import domain.model.PatientDTO;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    static void main() {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();

        PatientUI patientUI = container.select(PatientUI.class).get();
        DoctorUI doctorUI = container.select(DoctorUI.class).get();
        MedRecordUI medRecordUI = container.select(MedRecordUI.class).get();

            int option = 0;
            Scanner scanner = new Scanner(System.in);
            do {
                IO.println(Constants.OPTIONS_MENU);
                option = scanner.nextInt();
                switch (option) {
                    case 1:
                        IO.println(Constants.LIST_OF_PATIENTS +patientUI.getPatients());
                        break;
                    case 2:
                        IO.println(patientUI.addPatient(new PatientDTO(0, Constants.DANI, LocalDate.parse(Constants.DATEDANI), Constants.PHONEDANI, 2, Constants.USERDANI, Constants.PSWDANI)));
                        break;
                    case 3:
                        IO.println(patientUI.addPatient(new PatientDTO(25, Constants.MIKE, LocalDate.parse(Constants.DATEMIKE), Constants.PHONEMIKE, 0, Constants.USERMIKE, Constants.PSWMIKE)));
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
                        IO.println((medRecordUI.addMedRecord(new MedRecordDTO())));
                        break;
                    case 8:
                        medRecordUI.updateMedRecord(new MedRecordDTO());
                        break;
                    case 9:
                        IO.println(Constants.INSERT_ID);
                        int idMed = scanner.nextInt();
                        medRecordUI.deleteMedRecord(idMed);
                        break;
                    case 10:
                        IO.println(Constants.EXITMENU);
                    default:
                        IO.println(Constants.ERROR);
                }
            } while (option != 10);
        }

}
