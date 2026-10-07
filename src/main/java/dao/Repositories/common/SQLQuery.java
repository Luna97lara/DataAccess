package dao.Repositories.common;

public class SQLQuery
{
    public static final String SELECT_FROM_PATIENTS = "select * from patients";
    public static final String SELECT_FROM_DOCTORS = "select * from doctors";
    public static final String INSERT_PATIENT = "Insert into patients (name, date_of_birth, phone) values (?, ?, ?)";
    public static final String CREDENTIALS = "Insert into user_login (username, password) values (?, ?)";
    public static final String UPDATE_PATIENT = "Update patients set name = ?, date_of_birth = ?, phone = ? where id = ?";
    public static final String DELETE_PATIENT = "Delete patients where id = ?";
    public static final String INSERT_DOCTOR = "Insert into doctors (name, specialization, phone) values (?, ?, ?)";
    public static final String UPDATE_DOCTOR = "Update doctors set name = ?, specialization = ?, phone = ? where id = ?";
    public static final String DELETE_DOCTOR = "Delete doctors where id = ?";
    public static final String CREDENTIAL_QUERY = "SELECT * FROM user_login WHERE username = ?";
    public static final String INSERT_MEDRECORD = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis, admission_date) VALUES (?, ?, ?, ?)";
}
