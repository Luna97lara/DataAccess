package ui;

import commons.Constants;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import java.util.Scanner;

public class Main {

    static void main() {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();
        PatientUI patientUI = container.select(PatientUI.class).get();


    }
}
