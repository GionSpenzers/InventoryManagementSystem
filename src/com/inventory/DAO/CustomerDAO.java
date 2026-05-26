/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.DAO;

import com.inventory.DTO.CustomerDTO;
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

// Data Access Object for Customers
public class CustomerDAO {
    
    private Connection conn = null;
    private PreparedStatement prepStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private ConnectionFactory connectionFactory;

    public CustomerDAO() {
        try {
            connectionFactory = ConnectionFactory.getInstance();
            conn = connectionFactory.getConn();
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== ADD METHODS ====================
    
    // Method to add new customer
    public void addCustomerDAO(CustomerDTO customerDTO) {
        String checkQuery = "SELECT * FROM customers WHERE fullname = ? AND location = ? AND phone = ?";
        try {
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, customerDTO.getFullName());
            prepStatement.setString(2, customerDTO.getLocation());
            prepStatement.setString(3, customerDTO.getPhone());
            resultSet = prepStatement.executeQuery();
            
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Customer already exists.");
            } else {
                addFunction(customerDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking customer: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    
    private void addFunction(CustomerDTO customerDTO) {
        String query = "INSERT INTO customers (customercode, fullname, location, phone, debit, credit, balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, customerDTO.getCustCode());
            prepStatement.setString(2, customerDTO.getFullName());
            prepStatement.setString(3, customerDTO.getLocation());
            prepStatement.setString(4, customerDTO.getPhone());
            prepStatement.setDouble(5, customerDTO.getDebit());
            prepStatement.setDouble(6, customerDTO.getCredit());
            prepStatement.setDouble(7, customerDTO.getBalance());
            prepStatement.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "New customer has been added.");
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(null, "Customer code already exists. Please use a unique code.");
            } else {
                JOptionPane.showMessageDialog(null, "Error adding customer: " + e.getMessage());
            }
        } finally {
            closeResources();
        }
    }

    // ==================== EDIT METHODS ====================
    
    // Method to edit existing customer details
    public void editCustomerDAO(CustomerDTO customerDTO) {
        String query = "UPDATE customers SET fullname = ?, location = ?, phone = ?, debit = ?, credit = ?, balance = ? WHERE customercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, customerDTO.getFullName());
            prepStatement.setString(2, customerDTO.getLocation());
            prepStatement.setString(3, customerDTO.getPhone());
            prepStatement.setDouble(4, customerDTO.getDebit());
            prepStatement.setDouble(5, customerDTO.getCredit());
            prepStatement.setDouble(6, customerDTO.getBalance());
            prepStatement.setString(7, customerDTO.getCustCode());
            prepStatement.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Customer details have been updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating customer: " + e.getMessage());
        } finally {
            closeResources();
        }
    }
    
