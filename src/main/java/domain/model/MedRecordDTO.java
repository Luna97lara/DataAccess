package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class MedRecordDTO {
    private int id;
    private String description;
    private LocalDate date;
    private int idPatient;
    private int idDoctor;
    private List<String> medications;

    public MedRecordDTO(int idPatient, int idDoctor, String description, LocalDate date) {
        this.description = description;
        this.date = date;
        this.idPatient = idPatient;
        this.idDoctor = idDoctor;
        this.medications = new ArrayList<>();
    }
}
