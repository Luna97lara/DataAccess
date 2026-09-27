package domain.mappers;

import dao.model.Doctor;
import domain.model.DoctorDTO;

public class DoctorDTOMapper {
    public Doctor dtoToEntity(DoctorDTO doctorDTO) {
        return new Doctor(doctorDTO.getId(), doctorDTO.getName(), null, null);
    }

    public DoctorDTO entityToDto(Doctor doctor) {
        return new DoctorDTO(doctor.getId(), doctor.getName());
    }
}
