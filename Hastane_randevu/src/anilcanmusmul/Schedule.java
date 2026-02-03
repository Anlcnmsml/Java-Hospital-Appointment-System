package anilcanmusmul;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")
public class Schedule implements Serializable {
    private LinkedList<Rendezvous> sessions;
    private int maxPatientPerDay;

    public Schedule() {
        this.sessions = new LinkedList<>();
        this.maxPatientPerDay = 2;
    }

    public Schedule(int maxPatientPerDay) {
        this();
        if (maxPatientPerDay <= 0) {
            throw new IllegalArgumentException("Max patient per day must be greater than 0.");
        }
        this.maxPatientPerDay = maxPatientPerDay;
    }

    public synchronized boolean addRendezvous(Patient patient, Date desiredDate) {
        int countForDay = 0;

        for (Rendezvous r : sessions) {
            if (isSameDay(r.getDateTime(), desiredDate)) {
                countForDay++;
            }
        }

        if (countForDay >= maxPatientPerDay) {
            System.out.println("Cannot add rendezvous. Maximum patients per day reached for date: " + desiredDate);
            suggestAlternativeDate(desiredDate);
            return false;
        }

        Rendezvous newRendezvous = new Rendezvous(patient, desiredDate);
        sessions.add(newRendezvous);
        System.out.println("Rendezvous added successfully for date: " + desiredDate);
        return true;
    }

    private void suggestAlternativeDate(Date desiredDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(desiredDate);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date alternativeDate = calendar.getTime();
        System.out.println("Alternative date: " + alternativeDate);
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public synchronized LinkedList<Rendezvous> getSessions() {
        return new LinkedList<>(sessions);
    }

    public int getMaxPatientPerDay() {
        return maxPatientPerDay;
    }

    public void setMaxPatientPerDay(int maxPatientPerDay) {
        if (maxPatientPerDay <= 0) {
            throw new IllegalArgumentException("Max patient per day must be greater than 0.");
        }
        this.maxPatientPerDay = maxPatientPerDay;
    }
}
