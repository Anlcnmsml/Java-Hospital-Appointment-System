package anilcanmusmul;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Section implements Serializable {
    private int sectionId;
    private String name;
    private List<Doctor> doctors;

    public Section(int sectionId, String name) {
        this.sectionId = sectionId;
        this.name = name;
        this.doctors = new ArrayList<>();
    }

    public int getSectionId() {
        return sectionId;
    }

    public String getName() {
        return name;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void addDoctor(Doctor doctor) {
        if (doctors.contains(doctor)) {
            throw new DuplicateInfoException("Doktor zaten bu bölümde mevcut: " + doctor.getName());
        } else {
            doctors.add(doctor);
        }
    }

    public Doctor getDoctor(int diplomaId) {
        for (Doctor doctor : doctors) {
            if (doctor.getDiplomaId() == diplomaId) {
                return doctor;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Section{id=").append(sectionId).append(", name='").append(name).append("', doctors=[");

        for (Doctor doctor : doctors) {
            sb.append(doctor.getName()).append(", ");
        }

        if (!doctors.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]}");
        return sb.toString();
    }
}
