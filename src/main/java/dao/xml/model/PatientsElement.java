package dao.xml.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@XmlRootElement(name="patients")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientsElement {
    @XmlElement(name="patient")
    private List<PatientElement> patients;

    public void addPatient(PatientElement patient){
        patients.add(patient);
    }

}
