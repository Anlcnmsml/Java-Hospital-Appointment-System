package anilcanmusmul;
import java.util.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CRS {
    private HashMap<Long, Patient> patients;
    private LinkedList<Rendezvous> rendezvous;
    private HashMap<Integer, Hospital> hospitals;
    private List<Doctor> doctors;

    public CRS() {
        this.patients = new HashMap<>();
        this.rendezvous = new LinkedList<>();
        this.hospitals = new HashMap<>();
        this.doctors = new ArrayList<>();
    }

    public boolean makeRendezvous(long patientID, int hospitalID, int sectionID, int diplomaID, Date desiredDate) {
        Patient patient = patients.get(patientID);
        Hospital hospital = hospitals.get(hospitalID);

        if (patient == null || hospital == null) {
            throw new IDException("Patient or Hospital not found.");
        }

        Section section = hospital.getSection(sectionID);
        if (section == null) {
            throw new IDException("Section not found.");
        }

        System.out.println("Bölümdeki mevcut doktorlar: ");
        for (Doctor d : section.getDoctors()) {
            System.out.println(d);
        }

        Doctor doctor = section.getDoctor(diplomaID);
        if (doctor == null) {
            throw new IDException("Doctor not found.");
        }

        boolean success = doctor.getSchedule().addRendezvous(patient, desiredDate);
        if (success) {
            Rendezvous rendezvous = new Rendezvous(patient, desiredDate);
            this.rendezvous.add(rendezvous);
        }
        return success;
    }
    
    public void saveTablesToDisk(String fullPath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fullPath))) {
            out.writeObject(patients);
            out.writeObject(rendezvous);
            out.writeObject(hospitals);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
	public static CRS loadTablesFromDisk(String fullPath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fullPath))) {
            CRS crs = new CRS();
            crs.patients = (HashMap<Long, Patient>) in.readObject();
            crs.rendezvous = (LinkedList<Rendezvous>) in.readObject();
            crs.hospitals = (HashMap<Integer, Hospital>) in.readObject();
            return crs;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void processRendezvousAsync(long patientID, int hospitalID, int sectionID, int diplomaID, Date desiredDate) {
        Runnable task = () -> {
            try {
                boolean success = makeRendezvous(patientID, hospitalID, sectionID, diplomaID, desiredDate);
                if (success) {
                    System.out.println("Rendezvous successfully created.");
                } else {
                    System.out.println("Failed to create Rendezvous.");
                }
            } catch (IDException e) {
                System.err.println(e.getMessage());
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void addPatient(Patient patient) {
        if (patients.containsKey(patient.national_id)) {
            System.out.println("Bu TC kimlik numarasına sahip bir hasta zaten mevcut: " + patient.national_id);
            return;
        }

        for (Doctor doctor : doctors) {
            if (doctor.getNationalId() == patient.national_id) {
                System.out.println("Bu TC kimlik numarasına sahip bir doktor zaten mevcut: " + patient.national_id);
                return;
            }
        }

        patients.put(patient.national_id, patient);
        System.out.println("Hasta başarıyla eklendi: " + patient);
    }

    public Collection<Patient> getPatients() {
        return patients.values();
    }

    public List<Rendezvous> getRendezvous() {
        return rendezvous;
    }

    public void addHospital(Hospital hospital) {
        hospitals.put(hospital.getHospitalId(), hospital);
    }

    public void addDoctor(Doctor doctor) {
        for (Doctor existingDoctor : doctors) {
            if (existingDoctor.getDiplomaId() == doctor.getDiplomaId()) {
                System.out.println("Bu diploma ID'sine sahip bir doktor zaten mevcut: " + doctor.getDiplomaId());
                return;
            }
        }

        for (Doctor existingDoctor : doctors) {
            if (existingDoctor.getNationalId() == doctor.getNationalId()) {
                System.out.println("Bu TC kimlik numarasına sahip bir doktor zaten mevcut: " + doctor.getNationalId());
                return;
            }
        }

        if (patients.containsKey(doctor.getNationalId())) {
            System.out.println("Bu TC kimlik numarasına sahip bir hasta zaten mevcut: " + doctor.getNationalId());
            return;
        }

        doctors.add(doctor);
        System.out.println("Doktor başarıyla eklendi: " + doctor);
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

	public Collection<Hospital> getHospitals() {
        return hospitals.values();
    }
}