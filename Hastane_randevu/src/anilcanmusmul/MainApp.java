package anilcanmusmul;

import java.util.*;
import java.text.SimpleDateFormat;

public class MainApp {
    private static CRS crs = new CRS();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hangi modda başlatmak istiyorsunuz?");
        System.out.println("1. Konsol Modu");
        System.out.println("2. GUI Modu");
        System.out.print("Seçiminiz (1/2): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Konsol modu başlatılıyor...");
                runConsoleMode(crs, scanner);
                break;
            case 2:
                System.out.println("GUI modu başlatılıyor...");
                new HospitalManagementGUI(crs);
                break;
            default:
                System.out.println("Geçersiz seçim. Varsayılan olarak GUI modu başlatılıyor.");
                new HospitalManagementGUI(crs);
        }

        scanner.close();
    }

    private static void runConsoleMode(CRS crs, Scanner scanner) {

        while (true) {
            System.out.println("\n--- Klinik Rezervasyon Sistemi ---");
            System.out.println("1. Hasta Ekle");
            System.out.println("2. Doktor Ekle");
            System.out.println("3. Hastane ve Bölüm Ekle");
            System.out.println("4. Randevu Al");
            System.out.println("5. Tüm Randevuları Görüntüle");
            System.out.println("6. Çıkış");
            System.out.print("Seçiminizi yapın: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    addPatient(scanner);
                    break;
                case 2:
                    addDoctor(scanner);
                    break;
                case 3:
                    addHospitalAndSection(scanner);
                    break;
                case 4:
                    makeRendezvous(scanner);
                    break;
                case 5:
                    listRendezvous();
                    break;
                case 6:
                    System.out.println("Program sonlandırılıyor...");
                    return;
                default:
                    System.out.println("Geçersiz seçim, lütfen tekrar deneyiniz.");
            }
        }
    }
    
    private static boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z ]+");
    }

    private static void addPatient(Scanner scanner) {
        try {
            System.out.print("Hasta Adı: ");
            String name = scanner.nextLine();
            
            if (!isValidName(name)) {
                System.out.println("Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
                return;
            }

            System.out.print("TC Kimlik No: ");
            long nationalId = Long.parseLong(scanner.nextLine());

            Patient patient = new Patient(name, nationalId);
            crs.addPatient(patient);
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private static void addDoctor(Scanner scanner) {
        try {
            System.out.print("Doktor Adı: ");
            String name = scanner.nextLine();
            
            if (!isValidName(name)) {
                System.out.println("Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
                return;
            }

            System.out.print("TC Kimlik No: ");
            long nationalId = Long.parseLong(scanner.nextLine());
            System.out.print("Diploma ID: ");
            int diplomaId = Integer.parseInt(scanner.nextLine());
            System.out.print("Günlük Hasta Kapasitesi: ");
            int maxPatientPerDay = Integer.parseInt(scanner.nextLine());

            Doctor doctor = new Doctor(name, nationalId, diplomaId, maxPatientPerDay);
            doctor.getSchedule().setMaxPatientPerDay(maxPatientPerDay);
            crs.addDoctor(doctor);
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }


    private static void addHospitalAndSection(Scanner scanner) {
        try {
            System.out.print("Hastane Adı: ");
            String hospitalName = scanner.nextLine();
            if (!isValidName(hospitalName)) {
                System.out.println("Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
                return;
            }

            for (Hospital existingHospital : crs.getHospitals()) {
                if (existingHospital.getName().equalsIgnoreCase(hospitalName)) {
                    System.out.println("Hata: Bu hastane zaten mevcut.");
                    return;
                }
            }

            System.out.print("Hastane ID: ");
            int hospitalId = scanner.nextInt();
            scanner.nextLine();

            Hospital hospital = new Hospital(hospitalId, hospitalName);

            String addMoreSections = "Evet";
            while (addMoreSections.equalsIgnoreCase("Evet")) {
                System.out.print("Bölüm Adı: ");
                String sectionName = scanner.nextLine();
                if (!isValidName(sectionName)) {
                    System.out.println("Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
                    return;
                }

                boolean sectionExists = false;
                for (Section existingSection : hospital.getSections()) {
                    if (existingSection.getName().equalsIgnoreCase(sectionName)) {
                        sectionExists = true;
                        break;
                    }
                }

                if (sectionExists) {
                    System.out.println("Hata: Bu bölüm zaten mevcut.");
                    return;
                }

                System.out.print("Bölüm ID: ");
                int sectionId = scanner.nextInt();
                scanner.nextLine();

                Section section = new Section(sectionId, sectionName);

                String addMoreDoctors = "Evet";
                while (addMoreDoctors.equalsIgnoreCase("Evet")) {
                    System.out.print("Doktor Adı: ");
                    String doctorName = scanner.nextLine();
                    if (!isValidName(doctorName)) {
                        System.out.println("Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
                        return;
                    }
                    System.out.print("Doktor TC Kimlik No: ");
                    long doctorNationalId = scanner.nextLong();
                    System.out.print("Doktor Diploma ID: ");
                    int doctorDiplomaId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Günlük Hasta Kapasitesi: ");
                    int doctormaxPatientPerDay = scanner.nextInt();
                    scanner.nextLine();

					Doctor doctor = new Doctor(doctorName, doctorNationalId, doctorDiplomaId, doctormaxPatientPerDay);
                    section.addDoctor(doctor);

                    System.out.print("Başka bir doktor eklemek ister misiniz? (Evet/Hayır): ");
                    addMoreDoctors = scanner.nextLine();
                }

                hospital.addSection(section);
                System.out.print("Başka bir bölüm eklemek ister misiniz? (Evet/Hayır): ");
                addMoreSections = scanner.nextLine();
            }

            crs.addHospital(hospital);

            System.out.println("Hastane ve bölümler başarıyla eklendi: " + hospital);
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }


    private static void makeRendezvous(Scanner scanner) {
        try {
            System.out.print("Hasta TC Kimlik No: ");
            long patientId = Long.parseLong(scanner.nextLine());
            System.out.print("Hastane ID: ");
            int hospitalId = Integer.parseInt(scanner.nextLine());
            System.out.print("Bölüm ID: ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            System.out.print("Doktor Diploma ID: ");
            int diplomaId = Integer.parseInt(scanner.nextLine());
            System.out.print("Randevu Tarihi (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            boolean success = crs.makeRendezvous(patientId, hospitalId, sectionId, diplomaId, date);
            if (success) {
                System.out.println("Randevu başarıyla oluşturuldu.");
            } else {
                System.out.println("Randevu oluşturulamadı. Gün dolu olabilir.");
            }
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private static void listRendezvous() {
        System.out.println("\n--- Tüm Randevular ---");
        for (Rendezvous r : crs.getRendezvous()) {
            System.out.println(r);
        }
    }
}
