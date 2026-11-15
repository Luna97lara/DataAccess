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
public class MedRecordElement {

    @XmlAttribute
    private String id;

    @XmlElement
    private int doctorId;

    @XmlElement
    private String diagnosis;

    @XmlElement
    @XmlJavaTypeAdapter(value= LocalDateAdapter.class)
    private LocalDate admissionDate;

    @XmlElementWrapper(name = "medications")
    @XmlElement(name = "medication")
    private List<String> medications;

}
