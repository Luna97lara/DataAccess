package dao.xml.model;

import dao.xml.adapter.LocalDateAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@XmlAccessorType(XmlAccessType.FIELD)
public class PatientElement {
    @XmlAttribute
    private int id;

    @XmlElement
    private String name;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dob;

    @XmlElement
    private String phone;

    @XmlElementWrapper(name = "medicalrecords")
    @XmlElement(name="medicalrecord")
    private List<MedRecordElement> medicalrecords;

    @XmlElementWrapper(name="appointments")
    @XmlElement(name="appointment")
    private List<AppointmentElement> appointments;

    @XmlElementWrapper(name = "payments")
    @XmlElement(name="payment")
    private List<PaymentElement> payments;
}
