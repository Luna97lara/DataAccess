package dao.Interfaces;

import dao.model.Doctor;

import java.util.List;

public interface DoctorRepository {
    List<Doctor> getAll();
}
