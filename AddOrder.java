import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AddOrder extends JFrame {

    private JTable menuTable;
    private JTextField userIdField, userNameField, itemNameField, quantityField;
    private JButton submitButton;

    public AddOrder() {
        setTitle("Place Order - Delicious Kitchen");
        setSize(800, 600); // Increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    
        // ===== Top: Menu Items Table =====
        DefaultTableModel menuModel = new DefaultTableModel(new String[]{"Item Name", "Price"}, 0);
        menuTable = new JTable(menuModel);
        JScrollPane menuScroll = new JScrollPane(menuTable);
        loadMenuItems(menuModel);
        
        JLabel menuLabel = new JLabel("Available Menu Items:");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 20));
        menuLabel.setHorizontalAlignment(JLabel.CENTER);
        menuLabel.setForeground(Color.WHITE);            // White text
        menuLabel.setBackground(Color.DARK_GRAY);        // Dark background
        menuLabel.setOpaque(true);                       // Make background visible
        add(menuLabel, BorderLayout.NORTH);              // Add to the top
        
    
// ===== Center: Input Fields in GridBagLayout =====
JPanel inputPanel = new JPanel(new GridBagLayout());
inputPanel.setBackground(Color.DARK_GRAY);
inputPanel.setBorder(BorderFactory.createTitledBorder("Place Your Order"));
((javax.swing.border.TitledBorder)inputPanel.getBorder()).setTitleColor(Color.WHITE);

GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(10, 10, 10, 10);
gbc.fill = GridBagConstraints.HORIZONTAL;

String[] labels = {"User ID:", "User Name:", "Item Name:", "Quantity:"};
JTextField[] fields = {userIdField = new JTextField(20),
                       userNameField = new JTextField(20),
                       itemNameField = new JTextField(20),
                       quantityField = new JTextField(20)};

for (int i = 0; i < labels.length; i++) {
    gbc.gridx = 0;
    gbc.gridy = i;
    JLabel lbl = new JLabel(labels[i]);
    lbl.setForeground(Color.WHITE);
    inputPanel.add(lbl, gbc);

    gbc.gridx = 1;
    fields[i].setBackground(Color.LIGHT_GRAY);
    inputPanel.add(fields[i], gbc);
}

// ===== Submit Button =====
gbc.gridx = 0; gbc.gridy = labels.length;
gbc.gridwidth = 2;
gbc.anchor = GridBagConstraints.CENTER;

submitButton = new JButton("Submit Order");
submitButton.setForeground(Color.WHITE);
submitButton.setBackground(new Color(30, 144, 255)); // DodgerBlue
submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

inputPanel.add(submitButton, gbc);

    
        // ===== Add to Frame =====
        add(menuLabel, BorderLayout.NORTH);
        add(menuScroll, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    
        submitButton.addActionListener(e -> submitOrder());
        setVisible(true);
    }
    

    private void loadMenuItems(DefaultTableModel model) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT item_name, price FROM menu";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("item_name"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load menu items.");
        }
    }

    private void submitOrder() {
        String userIdText = userIdField.getText().trim();
        String userName = userNameField.getText().trim();
        String itemName = itemNameField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (userIdText.isEmpty() || userName.isEmpty() || itemName.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdText);
            int quantity = Integer.parseInt(quantityText);

            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                // Get price of the item from menu
                String priceQuery = "SELECT price FROM menu WHERE item_name = ?";
                PreparedStatement priceStmt = conn.prepareStatement(priceQuery);
                priceStmt.setString(1, itemName);
                ResultSet rs = priceStmt.executeQuery();

                if (rs.next()) {
                    double pricePerItem = rs.getDouble("price");
                    double totalPrice = pricePerItem * quantity;

                    // Insert into orders table
                    String insertQuery = "INSERT INTO orders (user_id, user_name, item_name, quantity, price) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, userName);
                    insertStmt.setString(3, itemName);
                    insertStmt.setInt(4, quantity);
                    insertStmt.setDouble(5, totalPrice);

                    int rows = insertStmt.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Order Successful!");
                        showBill(userName, itemName, quantity, totalPrice);
                        userIdField.setText("");
                        userNameField.setText("");
                        itemNameField.setText("");
                        quantityField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to insert order.");
                    }

                    insertStmt.close();
                } else {
                    JOptionPane.showMessageDialog(this, "Item not found in menu.");
                }

                rs.close();
                priceStmt.close();
                conn.close();
            } else {
                JOptionPane.showMessageDialog(this, "Database connection failed.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "User ID and Quantity must be numbers.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "SQL Error: " + ex.getMessage());
        }
    }

    private void showBill(String userName, String itemName, int quantity, double totalPrice) {
        JFrame billFrame = new JFrame("Bill");
        billFrame.setSize(400, 300);
        billFrame.setLocationRelativeTo(null);

        JTextArea billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setEditable(false);

        StringBuilder bill = new StringBuilder();
        bill.append("==================================\n");
        bill.append("          CANTEEN BILL\n");
        bill.append("==================================\n");
        bill.append("Customer Name: ").append(userName).append("\n");
        bill.append("----------------------------------\n");
        bill.append(String.format("%-15s %-10s %-10s\n", "Item", "Qty", "Total"));
        bill.append(String.format("%-15s %-10d ₹%-10.2f\n", itemName, quantity, totalPrice));
        bill.append("----------------------------------\n");
        bill.append(String.format("TOTAL BILL: ₹%.2f\n", totalPrice));
        bill.append("==================================\n");
        bill.append("      THANK YOU, VISIT AGAIN!\n");
        bill.append("==================================");

        billArea.setText(bill.toString());
        billFrame.add(new JScrollPane(billArea));
        billFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new AddOrder();
    }
}