    // Method to update customer balance
    public void updateCustomerBalance(String custCode, double amount, String transactionType) {
        String query = "";
        if (transactionType.equalsIgnoreCase("debit")) {
            query = "UPDATE customers SET debit = debit + ?, balance = balance - ? WHERE customercode = ?";
        } else if (transactionType.equalsIgnoreCase("credit")) {
            query = "UPDATE customers SET credit = credit + ?, balance = balance + ? WHERE customercode = ?";
        }
        
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setDouble(1, amount);
            prepStatement.setDouble(2, amount);
            prepStatement.setString(3, custCode);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    
    // Update customer with code change
    // Update customer with code change
    public void updateCustomerWithCodeChange(CustomerDTO customerDTO, String oldCustomerCode) {
        try {
            conn.setAutoCommit(false);

            // Check if new customer code already exists
            String checkQuery = "SELECT customercode FROM customers WHERE customercode = ? AND customercode != ?";
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, customerDTO.getCustCode());
            prepStatement.setString(2, oldCustomerCode);
            resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Customer code already exists! Please use a different code.");
                conn.rollback();
                return;
            }

            // Update customers table
            String query = "UPDATE customers SET customercode = ?, fullname = ?, location = ?, phone = ?, debit = ?, credit = ?, balance = ? WHERE customercode = ?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, customerDTO.getCustCode());
            prepStatement.setString(2, customerDTO.getFullName());
            prepStatement.setString(3, customerDTO.getLocation());
            prepStatement.setString(4, customerDTO.getPhone());
            prepStatement.setDouble(5, customerDTO.getDebit());
            prepStatement.setDouble(6, customerDTO.getCredit());
            prepStatement.setDouble(7, customerDTO.getBalance());
            prepStatement.setString(8, oldCustomerCode);
            prepStatement.executeUpdate();

            // Update salesinfo table
            String query2 = "UPDATE salesinfo SET customercode = ? WHERE customercode = ?";
            prepStatement = conn.prepareStatement(query2);
            prepStatement.setString(1, customerDTO.getCustCode());
            prepStatement.setString(2, oldCustomerCode);
            prepStatement.executeUpdate();

            conn.commit();
            JOptionPane.showMessageDialog(null, "Customer updated with new code!");

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating customer: " + e.getMessage());
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
    
    // Method to delete existing customer
    public void deleteCustomerDAO(String custCode) {
        // Check if customer has any sales records
        String checkQuery = "SELECT * FROM salesinfo WHERE customercode = ?";
        try {
            prepStatement = conn.prepareStatement(checkQuery);
            prepStatement.setString(1, custCode);
            resultSet = prepStatement.executeQuery();
            
            if (resultSet.next()) {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "This customer has sales records. Deleting will also delete all associated sales. Continue?",
                    "Warning", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Delete customer
            String query = "DELETE FROM customers WHERE customercode = ?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, custCode);
            int rowsAffected = prepStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Customer removed successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Customer not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting customer: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    // ==================== GETTER/QUERY METHODS ====================
    
    // Method to retrieve data set to be displayed
    public ResultSet getQueryResult() {
        try {
            String query = "SELECT customercode, fullname, location, phone FROM customers ORDER BY fullname ASC";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get customer by code
    public ResultSet getCustomerByCode(String custCode) {
        String query = "SELECT * FROM customers WHERE customercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, custCode);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get customer name by code
    public String getCustomerName(String custCode) {
        String name = null;
        String query = "SELECT fullname FROM customers WHERE customercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, custCode);
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
    
    // Get customer balance
    public double getCustomerBalance(String custCode) {
        double balance = 0.0;
        String query = "SELECT balance FROM customers WHERE customercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, custCode);
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
    
    // Get all customers (simple list for dropdowns)
    public ResultSet getAllCustomers() {
        try {
            String query = "SELECT customercode, fullname FROM customers ORDER BY fullname";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== SEARCH METHODS ====================
    
    // Method to retrieve search data
    public ResultSet getCustomerSearch(String text) {
        try {
            String query = """
                SELECT customercode, fullname, location, phone 
                FROM customers 
                WHERE customercode LIKE ? 
                   OR fullname LIKE ? 
                   OR location LIKE ? 
                   OR phone LIKE ?
                ORDER BY fullname ASC
                """;
            prepStatement = conn.prepareStatement(query);
            String searchPattern = "%" + text + "%";
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
    
    // Search customers with balance filter
    public ResultSet getCustomersByBalance(double minBalance, double maxBalance) {
        try {
            String query = """
                SELECT customercode, fullname, location, phone, debit, credit, balance 
                FROM customers 
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

    // ==================== CUSTOMER NAME METHODS ====================
    
    public ResultSet getCustName(String custCode) {
        String query = "SELECT * FROM customers WHERE customercode = ?";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, custCode);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // Get customer sales history
    public ResultSet getCustomerSalesHistory(String custCode) {
        String query = """
            SELECT salesid, date, productcode, quantity, revenue 
            FROM salesinfo 
            WHERE customercode = ?
            ORDER BY date DESC
            """;
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, custCode);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== PRODUCT NAME METHODS (for compatibility) ====================
    
    public ResultSet getProdName(String prodCode) {
        String query = """
            SELECT products.productname, currentstock.quantity 
            FROM products 
            INNER JOIN currentstock ON products.productcode = currentstock.productcode 
            WHERE currentstock.productcode = ?
            """;
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, prodCode);
            resultSet = prepStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // ==================== UTILITY METHODS ====================
    
    // Method to display data set in tabular form
    public DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            return new DefaultTableModel();
        }
        
        ResultSetMetaData metaData = resultSet.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int colCount = metaData.getColumnCount();

        for (int col = 1; col <= colCount; col++) {
            columnNames.add(metaData.getColumnName(col).toUpperCase(Locale.ROOT));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> vector = new Vector<>();
            for (int col = 1; col <= colCount; col++) {
                Object value = resultSet.getObject(col);
                // Format currency values
                if (metaData.getColumnTypeName(col).toUpperCase().contains("DOUBLE") || 
                    metaData.getColumnTypeName(col).toUpperCase().contains("REAL")) {
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
    
    // Check if customer exists
    public boolean customerExists(String custCode) {
        boolean exists = false;
        String query = "SELECT 1 FROM customers WHERE customercode = ? LIMIT 1";
        try {
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, custCode);
            resultSet = prepStatement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return exists;
    }
    
    // Get total number of customers
    public int getCustomerCount() {
        int count = 0;
        String query = "SELECT COUNT(*) as total FROM customers";
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
    
    // Get total debit/credit summary
    public double[] getFinancialSummary() {
        double[] summary = new double[3]; // [totalDebit, totalCredit, totalBalance]
        String query = "SELECT SUM(debit) as totalDebit, SUM(credit) as totalCredit, SUM(balance) as totalBalance FROM customers";
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