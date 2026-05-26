/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.inventory.UI;

import com.inventory.DAO.ProductDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author GKV
 */
public class CurrentStocksPage extends javax.swing.JPanel {

    /**
     * Creates new form Products
     */
    private ProductDAO productDAO;
    
    public CurrentStocksPage() {
        initComponents();
        productDAO = new ProductDAO();
        loadStockData();
        setupSearch();
        // Only customize table if there's data
        if (stocksTable.getRowCount() > 0) {
            customizeTable();
        }
    }
    
    // Load all stock data
    public void loadStockData() {
        try {
            ResultSet rs = productDAO.getCurrentStockInfo();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"PRODUCT CODE", "PRODUCT NAME", "QUANTITY", "COST PRICE", "SELL PRICE"});

            if (rs != null) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("productcode"),
                        rs.getString("productname"),
                        rs.getInt("quantity"),
                        String.format("%.2f", rs.getDouble("costprice")),
                        String.format("%.2f", rs.getDouble("sellprice"))
                    });
                }
                rs.close();
            }
            stocksTable.setModel(model);

            if (model.getRowCount() > 0) {
                customizeTable();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading stock data: " + ex.getMessage());
            stocksTable.setModel(new DefaultTableModel());
        }
    }
    
    // Search stocks
    private void searchStocks() {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            loadStockData();
        } else {
            try {
                ResultSet rs = productDAO.getProductSearch(searchText);
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"PRODUCT CODE", "PRODUCT NAME", "QUANTITY", "COST PRICE", "SELL PRICE"});

                if (rs != null) {
                    while (rs.next()) {
                        // Get current stock quantity from currentstock table
                        int quantity = getCurrentStockQuantity(rs.getString("productcode"));

                        model.addRow(new Object[]{
                            rs.getString("productcode"),
                            rs.getString("productname"),
                            quantity,
                            String.format("%.2f", rs.getDouble("costprice")),
                            String.format("%.2f", rs.getDouble("sellprice"))
                        });
                    }
                    rs.close();
                }
                stocksTable.setModel(model);

                if (model.getRowCount() > 0) {
                    customizeTable();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error searching stocks: " + ex.getMessage());
            }
        }
    }
    
    // Helper method to get current stock quantity
    private int getCurrentStockQuantity(String productCode) {
        int quantity = 0;
        try {
            ResultSet rs = productDAO.getProdStock();
            while (rs != null && rs.next()) {
                if (rs.getString("productcode").equals(productCode)) {
                    quantity = rs.getInt("quantity");
                    break;
                }
            }
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quantity;
    }
    
    // Build table model from ResultSet
    private DefaultTableModel buildStockTableModel(ResultSet rs) throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"PRODUCT CODE", "PRODUCT NAME", "QUANTITY", "COST PRICE", "SELL PRICE"});
        
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("productcode"),
                rs.getString("productname"),
                rs.getInt("quantity"),
                String.format("%.2f", rs.getDouble("costprice")),
                String.format("%.2f", rs.getDouble("sellprice"))
            });
        }
        return model;
    }
    
    // Setup search functionality
    private void setupSearch() {
        // Real-time search as user types
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchStocks();
            }
        });
    }
    
        // Customize table appearance
    // Customize table appearance
    private void customizeTable() {
        try {
            if (stocksTable.getColumnCount() == 0) {
                return;
            }

            // Set column widths
            if (stocksTable.getColumnCount() > 0) {
                stocksTable.getColumnModel().getColumn(0).setPreferredWidth(100);  // Product Code
            }
            if (stocksTable.getColumnCount() > 1) {
                stocksTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Product Name
            }
            if (stocksTable.getColumnCount() > 2) {
                stocksTable.getColumnModel().getColumn(2).setPreferredWidth(80);   // Quantity
            }
            if (stocksTable.getColumnCount() > 3) {
                stocksTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Cost Price
            }
            if (stocksTable.getColumnCount() > 4) {
                stocksTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Sell Price
            }

            // Center align quantity column
            if (stocksTable.getColumnCount() > 2) {
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                stocksTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            }

            // Right align price columns
            if (stocksTable.getColumnCount() > 3) {
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
                stocksTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
                stocksTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            }

            // Set table font
            stocksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            stocksTable.setRowHeight(25);

            // Set header font
            JTableHeader header = stocksTable.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 12));
            header.setBackground(new Color(240, 240, 240));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Add a refresh method
    // Add this method
    public void refreshStockData() {
        loadStockData();
        System.out.println("Stock data reloaded");
    }
    
    public void refreshPageData() {
        refreshStockData();
        System.out.println("Current stock page data refreshed");
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        stocksTable = new javax.swing.JTable();
        searchPanel = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        searchLabel = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1118, 616));

        jLabel1.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N
        jLabel1.setText("CURRENT STOCKS");

        stocksTable.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        stocksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(stocksTable);

        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRefresh.setText("REFRESH");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);

        searchLabel.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        searchLabel.setText("Search: ");

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addComponent(searchLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(searchLabel))
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchField))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 306, Short.MAX_VALUE)
                        .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        refreshStockData();
        JOptionPane.showMessageDialog(null, "Stock data refreshed!");
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTable stocksTable;
    // End of variables declaration//GEN-END:variables
}
