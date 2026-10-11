package dao.mappers;

import dao.model.Doctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorSQLMapper {
    public List<Doctor> mapDoctor(ResultSet rs){
        List<Doctor> doctors=new ArrayList<Doctor>();
        try{
            while (rs.next()){
                Doctor doctor=new Doctor();
                doctor.setId(rs.getInt("doctor_id"));
                doctor.setName(rs.getString("name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setPhone(rs.getString("phone"));
                doctors.add(doctor);
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return doctors;
    }
}
