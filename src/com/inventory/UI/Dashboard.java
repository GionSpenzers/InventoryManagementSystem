/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.inventory.UI;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import com.inventory.DAO.UserDAO;
import com.inventory.DTO.UserDTO;
import com.inventory.Database.ConnectionFactory;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author GKV
 */
public class Dashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dashboard.class.getName());

    /**
     * Creates new form Dashboard
     */
    CardLayout layout;
    String userSelect;
    String username;
    String fullName;
    UserDTO userDTO;
    LocalDateTime outTime;
    private ConnectionFactory connectionFactory;
    
    public Dashboard(String username, String userType, UserDTO userDTO) {
        initComponents();
        displayPanel.setVisible(true);
        layout = new CardLayout();
        
        userSelect = userType;
        this.username = username;
        this.userDTO = userDTO;
        if("EMPLOYEE".equalsIgnoreCase(userType))
            notForEmployee();
        currentUserSession();
        
        // Panel Layout set to Card Layout to allow switching between different sections
        displayPanel.setLayout(layout);
        
        HomePage homePage = new HomePage(username);
        homePage.setName("Home");
        displayPanel.add("Home", homePage);
        
        UsersPage usersPage = new UsersPage();
        usersPage.setName("Users");
        displayPanel.add("Users", usersPage);

        CustomersPage customersPage = new CustomersPage();
        customersPage.setName("Customers");
        displayPanel.add("Customers", customersPage);

        ProductsPage productsPage = new ProductsPage();
        productsPage.setName("Products");
        displayPanel.add("Products", productsPage);

        SuppliersPage suppliersPage = new SuppliersPage();
        suppliersPage.setName("Suppliers");
        displayPanel.add("Suppliers", suppliersPage);

        CurrentStocksPage stockPage = new CurrentStocksPage();
        stockPage.setName("Current Stock");
        displayPanel.add("Current Stock", stockPage);

        SalesPage salesPage = new SalesPage(username);  // Pass the username
        salesPage.setName("Sales");
        displayPanel.add("Sales", salesPage);
            
        PurchasePage purchasePage = new PurchasePage();
        purchasePage.setName("Purchase");
        displayPanel.add("Purchase", purchasePage);

        UserLogsPage logsPage = new UserLogsPage();
        logsPage.setName("Logs");
        displayPanel.add("Logs", logsPage);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        
        // Window Listener
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    Dashboard.this,
                    "Are you sure you want to exit the application?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Record logout time
                    if (userDTO != null && username != null) {
                        outTime = LocalDateTime.now();
                        userDTO.setOutTime(String.valueOf(outTime));
                        userDTO.setUsername(username);
                        new UserDAO().addUserLogin(userDTO);
                    }

                    // Close database connection
                    if (connectionFactory != null) {
                        connectionFactory.releaseConnection();
                    }

                    // Exit the application
                    System.exit(0);
                }
            }
        });
        
        setTitle("Inventory Manager");
        setVisible(true);
    }
    // Methods to display different sections in the mainframe
    public void addHomePage() {
        layout.show(displayPanel, "Home");
    }
    public void addUsersPage() {
        refreshPage("Users");
    }
    public void addCustPage() {
        refreshPage("Customers");
    }
    public void addProdPage() {
        refreshPage("Products");
    }
    public void addSuppPage() {
        refreshPage("Suppliers");
    }
    public void addStockPage() {
        // Create a fresh instance every time
        /*
        CurrentStocksPage freshPage = new CurrentStocksPage();
        displayPanel.add("Current Stock", freshPage);
        layout.show(displayPanel, "Current Stock");
        */
        refreshPage("Current Stock");
        
                
    }
    public void addSalesPage() {
        refreshPage("Sales");
    }
    public void addPurchasePage() {
        refreshPage("Purchase");
    }
    public void addLogsPage() {
        refreshPage("Logs");
    }
    
    
    
    // Method to refresh Current Stock page when products are added/updated
    
    // Add this method to Dashboard.java
    public void refreshCurrentStock() {
        // Find the CurrentStocksPage component and refresh it
        for (Component comp : displayPanel.getComponents()) {
            if (comp instanceof CurrentStocksPage) {
                ((CurrentStocksPage) comp).refreshStockData();
                System.out.println("Current stock page refreshed");
                return;
            }
        }
        // If not found, create a new reference
        CurrentStocksPage freshPage = new CurrentStocksPage();
        displayPanel.add("Current Stock", freshPage);
        freshPage.refreshStockData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        userPanel = new javax.swing.JPanel();
        logoutButton = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        navPanel = new javax.swing.JPanel();
        homeButton = new javax.swing.JButton();
        prodButton = new javax.swing.JButton();
        stockButton = new javax.swing.JButton();
        customerButton = new javax.swing.JButton();
        suppButton = new javax.swing.JButton();
        salesButton = new javax.swing.JButton();
        purchaseButton = new javax.swing.JButton();
        userButton = new javax.swing.JButton();
        logButton = new javax.swing.JButton();
        displayPanel = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        logoutButton.setText("LOGOUT");
        logoutButton.addActionListener(this::logoutButtonActionPerformed);

        nameLabel.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        nameLabel.setText("USER: ");

        jLabel2.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel2.setText("DASHBOARD");

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                    .addComponent(logoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        homeButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        homeButton.setText("Home");
        homeButton.addActionListener(this::homeButtonActionPerformed);

        prodButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        prodButton.setText("Products");
        prodButton.addActionListener(this::prodButtonActionPerformed);

        stockButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        stockButton.setText("Current Stock");
        stockButton.addActionListener(this::stockButtonActionPerformed);

        customerButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        customerButton.setText("Customers");
        customerButton.addActionListener(this::customerButtonActionPerformed);

        suppButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        suppButton.setText("Suppliers");
        suppButton.addActionListener(this::suppButtonActionPerformed);

        salesButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        salesButton.setText("Sales");
        salesButton.addActionListener(this::salesButtonActionPerformed);

        purchaseButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        purchaseButton.setText("Purchases");
        purchaseButton.addActionListener(this::purchaseButtonActionPerformed);

        userButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        userButton.setText("Users");
        userButton.addActionListener(this::userButtonActionPerformed);

        logButton.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        logButton.setText("Users Log");
        logButton.addActionListener(this::logButtonActionPerformed);

        javax.swing.GroupLayout navPanelLayout = new javax.swing.GroupLayout(navPanel);
        navPanel.setLayout(navPanelLayout);
        navPanelLayout.setHorizontalGroup(
            navPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(homeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prodButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stockButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(suppButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(salesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(purchaseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        navPanelLayout.setVerticalGroup(
            navPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(homeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prodButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suppButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(salesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(purchaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        displayPanel.setPreferredSize(new java.awt.Dimension(1118, 616));
        displayPanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(navPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSeparator1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(userPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(navPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButtonActionPerformed
        // TODO add your handling code here:
        addHomePage();
    }//GEN-LAST:event_homeButtonActionPerformed

    private void prodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodButtonActionPerformed
        // TODO add your handling code here:
        addProdPage();
    }//GEN-LAST:event_prodButtonActionPerformed

    private void stockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockButtonActionPerformed
        // TODO add your handling code here:
        addStockPage();
    }//GEN-LAST:event_stockButtonActionPerformed

    private void customerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerButtonActionPerformed
        // TODO add your handling code here:
        addCustPage();
    }//GEN-LAST:event_customerButtonActionPerformed

    private void suppButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppButtonActionPerformed
        // TODO add your handling code here:
        addSuppPage();
    }//GEN-LAST:event_suppButtonActionPerformed

    private void salesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salesButtonActionPerformed
        // TODO add your handling code here:
        addSalesPage();
    }//GEN-LAST:event_salesButtonActionPerformed

    private void purchaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseButtonActionPerformed
        // TODO add your handling code here:
        addPurchasePage();
    }//GEN-LAST:event_purchaseButtonActionPerformed

    private void userButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userButtonActionPerformed
        // TODO add your handling code here:
        addUsersPage();
    }//GEN-LAST:event_userButtonActionPerformed

    private void logButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logButtonActionPerformed
        // TODO add your handling code here:
        addLogsPage();
    }//GEN-LAST:event_logButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            endUserSession();  // Record logout and close connection
            dispose();  // Close dashboard

            // Show login page
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        }
    }//GEN-LAST:event_logoutButtonActionPerformed
    
    // A method to end current session
    // Add a method to handle user session end
    private void endUserSession() {
        try {
            // Record logout time
            if (userDTO != null && username != null) {
                outTime = LocalDateTime.now();
                userDTO.setOutTime(String.valueOf(outTime));
                userDTO.setUsername(username);
                new UserDAO().addUserLogin(userDTO);
                System.out.println("User " + username + " logged out at " + outTime);
            }

            // Close database connection
            if (connectionFactory != null) {
                connectionFactory.releaseConnection();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Helper method to refresh any page
    private void refreshPage(String pageName) {
        layout.show(displayPanel, pageName);

        // Refresh the existing page
        Component currentPage = null;
        for (Component comp : displayPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(pageName)) {
                currentPage = comp;
                break;
            }
        }

        // Call refresh method on the page if it exists
        if (currentPage != null) {
            if (currentPage instanceof ProductsPage) {
                ((ProductsPage) currentPage).refreshPageData();
            } else if (currentPage instanceof SuppliersPage) {
                ((SuppliersPage) currentPage).refreshPageData();
            } else if (currentPage instanceof CurrentStocksPage) {
                ((CurrentStocksPage) currentPage).refreshPageData();
            } else if (currentPage instanceof UsersPage) {
                ((UsersPage) currentPage).refreshPageData();
            } else if (currentPage instanceof CustomersPage) {
                ((CustomersPage) currentPage).refreshPageData();
            } else if (currentPage instanceof SalesPage) {
                    ((SalesPage) currentPage).refreshPageData();
            } else if (currentPage instanceof PurchasePage) {
                ((PurchasePage) currentPage).refreshPageData();
            } else if (currentPage instanceof UserLogsPage) {
                ((UserLogsPage) currentPage).refreshPageData();
            } else if (currentPage instanceof HomePage) {
                // ((HomePage) currentPage).refreshPageData();
            }
        }

        System.out.println(pageName + " page refreshed");
    }
    
    
    // Method to display the user currently logged in
    public void currentUserSession() {
        UserDTO userDTO = new UserDTO();
        new UserDAO().getFullName(userDTO, username);
        nameLabel.setText("User: " + userDTO.getFullName() + " ("+userSelect+")");
    }
    // Allows only the ADMINISTRATOR type user to view and manipulate 'Users' and 'User Logs'
    
    public void notForEmployee(){
        userButton.setEnabled(false);
        logButton.setEnabled(false);
        // Optional: Change tooltip to explain why disabled
        userButton.setToolTipText("Access restricted to Administrators only");
        logButton.setToolTipText("Access restricted to Administrators only");
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Save logout time if user is logged in
                if (userDTO != null && username != null) {
                    outTime = LocalDateTime.now();
                    userDTO.setOutTime(String.valueOf(outTime));
                    userDTO.setUsername(username);
                    new UserDAO().addUserLogin(userDTO);
                }
                
                // Close database connection
                if (connectionFactory != null) {
                    connectionFactory.releaseConnection();
                }
                
                // Dispose all windows
                Window[] windows = Window.getWindows();
                for (Window window : windows) {
                    window.dispose();
                }
                
                // Exit the application
                System.exit(0);
                
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton customerButton;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JButton homeButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton logButton;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel navPanel;
    private javax.swing.JButton prodButton;
    private javax.swing.JButton purchaseButton;
    private javax.swing.JButton salesButton;
    private javax.swing.JButton stockButton;
    private javax.swing.JButton suppButton;
    private javax.swing.JButton userButton;
    private javax.swing.JPanel userPanel;
    // End of variables declaration//GEN-END:variables
}
