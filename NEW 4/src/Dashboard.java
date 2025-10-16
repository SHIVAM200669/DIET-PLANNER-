import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;

public class Dashboard extends JFrame {
    private JTextField ageField, weightField, heightField;
    private JTextArea preferencesArea, dietArea;
    private String username;

    public Dashboard(String username) {
        this.username = username;
        setTitle("Food Diet Planner - Dashboard");
        setSize(850, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout());

        // Top Panel - Welcome
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setPreferredSize(new Dimension(850, 80));
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel - User Info + Diet Planner
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(new Color(245, 245, 245));

        // Left Panel - User Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(10, 1, 10, 10));
        infoPanel.setBackground(new Color(224, 255, 255));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Your Details"));

        infoPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        infoPanel.add(ageField);

        infoPanel.add(new JLabel("Weight (kg):"));
        weightField = new JTextField();
        infoPanel.add(weightField);

        infoPanel.add(new JLabel("Height (cm):"));
        heightField = new JTextField();
        infoPanel.add(heightField);

        infoPanel.add(new JLabel("Preferences / Goals:"));
        preferencesArea = new JTextArea(3, 20);
        preferencesArea.setLineWrap(true);
        preferencesArea.setWrapStyleWord(true);
        JScrollPane scrollPref = new JScrollPane(preferencesArea);
        infoPanel.add(scrollPref);

        JButton saveButton = new JButton("Save / Update Info");
        saveButton.setBackground(new Color(34, 139, 34));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(saveButton);

        loadUserDetails();
        saveButton.addActionListener(e -> saveUserDetails());

        // Right Panel - Diet Plan
        JPanel dietPanel = new JPanel();
        dietPanel.setLayout(new BorderLayout());
        dietPanel.setBackground(new Color(255, 228, 225));
        dietPanel.setBorder(BorderFactory.createTitledBorder("Suggested Diet Plan"));

        dietArea = new JTextArea();
        dietArea.setFont(new Font("Arial", Font.PLAIN, 16));
        dietArea.setEditable(false);
        dietArea.setLineWrap(true);
        dietArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(dietArea);
        dietPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton generateButton = new JButton("Generate Diet Plan");
        generateButton.setBackground(new Color(70, 130, 180));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFont(new Font("Arial", Font.BOLD, 16));

        JButton downloadButton = new JButton("Download Plan");
        downloadButton.setBackground(new Color(255, 140, 0));
        downloadButton.setForeground(Color.WHITE);
        downloadButton.setFont(new Font("Arial", Font.BOLD, 16));

        buttonPanel.add(generateButton);
        buttonPanel.add(downloadButton);
        dietPanel.add(buttonPanel, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> generateDietPlan());
        downloadButton.addActionListener(e -> downloadPlan());

        centerPanel.add(infoPanel);
        centerPanel.add(dietPanel);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadUserDetails() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT age, weight, height, preferences FROM users WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ageField.setText(String.valueOf(rs.getInt("age")));
                weightField.setText(String.valueOf(rs.getFloat("weight")));
                heightField.setText(String.valueOf(rs.getFloat("height")));
                preferencesArea.setText(rs.getString("preferences"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user details: " + e.getMessage());
        }
    }

    private void saveUserDetails() {
        try {
            int age = Integer.parseInt(ageField.getText());
            float weight = Float.parseFloat(weightField.getText());
            float height = Float.parseFloat(heightField.getText());
            String prefs = preferencesArea.getText();

            Connection conn = DatabaseConnection.getConnection();
            String query = "UPDATE users SET age=?, weight=?, height=?, preferences=? WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, age);
            ps.setFloat(2, weight);
            ps.setFloat(3, height);
            ps.setString(4, prefs);
            ps.setString(5, username);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Details updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating details: " + e.getMessage());
        }
    }

    private void generateDietPlan() {
        try {
            int age = Integer.parseInt(ageField.getText());
            float weight = Float.parseFloat(weightField.getText());
            float height = Float.parseFloat(heightField.getText());
            String prefs = preferencesArea.getText();

            double bmi = weight / ((height / 100) * (height / 100));
            StringBuilder plan = new StringBuilder();
            plan.append("Your Personalized Diet Plan:\n\n");

            String bmiStatus;
            Color bmiColor;

            if (bmi < 18.5) {
                bmiStatus = "Underweight";
                bmiColor = new Color(255, 204, 153);
                plan.append("- BMI: Underweight\n");
                plan.append("- Focus on calorie-rich, protein foods\n");
            } else if (bmi > 25) {
                bmiStatus = "Overweight";
                bmiColor = new Color(255, 102, 102);
                plan.append("- BMI: Overweight\n");
                plan.append("- Reduce sugar & fats, focus on veggies & protein\n");
            } else {
                bmiStatus = "Normal";
                bmiColor = new Color(102, 204, 102);
                plan.append("- BMI: Normal\n");
                plan.append("- Maintain balanced diet\n");
            }

            plan.append("\nBreakfast: Oatmeal / Eggs / Fruits\n");
            plan.append("Lunch: Rice / Vegetables / Lean Protein\n");
            plan.append("Snack: Nuts / Fruits / Yogurt\n");
            plan.append("Dinner: Light meal with Protein & Veggies\n");

            if (!prefs.isEmpty()) plan.append("\nPreferences/Goals: ").append(prefs).append("\n");

            // Activity Section
            plan.append("\n🏃‍♂️ Activity Suggestions:\n");
            if (bmi < 18.5) plan.append("- Light strength training 3-4x/week, mobility exercises daily\n");
            else if (bmi >= 18.5 && bmi <= 25) {
                plan.append("- Cardio + strength 3-4x/week\n");
                if (age < 25) plan.append("- Add HIIT 1-2x/week\n");
                else if (age > 50) plan.append("- Focus on stretching & low-impact cardio\n");
            } else {
                plan.append("- Cardio 30-45min 5x/week, light strength\n");
                if (age > 50) plan.append("- Walking or yoga for joint health\n");
            }

            // Daily section
            plan.append("\n Daily Tips:\n");
            if (bmi < 18.5) plan.append("- Eat protein-rich, small meals every 3-4h, stay hydrated\n");
            else if (bmi >= 18.5 && bmi <= 25) plan.append("- Balanced diet, moderate exercise daily\n");
            else plan.append("- Avoid sugary drinks, track calories, stay active\n");

            if (age < 25) plan.append("- Sleep 7-9 hours\n");
            else if (age <= 50) plan.append("- Sleep 7-8 hours, manage stress\n");
            else plan.append("- Focus on joint care, mobility, 7-8h sleep\n");

            dietArea.setText(plan.toString());
            dietArea.setBackground(bmiColor.brighter());

            JOptionPane.showMessageDialog(this,
                    "Your BMI is " + String.format("%.1f", bmi) + " (" + bmiStatus + ")",
                    "BMI Status",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid details to generate diet plan.");
        }
    }

    private void downloadPlan() {
        if (dietArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Generate diet plan first!");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Diet Plan");
        fileChooser.setSelectedFile(new java.io.File("DietPlan_" + username + ".txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(dietArea.getText());
                JOptionPane.showMessageDialog(this, "Diet plan saved as " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("Shivam"));
    }
}
