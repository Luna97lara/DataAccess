package domain.mappers;

import dao.model.Doctor;
import domain.model.DoctorDTO;

public class DoctorDTOMapper {
    public Doctor dtoToEntity(DoctorDTO doctorDTO) {
        return new Doctor(doctorDTO.getId(), doctorDTO.getName(), null, null, null);
    }

    public DoctorDTO entityToDto(Doctor doctor1) {
        return new DoctorDTO(doctor1.getId(), doctor1.getName(), null, null);
    }
}
