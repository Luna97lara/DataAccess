package dao.Repositories;

import dao.Interfaces.DoctorRepository;
import dao.model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class JDBCDoctorRepository implements DoctorRepository {
    private List<Doctor> doctors=new ArrayList();

    @Override
    public List<Doctor> getAll() {
        return doctors;
    }
}
