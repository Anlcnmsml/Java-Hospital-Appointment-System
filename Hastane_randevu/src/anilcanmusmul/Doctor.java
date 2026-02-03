package anilcanmusmul;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class Doctor implements Serializable{
    private String name;
    private long nationalId;
    private int diplomaId;
    private Schedule schedule;

    public Doctor(String name, long nationalId, int diplomaId, int maxPatientPerDay) {
        this.name = name;
        this.nationalId = nationalId;
        this.diplomaId = diplomaId;
        this.schedule = new Schedule(maxPatientPerDay);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public long getNationalId() {
        return nationalId;
    }

    public int getDiplomaId() {
        return diplomaId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Doctor doctor = (Doctor) obj;
        return diplomaId == doctor.diplomaId && nationalId == doctor.nationalId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(diplomaId, nationalId);
    }

    @Override
    public String toString() {
        return "Doctor[name=" + name + ", nationalId=" + nationalId + ", diplomaId=" + diplomaId + "]";
    }
}
