package ui;

import commons.Constants;
import dao.model.Doctor;
import domain.exceptions.AppError;
import domain.exceptions.DataBaseError;
import domain.exceptions.DuplicatedUsernameError;
import domain.exceptions.ForeignKeyERROR;
import domain.model.DoctorDTO;
import domain.service.DoctorService;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorUI {
    private final DoctorService doctorService;
    private final Scanner scanner = new Scanner(System.in);
    @Inject
    public DoctorUI(DoctorService doctorService){
        this.doctorService = doctorService;
    }
    public List<DoctorDTO> getAll(){
        return doctorService.getDoctors();
    }
    public int addDoctor(DoctorDTO doctor) {
        int id=0;
        try{
            id = doctorService.addDoctor(doctor);
        } catch (DuplicatedUsernameError ex){
            IO.println(Constants.DUPLICATED_USERNAME);
            String answer = scanner.nextLine().trim();
            doctor.setUserName(answer);
            IO.println(doctorService.addDoctor(doctor));
        }
        return id;
    }
    public void updateDoctor(DoctorDTO doctor) {
        try{
        doctorService.updateDoctor(doctor);
        } catch(DataBaseError e){
            Logger.getLogger(DoctorUI.class.getName()).log(Level.SEVERE, null, e);
        } catch(Exception e){
            Logger.getLogger(DoctorUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR +e.getMessage());
        }
    }
    public void deletePatient(int id){
        try{
            doctorService.deleteDoctor(id, false);
        } catch (ForeignKeyERROR f){
            IO.println(Constants.DOCTOR_WITH_MEDRECORDS);
            boolean confirm = scanner.nextLine().charAt(0)=='Y';
            doctorService.deleteDoctor(id, confirm);
        } catch (DataBaseError error) {
            Logger.getLogger(DoctorUI.class.getName()).log(Level.SEVERE, null, error);
        } catch(Exception e){
            Logger.getLogger(DoctorUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR+e.getMessage());
        }
    }

}
