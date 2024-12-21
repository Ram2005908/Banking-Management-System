import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SimpleBankManagementSystem extends JFrame {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String DB_USER = "root"; // Change to your MySQL username
    private static final String DB_PASSWORD = ""; // Change to your MySQL password

    // Panels
    private JPanel loginPanel, accountCreationPanel, bankingOperationsPanel;
    //private JLabel imageLabel;

    // Login Panel Components
    private JTextField usernameField;
    private JPasswordField passwordField;

    // Account Creation Panel Components
    private JTextField nameField, emailField, phoneField, aadhaarField, panField;
    private JRadioButton savingsAccountBtn, currentAccountBtn;

    // Banking Operations Panel Components
    private JTextField accountIdField, amountField;
    private JLabel accountIdLabel;
    private JButton addMoneyBtn, withdrawMoneyBtn, checkBalanceBtn;

    // Constructor
    public SimpleBankManagementSystem() {
        setTitle("Simple Bank Management System");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        // Initialize panels
        loginPanel = new BackgroundPanel("C:\\xampp\\htdocs\\banking system\\screenshot\\1.1.png");

        accountCreationPanel = new BackgroundPanel("C:\\xampp\\htdocs\\banking system\\screenshot\\1.11.png");
        bankingOperationsPanel = new BackgroundPanel("C:\\xampp\\htdocs\\banking system\\screenshot\\1.1.png");

        initializeLoginPanel();
        initializeAccountCreationPanel();
        initializeBankingOperationsPanel();

        // Add panels to CardLayout
        add(loginPanel, "Login");
        add(accountCreationPanel, "AccountCreation");
        add(bankingOperationsPanel, "BankingOperations");

        // Show login panel first
        switchToPanel("Login");
    }
    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Background image not found: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }



    // Initialize Login Panel
    private void initializeLoginPanel() {
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components

        // Title
        JLabel titleLabel = new JLabel("Banking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Login Screen");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        loginPanel.add(subtitleLabel, gbc);

        // Username label and field
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);
    }


    // Method to validate login
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    switchToPanel("AccountCreation");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // Initialize Account Creation Panel
    private void initializeAccountCreationPanel() {
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(15);
        aadhaarField = new JTextField(12);
        panField = new JTextField(10);
    
        savingsAccountBtn = new JRadioButton("Savings Account");
        currentAccountBtn = new JRadioButton("Current Account");
        ButtonGroup accountTypeGroup = new ButtonGroup();
        accountTypeGroup.add(savingsAccountBtn);
        accountTypeGroup.add(currentAccountBtn);
    
        JButton createAccountBtn = new JButton("Create Account");
        createAccountBtn.addActionListener(e -> createAccount());
        JButton nextBtn = new JButton("Next");
        nextBtn.addActionListener(e -> switchToPanel("BankingOperations"));
    
        accountCreationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Set layout to FlowLayout
    
        // Create individual panels for each field and label
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);
    
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(new JLabel("Email:"));
        emailPanel.add(emailField);
    
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.add(new JLabel("Phone:"));
        phonePanel.add(phoneField);
    
        JPanel aadhaarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        aadhaarPanel.add(new JLabel("Aadhaar:"));
        aadhaarPanel.add(aadhaarField);
    
        JPanel panPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panPanel.add(new JLabel("PAN:"));
        panPanel.add(panField);
    
        JPanel accountTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        accountTypePanel.add(savingsAccountBtn);
        accountTypePanel.add(currentAccountBtn);
    
        // Add components to the main panel
        accountCreationPanel.add(namePanel);
        accountCreationPanel.add(emailPanel);
        accountCreationPanel.add(phonePanel);
        accountCreationPanel.add(aadhaarPanel);
        accountCreationPanel.add(panPanel);
        accountCreationPanel.add(accountTypePanel);
        accountCreationPanel.add(createAccountBtn);
        accountCreationPanel.add(nextBtn);
    }
    
        // Create a new account
    private void createAccount() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String aadhaar = aadhaarField.getText();
        String pan = panField.getText();
        String accountType = savingsAccountBtn.isSelected() ? "Savings" : currentAccountBtn.isSelected() ? "Current" : "";

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || aadhaar.isEmpty() || pan.isEmpty()||accountType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO users (name, email, phone, aadhaar, pan,account_type, balance) VALUES (?, ?, ?, ?, ?,?, 0)";
            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setString(4, aadhaar);
                stmt.setString(5, pan);
                stmt.setString(6, accountType);
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int accountId = rs.getInt(1);
                    JOptionPane.showMessageDialog(this, "Account Created! ID: " + accountId);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Initialize Banking Operations Panel
   // Initialize Banking Operations Panel
   private void initializeBankingOperationsPanel() {
    // Initialize components here
    accountIdField = new JTextField(10);
    amountField = new JTextField(10);
    accountIdLabel = new JLabel("Account Number: ");
    
    addMoneyBtn = new JButton("Add Money");
    withdrawMoneyBtn = new JButton("Withdraw Money");
    checkBalanceBtn = new JButton("Check Balance");
    JButton previousBtn = new JButton("Previous");
    JButton exitBtn = new JButton("Exit");

    // Change the layout to FlowLayout.LEFT
    bankingOperationsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Set layout to FlowLayout with left alignment and spacing
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    // Add components to the panel
    bankingOperationsPanel.add(accountIdLabel);
    bankingOperationsPanel.add(accountIdField);
    panel.add(Box.createHorizontalStrut(100));
    bankingOperationsPanel.add(new JLabel("Amount:"));
    bankingOperationsPanel.add(amountField);
    panel.add(Box.createHorizontalStrut(100));
    bankingOperationsPanel.add(addMoneyBtn);
    bankingOperationsPanel.add(withdrawMoneyBtn);

    bankingOperationsPanel.add(checkBalanceBtn);

    bankingOperationsPanel.add(previousBtn);
    bankingOperationsPanel.add(exitBtn);

    // Add Action Listeners for Buttons (optional)
    addMoneyBtn.addActionListener(e -> addMoney());
    withdrawMoneyBtn.addActionListener(e -> withdrawMoney());
    checkBalanceBtn.addActionListener(e -> checkBalance());
    previousBtn.addActionListener(e -> switchToPanel("AccountCreation"));
    exitBtn.addActionListener(e -> System.exit(0));
}
private void switchToPanel(String panelName) {
    CardLayout cl = (CardLayout) getContentPane().getLayout();
    cl.show(getContentPane(), panelName);
}
    // Method to create a new account
    

    // Method to add money to an account
    private void addMoney() {
        try {
            int accountId = Integer.parseInt(accountIdField.getText()); // Use the text field input
            double amount = Double.parseDouble(amountField.getText());
    
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero.");
                return;
            }
    
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "UPDATE users SET balance = balance + ? WHERE account_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setDouble(1, amount);
                    stmt.setInt(2, accountId);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Money added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Account not found.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid account number or amount.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }
    
    // Method to withdraw money from an account
    private void withdrawMoney() {
        try {
            int accountId = Integer.parseInt(accountIdField.getText()); // Use the text field input
            double amount = Double.parseDouble(amountField.getText());
    
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero.");
                return;
            }
    
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Check current balance
                String checkBalanceQuery = "SELECT balance FROM users WHERE account_id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkBalanceQuery)) {
                    checkStmt.setInt(1, accountId);
                    ResultSet rs = checkStmt.executeQuery();
    
                    if (rs.next()) {
                        double currentBalance = rs.getDouble("balance");
                        if (currentBalance < amount) {
                            JOptionPane.showMessageDialog(this, "Insufficient balance.");
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Account not found.");
                        return;
                    }
                }
    
                // Update balance
                String updateQuery = "UPDATE users SET balance = balance - ? WHERE account_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setDouble(1, amount);
                    stmt.setInt(2, accountId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Money withdrawn successfully!");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid account number or amount.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }
    

    // Method to check balance of an account
    private void checkBalance() {
        try {
            int accountId = Integer.parseInt(accountIdField.getText()); // Use the text field input
    
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT balance FROM users WHERE account_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, accountId);
                    ResultSet rs = stmt.executeQuery();
    
                    if (rs.next()) {
                        double balance = rs.getDouble("balance");
                        JOptionPane.showMessageDialog(this, "Account Balance: " + balance);
                    } else {
                        JOptionPane.showMessageDialog(this, "Account not found.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid account number.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleBankManagementSystem().setVisible(true));
    }
}
