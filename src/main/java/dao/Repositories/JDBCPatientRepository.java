package dao.Repositories;

import dao.PatientRepository;
import dao.model.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCPatientRepository implements PatientRepository {
    public List<Patient> getAll() {
        List <Patient> patients = new ArrayList<>();
        patients.add(new Patient(1,"Nani", LocalDate.parse("2004-04-09"), "666666666"));
        patients.add(new Patient(2,"Miguelch", LocalDate.parse("1992-09-21"), "633336367"));
        return patients;
    }

    @Override
    public int add(Patient patient) {
        return 1;
    }

    @Override
    public void update(Patient patientDatabase) {

    }

    @Override
    public void delete(int idDelete) {

    }

    public boolean login(String patient, String password){
        return patient.equals("root")&&password.equals("quevedo2dam");
    }


}
