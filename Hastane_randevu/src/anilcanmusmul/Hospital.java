package anilcanmusmul;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Hospital implements Serializable {
    private int hospitalId;
    private String name;
    private List<Section> sections;

    public Hospital(int hospitalId, String name) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.sections = new ArrayList<>();
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        if (getSection(section.getSectionId()) != null) {
            throw new DuplicateInfoException("Bu bölüm zaten hastaneye eklenmiş: " + section.getName());
        } else {
            sections.add(section);
        }
    }

    public void addDoctorToSection(Doctor doctor, Section section) {
        if (!sections.contains(section)) {
            throw new IllegalArgumentException("Bölüm hastaneye eklenmemiş: " + section.getName());
        } else {
            if (section.getDoctors().contains(doctor)) {
                throw new DuplicateInfoException("Doktor zaten bu bölümde: " + doctor.getName());
            } else {
                boolean doctorExistsInHospital = false;
                for (Section s : sections) {
                    if (s.getDoctors().contains(doctor)) {
                        doctorExistsInHospital = true;
                        break;
                    }
                }
                if (doctorExistsInHospital) {
                    throw new DuplicateInfoException("Doktor zaten hastaneye eklenmiş: " + doctor.getName());
                } else {
                    section.addDoctor(doctor);
                }
            }
        }
    }

    public Section getSection(int sectionID) {
        for (Section section : sections) {
            if (section.getSectionId() == sectionID) {
                return section;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hospital{id=")
          .append(hospitalId)
          .append(", name='")
          .append(name)
          .append("', sections=[");

        for (Section section : sections) {
            sb.append("Section{id=").append(section.getSectionId()).append(", name=").append(section.getName()).append("}, ");
        }

        if (!sections.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]}");
        return sb.toString();
    }
}