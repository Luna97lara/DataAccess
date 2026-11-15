package dao.xml.mappers;


import dao.model.Appointment;
import dao.model.MedRecord;
import dao.model.Patient;
import dao.model.Payment;
import dao.xml.model.AppointmentElement;
import dao.xml.model.MedRecordElement;
import dao.xml.model.PatientElement;
import dao.xml.model.PaymentElement;

import java.util.ArrayList;
import java.util.List;

public class PatientXMLMapper {

    public PatientElement toXML(Patient p, List<MedRecord> medicalRecords, List<Appointment> appointments, List<Payment> payments) {
        PatientElement patientXML = new PatientElement();
        patientXML.setId(p.getId());
        patientXML.setName(p.getName());
        patientXML.setDob(p.getBirthDate());
        patientXML.setPhone(p.getPhone());

        if (medicalRecords != null) {
            patientXML.setMedicalrecords(toMedicalRecordsXML(medicalRecords));
        }
        if (appointments != null) {
            patientXML.setAppointments(toAppointmentXML(appointments));
        }
        if (payments != null) {
            patientXML.setPayments(toPaymentXML(payments));
        }
        return patientXML;
    }

    private List<MedRecordElement> toMedicalRecordsXML(List<MedRecord> medicalRecords) {
        List<MedRecordElement> medicalRecordXMLS = new ArrayList<>();
        for (MedRecord record : medicalRecords) {
            MedRecordElement medRecordXML = new MedRecordElement();
            medRecordXML.setId(String.valueOf(record.getId()));
            medRecordXML.setDoctorId(record.getDoctor_id());
            medRecordXML.setDiagnosis(record.getDiagnosis());
            medRecordXML.setAdmissionDate(record.getAdmission_date());
            medRecordXML.setMedications(record.getMedications() != null ? new ArrayList<>(record.getMedications()) : new ArrayList<>());
            medicalRecordXMLS.add(medRecordXML);
        }
        return medicalRecordXMLS;
    }

    private List<AppointmentElement> toAppointmentXML(List<Appointment> appointments) {
        List<AppointmentElement> appointmentXMLS = new ArrayList<>();
        for (Appointment a : appointments) {
            AppointmentElement appointmentXML = new AppointmentElement();
            appointmentXML.setId(a.getId());
            appointmentXML.setDoctorId(a.getDoctor_id());
            appointmentXML.setAppointmentDate(a.getAppointment_date());
            appointmentXMLS.add(appointmentXML);
        }
        return appointmentXMLS;
    }

    private List<PaymentElement> toPaymentXML(List<Payment> payments) {
        List<PaymentElement> paymentXMLS = new ArrayList<>();
        for (Payment p : payments) {
            PaymentElement paymentXML = new PaymentElement();
            paymentXML.setId(p.getId());
            paymentXML.setAmount(p.getAmount());
            paymentXML.setPaymentDate(p.getPayment_date());
            paymentXMLS.add(paymentXML);
        }
        return paymentXMLS;
    }


    public Patient toPatient(PatientElement patientXML) {
        Patient p = new Patient();
        p.setId(patientXML.getId());
        p.setName(patientXML.getName());
        p.setBirthDate(patientXML.getDob());
        p.setPhone(patientXML.getPhone());
        return p;
    }

    public List<MedRecord> toMedicalRecords(PatientElement patientXML) {
        List<MedRecord> medicalRecords = new ArrayList<>();
        if (patientXML.getMedicalrecords() == null) {
            return medicalRecords;
        }

        for (MedRecordElement record : patientXML.getMedicalrecords()) {
            MedRecord mrecord = new MedRecord();
            mrecord.setId(Integer.parseInt(record.getId()));
            mrecord.setPatient_id(patientXML.getId());
            mrecord.setDoctor_id(record.getDoctorId());
            mrecord.setDiagnosis(record.getDiagnosis());
            mrecord.setAdmission_date(record.getAdmissionDate());
            mrecord.setMedications(record.getMedications());
            medicalRecords.add(mrecord);
        }
        return medicalRecords;
    }

    public List<Appointment> toAppointments(PatientElement patientXML) {
        List<Appointment> appointments = new ArrayList<>();
        if (patientXML.getAppointments() == null) {
            return appointments;
        }
        for (AppointmentElement axml : patientXML.getAppointments()) {
            Appointment a = new Appointment();
            a.setId(axml.getId());
            a.setPatient_id(patientXML.getId());
            a.setDoctor_id(axml.getDoctorId());
            a.setAppointment_date(axml.getAppointmentDate());
            appointments.add(a);
        }
        return appointments;
    }

    public List<Payment> toPayments(PatientElement patientXML) {
        List<Payment> payments = new ArrayList<>();
        if (patientXML.getPayments() == null) {
            return payments;
        }
        for (PaymentElement pxml : patientXML.getPayments()) {
            Payment p = new Payment();
            p.setId(pxml.getId());
            p.setPatient_id(patientXML.getId());
            p.setAmount(pxml.getAmount());
            p.setPayment_date(pxml.getPaymentDate());
            payments.add(p);
        }
        return payments;
    }

}
