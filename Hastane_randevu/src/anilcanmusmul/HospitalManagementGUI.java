package anilcanmusmul;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class HospitalManagementGUI extends JFrame {
    private static CRS crs;
    private JTextArea textArea;

    @SuppressWarnings("static-access")
	public HospitalManagementGUI(CRS crs) {
        this.crs = crs;
        setTitle("Hastane Yönetim Sistemi");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        add(buttonPanel, BorderLayout.WEST);

        createButton("Hasta Ekle", buttonPanel, this::addPatient);
        createButton("Doktor Ekle", buttonPanel, this::addDoctor);
        createButton("Hastane ve Bölüm Ekle", buttonPanel, this::addHospitalAndSection);
        createButton("Randevu Al", buttonPanel, this::makeRendezvous);
        createButton("Tüm Randevuları Görüntüle", buttonPanel, this::listRendezvous);
        createButton("Veri Kaydet", buttonPanel, this::saveData);
        createButton("Veri Yükle", buttonPanel, this::loadData);

        setVisible(true);
    }

    private void createButton(String text, JPanel panel, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    private boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-Z ]+");
    }

    private void addPatient() {
        String name = JOptionPane.showInputDialog(this, "Hasta Adı:");

        if (!isValidName(name)) {
            JOptionPane.showMessageDialog(this, "Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
            return;
        }

        String nationalIdStr = JOptionPane.showInputDialog(this, "TC Kimlik No:");
        try {
            long nationalId = Long.parseLong(nationalIdStr);

            for (Patient existingPatient : crs.getPatients()) {
                if (existingPatient.getNationalId() == nationalId) {
                    JOptionPane.showMessageDialog(this, "Hata: Bu TC kimlik numarasına sahip bir hasta zaten mevcut.");
                    return;
                }
            }

            for (Doctor existingDoctor : crs.getDoctors()) {
                if (existingDoctor.getNationalId() == nationalId) {
                    JOptionPane.showMessageDialog(this, "Hata: Bu TC kimlik numarasına sahip bir doktor zaten mevcut.");
                    return;
                }
            }

            Patient patient = new Patient(name, nationalId);
            crs.addPatient(patient);
            textArea.append("Hasta başarıyla eklendi: " + patient + "\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            textArea.append("Hata: " + e.getMessage() + "\n");
        }
    }

    private void addDoctor() {
        String name = JOptionPane.showInputDialog(this, "Doktor Adı:");

        if (!isValidName(name)) {
            JOptionPane.showMessageDialog(this, "Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
            return;
        }

        String nationalIdStr = JOptionPane.showInputDialog(this, "TC Kimlik No:");
        String diplomaIdStr = JOptionPane.showInputDialog(this, "Diploma ID:");
        try {
            long nationalId = Long.parseLong(nationalIdStr);
            int diplomaId = Integer.parseInt(diplomaIdStr);

            for (Patient existingPatient : crs.getPatients()) {
                if (existingPatient.getNationalId() == nationalId) {
                    JOptionPane.showMessageDialog(this, "Hata: Bu TC kimlik numarasına sahip bir hasta zaten mevcut.");
                    return;
                }
            }

            for (Doctor existingDoctor : crs.getDoctors()) {
                if (existingDoctor.getNationalId() == nationalId) {
                    JOptionPane.showMessageDialog(this, "Hata: Bu TC kimlik numarasına sahip bir doktor zaten mevcut.");
                    return;
                }
            }

            for (Doctor existingDoctor : crs.getDoctors()) {
                if (existingDoctor.getDiplomaId() == diplomaId) {
                    JOptionPane.showMessageDialog(this, "Hata: Bu Diploma ID'ye sahip bir doktor zaten mevcut.");
                    return;
                }
            }
          String maxPatientPerDayStr = JOptionPane.showInputDialog(this, "Günlük hasta kapasitesi:");
            int maxPatientPerDay = Integer.parseInt(maxPatientPerDayStr);

			Doctor doctor = new Doctor(name, nationalId, diplomaId, maxPatientPerDay);
            crs.addDoctor(doctor);
            textArea.append("Doktor başarıyla eklendi: " + doctor + "\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            textArea.append("Hata: " + e.getMessage() + "\n");
        }
    }
    
    private void addHospitalAndSection() {
        String hospitalName = JOptionPane.showInputDialog(this, "Hastane Adı:");
        
        if (!isValidName(hospitalName)) {
            JOptionPane.showMessageDialog(this, "Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
            return;
        }

        for (Hospital existingHospital : crs.getHospitals()) {
            if (existingHospital.getName().equalsIgnoreCase(hospitalName)) {
                JOptionPane.showMessageDialog(this, "Hata: Bu hastane zaten mevcut.");
                return;
            }
        }
        
        String hospitalIdStr = JOptionPane.showInputDialog(this, "Hastane ID:");
        try {
            int hospitalId = Integer.parseInt(hospitalIdStr);

            Hospital hospital = new Hospital(hospitalId, hospitalName);

            String addMoreSections = "Evet";
            while (addMoreSections.equalsIgnoreCase("Evet")) {
                String sectionName = JOptionPane.showInputDialog(this, "Bölüm Adı:");
                if (!isValidName(sectionName)) {
                    JOptionPane.showMessageDialog(this, "Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
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
                    JOptionPane.showMessageDialog(this, "Hata: Bu bölüm zaten mevcut.");
                    return;
                }

                String sectionIdStr = JOptionPane.showInputDialog(this, "Bölüm ID:");
                int sectionId = Integer.parseInt(sectionIdStr);

                Section section = new Section(sectionId, sectionName);

                String addMoreDoctors = "Evet";
                while (addMoreDoctors.equalsIgnoreCase("Evet")) {
                    String doctorName = JOptionPane.showInputDialog(this, "Doktor Adı:");
                    if (!isValidName(doctorName)) {
                        JOptionPane.showMessageDialog(this, "Hata: Geçersiz isim! Sadece harfler kullanılabilir.");
                        return;
                    }
                    String doctorNationalIdStr = JOptionPane.showInputDialog(this, "Doktor TC Kimlik No:");
                    String doctorDiplomaIdStr = JOptionPane.showInputDialog(this, "Doktor Diploma ID:");
                    String doctormaxPatientPerDayStr = JOptionPane.showInputDialog(this, "Günlük hasta kapasitesi:");

                    long doctorNationalId = Long.parseLong(doctorNationalIdStr);
                    int doctorDiplomaId = Integer.parseInt(doctorDiplomaIdStr);
					int doctormaxPatientPerDay = Integer.parseInt(doctormaxPatientPerDayStr);

                    Doctor doctor = new Doctor(doctorName, doctorNationalId, doctorDiplomaId, doctormaxPatientPerDay);
                    section.addDoctor(doctor);

                    addMoreDoctors = JOptionPane.showInputDialog(this, "Başka bir doktor eklemek ister misiniz? (Evet/Hayır):");
                }

                hospital.addSection(section);

                addMoreSections = JOptionPane.showInputDialog(this, "Başka bir bölüm eklemek ister misiniz? (Evet/Hayır):");
            }

            crs.addHospital(hospital);

            textArea.append("Hastane ve bölümler başarıyla eklendi: " + hospital + "\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void makeRendezvous() {
        String patientIdStr = JOptionPane.showInputDialog(this, "Hasta TC Kimlik No:");
        String hospitalIdStr = JOptionPane.showInputDialog(this, "Hastane ID:");
        String sectionIdStr = JOptionPane.showInputDialog(this, "Bölüm ID:");
        String diplomaIdStr = JOptionPane.showInputDialog(this, "Doktor Diploma ID:");
        String dateStr = JOptionPane.showInputDialog(this, "Randevu Tarihi (yyyy-MM-dd):");
        try {
            long patientId = Long.parseLong(patientIdStr);
            int hospitalId = Integer.parseInt(hospitalIdStr);
            int sectionId = Integer.parseInt(sectionIdStr);
            int diplomaId = Integer.parseInt(diplomaIdStr);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            boolean success = crs.makeRendezvous(patientId, hospitalId, sectionId, diplomaId, date);
            if (success) {
                textArea.append("Randevu başarıyla oluşturuldu.\n");
            } else {
                textArea.append("Randevu oluşturulamadı. Gün dolu olabilir.\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void listRendezvous() {
        textArea.append("\n--- Tüm Randevular ---\n");
        for (Rendezvous r : crs.getRendezvous()) {
            textArea.append(r + "\n");
        }
    }

    private void saveData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Veri Kaydet");
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                crs.saveTablesToDisk(fileToSave.getAbsolutePath());
                textArea.append("Veri başarıyla kaydedildi.\n");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
                textArea.append("Hata: " + e.getMessage() + "\n");
            }
        }
    }

    private void loadData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Veri Yükle");

        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try {
                CRS loadedCRS = CRS.loadTablesFromDisk(fileToLoad.getAbsolutePath());
                if (loadedCRS != null) {
                    crs = loadedCRS;
                    textArea.append("Veri başarıyla yüklendi.\n");
                } else {
                    textArea.append("Veri yüklenemedi.\n");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
                textArea.append("Hata: " + e.getMessage() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        CRS crs = new CRS();
        new HospitalManagementGUI(crs);
    }
}
