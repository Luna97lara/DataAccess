package ui;

import domain.model.DoctorDTO;
import domain.service.DoctorService;

import java.util.List;

public class DoctorUI {
    private final DoctorService doctorService;
    public DoctorUI(DoctorService doctorService){
        this.doctorService = doctorService;
    }

    public List<DoctorDTO> getAll(){
        return doctorService.getDoctors();
    }
}
