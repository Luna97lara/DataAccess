package commons;

public class Constants {
    public static final String OPTIONS_MENU = """
            Login successful! Welcome root!
            1. Get all Patients
            2. Add Patient
            3. Update Patient
            4. Delete Patient
            5. Get all Doctors
            6. Get all MedRecords by patient
            7. Add MedRecord
            8. Update MedRecord
            9. Delete MedRecord
            10. Exit
            Enter your choice:
            """;
    public static final String LIST_OF_PATIENTS = "List of patients: ";
    public static final String DANI = "Dani";
    public static final String DATEDANI = "2004-04-09";
    public static final String PHONEDANI = "666777898";
    public static final String USERDANI = "danul0";
    public static final String PSWDANI = "quevedo2dam";
    public static final String PSWMIKE = "ilovejustintimberlake";
    public static final String USERMIKE = "mikerincones25";
    public static final String PHONEMIKE = "696969696";
    public static final String DATEMIKE = "1993-09-21";
    public static final String MIKE = "Mike";
    public static final String INSERT_ID = "Insert ID";
    public static final String EXITMENU = "See you later!";
    public static final String ERROR = "Invalid option. Please, try again";
    public static final String connected = "Connected to the database";
    public static final String closingconnection = "Releasing all open resources...";
    public static final String DATABASE_ERROR="Error in the Database";
    public static final String CRITICAL_ERROR = "Critical error with getAllPatients: ";
    public static final String LOGIN = "Insert your username and your password: ";
    public static final String LOGINERROR = "Login error";
    public static final String DUPLICATED_USERNAME = "Duplicated Username";
    public static final String IDMISSMATCHING = "The ID doesn't exist";
    public static final String FK_ERROR = "You can´t delete this patient: FK in another table";
}
