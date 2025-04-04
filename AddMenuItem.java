import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class AddMenuItem extends JFrame {

    private JTextField itemNameField, priceField;
    private JButton addButton;

    public AddMenuItem() {
        setTitle("🍽️ Add Menu Item");
        setSize(600, 350); // Increased window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // === Panel with Centralized Layout ===
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // padding

        // === Labels ===
        JLabel nameLabel = new JLabel("Item Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        nameLabel.setForeground(Color.WHITE);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        priceLabel.setForeground(Color.WHITE);

        itemNameField = new JTextField(20);
        priceField = new JTextField(10);

        itemNameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        priceField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        addButton = new JButton("Add Item");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setBackground(new Color(0, 204, 102));
        addButton.setForeground(Color.BLACK);
        addButton.setFocusPainted(false);

        // === Adding components to panel ===
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(itemNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(priceLabel, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(priceField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        // === Action ===
        addButton.addActionListener(e -> addItemToDatabase());

        add(panel);
        setVisible(true);
    }

    private void addItemToDatabase() {
        String itemName = itemNameField.getText().trim();
        String priceText = priceField.getText().trim();

        if (itemName.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);

            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO menu (item_name, price) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, itemName);
                stmt.setDouble(2, price);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Item added successfully!");
                    itemNameField.setText("");
                    priceField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to add item.");
                }

                stmt.close();
                conn.close();
            } else {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddMenuItem();
    }
}
