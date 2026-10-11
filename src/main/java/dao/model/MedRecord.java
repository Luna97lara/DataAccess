package dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedRecord {
    private int id;
    private int patient_id;
    private int doctor_id;
    private String diagnosis;
    private LocalDate admission_date;
    private List<String>medications;
}
