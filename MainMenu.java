import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class MainMenu extends JFrame {

    private JLabel clockLabel;
    private JLabel greetingLabel;

    public MainMenu() {
        setTitle("Welcome to Delicious Kitchen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        // === Main Panel ===
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // === Logo Image ===
        try {
            File file = new File("D:/PROJECT/Canteen_Management/chef.png");
            Image logo = ImageIO.read(file);
            Image scaled = logo.getScaledInstance(300, 300, Image.SCALE_SMOOTH); // Increased size
            JLabel logoLabel = new JLabel(new ImageIcon(scaled));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(10));
            panel.add(logoLabel);
        } catch (Exception e) {
            System.out.println("⚠️ Logo failed to load: " + e.getMessage());
        }
        panel.add(Box.createVerticalStrut(20)); // Space below logo


        // === Greeting ===
        greetingLabel = new JLabel(getGreetingMessage());
        greetingLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        greetingLabel.setForeground(Color.WHITE);
        greetingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(greetingLabel);

        // === Clock Label ===
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Consolas", Font.PLAIN, 18));
        clockLabel.setForeground(Color.LIGHT_GRAY);
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(5));
        panel.add(clockLabel);
        startClock();

        // === Title ===
        JLabel title = new JLabel("Welcome to Delicious Kitchen");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(title);

        // === Buttons Panel ===
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.BLACK);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 50));

        JButton addMenuBtn = createButton("➕ Add Menu Item", new Color(255, 193, 7));
        JButton orderFoodBtn = createButton("🍽️ Order Food", new Color(0, 255, 127));

        addHoverEffect(addMenuBtn, new Color(255, 193, 7), new Color(255, 215, 64));
        addHoverEffect(orderFoodBtn, new Color(0, 255, 127), new Color(102, 255, 178));

        btnPanel.add(addMenuBtn);
        btnPanel.add(orderFoodBtn);
        panel.add(btnPanel);

        // === Button Actions ===
        addMenuBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "⚠️ Only Admins should add menu items!\nAre you sure you want to proceed?",
                    "Admin Warning",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.OK_OPTION) {
                AddMenuItem.main(null);
            }
        });

        orderFoodBtn.addActionListener(e -> AddOrder.main(null));

        add(panel);
        setVisible(true);
    }

    // ==== Real-time Clock ====
    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            String currentTime = new SimpleDateFormat("hh:mm:ss a").format(new Date());
            clockLabel.setText("Current Time: " + currentTime);
        });
        timer.start();
    }

    // ==== Greeting ====
    private String getGreetingMessage() {
        int hour = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
        if (hour < 12) return "☀️ Good Morning, Welcome to Delicious Kitchen";
        else if (hour < 18) return "🌤️ Good Afternoon, Welcome to Delicious Kitchen";
        else return "🌙 Good Evening, Welcome to Delicious Kitchen";
    }

    // ==== Reusable Button Creator ====
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 60));
        return button;
    }

    // ==== Hover Effect ====
    private void addHoverEffect(JButton button, Color normal, Color hover) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normal);
            }
        });
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
