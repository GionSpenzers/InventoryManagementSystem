/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.DAO;

import com.inventory.DTO.SupplierDTO;
import com.inventory.Database.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Locale;
import java.util.Vector;

/**
 *
 * @author GKV
 */

// Data Access Object for Suppliers
public class SupplierDAO {

    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement prepStatement = null;
    private ResultSet resultSet = null;
    private ConnectionFactory connectionFactory;

    public SupplierDAO() {
        try {
            connectionFactory = ConnectionFactory.getInstance();
            conn = connectionFactory.getConn();
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== ADD METHODS ====================
    
    // Methods to add new supplier
    public void addSupplierDAO(SupplierDTO supplierDTO) {
        String checkQuery = "SELECT * FROM suppliers WHERE fullname = ? AND location = ? AND mobile = ?";
        try {
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, supplierDTO.getFullName());
            prepStatement.setString(2, supplierDTO.getLocation());
            prepStatement.setString(3, supplierDTO.getPhone());
            resultSet = prepStatement.executeQuery();
            
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "This supplier already exists.");
            } else {
                addFunction(supplierDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking supplier: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    
    private void addFunction(SupplierDTO supplierDTO) {
        String query = "INSERT INTO suppliers (suppliercode, fullname, location, mobile, debit, credit, balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, supplierDTO.getSuppCode());
            prepStatement.setString(2, supplierDTO.getFullName());
            prepStatement.setString(3, supplierDTO.getLocation());
            prepStatement.setString(4, supplierDTO.getPhone());
            prepStatement.setDouble(5, supplierDTO.getDebit());
            prepStatement.setDouble(6, supplierDTO.getCredit());
            prepStatement.setDouble(7, supplierDTO.getBalance());
            prepStatement.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "New supplier has been added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(null, "Supplier code already exists. Please use a unique code.");
            } else {
                JOptionPane.showMessageDialog(null, "Error adding supplier: " + e.getMessage());
            }
        } finally {
            closeResources();
        }
    }

    // ==================== EDIT METHODS ====================
    
    // Method to edit existing supplier details
    public void editSupplierDAO(SupplierDTO supplierDTO) {
        String query = "UPDATE suppliers SET fullname = ?, location = ?, mobile = ?, debit = ?, credit = ?, balance = ? WHERE suppliercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, supplierDTO.getFullName());
            prepStatement.setString(2, supplierDTO.getLocation());
            prepStatement.setString(3, supplierDTO.getPhone());
            prepStatement.setDouble(4, supplierDTO.getDebit());
            prepStatement.setDouble(5, supplierDTO.getCredit());
            prepStatement.setDouble(6, supplierDTO.getBalance());
            prepStatement.setString(7, supplierDTO.getSuppCode());
            prepStatement.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Supplier details have been updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating supplier: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    
    // Method to update supplier balance
    public void updateSupplierBalance(String suppCode, double amount, String transactionType) {
        String query = "";
        if (transactionType.equalsIgnoreCase("debit")) {
            query = "UPDATE suppliers SET debit = debit + ?, balance = balance - ? WHERE suppliercode = ?";
        } else if (transactionType.equalsIgnoreCase("credit")) {
            query = "UPDATE suppliers SET credit = credit + ?, balance = balance + ? WHERE suppliercode = ?";
        }
        
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setDouble(1, amount);
            prepStatement.setDouble(2, amount);
            prepStatement.setString(3, suppCode);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    
    // Update supplier with code change
    public void updateSupplierWithCodeChange(SupplierDTO supplierDTO, String oldSupplierCode) {
        try {
            conn.setAutoCommit(false);

            // Check if new supplier code already exists
            String checkQuery = "SELECT suppliercode FROM suppliers WHERE suppliercode = ? AND suppliercode != ?";
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, supplierDTO.getSuppCode());
            prepStatement.setString(2, oldSupplierCode);
            resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Supplier code already exists! Please use a different code.");
                conn.rollback();
                return;
            }

            // Update suppliers table
            String query = "UPDATE suppliers SET suppliercode = ?, fullname = ?, location = ?, mobile = ?, debit = ?, credit = ?, balance = ? WHERE suppliercode = ?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, supplierDTO.getSuppCode());
            prepStatement.setString(2, supplierDTO.getFullName());
            prepStatement.setString(3, supplierDTO.getLocation());
            prepStatement.setString(4, supplierDTO.getPhone());
            prepStatement.setDouble(5, supplierDTO.getDebit());
            prepStatement.setDouble(6, supplierDTO.getCredit());
            prepStatement.setDouble(7, supplierDTO.getBalance());
            prepStatement.setString(8, oldSupplierCode);
            prepStatement.executeUpdate();

            // Update purchaseinfo table
            String query2 = "UPDATE purchaseinfo SET suppliercode = ? WHERE suppliercode = ?";
            prepStatement = conn.prepareStatement(query2);
            prepStatement.setString(1, supplierDTO.getSuppCode());
            prepStatement.setString(2, oldSupplierCode);
            prepStatement.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(null, "Supplier updated with new code!");

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating supplier: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources();
        }
    }

    // ==================== DELETE METHODS ====================
    
    // Method to delete existing supplier
    public void deleteSupplierDAO(String suppCode) {
        // Check if supplier has any purchase records
        String checkQuery = "SELECT * FROM purchaseinfo WHERE suppliercode = ?";
        try {
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, suppCode);
            resultSet = prepStatement.executeQuery();
            
            if (resultSet.next()) {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "This supplier has purchase records. Deleting will also delete all associated purchases. Continue?",
                    "Warning", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Delete supplier
            String query = "DELETE FROM suppliers WHERE suppliercode = ?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppCode);
            int rowsAffected = prepStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Supplier has been removed.");
            } else {
                JOptionPane.showMessageDialog(null, "Supplier not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting supplier: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    // ==================== GETTER/QUERY METHODS ====================
    
    // Supplier data set retrieval method
    public ResultSet getQueryResult() {
        try {
            String query = "SELECT suppliercode, fullname, location, mobile FROM suppliers ORDER BY suppliercode ASC";
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get supplier by code
    public ResultSet getSupplierByCode(String suppCode) {
        String query = "SELECT * FROM suppliers WHERE suppliercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppCode);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get supplier name by code
    public String getSupplierName(String suppCode) {
        String name = null;
        String query = "SELECT fullname FROM suppliers WHERE suppliercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppCode);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("fullname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return name;
    }
    
    // Get supplier balance
    public double getSupplierBalance(String suppCode) {
        double balance = 0.0;
        String query = "SELECT balance FROM suppliers WHERE suppliercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppCode);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                balance = resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return balance;
    }
    
    // Get all suppliers (simple list for dropdowns)
    public ResultSet getAllSuppliers() {
        try {
            String query = "SELECT suppliercode, fullname FROM suppliers ORDER BY fullname";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get supplier code by name
    public String getSuppCodeByName(String suppName) {
        String suppCode = null;
        String query = "SELECT suppliercode FROM suppliers WHERE fullname = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppName);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                suppCode = resultSet.getString("suppliercode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return suppCode;
    }
    
    // Simple method for combo box
    public ResultSet getSuppliersForCombo() {
        try {
            String query = "SELECT fullname FROM suppliers ORDER BY fullname";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== SEARCH METHODS ====================
    
    // Search method
    public ResultSet getSearchResult(String searchText) {
        try {
            String query = """
                SELECT suppliercode, fullname, location, mobile 
                FROM suppliers 
                WHERE suppliercode LIKE ? 
                   OR fullname LIKE ? 
                   OR location LIKE ? 
                   OR mobile LIKE ?
                ORDER BY suppliercode ASC
                """;
            prepStatement = conn.prepareStatement(query);
            String searchPattern = "%" + searchText + "%";
            prepStatement.setString(1, searchPattern);
            prepStatement.setString(2, searchPattern);
            prepStatement.setString(3, searchPattern);
            prepStatement.setString(4, searchPattern);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Search suppliers with balance filter
    public ResultSet getSuppliersByBalance(double minBalance, double maxBalance) {
        try {
            String query = """
                SELECT suppliercode, fullname, location, mobile, debit, credit, balance 
                FROM suppliers 
                WHERE balance BETWEEN ? AND ?
                ORDER BY balance DESC
                """;
            prepStatement = conn.prepareStatement(query);
            prepStatement.setDouble(1, minBalance);
            prepStatement.setDouble(2, maxBalance);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get suppliers with outstanding balance (credit > debit)
    public ResultSet getSuppliersWithOutstanding() {
        try {
            String query = """
                SELECT suppliercode, fullname, location, mobile, debit, credit, balance 
                FROM suppliers 
                WHERE balance > 0
                ORDER BY balance DESC
                """;
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== SUPPLIER PURCHASE HISTORY ====================
    
    // Get supplier purchase history
    public ResultSet getSupplierPurchaseHistory(String suppCode) {
        String query = """
            SELECT purchaseid, date, productcode, quantity, totalcost 
            FROM purchaseinfo 
            WHERE suppliercode = ?
            ORDER BY date DESC
            """;
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppCode);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get total purchases from supplier
    public double getTotalPurchasesFromSupplier(String suppCode) {
        double total = 0.0;
        String query = "SELECT SUM(totalcost) as total FROM purchaseinfo WHERE suppliercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppCode);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return total;
    }

    // ==================== COMBOBOX METHODS ====================
    
    // Method to set/update supplier combo box
    public DefaultComboBoxModel<String> setComboItems(ResultSet resultSet) throws SQLException {
        Vector<String> suppNames = new Vector<>();
        while (resultSet.next()) {
            suppNames.add(resultSet.getString("fullname"));
        }
        return new DefaultComboBoxModel<>(suppNames);
    }
    
    // Get suppliers as combo box model (simplified)
    public DefaultComboBoxModel<String> getSupplierComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            ResultSet rs = getAllSuppliers();
            while (rs.next()) {
                model.addElement(rs.getString("fullname"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    // ==================== UTILITY METHODS ====================
    
    // Method to display retrieved data set in tabular form
    public DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return new DefaultTableModel();
        }
        
        ResultSetMetaData metaData = resultSet.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int colCount = metaData.getColumnCount();

        for (int col = 1; col <= colCount; col++) {
            String columnName = metaData.getColumnName(col).toUpperCase(Locale.ROOT);
            // Make column names more user-friendly
            switch (columnName) {
                case "SUPPLIERCODE":
                    columnNames.add("CODE");
                    break;
                case "FULLNAME":
                    columnNames.add("SUPPLIER NAME");
                    break;
                case "LOCATION":
                    columnNames.add("LOCATION");
                    break;
                case "MOBILE":
                    columnNames.add("CONTACT");
                    break;
                case "DEBIT":
                    columnNames.add("DEBIT");
                    break;
                case "CREDIT":
                    columnNames.add("CREDIT");
                    break;
                case "BALANCE":
                    columnNames.add("BALANCE");
                    break;
                default:
                    columnNames.add(columnName);
            }
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>();
            for (int col = 1; col <= colCount; col++) {
                Object value = resultSet.getObject(col);
                // Format currency values
                String columnType = metaData.getColumnTypeName(col);
                if (columnType.toUpperCase().contains("DOUBLE") || 
                    columnType.toUpperCase().contains("REAL") ||
                    columnNameContains(metaData.getColumnName(col), "DEBIT", "CREDIT", "BALANCE")) {
                    if (value != null) {
                        vector.add(String.format("%.2f", (Double) value));
                    } else {
                        vector.add("0.00");
                    }
                } else {
                    vector.add(value);
                }
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }
    
    private boolean columnNameContains(String columnName, String... names) {
        for (String name : names) {
            if (columnName.toUpperCase().contains(name)) {
                return true;
            }
        }
        return false;
    }
    
    // Check if supplier exists
    public boolean supplierExists(String suppCode) {
        boolean exists = false;
        String query = "SELECT 1 FROM suppliers WHERE suppliercode = ? LIMIT 1";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, suppCode);
            resultSet = prepStatement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exists;
    }
    
    // Get total number of suppliers
    public int getSupplierCount() {
        int count = 0;
        String query = "SELECT COUNT(*) as total FROM suppliers";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }
    
    // Get total debit/credit summary for all suppliers
    public double[] getFinancialSummary() {
        double[] summary = new double[3]; // [totalDebit, totalCredit, totalBalance]
        String query = "SELECT SUM(debit) as totalDebit, SUM(credit) as totalCredit, SUM(balance) as totalBalance FROM suppliers";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                summary[0] = resultSet.getDouble("totalDebit");
                summary[1] = resultSet.getDouble("totalCredit");
                summary[2] = resultSet.getDouble("totalBalance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return summary;
    }

    private void closeResources() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (prepStatement != null) {
                prepStatement.close();
                prepStatement = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        closeResources();
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}