package dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private int id;
    private int patient_id;
    private int doctor_id;
    private LocalDate appointment_date;
}
