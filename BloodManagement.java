import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class BloodManagement extends JFrame {
    // Labels and text fields for donor input
    JLabel nameLabel, ageLabel, weightLabel, groupLabel, phnoLabel, addressLabel;
    JTextField nameField, ageField, weightField, groupField, phoneField, addressField;

    // Table models for donor and available blood
    DefaultTableModel donorTableModel, availableTableModel;

    public BloodManagement() {
        setSize(600, 400);
        setTitle("Add Donor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create main panel with CardLayout for switching between pages
        JPanel mainPanel = new JPanel(new CardLayout());
        mainPanel.setBackground(new Color(255, 182, 193)); // Light red background color

        // Welcome page
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel welcomeLabel = new JLabel("WELCOME");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Increase font size
        welcomePanel.add(welcomeLabel);

        JButton nextButton = createButton("Next", e -> switchPanel(mainPanel, "home"));
        welcomePanel.add(nextButton);

        // Home page
        JPanel homePanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 columns
        homePanel.add(createButton("Add Donor", e -> switchPanel(mainPanel, "page1")));
        homePanel.add(createButton("View Donor Details", e -> switchPanel(mainPanel, "details")));
        homePanel.add(createButton("Blood Availability", e -> switchPanel(mainPanel, "availablePanel")));
        homePanel.add(createBackButton(mainPanel)); // Back button

        // Create the first page (Add Donor)
        JPanel addDonorPanel = createAddDonorPanel(mainPanel);
       
        // Create the second page (Donor Information Display)
        JPanel detailsPanel = createDetailsPanel(mainPanel);

        // Create the available blood panel
        JPanel availablePanel = createAvailableBloodPanel(mainPanel);

        // Add all panels to the main panel
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(homePanel, "home");
        mainPanel.add(addDonorPanel, "page1");
        mainPanel.add(detailsPanel, "details");
        mainPanel.add(availablePanel, "availablePanel");

        // Add the main panel to the frame
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Utility method to create buttons with red color
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50)); // Increase button size
        button.setBackground(Color.RED); // Set button color to red
        button.setForeground(Color.WHITE); // Set text color to white
        button.addActionListener(action);
        return button;
    }

    // Method to create "Back to Home" button
    private JButton createBackButton(JPanel mainPanel) {
        JButton backButton = createButton("Back to Home", e -> switchPanel(mainPanel, "home"));
        return backButton;
    }

    private JPanel createAddDonorPanel(JPanel mainPanel) {
        JPanel panel = new JPanel(new GridLayout(7, 2));
        nameLabel = new JLabel("Enter Your Name:");
        ageLabel = new JLabel("Enter Your Age:");
        weightLabel = new JLabel("Enter Your Weight:");
        groupLabel = new JLabel("Enter Your Blood Group:");
        phnoLabel = new JLabel("Enter Your Phone Number:");
        addressLabel = new JLabel("Enter Your Address:");

        nameField = new JTextField(20);
        ageField = new JTextField(20);
        weightField = new JTextField(20);
        groupField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(50);

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(weightLabel);
        panel.add(weightField);
        panel.add(groupLabel);
        panel.add(groupField);
        panel.add(phnoLabel);
        panel.add(phoneField);
        panel.add(addressLabel);
        panel.add(addressField);

        JButton addButton = createButton("Add", e -> {
            if (validateInputs()) {
                donorTableModel.addRow(new Object[]{
                    nameField.getText(),
                    ageField.getText(),
                    weightField.getText(),
                    groupField.getText(),
                    phoneField.getText(),
                    addressField.getText()
                });
                clearDonorFields();
                switchPanel(mainPanel, "details");
            }
        });

        panel.add(addButton);
        panel.add(createBackButton(mainPanel)); // Add back button to this panel
        return panel;
    }

    private JPanel createDetailsPanel(JPanel mainPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        donorTableModel = new DefaultTableModel(new String[]{"Name", "Age", "Weight", "Blood Group", "Phone", "Address"}, 0);
        JTable donorTable = new JTable(donorTableModel);
        panel.add(new JScrollPane(donorTable), BorderLayout.CENTER);

        panel.add(createBackButton(mainPanel), BorderLayout.SOUTH); // Add back button
        return panel;
    }

    private JPanel createAvailableBloodPanel(JPanel mainPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        availableTableModel = new DefaultTableModel(new String[]{
            "Hospital", "O+", "O-", "A+", "A-", "A1+", "A1-", "B+", "B-", "AB+", "AB-"}, 0);
        JTable availableTable = new JTable(availableTableModel);
        panel.add(new JScrollPane(availableTable), BorderLayout.CENTER);

        // Button to go to hospital registration
        JButton newRegisterButton = createButton("New Register", e -> registerHospital());
        JButton updateButton = createButton("Update", e -> updateHospital());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newRegisterButton);
        buttonPanel.add(updateButton);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(createBackButton(mainPanel), BorderLayout.SOUTH); // Add back button
        return panel;
    }

    private void registerHospital() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField hospitalField = new JTextField(20);
        JTextField bloodGroupField = new JTextField(20);
        JTextField unitsField = new JTextField(20);

        panel.add(new JLabel("Hospital Name:"));
        panel.add(hospitalField);
        panel.add(new JLabel("Blood Group (e.g., O+):"));
        panel.add(bloodGroupField);
        panel.add(new JLabel("Units:"));
        panel.add(unitsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Register Hospital", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String hospitalName = hospitalField.getText();
            String bloodGroup = bloodGroupField.getText().toUpperCase();
            int newUnits;
            try {
                newUnits = Integer.parseInt(unitsField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for units.");
                return;
            }

            boolean exists = false;
            for (int i = 0; i < availableTableModel.getRowCount(); i++) {
                String existingHospital = (String) availableTableModel.getValueAt(i, 0);
                if (existingHospital.equalsIgnoreCase(hospitalName)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Object[] newRow = new Object[availableTableModel.getColumnCount()];
                newRow[0] = hospitalName; // Hospital Name
                int columnIndex = getBloodGroupColumnIndex(bloodGroup);
                if (columnIndex != -1) {
                    newRow[columnIndex] = String.valueOf(newUnits);
                    availableTableModel.addRow(newRow);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid blood group entered.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Hospital is already registered.");
            }
        }
    }

    private void updateHospital() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField hospitalField = new JTextField(20);
        JTextField bloodGroupField = new JTextField(20);
        JTextField unitsField = new JTextField(20);

        panel.add(new JLabel("Hospital Name:"));
        panel.add(hospitalField);
        panel.add(new JLabel("Blood Group (e.g., O+):"));
        panel.add(bloodGroupField);
        panel.add(new JLabel("Units:"));
        panel.add(unitsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Hospital", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String hospitalName = hospitalField.getText();
            String bloodGroup = bloodGroupField.getText().toUpperCase();
            int newUnits;
            try {
                newUnits = Integer.parseInt(unitsField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for units.");
                return;
            }

            boolean exists = false;
            for (int i = 0; i < availableTableModel.getRowCount(); i++) {
                String existingHospital = (String) availableTableModel.getValueAt(i, 0);
                if (existingHospital.equalsIgnoreCase(hospitalName)) {
                    exists = true;
                    int columnIndex = getBloodGroupColumnIndex(bloodGroup);
                    if (columnIndex != -1) {
                        String existingUnitsStr = (String) availableTableModel.getValueAt(i, columnIndex);
                        int existingUnits = (existingUnitsStr != null && !existingUnitsStr.isEmpty()) 
                            ? Integer.parseInt(existingUnitsStr) : 0;
                        availableTableModel.setValueAt(String.valueOf(existingUnits + newUnits), i, columnIndex);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid blood group entered.");
                    }
                    break;
                }
            }

            if (!exists) {
                JOptionPane.showMessageDialog(this, "Hospital is not registered.");
            }
        }
    }

    private int getBloodGroupColumnIndex(String bloodGroup) {
        switch (bloodGroup) {
            case "O+": return 1;
            case "O-": return 2;
            case "A+": return 3;
            case "A-": return 4;
            case "A1+": return 5;
            case "A1-": return 6;
            case "B+": return 7;
            case "B-": return 8;
            case "AB+": return 9;
            case "AB-": return 10;
            default: return -1; // Invalid blood group
        }
    }

    private void switchPanel(JPanel mainPanel, String card) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, card);
    }

    private boolean validateInputs() {
        // Validation logic for donor inputs (can be expanded)
        return true; // Replace with actual validation
    }

    private void clearDonorFields() {
        nameField.setText("");
        ageField.setText("");
        weightField.setText("");
        groupField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    public static void main(String[] args) {
       // SwingUtilities.invokeLater(BloodManagement::new);
        BloodManagement bd=new BloodManagement();
    }
}