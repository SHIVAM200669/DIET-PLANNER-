
import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard(String username) {
        setTitle("Food Diet Planner - Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setBounds(20, 20, 400, 30);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel);

        // Future: Add diet plan buttons and features

        setVisible(true);
    }
}
