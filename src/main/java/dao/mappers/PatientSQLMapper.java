package dao.mappers;

import dao.model.Credential;
import dao.model.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientSQLMapper {
    public List<Patient> mapPatient(ResultSet rs) {
        List<Patient> patients = new ArrayList<>();
        try {
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("patient_id"));
                patient.setName(rs.getString("name"));
                patient.setBirthDate(rs.getDate("date_of_birth").toLocalDate());
                patient.setPhone(rs.getString("phone"));
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }


}
