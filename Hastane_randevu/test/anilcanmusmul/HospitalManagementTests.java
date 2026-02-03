package anilcanmusmul;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class HospitalManagementTests {

    @Test
    void testPatientCreation() {
        Patient patient = new Patient("John Doe", 123456789L);
        assertEquals("John Doe", patient.getName());
        assertEquals(123456789L, patient.getNationalId());
    }
    
    @Test
    void testPatientWithSameNameDifferentId() {
        Patient patient1 = new Patient("John Doe", 123456789L);
        Patient patient2 = new Patient("John Doe", 987654321L);

        assertEquals("John Doe", patient1.getName());
        assertEquals("John Doe", patient2.getName());

        assertNotEquals(patient1.getNationalId(), patient2.getNationalId());
    }

    @Test
    void testDoctorSchedule() {
        Doctor doctor = new Doctor("Dr. Smith", 987654321L, 1001, 2);
        Schedule schedule = doctor.getSchedule();
        schedule.setMaxPatientPerDay(1);

        Patient patient1 = new Patient("Jane Doe", 123456789L);
        Patient patient2 = new Patient("John Doe", 987654321L);
        Date desiredDate = new Date();

        assertTrue(schedule.addRendezvous(patient1, desiredDate));

        assertFalse(schedule.addRendezvous(patient2, desiredDate));
    }
    
    @Test
    void testDoctorSchedule1() {
        Doctor doctor = new Doctor("Dr. Smith", 987654321L, 1001, 1);
        Schedule schedule = doctor.getSchedule();
        schedule.setMaxPatientPerDay(2);

        Patient patient1 = new Patient("Jane Doe", 123456789L);
        Patient patient2 = new Patient("John Doe", 987654321L);
        Date desiredDate = new Date();

        assertTrue(schedule.addRendezvous(patient1, desiredDate));

        assertTrue(schedule.addRendezvous(patient2, desiredDate));
    }
    
    @Test
    void testCRSAddPatient() {
        CRS crs = new CRS();
        Patient patient = new Patient("Alice", 111222333L);
        crs.addPatient(patient);
        assertEquals(1, crs.getPatients().size());
    }

    @Test
    void testCRSMakeRendezvous() {
        CRS crs = new CRS();
        Patient patient = new Patient("Bob", 444555666L);
        crs.addPatient(patient);

        Hospital hospital = new Hospital(1, "City Hospital");
        Section section = new Section(1, "Cardiology");
        Doctor doctor = new Doctor("Dr. Adams", 777888999L, 2001, 2);
        section.addDoctor(doctor);
        hospital.addSection(section);
        crs.addHospital(hospital);

        Schedule schedule = doctor.getSchedule();
        schedule.setMaxPatientPerDay(1);

        boolean success = crs.makeRendezvous(444555666L, 1, 1, 2001, new Date());
        assertTrue(success);
        boolean secondSuccess = crs.makeRendezvous(444555666L, 1, 1, 2001, new Date());
        assertFalse(secondSuccess);
    }
    
    @Test
    void testRendezvousCreation() {
        Date date = new Date();
        Patient patient = new Patient("John Doe", 123456789L);
        Rendezvous rendezvous = new Rendezvous(patient, date);

        assertEquals(patient, rendezvous.getPatient());
        assertEquals(date, rendezvous.getDateTime());
    }
    
    @Test
    void testRendezvousEquality() {
        Date date = new Date();
        Patient patient = new Patient("Jane Doe", 987654321L);
        Rendezvous rendezvous1 = new Rendezvous(patient, date);
        Rendezvous rendezvous2 = new Rendezvous(patient, date);

        assertEquals(rendezvous1, rendezvous2);
        assertEquals(rendezvous1.hashCode(), rendezvous2.hashCode());
    }
    
    @Test
    void testPersonCreation() {
        Person person = new Person("Alice", 1122334455L);

        assertEquals("Alice", person.getName());
        assertEquals(1122334455L, person.getNationalId());
    }
    
    @Test
    void testPersonEquality() {
        Person person1 = new Person("Bob", 123456789L);
        Person person2 = new Person("Bob", 987654321L);

        assertNotEquals(person1, person2);
        assertNotEquals(person1.getNationalId(), person2.getNationalId());
    }

    @Test
    void testSectionDuplicateDoctor() {
        Section section = new Section(1, "Radiology");
        Doctor doctor1 = new Doctor("Dr. Brown", 1122334455L, 3001, 1);
        Doctor doctor2 = new Doctor("Dr. Brown", 1122334455L, 3001, 1);
        
        section.addDoctor(doctor1);
        assertThrows(DuplicateInfoException.class, () -> section.addDoctor(doctor2));
    }
    
    @Test
    void testSectionAddDoctor() {
        Section section = new Section(1, "Radiology");
        Doctor doctor = new Doctor("Dr. Brown", 1122334455L, 3001, 1);
        section.addDoctor(doctor);
        assertTrue(section.getDoctors().contains(doctor));
    }

    @Test
    void testHospitalAddSection() {
        Hospital hospital = new Hospital(1, "Central Hospital");
        Section section = new Section(2, "Neurology");
        hospital.addSection(section);
        assertEquals(1, hospital.getSections().size());
    }
    
    @Test
    void testScheduleCreation() {
        Schedule schedule = new Schedule(5);

        assertEquals(5, schedule.getMaxPatientPerDay());
        assertEquals(0, schedule.getSessions().size());
    }
    
    @Test
    void testScheduleMaxCapacityExceeded() {
        Schedule schedule = new Schedule(1);

        Patient patient1 = new Patient("John Doe", 123456789L);
        Patient patient2 = new Patient("Jane Doe", 987654321L);
        Date date = new Date();

        assertTrue(schedule.addRendezvous(patient1, date));

        assertFalse(schedule.addRendezvous(patient2, date));
    }
    
    @Test
    void testHospitalHasSections() {
        Hospital hospital = new Hospital(1, "Central Hospital");
        Section section = new Section(2, "Neurology");
        hospital.addSection(section);
        assertTrue(hospital.getSections().contains(section));
    }

    @Test
    void testSaveAndLoadCRS() throws IOException, ClassNotFoundException {
        CRS crs = new CRS();
        Patient patient = new Patient("Alice", 123456789L);
        crs.addPatient(patient);
        String filePath = "test_crs_data.dat";

        crs.saveTablesToDisk(filePath);

        CRS loadedCRS = CRS.loadTablesFromDisk(filePath);

        assertNotNull(loadedCRS);
        assertEquals(1, loadedCRS.getPatients().size());
        assertEquals("Alice", loadedCRS.getPatients().iterator().next().getName());

        new File(filePath).delete();
    }
    
    @Test
    void testSaveAndLoadDifferentFile() throws IOException, ClassNotFoundException {
        CRS crs = new CRS();
        Patient patient = new Patient("Bob", 987654321L);
        crs.addPatient(patient);
        String filePath = "test_crs_data_2.dat";

        crs.saveTablesToDisk(filePath);

        CRS loadedCRS = CRS.loadTablesFromDisk(filePath);

        assertNotNull(loadedCRS);
        assertEquals(1, loadedCRS.getPatients().size());
        assertEquals("Bob", loadedCRS.getPatients().iterator().next().getName());

        new File(filePath).delete();
    }
}