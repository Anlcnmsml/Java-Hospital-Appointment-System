package anilcanmusmul;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings("serial")
public class Rendezvous implements Serializable {
    private final Patient patient;
    private final Date dateTime;  

    public Rendezvous(Patient patient, Date dateTime) {
        this.patient = patient;
        this.dateTime = dateTime;
    }

    public Patient getPatient() {
        return patient;
    }

    public Date getDateTime() {
        return dateTime;
    }
    
    private String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return dateFormat.format(dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rendezvous that = (Rendezvous) o;
        return patient.equals(that.patient) && dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient, dateTime);
    }

    @Override
    public String toString() {
        return "Rendezvous{" +
                "patient=" + patient.getName() +
                ", dateTime=" + getFormattedDate() + 
                '}';
    }
}
