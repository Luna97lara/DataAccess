package ui;

import commons.Constants;
import domain.exceptions.AppError;
import domain.exceptions.DataBaseError;
import domain.exceptions.ForeignKeyERROR;
import domain.model.MedRecordDTO;
import domain.service.MedRecordService;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedRecordUI {
    private final MedRecordService medRecordService;
    private final Scanner scanner = new Scanner(System.in);

    @Inject
    public MedRecordUI(MedRecordService medRecordService){this.medRecordService = medRecordService;}

    public List<MedRecordDTO> getMedRecords(int patientId){
        List<MedRecordDTO> list = new ArrayList<>();
        try {
            list = medRecordService.getAllByPatientId(patientId);
        } catch (DataBaseError e){
            Logger.getLogger(MedRecordUI.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(MedRecordUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR+e.getMessage());
        }
        return list;
    }

    public int addMedRecord(MedRecordDTO medRecordDTO){
        int id = 0;
        try {
            id = medRecordService.addMedRecord(medRecordDTO);
        } catch (DataBaseError e){
            Logger.getLogger(MedRecordUI.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(MedRecordUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR+e.getMessage());
        }
        return  id;
    }

    public void updateMedRecord(MedRecordDTO medRecordDTO){
        try {
            medRecordService.updateMedRecord(medRecordDTO);
        } catch (DataBaseError e){
            Logger.getLogger(MedRecordUI.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(MedRecordUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR+e.getMessage());
        }
    }

    public void deleteMedRecord(int idDelete){
        try {
            medRecordService.deleteMedRecord(idDelete, false);
        } catch (ForeignKeyERROR f) {
            IO.println(Constants.MEDRECORD_WITH_MEDICATIONS);
            boolean confirm = scanner.nextLine().charAt(0) == 'Y';
            medRecordService.deleteMedRecord(idDelete, confirm);
        } catch (DataBaseError e){
            Logger.getLogger(MedRecordUI.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e){
            Logger.getLogger(MedRecordUI.class.getName()).log(Level.SEVERE, null, e);
            throw new AppError(Constants.CRITICAL_ERROR +e.getMessage());
        }
    }




}
