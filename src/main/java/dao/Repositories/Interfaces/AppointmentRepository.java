package dao.Repositories.Interfaces;



import dao.model.Appointment;

import java.util.List;

public interface AppointmentRepository {

    List<Appointment> getAllById(int id);

    void restore(Appointment appointment);
}
