package dao.Repositories.Interfaces;

import dao.model.Doctor;

import java.util.List;

public interface DoctorRepository {
    List<Doctor> getAll();
    public int addDoctor(Doctor doctor);
    public void updateDoctor(Doctor doctor);
    public void deleteDoctor(int id, boolean confirm);


}
