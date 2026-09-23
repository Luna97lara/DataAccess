package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MedRecordDTO {
    private int id;
    private String description;
    private String date;
    private int idPatient;
    private int idDoctor;
    private List<String> medications;
}
