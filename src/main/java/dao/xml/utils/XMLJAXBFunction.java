package dao.xml.utils;

import commons.Configuration;
import commons.Constants;
import dao.xml.model.PatientElement;
import dao.xml.model.PatientsElement;
import domain.exceptions.DataBaseError;
import jakarta.inject.Inject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLJAXBFunction {
    private final Configuration config;

    @Inject
    public XMLJAXBFunction(Configuration config) {
        this.config = config;
    }

    private static final Logger LOGGER = Logger.getLogger(XMLJAXBFunction.class.getName());

    public void createFile(PatientElement patient){
        try {
            JAXBContext context = JAXBContext.newInstance(PatientsElement.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Path xmlFile= Paths
                    .get(Configuration.getInstance().getProperty("hospitalxml"));

            PatientsElement patientsList;
            if (Files.exists(xmlFile)) {
                patientsList = (PatientsElement) unmarshaller.unmarshal(Files.newInputStream(xmlFile));
                Files.delete(xmlFile);
            } else {
                patientsList = new PatientsElement();
            }
            patientsList.getPatients().removeIf(p -> p.getId() == patient.getId());
            patientsList.getPatients().add(patient);
            marshaller.marshal(patientsList, Files.newOutputStream(xmlFile));

        } catch (JAXBException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error while backing up patient in the XML file", e);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }

    public PatientElement getPatient(int id) {
        return getPatients().stream()
                    .filter(p -> p.getId() == id).findFirst().orElseThrow(() -> new DataBaseError(Constants.IDMISSMATCHING));
    }

    public List<PatientElement> getPatients() {
        try {
            Path xmlFile = Paths
                    .get(Configuration.getInstance().getProperty("hospitalxml"));
            if (!Files.exists(xmlFile)) {
                throw new DataBaseError(Constants.XML_FILE_DOES_NOT_EXIST);
            }

            JAXBContext context = JAXBContext.newInstance(PatientsElement.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            PatientsElement patientsList = (PatientsElement) unmarshaller.unmarshal(Files.newInputStream(xmlFile));
            return patientsList.getPatients();
        } catch (DataBaseError e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } catch (JAXBException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error while restoring the patient from the XML file", e);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }

    public void removePatientFromBackup(int id) {
        try {
            Path xmlFile = Paths
                    .get(Configuration.getInstance().getProperty("hospitalxml"));
            JAXBContext context = JAXBContext.newInstance(PatientsElement.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            PatientsElement patientsList = (PatientsElement) unmarshaller.unmarshal(Files.newInputStream(xmlFile));
            patientsList.getPatients().removeIf(p -> p.getId() == id);
            Files.delete(xmlFile);
            marshaller.marshal(patientsList, Files.newOutputStream(xmlFile));
        } catch (JAXBException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error while removing patient from the XML file", e);
            throw new DataBaseError(Constants.DATABASE_ERROR);
        }
    }

}


