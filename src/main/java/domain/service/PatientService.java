package domain.service;

import commons.Configuration;
import commons.Constants;
import dao.Repositories.Interfaces.AppointmentRepository;
import dao.Repositories.Interfaces.MedRecordRepository;
import dao.Repositories.Interfaces.PatientRepository;
import dao.Repositories.Interfaces.PaymentRepository;
import dao.Repositories.JDBC.JDBCPatientRepository;
import dao.model.Appointment;
import dao.model.MedRecord;
import dao.model.Patient;
import dao.model.Payment;
import dao.xml.mappers.PatientXMLMapper;
import dao.xml.model.PatientElement;
import dao.xml.model.PatientsElement;
import dao.xml.utils.XMLJAXBFunction;
import domain.exceptions.DataBaseError;
import domain.mappers.PatientDTOMapper;
import domain.model.PatientDTO;
import jakarta.inject.Inject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientDTOMapper patientMapper;
    private final XMLJAXBFunction xmlJAXBFunction;
    private final PatientXMLMapper patientXMLMapper;
    private final MedRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;

    private Path getXmlPath() {
        return Paths.get(Configuration.getInstance().getProperty("xmlPatientsPath"));
    }

    @Inject
    public PatientService(PatientRepository patientRepository, PatientDTOMapper patientMapper, XMLJAXBFunction xmlJAXBFunction, PatientXMLMapper patientXMLMapper, MedRecordRepository medicalRecordRepository, AppointmentRepository appointmentRepository, PaymentRepository paymentRepository){
        this.patientRepository = patientRepository;
        this.patientMapper=patientMapper;
        this.xmlJAXBFunction = xmlJAXBFunction;
        this.patientXMLMapper = patientXMLMapper;
        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<PatientDTO> getPatients(){
        List<Patient> patients = patientRepository.getAll();
        List<PatientDTO> patientDTOS = new ArrayList<>();
        for (Patient patient : patients){
            PatientDTO patientDTO = patientMapper.entityDTO(patient);
            patientDTOS.add(patientDTO);
        }
        return patientDTOS;
    }

    public int addPatient(PatientDTO patientDTO){
        return patientRepository.add(patientMapper.dtoToEntity(patientDTO));
    }

    public void updatePatient(PatientDTO patient) {
        patientRepository.update(patientMapper.dtoToEntity(patient));
    }

    public void deletePatient(int idDelete, boolean confirmation) {
        Patient patient = patientRepository.findById(idDelete);
        List<MedRecord> medicalRecords = medicalRecordRepository.getAllById(idDelete);
        List<Appointment> appointments = appointmentRepository.getAllById(idDelete);
        List<Payment> payments = paymentRepository.getAllById(idDelete);
        ;

        if (confirmation || medicalRecords.isEmpty()) {
            PatientElement patientBackUp = patientXMLMapper.toXML(patient, medicalRecords, appointments, payments);
            xmlJAXBFunction.createFile(patientBackUp);
        }
        patientRepository.delete(idDelete, confirmation);
    }

    public PatientElement getPatientFromBackup(int patientId) {
        try {
            Path xmlFile = getXmlPath();
            if (!Files.exists(xmlFile)) {
                throw new DataBaseError(Constants.CRITICAL_ERROR);
            }

            JAXBContext context = JAXBContext.newInstance(PatientElement.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            PatientsElement patientsList = (PatientsElement) unmarshaller.unmarshal(Files.newInputStream(xmlFile));
            return patientsList.getPatients().stream()
                    .filter(p -> p.getId() == patientId).findFirst().orElseThrow(() -> new DataBaseError(Constants.MISSING_USER));
        } catch (DataBaseError e) {
            Logger.getLogger(XMLJAXBFunction.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (JAXBException | IOException e) {
            Logger.getLogger(XMLJAXBFunction.class.getName()).log(Level.SEVERE, "Error while restoring the patient from the XML file", e);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }

    public void restore(int patientId) {
        PatientElement patientRestored = xmlJAXBFunction.getPatient(patientId);
        Patient patient = patientXMLMapper.toPatient(patientRestored);
        List<MedRecord> medicalRecords = patientXMLMapper.toMedicalRecords(patientRestored);
        List<Appointment> appointments = patientXMLMapper.toAppointments(patientRestored);
        List<Payment> payments = patientXMLMapper.toPayments(patientRestored);

        patientRepository.restore(patient);
        medicalRecords.forEach(medicalRecordRepository::restore);
        appointments.forEach(appointmentRepository::restore);
        payments.forEach(paymentRepository::restore);

        xmlJAXBFunction.removePatientFromBackup(patientId);

    }

    public List<PatientElement> getRestorable() {
        return xmlJAXBFunction.getPatients();
    }
}
