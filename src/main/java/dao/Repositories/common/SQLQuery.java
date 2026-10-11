package dao.Repositories.common;

public class SQLQuery
{
    public static final String SELECT_FROM_PATIENTS = "select * from patients";
    public static final String SELECT_FROM_DOCTORS = "select * from doctors";
    public static final String INSERT_PATIENT = "Insert into patients (name, date_of_birth, phone) values (?, ?, ?)";
    public static final String INSERT_CREDENTIALS = "Insert into user_login (username, password) values (?, ?)";
    public static final String UPDATE_PATIENT = "Update patients set name = ?, date_of_birth = ?, phone = ? where id = ?";
    public static final String DELETE_PATIENT = "Delete from patients where patient_id = ?";
    public static final String INSERT_DOCTOR = "Insert into doctors (name, specialization, phone) values (?, ?, ?)";
    public static final String UPDATE_DOCTOR = "Update doctors set name = ?, specialization = ?, phone = ? where id = ?";
    public static final String DELETE_DOCTOR = "Delete from doctors where doctor_id = ?";
    public static final String CREDENTIAL_QUERY = "SELECT * FROM user_login WHERE username = ?";
    public static final String INSERT_MEDRECORD = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis, admission_date) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_CREDENTIALS= "Update user_login set username = ?, password = ? where patient_id = ?";
    public static final String DELETE_MEDRECORD = "Delete from medical_records where patient_id = ?";
    public static final String DELETE_APPOINTMENT = "Delete from appointments where patient_id = ?";
    public static final String DELETE_PATIENTPAYMENTS = "Delete from patient_payments where patient_id = ?";
    public static final String DELETE_MEDICATION = "Delete from prescribed_medication where record_id in (select record_id from medical_records where patient_id = ?)";
    public static final String DELETE_CREDENTIALS= "Delete from user_login where patient_id = ?";
    public static final String INSERT_MEDICATION="Insert into prescribed_medication values (null, ?, ?, null)";
    public static final String DELETE_MED_WHEN_RECORDID = "Delete from prescribed_medication where record_id = ?";
    public static final String SELECT_MEDRECORD = "select * from medical_records where patient_id = ?";

}
